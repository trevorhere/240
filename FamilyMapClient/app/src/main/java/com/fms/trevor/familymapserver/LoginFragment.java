package com.fms.trevor.familymapserver;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fms.trevor.familymapserver.model.Main;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;



//NOTES: lot of duplicate code in this file,  HTTP Connections need to be
// combined into HTTPost activity


public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        final EditText etServerHost = (EditText) view.findViewById(R.id.etServerHost);
        final EditText etServerPort = (EditText) view.findViewById(R.id.etServerPort);
        final EditText etUserName = (EditText)   view.findViewById(R.id.etUserName);
        final EditText etPassword = (EditText)   view.findViewById(R.id.etPassword);
        final EditText etFirstName = (EditText)  view.findViewById(R.id.etFirstName);
        final EditText etLastName = (EditText)   view.findViewById(R.id.etLastName);
        final EditText etEmail = (EditText)      view.findViewById(R.id.etEmail);
        final Button bRegister = (Button)        view.findViewById(R.id.bRegister);
        final Button bLogin = (Button)           view.findViewById(R.id.bLogin);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                final String serverHost =  etServerHost.getText().toString();
                final String serverPort =  etServerPort.getText().toString();
                final String userName   =  etUserName.getText().toString();
                final String password   =  etPassword.getText().toString();
                final String firstName  =  etFirstName.getText().toString();
                final String lastName   =  etLastName.getText().toString();
                final String email      =  etEmail.getText().toString();


                //    RegisterRequest registerRequest = new RegisterRequest(serverHost, serverPort, userName, password, firstName, lastName, email );


                Log.e("MSG", "Starting post req");
                Map<String, String> postData = new HashMap<>();
                postData.put("userName", userName);
                postData.put("password", password);
                postData.put("firstName", firstName);
                postData.put("lastName", lastName);
                postData.put("email", email);
                postData.put("IP", serverHost);
                postData.put("port", serverPort);
                Register task = new Register(postData);
                task.execute("http://" + serverHost + ":" + serverPort );
                Log.e(TAG, "Ending post req");

            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final String serverHost =  etServerHost.getText().toString();
                final String serverPort =  etServerPort.getText().toString();
                final String userName   =  etUserName.getText().toString();
                final String password   =  etPassword.getText().toString();

                Log.e("MSG", "Starting post req");
                Map<String, String> postData = new HashMap<>();


                //Testing data
//                postData.put("userName", "sheila");// userName);
//                postData.put("password", "parker" ); //password);
//                postData.put("IP", "192.168.43.82"); // serverHost);
//                postData.put("port", "3000" ); //serverPort);
                  postData.put("userName", userName);
                  postData.put("password", password);
                  postData.put("IP", serverHost);
                  postData.put("port", serverPort);


                Login task = new Login(postData);
                task.execute("http://" + serverHost + ":" + serverPort );
                //Testing data
               // task.execute("http://192.168.43.82:3000");
                Log.e(TAG, "Ending post req");
            }
        });



        return view;
    }


    public class Register extends AsyncTask<String, Void, Register.Wrapper>
    {
        JSONObject postData;
        String urlString;

        public Register(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        public class Wrapper
        {
            public String message = "empty";
            public Map<String, String > getData = null;
        }
        @Override
        protected Wrapper doInBackground(String... params) {

            try {
                System.setProperty("http.proxyHost", "proxy.example.com");
                System.setProperty("http.proxyPort", "8080");
                urlString = params[0];
                URL url = new URL(urlString + "/user/register");
                Log.e("URL", urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Authorization", "someAuthString");
                if (this.postData != null) {
                    OutputStream os = urlConnection.getOutputStream();   //   new OutputStreamWriter(urlConnection.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(postData.toString());
                    writer.flush();
                    writer.close();
                    os.close();

                    urlConnection.connect();
                }
                int statusCode = urlConnection.getResponseCode();
                if (statusCode ==  200)
                {
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                    String response = stringify(inputStream);
                    Log.e("IS STRING", response);
                    JSONObject json = new JSONObject(response);
                    Register.Wrapper w = new Register.Wrapper();

                    if(json.has("Message"))
                    {
                        String error = json.getString("Message");
                        w.message = error;
                        return w;
                    }
                    else
                    {
                        Map<String, String> getData = new HashMap<>();
                        String personId = json.getString("personId");
                        String authToken = json.getString("authToken");
                        String userName = json.getString("userName");
                        getData.put("personID", personId);
                        getData.put("authToken", authToken);
                        getData.put("userName", userName);
                        MainActivity.mMain.currentUserPersonID = personId;

                        w.getData = getData;
                        return w;
                    }
                }
                else
                {
                    Log.d(TAG, "error in httpPostAsyncTask do in Background");
                }

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return null;
        }

        protected void onPostExecute(Register.Wrapper w) {

            if(w.message.equals("empty"))
            {
                try
                {
                    Main.fetchData();
                    loginSuccessful();
                }
                catch (Exception e)
                {
                    Log.e(TAG, "issue with Register.java line 138");
                }

                loginSuccessful();

            }
            else
            {
                super.onPostExecute(w);
                Log.e(TAG, "Register didnt work");

                Toast.makeText(getActivity().getApplicationContext(),w.message,Toast.LENGTH_SHORT).show();

            }
        }


        private String stringify(InputStream is) {

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();

        }

    }

    public void loginSuccessful()
    {
        ((MainActivity)getActivity()).runMapFragment();
    }

    public class Login  extends AsyncTask<String, Void, Login.Wrapper>
    {
        JSONObject postData;
        String urlString;

        public Login(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        public class Wrapper
        {
            public String message = "empty";
            public Map<String, String > getData = null;
        }

        @Override
        protected Wrapper doInBackground(String... params) {

            try {

                System.setProperty("http.proxyHost", "proxy.example.com");
                System.setProperty("http.proxyPort", "8080");

                urlString = params[0];
                URL url = new URL(urlString + "/user/login");
                Log.e(TAG, urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Authorization", "someAuthString");
                if (this.postData != null) {
                    OutputStream os = urlConnection.getOutputStream();   //   new OutputStreamWriter(urlConnection.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(postData.toString());
                    writer.flush();
                    writer.close();
                    os.close();

                    urlConnection.connect();
                }



                int statusCode = urlConnection.getResponseCode();
                if (statusCode ==  200) {
                    InputStream inputStream = new
                            BufferedInputStream(urlConnection.getInputStream());

                    String response = stringify(inputStream);
                    Log.e("AUTH STRING", response);
                    JSONObject json = new JSONObject(response);
                    Wrapper w = new Wrapper();

                    if(json.has("Message"))
                    {
                        String error = json.getString("Message");
                        w.message = error;
                        return w;
                    }
                    else
                    {
                        Map<String, String> getData = new HashMap<>();
                        String personId = json.getString("personId");
                        String authToken = json.getString("authToken");
                        getData.put("personID", personId);
                        getData.put("authToken", authToken);

                        MainActivity.mMain.authToken = authToken;
                        MainActivity.mMain.personID = personId;
                        MainActivity.mMain.url = urlString;
                        MainActivity.mMain.currentUserPersonID = personId;



                        w.getData = getData;
                        return w;
                    }
                }
                else
                {
                    Log.d(TAG, "error in httpPostAsyncTask do in Background");
                }

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return null;
        }

        protected void onPostExecute(Wrapper w) {

            if(w.message.equals("empty"))
            {
                Main.fetchData();
                loginSuccessful();
            }
            else
            {
                super.onPostExecute(w);
                Toast.makeText(getActivity().getApplicationContext(),w.message,
                        Toast.LENGTH_SHORT).show();
            }


        }


        private  String stringify(InputStream is) {

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();
        }

    }




}