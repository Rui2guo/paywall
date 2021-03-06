/*
 *************************************************************************
 *                                                                       *
 *  LightningJ                                                           *
 *                                                                       *
 *  This software is free software; you can redistribute it and/or       *
 *  modify it under the terms of the GNU Lesser General Public License   *
 *  (LGPL-3.0-or-later)                                                  *
 *  License as published by the Free Software Foundation; either         *
 *  version 3 of the License, or any later version.                      *
 *                                                                       *
 *  See terms of license at gnu.org.                                     *
 *                                                                       *
 *************************************************************************/
package org.lightningj.paywall.util

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.ChannelPipeline
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpResponseEncoder
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler



/**
 * Help class that sets up a test web server on a specified port with a given handler.
 *
 * Used in unit test to test http communication.
 *
 * Created by Philip Vendil on 2018-10-15.
 */
class TestWebServer {

    private int port;
    private ChannelInboundHandlerAdapter handler
    private EventLoopGroup eventLoopGroup
    private ChannelFuture channelFuture

    TestWebServer(int port, ChannelInboundHandlerAdapter handler){
        this.port = port
        this.handler = handler
    }

    void startup(){

        eventLoopGroup = new NioEventLoopGroup();

            ServerBootstrap b = new ServerBootstrap()
                    b.group(eventLoopGroup)
                           .channel(NioServerSocketChannel.class)
                           .handler(new LoggingHandler(LogLevel.INFO))
                           .childHandler(new ChannelInitializer<SocketChannel>() {
                              @Override
                              void initChannel(SocketChannel ch) throws Exception {
                                  ChannelPipeline p = ch.pipeline()
                                  p.addLast(new HttpServerCodec())
                                  p.addLast(new HttpObjectAggregator(512*1024))
                                  p.addLast(handler)
                              }
                             })

            channelFuture = b.bind(port)
    }

    void shutdown(){
        eventLoopGroup.shutdownGracefully()
        channelFuture.channel().closeFuture().sync()

    }
}
