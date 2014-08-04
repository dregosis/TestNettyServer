package hello;

import io.netty.channel.Channel;

import java.util.Date;

public class ConnectionInfo {
		private Channel channel;
		private String src_ip, URI;
		private Date timestamp;
		private long sent_bytes;
		private long  speed;
		private long received_bytes;
		
		public ConnectionInfo(Channel channel){
			this.channel = channel;
		}
		
		public boolean isThisChannel(Channel channel){
			return(this.channel == channel);
		}
		
		public String getSrc_ip() {
			return src_ip;
		}
		public void setSrc_ip(String src_ip) {
			this.src_ip = src_ip;
		}
		public String getURI() {
			return URI;
		}
		public void setURI(String uRI) {
			URI = uRI;
		}
		public Date getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(Date date) {
			this.timestamp = date;
		}
		public long getSent_bytes() {
			return sent_bytes;
		}
		public void setSent_bytes(long l) {
			this.sent_bytes = l;
		}
		public long getReceived_bytes() {
			return received_bytes;
		}
		public void setReceived_bytes(long l) {
			this.received_bytes = l;
		}
		public long getSpeed() {
			return speed;
		}
		public void setSpeed(long speed2) {
			this.speed = speed2;
		}
		
}
