package com.project.starwarsgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestsActivity extends AppCompatActivity {
    ListView rl;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference fr = fStore.collection("friends");
    CollectionReference requestRef = fStore.collection("requests");
    CollectionReference ur = fStore.collection("users");
    String userID;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    int my_user_num_of_f;
    int other_user_num_of_f;
    TextView noRequests;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        rl = findViewById(R.id.requestsList);
        noRequests = findViewById(R.id.text_no_requests);
        userID = mAuth.getUid();
        final DocumentReference requestReference = requestRef.document(userID);
        final ArrayList<String> userIDRequests = new ArrayList<>();
        requestReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> requests = new HashMap<>();
                requests.putAll(documentSnapshot.getData());
                System.out.println(requests);
                for (Map.Entry<String, Object> entry : requests.entrySet()) {
                    if(entry.getValue().equals("request")){
                        userIDRequests.add(entry.getKey());
                        System.out.println(userIDRequests);
                    }
                }
                if(userIDRequests.isEmpty()){
                    rl.setVisibility(View.INVISIBLE);
                    noRequests.setVisibility(View.VISIBLE);
                }
                final requestAdapter adapter = new requestAdapter(RequestsActivity.this,userIDRequests);

                rl.setAdapter(adapter);
                rl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final String selectedItem = (String) userIDRequests.get(i);
                        System.out.println(selectedItem);
                        Toast.makeText(RequestsActivity.this,"Friend Added",Toast.LENGTH_SHORT).show();
                        ur.document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Users user = documentSnapshot.toObject(Users.class);
                                my_user_num_of_f = user.getNumOfFriends();
                            }
                        });
                        ur.document(selectedItem).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Users user = documentSnapshot.toObject(Users.class);
                                other_user_num_of_f =  user.getNumOfFriends();
                            }
                        });


                        String myFriendNum = "friend"+my_user_num_of_f;
                        Map<String,Object> mynewFriend = new HashMap<>();
                        mynewFriend.put(myFriendNum,selectedItem);
                        fr.document(userID).set(mynewFriend);

                        String otherfriendNum = "friend"+other_user_num_of_f;
                        Map<String,Object> newFriend = new HashMap<>();
                        newFriend.put(otherfriendNum,userID);
                        fr.document(selectedItem).set(newFriend);


                        Map<String,Object> deleteUserRequest= new HashMap<>();
                        deleteUserRequest.put(selectedItem, FieldValue.delete());
                        requestRef.document(userID).update(deleteUserRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                System.out.println("Request Deleted");
                            }
                        });
                        Map<String,Object> deleteOtherRequest = new HashMap<>();
                        deleteOtherRequest.put(userID, FieldValue.delete());
                        requestRef.document(selectedItem).update(deleteOtherRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                System.out.println("Request Deleted");
                            }
                        });


                        userIDRequests.remove(i);
                        adapter.notifyDataSetChanged();
                        my_user_num_of_f += 1;
                        other_user_num_of_f += 1;

                        ur.document(userID).update("numOfFriends",Integer.valueOf(my_user_num_of_f));
                        ur.document(selectedItem).update("numOfFriends",Integer.valueOf(otherfriendNum));
                    }
                });
            }
        });
    }
    public void openSearch(View view) {
        startActivity(new Intent(this,searchUsers.class));
    }
}