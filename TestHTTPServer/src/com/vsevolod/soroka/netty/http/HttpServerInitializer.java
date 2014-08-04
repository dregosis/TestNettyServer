package com.vsevolod.soroka.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
  
  public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
	  
      @Override
      public void initChannel(SocketChannel ch) {
          ChannelPipeline p = ch.pipeline();
          p.addFirst(new TraficHandler(500));
          p.addLast("aggregator", new HttpObjectAggregator(65536));
          p.addLast(new HttpServerCodec());
          p.addLast(new RequestsHandler());
          ServerInfo.addConnection(ch);
      }
  }
