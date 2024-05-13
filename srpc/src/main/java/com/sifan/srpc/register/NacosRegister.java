package com.sifan.srpc.register;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.sifan.srpc.loadbalance.LoadBalance;
import com.sifan.srpc.loadbalance.RoundLoadBalance;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

public class NacosRegister implements ServiceRegister {

    private final String ROOT_PATH = "SRPC";
    //  提供的nacos客户端
    private NamingService namingService;
    // 初始化负载均衡器， 这里用的是随机， 一般通过构造函数传入
//    private LoadBalance loadBalance = new RandomLoadBalance();
    private LoadBalance loadBalance = new RoundLoadBalance();

    public NacosRegister() {
        try {
            this.namingService = NamingFactory.createNamingService("127.0.0.1:8848");

        } catch (NacosException e) {
            System.out.println("nacos 初始化失败");
            System.exit(1);
            e.printStackTrace();
        }
    }

    @Override
    public void register(String serviceName, InetSocketAddress serverAddress) {
        try {
            namingService.registerInstance(ROOT_PATH, serviceName, serverAddress.getHostName(), serverAddress.getPort());
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            List<Instance> allInstances = namingService.getAllInstances(ROOT_PATH, serviceName);
            List<String> allHosts = allInstances.stream().map((item) -> item.getIp() + ":" + item.getPort()).collect(Collectors.toList());
            String ip = loadBalance.balance(allHosts);

            return new InetSocketAddress(ip.split(":")[0], Integer.parseInt(ip.split(":")[1]));
        } catch (NacosException e) {
            System.out.println("nacos 获取服务失败");
            System.exit(1);
            e.printStackTrace();
        }
        return null;
    }
}
