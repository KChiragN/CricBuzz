package com.example.cricbuzz;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class HomeFragment extends Fragment {
    View parentholder;
    String url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentholder=inflater.inflate(R.layout.fragment_home,container,false);
        ListView listView=parentholder.findViewById(R.id.listView);
        url = "https://api.cricapi.com/v1/cricScore?apikey=061428d7-7ba1-4d29-9d25-1ca6211ed5ae";
        ArrayList<String> namearraylist=new ArrayList<>();
        ArrayList<String> idarraylist=new ArrayList<>();
        StringRequest jsonObjectRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public  void onResponse(String response){
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();
                    JSONArray array = new JSONObject(response).getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String team1 = jsonObject.getString("t1");
                        String team2 = jsonObject.getString("t2");
                        String t1s = jsonObject.getString("t1s");
                        String t2s = jsonObject.getString("t2s");
                        String datetime = jsonObject.getString("dateTimeGMT");
                        Date matchDate = dateFormat.parse(datetime);
                        calendar.setTime(matchDate);
                        boolean sameDay = calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) && calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);// convert the date string to a Date object
                        String status = jsonObject.getString("status");
                        String team = team1 + "vs" + team2 + "\n\n" +status+"\n\n"+ t1s + " " + t2s + "\n\n" + datetime;
                        if (sameDay){
                            namearraylist.add(team);
                            idarraylist.add(id);
                    }}
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, namearraylist);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                            try {
                                String id1 = idarraylist.get(pos);
                                Intent intent = new Intent(getContext(), MainActivity1.class);
                                intent.putExtra("id", id1);
                                Log.d("MainActivity1", "ID: " + id1);
                                Log.d("MainActivity1", "Intent: " + intent.getExtras());
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
        return parentholder;
    }
}