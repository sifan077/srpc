package com.sifan.srpc.register;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.sifan.srpc.loadbalance.LoadBalance;
import com.sifan.srpc.loadbalance.LoadBalanceFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class NacosRegister implements ServiceRegister {

    private final String ROOT_PATH = "SRPC";
    //  提供的nacos客户端
    private NamingService namingService;
    // 初始化负载均衡器（可通过配置切换，默认轮询）
    private LoadBalance loadBalance = LoadBalanceFactory.getLoadBalance();

    public NacosRegister() {
        try {
            Properties properties = new Properties();
            // 读取 Nacos 地址，优先级：JVM 属性 > 环境变量 > 默认
            String addr = System.getProperty(PropertyKeyConst.SERVER_ADDR);
            if (addr == null || addr.length() == 0) {
                addr = System.getProperty("srpc.nacos.addr");
            }
            if (addr == null || addr.length() == 0) {
                addr = System.getenv("SRPC_NACOS_ADDR");
            }
            if (addr == null || addr.length() == 0) {
                addr = "127.0.0.1:8848";
            }
            properties.setProperty(PropertyKeyConst.SERVER_ADDR, addr);
            this.namingService = NamingFactory.createNamingService(properties);
        } catch (NacosException e) {
            System.out.println("nacos 初始化失败");
            System.exit(1);
            e.printStackTrace();
        }
    }

    @Override
    public void register(String serviceName, InetSocketAddress serverAddress) {
        try {
            namingService.registerInstance(ROOT_PATH, serviceName,
                    serverAddress.getHostName(),
                    serverAddress.getPort());
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            List<Instance> allInstances = namingService.getAllInstances(ROOT_PATH, serviceName);
            List<InterfaceItem> interfaceItems = allInstances
                    .stream()
                    .map((item) -> InterfaceItem.builder()
                            .host(item.getIp())
                            .port(item.getPort())
                            .weight(item.getWeight())
                            .build()).collect(Collectors.toList());
            String ip = loadBalance.balance(interfaceItems);
            return new InetSocketAddress(ip.split(":")[0], Integer.parseInt(ip.split(":")[1]));
        } catch (NacosException e) {
            System.out.println("nacos 获取服务失败");
            System.exit(1);
            e.printStackTrace();
        }
        return null;
    }
}
