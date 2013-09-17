package net.tmt;

public interface Constants {
	public static final int		WIDTH					= 1024;
	public static final int		HEIGHT					= 640;
	public static final int		SERVER_PORT_TCP			= 54555;
	public static final int		SERVER_PORT_UDP			= 54777;
	public static final String	SERVER_IP				= "127.0.0.1";
	public static final int		DELTA_TARGET			= 10;


	public static final int		SERVER_ID				= 0;
	public static final int		CLIENT_ID_UNREGISTERED	= Integer.MIN_VALUE;
	public static final int		SERVER_UPDATE_DELTA		= 100;
	public static final int		CLIENT_UPDATE_DELTA		= 100;
}
