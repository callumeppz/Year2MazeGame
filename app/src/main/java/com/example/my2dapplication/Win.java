package com.example.my2dapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
// unused class
public class Win extends MainActivity {

    public static void showWinScreen(Context context, int score) {
        Intent intent = new Intent(context, Win.class);
        intent.putExtra("score", score);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win);
    }

    public void onRestartButtonClick(View view) {
        // Start the main activity and finish this activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void exit(View view){
        finish();
    }
}
