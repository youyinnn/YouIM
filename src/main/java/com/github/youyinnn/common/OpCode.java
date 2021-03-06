package com.github.youyinnn.common;

import java.util.HashMap;
import java.util.Map;

/**
 * The enum Operation code.
 *
 * @author youyinnn
 */
public enum  OpCode {

    /**
     * Text operation code.
     */
    TEXT((byte) 1),
    /**
     * Binary operation code.
     */
    BINARY((byte) 2),
    /**
     * Close operation code.
     */
    CLOSE((byte) 8),
    /**
     * Ping operation code.
     */
    PING((byte) 9),
    /**
     * Pong operation code.
     */
    PONG((byte) 10);

    private final byte code;

    private static final Map<Byte, OpCode> MAP = new HashMap<>();

    static {
        for (OpCode operationCode : values()) {
            MAP.put(operationCode.getCode(), operationCode);
        }
    }

    /**
     * Value of operation code.
     *
     * @param code the code
     * @return the operation code
     */
    public static OpCode valueOf(byte code) {
        return MAP.get(code);
    }

    private OpCode(byte code) {
        this.code = code;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public byte getCode() {
        return code;
    }
}
