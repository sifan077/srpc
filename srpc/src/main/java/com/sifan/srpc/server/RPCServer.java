package com.sifan.srpc.server;

public interface RPCServer {
    void start(int port);

    void stop();
}
