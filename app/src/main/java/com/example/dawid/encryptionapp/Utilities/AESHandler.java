package com.example.dawid.encryptionapp.Utilities;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Dawid on 2015-07-12.
 */
public class AESHandler {

    public static byte[] Key;
    private byte[] IV;

    private SecretKeySpec keySpec;
    private IvParameterSpec ivSpec;
    private Cipher cipher;

    public static boolean isEncrypted;

    public AESHandler() throws NoSuchPaddingException, NoSuchAlgorithmException {
        Key = generateKey();
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        keySpec = new SecretKeySpec(Key, "AES");
    }

    private byte[] generateKey()
    {
        byte[] key = new byte[32];
        try {
            SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
            secureRandomGenerator.nextBytes(key);
        }
        catch(NoSuchAlgorithmException e){
            Log.e("KeyError", e.toString());
        }
        return key;
    }

    private byte[] generateIV()
    {
        byte[] iv = new byte[16];
        try {
            SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
            secureRandomGenerator.nextBytes(iv);
        }
        catch(NoSuchAlgorithmException e){
            Log.e("KeyError", e.toString());
        }
        return iv;
    }

    public byte[] getKey()
    {
        return Key;
    }

    public byte[] getIV()
    {
        return IV;
    }

    public String encryptLocally(String message) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        IV = generateIV();
        ivSpec = new IvParameterSpec(IV);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherText = cipher.doFinal(message.getBytes());
        byte[] base64encodedSecretData = Base64.encode(cipherText, Base64.DEFAULT);

        String encryptedMessage = new String(base64encodedSecretData);
        String ivHex = FIleHandler.bytesToHex(IV);

        return encryptedMessage+ivHex;
    }

    public String decryptLocally(String encryptedMessage) throws BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException {

        String ivHex = encryptedMessage.substring(encryptedMessage.length() - 33, encryptedMessage.length() - 1);
        encryptedMessage = encryptedMessage.substring(0, encryptedMessage.length() - 34);

        IV = FIleHandler.hexToBytes(ivHex);
        ivSpec = new IvParameterSpec(IV);

        byte[] decodedValue = Base64.decode(encryptedMessage.getBytes(), Base64.DEFAULT);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decryptedMessage = cipher.doFinal(decodedValue);

        return new String(decryptedMessage);
    }

}
