package hello;

import io.netty.channel.Channel;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.HttpContentEncoder.Result;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class ServerInfo {
	private static Hashtable<String, Date> requestsDate = new Hashtable<String, Date>();
	private static Hashtable<String, Integer> requestsCount = new Hashtable<String, Integer>();
	private static Hashtable<String, Integer> urlRequests = new Hashtable<String,Integer>();
	private static DefaultChannelGroup channelGroup = new DefaultChannelGroup(null);
    private static Vector<ConnectionInfo> connectionsInfo = new Vector<ConnectionInfo>();
    private static Hashtable<Channel,String> channelsURL = new Hashtable<Channel, String>();

    
    public static String getURLByChannel(Channel channel){
    	return channelsURL.get(channel);
    }
	
    public static void setURLToChannel(Channel channel, String url){
    	channelsURL.put(channel, url);
    }
    
    public static  Vector<ConnectionInfo> getConnectionsInfo(){
    	Vector<ConnectionInfo> result = new Vector<ConnectionInfo>();
    	result.addAll(connectionsInfo);
    	return result;
    } 
    
    public static ConnectionInfo findConnectionInfoByChannel(Channel channel){
    	for (ConnectionInfo element : connectionsInfo) {
			if(element.isThisChannel(channel))
				return element;
		}
    	return null;
    } 
    
    public static void addNewConnectionInfo(ConnectionInfo connectionInfo) {
		connectionsInfo.add(connectionInfo);
		while(connectionsInfo.size()>16){
			connectionsInfo.remove(0);
		}
	}
    
	public static synchronized void addConnection(Channel channel ){
		channelGroup.add(channel);
	}
	
	public static synchronized int getConnectionsCount(){
		int i =  channelGroup.size();
		return i;
	}
	
	
	public static void addURLRequest(String url){
		if(urlRequests.containsKey(url))
			urlRequests.replace(url, urlRequests.get(url)+1);
		else
			urlRequests.put(url, 1);
	}
	
	public static void addRequest(String IP, Date date){
		if(requestsDate.containsKey(IP))
			requestsCount.replace(IP, requestsCount.get(IP)+1);
		else
			requestsCount.put(IP, 1);
		requestsDate.put(IP, date);
	}
	
	public static int getRequestsCountWithUniqueIP(){
		return requestsDate.keySet().size();
	} 
	
	public static int getRequestsCount(){
		Enumeration<String> keys = requestsCount.keys();
		int result = 0;
		while(keys.hasMoreElements())
			result += requestsCount.get(keys.nextElement());
		return result;
	} 
	
	public static String[] getRequestInformationList(){
		String[]  result = new String[requestsDate.size()*3];
		Enumeration<String> ips = requestsDate.keys();
		for (int i = 0; i < requestsDate.size()*3; i+=3) {
			String ip = ips.nextElement();  
			result[i] = ip;
			result[i+1] = requestsDate.get(ip).toString() ;
			result[i+2] = requestsCount.get(ip).toString();
		}
		return result;
	}
	
	public static String[] getRedirectRequestsInformation(){
		String[]  result = new String[urlRequests.size()*2];
		Enumeration<String> urls = urlRequests.keys();
		for (int i = 0; i < urlRequests.size()*2; i+=2) {
			String url = urls.nextElement();  
			result[i] = url;
			result[i+1] = urlRequests.get(url).toString() ;
		}
		return result;
	}
}

	
	
	

