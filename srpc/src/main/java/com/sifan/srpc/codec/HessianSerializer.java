package com.sifan.srpc.codec;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
//        System.out.println("使用HessianSerializer序列化");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Hessian2Output hessianOutput = new Hessian2Output(outputStream);
        try {
            hessianOutput.writeObject(obj);
            hessianOutput.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                hessianOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
//        System.out.println("使用HessianSerializer反序列化");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Hessian2Input hessianInput = new Hessian2Input(inputStream);
        try {
            return hessianInput.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                hessianInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getType() {
        return 2; // Assuming 2 represents Hessian serialization
    }
}