package com.example.f1app;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import android.view.View;

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

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StatDisplay extends AppCompatActivity {

    private static final String TAG= "StatDisplay";
    private static String xmlAPI= null;
    private static String xmlAPI2= null;
    private static String xmlAPI2_1= null;
    private static String xmlAPI2_2= null;

    private static String xmlAPI3= null;
    private static String xmlAPI4= null;
    private static String xmlAPI5= null;
    private static String xmlAPI5_2= null;
    private static String xmlAPI5_3= null;
    private static String xmlAPI6= null;



    private static String choice = "a";
    private static String choice2 = "a";
    private static int offset = 1000;

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String driver1 = getIntent().getStringExtra("DRIVER1_ID");
        String driver2 = getIntent().getStringExtra("DRIVER2_ID");
        String driver1name = getIntent().getStringExtra("DRIVER1_NAME");
        String driver2name = getIntent().getStringExtra("DRIVER2_NAME");

        String choiceId = getIntent().getStringExtra("CHOICE_ID");
        if (choiceId.equals("D")){choice = "drivers";
        choice2= "driver";}
        else{choice="constructors";
        choice2="constructor";}


        TextView driver1tv = (TextView)findViewById(R.id.textView20);
        driver1tv.setText(driver1name);
        TextView driver2tv = (TextView)findViewById(R.id.textView19);
        driver2tv.setText(driver2name);
        TextView d1champtv = (TextView)findViewById(R.id.textView17);
        TextView d2champtv = (TextView)findViewById(R.id.textView16);
        TextView d1wintv = (TextView)findViewById(R.id.textView13);
        TextView d2wintv = (TextView)findViewById(R.id.textView12);
        TextView d1poletv = (TextView)findViewById(R.id.textView11);
        TextView d2poletv = (TextView)findViewById(R.id.textView10);
        TextView d1pointstv = (TextView)findViewById(R.id.textView4);
        TextView d2pointstv = (TextView)findViewById(R.id.textView6);
        TextView d1podiumtv = (TextView)findViewById(R.id.textView5);
        TextView d2podiumtv = (TextView)findViewById(R.id.textView7);


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://ergast.com/api/f1/"+choice+"/"+driver1+"/"+choice2+"Standings?limit=1000")
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

                try  { for (int i = 0; i<3; i++){
                    int off = i*offset;
                    String offS= Integer.toString(off);
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://ergast.com/api/f1/"+choice+"/"+driver1+"/results?limit=1000&offset="+offS)
                            .method("GET", null)
                            .build();
                    try{
                        ResponseBody responseBody = client.newCall(request).execute().body();
                        if (i==0){
                        xmlAPI2 = responseBody.string();}
                        if (i==1){xmlAPI2_1 = responseBody.string();}
                        if (i==2){xmlAPI2_2 = responseBody.string();}
                        //Log.d(TAG, "success " + xmlAPI2);
                    }
                    catch(Exception e){
                        Log.d(TAG, "err " + e);}}
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try  {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://ergast.com/api/f1/"+choice+"/"+driver1+"/qualifying/1?limit=1000")
                            .method("GET", null)
                            .build();
                    try{
                        ResponseBody responseBody = client.newCall(request).execute().body();
                        xmlAPI3 = responseBody.string();
                        //Log.d(TAG, "success " + xmlAPI3);
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
                    if (choiceId.equals("D")){tempChoice = "DriverStanding";}
                    else{tempChoice="ConstructorStanding";}

                    NodeList list = doc.getElementsByTagName(tempChoice);
                    int count = 0;

                    for (int temp =0; temp< list.getLength(); temp++)
                    {
                        Node node = list.item(temp);
                        String pos = "a";

                        if (node.getNodeType()==Node.ELEMENT_NODE){
                            Element element = (Element) node;
                            pos = element.getAttribute("position");

                            if (pos.equals("1")){count++;}
                        }
                    }
                    d1champtv.setText(Integer.toString(count));
                }
                catch(ParserConfigurationException | SAXException | IOException e){Log.d(TAG, "err2: " + e);}

                try{
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(xmlAPI2));
                    Document doc = builder.parse(is);

                    doc.getDocumentElement().normalize();

                    DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder1 = factory1.newDocumentBuilder();
                    InputSource is1 = new InputSource(new StringReader(xmlAPI2_1));
                    Document doc1 = builder1.parse(is1);

                    doc1.getDocumentElement().normalize();

                    DocumentBuilderFactory factory2 = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder2 = factory2.newDocumentBuilder();
                    InputSource is2 = new InputSource(new StringReader(xmlAPI2_2));
                    Document doc2 = builder2.parse(is2);

                    doc2.getDocumentElement().normalize();


                    NodeList list = doc.getElementsByTagName("Result");
                    NodeList list1 = doc1.getElementsByTagName("Result");
                    NodeList list2 = doc2.getElementsByTagName("Result");

                    int countW = 0;
                    int countP = 0;
                    double countpts = 0;

                    for (int temp =0; temp< list.getLength(); temp++)
                    {
                        Node node = list.item(temp);
                        String pos = "a";


                        if (node.getNodeType()==Node.ELEMENT_NODE){
                            Element element = (Element) node;
                            pos = element.getAttribute("position");
                            countpts = countpts + Double.parseDouble(element.getAttribute("points"));

                            if (pos.equals("1")){countW++;}
                            if (pos.equals("2") || pos.equals("3")){countP++;}

                        }
                    }
                    for (int temp =0; temp< list1.getLength(); temp++)
                    {
                        Node node = list1.item(temp);
                        String pos = "a";


                        if (node.getNodeType()==Node.ELEMENT_NODE){
                            Element element = (Element) node;
                            pos = element.getAttribute("position");
                            countpts = countpts + Double.parseDouble(element.getAttribute("points"));

                            if (pos.equals("1")){countW++;}
                            if (pos.equals("2") || pos.equals("3")){countP++;}

                        }
                    }
                    for (int temp =0; temp< list2.getLength(); temp++)
                    {
                        Node node = list2.item(temp);
                        String pos = "a";


                        if (node.getNodeType()==Node.ELEMENT_NODE){
                            Element element = (Element) node;
                            pos = element.getAttribute("position");
                            countpts = countpts + Double.parseDouble(element.getAttribute("points"));

                            if (pos.equals("1")){countW++;}
                            if (pos.equals("2") || pos.equals("3")){countP++;}

                        }
                    }
                    d1wintv.setText(Integer.toString(countW));
                    d1podiumtv.setText(Integer.toString(countP+countW));
                    d1pointstv.setText(Double.toString(round(countpts,2)));
                }
                catch(ParserConfigurationException | SAXException | IOException e){Log.d(TAG, "err2: " + e);}

                try{
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(xmlAPI3));
                    Document doc = builder.parse(is);

                    doc.getDocumentElement().normalize();


                    NodeList list = doc.getElementsByTagName("QualifyingResult");
                    int count = 0;

                    for (int temp =0; temp< list.getLength(); temp++)
                    {
                        Node node = list.item(temp);
                        String pos = "a";

                        if (node.getNodeType()==Node.ELEMENT_NODE){
                            Element element = (Element) node;
                            pos = element.getAttribute("position");

                            if (pos.equals("1")){count++;}
                        }
                    }
                    d1poletv.setText(Integer.toString(count));
                }
                catch(ParserConfigurationException | SAXException | IOException e){Log.d(TAG, "err2: " + e);}




            }
        });

        thread.start();
        try {
            thread.join();
        }
        catch(Exception e){System.out.println("err join: " + e);}

        Thread thread2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://ergast.com/api/f1/"+choice+"/"+driver2+"/"+choice2+"Standings?limit=1000")
                            .method("GET", null)
                            .build();
                    try{
                        ResponseBody responseBody = client.newCall(request).execute().body();
                        xmlAPI4 = responseBody.string();
                        //Log.d(TAG, "success " + xmlAPI);
                    }
                    catch(Exception e){
                        Log.d(TAG, "err " + e);}
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try  { for (int i = 0; i<3; i++){
                    int off = i*offset;
                    String offS= Integer.toString(off);
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://ergast.com/api/f1/"+choice+"/"+driver2+"/results?limit=1000&offset="+offS)
                            .method("GET", null)
                            .build();
                    try{
                        ResponseBody responseBody = client.newCall(request).execute().body();
                        if (i==0){
                            xmlAPI5 = responseBody.string();}
                        if (i==1){xmlAPI5_2 = responseBody.string();}
                        if (i==2){xmlAPI5_3 = responseBody.string();}
                        //Log.d(TAG, "success " + xmlAPI2);
                    }
                    catch(Exception e){
                        Log.d(TAG, "err " + e);}}
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try  {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://ergast.com/api/f1/"+choice+"/"+driver2+"/qualifying/1?limit=5000")
                            .method("GET", null)
                            .build();
                    try{
                        ResponseBody responseBody = client.newCall(request).execute().body();
                        xmlAPI6 = responseBody.string();
                        //Log.d(TAG, "success " + xmlAPI3);
                    }
                    catch(Exception e){
                        Log.d(TAG, "err " + e);}
                } catch (Exception e) {
                    e.printStackTrace();
                }





                try{
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(xmlAPI4));
                    Document doc = builder.parse(is);

                    doc.getDocumentElement().normalize();
                    String tempChoice = "a";
                    if (choiceId.equals("D")){tempChoice = "DriverStanding";}
                    else{tempChoice="ConstructorStanding";}

                    NodeList list = doc.getElementsByTagName(tempChoice);
                    int count = 0;

                    for (int temp =0; temp< list.getLength(); temp++)
                    {
                        Node node = list.item(temp);
                        String pos = "a";

                        if (node.getNodeType()==Node.ELEMENT_NODE){
                            Element element = (Element) node;
                            pos = element.getAttribute("position");

                            if (pos.equals("1")){count++;}
                        }
                    }
                    d2champtv.setText(Integer.toString(count));
                }
                catch(ParserConfigurationException | SAXException | IOException e){Log.d(TAG, "err2: " + e);}

                try{
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(xmlAPI5));
                    Document doc = builder.parse(is);

                    doc.getDocumentElement().normalize();

                    DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder1 = factory1.newDocumentBuilder();
                    InputSource is1 = new InputSource(new StringReader(xmlAPI5_2));
                    Document doc1 = builder1.parse(is1);

                    doc1.getDocumentElement().normalize();

                    DocumentBuilderFactory factory2 = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder2 = factory2.newDocumentBuilder();
                    InputSource is2 = new InputSource(new StringReader(xmlAPI5_3));
                    Document doc2 = builder2.parse(is2);

                    doc2.getDocumentElement().normalize();


                    NodeList list = doc.getElementsByTagName("Result");
                    NodeList list1 = doc1.getElementsByTagName("Result");
                    NodeList list2 = doc2.getElementsByTagName("Result");

                    int countW = 0;
                    int countP = 0;
                    double countpts = 0;

                    for (int temp =0; temp< list.getLength(); temp++)
                    {
                        Node node = list.item(temp);
                        String pos = "a";


                        if (node.getNodeType()==Node.ELEMENT_NODE){
                            Element element = (Element) node;
                            pos = element.getAttribute("position");
                            countpts = countpts + Double.parseDouble(element.getAttribute("points"));

                            if (pos.equals("1")){countW++;}
                            if (pos.equals("2") || pos.equals("3")){countP++;}

                        }
                    }
                    for (int temp =0; temp< list1.getLength(); temp++)
                    {
                        Node node = list1.item(temp);
                        String pos = "a";


                        if (node.getNodeType()==Node.ELEMENT_NODE){
                            Element element = (Element) node;
                            pos = element.getAttribute("position");
                            countpts = countpts + Double.parseDouble(element.getAttribute("points"));

                            if (pos.equals("1")){countW++;}
                            if (pos.equals("2") || pos.equals("3")){countP++;}

                        }
                    }
                    for (int temp =0; temp< list2.getLength(); temp++)
                    {
                        Node node = list2.item(temp);
                        String pos = "a";


                        if (node.getNodeType()==Node.ELEMENT_NODE){
                            Element element = (Element) node;
                            pos = element.getAttribute("position");
                            countpts = countpts + Double.parseDouble(element.getAttribute("points"));

                            if (pos.equals("1")){countW++;}
                            if (pos.equals("2") || pos.equals("3")){countP++;}

                        }
                    }
                    d2wintv.setText(Integer.toString(countW));
                    d2podiumtv.setText(Integer.toString(countP+countW));
                    d2pointstv.setText(Double.toString(round(countpts,2)));
                }
                catch(ParserConfigurationException | SAXException | IOException e){Log.d(TAG, "err2: " + e);}

                try{
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(xmlAPI6));
                    Document doc = builder.parse(is);

                    doc.getDocumentElement().normalize();


                    NodeList list = doc.getElementsByTagName("QualifyingResult");
                    int count = 0;

                    for (int temp =0; temp< list.getLength(); temp++)
                    {
                        Node node = list.item(temp);
                        String pos = "a";

                        if (node.getNodeType()==Node.ELEMENT_NODE){
                            Element element = (Element) node;
                            pos = element.getAttribute("position");

                            if (pos.equals("1")){count++;}
                        }
                    }
                    d2poletv.setText(Integer.toString(count));
                }
                catch(ParserConfigurationException | SAXException | IOException e){Log.d(TAG, "err2: " + e);}




            }
        });

        thread2.start();
        try {
            thread2.join();
        }
        catch(Exception e){System.out.println("err join: " + e);}




    }
}