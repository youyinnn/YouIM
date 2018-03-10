package com.github.youyinnn.server;

import com.github.youyinnn.common.BaseSessionContext;
import org.tio.core.ChannelContext;
import org.tio.server.intf.ServerAioListener;

/**
 * @author youyinnn
 */
public abstract class AbstractServerAioListener implements ServerAioListener {

    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        //连接成功后设置一个连接会话对象给该连接通道
        channelContext.setAttribute(new BaseSessionContext());
        onAfterConnectedAndSetSession(channelContext,isConnected,isReconnect);
    }

    /**
     * 连接成功后必然要设置一个session对象, 这样做避免用户忘了设置.
     *
     * @param channelContext
     * @param isConnected
     * @param isReconnect
     * @throws Exception
     */
    public abstract void onAfterConnectedAndSetSession(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception;
}
