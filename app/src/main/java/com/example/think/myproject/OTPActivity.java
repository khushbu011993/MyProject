package com.example.think.myproject;

import android.content.Intent;
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
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class OTPActivity extends AppCompatActivity {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://mycity.skskup.com/WebService3.asmx"; //?op=insert_data
    private final String SOAP_ACTION = "http://tempuri.org/login";
    private final String METHOD_NAME = "login";

    EditText editmobile;
    Button mobilesubmit;
    private String TAG = "PGGURU";
    TextView tv,setmobileno;
    private static String mobilestoreinput;
    private static String mobileresponse;
    private static String getMobilestoreinput1;
    private static int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        String kk=getSharedPreferences("TEXT", 0).getString("DATA", null);

        tv = (TextView) findViewById(R.id.tv_result);
        editmobile = (EditText) findViewById(R.id.editmobile);
        setmobileno=(TextView)findViewById(R.id.setmobileno);
        mobilesubmit = (Button) findViewById(R.id.mobilesubmit);
        setmobileno.setText(kk);
        mobilesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getMobilestoreinput1=setmobileno.getText().toString();
                if (editmobile.getText().length() != 0 && editmobile.getText().toString() != "") {
                mobilestoreinput = editmobile.getText().toString();
                editmobile.setText("");
                AsyncCallWS task = new AsyncCallWS();
                //Call execute
                task.execute();
                } else {
                    tv.setText("Please enter OTP Number");
                }
            }
        });
    }
    public void insert_data(String getMobilestoreinput1,String mobilestoreinput) {
        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //Property which holds input parameters
    /*    PropertyInfo celsiusPI = new PropertyInfo();
        //Set Name
        celsiusPI.setName("mobile");
        //Set Value
        celsiusPI.setValue(mobilestoreinput);//
        // Set dataType
        celsiusPI.setType(String .class);
        //  Add the property to request object
        request.addProperty(celsiusPI);*/
        request.addProperty("mobile", getMobilestoreinput1);
         request.addProperty("otp", mobilestoreinput);


        //Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            insert_data(getMobilestoreinput1,mobilestoreinput);

            return null;
        }

       /* private void getvalue(String username, String useremail) {
        }*/

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            tv.setText(mobileresponse + "Submitted");
           if(mobileresponse.equals("1")) {
            Toast.makeText(getBaseContext(), "OTP Registered Successfully", Toast.LENGTH_LONG).show();
            Intent userPage=new Intent(OTPActivity.this,MainActivity.class);
            startActivity(userPage);
            }
            else
           {
               Toast.makeText(getBaseContext(), "Enter valid five digit numeric number", Toast.LENGTH_LONG).show();

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
        getMenuInflater().inflate(R.menu.menu_ot, menu);
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
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent startMain=new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
