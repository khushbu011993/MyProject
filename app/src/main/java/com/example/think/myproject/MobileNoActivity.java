package com.example.think.myproject;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MobileNoActivity extends AppCompatActivity {
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://mycity.skskup.com/WebService3.asmx"; //?op=insert_data
    private final String SOAP_ACTION = "http://tempuri.org/insert_data";
    private final String METHOD_NAME = "insert_data";
    EditText editmobile;
    Button mobilesubmit;
    private String TAG = "PGGURU";
    TextView tv;
    private static String mobilestoreinput;
    private static String mobileresponse;
    SharedPreferences.Editor editor;
    SharedPreferences preferences ;
    private static int i;
    Boolean isFirstTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_no);
        tv = (TextView) findViewById(R.id.tv_result);
        editmobile = (EditText) findViewById(R.id.editmobile);
        mobilesubmit = (Button) findViewById(R.id.mobilesubmit);
        mobilesubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("TEXT", 0);
                //Boolean  isFirstTime = preferences.getBoolean("isFirstTime", true);
                editor = preferences.edit();

                String regexStr = "^[0-9]$";

                mobilestoreinput = editmobile.getText().toString();

                editmobile.setText("");

                AsyncCallWS task = new AsyncCallWS();
                //Call execute
                task.execute();
              /*  if (editmobile.getText().toString().length() <10) {

                    Toast.makeText(getApplicationContext(),
                            "Please enter your valid  mobile number", Toast.LENGTH_LONG)
                            .show();

                }*/

            }
        });
    }
    public void insert_data(String mobilestoreinput) {
        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //Property which holds input parameters
        PropertyInfo value = new PropertyInfo();
        //Set Name
        value.setName("mobile");
        //Set Value
        value.setValue(mobilestoreinput);//
        // Set dataType
        value.setType(String .class);
        //  Add the property to request object
        request.addProperty(value);

        // request.addProperty("mobile", mobilestoreinput);

        //Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        //Set output SOAP object
        envelope.setOutputSoapObject(request);
        //Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            //Invole web service
            androidHttpTransport.call(SOAP_ACTION, envelope);
            //Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            //Assign it to fahren static variable
            mobileresponse = response.toString();
            i=Integer.parseInt(response.toString());
            if(i>0)
            {

                editor = preferences.edit();

                editor.putString("otpKey", "true");
                editor.putString("DATA", mobilestoreinput);
                editor.commit();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            insert_data(mobilestoreinput);

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            tv.setText(mobileresponse + "plz enter valid number");
            if(mobileresponse.equals("1")) {
                Toast.makeText(getBaseContext(), "Mobile Number Registered Successfully", Toast.LENGTH_LONG).show();
                Intent userPage=new Intent(MobileNoActivity.this,OTPActivity.class);
                startActivity(userPage);
            }
        }
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            tv.setText("Sending...");

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mobile_no, menu);
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
