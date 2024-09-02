package com.example.ex04;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class InsertActivity extends AppCompatActivity {
    AddressHelper helper;
    SQLiteDatabase db;
    EditText name, juso, phone;
    CircleImageView photo;
    String strPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert);
        getSupportActionBar().setTitle("주소등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = findViewById(R.id.name);
        juso = findViewById(R.id.juso);
        phone = findViewById(R.id.phone);
        photo = findViewById(R.id.photo);

        helper = new AddressHelper(this);
        db = helper.getWritableDatabase();
        findViewById(R.id.btnInsert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName = name.getText().toString();
                String strJuso = juso.getText().toString();
                String strPhone = phone.getText().toString();

                if(strName.equals("") || strJuso.equals("") || strPhone.equals("")) {
                    Toast.makeText(InsertActivity.this, "모든 내용을 입력하십시오!", Toast.LENGTH_SHORT).show();
                } else{
                    String sql = "insert into address(name, juso, phone, photo) values(";
                    sql += "'" + strName + "', ";
                    sql += "'" + strJuso + "', ";
                    sql += "'" + strPhone + "', ";
                    sql += "'" + strPhoto + "')";
                    db.execSQL(sql);
                    finish();
                }
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityResult.launch(intent);
            }
        });
    }
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() == RESULT_OK) {
                        Cursor cursor = getContentResolver().query(o.getData().getData(), null, null, null, null);
                        cursor.moveToFirst();
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        strPhoto = cursor.getString(index);
                        photo.setImageBitmap(BitmapFactory.decodeFile(strPhoto));
                        cursor.close();
                    }
                }
            }
    );

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}