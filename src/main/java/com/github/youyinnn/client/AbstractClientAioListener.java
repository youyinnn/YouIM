package com.github.youyinnn.client;

import com.github.youyinnn.common.BaseSessionContext;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.ChannelContext;

/**
 * @author youyinnn
 */
public abstract class AbstractClientAioListener implements ClientAioListener {

    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        //连接成功后设置一个连接会话对象给该连接通道
        channelContext.setAttribute(new BaseSessionContext());
        afterConnected(channelContext,isConnected,isReconnect);
        if (isReconnect) {
            if (Client.isLogin()) {
                Client.login(Client.getLoginUserId());
            }
        }
    }

    /**
     * 连接成功后必然要设置一个session对象, 这样做避免用户忘了设置.
     *
     * @param channelContext
     * @param isConnected
     * @param isReconnect
     * @throws Exception
     */
    public abstract void afterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception ;
}
