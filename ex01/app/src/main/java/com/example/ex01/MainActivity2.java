package com.example.ex01;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {
    Button btnDecrease, btnIncrease;
    TextView count;
    int intCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setTitle("Maple Story");
        btnDecrease = findViewById(R.id.btn1);
        btnIncrease = findViewById(R.id.btn2);
        count = findViewById(R.id.count);

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intCount--;
                count.setText("현재값 : " + intCount);
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intCount++;
                count.setText("현재값 : " + intCount);
            }
        });

        btnDecrease.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                intCount -= 100;
                count.setText("현재값 : " + intCount);
                return true;
            }
        });

        btnIncrease.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                intCount += 100;
                count.setText("현재값 : " + intCount);
                return true;
            }
        });
    } // onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.all) {
            Toast.makeText(this, "전체월드 캐릭터", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == R.id.skania) {
            Toast.makeText(this, "스카니아 캐릭터", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == R.id.luna) {
            Toast.makeText(this, "루나 캐릭터", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == R.id.reboot) {
            Toast.makeText(this, "리부트 캐릭터", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == R.id.jenis) {
            Toast.makeText(this, "제니스 캐릭터", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == R.id.croa) {
            Toast.makeText(this, "크로아 캐릭터", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}