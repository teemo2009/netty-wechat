package com.chat.boot;

import com.chat.entity.IOBean;
import com.chat.entity.MDecoder;
import com.chat.entity.MEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("127.0.0.1", 8020))
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ch.pipeline().addLast(new MDecoder(Integer.MAX_VALUE,1,4,
                                    2,1));
                            ch.pipeline().addLast(new MEncoder());
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<IOBean>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, IOBean msg) throws Exception {

                                }

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    short version=1;
                                    String data="hello world!大司马之里皮大司马之里皮大司马之里皮大司马之里皮";
                                    for (int i = 0; i < 1000; i++) {
                                        IOBean bean=new IOBean(data.getBytes("UTF-8").length,version,data);
                                        ctx.writeAndFlush(bean);
                                    }
                                }


                            });


                        }
                    });
            // 链接服务器
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }


    }
}