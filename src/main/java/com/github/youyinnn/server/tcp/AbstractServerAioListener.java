package com.github.youyinnn.server.tcp;

import com.github.youyinnn.common.BaseSessionContext;
import com.github.youyinnn.server.Server;
import com.github.youyinnn.youwebutils.third.Log4j2Helper;
import org.apache.logging.log4j.Logger;
import org.tio.core.ChannelContext;
import org.tio.server.intf.ServerAioListener;

/**
 * The type Abstract server aio listener.
 *
 * @author youyinnn
 */
public abstract class AbstractServerAioListener implements ServerAioListener {

    private static final Logger SERVER_LOG = Log4j2Helper.getLogger("$im_server");

    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        //连接成功后设置一个连接会话对象给该连接通道
        channelContext.setAttribute(new BaseSessionContext());
        if (Server.isServerListenerLogEnabled()) {
            SERVER_LOG.info("客户端连接:channel:{}, isConnected:{}, isReconnect:{}.", channelContext, isConnected, isReconnect);
        }
        afterConnected(channelContext,isConnected,isReconnect);
    }

    @Override
    public void onAfterClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
        if (Server.isServerListenerLogEnabled()) {
            SERVER_LOG.info("客户端已关闭:channel:{}, remark:{}, isRemove:{}.", channelContext, remark, isRemove);
        }
        afterClosed(channelContext, throwable, remark, isRemove);
    }

    /**
     * 连接成功后必然要设置一个session对象, 这样做避免用户忘了设置.
     *
     * @param channelContext the channel context
     * @param isConnected    the is connected
     * @param isReconnect    the is reconnect
     * @throws Exception the exception
     */
    public abstract void afterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception;

    /**
     * After closed.
     *
     * @param channelContext the channel context
     * @param throwable      the throwable
     * @param remark         the remark
     * @param isRemove       the is remove
     * @throws Exception the exception
     */
    public abstract void afterClosed(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception;

}
