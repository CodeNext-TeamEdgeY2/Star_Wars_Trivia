package com.project.starwarsgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class searchUsers extends AppCompatActivity {
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference cr = fStore.collection("users");
    CollectionReference requestRef = fStore.collection("requests");
    CollectionReference fr = fStore.collection("friends");
    EditText search_bar;
    CircleImageView userImageView;
    TextView name,email;
    String user_searched_ID;
    String userID;
    String userEmail;
    ArrayList<String> friends;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Map<String,Object> userFriends = new HashMap<>();
    int my_user_num_of_f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);
        search_bar = findViewById(R.id.search_bar);
        userImageView = findViewById(R.id.personImageView);
        email = findViewById(R.id.useremail);
        name = findViewById(R.id.username);
        userID = mAuth.getUid();

        cr.document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Users you = documentSnapshot.toObject(Users.class);
                userEmail = you.getEmail();
            }
        });

    }

    public void searchUser(View view) {

        String emailSearched = search_bar.getText().toString();
        if(userEmail.equals(emailSearched)){
            Toast.makeText(this,"Search a valid User.",Toast.LENGTH_SHORT).show();
        } else {
            cr.whereEqualTo("Email", emailSearched).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        user_searched_ID = documentSnapshot.getId();

                        Users user = documentSnapshot.toObject(Users.class);
                        String url = user.getImage_url();
                        if (url != null) {
                            Picasso.get().load(url).into(userImageView);
                        } else {
                            userImageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
                        }
                        name.setText(user.getName());
                        email.setText(user.getEmail());

                    }
                }
            });
        }
    }

    public void sendRequest(View view) {
        Map<String,Object> requesterStuff = new HashMap<>();
        requesterStuff.put(user_searched_ID,"requested");

        Map<String,Object> requestedUserStuff= new HashMap<>();
        requestedUserStuff.put(userID,"request");

        DocumentReference requester = requestRef.document(userID);
        DocumentReference requestedUser = requestRef.document(user_searched_ID);

        requester.set(requesterStuff);
        requestedUser.set(requestedUserStuff);
        Toast.makeText(this,"Request Sent",Toast.LENGTH_SHORT).show();
    }
}