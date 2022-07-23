package com.firstapp.speech_to_text1;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class Sendmail extends AppCompatActivity{
    EditText etTo,etSubject,etMessage;
    Button btSend;
    String sEmail,sPassword,s1;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmail);
        etTo=findViewById(R.id.et_to);
        etSubject=findViewById(R.id.et_subject);
        etMessage=findViewById(R.id.et_message);

        etTo.setFocusable(false);
        etSubject.setFocusable(false);
        etMessage.setFocusable(false);
        etTo.setLongClickable(false);
        etMessage.setLongClickable(false);
        etSubject.setLongClickable(false);

        btSend=findViewById(R.id.bt_send);
        btSend.setVisibility(View.INVISIBLE);

        Bundle extras=getIntent().getExtras();
        sEmail=extras.getString("username");
        sPassword=extras.getString("pwd");

        btSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fun5();
            }
        });

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR){
                    tts.setLanguage(Locale.US);
                    fun();
                }
            }
        });
    }

    public void talk(String s){
        tts.speak(s, TextToSpeech.QUEUE_FLUSH,null,null);
    }

    public void sleep(long i){
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void fun(){
        talk("Provide receiver gmail address");
        sleep(2000);

        Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
        startActivityForResult(intent,1);
    }

    public void fun1(){
        talk("Provide subject for the mail ... ");
        sleep(2500);

        Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
        startActivityForResult(intent, 2);
    }

    public void fun2(){
        talk("Provide body of the mail ... ");
        sleep(2500);

        Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
        startActivityForResult(intent, 3);
    }

    public void fun3(){
        String tmp= etTo.getText().toString();
        int t=tmp.length()*600;
        tmp=tmp.replace(".","");
        tmp=tmp.replace("", " ... ").trim();
        talk("Recognized receiver address "+tmp);
        sleep(2000+t);

        tmp= etSubject.getText().toString();
        StringTokenizer tokens = new StringTokenizer(tmp);
        t=tokens.countTokens()*750;
        tmp=tmp.replace(" ", " ... ").trim();
        talk("Recognized subject is "+tmp);
        sleep(2000+t);

        tmp= etMessage.getText().toString();
        tokens = new StringTokenizer(tmp);
        t=tokens.countTokens()*750;
        tmp=tmp.replace(" ", " ... ").trim();
        talk("Recognized message is "+tmp);
        sleep(2000+t);

        talk("To edit manually say one ... to send mail say two ... to clear and start over again say three ... ");
        sleep(7000);

        Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
        startActivityForResult(intent,4);
    }

    public void fun4(){
        s1=s1.replace("1","one");
        s1=s1.replace("2","two");
        s1=s1.replace("3","three");
        if(s1.toLowerCase().equals("one")){
            etTo.setFocusableInTouchMode(true);
            etSubject.setFocusableInTouchMode(true);
            etMessage.setFocusableInTouchMode(true);
            btSend.setVisibility(View.VISIBLE);
        }else if(s1.toLowerCase().equals("two") || s1.toLowerCase().equals("tu")){
            fun5();
        }else if(s1.toLowerCase().equals("three")){
            etTo.setText("");
            etSubject.setText("");
            etMessage.setText("");
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

    public void fun5(){
        Properties properties=new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        Session session= Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sEmail,sPassword);
            }
        });
        try {
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(sEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(etTo.getText().toString().trim()));
            message.setSubject(etSubject.getText().toString().trim());
            message.setText(etMessage.getText().toString().trim());
            new SendMail().execute(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void fun6(){
        talk("Mail sent successfully ... ");
        sleep(1500);
        finishAndRemoveTask();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if(requestCode==1){
            String tmp= Objects.requireNonNull(result).get(0);
            tmp=tmp.replace(" ","");
            tmp=tmp.replace("at","@");
            tmp=tmp.replace("8gmail","@gmail");
            etTo.setText(tmp.toLowerCase());

            Runnable runnable = new Runnable(){
                public void run() {
                    fun1();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }else if(requestCode==2){
            String tmp=Objects.requireNonNull(result).get(0);
            tmp=tmp.substring(0,1).toUpperCase()+tmp.substring(1);
            etSubject.setText(tmp);

            Runnable runnable = new Runnable(){
                public void run() {
                    fun2();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }else if(requestCode==3){
            String tmp=Objects.requireNonNull(result).get(0);
            tmp=tmp.substring(0,1).toUpperCase()+tmp.substring(1);
            etMessage.setText(tmp);

            Runnable runnable = new Runnable(){
                public void run() {
                    fun3();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }else if(requestCode==4){
            s1=Objects.requireNonNull(result).get(0);
            fun4();
        }
    }

    private class SendMail extends AsyncTask<Message,String,String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(Sendmail.this,"Please Wait","Sending Mail...",true,false);
            talk("Sending mail");
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            etTo.setText("");
            etSubject.setText("");
            etMessage.setText("");
            if(s.equals("Success")){
                fun6();
            }else{
                talk("Something went wrong ... try again ");
                sleep(2000);
                fun();
            }
        }
    }
}