package com.project.starwarsgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    SignInButton sign_in;
    int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;
    String TAG = "Star Wars login:";
    String userID;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        sign_in = findViewById(R.id.sign_in_button);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(loginActivity.this,HomeActivity.class));
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                e.printStackTrace();
            }

            handleSignInResult(task);
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();

                            boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(newuser){
                                createUser();
                            }

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                                    }

                                    // [START_EXCLUDE]

                                    // [END_EXCLUDE]
                                }
                            });
                        }
    private void createUser() {
        //testing to see if userID Prints
        System.out.println(userID);
        //Document references
        final DocumentReference documentReference = fStore.collection("users").document(userID);
        final DocumentReference leaderboardReference = fStore.collection("leaderboard").document(userID);
        final DocumentReference requestReference = fStore.collection("requests").document(userID);
        final DocumentReference friendsReference = fStore.collection("friends").document(userID);
        final DocumentReference notiRef = fStore.collection("notifications").document(userID);
        final Map<String, Object> leaderboardUser = new HashMap<>();
        //empty Map to use for create of empty documents
        Map<String, Object> data = new HashMap<>();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(loginActivity.this);

        leaderboardUser.put("Name", acct.getDisplayName());
        leaderboardUser.put("High_Score", 0);

        leaderboardReference.set(leaderboardUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "On Success: Leaderboard Profile Created for" + userID);
            }
        });
        final Map<String, Object> user = new HashMap<>();
        Uri ur = acct.getPhotoUrl();
        String url = ur.toString();
        user.put("Name", acct.getDisplayName());
        user.put("Email", acct.getEmail());
        user.put("planets_score", 0);
        user.put("people_score", 0);
        user.put("films_score", 0);
        user.put("vehicles_score", 0);
        user.put("species_score", 0);
        user.put("starships_score",0);
        user.put("exists",true);
        user.put("High_Score", 0);
        user.put("numOfFriends",0);
        user.put("image_url",url);

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "On Success: User Profile Created for" + userID);
            }
        });

        // create new empty documents for later users when making Friend Requests, viewing friends, and playing with friends
        requestReference.set(data);
        friendsReference.set(data);
        notiRef.set(data);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");

                    }
                }
            }
        });
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Intent intent = new Intent(loginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this,e.getStatusCode(),Toast.LENGTH_SHORT).show();

        }
    }
}