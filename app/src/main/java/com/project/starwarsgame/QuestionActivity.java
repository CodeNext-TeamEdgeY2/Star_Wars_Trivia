package com.project.starwarsgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {

    TextView questionTV;
    Random rand = new Random();
    Button button1,button2,button3;
    String[] attributes;
    int attribute_item;
    int item;
    int total;
    String search;
    int score = 0;
    String answer;
    ProgressBar progressBar;
    String Url = "https://swapi.dev/api/";
    String userID;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    CollectionReference gr = fStore.collection("gameRoom");
    final CollectionReference notiRef = fStore.collection("notifications");
    CollectionReference ur = fStore.collection("users");
    String TAG = "Star Wars:";
    DocumentReference usersReference;
    int category_high_score;
    String category;
    String addString = "_score";
    boolean vs_game;
    String otheruserid;
    String currentPlayer;
    String otherPlayer;
    int game_round;
    String str;

    String document_to_send;

    String myName;
    String friendName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        questionTV = findViewById(R.id.questionTV);
        progressBar = findViewById(R.id.progressBar);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button1.getText();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
        usersReference = fStore.collection("users").document(userID);

        Bundle bundle = getIntent().getExtras();
        search = (String)bundle.get("Search");
        category = search.substring(0,search.length()-1);
        category += addString;
        Url += search;
        attributes = bundle.getStringArray("attributes");
        total = (int) bundle.get("Total");
        vs_game = (boolean) bundle.get("versus_game");
        otheruserid = (String) bundle.get("OtherUserID");
        document_to_send = (String) bundle.get("doc");
        System.out.println(otheruserid);

        if(vs_game){
            createTimerV();
        } else {
            createTimerN();
        }

        getRandomItem();
    }

    public void createTimerN(){
        progressBar.setMax(60000);
        progressBar.setProgress(60000);
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long l) {
                int myInt = ((int) l);
                progressBar.setProgress(myInt);
            }

            @Override
            public void onFinish() {
                normalGameDialog();
                System.out.println("Finished");
            }
        }.start();

    }
    public void createTimerV(){
        progressBar.setMax(60000);
        progressBar.setProgress(60000);
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long l) {
                int myInt = ((int) l);
                progressBar.setProgress(myInt);
            }

            @Override
            public void onFinish() {
                versusGameEnd();
                System.out.println("Finished");
            }
        }.start();
    }

    private void getHighScore(final int score) {
        final DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Users users = documentSnapshot.toObject(Users.class);
                if (category.contains("films")){
                    category_high_score = users.getFilms_score();
                } else if (category.contains("people")){
                    category_high_score = users.getPeople_score();
                }else if (category.contains("planets")){
                    category_high_score = users.getPlanets_score();
                } else if (category.contains("species")){
                    category_high_score = users.getSpecies_score();
                } else if(category.contains("starships")){
                    category_high_score = users.getStarships_score();
                } else{
                    category_high_score = users.getVehicles_score();
                }
                if(score > category_high_score){
                    documentReference.update(category,score);
                    updateHighScore();
                }
            }
        });

    }
    private void updateHighScore() {
        final DocumentReference usersReference = fStore.collection("users").document(userID);
        final DocumentReference leaderboardReference = fStore.collection("leaderboard").document(userID);

        usersReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Users users = documentSnapshot.toObject(Users.class);

               int a = users.getVehicles_score();
               int b = users.getPeople_score();
               int c = users.getSpecies_score();
               int d= users.getPlanets_score();
               int g = users.getStarships_score();
               int f= users.getFilms_score();
               int total = a + b+ c + d +g + f;
                leaderboardReference.update("High_Score",total);
                usersReference.update("High_Score",total);
            }

        });
    }
    public void normalGameDialog(){
        button1.setClickable(false);
        button2.setClickable(false);
        button3.setClickable(false);
        getHighScore(score);
        AlertDialog.Builder alert = new AlertDialog.Builder(QuestionActivity.this);
        alert.setTitle("Game Over");
        alert.setMessage("Your final score is "+score);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent gameOver = new Intent(QuestionActivity.this,MainActivity.class);
                startActivity(gameOver);
                finish();
            }
        });
        alert.create().show();
    }



    public void getRandomItem()
    {

        attribute_item = rand.nextInt(attributes.length -1);
        attribute_item += 1;

        int random_item = rand.nextInt(total+1);

        int page;
        if (random_item == 0 ){
            page = 1;
        } else {
            page = (random_item + 9)/10;
        }
        String str_page = "?page="+page;
        Url += str_page;
        if(search.equals("films/")) {
            item = random_item -1;
        } else {
            item = Math.abs(random_item-((page-1)*10)-1);
        }
        fetchData();
    }
    public void fetchData()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject star_wars_object = new JSONObject(response);

                    JSONArray people = star_wars_object.getJSONArray("results");
                    JSONObject person = people.getJSONObject(item);
                    JSONObject randomish = people.getJSONObject(Math.abs(item-1));
                    
                    String name = person.getString(attributes[0]);
                    questionTV.setText(String.format("What is %s's %s?",name, attributes[attribute_item]).replace("_"," "));
                    answer = person.getString(attributes[attribute_item]);
                    String randOption = randomish.getString(attributes[attribute_item]);
                    String option3;

                    if (answer.equals("n/a")){
                        option3 = "unknown";
                    } else {
                        option3 = "n/a";
                    }

                    if(randOption.equals(answer)){
                        randOption = randOption.substring(0,randOption.length()-1);
                    }

                    String[] answerOptions = new String[] {answer,randOption,option3};
                    List<String> strList = Arrays.asList(answerOptions);
                    Collections.shuffle(strList);
                    answerOptions = strList.toArray(new String[strList.size()]);

                    button1.setText(answerOptions[0]);
                    button2.setText(answerOptions[1]);
                    button3.setText(answerOptions[2]);
                    
                    Url = "https://swapi.dev/api/" + search;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                questionTV.setText("error");

            }
        });
        queue.add(stringRequest);
    }
    public void button_clicked(View view) {
        Button b = (Button)view;
        String buttonText = b.getText().toString();
        checkAnswer(buttonText);
        getRandomItem();
    }
    public void checkAnswer(String option){
        if(option.equals(answer)){
            score += 1;
        }
    }
    public void versusGameEnd(){
        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.INVISIBLE);

        System.out.println(document_to_send);


                gr.document(document_to_send).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       Game cg = documentSnapshot.toObject(Game.class);

                        System.out.println(cg);

                        game_round = cg.getRound();

                        if(userID.equals(cg.getUser0())){
                            currentPlayer = "user0";
                            otherPlayer = "user1";
                        } else {
                            currentPlayer = "user1";
                            otherPlayer = "user0";
                        }


                        if(userID.equals(cg.getTurn())){
                            String update_field = currentPlayer+"round"+game_round+"score";
                            gr.document(document_to_send).update(update_field,score);
                            str = "round";

                        } else {
                            str = "practice";
                        }
                        showAlert();

                    updateGameScore();


                    }
                });


    }
    public void showAlert(){
        AlertDialog.Builder al = new AlertDialog.Builder(QuestionActivity.this);
        al.setTitle("Round Over");
        if(str.equals("practice")){
            al.setMessage("Your Score for "+ str +" is :" +score);
        } else {
            al.setMessage("Your Score for round "+ game_round +" is :"+score);
        }
        al.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent gameOver = new Intent(QuestionActivity.this,HomeActivity.class);
                startActivity(gameOver);
                finish();
            }
        });
        al.create().show();
    }
    public void updateGameScore(){
        if(currentPlayer.equals("user1") && game_round == 3){
            gr.document(document_to_send).update("is_game_finish",true);
            gr.document(document_to_send).delete();

            ur.document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Users userme = documentSnapshot.toObject(Users.class);
                    myName = userme.getName();
                }
            });

            ur.document(otheruserid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Users friend = documentSnapshot.toObject(Users.class);
                    friendName = friend.getName();
                }
            });

            notiRef.document(userID).update(otheruserid +rand.nextInt(100000000),"Game versus"+ friendName+" is finished");
            notiRef.document(otheruserid).update(userID+ rand.nextInt(100000000),"Game versus"+ myName+" is finished");
        }



        if(currentPlayer.equals("user1")){

            gr.document(document_to_send).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Game ggggg = documentSnapshot.toObject(Game.class);
                    if(game_round == 1) {
                        int the1 = ggggg.getUser0round1score();
                        int the2 = ggggg.getUser1round1score();

                        if(the1 > the2){
                            gr.document(document_to_send).update("round1Winner",the1);
                            notiRef.document(userID).update(otheruserid +rand.nextInt(100000000), friendName+"won round 1");
                            notiRef.document(otheruserid).update(userID+ rand.nextInt(100000000),friendName+"won round 1 versus" + myName);
                        } else {
                            gr.document(document_to_send).update("round1Winner",the2);
                            notiRef.document(userID).update(otheruserid +rand.nextInt(100000000), myName +"won round 1 versus" + friendName);
                            notiRef.document(otheruserid).update(userID+ rand.nextInt(100000000),myName+"won round 1");
                        }
                    } else if (game_round == 2){
                        int the1 = ggggg.getUser0round2score();
                        int the2 = ggggg.getUser1round2score();

                        if(the1 > the2){
                            gr.document(document_to_send).update("round2Winner",the1);
                            notiRef.document(userID).update(otheruserid +rand.nextInt(100000000), friendName+"won round 2");
                            notiRef.document(otheruserid).update(userID+ rand.nextInt(100000000),friendName+"won round 2 versus" + myName);
                        } else {
                            gr.document(document_to_send).update("round2Winner",the2);
                            notiRef.document(userID).update(otheruserid +rand.nextInt(100000000), myName +"won round 2 versus" + friendName);
                            notiRef.document(otheruserid).update(userID+ rand.nextInt(100000000),myName+"won round 2");
                        }
                    } else {
                        int the1 = ggggg.getUser0round3score();
                        int the2 = ggggg.getUser1round3score();

                        if(the1 > the2){
                            gr.document(document_to_send).update("round3Winner",the1);
                            notiRef.document(userID).update(otheruserid +rand.nextInt(100000000), friendName+"won round 3");
                            notiRef.document(otheruserid).update(userID+ rand.nextInt(100000000),friendName+"won round 3 versus" + myName);
                        } else {
                            gr.document(document_to_send).update("round3Winner",the2);
                            notiRef.document(userID).update(otheruserid +rand.nextInt(100000000), myName +"won round 3 versus" + friendName);
                            notiRef.document(otheruserid).update(userID+ rand.nextInt(100000000),myName+"won round 3");
                        }
                    }
                }
            });
            gr.document(document_to_send).update("turn",otherPlayer);
            game_round += 1;
            gr.document(document_to_send).update("round",game_round);
        }
    }
}