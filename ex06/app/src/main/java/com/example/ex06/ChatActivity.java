package com.example.ex06;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText contents;
    ImageView send;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    ArrayList<ChatVO> array = new ArrayList<>();
    ChatAdapter adapter = new ChatAdapter();
    RecyclerView list;
    FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        getList();
        list = findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
        // Log.i("size", array.size() + "");
        
        getSupportActionBar().setTitle("채팅 : " + user.getEmail());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        contents = findViewById(R.id.edtContents);
        send = findViewById(R.id.btnSend);
        
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strContents = contents.getText().toString();
                if(strContents.equals("")) {
                    Toast.makeText(ChatActivity.this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    ChatVO vo = new ChatVO();
                    vo.setContents(strContents);
                    vo.setEmail(user.getEmail());
                    Date now = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                    vo.setDate(sdf.format(now));
                    // Log.i("vo", vo.toString());
                    DatabaseReference ref = db.getReference("/chat").push();
                    vo.setKey(ref.getKey());
                    ref.setValue(vo);
                    contents.setText("");
                }
            }
        });
    } // onCreate

    public void getList() {
        DatabaseReference ref = db.getReference("chat");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatVO vo = snapshot.getValue(ChatVO.class);
                array.add(vo);
                adapter.notifyDataSetChanged();
                list.scrollToPosition(array.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ChatVO vo = snapshot.getValue(ChatVO.class);
                for(ChatVO chat:array) {
                    if(chat.getKey().equals(vo.getKey())) {
                        array.remove(chat);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    } // getList

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
        @NonNull
        @Override
        public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_chat, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
            ChatVO vo = array.get(position);
            holder.email.setText(vo.getEmail());
            holder.contents.setText(vo.getContents());
            holder.date.setText(vo.getDate());
            LinearLayout.LayoutParams paramsContents=(LinearLayout.LayoutParams)holder.contents.getLayoutParams();
            LinearLayout.LayoutParams paramsDate=(LinearLayout.LayoutParams)holder.date.getLayoutParams();
            if(vo.getEmail().equals(user.getEmail())) {
                paramsContents.gravity = Gravity.RIGHT;
                paramsDate.gravity = Gravity.RIGHT;
                holder.email.setVisibility(View.GONE);
            }else {
                paramsContents.gravity = Gravity.LEFT;
                paramsDate.gravity= Gravity.LEFT;
                holder.email.setVisibility(View.VISIBLE);
            }

            holder.contents.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(vo.getEmail().equals(user.getEmail())) {
                        AlertDialog.Builder box = new AlertDialog.Builder(ChatActivity.this);
                        box.setTitle("메시지 삭제");
                        box.setMessage("메시지를 삭제하시겠습니까?");
                        box.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.getReference("chat/" + vo.getKey()).removeValue();
                            }
                        });
                        box.setNegativeButton("취소", null);
                        box.show();
                    }
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView email, contents, date;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                email = itemView.findViewById(R.id.txtEmail);
                contents = itemView.findViewById(R.id.txtContents);
                date = itemView.findViewById(R.id.txtDate);
            }
        }
    } // ChatAdapter

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
}