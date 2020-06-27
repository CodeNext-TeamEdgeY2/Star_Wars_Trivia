package com.project.starwarsgame;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;




public class friendAdapter extends ArrayAdapter<String> {

FirebaseAuth auth;



    public friendAdapter(Activity activity, ArrayList<String> list) {
        super(activity,0,list);
    }

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference usersref = fStore.collection("users");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_layout,parent,false);
        String itemToSend = String.valueOf(getItem(position));
        System.out.println(itemToSend);
        final View finalConvertView = convertView;

        try {
            usersref.document(String.valueOf(getItem(position))).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    System.out.println(documentSnapshot.getData());
                    Users user = documentSnapshot.toObject(Users.class);
                    System.out.println(user);
                    TextView email = finalConvertView.findViewById(R.id.email);
                    email.setText(user.getEmail());
                    TextView username = finalConvertView.findViewById(R.id.name);
                    username.setText(user.getName());
                    CircleImageView profilePic = finalConvertView.findViewById(R.id.personImageView);
                    if (user.getImage_url() != null) {
                        Picasso.get().load(user.getImage_url()).into(profilePic);
                    } else {
                        profilePic.setImageResource(R.drawable.ic_account_circle_black_24dp);
                    }
                }
            });
        } finally {

        }
        return finalConvertView;
    }
}
