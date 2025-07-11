package com.platform.modules.chat.rtc;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.Deflater;

public class RtcToken {

    private static final String VERSION = "007";
    public static final short SERVICE_TYPE_RTC = 1;

    private String appId;
    private String secret;
    private int expire;
    private int issueTs;
    private int salt;

    public Map<Short, Service> services = new TreeMap<>();

    public RtcToken(String appId, String secret, int expire) {
        this.secret = secret;
        this.appId = appId;
        this.expire = expire;
        this.issueTs = getTimestamp();
        this.salt = randomInt();
    }

    public void addService(Service service) {
        this.services.put(service.getServiceType(), service);
    }

    public String build() throws Exception {
        if (!isUUID(this.appId) || !isUUID(this.secret)) {
            return "";
        }

        RtcByteBuf buf = new RtcByteBuf().put(this.appId).put(this.issueTs).put(this.expire).put(this.salt)
                .put((short) this.services.size());
        byte[] signing = getSign();

        this.services.forEach((k, v) -> {
            v.pack(buf);
        });

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(signing, "HmacSHA256"));
        byte[] signature = mac.doFinal(buf.asBytes());

        RtcByteBuf bufferContent = new RtcByteBuf();
        bufferContent.put(signature);
        bufferContent.buffer.put(buf.asBytes());
        return getVersion() + base64Encode(compress(bufferContent.asBytes()));
    }

    public byte[] getSign() throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(new RtcByteBuf().put(this.issueTs).asBytes(), "HmacSHA256"));
        byte[] signing = mac.doFinal(this.secret.getBytes());
        mac.init(new SecretKeySpec(new RtcByteBuf().put(this.salt).asBytes(), "HmacSHA256"));
        return mac.doFinal(signing);
    }

    public static String getVersion() {
        return VERSION;
    }


    public static class Service {
        public short type;
        public TreeMap<Short, Integer> privileges = new TreeMap<Short, Integer>() {
        };

        public Service() {
        }

        public void addPrivilegeRtc(RtcType privilege, int expire) {
            this.privileges.put(privilege.intValue, expire);
        }

        public short getServiceType() {
            return this.type;
        }

        public RtcByteBuf pack(RtcByteBuf buf) {
            return buf.put(this.type).putIntMap(this.privileges);
        }

        public void unpack(RtcByteBuf byteBuf) {
            this.privileges = byteBuf.readIntMap();
        }
    }

    public static class ServiceRtc extends Service {
        public String channel;
        public String userNo;

        public ServiceRtc(String channel, String userNo) {
            this.type = SERVICE_TYPE_RTC;
            this.channel = channel;
            this.userNo = userNo;
        }

        public RtcByteBuf pack(RtcByteBuf buf) {
            return super.pack(buf).put(this.channel).put(this.userNo);
        }

        public void unpack(RtcByteBuf byteBuf) {
            super.unpack(byteBuf);
            this.channel = byteBuf.readString();
            this.userNo = byteBuf.readString();
        }
    }

    public static String base64Encode(byte[] data) {
        byte[] encodedBytes = Base64.encodeBase64(data);
        return new String(encodedBytes);
    }

    public static int getTimestamp() {
        return (int) ((new Date().getTime()) / 1000);
    }

    public static int randomInt() {
        return new SecureRandom().nextInt();
    }

    public static boolean isUUID(String uuid) {
        if (uuid.length() != 32) {
            return false;
        }
        return uuid.matches("\\p{XDigit}+");
    }

    public static byte[] compress(byte[] data) {
        byte[] output;
        Deflater deflater = new Deflater();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            deflater.reset();
            deflater.setInput(data);
            deflater.finish();
            byte[] buf = new byte[data.length];
            while (!deflater.finished()) {
                int i = deflater.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            deflater.end();
        }
        return output;
    }

}
