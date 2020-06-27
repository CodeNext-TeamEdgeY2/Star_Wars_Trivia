package com.project.starwarsgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class leaderboardActivity extends AppCompatActivity {
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    ListView lv;
    CollectionReference cr = fStore.collection("leaderboard");
    ArrayList<String> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        lv = findViewById(R.id.leaderboard);

        cr.orderBy("High_Score", Query.Direction.DESCENDING).limit(10)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    System.out.println(documentSnapshot.getData().getClass());
                    leaderboardUser leaderboardUser = documentSnapshot.toObject(leaderboardUser.class);
                    String name = leaderboardUser.getName();
                    int high_score = leaderboardUser.getHigh_Score();
                    Integer.toString(high_score);
                    users.add(String.format("%-20s Score: %s",name,high_score));
                }
                System.out.println(users);
                ArrayAdapter adapter = new ArrayAdapter(leaderboardActivity.this,android.R.layout.simple_list_item_1,users){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                       View view =  super.getView(position, convertView, parent);
                       if(position % 2 == 0){
                           view.setBackgroundColor(getColor(android.R.color.holo_blue_bright));
                       } else {
                           view.setBackgroundColor(getColor(android.R.color.white));
                       }
                        return view;
                    }
                };
                lv.setAdapter(adapter);
            }
        });


    }
}
