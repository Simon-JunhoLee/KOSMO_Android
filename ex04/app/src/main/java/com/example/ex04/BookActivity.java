package com.example.ex04;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class BookActivity extends AppCompatActivity {
    ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>();
    BookAdapter adapter = new BookAdapter();
    String query="안드로이드";
    int page=1;
    boolean is_end=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book);
        getSupportActionBar().setTitle("도서 검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new BookThread().execute();
        ListView list=findViewById(R.id.list);
        list.setAdapter(adapter);

        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!is_end) {
                    page +=1;
                    new BookThread().execute();
                }else{
                    Toast.makeText(BookActivity.this, "마지막 페이지입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    } // onCreate

    class BookThread extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String url="https://dapi.kakao.com/v3/search/book?target=title&query=" + query + "&page=" + page;
            String result=KakaoAPI.connect(url);
            Log.i("result", result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            bookParser(s);
            adapter.notifyDataSetChanged();
        }

        public void bookParser(String result) {
            try {
                JSONObject meta = new JSONObject(result).getJSONObject("meta");
                is_end = meta.getBoolean("is_end");
                JSONArray jArray = new JSONObject(result).getJSONArray("documents");
                for(int i=0; i<jArray.length(); i++) {
                    JSONObject obj = jArray.getJSONObject(i);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("title", obj.getString("title"));
                    map.put("image", obj.getString("thumbnail"));
                    map.put("price", obj.getInt("price"));
                    map.put("contents", obj.getString("contents"));
                    map.put("publisher", obj.getString("publisher"));
                    map.put("authors", obj.getString("authors"));
                    array.add(map);
                }
            }catch (Exception e) {
                System.out.println("파싱오류 : " + e.toString());
            }
        }
    } // BookThread

    class BookAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return array.size();
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
            convertView=getLayoutInflater().inflate(R.layout.item_book, parent, false);
            HashMap<String, Object> map=array.get(i);
            TextView title=convertView.findViewById(R.id.title);
            title.setText(map.get("title").toString());
            int intPrice=Integer.parseInt(map.get("price").toString());
            DecimalFormat df=new DecimalFormat("#,###원");
            TextView price=convertView.findViewById(R.id.price);
            price.setText(df.format(intPrice));
            TextView authors=convertView.findViewById(R.id.authors);
            authors.setText(map.get("authors").toString());
            ImageView image=convertView.findViewById(R.id.image);
            String strImage=map.get("image").toString();
            if(strImage.equals("")){
                image.setImageResource(R.drawable.no_image);
            }else {
                Picasso.with(BookActivity.this)
                        .load(map.get("image").toString())
                        .into(image);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View detail = (View)View.inflate(BookActivity.this, R.layout.detail_book, null);
                    TextView title=detail.findViewById(R.id.title);
                    title.setText(map.get("title").toString());
                    int intPrice=Integer.parseInt(map.get("price").toString());
                    DecimalFormat df=new DecimalFormat("#,###원");
                    TextView price=detail.findViewById(R.id.price);
                    price.setText(df.format(intPrice));
                    TextView authors=detail.findViewById(R.id.authors);
                    authors.setText(map.get("authors").toString());
                    TextView contents=detail.findViewById(R.id.contents);
                    contents.setText(map.get("contents").toString());
                    ImageView image=detail.findViewById(R.id.image);
                    String strImage=map.get("image").toString();
                    if(strImage.equals("")){
                        image.setImageResource(R.drawable.no_image);
                    }else {
                        Picasso.with(BookActivity.this).load(map.get("image").toString()).into(image);
                    }
                    AlertDialog.Builder box = new AlertDialog.Builder(BookActivity.this);
                            box.setTitle("도서정보");
                            box.setView(detail);
                            box.setPositiveButton("확인", null);
                            box.show();
                }
            });
            return convertView;
        }
    } // BookAdapter

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kakao, menu);

        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query = s;
                page = 1;
                array.clear();
                new BookThread().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
} // BookActivity