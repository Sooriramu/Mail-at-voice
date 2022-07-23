package com.firstapp.speech_to_text1;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{
    private TextView edittext1,edittext2;
    DBHandler dbHandler;
    TextToSpeech tts;
    String s1,s2;
    Button button;
    AlertDialog.Builder builder;
    AlertDialog test;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        edittext1 = findViewById(R.id.edittext1);
        edittext2 = findViewById(R.id.edittext2);
        edittext1.setFocusable(false);
        edittext2.setFocusable(false);
        edittext1.setLongClickable(false);
        edittext2.setLongClickable(false);
        button = findViewById(R.id.button);
        button.setVisibility(View.INVISIBLE);

        dbHandler = new DBHandler(MainActivity.this);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR){
                    tts.setLanguage(Locale.US);
                    funstart();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg();
            }
        });
    }

    public void funstart(){
        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);
        if(firstStart){
            showStartDialog();
        }else{
            fun();
        }
    }

    private void showStartDialog(){
        builder=new AlertDialog.Builder(this);
        builder.setTitle("NOTICE");
        builder.setMessage("1. If app doesn't work properly check whether all required permissions are provided \n\n" +
                "2. The gmail id and password are stored only in database of your local device and so you can feel free to use this app.");

        test=builder.create();
        test.show();

        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putBoolean("firstStart",false);
        editor.apply();

        Runnable runnable = new Runnable(){
            public void run() {
                tmpfun();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void tmpfun(){
        talk("If app doesn't work properly check whether all required permissions are provided");
        sleep(5000);

        Runnable runnable = new Runnable(){
            public void run() {
                tmpfun2();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void tmpfun2(){
        talk("The G mail I D and password are stored only in database of your local device and so you can feel free to use this app");
        sleep(8000);
        test.dismiss();
        fun();
    }

    public void fun(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
        if (dbHandler.isuserpresent() == "1") {
            Runnable runnable = new Runnable(){
                public void run() {
                    String tmp=dbHandler.getusername();
                    tmp=tmp.replace(".","");
                    int t=tmp.length()*625;
                    tmp=tmp.replace("", " ... ").trim();
                    tmp="Logged user "+tmp;
                    talk(tmp);
                    sleep(2500+t);
                    tmpfun3();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }else {
            talk("Welcome user ... Provide your gmail I D ... ");
            sleep(3000);
            startActivityForResult(intent, 1);
        }
    }
    public void tmpfun3(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
        talk("Do you want to proceed or logout? ... ");
        sleep(2500);
        startActivityForResult(intent,3);
    }

    public void talk(String s){
        tts.speak(s,TextToSpeech.QUEUE_FLUSH,null,null);
    }

    public void sleep(long i){
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void fun1(){
        Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
        talk("Provide your password ... ");
        sleep(2500);
        startActivityForResult(intent, 2);
    }

    public void fun2(){
        String tmp= edittext1.getText().toString();
        int t=tmp.length()*600;
        tmp=tmp.replace(".","");
        tmp=tmp.replace("", " ... ").trim();
        talk("Recognized username is "+tmp);
        sleep(2500+t);

        tmp= edittext2.getText().toString();
        t=tmp.length()*600;
        tmp=tmp.replace("", " ... ").trim();
        talk("Recognized password is "+tmp);
        sleep(2500+t);

        talk("To edit manually say one ... to complete registration say two ... to clear and start over again say three ... ");
        sleep(7000);

        Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
        startActivityForResult(intent,4);
    }

    public void fun3(){
        if(s1.toLowerCase().equals("proceed")){
            Intent i = new Intent(MainActivity.this, Sendmail.class);
            i.putExtra("username",dbHandler.getusername());
            i.putExtra("pwd",dbHandler.getpwd());
            startActivity(i);
            this.finish();
        }else if(s1.toLowerCase().equals("logout") || (s1.toLowerCase().equals("log out"))){
            dbHandler.removeuser();
            fun();
        }else{
            Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

            talk("Invalid input ... Say proceed or logout");
            sleep(2500);
            startActivityForResult(intent,3);
        }
    }

    public void fun4(){
        s2=s2.replace("1","one");
        s2=s2.replace("2","two");
        s2=s2.replace("tu","two");
        s2=s2.replace("Tu","two");
        s2=s2.replace("3","three");
        if(s2.toLowerCase().equals("one")){
            edittext1.setFocusableInTouchMode(true);
            edittext2.setFocusableInTouchMode(true);
            button.setVisibility(View.VISIBLE);
        }else if(s2.toLowerCase().equals("two")){
            reg();
        }else if(s2.toLowerCase().equals("three")){
            edittext1.setText("");
            edittext2.setText("");
            fun();
        }else{
            talk("Invalid input ... Say one or two or three");
            sleep(3500);

            Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
            startActivityForResult(intent,4);
        }
    }

    public void reg(){
        dbHandler.adduser(edittext1.getText().toString(),edittext2.getText().toString());
        tts.speak("Registration successful",TextToSpeech.QUEUE_FLUSH,null,null);

        Intent i = new Intent(MainActivity.this, Sendmail.class);
        i.putExtra("username",dbHandler.getusername());
        i.putExtra("pwd",dbHandler.getpwd());
        startActivity(i);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if(requestCode==1){
            String tmp=Objects.requireNonNull(result).get(0);
            tmp=tmp.replace(" ","");
            tmp=tmp.replace("at","@");
            tmp=tmp.replace("8gmail","@gmail");
            edittext1.setText(tmp.toLowerCase());

            Runnable runnable = new Runnable(){
                public void run() {
                    fun1();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }else if(requestCode==2){
            String tmp=Objects.requireNonNull(result).get(0);
            tmp=tmp.replace(" ","");
            edittext2.setText(tmp);

            Runnable runnable = new Runnable(){
                public void run() {
                    fun2();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }else if(requestCode==3){
            s1=Objects.requireNonNull(result).get(0);
            fun3();
        }else if(requestCode==4){
            s2=Objects.requireNonNull(result).get(0);
            fun4();
        }
    }
}