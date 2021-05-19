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

public class Stats extends AppCompatActivity {

    private static final String TAG= "Races";
    private static String xmlAPI= null;
    //private static String[] years = null;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private static ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private static ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listItems.clear();
        ListView listView= findViewById(R.id.statisticChoiceListView);



        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);
        listItems.add("Driver stats");
        listItems.add("Constructor stats");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(getBaseContext(), StatComparator.class);
                //System.out.println(position);
                //System.out.println(listItems.get(position));
                intent.putExtra("CHOICE_ID", listItems.get(position).substring(0,1));

                startActivity(intent);

            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }
}