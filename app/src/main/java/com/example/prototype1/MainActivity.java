package com.example.prototype1;

import android.media.MediaDrm;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import io.whitfin.siphash.SipHasher;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText editText;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.answer1);
        textView1 = findViewById(R.id.question1Header);
        textView2 = findViewById(R.id.question2Header);
        textView3 = findViewById(R.id.question3Header);
        textView4 = findViewById(R.id.question4Header);

        textView1.setTextIsSelectable(true);
        textView2.setTextIsSelectable(true);
        textView3.setTextIsSelectable(true);
        textView4.setTextIsSelectable(true);

        button = findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    string = editText.getText().toString();
                    getUniqueID();
                    toHash();
                    toFnv();
                    toSipHash();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void getUniqueID() {
        UUID wideVineUuid = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);
        try {
            MediaDrm mediaDrm = new MediaDrm(wideVineUuid);
            byte[] wideVineId = mediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
            //MessageDigest md = MessageDigest.getInstance("SHA-256");
            //md.update(wideVineId);
            //textView4.setText("DRM id " + md.digest().toString());

            textView4.setText("DRM id " + Base64.encodeToString(wideVineId,Base64.DEFAULT).trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toSipHash() {
        long sipHasher = SipHasher.hash("0123456789ABCDEF".getBytes(), string.getBytes(), 2, 4);
        textView3.setText("SipHasher " + sipHasher);
    }

    private void toFnv() {
        FNV fnv = new FNV();
        textView2.setText("FNV 64 bit " + fnv.fnv1a_32(string.getBytes()));
    }

    private void toHash() throws NoSuchAlgorithmException {
        Log.v("EditText", string);
        MessageDigest msg = MessageDigest.getInstance("MD5");
        msg.update(string.getBytes(), 0, string.length());
        String digest1 = new BigInteger(1, msg.digest()).toString(16).substring(0, 16);
        textView1.setText("MD5 substring(0, 16) " + digest1);

    }
}
