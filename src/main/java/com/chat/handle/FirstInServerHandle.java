package com.chat.handle;

import com.chat.entity.IOBean;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author:luqi
 * @description: 第一个处理器
 * @date:2018/9/28_14:02
 */
@Slf4j
public class FirstInServerHandle extends  SimpleChannelInboundHandler<IOBean> {

    AtomicInteger atomicInteger=new AtomicInteger(0);

  /*  @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in= (ByteBuf) msg;
        //获取长度
        int len=in.readInt();
        //获取版本
        short version=in.readShort();
        ByteBuf array= in.readBytes(in.readableBytes());
        byte [] dataBytes=new byte[len];
        array.readBytes(dataBytes);
        String str=new String(dataBytes,"UTF-8");

    }*/

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IOBean msg) throws Exception {
        log.info("{}==========={}",msg.getData(),atomicInteger.incrementAndGet());
    }
}
