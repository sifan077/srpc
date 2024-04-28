package com.sifan.srpc.client;


import com.sifan.srpc.common.RPCRequest;
import com.sifan.srpc.common.RPCResponse;

public interface RPCClient {
    RPCResponse sendRequest(RPCRequest request);
    void close();
}
