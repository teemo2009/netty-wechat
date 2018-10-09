package com.chat.entity;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MDecoder extends LengthFieldBasedFrameDecoder {
    private final int HEAD_SIZE=6;

    public MDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

   @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        in= (ByteBuf) super.decode(ctx,in);
        if (in==null){
            return null;
        }
        if(in.readableBytes()<HEAD_SIZE){
            return null;
        }
        //获取长度
        int len=in.readInt();
        //获取版本
        short version=in.readShort();
        if(in.readableBytes()!=len){
            return null;
        }
        ByteBuf array= in.readBytes(in.readableBytes());
        byte [] dataBytes=new byte[len];
        array.readBytes(dataBytes);
        IOBean ioBean=new IOBean(len,version,new String(dataBytes,"UTF-8"));
        return ioBean;
    }

    /*
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        if(in.readableBytes()<HEAD_SIZE){
            in.skipBytes(in.readableBytes());
            return;
        }
        //跳过头
         in.skipBytes(1);
        //获取长度
        int len=in.readInt();
        //获取版本
        short version=in.readShort();
        //判断长度和剩余可读是否相同，不相同丢包
        if (len!=in.readableBytes()){
            in.skipBytes(in.readableBytes());
            return;
        }
        log.info("len===={}",in.readableBytes());
        ByteBuf array= in.readBytes(in.readableBytes());
        byte [] dataBytes=new byte[len];
        array.readBytes(dataBytes);
        IOBean ioBean=new IOBean(len,version,new String(dataBytes,"UTF-8"));
        out.add(ioBean);
    }*/
}
