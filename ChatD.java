package com.minipro.starcsimul;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChatD extends AppCompatActivity {
    private void CheckPermission () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (! (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText (getApplicationContext(),"Please Grant Permission To Record Audio In Settings",Toast.LENGTH_LONG).show();
            }
        }
    }
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference reference1, reference2,reference3;
    ChildEventListener listener1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deaf);

        CheckPermission();

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.getApplicationContext());

        final Intent mSpeechRecognizerIntent = new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        mSpeechRecognizerIntent.putExtra (RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra (RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null)
                {
                    String messageText = matches.get(0);
                    if (!messageText.equals("")) {

                        Map<String, String> map = new HashMap<String, String>();

                        map.put("message", messageText);
                        map.put("user", UserDetails.username);
                        //addMessageBox(UserDetails.username + " :  " + messageText, 1);
                        //addMessageBox(UserDetails.username + " : " + messageText, 2);
                        reference1.push().setValue(map);
                        reference2.push().setValue(map);
                        //messageArea.setText("");
                        mSpeechRecognizer.stopListening();
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    }

                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout) findViewById(R.id.layout2);
        //sendButton = (ImageView) findViewById(R.id.sendButton);
        //messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        //String path = "messages/" + UserDetails.username + "_" + UserDetails.chatWith;

        reference1 = FirebaseDatabase.getInstance().getReference("messages").child(UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = FirebaseDatabase.getInstance().getReference("messages").child(UserDetails.chatWith + "_" + UserDetails.username);


        /*sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();


                if (!messageText.equals("")) {

                    Map<String, String> map = new HashMap<String, String>();

                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    //addMessageBox(UserDetails.username + " :  " + messageText, 1);
                    //addMessageBox(UserDetails.username + " : " + messageText, 2);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });*/

        ChildEventListener listener1 = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = (Map) dataSnapshot.getValue();

                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(!(userName.equals(UserDetails.username))){
                    addMessageBox(message, 1);
                }
                String msg=message.toString();
                int l=msg.length();
                int factor = l/15;
                factor=(factor*1000)+1000;


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSpeechRecognizer.stopListening();
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

                    }
                }, factor);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference1.addChildEventListener(listener1);
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        mSpeechRecognizer.stopListening();




        /*reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)  {
                Map map = dataSnapshot.getValue(Map.class);

                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UserDetails.username)){
                    addMessageBox("You:-\n" + message, 1);
                    Toast.makeText(Chat.this,message,Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(Chat.this,message,Toast.LENGTH_LONG).show ();
                    addMessageBox(UserDetails.chatWith + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Chat.this,"wtf is dis error",Toast.LENGTH_LONG).show ();

            }
        });*/
    }



    public void addMessageBox (String message,int type){
        TextView textView = new TextView(ChatD.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.START;

        } else {
            lp2.gravity = Gravity.END;

        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }


}
