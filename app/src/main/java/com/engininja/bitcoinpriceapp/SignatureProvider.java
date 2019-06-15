package com.engininja.bitcoinpriceapp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class SignatureProvider {

    static String getSignature(String secretKey, String publicKey) throws NoSuchAlgorithmException, InvalidKeyException {

        long timestamp = System.currentTimeMillis() / 1000L;
        String payload = timestamp + "." + publicKey;

        Mac sha256_Mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        sha256_Mac.init(secretKeySpec);
        String hashHex = DatatypeConverter.printHexBinary(sha256_Mac.doFinal(payload.getBytes())).toLowerCase();
        String signature = payload + "." + hashHex;
        return signature;
    }
}
