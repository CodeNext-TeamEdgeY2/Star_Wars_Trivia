package com.project.starwarsgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    String personName;
    String personEmail;
    Uri personPhoto;



    String[] character_attributes = {"name","birth_year","eye_color","gender","hair_color","height","mass","skin_color"};
    String[] planets_attributes = {"name","diameter","rotation_period","orbital_period","gravity","population","climate","terrain","surface_water"};
    String[] films_attributes = {"title","episode_id","opening_crawl","director","producer"};
    String[] starships_attributes = {"name","MGLT","passengers","crew","cargo_capacity","consumables","length","model","hyperdrive_rating","max_atmosphering_speed","starship_class","manufacturer"};
    String[] vehicle_attributes = {"name","length","crew","consumables","passengers","model","vehicle_class","max_atmosphering_speed","cargo_capacity","cost_in_credits","manufacturer"};
    String[] species_attributes = {"name","language","classification","designation","average_height","average_lifespan","eye_colors","hair_colors","skin_colors"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView name = findViewById(R.id.username);
        CircleImageView profilePicImageview = findViewById(R.id.personImageView);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            personPhoto = acct.getPhotoUrl();
        }
        name.setText(personName);
        if(personPhoto != null) {
            Picasso.get().load(personPhoto).into(profilePicImageview);
        } else {
            profilePicImageview.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }
    }

    public void openPlanets(View view) {
        Intent planets = new Intent(MainActivity.this,QuestionActivity.class);
        planets.putExtra("Total",60);
        planets.putExtra("Search","planets/");
        planets.putExtra("attributes",planets_attributes);
        planets.putExtra("versus_game",false);
        startActivity(planets);
        finish();
    }

    public void openChar(View view) {
        Intent Character = new Intent(MainActivity.this,QuestionActivity.class);
        Character.putExtra("Total",82);
        Character.putExtra("Search","people/");
        Character.putExtra("attributes",character_attributes);
        Character.putExtra("versus_game",false);
        startActivity(Character);
    }

    public void openSpecies(View view) {
        Intent Species = new Intent(MainActivity.this,QuestionActivity.class);
        Species.putExtra("Total",37);
        Species.putExtra("Search","species/");
        Species.putExtra("attributes",species_attributes);
        Species.putExtra("versus_game",false);
        startActivity(Species);
    }

    public void openVehicles(View view) {
        Intent Vehicles = new Intent(MainActivity.this,QuestionActivity.class);
        Vehicles.putExtra("Total",39);
        Vehicles.putExtra("Search","vehicles/");
        Vehicles.putExtra("attributes",vehicle_attributes);
        Vehicles.putExtra("versus_game",false);
        startActivity(Vehicles);
    }

    public void openStarShips(View view) {
        Intent Starships = new Intent(MainActivity.this,QuestionActivity.class);
        Starships.putExtra("Total",36);
        Starships.putExtra("Search","starships/");
        Starships.putExtra("attributes",starships_attributes);
        Starships.putExtra("versus_game",false);
        startActivity(Starships);
    }

    public void openFilms(View view) {
        Intent Films= new Intent(MainActivity.this,QuestionActivity.class);
        Films.putExtra("Total",6);
        Films.putExtra("Search","films/");
        Films.putExtra("attributes",films_attributes);
        Films.putExtra("versus_game",false);
        startActivity(Films);
    }


}