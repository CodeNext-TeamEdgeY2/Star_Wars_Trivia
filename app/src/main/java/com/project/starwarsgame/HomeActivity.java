package com.project.starwarsgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    String personName;
    String personEmail;
    Uri personPhoto;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    String TAG = "Star Wars:";
    String userID;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView name = findViewById(R.id.username);
        CircleImageView profilePicImageview = findViewById(R.id.personImageView);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            personPhoto = acct.getPhotoUrl();
            System.out.println(personPhoto.toString());
        }
        name.setText(personName);
        if(personPhoto != null) {
            Picasso.get().load(personPhoto).into(profilePicImageview);

        } else {
            profilePicImageview.setImageResource(R.drawable.ic_account_circle_black_24dp);

        }

    }
    private void checkUserExists(){
        final DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Users user = documentSnapshot.toObject(Users.class);
                System.out.println(user);
                if(user != null){
                    return;
                }
            }
        });
    }


    public void sign_out(View view) {
        switch (view.getId()) {
            case R.id.sign_out_btn:
                signOut();
                break;
        }
    }
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this,loginActivity.class));
        finish();

    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        System.out.println(FirebaseAuth.getInstance());
        System.out.println(currentUser);
    }

    public void openCategories(View view) {
        startActivity(new Intent(this,MainActivity.class));
    }

    public void openLeaderboard(View view) {
        startActivity(new Intent(this,leaderboardActivity.class));
    }

    public void instructions(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        alert.setTitle("Instructions");
        alert.setMessage("Each category has it's specific questions relating to a star wars topic. Answer questions right to get points. Each game lasts 1 minute, answer as many questions as you can in that time. You can check the leaderboard to view the top 10 High Scores. High Scores are a culmination of your scores gathered in each category. Good Luck and may the force be with you.");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent info = new Intent(HomeActivity.this,HomeActivity.class);
                startActivity(info);
                finish();
            }
        });
        alert.create().show();

    }

    public void openFriends(View view) {
        startActivity(new Intent(this,RequestsActivity.class));

    }
    public void openRequests(View view){
        startActivity(new Intent(this,FriendsActivity.class));
    }

    public void openNotif(View view) {
        startActivity(new Intent(HomeActivity.this,NotificationActivity.class));
    }
}