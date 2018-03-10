package com.github.youyinnn.def.client;

import com.github.youyinnn.client.core.AbstractClientAioListener;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 * @author youyinnn
 */
public class MyClientAioListener extends AbstractClientAioListener {

    @Override
    public void onAfterClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
        System.out.println("onAfterClose channelContext:" + channelContext +
                ", throwable:" + throwable +
                ", remark:" + remark +
                ", isRemove:" +  isRemove);
    }

    @Override
    public void onAfterConnectedAndSetSession(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        System.out.println("onAfterConnected channelContext:" + channelContext +
                ", isConnected:" + isConnected +
                ", isReconnect:" + isReconnect);
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
