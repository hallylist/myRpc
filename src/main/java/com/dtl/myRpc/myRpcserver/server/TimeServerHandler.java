package com.dtl.myRpc.myRpcserver.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf buf;
    int index = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf in=(ByteBuf)msg;
        if(buf==null){
            buf = ctx.alloc().buffer(in.readableBytes());
            in.readBytes(buf);
        }else {
            ByteBuf bufTem=ctx.alloc().buffer(buf.readableBytes()+in.readableBytes());
            buf.readBytes(bufTem,bufTem.writerIndex(),buf.readableBytes());
            in.readBytes(bufTem,bufTem.writerIndex(),in.readableBytes());
            buf.release();
            buf = bufTem;
        }
        while (buf.readableBytes()>12) {
            System.out.println("server receive from client: "+buf.readBytes(12).toString(CharsetUtil.UTF_8));
            System.out.println(index++);
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello client".getBytes()));
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
