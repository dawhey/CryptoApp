package com.example.dawid.encryptionapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dawid.encryptionapp.Fragments.AddressDialogFragment;
import com.example.dawid.encryptionapp.Fragments.LocalModuleFragment;
import com.example.dawid.encryptionapp.Fragments.ServerModuleFragment;
import com.example.dawid.encryptionapp.R;
import com.example.dawid.encryptionapp.Utilities.AESHandler;


public class MainActivity extends ActionBarActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("Local").setIndicator("Local"),
                LocalModuleFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Server").setIndicator("Server"),
                ServerModuleFragment.class, null);

        ServerModuleFragment.ipAddress = "192.168.1.15";
        AESHandler.isEncrypted = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.show_file)
        {
            Intent i = new Intent(this ,FileActivity.class);
            startActivity(i);
        }
        if (id == R.id.edit_address)
        {
            DialogFragment dialogFragment = new AddressDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "ip edit");
        }

        return super.onOptionsItemSelected(item);
    }

}
