package com.example.prototype1;

import android.media.MediaDrm;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.bouncycastle.crypto.digests.Blake2bDigest;
import org.bouncycastle.crypto.digests.Blake2sDigest;
import org.kocakosm.jblake2.Blake2b;
import org.kocakosm.jblake2.Blake2s;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import io.whitfin.siphash.SipHasher;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText editText;
    TextView textViewMd5;
    TextView textViewFnv;
    TextView textViewSipHash;
    TextView textViewBlake2;
    TextView textViewBlake2Digest;
    TextView textViewDrmId;
    String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.answer1);
        textViewMd5 = findViewById(R.id.textViewMd5);
        textViewFnv = findViewById(R.id.textViewFnv);
        textViewSipHash = findViewById(R.id.textViewSipHash);
        textViewBlake2 = findViewById(R.id.textViewBlake2);
        textViewBlake2Digest = findViewById(R.id.textViewBlake2Digest);
        textViewDrmId = findViewById(R.id.textViewDrmId);

        textViewMd5.setTextIsSelectable(true);
        textViewFnv.setTextIsSelectable(true);
        textViewSipHash.setTextIsSelectable(true);
        textViewBlake2.setTextIsSelectable(true);
        textViewBlake2Digest.setTextIsSelectable(true);
        textViewDrmId.setTextIsSelectable(true);

        button = findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    string = editText.getText().toString();
                    getUniqueID();
                    toHash();
                    toFnv();
                    toSipHash();
                    toBlake2();
                    toBlake2Digest();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toBlake2Digest() {
        Blake2bDigest blake2bDigest = new Blake2bDigest(8);
        blake2bDigest.reset();
        blake2bDigest.update(string.getBytes(),0,string.length());
        //String digest1 = new BigInteger(1, blake2bDigest.).toString(16);

        Blake2sDigest blake2sDigest = new Blake2sDigest(8);
        blake2sDigest.reset();
        blake2sDigest.update(string.getBytes(),0,string.length());
        //textViewBlake2Digest.setText("blake2bDigest " + digest1.length() + " " + digest1+" blake2s " + digest2.length() + " " + digest2);

    }

    private void toBlake2() {
        Blake2b blake2b = new Blake2b(64);
        blake2b.reset();
        blake2b.update(string.getBytes());
        String digest1 = new BigInteger(1, blake2b.digest()).toString(16);//new String(blake2b.digest());

        Blake2s blake2s = new Blake2s(8);
        blake2s.reset();
        blake2s.update(string.getBytes());
        String digest2 = new BigInteger(1, blake2s.digest()).toString(16);

        textViewBlake2.setText("blake2b " + digest1.length() + " " + digest1+" blake2s " + digest2.length() + " " + digest2);

    }

    void getUniqueID() {
        UUID wideVineUuid = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);
        try {
            MediaDrm mediaDrm = new MediaDrm(wideVineUuid);
            byte[] wideVineId = mediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
            Blake2b blake2b = new Blake2b(64);
            blake2b.reset();
            blake2b.update(wideVineId);
            String digest1 = new BigInteger(1, blake2b.digest()).toString(16);//new String(blake2b.digest());

            Blake2s blake2s = new Blake2s(8);
            blake2s.reset();
            blake2s.update(wideVineId);
            String digest2 = new BigInteger(1, blake2s.digest()).toString(16);

            //textViewDrmId.setText("DRM id blake2s " + digest2.length() + " " + digest2);
            textViewDrmId.setText("DRM id " + digest1.length() + " " + digest1+" blake2s " + digest2.length() + " " + digest2);
            //textViewDrmId.setText("DRM id " + digest1);
            //textViewDrmId.setText("DRM id " + Base64.encodeToString(wideVineId,Base64.DEFAULT).trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toSipHash() {
        long sipHasher = SipHasher.hash("0123456789ABCDEF".getBytes(), string.getBytes(), 2, 4);
        textViewSipHash.setText("SipHasher " + sipHasher);
    }

    private void toFnv() {
        FNV fnv = new FNV();
        textViewFnv.setText("FNV 64 bit " + fnv.fnv1a_32(string.getBytes()));
    }

    private void toHash() throws NoSuchAlgorithmException {
        Log.v("EditText", string);
        MessageDigest msg = MessageDigest.getInstance("MD5");
        msg.update(string.getBytes(), 0, string.length());
        String digest1 = new BigInteger(1, msg.digest()).toString(16).substring(0, 16);
        textViewMd5.setText("MD5 substring(0, 16) " + digest1);

    }
}
