package com.dtl.myRpc.myRpcserver.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    private String buf = "";
    private int readIndex = 0;
    int index = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        if ("".equals(buf)) {
            circurRead(ctx, in);
        } else {
            System.out.println("server receive from client: " + buf + in.toString(0, 12-readIndex, CharsetUtil.UTF_8));
            in.readerIndex(12-readIndex);
            System.out.println(index++);
            readIndex = 0;
            buf = "";
            circurRead(ctx, in);
        }
    }

    private void circurRead(ChannelHandlerContext ctx, ByteBuf in) {
        while (in.readableBytes() >= 12) {
            System.out.println("server receive from client: " + in.readBytes(12).toString(CharsetUtil.UTF_8));
            System.out.println(index++);
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello client".getBytes()));
        }
        if (in.readableBytes() > 0) {
            readIndex = in.readableBytes();
            buf += in.toString(CharsetUtil.UTF_8);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
