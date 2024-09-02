package com.example.ex01;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView characterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Maple Story");

        characterImageView = findViewById(R.id.character);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit1 = findViewById(R.id.edit1);
                String str = edit1.getText().toString();
                Toast.makeText(MainActivity.this, "어서오세요! " + str + "님", Toast.LENGTH_SHORT).show();
            }
        });
    } // onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int imageResourceId = 0;

        if (item.getItemId() == R.id.all) {
            imageResourceId = R.drawable.all_character;
        } else if (item.getItemId() == R.id.skania) {
            imageResourceId = R.drawable.skania_character;
        } else if (item.getItemId() == R.id.luna) {
            imageResourceId = R.drawable.luna_character;
        } else {
            return super.onOptionsItemSelected(item);
        }

        characterImageView.setImageResource(imageResourceId);
        characterImageView.setTag(imageResourceId);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Object tag = characterImageView.getTag();
        int currentImageId = (tag != null) ? (Integer) tag : R.drawable.totoro;

        if (currentImageId == R.drawable.all_character) {
            menu.findItem(R.id.all).setChecked(true);
        } else if (currentImageId == R.drawable.skania_character) {
            menu.findItem(R.id.skania).setChecked(true);
        } else if (currentImageId == R.drawable.luna_character) {
            menu.findItem(R.id.luna).setChecked(true);
        } else {
            menu.findItem(R.id.all).setChecked(false);
            menu.findItem(R.id.skania).setChecked(false);
            menu.findItem(R.id.luna).setChecked(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }
}