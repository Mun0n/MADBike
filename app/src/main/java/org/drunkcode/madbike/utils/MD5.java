package org.drunkcode.madbike.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    public static String hash(String string) {

        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(string.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer MD5Hash = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }

            return MD5Hash.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String buildIdSecurity(String oneData, String twoData) {
        String unite = oneData + twoData;
        String uniteMd5 = MD5.hash(unite);
        String uniteMd5Second = MD5.hash(unite.substring(0, unite.length() / 2));
        return uniteMd5 + uniteMd5Second;
    }
}
