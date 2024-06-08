package com.sonal.project.firstproject.echoserver;

import com.sonal.project.firstproject.channelhandler.echoserverhandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class echoserver {
    private final int port;

    public echoserver(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
       
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG, 100)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) {
                     ch.pipeline().addLast(new echoserverhandler());
                 }
             });

            
            ChannelFuture f = b.bind(port).sync();
            System.out.println("Server started on port: " + port);

           
            f.channel().closeFuture().sync();
        } finally {
            
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8085;
        new echoserver(port).start();
    }
}