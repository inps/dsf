package cn.inps.dsf.handler;

import java.util.concurrent.TimeUnit;

import cn.inps.dsf.common.ServiceMessageType;
import cn.inps.dsf.pojo.ServiceMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;

public class HeartBeatRequestHandler extends ChannelHandlerAdapter {
	
	private volatile ScheduledFuture<?> heartBeat;
	

	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ServiceMessage message = (ServiceMessage)msg;
		if(message.getHeader()!=null && message.getHeader().getType()== ServiceMessageType.LOGIN_RESPONSE){
			heartBeat  = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
		}
		else if(message.getHeader()!=null && message.getHeader().getType()== ServiceMessageType.HEARTBEAT_RESPONSE){
			
			System.out.println("Client receive heart beat message to server: --->"+message);
		}
		else{
			ctx.fireChannelRead(msg);
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		if(heartBeat !=null){
			heartBeat.cancel(true);
			heartBeat=null;
		}
		ctx.fireExceptionCaught(cause);
	}
	
	

}
