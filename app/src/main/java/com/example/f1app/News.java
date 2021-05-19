package com.example.f1app;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

public class News extends AppCompatActivity {

    private static String jsonAPI=null;
    private static String TAG="NEWS";

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private static ArrayList<String> listItems=new ArrayList<String>();
    private static ArrayList<String> listItems2=new ArrayList<String>();


    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listItems.clear();
        ListView listView= findViewById(R.id.newsListView);

        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String url = listItems2.get(position);

                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url("https://newsapi.org/v2/everything?q=F1&apiKey=54860061cb844bb3be65432e39bad464")
                            .method("GET", null)
                            .build();
                    try{
                        ResponseBody responseBody = client.newCall(request).execute().body();
                        jsonAPI = responseBody.string();
                        Log.d(TAG, "success " + jsonAPI);
                    }
                    catch(Exception e){
                        Log.d(TAG, "err " + e);}
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (jsonAPI != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonAPI);

                        // Getting JSON Array node
                        JSONArray articles = jsonObj.getJSONArray("articles");

                        // looping through All Contacts
                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject c = articles.getJSONObject(i);
                            String title = c.getString("title");
                            String url = c.getString("url");

                            listItems.add(title);
                            listItems2.add(url);

                        }
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
                }
            }
        });

        thread.start();
        try {
            thread.join();
        }
        catch(Exception e){System.out.println("err join: " + e);}

    }
}