package com.example.fusan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class AllFaces extends AppCompatActivity {

    ListView faceList;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_faces);

        faceList = findViewById(R.id.faceList);
        done = findViewById(R.id.done);

        Bundle extras = getIntent().getExtras();

        final ArrayList<String> faces = extras.getStringArrayList("faces");

        final ArrayList<Integer> times = extras.getIntegerArrayList("times");

        ArrayList<String> persons = new ArrayList<String>();


        //Populating People Array
        ArrayList<Person> people = new ArrayList<Person>();

        for(int i = 0; i< times.size(); i++){
            String face = faces.get(i);
            String person = "";
            if(i==0){
                person = "Suhas Gumma";
            }
            if(i == 1){
                person = "Navaneeth Nanda";
            }

            if(i == 2){
                person = "Siva Prakash";
            }

            if (i == 3){
                person = "Pranay Srinivas";
            }

            if(i > 3){
                person = "Person " + Integer.toString(i+1);
            }
            String t = Integer.toString(times.get(i));
            String time = "";
            if(t.equals("1")){
                time = t + " Second";
            }
            else{
                time = t + " Seconds";
            }


            Person newPerson = new Person(face,person,time);

            people.add(newPerson);
        }

        FaceAdapter faceAdapter = new FaceAdapter(this, people);

        faceList.setAdapter(faceAdapter);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent);
            }
        });

    }
}