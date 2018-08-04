package com.example.webviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

//        WebView webView = (WebView)findViewById ( R.id.web_view );
//        webView.getSettings ().setJavaScriptEnabled ( true );
//        webView.setWebViewClient ( new WebViewClient () );
//        webView.loadUrl ( "http://www.baidu.com" );

        Button sendRequest = (Button)findViewById ( R.id.send_request );
        responseText = (TextView)findViewById ( R.id.response_text );
        sendRequest.setOnClickListener ( this );
    }

    @Override
    public void onClick(View v){
        if(v.getId () == R.id.send_request){
            sendRequestWithHttpURLConnection ();
        }
    }

    private void sendRequestWithHttpURLConnection(){
        //开启一个线程发起网络请求
//        new Thread ( new Runnable () {
//            @Override
//            public void run() {
//                HttpURLConnection httpURLConnection = null;
//                BufferedReader reader = null;
//
//                try{
//
//                    URL url = new URL ( "http://www.baidu.com" );
//                    httpURLConnection = (HttpURLConnection)url.openConnection ();
//                    httpURLConnection.setRequestMethod ( "GET" );
//                    httpURLConnection.setConnectTimeout ( 8000 );
//                    httpURLConnection.setReadTimeout ( 8000 );
//
//                    InputStream in = httpURLConnection.getInputStream ();
//                    reader = new BufferedReader ( new InputStreamReader ( in ) );
//
//                    StringBuilder response = new StringBuilder ( );
//                    String line;
//                    while((line = reader.readLine ()) != null){
//                        response.append ( line );
//                    }
//                    showResponse(response.toString ());
//
//                }catch(Exception e){
//                    e.printStackTrace ();
//                }finally {
//                    if(reader != null){
//                        try{
//                            reader.close ();
//                        }catch(IOException e){
//                            e.printStackTrace ();
//                        }
//                    }
//
//                    if(httpURLConnection != null){
//                        httpURLConnection.disconnect ();
//                    }
//                }
//            }
//        } ).start ();

//        new Thread ( new Runnable () {
//            @Override
//            public void run() {
//                try{
//                    OkHttpClient client = new OkHttpClient ();
//                    Request request = new Request.Builder ().url ( "http://www.baidu.com" ).build ();
//                    Response response = client.newCall ( request ).execute ();
//                    String responseData = response.body ().string ();
//                    showResponse ( responseData );
//                }catch (Exception e){
//                    e.printStackTrace ();
//                }
//            }
//        } ).start ();

        new Thread ( new Runnable () {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient ();
                    Request request = new Request.Builder ().url ( "http://localhost:8080/xml/get_data.xml" ).build ();
                    Response response = client.newCall ( request ).execute ();
                    String responseData = response.body ().string ();
                    parseXMLWithSAX ( responseData );
                }catch (Exception e){
                    e.printStackTrace ();
                }
            }
        } ).start ();
    }

    private void showResponse(final String response){
        runOnUiThread ( new Runnable () {
            @Override
            public void run() {
                responseText.setText ( response );
            }
        } );
    }

//    private void parseXMLWithPull(String xmlData){
//        try{
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance ();
//            XmlPullParser xmlPullParser = factory.newPullParser ();
//            xmlPullParser.setInput ( new StringReader ( xmlData ) );
//            int eventType = xmlPullParser.getEventType ();
//
//            String id = "";
//            String name = "";
//            String version = "";
//
//            while (eventType != XmlPullParser.END_DOCUMENT){
//                String nodeName = xmlPullParser.getName ();
//                switch (eventType){
//                    //开始解析某个节点
//                    case XmlPullParser.START_TAG:{
//                        if("id".equals ( nodeName )){
//                            id = xmlPullParser.nextText ();
//                        }else if("name".equals ( nodeName )){
//                            name = xmlPullParser.nextText ();
//                        }else if("version".equals ( nodeName )){
//                            version = xmlPullParser.nextText ();
//                        }
//                        break;
//                    }
//
//                    //完成解析某个节点
//                    case XmlPullParser.END_TAG:{
//                        if("app".equals ( nodeName )){
//                            Log.d ( "MainActivity","id is"+id );
//                            Log.d ( "MainActivity","name is"+name );
//                            Log.d ( "MainActivity","version is"+version );
//                        }
//                        break;
//                    }
//                    default:
//                        break;
//                }
//                eventType = xmlPullParser.next ();
//            }
//        }catch(Exception e){
//            e.printStackTrace ();
//        }
//    }

    private void parseXMLWithSAX(String xmlData){
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance ();
            XMLReader xmlReader = factory.newSAXParser ().getXMLReader ();
            ContentHandler handler = new ContentHandler ();
            xmlReader.setContentHandler ( handler);

            //开始执行解析
            xmlReader.parse ( new InputSource ( new StringReader ( xmlData ) ) );
        }catch (Exception e){
            e.printStackTrace ();
        }
    }
}
