package com.sifan.srpc.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.sifan.srpc.common.RPCRequest;
import com.sifan.srpc.common.RPCResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoSerializer implements Serializer {
    /**
     * kryo 线程不安全，使用 ThreadLocal 保证每个线程只有一个 Kryo
     */
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // 设置动态动态序列化和反序列化类，不提前注册所有类（可能有安全问题）
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        KRYO_THREAD_LOCAL.get().writeObject(output, obj);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        Object result = null;
        switch (messageType) {
            case 0: {
                result = KRYO_THREAD_LOCAL.get().readObject(input, RPCRequest.class);
                break;
            }
            case 1: {
                result = KRYO_THREAD_LOCAL.get().readObject(input, RPCResponse.class);
                break;
            }
        }
        input.close();
        return result;
    }

    @Override
    public int getType() {
        return 3;
    }
}