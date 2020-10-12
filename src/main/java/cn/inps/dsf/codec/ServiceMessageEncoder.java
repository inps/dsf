package cn.inps.dsf.codec;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

import java.util.List;
import java.util.Map;

import cn.inps.dsf.pojo.ServiceMessage;

public class ServiceMessageEncoder extends MessageToMessageEncoder<ServiceMessage>{

	private ServiceMarshallingEncoder marshallingEncoder;
	
	public ServiceMessageEncoder(){
		marshallingEncoder = MarshallingCodecFactory.buildMarshallingEncoder();
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, ServiceMessage msg,
			List<Object> out) throws Exception {
		if(msg == null || msg.getHeader() == null){
			throw new Exception("The encode message is null");
		}
		
		ByteBuf sendBuf = Unpooled.buffer();
		sendBuf.writeInt(msg.getHeader().getCrcCode());
		sendBuf.writeInt(msg.getHeader().getLength());
		sendBuf.writeLong(msg.getHeader().getSessionID());
		sendBuf.writeByte(msg.getHeader().getType());
		sendBuf.writeByte(msg.getHeader().getPriority());
		sendBuf.writeInt(msg.getHeader().getAttachment().size());
		
		String key = null;
		byte[] keyArray = null;
		Object value = null;
		for(Map.Entry<String, Object> param: msg.getHeader().getAttachment().entrySet()){
			key = param.getKey();
			keyArray = key.getBytes("UTF-8");
			sendBuf.writeInt(keyArray.length);
			sendBuf.writeBytes(keyArray);
			value = param.getValue();
			marshallingEncoder.encode(ctx, value, sendBuf);
		}
		key = null;
		keyArray = null;
		value = null;
		if(msg.getBody() != null){
			marshallingEncoder.encode(ctx, msg.getBody(), sendBuf);
		}
		
//		sendBuf.writeInt(0);
		// �ڵ�4���ֽڳ�д��Buffer�ĳ���
		int readableBytes = sendBuf.readableBytes();
		sendBuf.setInt(4, readableBytes);
		
		// ��Message��ӵ�List���ݵ���һ��Handler 
		out.add(sendBuf);
	}

}
