package com.sifan.srpc.loadbalance;

import com.sifan.srpc.register.InterfaceItem;

import java.util.List;

/**
 * 最小连接数平衡
 *
 * @author 思凡
 * @date 2024/06/06
 */
public class MinConnectionBalance implements LoadBalance {
    @Override
    public String balance(List<InterfaceItem> addressList) {
        if (addressList.size() == 0) {
            return null;
        }
        InterfaceItem ans = addressList.get(0);
        for (InterfaceItem interfaceItem : addressList) {
            if (ans.getMeta().containsKey("connection") && interfaceItem.getMeta().containsKey("connection")) {
                if ((Integer) ans.getMeta().get("connection") > (Integer) interfaceItem.getMeta().get("connection")) {
                    ans = interfaceItem;
                }
            }
        }
        return ans.getHost() + ":" + ans.getPort();
    }
}
