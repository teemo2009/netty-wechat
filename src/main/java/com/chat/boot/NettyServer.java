package com.chat.boot;

import com.chat.entity.MDecoder;
import com.chat.entity.MEncoder;
import com.chat.handle.FirstInServerHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup(10);
        serverBootstrap
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new MDecoder(Integer.MAX_VALUE,1,4,
                                2,1));
                        ch.pipeline().addLast(new MEncoder());
                        ch.pipeline().addLast(new FirstInServerHandle());
                       /* ch.pipeline().addLast(new SecondInServerHandle());
                        ch.pipeline().addLast(new FirstOutServerHandle());
                        ch.pipeline().addLast(new SecondOutServerHandle());
                        ch.pipeline().addLast(new ThreeInServerHandle());*/

                    }
                });
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(8020).sync();
            channelFuture.addListener((ChannelFutureListener) future -> log.info("8020"));
        } catch (InterruptedException e) {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
