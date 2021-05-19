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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class Races extends AppCompatActivity {

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
        setContentView(R.layout.activity_races);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listItems.clear();
        ListView listView= findViewById(R.id.raceListView);
        setSupportActionBar(toolbar);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);
        String yearId = getIntent().getStringExtra("YEAR_ID");
        //System.out.println(yearId);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(getBaseContext(), ChooseResultType.class);
                //System.out.println(position);
                //System.out.println(listItems.get(position));
                intent.putExtra("YEAR_ID", yearId);
                intent.putExtra("ROUND_ID",Integer.toString(position+1));
                System.out.println("round: "+ Integer.toString(position+1));
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
                            .url("http://ergast.com/api/f1/"+yearId+"/results/?limit=1000")
                            .method("GET", null)
                            .build();
                    try{
                        ResponseBody responseBody = client.newCall(request).execute().body();
                        xmlAPI = responseBody.string();
                        //Log.d(TAG, "success " + xmlAPI);
                    }
                    catch(Exception e){Log.d(TAG, "err " + e);}
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try{
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(xmlAPI));
                    Document doc = builder.parse(is);

                    doc.getDocumentElement().normalize();

                    NodeList list = doc.getElementsByTagName("Race");

                    for (int temp =0; temp< list.getLength(); temp++)
                    {
                        Node node = list.item(temp);

                        if (node.getNodeType()==Node.ELEMENT_NODE){
                            Element element = (Element) node;
                            String round = element.getAttribute("round");

                            String gp = element.getElementsByTagName("RaceName").item(0).getTextContent();
                            //System.out.println("R" + round + ": " + gp);
                            listItems.add("R" + round + ": " + gp);
                        }
                        //listItems.add(node.getTextContent());
                        //System.out.println(listItems.get(temp));

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