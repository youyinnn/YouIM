package com.github.youyinnn.def.server;

import com.github.youyinnn.common.packet.GroupMsgRequestBody;
import com.github.youyinnn.server.AbstractServerAioHandler;
import org.tio.core.ChannelContext;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author youyinnn
 */
public class MyServerAioHandler extends AbstractServerAioHandler {

    private AtomicLong subToken = new AtomicLong();

    @Override
    protected String getToken() {
        return System.currentTimeMillis() + "_" + subToken.incrementAndGet();
    }

    @Override
    protected void beforeLoginRequestHandle() {

    }

    @Override
    protected void afterLoginRequestHandle() {

    }

    @Override
    protected void beforeGroupMsgRequestHandle() {

    }

    @Override
    protected void afterGroupMsgRequestHandle() {

    }

    @Override
    protected void beforeJoinGroupRequestHandle() {

    }

    @Override
    protected void afterJoinGroupRequestHandle() {

    }

    @Override
    protected void beforeP2PMsgRequestHandle() {

    }

    @Override
    protected void afterP2PMsgRequestHandle() {

    }

    @Override
    protected void beforeLogoutRequestHandle() {

    }

    @Override
    protected void afterLogoutRequestHandle() {

    }

    @Override
    protected void beforeQuitGroupRequestHandle() {

    }

    @Override
    protected void afterQUitGroupRequestHandle() {

    }


    @Override
    protected boolean heartbeatRequestHandler(GroupMsgRequestBody baseMsgBody, ChannelContext channelContext) {
        return true;
    }
}
