package com.hinsliu.bestroute.utils.wechat;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class DigestUtil {

    public static String sha1DigestAsHex(String data) {
        // SHA1
        MessageDigest md = getDigest("SHA-1");
        md.update(data.getBytes());
        byte[] digest = md.digest();
        StringBuffer hexstr = new StringBuffer();
        String shaHex = "";
        for (int i = 0; i < digest.length; i++) {
            shaHex = Integer.toHexString(digest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexstr.append(0);
            }
            hexstr.append(shaHex);
        }
        return hexstr.toString();
    }

    /**
     * Creates a new {@link MessageDigest} with the given algorithm. Necessary
     * because {@code MessageDigest} is not
     * thread-safe.
     */
    private static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            log.error("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
            throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
        }
    }
}
