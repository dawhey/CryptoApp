package com.example.dawid.encryptionapp.Fragments;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dawid.encryptionapp.R;
import com.example.dawid.encryptionapp.Utilities.AESHandler;
import com.example.dawid.encryptionapp.Utilities.FIleHandler;
import java.io.UnsupportedEncodingException;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import javax.crypto.NoSuchPaddingException;


/**
 * Created by Dawid on 2015-07-11.
 */
public class LocalModuleFragment extends Fragment {

    private TextView ivView;
    private TextView keyView;
    private Button encryptButton;
    private Button decryptButton;
    private String encryptedMessage;
    private String decryptedMessage;

    private AESHandler aesHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        try {
            aesHandler = new AESHandler();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.local_fragment_layout, container, false);
        keyView = (TextView) v.findViewById(R.id.keyTextView);
        ivView = (TextView) v.findViewById(R.id.ivTextView);

        keyView.setText(FIleHandler.bytesToHex(aesHandler.getKey()));

        encryptButton = (Button) v.findViewById(R.id.encryptButton);
        decryptButton = (Button) v.findViewById(R.id.decryptButton);

        encryptButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (AESHandler.isEncrypted)
                    Toast.makeText(getActivity(), "Message is already encrypted.", Toast.LENGTH_SHORT).show();
                else {
                    encrypt();
                    ivView.setText(FIleHandler.bytesToHex(aesHandler.getIV()));
                }
            }
        });

        decryptButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (!AESHandler.isEncrypted)
                    Toast.makeText(getActivity(), "Encrypt the message first.", Toast.LENGTH_SHORT).show();
                else
                {
                    decrypt();
                }
            }
        });

        return v;
    }

    public void encrypt()
    {
        try {
            encryptedMessage = aesHandler.encryptLocally(FIleHandler.getMessage().toString());
            FIleHandler.saveMessage(encryptedMessage);
            Toast.makeText(getActivity(), "Message encrypted successfully.", Toast.LENGTH_SHORT).show();
            AESHandler.isEncrypted = true;

        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | UnsupportedEncodingException
                | InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | BadPaddingException e) {
            Log.e("error", e.toString());
        }
    }

    public void decrypt()
    {
        try {
            decryptedMessage = aesHandler.decryptLocally(FIleHandler.getMessage().toString());
            FIleHandler.saveMessage(decryptedMessage);
            Toast.makeText(getActivity(), "Message decrypted successfully.", Toast.LENGTH_SHORT).show();
            AESHandler.isEncrypted = false;
        } catch (BadPaddingException
                | IllegalBlockSizeException
                | InvalidAlgorithmParameterException
                | InvalidKeyException
                | UnsupportedEncodingException e) {
            Log.e("error", e.toString());
        }
    }
}

