package cn.inps.dsf.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class ServiceMarshallingDecoder extends MarshallingDecoder{

	public ServiceMarshallingDecoder(UnmarshallerProvider provider) {
		super(provider);
	}

	public ServiceMarshallingDecoder(UnmarshallerProvider provider, int maxObjectSize){
		super(provider, maxObjectSize);
	}
	
	public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		return super.decode(ctx, in);
	}
	
}