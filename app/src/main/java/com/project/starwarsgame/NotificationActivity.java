package com.project.starwarsgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
    ListView nl;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference nr = fStore.collection("notifications");
    String userID;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        nl = findViewById(R.id.notifications_list);
        userID = mAuth.getUid();

        final ArrayList<String> notifications = new ArrayList<>();
        final ArrayList<String> fieldID = new ArrayList<>();
        
            nr.document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, Object> requests = new HashMap<>();

                    try{requests.putAll(documentSnapshot.getData());} catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(requests);
                    for (Map.Entry<String, Object> entry : requests.entrySet()) {
                        if(!"0".equals(String.valueOf(entry.getValue()))){
                            fieldID.add(String.valueOf(entry.getKey()));
                            notifications.add(String.valueOf(entry.getValue()));
                        }

                    }
                    if (notifications.isEmpty()) {
                        notifications.add("Welcome to Star Wars Trivia. You have no new notifications at the moment. When new notifications appear click on them to simply delete.");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter(NotificationActivity.this, android.R.layout.simple_list_item_1, notifications) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            if (position % 2 == 0) {
                                view.setBackgroundColor(getColor(android.R.color.holo_blue_bright));
                            } else {
                                view.setBackgroundColor(getColor(android.R.color.white));
                            }
                            return view;
                        }
                    };
                    nl.setAdapter(adapter);
                    nl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final String itemToDelete = fieldID.get(i);
                            Map<String,Object> deleteUserNotif= new HashMap<>();
                            deleteUserNotif.put(itemToDelete, FieldValue.delete());
                            nr.document(userID).update(deleteUserNotif);
                        }
                    });
                }
            });
        }
    }
