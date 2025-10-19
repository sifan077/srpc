package com.sifan.srpc.loadbalance;

/**
 * 负载均衡工厂
 */
public class LoadBalanceFactory {

    public static final String ROUND = "round";
    public static final String RANDOM = "random";
    public static final String WEIGHT = "weight";
    public static final String MIN_CONN = "min-conn";

    /**
     * 获取默认负载均衡器，优先级：JVM 属性 > 环境变量 > 默认 round
     * 属性名：srpc.loadbalance 或环境变量 SRPC_LOADBALANCE
     */
    public static LoadBalance getLoadBalance() {
        String type = System.getProperty("srpc.loadbalance");
        if (type == null || type.length() == 0) {
            type = System.getenv("SRPC_LOADBALANCE");
        }
        if (type == null || type.length() == 0) {
            type = ROUND;
        }
        return getLoadBalance(type);
    }

    public static LoadBalance getLoadBalance(String type) {
        if (type == null) return new RoundLoadBalance();
        String t = type.trim().toLowerCase();
        switch (t) {
            case RANDOM:
                return new RandomLoadBalance();
            case WEIGHT:
                return new WeightBalance();
            case MIN_CONN:
            case "minconn":
            case "min_connection":
                return new MinConnectionBalance();
            case ROUND:
            default:
                return new RoundLoadBalance();
        }
    }
}
