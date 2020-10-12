package cn.inps.dsf.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.inps.dsf.common.ServiceMessageType;
import cn.inps.dsf.pojo.ServiceHeader;
import cn.inps.dsf.pojo.ServiceMessage;

public class ValidateResponseHandler extends ChannelHandlerAdapter {
	private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();
	private String[] whiteList = { "127.0.0.1", "localhost" };


	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ServiceMessage message = (ServiceMessage) msg;
		if (message.getHeader() != null
				&& message.getHeader().getType() == ServiceMessageType.LOGIN_REQUEST) {
			String nodeIndex = ctx.channel().remoteAddress().toString();
			ServiceMessage loginResponse = null;
			if (nodeCheck.containsKey(nodeIndex)) {
				loginResponse = buildLoginResponse((byte) -1);
			} else {
				InetSocketAddress address = (InetSocketAddress) ctx.channel()
						.remoteAddress();
				String ip = address.getAddress().getHostAddress();
				boolean isOK = false;
				for (String WIP : whiteList) {
					if (WIP.equals(ip)) {
						isOK = true;
						break;
					}
				}
				loginResponse = isOK ? buildLoginResponse((byte) 0)
						: buildLoginResponse((byte) -1);
				if (isOK) {
					nodeCheck.put(nodeIndex, true);
				}
				System.out.println("The Login response is :" + loginResponse);
				ctx.writeAndFlush(loginResponse);

			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		nodeCheck.remove(ctx.channel().remoteAddress().toString());
		System.out.println("nodeCheck:"+nodeCheck.toString());
		ctx.close();
		ctx.fireExceptionCaught(cause);
	}

	private ServiceMessage buildLoginResponse(byte result) {
		ServiceMessage message = new ServiceMessage();
		ServiceHeader header = new ServiceHeader();
		header.setType(ServiceMessageType.LOGIN_RESPONSE);
		message.setHeader(header);
		message.setBody(result);
		return message;
	}

}
