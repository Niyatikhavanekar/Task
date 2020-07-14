package com.example.apiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.google.gson.stream.JsonReader;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    TextView data;
    Button but;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = (TextView) findViewById(R.id.textView1);
        tv = (TextView) findViewById(R.id.textView2);
        but = (Button) findViewById(R.id.button);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PlaceOp().execute();

            }
        });

    }

    private class PlaceOp
         extends AsyncTask<Void, Void, String> {


            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL url = new URL("https://jsonplaceholder.typicode.com/posts");
                    String postRequest = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    URLConnection con = url.openConnection();
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(postRequest.getBytes());
                    outputStream.flush();

                    InputStream inStream = con.getInputStream();
                    InputStreamReader inReader = new InputStreamReader(inStream);
                    BufferedReader br = new BufferedReader(inReader);
                    String line ;

                    String results = "";
                    while ((line = br.readLine()) != null) {
                        results = results + line + "\n";
                    }
                    Gson gson = new Gson();
                } catch (MalformedURLException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String results) {
                super.onPostExecute(results);
                if (!results.equals("")) {
                    JsonReader reader = new JsonReader(new StringReader(results.trim()));
                    reader.setLenient(true);
                    JsonElement resultTree = new JsonParser().parse(reader);
                    JsonObject responseJsonObject = null;
                    if (resultTree.isJsonObject()) {
                        responseJsonObject = resultTree.getAsJsonObject();
                    }

                    if (responseJsonObject.get("result").getAsBoolean() == true) {
                        JsonArray jobj = (JsonArray) responseJsonObject.get("data");
                        for (int i = 0; i < jobj.size(); i++) {
                            JsonObject responseJson = (JsonObject) jobj.get(i);
                            String dta = (String) responseJson.get("username").getAsString();
                            tv.setText(dta);
                            Toast.makeText(getApplicationContext(),"Response displayed",Toast.LENGTH_SHORT).show();
                        }

                    }


                } else {
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                }
            }
        }


        public void execute() {
        }
    }





