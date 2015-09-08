package com.example.dawid.encryptionapp.Utilities;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Dawid on 2015-07-12.
 */
public class FIleHandler {

    public static String bytesToHex(byte[] in)
    {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        String out = builder.toString();
        return out;
    }

    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static StringBuilder getMessage()
    {

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "message.txt");

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {

        }

        return text;
    }

    public static void saveMessage(String message)
    {
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "message.txt");

        try{
            PrintWriter out = new PrintWriter(file);
            out.append(message);
            out.close();
        }catch(IOException e){
            Log.e("Overwriting error", e.toString());
        }
    }

}
