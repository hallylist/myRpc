package com.dtl.myRpc.myRpcserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfiguration {
    @Bean
    public ServerBootstrap serverBootstrapInstance(){
        return new ServerBootstrap();
    }
    @Bean("boss")
    public EventLoopGroup bossEventLoopGroupInstance(){
        return new NioEventLoopGroup();
    }
    @Bean("worker")
    public EventLoopGroup workerEventLoopGroupInstance(){
        return new NioEventLoopGroup();
    }

}
