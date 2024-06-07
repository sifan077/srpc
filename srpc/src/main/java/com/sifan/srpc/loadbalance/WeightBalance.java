package com.sifan.srpc.loadbalance;

import com.sifan.srpc.register.InterfaceItem;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WeightBalance implements LoadBalance {

    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public synchronized String balance(List<InterfaceItem> addressList) {
        if (addressList == null || addressList.isEmpty()) {
            return null;
        }

        int totalWeight = 0;
        for (InterfaceItem address : addressList) {
            totalWeight += address.getWeight();
        }
        final int tempTotalWeight = totalWeight;
        int index = currentIndex.getAndUpdate(i -> (i + 1) % tempTotalWeight);

        for (InterfaceItem address : addressList) {
            index -= address.getWeight();
            if (index < 0) {
                return address.getHost() + ":" + address.getPort();
            }
        }

        // Fallback
        return addressList.get(0).getHost() + ":" + addressList.get(0).getPort();
    }
}
