package com.example.f1app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import okhttp3.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent newsFeedIntent = new Intent(getApplicationContext(), News.class);
                startActivity(newsFeedIntent);
            }
        });
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button2 Clicked");

                Intent twitterFeedIntent = new Intent(getApplicationContext(), Twitter.class);
                startActivity(twitterFeedIntent);
            }
        });
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button3 Clicked");

                Intent statsIntent = new Intent(getApplicationContext(), Stats.class);
                startActivity(statsIntent);
            }
        });
        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button4 Clicked");

                Intent calendarIntent = new Intent(getApplicationContext(), F1Calendar.class);
                startActivity(calendarIntent);
            }
        });
        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button5 Clicked");

                Intent resultsIntent = new Intent(getApplicationContext(), Results.class);
                startActivity(resultsIntent);


            }
        });
        Button button7 = findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button7 Clicked");

                Intent standingsIntent = new Intent(getApplicationContext(), StandingsYear.class);
                startActivity(standingsIntent);


            }
        });

    }
}