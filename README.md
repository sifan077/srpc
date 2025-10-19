# 基于 Netty 的 RPC 框架

借鉴了 he2121 的项目：https://github.com/he2121/MyRPCFromZero
在此基础上进行了改造并集成 Spring Boot Starter，提供注解化、自动装配的使用体验。

## 模块总览

- srpc：核心 RPC 框架（客户端/服务端、编解码、序列化、注册中心、负载均衡等）
- springboot-starter-srpc：Spring Boot Starter 集成（@EnableRpc、@RpcService、@RpcReference 等）
- srpc-test：纯 Java/Netty 示例与压测
- srpc-provider：示例 Provider 应用（Spring Boot）
- srpc-consumer：示例 Consumer 应用（Spring Boot）

更多详细说明请查看：docs/项目结构说明.md

## 快速开始（示例）

1) 启动注册中心（默认使用 Nacos，地址 127.0.0.1:8848；也支持 Zookeeper/Consul）
2) 在工程根目录执行：
   mvn clean install -DskipTests
3) 启动示例 Provider 应用：com.example.sprcprovider.SrpcProviderApplication（@EnableRpc(needServer = true, port = 9999)）
4) 启动示例 Consumer 应用：com.example.srpcconsumer.SrpcConsumerApplication（@EnableRpc(needServer = false)）
5) 访问 Consumer 示例接口：GET http://localhost:8080/app/hello
