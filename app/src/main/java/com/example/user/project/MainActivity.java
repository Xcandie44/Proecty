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

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity{
    private TextView mInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInfoTextView = (TextView) findViewById(R.id.textView);
    }

    public void onClick(View view) {
        new send().execute();
    }



    class send extends AsyncTask<Object,Object,StringBuilder> {
        HttpsURLConnection connection = null;
        StringBuilder sb = new StringBuilder();
        String query;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            query="https://api.binance.com/api/v3/ticker/price";
            mInfoTextView.setText("Начало");
        }

        @Override
        protected StringBuilder doInBackground(Object... objects) {
            try{
                URL url = new URL(query);
                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.connect();
                if (HttpsURLConnection.HTTP_OK == connection.getResponseCode()){
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while((line = in.readLine())!= null);
                    sb.append(line);
                    sb.append("\n");
                }
            }catch (Throwable cause){
                cause.printStackTrace();
                mInfoTextView.setText("Ошибка");
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return sb;
        }

        @Override
        protected void onPostExecute(StringBuilder result) {
            super.onPostExecute(result);
            mInfoTextView.setText("Конец");
            mInfoTextView.setText(sb.toString());
        }
    }
}
