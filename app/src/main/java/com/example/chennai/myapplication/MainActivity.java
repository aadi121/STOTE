package com.example.chennai.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Intent;
import android.speech.RecognizerIntent;
import java.util.Locale;
import java.util.ArrayList;
import android.content.ActivityNotFoundException;
import java.io.*;
import android.os.Environment;
import android.util.*;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
{
        int number;
        TextView voiceInput;
        private TextView speakButton;
        private final int REQ_CODE_SPEECH_INPUT = 100;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            voiceInput = (TextView) findViewById(R.id.voiceInput);
            speakButton = (TextView) findViewById(R.id.btnSpeak);

            speakButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    askSpeechInput();
                }
            });

        }

        // Showing google speech input dialog - Date 19/12/2018

        private void askSpeechInput() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    "Hi speak something");
            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            } catch (ActivityNotFoundException a) {

            }
        }

        // Receiving speech input - Date 19/12/2018

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        voiceInput.setText(result.get(0));

                        if  (checkWriteExternalPermission());
                        {
                            generateNoteOnSD(result);
                        } ;

                        writeFileOnInternalStorage( MainActivity.this,result);

                    }
                    break;
                }

            }
        }

      // checking whether external writing permission

        private boolean checkWriteExternalPermission()
        {
            String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
            int res = MainActivity.this.checkCallingOrSelfPermission(permission);
           if (res == PackageManager.PERMISSION_GRANTED);
            {
                Log.d("ADebugTag", "Value: " + (res));

            }
           return(res == PackageManager.PERMISSION_GRANTED);
        }



        // Creating File in External Storage - Date 19/12/2018

        public void generateNoteOnSD(ArrayList result) {

                    StringBuilder sb = new StringBuilder();
                                  sb.append(result);
            Log.d("ADebugTag", "Value: " + (sb));


            try
            {

                File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());


                Log.d("ADebugTag", "Value: " + (root));
                if (!root.exists())
                {
                    root.mkdirs();
                }

                String str = "MOM_File_";
                String tonum = String.valueOf(number);
                str = str+number;

                File gpxfile = new File(root,str + ".txt");
                Log.d("ADebugTag", "Value: " + (gpxfile));
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(sb);
                writer.flush();
                number++;
                writer.close();
          //      Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
            }

            catch (IOException e)
            {
                e.printStackTrace();
            }

        }



     public void writeFileOnInternalStorage(Context mcoContext,ArrayList result)
        {

            StringBuilder sb = new StringBuilder();
            sb.append(result);
            Log.d("ADebugTag", "Value: " + (sb));

            String sFileName = "myfile.txt";

            FileOutputStream outputStream =null;



             File file = new File(mcoContext.getFilesDir(),"mydir");
            Log.d("ADebugTag 1", "Value: " + (file));

            try {
                outputStream = openFileOutput(sFileName, MODE_PRIVATE);
                outputStream.write(sb.getByte());
                Toasty.sucess(getApplication(), "file create success", Toast.LENGTH_LONG, true).show();
                }catch (IOException e) {
                 e.printStackTrace();
             } finally{
                    try{ outputStream.close();
                       }catch (IOException e) {
                        e.printStackTrace();
                    }



            if(!file.exists())
            {
                file.mkdirs();
             }

            try{
                File gpxfile = new File(file, sFileName);
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(sb);
                writer.flush();
                writer.close();
            }
            catch (Exception e)
            {
                    e.printStackTrace();

            }
        }




    }
