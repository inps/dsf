package cn.inps.dsf.common;

public  interface ServiceMessageType {
	
	public static final byte LOGIN_REQUEST = 1;
	public static final byte LOGIN_RESPONSE = 2;
	public static final byte HEARTBEAT_REQUEST = 3;
	public static final byte HEARTBEAT_RESPONSE = 4;
}
