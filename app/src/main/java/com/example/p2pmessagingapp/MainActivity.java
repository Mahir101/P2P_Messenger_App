package com.example.p2pmessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {


    EditText receivePortEditText, targetPortEditText, messageEditText, targetIPEditText;
    TextView chatText;
    ScrollView scrollView;

    ServerClass serverClass;
    ClientClass clientClass;
    SendReceive sendReceive;
    MediaPlayer mp;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    static final int MESSAGE_READ = 1;
    static final String TAG = "yourTag";
    String Full_msg ="",userName="";

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_READ:
                    byte[] readBuff = (byte[]) msg.obj;
                    String tempMsg = new String(readBuff, 0, msg.arg1);

                    String[] messages = tempMsg.split("111", 0);
                    //Full_msg=Full_msg+"\n"+"You: "+messages[0]+"asd"+ messages[1];

                    //To send text
                    if (messages[0].equals("a")) {
                        Full_msg = Full_msg + userName +": "+ messages[1]+"\n";
                        chatText.setText(Full_msg);
                    }

                    //To send File
                    if (messages[0].equals("b")) {

                        String[] file_string = messages[1].split("###", 0);

                        String[] file_name=file_string[1].split(".txt",0);

                        writeToFile(file_name[0], file_string[0]);

                        //chatText.setText(file_string[0]+" "+file_name[0]);

                    }

                    //To change Background Image
                    if (messages[0].equals("c")) {

                        if(messages[1].equals("background1"))
                        {
                            ConstraintLayout constraintlayout = (ConstraintLayout) findViewById(R.id.main_activity_id);
                            constraintlayout.setBackgroundResource(R.drawable.background1);
                        }

                        else if(messages[1].equals("background2"))
                        {
                            ConstraintLayout constraintlayout = (ConstraintLayout) findViewById(R.id.main_activity_id);
                            constraintlayout.setBackgroundResource(R.drawable.background2);
                        }

                        else if(messages[1].equals("background3"))
                        {
                            ConstraintLayout constraintlayout = (ConstraintLayout) findViewById(R.id.main_activity_id);
                            constraintlayout.setBackgroundResource(R.drawable.background3);
                        }
                    }




                    //To change chat color
                    if (messages[0].equals("d")) {

                        if(messages[1].equals("blue"))
                        {
                            scrollView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
                        }

                        else if(messages[1].equals("red"))
                        {
                            scrollView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                        }

                        else if(messages[1].equals("green"))
                        {
                            scrollView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                        }
                    }

                    break;
            }

            return true;
        }
    });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //chatText.setBackgroundColor(getResources().getColor(android.R.color.white));

        messageEditText = findViewById(R.id.messageEditText);
        chatText = findViewById(R.id.chatText);
        scrollView = findViewById(R.id.scrollView1);

        chatText.setTypeface(null, Typeface.BOLD);
        //chatText.setTypeface(null, Typeface.BOLD|Typeface.ITALIC);
        //chatText.setSelectAllOnFocus(true);

        scrollView.setBackgroundColor(getResources().getColor(android.R.color.white));

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String receiveport = bundle.getString("tag1");
            String ipaddress = bundle.getString("tag2");
            String targetport = bundle.getString("tag3");
            userName = bundle.getString("tag4");

            //chatText.setText(receiveport+ipaddress+targetport);
            CreateServer(receiveport);
            CreateClient(ipaddress,targetport);
        }

        verifyStoragePermissions();

    }







    //Three Button Menu Bar Code===============
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.threedot_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg;
        ConstraintLayout  constraintlayout;

        switch (item.getItemId())
        {

            //Change Background Color======================
            case R.id.blue:
                scrollView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
                msg="d111"+"blue";
                sendReceive.write(msg.getBytes());
                return true;

            case R.id.red:
                scrollView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                msg="d111"+"red";
                sendReceive.write(msg.getBytes());
                return true;



            case R.id.green:
                scrollView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                msg="d111"+"green";
                sendReceive.write(msg.getBytes());
                return true;


            //Change Background=================================
            case R.id.background1:
                constraintlayout = (ConstraintLayout) findViewById(R.id.main_activity_id);
                constraintlayout.setBackgroundResource(R.drawable.background1);
                msg="c111"+"background1";
                sendReceive.write(msg.getBytes());
                return true;

            case R.id.background2:
                constraintlayout = (ConstraintLayout) findViewById(R.id.main_activity_id);
                constraintlayout.setBackgroundResource(R.drawable.background2);
                msg="c111"+"background2";
                sendReceive.write(msg.getBytes());
                return true;

            case R.id.background3:
                constraintlayout = (ConstraintLayout) findViewById(R.id.main_activity_id);
                constraintlayout.setBackgroundResource(R.drawable.background3);
                msg="c111"+"background3";
                sendReceive.write(msg.getBytes());
                return true;

        }



        return super.onOptionsItemSelected(item);
    }



    //=======================================







    //==============Returning to previous Activity========================

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        System.exit(1);
    }

    //=====================================================



    public void CreateServer(String port)
    {
        serverClass = new ServerClass(Integer.parseInt(port));
        serverClass.start();
    }


    public void CreateClient(String ip,String port)
    {
        clientClass = new ClientClass(ip, Integer.parseInt(port));
        clientClass.start();
    }



    public void onSendClicked(View v){

        String msg=messageEditText.getText().toString();


        if(msg.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Write Message First",Toast.LENGTH_SHORT).show();
            return;
        }

        else
        {
            messageEditText.setText("");
            Full_msg=Full_msg+userName+": "+msg+"\n";
            chatText.setText(Full_msg);
            msg="a111"+msg; //"a111" here "a" code used normal messages

            sendReceive.write(msg.getBytes());
        }

    }


    public void onSaveClicked(View v){

        writeToFile("P2P Messenger Chats", Full_msg);

    }


    public void onSendFileClicked(View v) {

        Intent intent = new Intent().setType("text/plain").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a TXT file"), 123);

    }

    public void onClearTrayClicked(View view) {
            Full_msg="";
            chatText.setText("");
    }

    public void onVoiceButtonClicked(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());

        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(intent,10);
        }
        else
        {
            Toast.makeText(this,"Device Don't Support Speech Input",Toast.LENGTH_LONG).show();
        }


    }





    //=========================Functions to send file=====================================


    //URI=Uniform Resource Identifier


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode==123 && resultCode==RESULT_OK) {
            Uri uri = intent.getData();

            File file = new File(path(uri));
            String strFileName = file.getName();
            //chatText.setText(strFileName);

            MediaPlayer Sound = MediaPlayer.create(this,R.raw.sound);
            Sound.start();

            String textInsideTheSelectedFile = uriToString(uri);

            textInsideTheSelectedFile="b111"+textInsideTheSelectedFile;   //"b111" here "b" code used for sending file

            textInsideTheSelectedFile=textInsideTheSelectedFile+"###"+strFileName;  //"$$$" here for "File name"


            sendReceive.write(textInsideTheSelectedFile.getBytes());

        }

        else if(requestCode==10 && resultCode==RESULT_OK && intent!=null)
        {
            ArrayList<String> result = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            messageEditText.setText(result.get(0));

        }


    }




    private String uriToString(Uri uri){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));
            String line = "";

            while ((line = reader.readLine()) != null) {
                builder.append("\n"+line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Toast.makeText(getApplicationContext(),"File Sent",Toast.LENGTH_SHORT).show();

        return builder.toString();
    }




    private String path(Uri uri){
        String path = uri.getPathSegments().get(1);
        path = Environment.getExternalStorageDirectory().getPath()+"/"+path.split(":")[1];
        return path;
    }



    void showToast(String str)
    {
        Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
    }



    public void verifyStoragePermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
        }

    }



    private void writeToFile(String fileName, String data) {
        Long time= System.currentTimeMillis();
        String timeMill = " "+time.toString();
        File defaultDir = Environment.getExternalStorageDirectory();
        File file = new File(defaultDir, fileName+"("+timeMill+")"+".txt");
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(file, false);
            stream.write(data.getBytes());
            stream.close();
            showToast("file saved in: "+file.getPath());
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.toString());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }
    }




    //=======================================Server AND  Client Classes================================================================

    public class ServerClass extends Thread{
        Socket socket;
        ServerSocket serverSocket;
        int port;

        private boolean exit=false;


        public ServerClass(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            //while (!exit)
            //{
                    try {
                        serverSocket = new ServerSocket(port);
                        Log.d(TAG, "Waiting for client...");
                        //showToast("Waiting for client...");
                        socket = serverSocket.accept();
                        Log.d(TAG, "Connection established from server");
                        //showToast("Connection established from server");
                        sendReceive = new SendReceive(socket);
                        sendReceive.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "ERROR/n" + e);
                    }

            //}

        }


        public void stopThread()
        {
            exit = true;
        }

    }

    private class SendReceive extends Thread{
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public SendReceive(Socket skt)
        {
            socket=skt;
            try {
                inputStream=socket.getInputStream();
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte[] buffer=new byte[1774470];
            int bytes;

            while (socket!=null)
            {
                try {
                    bytes=inputStream.read(buffer);
                    if(bytes>0)
                    {
                        handler.obtainMessage(MESSAGE_READ,bytes,-1,buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(final byte[] bytes) {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        outputStream.write(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }



    public class ClientClass extends Thread{
        Socket socket;
        String hostAdd;
        int port;

        private boolean exit=false;

        public  ClientClass(String hostAddress, int port)
        {
            this.port = port;
            this.hostAdd = hostAddress;
        }

        @Override
        public void run() {

            //while (!exit) {
                    try {
                        socket = new Socket(hostAdd, port);
                        Log.d(TAG, "Client is connected to server");
                        //showToast("Client is connected to server");
                        sendReceive = new SendReceive(socket);
                        sendReceive.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                        //showToast("Can't connect from client/n");
                        Log.d(TAG, "Can't connect from client/n" + e);
                    }
              //  }
        }

        public void stopThread()
        {
            exit = true;
        }


    }


}