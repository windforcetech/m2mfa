package com.m2micro.m2mfa.push;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class MyChatServerInitializer  extends ChannelInitializer<SocketChannel> {


  @Override
  protected void initChannel(SocketChannel ch)  {
    ChannelPipeline pipeline = ch.pipeline();
    //解码器   （默认以什么结束符，可以为自定义某个字段）
    pipeline.addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()));
    pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
    pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
    pipeline.addLast(new MyChatServerHandler());
  }
}
