package com.example.prototype1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText editText;
    TextView textView1;
    TextView textView2;

    String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.answer1);
        textView1 = findViewById(R.id.question1Header);
        textView2 = findViewById(R.id.question2Header);

        button = findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    string = editText.getText().toString();
                    toHash();
                    toFnv();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toFnv() {
        FNV fnv = new FNV();
        textView2.setText("FNV 64 bit"+fnv.fnv1a_64(string.getBytes()));
    }

    private void toHash() throws NoSuchAlgorithmException {
        Log.v("EditText", string);
        MessageDigest msg = MessageDigest.getInstance("MD5");
        msg.update(string.getBytes(), 0, string.length());
        String digest1 = new BigInteger(1, msg.digest()).toString(16).substring(0, 16);
        textView1.setText("MD5 substring(0, 16) "+digest1);

    }
}
