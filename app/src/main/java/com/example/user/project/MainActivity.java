package com.example.user.project;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity{
    private TextView mInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInfoTextView = (TextView) findViewById(R.id.textView);
    }

    public void onClick(View view) {
        send send = new send();
        send.execute();
    }



    class send extends AsyncTask<Object,Object,StringBuilder> implements com.example.user.project.send {
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();
        String query = "https://api.binance.com/api/v3/ticker/price";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mInfoTextView.setText("Начало");
        }

        @Override
        protected StringBuilder doInBackground(Object... objects) {
            try{
                connection = (HttpURLConnection) new URL(query).openConnection();
                connection.setRequestMethod("GET");
                connection.setUseCaches(false);
                connection.setConnectTimeout(250);
                connection.setReadTimeout(250);
                connection.connect();
                if (HttpURLConnection.HTTP_OK == connection.getResponseCode()){
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while((line = in.readLine())!=null);
                    sb.append(line);
                    sb.append("\n");
                }
            }catch (Throwable cause){
                cause.printStackTrace();
            }finally {
                if(connection!=null){
                    connection.disconnect();
                }
            }
            return sb;
        }

        @Override
        protected void onPostExecute(StringBuilder result) {
            super.onPostExecute(result);
            mInfoTextView.setText(sb.toString());
        }
    }
}
