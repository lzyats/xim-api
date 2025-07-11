package com.platform.modules.chat.rtc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.TreeMap;

public class RtcByteBuf {
    ByteBuffer buffer = ByteBuffer.allocate(1024).order(ByteOrder.LITTLE_ENDIAN);

    public RtcByteBuf() {
    }

    public byte[] asBytes() {
        byte[] out = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(out, 0, out.length);
        return out;
    }

    // packUint16
    public RtcByteBuf put(short v) {
        buffer.putShort(v);
        return this;
    }

    public RtcByteBuf put(byte[] v) {
        put((short) v.length);
        buffer.put(v);
        return this;
    }

    // packUint32
    public RtcByteBuf put(int v) {
        buffer.putInt(v);
        return this;
    }

    public RtcByteBuf put(String v) {
        return put(v.getBytes());
    }


    public RtcByteBuf putIntMap(TreeMap<Short, Integer> extra) {
        put((short) extra.size());

        for (Map.Entry<Short, Integer> pair : extra.entrySet()) {
            put(pair.getKey());
            put(pair.getValue());
        }

        return this;
    }

    public short readShort() {
        return buffer.getShort();
    }


    public int readInt() {
        return buffer.getInt();
    }

    public byte[] readBytes() {
        short length = readShort();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }

    public String readString() {
        byte[] bytes = readBytes();
        return new String(bytes);
    }

    public TreeMap<Short, Integer> readIntMap() {
        TreeMap<Short, Integer> map = new TreeMap<>();

        short length = readShort();

        for (short i = 0; i < length; ++i) {
            short k = readShort();
            Integer v = readInt();
            map.put(k, v);
        }

        return map;
    }
}
