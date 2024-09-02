package com.example.ex04;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateActivity extends AppCompatActivity {
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
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        name = findViewById(R.id.name);
        juso = findViewById(R.id.juso);
        phone = findViewById(R.id.phone);
        photo = findViewById(R.id.photo);

        helper = new AddressHelper(this);
        db = helper.getWritableDatabase();
        String sql = "select _id, name, juso, phone, photo from address where _id=" + id;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToNext()) {
            name.setText(cursor.getString(1));
            juso.setText(cursor.getString(2));
            phone.setText(cursor.getString(3));
            strPhoto = cursor.getString(4);

            if (strPhoto != null && !strPhoto.isEmpty()) {
                photo.setImageBitmap(BitmapFactory.decodeFile(strPhoto));
            } else {
                photo.setImageResource(R.drawable.profile);
            }
        }
        getSupportActionBar().setTitle("정보수정" + id);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button = findViewById(R.id.btnInsert);
        button.setText("수정");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder box = new AlertDialog.Builder(UpdateActivity.this);
                box.setTitle("질의");
                box.setMessage(id + "번 정보를 수정하시겠습니까?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "update address set ";
                        sql += "name='" + name.getText().toString() + "', ";
                        sql += "phone='" + phone.getText().toString() + "', ";
                        sql += "juso='" + juso.getText().toString() + "', ";
                        sql += "photo='" + strPhoto + "' ";
                        sql += "where _id=" + id;
                        db.execSQL(sql);
                        finish();
                    }
                });
                box.setNegativeButton("아니오", null);
                box.show();
            }
        });

        findViewById(R.id.photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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