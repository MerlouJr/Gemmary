package com.example.cosain.gemmary;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    String dataUrl = "http://dev.teslasuite.com:8080/stocksbranch/api/stocksanalytics.asp?";
    String username;
    String password;
    String spin;

    URL url;
    HttpURLConnection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void login(View view) {
        EditText text = (EditText) findViewById(R.id.editText);
        EditText text1 = (EditText) findViewById(R.id.editText2);
        EditText text2 = (EditText) findViewById(R.id.editText3);

        if (text.getText().toString().isEmpty()) {

            Toast.makeText(this, "Please enter username!", Toast.LENGTH_LONG).show();
        } else if (text1.getText().toString().isEmpty()) {

            Toast.makeText(this, "Please enter password!", Toast.LENGTH_LONG).show();

        } else if (text2.getText().toString().isEmpty()) {

            Toast.makeText(this, "Please enter SPIN!", Toast.LENGTH_LONG).show();
        } else {

            username = text.getText().toString();
            password = text1.getText().toString();
            spin = text2.getText().toString();

            new  login().execute();
    }
    }
    //Region THREADS
  private class login extends AsyncTask<Void,Void,String>{



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection connection = null;

            dataUrl = dataUrl + "cmd=cmdLogin";
            dataUrl  = dataUrl + "&Username="+username;
            dataUrl = dataUrl + "&Password="+password;
            dataUrl = dataUrl + "&SPIN="+spin;
            Log.d("Data", "" + dataUrl);

            try{

                URL url = new URL(dataUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(
                connection.getOutputStream());
                wr.writeBytes(dataUrl);
                wr.flush();
                wr.close();

                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                String responseStr = response.toString();
                Log.d("Server response",responseStr);

            }
            catch(MalformedURLException error) {

                error.printStackTrace();
                //Handles an incorrectly entered URL
            }
            catch(SocketTimeoutException error) {
            //Handles URL access timeout.
                error.printStackTrace();
            }
            catch (Exception e){

                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    //End Region
}
