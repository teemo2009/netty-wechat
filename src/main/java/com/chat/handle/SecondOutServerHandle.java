package com.chat.handle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecondOutServerHandle extends ChannelOutboundHandlerAdapter  {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            log.info("SecondOutServerHandle");
            ctx.writeAndFlush(msg);
    }
}
