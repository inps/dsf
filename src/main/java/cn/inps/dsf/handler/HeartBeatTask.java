package cn.inps.dsf.handler;

import io.netty.channel.ChannelHandlerContext;


import cn.inps.dsf.common.ServiceMessageType;
import cn.inps.dsf.pojo.ServiceHeader;
import cn.inps.dsf.pojo.ServiceMessage;

public   class HeartBeatTask implements Runnable {
	
	private final ChannelHandlerContext ctx;
	

	public HeartBeatTask(ChannelHandlerContext ctx) {
		//super();
		this.ctx = ctx;
	}


	public void run() {
		// TODO Auto-generated method stub
		ServiceMessage  heartBeat = buildHeartBeat();
		System.out.println("Client send heart beat message to server: --->"+heartBeat);
		ctx.writeAndFlush(heartBeat);
	}
	
	
	private ServiceMessage buildHeartBeat(){
		ServiceMessage message  =new ServiceMessage();
		ServiceHeader header = new ServiceHeader();
		header.setType(ServiceMessageType.HEARTBEAT_REQUEST);
		message.setHeader(header);
		return message;
	}
	

}
