package com.github.youyinnn.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.RandomUtil;
import org.tio.core.intf.TioUuid;

/**
 * @author youyinnn
 */
public class WsTioUuId implements TioUuid {

    private Snowflake snowflake;

    public WsTioUuId() {
        snowflake = new Snowflake(RandomUtil.randomInt(1, 30), RandomUtil.randomInt(1, 30));
    }

    public WsTioUuId(long workerId, long dataCenterId) {
        snowflake = new Snowflake(workerId, dataCenterId);
    }

    @Override
    public String uuid() {
        return snowflake.nextId() + "_" + System.nanoTime();
    }
}
