package com.teamness.smane;

public interface Base64Provider {

    byte[] decode(String base64Str);
    String encode(byte[] bytes);

}
