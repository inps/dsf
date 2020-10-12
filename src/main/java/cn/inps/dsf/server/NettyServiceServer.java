package cn.inps.dsf.server;

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
import io.netty.handler.timeout.ReadTimeoutHandler;
import cn.inps.dsf.codec.ServiceMessageDecoder;
import cn.inps.dsf.codec.ServiceMessageEncoder;
import cn.inps.dsf.common.NettyConstants;
import cn.inps.dsf.handler.HeartBeatResponseHandler;
import cn.inps.dsf.handler.ValidateResponseHandler;


public class NettyServiceServer {



    public void bind() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) // (3)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(new ServiceMessageDecoder(1024*1024,4,4,-8,0));
                     ch.pipeline().addLast("ServiceMessageEncoder", new ServiceMessageEncoder());
                     ch.pipeline().addLast("ReadTimeoutHandler", new ReadTimeoutHandler(50));
                     ch.pipeline().addLast("ValidateResponseHandler", new ValidateResponseHandler());
                     ch.pipeline().addLast("HeartBeatResponseHandler", new HeartBeatResponseHandler()); 
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128);        // (5)
             //.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(NettyConstants.SERVER_HOST, NettyConstants.SERVER_PORT).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        new NettyServiceServer().bind();
    }
}