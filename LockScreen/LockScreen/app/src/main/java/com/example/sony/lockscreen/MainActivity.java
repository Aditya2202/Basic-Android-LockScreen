package com.example.sony.lockscreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    private Button lock;
    private Button disable;
    private Button enable;
    private Button set_password;
    static final int RESULT_ENABLE = 1;

    DevicePolicyManager deviceManger;
    ActivityManager activityManager;
    ComponentName compName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





       deviceManger = (DevicePolicyManager)getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager)getSystemService(
                Context.ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);

        setContentView(R.layout.activity_main);

        lock =(Button)findViewById(R.id.lock);
        lock.setOnClickListener(this);

        disable = (Button)findViewById(R.id.btn_disable);
        enable =(Button)findViewById(R.id.btn_enable);
        set_password=(Button) findViewById(R.id.set_password);

        set_password.setOnClickListener(this);
        disable.setOnClickListener(this);
        enable.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == lock){
            Toast.makeText(getApplicationContext(),"Lock clicked",Toast.LENGTH_SHORT).show();
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                deviceManger.lockNow();
            }
        }

        if(v == enable){
            Toast.makeText(getApplicationContext(),"Enable clicked",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            ComponentName deviceAdminComponentName
                    = new ComponentName(this, MyAdmin.class);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminComponentName);
            startActivityForResult(intent, RESULT_ENABLE);
        }

        if(v == disable){
            Toast.makeText(getApplicationContext(),"Disale clicked",Toast.LENGTH_SHORT).show();
            deviceManger.removeActiveAdmin(compName);
            updateButtonStates();
        }

        if(v==set_password)
        {
            Toast.makeText(getApplicationContext(),"Set Password clicked",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
            startActivity(intent);
        }


    }

    private void updateButtonStates() {

        boolean active = deviceManger.isAdminActive(compName);
        if (active) {
            enable.setEnabled(false);
            disable.setEnabled(true);

        } else {
            enable.setEnabled(true);
            disable.setEnabled(false);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("DeviceAdminSample", "Admin enabled!");
                } else {
                    Log.i("DeviceAdminSample", "Admin enable FAILED!");
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}