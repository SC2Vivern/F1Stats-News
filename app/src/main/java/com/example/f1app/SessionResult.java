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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class SessionResult extends AppCompatActivity {

    private static final String TAG= "SessionResult";
    private static String xmlAPI= null;
    //private static String[] years = null;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private static ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private static ArrayAdapter<String> adapter;

    private static String choice = "a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listItems.clear();
        ListView listView= findViewById(R.id.sessionListView);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);

        String yearId = getIntent().getStringExtra("YEAR_ID");
        String roundId = getIntent().getStringExtra("ROUND_ID");
        String choiceId = getIntent().getStringExtra("CHOICE_ID");
        int yearCheck = Integer.parseInt(yearId);
        System.out.println(yearCheck);
        if (yearCheck < 2003 ){System.out.println("asdfff123");}
        if (choiceId.equals("R")){choice = "results";}
        else{choice="qualifying";}
        System.out.println(choice);


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://ergast.com/api/f1/"+yearId+"/"+ roundId +"/"+ choice + "/?limit=1000")
                            .method("GET", null)
                            .build();
                    try{
                        ResponseBody responseBody = client.newCall(request).execute().body();
                        xmlAPI = responseBody.string();
                        //Log.d(TAG, "success " + xmlAPI);
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

                    String nodeChoice = "a";
                    if (choiceId.equals("R"))
                    {
                        nodeChoice = "Result";
                    }
                    else
                        {
                            nodeChoice = "QualifyingResult";
                            if (yearCheck < 1994 || yearCheck == 2001 || yearCheck == 2002){
                                listItems.add("Quali results not available");
                                System.out.println("ffd");}
                        }
                    NodeList list = doc.getElementsByTagName(nodeChoice);


                    for (int temp =0; temp< list.getLength(); temp++)
                    {
                        Node node = list.item(temp);

                        if (node.getNodeType()==Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            NodeList childList = element.getElementsByTagName("Driver");
                            NodeList childList2 = element.getElementsByTagName("Status");
                            String name = "a";
                            String pos = element.getAttribute("position");
                            String pts = element.getAttribute("points");

                            for (int i = 0; i <childList.getLength(); i++)
                            {
                                Node childNode = childList.item(i);
                                if (node.getNodeType()==Node.ELEMENT_NODE)
                                {
                                    Element childElement = (Element) childNode;

                                        name = childElement.getElementsByTagName("GivenName").item(0).getTextContent() + " " + childElement.getElementsByTagName("FamilyName").item(0).getTextContent();



                                }

                            }

                            if(choiceId.equals("R")){
                            listItems.add("P" + pos + ", "+ name +", pts: "+pts);}
                            else{


                                    listItems.add("P" + pos + ", "+ name);


                            }

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