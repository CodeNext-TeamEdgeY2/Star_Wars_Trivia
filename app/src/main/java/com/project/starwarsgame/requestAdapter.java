package com.project.starwarsgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class requestAdapter extends ArrayAdapter<String> {
    public requestAdapter(@NonNull Context context, ArrayList<String> list) {
        super(context,0, list);
    }
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference ur = fStore.collection("users");

    @Nullable
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_item,parent,false);
        String id = String.valueOf(getItem(position));
        final View finalConvertView = convertView;
        ur.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Users user = documentSnapshot.toObject(Users.class);
                TextView email = finalConvertView.findViewById(R.id.requesterEmail);
                email.setText(user.getEmail());
            }
        });
        return  finalConvertView;
    }
}
