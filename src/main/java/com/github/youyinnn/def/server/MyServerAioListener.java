package com.github.youyinnn.def.server;

import com.github.youyinnn.server.core.AbstractServerAioListener;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 * @author youyinnn
 */
public class MyServerAioListener extends AbstractServerAioListener {

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
        //System.out.println("onAfterReceived channelContext:" + channelContext +
        //        ", packet:" + packet +
        //        ", packetSize:" + packetSize);
    }

    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
        //System.out.println("onAfterSent channelContext:" + channelContext +
        //        ", packet:" + packet +
        //        ", isSentSuccess:" + isSentSuccess);
    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) {

    }
}
