package com.sifan.srpc.codec;


public interface Serializer {
    public final static int OBJECT_SERIALIZER = 0;
    public final static int JSON_SERIALIZER = 1;
    public final static int HESSIAN_SERIALIZER = 2;
    public final static int KRYO_SERIALIZER = 3;
    public final static int PROTOSTUFF_SERIALIZER = 4;

    // 根据序号取出序列化器，暂时有两种实现方式，需要其它方式，实现这个接口即可
    static Serializer getSerializerByCode(int code) {
        switch (code) {
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new HessianSerializer();
            case 3:
                return new KryoSerializer();
            default:
                return null;
        }
    }

    // 根据名称获取序列化器（java/object/jdk、json、hessian、kryo）
    static Serializer getSerializerByName(String name) {
        if (name == null) return null;
        String n = name.trim().toLowerCase();
        if ("java".equals(n) || "object".equals(n) || "jdk".equals(n)) {
            return new ObjectSerializer();
        }
        if ("json".equals(n) || "fastjson".equals(n)) {
            return new JsonSerializer();
        }
        if ("hessian".equals(n)) {
            return new HessianSerializer();
        }
        if ("kryo".equals(n)) {
            return new KryoSerializer();
        }
        return null;
    }

    // 默认序列化器：优先 JVM 属性 srpc.serializer > 环境变量 SRPC_SERIALIZER > 默认 kryo
    static Serializer getDefaultSerializer() {
        String name = System.getProperty("srpc.serializer");
        if (name == null || name.length() == 0) {
            name = System.getenv("SRPC_SERIALIZER");
        }
        Serializer s = getSerializerByName(name);
        if (s == null) {
            s = getSerializerByCode(KRYO_SERIALIZER);
        }
        return s;
    }

    // 把对象序列化成字节数组
    byte[] serialize(Object obj);

    // 从字节数组反序列化成消息, 使用java自带序列化方式不用messageType也能得到相应的对象（序列化字节数组里包含类信息）
    // 其它方式需指定消息格式，再根据message转化成相应的对象
    Object deserialize(byte[] bytes, int messageType);

    // 返回使用的序列器，是哪个
    // 0：java自带序列化方式, 1: json序列化方式
    int getType();
}
