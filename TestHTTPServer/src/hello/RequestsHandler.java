package hello;

import java.util.Date;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
  
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;
  
  public class RequestsHandler extends SimpleChannelInboundHandler<HttpRequest> {
      @Override
      public void channelReadComplete(ChannelHandlerContext ctx) {
          //ctx.flush();
   
    	  //ctx.close();
      }
  
      @Override
      public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	  System.out.print(cause);
          cause.printStackTrace();
          ctx.close();
      }

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request)
			throws Exception {
   		  String address = ctx.channel().remoteAddress().toString();
   		  ServerInfo.addRequest(address.substring(1,address.lastIndexOf(":")), new Date());
   		  ServerInfo.setURLToChannel(ctx.channel(), request.getUri());    
          QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
             
          
	      
	      
	      switch (queryStringDecoder.path()) {
	      
	      case "/hello":
			sendHelloResponse(ctx);
			break;
			
	      case "/redirect":
	    	    String redirectPath = queryStringDecoder.parameters().get("url").get(0);
	           	sendRedirectResponse(ctx, redirectPath);
	          	ServerInfo.addURLRequest(redirectPath);
				break;
				
	      case "/status":
				sendStatusResponse(ctx);
				break;
				
		default:
			if (is100ContinueExpected(request))
		         ctx.writeAndFlush(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
			else{
				FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			}
			break;
	      }
		}
	      

	private void sendHelloResponse(final ChannelHandlerContext ctx){
		ctx.executor().schedule(new Runnable() {
            public void run() {
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8));
                response.headers().set(CONTENT_TYPE, "text/plain");
                response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(CONNECTION, Values.KEEP_ALIVE);
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }}, 10, TimeUnit.SECONDS);

	}
	
	private  void sendStatusResponse(ChannelHandlerContext ctx){
	  	StringBuilder responseContent = new StringBuilder();
	  	 responseContent.append("<html>")
	  	 				.append("<head>")
	  	 				.append("</head>\r\n")
	  	 				.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>")
 		   		  	    .append("Total number of requests:")
       				    .append(ServerInfo.getRequestsCount())
				  	    .append("</br>")
			       	    .append("Number of unique requests (one IP):")
				  	    .append(ServerInfo.getRequestsCountWithUniqueIP())       	   
			       	    .append("</br>")
			       	    .append("</br>")
			       	    .append("Requests information:\n")
				  	    .append("<table border = \"1\" >")
				  	    .append( "<tr><td> IP(Without port) </td><td> Date of last request from this IP </td>  <td> count of requests: </td> </tr>");
	  	 String[] result = ServerInfo.getRequestInformationList();
		 for (int i = 0; i < result.length; i+=3) {
			 responseContent.append( "<tr><td>");
			 responseContent.append(result[i]);
			 responseContent.append( "</td> <td>");
			 responseContent.append(result[i+1]);
			 responseContent.append( "</td> <td>");
			 responseContent.append(result[i+2]);
			 responseContent.append( "</td> </tr>");
		 }   
		 responseContent.append("</table>\r\n");
		 responseContent.append("</br>");
		 responseContent.append("Url requests information:\n");
	  	 responseContent.append("<table border = \"1\" >");
	  	 responseContent.append( "<tr><td> URL </td>  <td> count of redirects: </td> </tr>");
		 result = ServerInfo.getRedirectRequestsInformation();
		 for (int i = 0; i < result.length; i+=2) {
			 responseContent.append( "<tr><td>");
			 responseContent.append(result[i]);
			 responseContent.append( "</td> <td>");
			 responseContent.append(result[i+1]);
			 responseContent.append( "</td> </tr>");
		}   
		 responseContent.append("</table>\r\n");
		 responseContent.append("</br>");
		 responseContent.append("Number of connections that are currently open:\n");
		 responseContent.append(ServerInfo.getConnectionsCount());
		 responseContent.append("</br>");
		 responseContent.append("Last 16 connections info");
		 responseContent.append("</br>");
		 responseContent.append("<table border = \"1\" >");
	  	 responseContent.append( "<tr><td> src_ip </td>  <td> URI</td><td> timestamp</td> <td>sent_bytes</td> <td> received_bytes</td> <td>speed (bytes/sec)</td>  </tr>");
		 Vector<ConnectionInfo> connectionsInfo = ServerInfo.getConnectionsInfo();
		 for (ConnectionInfo connectionInfo : connectionsInfo) {
			 responseContent.append( "<tr><td>");
			 responseContent.append(connectionInfo.getSrc_ip());
			 responseContent.append( "</td> <td>");
			 responseContent.append(connectionInfo.getURI());
			 responseContent.append( "</td> <td>");
			 responseContent.append(connectionInfo.getTimestamp());
			 responseContent.append( "</td> <td>");
			 responseContent.append(connectionInfo.getSent_bytes());
			 responseContent.append( "</td> <td>");
			 responseContent.append(connectionInfo.getReceived_bytes());
			 responseContent.append( "</td> <td>");
			 responseContent.append(connectionInfo.getSpeed());
			 responseContent.append( "</td> </tr>");
			 
		 }
		 responseContent.append("</table>\r\n");
		 responseContent.append("</br>");
	  	 responseContent.append("</body>");
	  	 responseContent.append("</html>");
       	FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.copiedBuffer(responseContent, CharsetUtil.UTF_8));
            response.headers().set(CONTENT_TYPE, "text/html");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
       }
	
    private static void sendRedirectResponse(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(LOCATION, newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    
  }
