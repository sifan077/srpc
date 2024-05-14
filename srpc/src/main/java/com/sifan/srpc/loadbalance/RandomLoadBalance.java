package com.sifan.srpc.loadbalance;

import com.sifan.srpc.register.InterfaceItem;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡
 */
public class RandomLoadBalance implements LoadBalance {
    @Override
    public String balance(List<InterfaceItem> addressList) {

        Random random = new Random();
        int choose = random.nextInt(addressList.size());
        System.out.println("负载均衡选择了" + choose + "服务器");
        InterfaceItem interfaceItem = addressList.get(choose);

        return interfaceItem.getHost() + ":" + interfaceItem.getPort();
    }
}
