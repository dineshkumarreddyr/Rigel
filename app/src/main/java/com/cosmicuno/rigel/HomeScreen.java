package com.cosmicuno.rigel;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HomeScreen extends Activity {

    Button login;
    private static String USER_AGENT = "Mozilla/5.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }

        login = (Button) findViewById(R.id.user_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser(){
        StringBuffer strBuffer = null;
        String url = "http://192.168.223.1:2015/api/v1/rigel/auth";
        EditText username = (EditText)findViewById(R.id.user_name);
        EditText password = (EditText)findViewById(R.id.user_password);
        String uname = username.getText().toString();
        String upass = password.getText().toString();

        String Params = "username="+uname+"&password="+upass;
        strBuffer = loginUser(url,Params);

        Intent intent = new Intent(this,Bookings.class);
        startActivity(intent);
    }

    private static StringBuffer loginUser(String connectionString,String params){
        OutputStream outStream = null;
        int responseCode = 0;
        StringBuffer response = null;
        try{
            URL url = new URL(connectionString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.connect();
            outStream = con.getOutputStream();
            outStream.write(params.getBytes());
            outStream.flush();
            outStream.close();

            responseCode = con.getResponseCode();

            if(responseCode==HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                response = new StringBuffer();
                while((inputLine=in.readLine())!=null){
                    response.append(inputLine);
                }
                in.close();

                //Response - Object will have output
            }
        }
        catch (MalformedURLException m){
            m.printStackTrace();
        }
        catch (IOException i){
            i.printStackTrace();
        }
        return response;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
