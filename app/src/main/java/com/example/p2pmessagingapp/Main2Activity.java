package com.example.p2pmessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.*;
import java.util.*;

public class Main2Activity extends AppCompatActivity {


    EditText receivePortEditText, targetPortEditText, messageEditText, targetIPEditText,hostIPAddressText;
    TextView chatText,userNameTextView;
    String recieveport="",targetport="",ipaddress="",userName="";
    Button StartServer,Connect;
    boolean server=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        receivePortEditText =(EditText) findViewById(R.id.receiveEditText);
        targetPortEditText =(EditText) findViewById(R.id.targetPortEditText);
        messageEditText = findViewById(R.id.messageEditText);
        targetIPEditText = findViewById(R.id.targetIPEditText);
        userNameTextView=findViewById(R.id.username);
        chatText = findViewById(R.id.chatText);

        hostIPAddressText=(EditText) findViewById(R.id.HostIPAddressText);
        hostIPAddressText.setFocusable(false);
        //StartServer = findViewById(R.id.serverButton);

    }



    public void onServerClicked(View v){

        recieveport = receivePortEditText.getText().toString();
        userName = userNameTextView.getText().toString();

        if(recieveport.equals("") && userName.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Enter Receive Port Number && Username",Toast.LENGTH_SHORT).show();
        }

        else if(recieveport.equals(""))
        {
            Toast.makeText(getApplicationContext(),"First Enter Receive Port Number",Toast.LENGTH_SHORT).show();
        }

        else if(userName.equals(""))
        {
            Toast.makeText(getApplicationContext(),"You must Enter UserName",Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(getApplicationContext(),"Server Created",Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),getIPAddress(true),Toast.LENGTH_SHORT).show();
            server=true;
            hostIPAddressText.setText(getIPAddress(true));
        }

    }


    public void onConnectClicked(View v){

        ipaddress = targetIPEditText.getText().toString();
        targetport = targetPortEditText.getText().toString();

        if(targetport.equals("") && ipaddress.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Enter Target Port && IP Address ",Toast.LENGTH_SHORT).show();
        }

        else if(targetport.equals(""))
        {
            Toast.makeText(getApplicationContext(),"You must Enter Target Port Number",Toast.LENGTH_SHORT).show();
        }

        else if(ipaddress.equals(""))
        {
            Toast.makeText(getApplicationContext(),"You must Enter target IP Address",Toast.LENGTH_SHORT).show();
        }

        else if(server==false)
        {
            Toast.makeText(getApplicationContext(),"You must Start server",Toast.LENGTH_SHORT).show();
        }

        else {

            Intent i = new Intent(Main2Activity.this, MainActivity.class);
            i.putExtra("tag1", recieveport);
            i.putExtra("tag2", ipaddress);
            i.putExtra("tag3", targetport);
            i.putExtra("tag4", userName);

            Toast.makeText(getApplicationContext(), "Device connected", Toast.LENGTH_SHORT).show();

            startActivity(i);
        }

    }


    // To Get IP Address of the device
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";


    }



}