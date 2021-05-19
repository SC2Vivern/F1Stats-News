package com.example.f1app;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;


import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class StandingsDisplay extends AppCompatActivity {

    private static final String TAG= "StandingsDisplay";
    private static String xmlAPI= null;
    private static String choice= "a";
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private static ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listItems.clear();
        ListView listView= findViewById(R.id.standingsListView);
        String yearId = getIntent().getStringExtra("YEAR_ID");
        System.out.println(yearId);
        String choiceId = getIntent().getStringExtra("CHOICE_ID");
        System.out.println(choiceId);

        if (choiceId.equals("D")){choice = "Driver";}
        else{choice = "Constructor";}


        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://ergast.com/api/f1/"+yearId+"/"+choice+"Standings?limit=80")
                            .method("GET", null)
                            .build();
                    try{
                        ResponseBody responseBody = client.newCall(request).execute().body();
                        xmlAPI = responseBody.string();
                        Log.d(TAG, "success " + xmlAPI);
                    }
                    catch(Exception e){
                        Log.d(TAG, "err " + e);}
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try{
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(xmlAPI));
                    Document doc = builder.parse(is);

                    doc.getDocumentElement().normalize();

                    NodeList list = doc.getElementsByTagName(choice + "Standing");

                    for (int temp =0; temp< list.getLength(); temp++)
                    {
                        Node node = list.item(temp);

                        if (node.getNodeType()==Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            NodeList childList = element.getElementsByTagName(choice);
                            String name = "a";

                            for (int i = 0; i <childList.getLength(); i++)
                            {
                                Node childNode = childList.item(i);
                                if (node.getNodeType()==Node.ELEMENT_NODE)
                                {
                                    Element childElement = (Element) childNode;
                                    if (choiceId.equals("D"))
                                    {
                                        name = childElement.getElementsByTagName("GivenName").item(0).getTextContent() + " " + childElement.getElementsByTagName("FamilyName").item(0).getTextContent();
                                    }
                                    else
                                        {
                                            name = childElement.getElementsByTagName("Name").item(0).getTextContent();
                                        }


                                }

                            }
                            String pos = element.getAttribute("position");
                            String pts = element.getAttribute("points");
                            listItems.add("Pos: " + pos + ", "+ name +", pts: "+pts);

                        }

                    }
                }
                catch(ParserConfigurationException | SAXException | IOException e){Log.d(TAG, "err2: " + e);}

            }
        });

        thread.start();
        try {
            thread.join();
        }
        catch(Exception e){System.out.println("err join: " + e);}

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }
}