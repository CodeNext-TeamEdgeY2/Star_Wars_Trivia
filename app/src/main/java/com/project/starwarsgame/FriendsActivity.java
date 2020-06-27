package com.project.starwarsgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FriendsActivity extends AppCompatActivity {

    String[] character_attributes = {"name","birth_year","eye_color","gender","hair_color","height","mass","skin_color"};
    String[] planets_attributes = {"name","diameter","rotation_period","orbital_period","gravity","population","climate","terrain","surface_water"};
    String[] films_attributes = {"title","episode_id","opening_crawl","director","producer"};
    String[] starships_attributes = {"name","MGLT","passengers","crew","cargo_capacity","consumables","length","model","hyperdrive_rating","max_atmosphering_speed","starship_class","manufacturer"};
    String[] vehicle_attributes = {"name","length","crew","consumables","passengers","model","vehicle_class","max_atmosphering_speed","cargo_capacity","cost_in_credits","manufacturer"};
    String[] species_attributes = {"name","language","classification","designation","average_height","average_lifespan","eye_colors","hair_colors","skin_colors"};
    int[] totals = {60,82,37,39,26,6};
    String[] searches = {"planets/","people/","species/","vehicles/","starships/","films/"};
    String[][] arrayNames = {planets_attributes,character_attributes,species_attributes,vehicle_attributes,starships_attributes,films_attributes};
    String TAG = "Star Wars";
    ListView lv;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference fr = fStore.collection("friends");
    CollectionReference ur = fStore.collection("users");
    CollectionReference gr = fStore.collection("gameRoom");
    String userID;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    int my_user_num_of_f;

    Random rand = new Random();
    String document_to_send;
    ArrayList<String> userFriendsIDS = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        lv = findViewById(R.id.friends_list);
        userID = mAuth.getUid();


        ur.document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                my_user_num_of_f = documentSnapshot.toObject(Users.class).getNumOfFriends();
                System.out.println(my_user_num_of_f);
            }
        });
        final Map<String,Object> userFriends = new HashMap<>();
        fr.document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userFriends.putAll(documentSnapshot.getData());

                for (Map.Entry<String, Object> entry : userFriends.entrySet()) {
                    userFriendsIDS.add((String) entry.getValue());
                }
                System.out.println(userFriendsIDS);
                friendAdapter adapter2 = new friendAdapter(FriendsActivity.this,userFriendsIDS);
                lv.setAdapter(adapter2);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final String otherUserID = (String) userFriendsIDS.get(i);
                        String thisISIT = checkRealDocument(userID,otherUserID);
                        Intent startGame = new Intent(FriendsActivity.this,QuestionActivity.class);
                        System.out.println(document_to_send);
                        int category = rand.nextInt(6);
                        System.out.println(category);
                        startGame.putExtra("Total",totals[category]);
                        startGame.putExtra("Search",searches[category]);
                        startGame.putExtra("attributes",arrayNames[category]);
                        System.out.println(Arrays.toString(arrayNames[category]));
                        startGame.putExtra("versus_game",true);
                        startGame.putExtra("OtherUserID",otherUserID);
                        startGame.putExtra("doc",document_to_send);
                        startActivity(startGame);
                    }
                });

            }

        });

    }


    public String checkRealDocument(final String UserID, final String OtherUserID){

        final String option1 = UserID +","+OtherUserID;
        final String option2 = OtherUserID+","+UserID;
        final String[] toreturn = new String[1];


        gr.document(option2).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot != null){
                    if(documentSnapshot.exists()){
                        document_to_send = option2;
                        toreturn[0] = option2;
                    }
                } else {
                    Game game = new Game(false,UserID,OtherUserID,UserID,"","","",1,-10,-10,-10,-10,-10,-10);
                    gr.document(option1).set(game);
                    document_to_send = option1;
                    toreturn[0] = option1;
                }
                System.out.println(document_to_send);
                System.out.println(toreturn[0]);
            }


        });

        return toreturn[0];
    }
}