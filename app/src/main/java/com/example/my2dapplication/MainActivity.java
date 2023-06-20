package com.example.my2dapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my2dapplication.GameView;


/**
 * Main Activity Class
 * This class manages application's UI interactions, music, and menu options.
 * initializes the GameView and GameThread instances.
 */
public class MainActivity extends AppCompatActivity {

    Button buttonSubmit;
    private static final int MENU_RESUME = 1;
    private static final int MENU_START = 2;
    private static final int MENU_SAVE = 3;
    private static final int MENU_BACK = 4;
    private static final int MENU_EXIT = 5;
    private static final int MENU_SETTINGS = 6;
    private static final int MENU_END = 7;
    MediaPlayer mediaPlayer;
    private GameThread gameThread;
    TheGame game;


    /**
     * The onCreate method initializes the MainActivity UI and sets the initial state of the app.
     * It also starts the background music. API METHOD HERE
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wallpaper);
        mediaPlayer.start();
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ImageView moving_lines = findViewById(R.id.moving_lines);
        AnimationDrawable animationDrawable = (AnimationDrawable) moving_lines.getDrawable();
        animationDrawable.start();
    }


    /**
     * The startGame method initializes a new GameView instance and sets it as the content view.
     * It also starts the GameThread to manage the game loop.
     */
    public void startGame(View view) {
        GameView gameView = new GameView(this, null);
        gameView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(gameView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        gameThread = new GameThread(gameView);
        gameThread.setRunning(true);
        gameThread.startThread();
    }


    /**
     * The settings method sets the content view to the settings layout.
     */
    public void settings(View view) {
        setContentView(R.layout.settings);
    }

    /**
     * The back method sets the content view to the main activity layout.
     */

    public void back(View view) {
        setContentView(R.layout.activity_main);
    }


    /**
     * The levelselect method sets the content view to the level selection layout
     * and handles user interactions for selecting a level.
     */

    public void levelselect(View view) {
        setContentView(R.layout.levelselect);
        final int[] selectedLevel = {3};
        EditText editTextRow = findViewById(R.id.edittext_done);
        Button buttonSubmit = findViewById(R.id.submit);
        Button l1 = findViewById(R.id.l1);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLevel[0] = 1;
                TheGame.ROWS = 8;
                TheGame.COLS = 8;
            }
        });
        Button l2 = findViewById(R.id.l2);
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedLevel[0] = 2;
                TheGame.ROWS = 16;
                TheGame.COLS = 16;
            }
        });
        Button l3 = findViewById(R.id.l3);
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedLevel[0] = 3;
                TheGame.ROWS = 24;
                TheGame.COLS = 24;
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int rowInt = Integer.parseInt(editTextRow.getText().toString());
                    int remainder = rowInt%4; // default contrustor to set to nearest multiple of 4
                    rowInt -= remainder;
                    TheGame.ROWS = rowInt;
                    TheGame.COLS = rowInt;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * The exit method exits the application.
     */
    public void exit(View view) {
        System.exit(0);
    }

    /**
     * The onCreateOptionsMenu method inflates the main menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, MENU_START, 0, R.string.menu_start);
        menu.add(0, MENU_SAVE, 0, R.string.menu_save);
        menu.add(0, MENU_RESUME, 0, R.string.menu_resume);
        menu.add(0, MENU_BACK, 0, R.string.menu_back);
        menu.add(0, MENU_EXIT, 0, R.string.menu_exit);
        menu.add(0, MENU_SETTINGS, 0, R.string.menu_settings);
        menu.add(0, MENU_END, 0, R.string.menu_endgame);

        return true;
    }


    /**
     * The onPause method pauses the GameThread when the activity is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (gameThread != null) {
            gameThread.pauseThread();
        }
    }

    /**
     * The onResume method resumes the GameThread when the activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (gameThread != null) {
            gameThread.resumeThread();
        }
    }


    /**
     * The onDestroy method cleans up resources when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gameThread != null) {
            gameThread.destroyThread();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    /**
     * The onOptionsItemSelected method handles the user's selection of menu items.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_EXIT:
                System.exit(0);
                return true;
            case MENU_BACK:
                setContentView(R.layout.activity_main);
                return true;
            case MENU_SETTINGS:
                setContentView(R.layout.settings);
                return true;
            case MENU_START:
                startGame(null);
                return true;
            case MENU_SAVE:
                if (gameThread != null) {
                    gameThread.pauseThread(); // saves/pauses the thread
                    setContentView(R.layout.activity_main);
                }
                return true;
            case MENU_RESUME:
                if (gameThread != null) {
                    gameThread.resumeThread(); // resumes
                    setContentView(gameThread.getGameView());
                }
                return true;
            case MENU_END:
                if (gameThread != null) {
                    gameThread.stopThread();
                    setContentView(R.layout.ggame_over);
                }
                return true;
        }
        return false;
    }
}

