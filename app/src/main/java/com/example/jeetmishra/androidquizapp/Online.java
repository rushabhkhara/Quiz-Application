package com.example.jeetmishra.androidquizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jeetmishra.androidquizapp.Common1.Common;
import com.example.jeetmishra.androidquizapp.Model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Collections;

public class Online extends AppCompatActivity {
    Button onlinePlay;
    Calendar calendar;
    long endtime;

    long currenttime,time;
    FirebaseDatabase database;
    DatabaseReference questions,Time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");
        onlinePlay = (Button) findViewById(R.id.OnlinePlay);
        calendar = Calendar.getInstance();


        Time = database.getReference("Time").child("Online Time");
        loadQuestion(Common.gameModesNo);
        Time.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String stringtime = dataSnapshot.getValue(String.class);
                time = Long.parseLong(stringtime);
                Log.i("Time in log",String.valueOf(time));
                Toast.makeText(Online.this, String.valueOf(time), Toast.LENGTH_SHORT).show();

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
     // DatabaseReference time_id = Time.child("Time").child("Online Time");

          //9:10pm
      // time = (Integer.parseInt(stringtime))




        onlinePlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Log.i("Time in log",String.valueOf(time));
                //Toast.makeText(Online.this, "Online Play : "+String.valueOf(time), Toast.LENGTH_SHORT).show();
                endtime = time + 30000;
               // Toast.makeText(Online.this, "Online Endtime : "+String.valueOf(endtime), Toast.LENGTH_SHORT).show();
                //Toast.makeText(Online.this, "Online CurrentTime : "+String.valueOf(calendar.getTimeInMillis()), Toast.LENGTH_SHORT).show();

                if(calendar.getTimeInMillis()>=time&&calendar.getTimeInMillis()<=endtime)
                {

                        Intent intent = new Intent(Online.this, OnlinePlaying.class);
                        startActivity(intent);
                        finish();



                }
                else
                {




                     if(calendar.getTimeInMillis()<time) {
                        Toast.makeText(Online.this, "The game has not started yet!", Toast.LENGTH_SHORT).show();
                    }
                    else if(calendar.getTimeInMillis()>endtime)
                    {
                        Toast.makeText(Online.this, "The game has ended!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    private void loadQuestion(String gameModesNo) {
        if (Common.questionList.size()>0)
            Common.questionList.clear();
        questions.orderByChild("gameModesNo").equalTo(gameModesNo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Question ques = postSnapshot.getValue(Question.class);
                    Common.questionList.add(ques);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    }

