package com.example.scarnedice;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static int user_overall_score = 0;
    public static int user_turn_score = 0;
    public static int computer_overall_score = 0;
    public static int computer_turn_score = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void rollDice(View view) {
        ImageView dice = (ImageView) findViewById(R.id.imageView);

        Random rand = new Random();


        TextView score = (TextView) findViewById(R.id.turn_score_int);
        TextView user_score = (TextView) findViewById(R.id.your_score);
        TextView computer_score = (TextView) findViewById(R.id.computer_score);
        int randomNum = rand.nextInt(6) + 1;
        if (randomNum == 1) {
            score.setText(String.valueOf(0));
            user_turn_score = 0;
            computerTurn();
        } else {
            user_turn_score += randomNum;
            score.setText(String.valueOf(user_turn_score));
            user_score.setText(String.valueOf(user_overall_score));
            int[] dices = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice5};
            dice.setImageResource(dices[randomNum-1]);

        }
        user_overall_score += user_turn_score;

    }

    public void resetButton(View view) {
        user_overall_score = 0;
        user_turn_score = 0;
        computer_overall_score = 0;
        computer_turn_score = 0;
        TextView score = (TextView) findViewById(R.id.turn_score_int);
        TextView user_score = (TextView) findViewById(R.id.your_score);
        TextView comp_score = (TextView) findViewById(R.id.computer_score);
        score.setText(String.valueOf(user_turn_score));
        user_score.setText(String.valueOf(user_overall_score));
        comp_score.setText(String.valueOf(computer_overall_score));

    }

    public void holdButton(View view) {
        user_turn_score = 0;
        TextView score = (TextView) findViewById(R.id.turn_score_int);
        score.setText(String.valueOf(user_turn_score));
        computerTurn();

    }

    public void computerTurn() {
        Button hold = (Button) findViewById(R.id.button);
        Button reset = (Button) findViewById(R.id.button3);
        hold.setEnabled(false);
        reset.setEnabled(false);
        Random rand = new Random();
        TextView comp_score = (TextView) findViewById(R.id.computer_score);

            int randomNum = rand.nextInt(6) + 1;
            if (randomNum == 1) {
                computer_turn_score = 0;
            } else {

                computer_turn_score += randomNum;
                if (computer_turn_score < 20){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run(){
                            computerTurn();
                        }
                    }, 500);
                }
            }

        computer_overall_score+=computer_turn_score;
        comp_score.setText(String.valueOf(computer_overall_score));



        computer_turn_score = 0;

        hold.setEnabled(true);
        reset.setEnabled(true);


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
