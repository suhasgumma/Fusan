package com.example.fusan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Base64;

public class most_appeared extends AppCompatActivity {

    ImageView mostAppeared;
    TextView mostApp, totalfaces;
    Button info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_appeared);

        Bundle extras = getIntent().getExtras();

        final ArrayList<String> faces = extras.getStringArrayList("faces");

        final ArrayList<Integer> times = extras.getIntegerArrayList("times");

        mostAppeared = findViewById(R.id.mostAppeared);
        mostApp = findViewById(R.id.appearedTime);
        totalfaces = findViewById(R.id.totalFaces);
        info = findViewById(R.id.infoB);

        Base64.Decoder decoder = Base64.getMimeDecoder();

        byte[] decoded = decoder.decode(faces.get(0));

        Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);

        mostAppeared.setImageBitmap(bitmap);

        final String mostAppearedTime = Integer.toString(times.get(0)) + " Seconds";
        String totalFaces = Integer.toString(times.size()) + " Faces Identified";

        mostApp.setText(mostAppearedTime);
        totalfaces.setText(totalFaces);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllFaces.class);
                intent.putStringArrayListExtra("faces", faces);
                intent.putIntegerArrayListExtra("times", times);
                startActivity(intent);
            }
        });
    }
}