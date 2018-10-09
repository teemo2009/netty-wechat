package com.chat.entity;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MDecoder extends ByteToMessageDecoder {

    //private final int HEAD_SIZE=6;

   /* public MDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }*/

 /*  @Override
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
    }*/


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int beginIndex=in.readerIndex();
        if(in.readableBytes()<7){
            return;
        }
        //跳过头
         in.skipBytes(1);
        //获取长度
        int len=in.readInt();
        //获取版本
        short version=in.readShort();
        //判断长度和剩余可读是否相同，重置包
        int readableBytes =in.readableBytes();
        if (readableBytes<len){
            in.readerIndex(beginIndex);
            return;
        }
        int t=beginIndex+7+len;
        //长度全部到达开始设置位不可读
        in.readerIndex(t);
        ByteBuf otherByteBufRef=in.slice(beginIndex+7,len);
        //计量加1
        otherByteBufRef.retain();
        ByteBuf array= otherByteBufRef.readBytes(otherByteBufRef.readableBytes());
        byte [] dataBytes=new byte[len];
        array.readBytes(dataBytes);
        IOBean ioBean=new IOBean(len,version,new String(dataBytes,"UTF-8"));
        out.add(ioBean);
        otherByteBufRef.release();
        array.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }
}
