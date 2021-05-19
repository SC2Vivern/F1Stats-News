package com.example.f1app;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

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

public class StatComparator extends AppCompatActivity {

    private static final String TAG= "StatCompare";
    private static String xmlAPI= null;
    //private static String[] years = null;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private static ArrayList<String> listItems=new ArrayList<String>();
    private static ArrayList<String> listItems2=new ArrayList<String>();


    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private static ArrayAdapter<String> adapter;
    private static ArrayAdapter<String> adapter2;
    private static String choice = "a";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_comparator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listItems.clear();
        listItems2.clear();
        Spinner spinner1= findViewById(R.id.spinner);
        Spinner spinner2= findViewById(R.id.spinner2);
        String choiceId = getIntent().getStringExtra("CHOICE_ID");
        if (choiceId.equals("D")){choice = "drivers";}
        else{choice="constructors";}
        System.out.println(choice);


        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter);
        adapter2=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(adapter2);

        Button button = findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), StatDisplay.class);


                int d1 = spinner1.getSelectedItemPosition();
                int d2 = spinner2.getSelectedItemPosition();

                intent.putExtra("DRIVER1_ID", listItems2.get(d1));
                intent.putExtra("DRIVER2_ID", listItems2.get(d2));
                intent.putExtra("CHOICE_ID", choiceId);
                intent.putExtra("DRIVER1_NAME", spinner1.getSelectedItem().toString());
                intent.putExtra("DRIVER2_NAME", spinner2.getSelectedItem().toString());





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
                            .url("http://ergast.com/api/f1/"+choice+"?limit=1000")
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
                    String tempChoice = "a";
                    if (choiceId.equals("D")){tempChoice = "Driver";}
                    else{tempChoice="Constructor";}

                    NodeList list = doc.getElementsByTagName(tempChoice);

                    for (int temp =0; temp< list.getLength(); temp++)
                    {
                        Node node = list.item(temp);
                        String name = "a";

                        if (node.getNodeType()==Node.ELEMENT_NODE){
                            Element element = (Element) node;
                            if (choiceId.equals("D"))
                            {
                                name = element.getElementsByTagName("FamilyName").item(0).getTextContent() + ", " + element.getElementsByTagName("GivenName").item(0).getTextContent();
                                listItems2.add(element.getAttribute("driverId"));
                            }
                            else
                            {
                                name = element.getElementsByTagName("Name").item(0).getTextContent();
                                listItems2.add(element.getAttribute("constructorId"));

                            }
                            listItems.add(name);

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
        adapter2.notifyDataSetChanged();
    }
}