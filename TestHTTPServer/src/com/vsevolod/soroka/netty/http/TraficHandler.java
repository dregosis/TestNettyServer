package com.vsevolod.soroka.netty.http;

import java.util.Date;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;

public class TraficHandler extends ChannelTrafficShapingHandler {
	 private ConnectionInfo connectionInfo; 
	 private long speed;
	 
	 public TraficHandler(long checkInterval) {
		super(checkInterval);
	}
	 


@Override
public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	speed = getReadSpeed();
	super.channelReadComplete(ctx);
}
	
	
	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise future)	throws Exception {
		connectionInfo = new ConnectionInfo(ctx.channel());
		connectionInfo.setTimestamp(new Date());
		if(speed == 0)
			speed = getReadSpeed();
		connectionInfo.setSpeed(speed);
	    connectionInfo.setReceived_bytes(trafficCounter.cumulativeReadBytes());
	    connectionInfo.setSent_bytes( trafficCounter.cumulativeWrittenBytes());
	    String address = ctx.channel().remoteAddress().toString();
		connectionInfo.setSrc_ip(address.substring(1,address.lastIndexOf(":")));
		connectionInfo.setURI(ServerInfo.getURLByChannel(ctx.channel()));
		if(connectionInfo.getURI()!=null /*&& !connectionInfo.getURI().equals("/favicon.ico")*/)
			ServerInfo.addNewConnectionInfo(connectionInfo);
		super.close(ctx, future);
	}
	
	private long getReadSpeed(){
		TrafficCounter trafficCounter =  this.trafficCounter();
	    long time = System.currentTimeMillis() - trafficCounter.lastTime();
		if(time != 0){
			return trafficCounter.currentReadBytes()*1000/time;
		}
		else
			return trafficCounter.currentReadBytes()*1000;
	}

}
