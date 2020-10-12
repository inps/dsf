package cn.inps.dsf.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import cn.inps.dsf.common.ServiceMessageType;
import cn.inps.dsf.pojo.ServiceHeader;
import cn.inps.dsf.pojo.ServiceMessage;

public class ValidateRequestHandler extends ChannelHandlerAdapter {




	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ServiceMessage message =  buildLoginRequest();
		System.out.println("Client send login message to Server:" + message);
		
		ctx.writeAndFlush(message);
	}


	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ServiceMessage message = (ServiceMessage)msg;
		if(message.getHeader()!=null && message.getHeader().getType()==ServiceMessageType.LOGIN_RESPONSE){
			Byte loginResult = (Byte) message.getBody();
			if(loginResult !=0){
				ctx.close();
			}else{
				System.out.println("Login is ok:" + message);
				ctx.fireChannelRead(msg);
			}
			
			
		}else
			ctx.fireChannelRead(msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		ctx.fireExceptionCaught(cause);
	}
	
	private ServiceMessage buildLoginRequest(){
		ServiceMessage message  =new ServiceMessage();
		ServiceHeader header = new ServiceHeader();
		header.setType(ServiceMessageType.LOGIN_REQUEST);
		message.setHeader(header);
		return message;
	}

	
}
