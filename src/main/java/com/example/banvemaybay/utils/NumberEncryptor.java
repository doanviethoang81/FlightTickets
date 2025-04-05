package com.example.banvemaybay.utils;

public class NumberEncryptor {
    private static final int SECRET_KEY = 987654;

    public static String encryptId(int id) throws Exception {
        int encrypted = id ^ SECRET_KEY; // XOR với khóa bí mật
        return String.valueOf(encrypted);
    }

    public static int decryptId(String encryptedId) throws Exception {
        return Integer.parseInt(encryptedId) ^ SECRET_KEY;
    }
}
