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

    public static ServiceRegister getServiceRegister(String registerType) {
        if ("zk".equals(registerType)) {
            return new ZkServiceRegister();
        } else if ("nacos".equals(registerType)) {
            return new NacosRegister();
        } else if ("consul".equals(registerType)) {
            return new ConsulRegister();
        }
        return null;
    }
}
