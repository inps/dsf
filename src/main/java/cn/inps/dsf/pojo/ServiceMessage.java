package cn.inps.dsf.pojo;

public final class ServiceMessage {
	private ServiceHeader header;
	private Object body;

	
	
	@Override
	public String toString() {
		return "ServiceMessage [header=" + header + ", body=" + body + "]";
	}
	public final ServiceHeader getHeader() {
		return header;
	}
	public final void setHeader(ServiceHeader header) {
		this.header = header;
	}
	public final Object getBody() {
		return body;
	}
	public final void setBody(Object body) {
		this.body = body;
	}

	

}
