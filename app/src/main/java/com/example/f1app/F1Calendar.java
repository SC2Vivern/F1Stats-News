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

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import android.widget.TextView;

public class F1Calendar extends AppCompatActivity {

    private static final String TAG= "CalendarDisplay";
    private static String xmlAPI= null;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private static ArrayList<String> listItems=new ArrayList<String>();
    private static boolean checkDate = false;
    private static int datePos = 0;


    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private static ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar cal = Calendar.getInstance();
        Date dat= cal.getTime();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(dat);
        TextView editDate= findViewById(R.id.textView25);
        editDate.setText(strDate);

        listItems.clear();
        ListView listView= findViewById(R.id.calendarListView);

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
                            .url("http://ergast.com/api/f1/current")
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

                    NodeList list = doc.getElementsByTagName("Race");

                    for (int temp =0; temp< list.getLength(); temp++)
                    {
                        Node node = list.item(temp);

                        if (node.getNodeType()==Node.ELEMENT_NODE) {
                            Element element = (Element) node;

                            String round = element.getAttribute("round");
                            String name = element.getElementsByTagName("RaceName").item(0).getTextContent();
                            String date = element.getElementsByTagName("Date").item(0).getTextContent();
                            Date d= null;
                            try{
                            d = dateFormat.parse(date);}
                            catch(Exception e){System.out.println("date err: " + e);}



                            listItems.add("R" + round + ": "+ date +", "+name);
                            if (checkDate == false){
                                if(d.before(dat)){
                                    datePos++;
                                }else{checkDate=true;}

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
        TextView tv = findViewById(R.id.textView24);
        tv.setText(listItems.get(datePos));
    }
}