package com.sifan.srpc.register;

/**
 * 服务注册工厂
 *
 * @author 思凡
 * @date 2024/05/14
 */
public class ServiceRegisterFactory {
    public final static String ZK = "zk";
    public final static String NACOS = "nacos";
    public final static String CONSUL = "consul";

    /**
     * 根据配置返回默认的服务注册中心实现
     */
    public static ServiceRegister getServiceRegister() {
        return getServiceRegister(getDefaultRegisterType());
    }

    /**
     * 读取默认的注册中心类型，优先级：JVM 属性 > 环境变量 > 默认 nacos
     */
    public static String getDefaultRegisterType() {
        String type = System.getProperty("srpc.registry");
        if (type == null || type.length() == 0) {
            type = System.getenv("SRPC_REGISTRY");
        }
        if (type == null || type.length() == 0) {
            type = NACOS;
        }
        return type.toLowerCase();
    }

    public static ServiceRegister getServiceRegister(String registerType) {
        if (ZK.equalsIgnoreCase(registerType)) {
            return new ZkServiceRegister();
        } else if (NACOS.equalsIgnoreCase(registerType)) {
            return new NacosRegister();
        } else if (CONSUL.equalsIgnoreCase(registerType)) {
            return new ConsulRegister();
        }
        return null;
    }
}
