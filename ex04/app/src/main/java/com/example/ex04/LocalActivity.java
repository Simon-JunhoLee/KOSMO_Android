package com.example.ex04;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LocalActivity extends AppCompatActivity {

    String query="가산디지털";
    int page=1;
    boolean is_end=false;
    JSONArray array = new JSONArray();
    LocalAdapter adapter = new LocalAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_local);

        getSupportActionBar().setTitle("지역검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new LocalThread().execute();
        ListView list=findViewById(R.id.list);
        list.setAdapter(adapter);
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!is_end) {
                    page +=1;
                    new LocalThread().execute();
                }else{
                    Toast.makeText(LocalActivity.this, "마지막 페이지입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    } // onCreate

    class LocalThread extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String url="https://dapi.kakao.com/v2/local/search/keyword.json?query=" + query + "&page=" + page;
            String result=KakaoAPI.connect(url);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                array = new JSONObject(s).getJSONArray("documents");
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            super.onPostExecute(s);
        }
    } // KakaoThread

    class LocalAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return array.length();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_local, parent, false);

            try {
                JSONObject obj = array.getJSONObject(i);
                String strName = obj.getString("place_name");
                String strPhone = obj.getString("phone");
                String strAddress = obj.getString("address_name");
                String strX = obj.getString("x");
                String strY = obj.getString("y");
                TextView name = convertView.findViewById(R.id.name);
                name.setText(strName);
                TextView phone = convertView.findViewById(R.id.phone);
                phone.setText(strPhone);
                TextView address = convertView.findViewById(R.id.address);
                address.setText(strAddress);
                ImageView local = convertView.findViewById(R.id.local);
                local.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LocalActivity.this, MapsActivity.class);
                        intent.putExtra("x", strX);
                        intent.putExtra("y", strY);
                        intent.putExtra("name", strName);
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return convertView;
        }
    } // LocalAdapter

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }  //onOptionsItemSelected

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kakao, menu);
        SearchView searchView=(SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String str) {
                query=str;
                page=1;
                new LocalThread().execute();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }  //onCreateOptionsMenu

} // LocalActivity