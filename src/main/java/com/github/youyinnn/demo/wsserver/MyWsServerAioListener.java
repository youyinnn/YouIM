package com.github.youyinnn.demo.wsserver;

import com.github.youyinnn.server.ws.AbstractWsServerAioListener;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 * @author youyinnn
 */
public class MyWsServerAioListener extends AbstractWsServerAioListener {

    @Override
    public void afterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {

    }

    @Override
    public void afterClosed(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {

    }

    @Override
    public void onAfterReceived(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {

    }

    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {

    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) {

    }
}
