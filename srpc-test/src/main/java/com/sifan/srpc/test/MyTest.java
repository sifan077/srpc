package com.sifan.srpc.test;

import com.alibaba.fastjson2.JSON;
import com.sifan.srpc.codec.Serializer;
import com.sifan.srpc.test.common.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MyTest {
    public static void main(String[] args) {
        List<User> list = new ArrayList<>();
        Serializer jsonSerializer = Serializer.getSerializerByCode(Serializer.JSON_SERIALIZER);
        Serializer objectSerializer = Serializer.getSerializerByCode(Serializer.OBJECT_SERIALIZER);
        Serializer hessianSerializer = Serializer.getSerializerByCode(Serializer.HESSIAN_SERIALIZER);
        Serializer kryoSerializer = Serializer.getSerializerByCode(Serializer.KRYO_SERIALIZER);
        List<Serializer> serializers = Arrays.asList(jsonSerializer, objectSerializer, hessianSerializer, kryoSerializer);
        List<ArrayList<Long>> timeLists = Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        List<ArrayList<Integer>> sizeLists = Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        for (int i = 0; i < 100000; i++) {
            User user = User.builder().id(i).userName("sifan" + i).sex(i % 2 == 0).build();
            list.add(user);
            if (i % 10000 == 0)
                for (int j = 0; j < serializers.size(); j++) {
                    Serializer serializer = serializers.get(j);
                    long start = System.currentTimeMillis();
                    byte[] bytes = serializer.serialize(list);
                    long end = System.currentTimeMillis();
                    timeLists.get(j).add(end - start);
                    sizeLists.get(j).add(bytes.length);
                }
        }
        // json化数据
        Map<String, List> map = new HashMap<>();
        map.put("json", Arrays.asList(timeLists.get(0), sizeLists.get(0)));
        map.put("object", Arrays.asList(timeLists.get(1), sizeLists.get(1)));
        map.put("hessian", Arrays.asList(timeLists.get(2), sizeLists.get(2)));
        map.put("kryo", Arrays.asList(timeLists.get(3), sizeLists.get(3)));

        // 将map json写入到文件中
        File f = new File("D:/data.txt");
        try {
            f.createNewFile();
            FileWriter fw = new FileWriter(f);
            fw.write(JSON.toJSONString(map));
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
