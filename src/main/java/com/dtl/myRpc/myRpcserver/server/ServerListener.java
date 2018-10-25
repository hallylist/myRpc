package com.dtl.myRpc.myRpcserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerListener.class);
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ServerBootstrap serverBootstrap;
    @Value("${netty.port}")
    public int port;

    public ServerListener(@Qualifier("boss") EventLoopGroup boss, @Qualifier("worker") EventLoopGroup worker, ServerBootstrap serverBootstrap) {
        this.boss = boss;
        this.worker = worker;
        this.serverBootstrap = serverBootstrap;
    }

    /**
     * 关闭netty服务
     */
    public void close() {
        LOGGER.info("netty server close ...");
        this.boss.shutdownGracefully();
        this.worker.shutdownGracefully();
    }

    /**
     * 启动netty-server服务
     */
    public void start() {
        LOGGER.info("netty server start ...");
        this.serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128);
        try {
            //设置事件处理
            this.serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch){
                    ch.pipeline().addLast(new TimeServerHandler());
                }
            });
            LOGGER.info("netty is listening on ", port);
            ChannelFuture f = this.serverBootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.info("release source");
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
