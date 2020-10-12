package cn.inps.dsf.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.inps.dsf.codec.ServiceMessageDecoder;
import cn.inps.dsf.codec.ServiceMessageEncoder;
import cn.inps.dsf.common.NettyConstants;
import cn.inps.dsf.handler.HeartBeatRequestHandler;
import cn.inps.dsf.handler.ValidateRequestHandler;
/**
 *
 * @author zhoudl
 * @date 2014年12月19日 下午9:16:23
 * @tags  问题： 如果服务端没有开启，客户端会产生很多个线程， 需要关闭已经启动的线程。
 */
public class NettyServiceClient {
		/**
		 * 
		 * @param host
		 * @param port
		 * @throws Exception
		 */
        public void connect(String host , int port) throws Exception{
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.TCP_NODELAY, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ServiceMessageDecoder(1024*1024,4,4,-8,0));
                    ch.pipeline().addLast("ServiceMessageEncoder", new ServiceMessageEncoder());
                    ch.pipeline().addLast("ReadTimeoutHandler", new ReadTimeoutHandler(50));
                    ch.pipeline().addLast("ValidateRequestHandler", new ValidateRequestHandler());
                    ch.pipeline().addLast("HeartBeatRequestHandler", new HeartBeatRequestHandler());        
                }
            });

            // Start the client.
            //ChannelFuture f = b.connect(new InetSocketAddress(host, port),new InetSocketAddress(NettyConstants.CLIENT_PORT, NettyConstants.CLIENT_PORT)).sync(); // (5)
            ChannelFuture f = b.connect(NettyConstants.CLIENT_HOST, NettyConstants.CLIENT_PORT).sync(); // (5)
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            

         //   executor.shutdown();
            executor.execute(new Runnable() {
				
				public void run() {
					try {
						TimeUnit.SECONDS.sleep(5);
						try {

							connect(NettyConstants.SERVER_HOST,NettyConstants.SERVER_PORT);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			});
            
            
            
            
        }
        }
        /**
         * 
         * @param args
         * @throws Exception
         */
        public static void main(String[] args) throws Exception {
        	new NettyServiceClient().connect(NettyConstants.SERVER_HOST,NettyConstants.SERVER_PORT);
        }
}