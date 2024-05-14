package com.sifan.srpc.register;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.health.ServiceHealth;
import com.sifan.srpc.loadbalance.LoadBalance;
import com.sifan.srpc.loadbalance.RoundLoadBalance;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

public class ConsulRegister implements ServiceRegister {

    private final String ROOT_PATH = "SRPC";
    //  提供的nacos客户端
    private Consul consul = Consul.builder().withHostAndPort(HostAndPort.fromString("127.0.0.1:8500")).build();
    // 初始化负载均衡器， 这里用的是随机， 一般通过构造函数传入
//    private LoadBalance loadBalance = new RandomLoadBalance();
    private LoadBalance loadBalance = new RoundLoadBalance();

    @Override
    public void register(String serviceName, InetSocketAddress serverAddress) {
        AgentClient agent = consul.agentClient();
        ImmutableRegistration.Builder builder = ImmutableRegistration.builder();
        ImmutableRegCheck check = ImmutableRegCheck.builder()
                .tcp(serverAddress.getHostName() + ":" + serverAddress.getPort())
                .interval("5s").build();
        ImmutableRegistration registration = builder.id(ROOT_PATH + serviceName + serverAddress.getPort())
                .name(serviceName).addTags("v1")
                .address(serverAddress.getHostName())
                .port(serverAddress.getPort())
                .check(check)
                .build();
        agent.register(registration);
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        HealthClient healthClient = consul.healthClient();
        List<ServiceHealth> allServiceInstances = healthClient.getAllServiceInstances(serviceName).getResponse();
        List<InterfaceItem> interfaceItems = allServiceInstances.stream().map(item -> InterfaceItem.builder().host(item.getService().getAddress())
                .port(item.getService().getPort())
                .build()).collect(Collectors.toList());
        String balance = loadBalance.balance(interfaceItems);
        if (balance != null) {
            String[] split = balance.split(":");
            return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
        }
        return null;
    }
}
