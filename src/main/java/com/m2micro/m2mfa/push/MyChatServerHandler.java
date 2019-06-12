package com.m2micro.m2mfa.push;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyChatServerHandler  extends SimpleChannelInboundHandler<String> {

  //保存客户端所有用户
  private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg)  {
//    Channel channel = ctx.channel();
//    channelGroup.stream().forEach(ch -> {
//      if(channel!=ch){
//        ch.writeAndFlush(channel.remoteAddress()+"发送的信息"+msg+"\n");
//      }else {
//        ch.writeAndFlush("【自己】"+msg+"\n");
//      }
//    });
  }


  //出现异常时
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  {
    cause.printStackTrace();
    ctx.close();
  }


  //客户端链接服务器触发事件
  @Override
  public void handlerAdded(ChannelHandlerContext ctx)  {
    Channel channel = ctx.channel();
    //channelGroup.writeAndFlush("【服务器】-"+channel.remoteAddress()+"加入\n");
    channelGroup.add(channel);

  }


  //客户端断开触发的事件
  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
   // channelGroup.writeAndFlush("【服务器】-"+channel.remoteAddress()+"离开\n");
  }


  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
//    Channel channel = ctx.channel();
//    System.out.println(channel.remoteAddress()+"上线了");

  }


  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    System.out.println(channel.remoteAddress()+"下线了");
  }

  /**
   * 推送更新大屏数据
   */
  public static  void pushMainScreen(){
    channelGroup.stream().forEach(ch -> {
        ch.writeAndFlush("updateMainScreen");
    });
  }
}
