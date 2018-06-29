package com.example.administrator.volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{

    public static final String REQUEST_TAG = "STRING_REQUEST_TAG";
    public static final String JSON_URL = "https://tutorialwing.com/api/tutorialwing_posts.json";
    ListView lv;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        lv = (ListView) findViewById(R.id.list);

        Button btnsend = (Button) findViewById(R.id.get_request);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {

        final ProgressDialog dialog=new ProgressDialog(MainActivity.this);

        StringRequest stringRequest = new StringRequest(JSON_URL,new Response.Listener<String>()
        {
                    @Override
                    public void onResponse(String response)
                    {
                        showResponse(response);
                        dialog.dismiss();
                    }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, REQUEST_TAG);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showResponse(String response) {

        ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

        try
        {

            JSONArray jsonArray= new JSONArray(response);

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject c = jsonArray.getJSONObject(i);

                String name = c.getString("name");
                String title = c.getString("title");
                String category = c.getString("category");
                String url = c.getString("url");

                HashMap<String, String> contact = new HashMap<>();
                contact.put("name", name);
                contact.put("title", title);
                contact.put("category", category);
                contact.put("url", url);

                contactList.add(contact);

                ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
                        R.layout.list_item, new String[]
                        { "name","title","category","url"},
                        new int[]{R.id.txtname, R.id.txttitle, R.id.txtcategory, R.id.txturl});
                lv.setAdapter(adapter);
            }

        }
        catch (Exception er)
        {
            Toast.makeText(getApplicationContext(),er.toString(),Toast.LENGTH_LONG).show();
        }

    }

}
