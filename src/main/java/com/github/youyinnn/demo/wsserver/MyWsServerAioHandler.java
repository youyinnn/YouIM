package com.github.youyinnn.demo.wsserver;

import com.github.youyinnn.server.ws.AbstractWsServerAioHandler;
import org.tio.core.ChannelContext;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author youyinnn
 */
public class MyWsServerAioHandler extends AbstractWsServerAioHandler{

    private AtomicLong subToken = new AtomicLong();

    @Override
    protected String getToken() {
        return System.currentTimeMillis() + "_" + subToken.incrementAndGet();
    }

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
