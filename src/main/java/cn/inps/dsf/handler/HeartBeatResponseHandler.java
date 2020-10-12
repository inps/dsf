package cn.inps.dsf.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import cn.inps.dsf.common.ServiceMessageType;
import cn.inps.dsf.pojo.ServiceHeader;
import cn.inps.dsf.pojo.ServiceMessage;

public class HeartBeatResponseHandler extends ChannelHandlerAdapter {


	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ServiceMessage message = (ServiceMessage)msg;
		if(message.getHeader()!=null && message.getHeader().getType()== ServiceMessageType.HEARTBEAT_REQUEST){
			System.out.println("Receive client heart beat message: --->"+message);
			ServiceMessage heartBeat = buildHeartBeat();
			System.out.println("Send heart beat response message to client: --->"+heartBeat);
			ctx.writeAndFlush(heartBeat);		
		}
		else{
			ctx.fireChannelRead(msg);
		}
		

	}
	private ServiceMessage buildHeartBeat(){
		ServiceMessage message  =new ServiceMessage();
		ServiceHeader header = new ServiceHeader();
		header.setType(ServiceMessageType.HEARTBEAT_RESPONSE);
		message.setHeader(header);
		return message;
	}
	
	

}
