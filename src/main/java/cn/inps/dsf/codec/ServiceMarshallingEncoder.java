package cn.inps.dsf.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

public class ServiceMarshallingEncoder extends MarshallingEncoder{

	public ServiceMarshallingEncoder(MarshallerProvider provider) {
		super(provider);
	}

	public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception{
		super.encode(ctx, msg, out);
	}
	
}