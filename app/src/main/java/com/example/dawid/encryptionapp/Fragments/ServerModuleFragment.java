package com.example.dawid.encryptionapp.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dawid.encryptionapp.R;
import com.example.dawid.encryptionapp.Utilities.FIleHandler;
import com.example.dawid.encryptionapp.Utilities.JSONHandler;

/**
 * Created by Dawid on 2015-07-11.
 */
public class ServerModuleFragment extends Fragment {

    private ProgressBar progBar;
    public static String ipAddress;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.server_fragment_layout, container, false);
        Button encryptButton = (Button) v.findViewById(R.id.encryptButton);
        Button decryptButton = (Button) v.findViewById(R.id.decryptButton);
        progBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progBar.setIndeterminate(true);
        progBar.setVisibility(View.INVISIBLE);

        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EncryptTask encryptTask = new EncryptTask();
                encryptTask.execute();
            }
        });

        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecryptTask decryptTask = new DecryptTask();
                decryptTask.execute();
            }
        });

        return v;
    }


    public class EncryptTask extends AsyncTask<Void, Void, String>
    {

        String encryptionUrl = "http://" + ipAddress + ":5000/encrypt";

        String encryptedMessage;

        @Override
        protected void onPreExecute() {

            progBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            JSONHandler jsonHandler = new JSONHandler();
            encryptedMessage = jsonHandler.processDataOnServer(encryptionUrl);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (encryptedMessage.equals("Connection with server failed. ")) {
                Toast.makeText(getActivity(), encryptedMessage, Toast.LENGTH_SHORT).show();
            }
            else {
                FIleHandler.saveMessage(encryptedMessage);
                Toast.makeText(getActivity(), "Server encryption successful.", Toast.LENGTH_SHORT).show();
            }
            progBar.setVisibility(View.INVISIBLE);
        }
    }

    public class DecryptTask extends AsyncTask<Void, Void, String>
    {

        String decryptionUrl = "http://" + ipAddress + ":5000/decrypt";

        String decryptedMessage;

        @Override
        protected void onPreExecute() {

            progBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            JSONHandler jsonHandler = new JSONHandler();
            decryptedMessage = jsonHandler.processDataOnServer(decryptionUrl);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (decryptedMessage.equals("Connection with server failed. ")) {
                Toast.makeText(getActivity(), decryptedMessage, Toast.LENGTH_SHORT).show();
            }
            else {
                FIleHandler.saveMessage(decryptedMessage);
                Toast.makeText(getActivity(), "Server decryption successful.", Toast.LENGTH_SHORT).show();
            }

            progBar.setVisibility(View.INVISIBLE);
        }
    }
}
