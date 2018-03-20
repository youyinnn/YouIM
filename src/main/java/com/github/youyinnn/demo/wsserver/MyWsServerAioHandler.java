package com.github.youyinnn.demo.wsserver;

import com.github.youyinnn.server.ws.AbstractWsServerAioHandler;
import org.tio.core.ChannelContext;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;

/**
 * @author youyinnn
 */
public class MyWsServerAioHandler extends AbstractWsServerAioHandler{

    @Override
    protected void afterHandshaked(HttpRequest request, HttpResponse response, ChannelContext channelContext) {

    }

    @Override
    protected void beforeHandshake(HttpRequest request, HttpResponse response, ChannelContext channelContext) {

    }

    @Override
    protected void afterHandled(HttpRequest request, HttpResponse response, ChannelContext channelContext) {

    }

    @Override
    protected void beforeHandle(HttpRequest request, HttpResponse response, ChannelContext channelContext) {

    }
}
