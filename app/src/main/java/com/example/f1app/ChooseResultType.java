package com.example.f1app;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ChooseResultType extends AppCompatActivity {

    private static ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private static ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_result_type);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ListView listView= findViewById(R.id.choiceListView);

        listItems.clear();
        setSupportActionBar(toolbar);

        String yearId = getIntent().getStringExtra("YEAR_ID");
        String roundId = getIntent().getStringExtra("ROUND_ID");
        listItems.add("Race");
        listItems.add("Qualifying");
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(getBaseContext(), SessionResult.class);
                //System.out.println(position);
                //System.out.println(listItems.get(position));
                intent.putExtra("YEAR_ID", yearId);
                intent.putExtra("ROUND_ID",roundId);
                intent.putExtra("CHOICE_ID", listItems.get(position).substring(0,1));
                startActivity(intent);

            }
        });


    }

}