package com.chat.entity;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MEncoder extends MessageToByteEncoder<IOBean> {

    @Override
    protected void encode(ChannelHandlerContext ctx, IOBean msg, ByteBuf out) throws Exception {
        if (msg == null) {
            throw new Exception("msg obj is null");
        }
        out.writeByte(msg.getHead());
        out.writeInt(msg.getLength());
        out.writeShort(msg.getVersion());
        out.writeBytes(msg.getData().getBytes("UTF-8"));
    }
}
