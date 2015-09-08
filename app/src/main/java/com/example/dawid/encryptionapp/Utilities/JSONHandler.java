package com.example.dawid.encryptionapp.Utilities;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Dawid on 2015-08-06.
 */
public class JSONHandler {


    public String processDataOnServer(String url)
    {
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
        // Limit
        HttpResponse response;
        JSONObject json = new JSONObject();
        try {
            HttpPost post = new HttpPost(url);

            //removing new line character from message
            StringBuilder message = FIleHandler.getMessage();
            json.put("message",message.substring(0, message.length() -1) );
            if (AESHandler.isEncrypted)
                json.put("key", AESHandler.Key);

            post.setHeader("json", json.toString());
            StringEntity se = new StringEntity(json.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
            post.setEntity(se);

            response = client.execute(post);
            //checking for response
            if (response != null) {
                InputStream in = response.getEntity().getContent();

                String a = convertStreamToString(in);
                Log.i("Data from Server", a);
                return a;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Connection with server failed. ";
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
