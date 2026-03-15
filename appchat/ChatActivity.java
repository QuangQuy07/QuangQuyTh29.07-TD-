package com.example.appchat;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchat.adapter.ChatRecyclerAdapter;
import com.example.appchat.adapter.SearchUserRecyclerAdapter;
import com.example.appchat.model.ChatMessageModel;
import com.example.appchat.model.ChatroomModel;
import com.example.appchat.model.UserModel;
import com.example.appchat.utils.AndroidUtil;
import com.example.appchat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.sql.Array;
import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
    UserModel otherUser;
String chatroomId;
ChatroomModel chatroomModel;
ChatRecyclerAdapter adapter;
EditText messageInput;
ImageButton sendMessageBtn;
ImageButton backBtn;
TextView otherUsername;
RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        // mô hình người dùng
        otherUser = AndroidUtil.getUserModelFormIntent(getIntent());
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currenUserId(),otherUser.getUserId());

        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        backBtn = findViewById(R.id.back_btn);
        otherUsername = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_recycler_view);


backBtn.setOnClickListener((V)->{
    onBackPressed();
});
        otherUsername.setText(otherUser.getUsername());

        sendMessageBtn.setOnClickListener((v ->{
            String message = messageInput.getText().toString().trim();
            if(message.isEmpty())
                return;
            sendMessageToUser(message);
        }));

        getOrCreateChatroom();
        setupChatRecyclerView();

    }
    void setupChatRecyclerView(){
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();
        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    void sendMessageToUser(String message){

        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FirebaseUtil.currenUserId());
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

            ChatMessageModel chatMessageModel = new ChatMessageModel(message,FirebaseUtil.currenUserId(),Timestamp.now());

         FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                 .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                     @Override
                     public void onComplete(@NonNull Task<DocumentReference> task) {
                      if(task.isSuccessful()){
                          messageInput.setText(" ");
                      }
                     }
                 });
    }

    void getOrCreateChatroom(){
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
if(task.isSuccessful()){
chatroomModel = task.getResult().toObject(ChatroomModel.class);
if(chatroomModel==null){
    // trò chuyện lần đầu tiên
    chatroomModel = new ChatroomModel(
            chatroomId,
            Arrays.asList(FirebaseUtil.currenUserId(),otherUser.getUserId()),
            Timestamp.now(),
            ""
    );
    FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);
}
}
        });

    }


}