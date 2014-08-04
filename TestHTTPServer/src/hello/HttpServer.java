package hello;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
  
  public final class HttpServer {
  
      
      
      public static void main(String[] args) throws Exception {
    	  int port;
	      if (args.length > 0) {
	            port = Integer.parseInt(args[0]);
	      } else {
	            port = 8080;
	      }
          EventLoopGroup bossGroup = new NioEventLoopGroup();
          EventLoopGroup workerGroup = new NioEventLoopGroup();
          try {
              ServerBootstrap b = new ServerBootstrap();
               b.group(bossGroup, workerGroup)
               .channel(NioServerSocketChannel.class)
 //              .handler(new LoggingHandler(LogLevel.WARN))
               .childHandler(new HttpServerInitializer());  
               Channel ch = b.bind(port).sync().channel();
              System.err.println("Open your web browser and navigate to http://127.0.0.1:" + port + '/');
  
              ch.closeFuture().sync();
          } finally {
              bossGroup.shutdownGracefully();
              workerGroup.shutdownGracefully();
          }
      }
  }
