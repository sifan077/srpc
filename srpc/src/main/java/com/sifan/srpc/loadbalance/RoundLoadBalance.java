package com.sifan.srpc.loadbalance;

import com.sifan.srpc.register.InterfaceItem;

import java.util.List;

/**
 * 轮询负载均衡
 */
public class RoundLoadBalance implements LoadBalance {
    private int choose = -1;

    @Override
    public String balance(List<InterfaceItem> addressList) {
        choose++;
        if (addressList.size() == 0) {
            return null;
        }
        choose = choose % addressList.size();
        InterfaceItem interfaceItem = addressList.get(choose);
        return interfaceItem.getHost() + ":" + interfaceItem.getPort();
    }
}
