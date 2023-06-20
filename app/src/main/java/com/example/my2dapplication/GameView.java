/**
 * This is the main GameView class that handles the game graphics and user input.
 * @author Callum Apps (30010641)
 * @version 2.0
 *
 * This is the second version of this code. The previous version had memory errors,
 * so it was restarted and created anew.
 */

package com.example.my2dapplication;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
    private SimpleEnemy simpleEnemy;
    private final Handler handler = new Handler();
    private final int dWidth;
    Bitmap backgroundimg = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundimg); // imagefetching
    private static final int UPDATE_MILLIS = 100;
    private final Runnable runnable = new Runnable() {

        @Override
        public void run() {
            invalidate();
        }
    };
    private TheGame game;
    private GameOver gameover;

    private int fps = 0;
    public int levels = 1;

    private int cellSize;
    private Context context;
    private GameThread gameThread;
    private Paint wallPaint, exitPaint, bunnyPaint, enemyPaint, pointPaint, healthPaint;

    /**
     *This constructor initializes the GameView class with a given context,
     * this would also be used to initialise the TheGame class, simple enemy and the game thread.
     * @param context
     */
    public GameView(Context context) {
        super(context);
        game = new TheGame();
        dWidth = getResources().getDisplayMetrics().widthPixels;
        simpleEnemy = new SimpleEnemy(5, 5);
        init();
        gameThread = new GameThread(this);
        gameThread.startThread();
    }

    /**
     *This constructor initializes the GameView class with a given context and attribute set and
     * creates a new instance of TheGame class. The constructor would also be used to set the display width
     * and initializes a simple enemy,
     * and initializes paint objects.
     * @param context
     * @param attrs
     */
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        game = new TheGame();
        dWidth = getResources().getDisplayMetrics().widthPixels;
        simpleEnemy = new SimpleEnemy(10, 10);
        init();
    }

    /**
     *This constructor initializes the GameView class with a given context, attribute set,
     * and default style attribute, creates a new instance of TheGame class, sets the display width,
     * initializes a simple enemy, and initializes paint objects.
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        game = new TheGame();
        dWidth = getResources().getDisplayMetrics().widthPixels;
        simpleEnemy = new SimpleEnemy(10, 10);
        init();
    }

    /**
     * This method initializes paint objects for various game elements (walls, exit, bunny, enemy, points, and health bar)
     * and starts the game thread.
     */
    private void init() {
        wallPaint = new Paint();
        wallPaint.setColor(Color.rgb(93, 63, 211));
        healthPaint = new Paint();
        wallPaint.setStrokeWidth(5);
        exitPaint = new Paint();
        exitPaint.setColor(Color.BLUE);
        bunnyPaint = new Paint();
        bunnyPaint.setColor(Color.GREEN);
        enemyPaint = new Paint();
        enemyPaint.setColor(Color.RED);
        pointPaint = new Paint();
        pointPaint.setColor(Color.YELLOW);
        gameThread = new GameThread(this);
        gameThread.setRunning(true);
        gameThread.startThread();
    }
    ///This method returns the current instance of TheGame class.
    public TheGame getGame() {
        return game;
    }

    //This method updates the fps (frames per second) value of the game view.

    public void update(int fps) {
        this.fps = fps/25;
    }

    private long startTime = 0;
    private int elapsedTime = 0;
    private int highScore = 0;
    private int highLevel = 0;


    SharedPreferences sharedPreferences = getContext().getSharedPreferences("highscore", Context.MODE_PRIVATE);
    // shared preferances instances are created to store highlevel and highscore
    SharedPreferences sharedPreferences2 = getContext().getSharedPreferences("highlevel", Context.MODE_PRIVATE);


    /**
     *This method draws game elements such as the background, maze, bunny, enemies, points, score, level, timer, high score, high level,
     * and health bar on to the canvas.
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        cellSize = canvas.getWidth() / TheGame.COLS;
        canvas.drawBitmap(backgroundimg, 0, 0, null);
        int paddingX = (canvas.getWidth() - (cellSize * TheGame.COLS)) / 2;
        int paddingY = (canvas.getHeight() - (cellSize * TheGame.ROWS)) / 2;
        enemyPaint.setColor(game.getSimpleEnemy().getColor());
        enemyPaint.setStyle(Paint.Style.FILL);
        game.moveenemy();
        for (int col = 0; col < TheGame.COLS; col++) {
            for (int row = 0; row < TheGame.ROWS; row++) {
                Direction cell = game.getCells()[col][row];
                int x1 = col * cellSize + paddingX;
                int y1 = row * cellSize + paddingY;
                int x2 = (col + 1) * cellSize + paddingX;
                int y2 = (row + 1) * cellSize + paddingY;
                if (cell.isTopWall()) {
                    canvas.drawLine(x1, y1, x2, y1, wallPaint);
                }
                if (cell.isLeftWall()) {
                    canvas.drawLine(x1, y1, x1, y2, wallPaint); // drawing maze walls to canvas
                }
                if (cell.isBottomWall()) {
                    canvas.drawLine(x1, y2, x2, y2, wallPaint);
                }
                if (cell.isRightWall()) {
                    canvas.drawLine(x2, y1, x2, y2, wallPaint);
                }
                if (col == 0 && row == 0) {
                    canvas.drawRect(x1, y1, x2, y2, exitPaint);
                }
                if (cell.isBunny()) {
                    float bunnyCenterX = game.getBunny().getCol() * cellSize + cellSize / 2 + paddingX;
                    float bunnyCenterY = game.getBunny().getRow() * cellSize + cellSize / 2 + paddingY;
                    canvas.drawCircle(bunnyCenterX, bunnyCenterY, cellSize / 2, bunnyPaint);
                }
                if (cell.isEnemy()) {
                    float enemyCenterX = game.getEnemy().getCol() * cellSize + cellSize / 2 + paddingX;
                    float enemyCenterY = game.getEnemy().getRow() * cellSize + cellSize / 2 + paddingY;
                    canvas.drawCircle(enemyCenterX, enemyCenterY, cellSize / 2, enemyPaint);
                }
                if (cell.isPoint()) { // drawing entities
                    float pointCenterX = game.getPoint().getCol() * cellSize + cellSize / 2 + paddingX;
                    float pointCenterY = game.getPoint().getRow() * cellSize + cellSize / 2 + paddingY;
                    canvas.drawCircle(pointCenterX, pointCenterY, cellSize / 2, pointPaint);
                }
            }
        }
        //FPS COUNTER
        Paint fpsPaint = new Paint();
        fpsPaint.setColor(Color.WHITE);
        fpsPaint.setTextSize(50);
        canvas.drawText("FPS: " + fps/10, canvas.getWidth() - 200, 100, fpsPaint);

        // Draw the score and elapsed time
        Paint paint = new Paint();
        paint.setColor(Color.rgb(93, 63, 211));
        paint.setTextSize(60);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        float margin = cellSize / 10;
        canvas.drawText("SCORE: " + game.getScore(), margin, 300, paint);

        // Draw the level
        Paint levelpaint = new Paint();
        levelpaint.setColor(Color.rgb(93, 63, 211));
        paint.setTextSize(60);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        canvas.drawText("Level: " + game.getLevel(), margin, 200, paint);

        // Draw the timer
        Paint timerPaint = new Paint();
        timerPaint.setColor(Color.WHITE);
        timerPaint.setTextSize(48);
        if (startTime == 0) {
            startTime = System.currentTimeMillis(); // set start time when the view is first drawn
        }
        elapsedTime = (int) ((System.currentTimeMillis() - startTime) / 1000); // calculate elapsed time in seconds
        canvas.drawText("Time: " + elapsedTime + "s", canvas.getWidth() - 300, 300, timerPaint);

// Draw the high score
        if (game.getScore() > highScore) {
            highScore = game.getScore(); // update high score if current score is greater
            // Save high score to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highScore", highScore);
            editor.apply();
        }
        // Draw the high level
        if (game.getLevel() > highLevel) {
            highLevel = game.getLevel(); // update high level if current level is greater
            // Save high level to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences2.edit();
            editor.putInt("highLevel", highLevel);
            editor.apply();
        }

        // Get high score and high level from SharedPreferences
        highScore = sharedPreferences.getInt("highScore", 0);
        highLevel = sharedPreferences2.getInt("highLevel", 0);
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        canvas.drawText("HIGH SCORE: " + highScore, margin, canvas.getHeight() - margin, paint);
        canvas.drawText("HIGH LEVEL: " + highLevel, margin + paint.measureText("HIGH SCORE: " + highScore) + 150, canvas.getHeight() - margin, paint);

        // Draw the health bar
        if (game.getLife() >= 7) {
            healthPaint.setColor(Color.GREEN);
        } else if (game.getLife() <= 5 && game.getLife() > 2) {
            healthPaint.setColor(Color.YELLOW);
        } else if (game.getLife() <= 2) {
            healthPaint.setColor(Color.RED);
        } else if (game.getLife() == 0) {
            gameThread.stopThread();
            Intent gameOverIntent = new Intent(context, GameOver.class);
            gameOverIntent.putExtra("score", game.getScore());
            gameOverIntent.putExtra("level", game.getLevel());
            context.startActivity(gameOverIntent);
        }
        canvas.drawRect(0, 0, canvas.getWidth(), 30, wallPaint);
        canvas.drawRect(0, 0, canvas.getWidth() * game.getLife() / 10, 30, healthPaint);
    }

    /**
     *This method handles user touch events to control the bunny's movements,
     * interact with the exit cell, reset
     * the game if the bunny's(players) life reaches zero.
     * @param event The motion event.
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int bunnyCol = game.getBunny().getCol();
        int bunnyRow = game.getBunny().getRow();

        int paddingX = (getWidth() - (cellSize * TheGame.COLS)) / 2;
        int paddingY = (getHeight() - (cellSize * TheGame.ROWS)) / 2;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int touchX = (int) event.getX();
                int touchY = (int) event.getY();
                int touchCol = (int) ((touchX - paddingX) / cellSize);
                int touchRow = (int) ((touchY - paddingY) / cellSize);

                // If bunny is touching the exit cell, increase bunny's row and col by 2
                if (game.getCells()[bunnyCol][bunnyRow].isExit() && touchCol == 0 && touchRow == 0) {
                    bunnyCol += 2;
                    bunnyRow += 2;
                    levels = levels + 1;
                    startTime = 0; // reset the start time to start counting from 0
                    elapsedTime = 0; // reset the elapsed time to 0
                    return true;
                }
                if (game.getCells()[bunnyCol][bunnyRow].isExit() && touchCol == 0 && touchRow == 0) {
                    bunnyCol += 2;
                    bunnyRow += 2;
                    levels = levels + 1;
                    startTime = 0;
                    elapsedTime = 0;
                    return true;
                }
                if (game.life == 0) {
                    game.score = 0;
                    game.resetGame();
                    game.life = 8;
                }
                // If bunny is touching the exit cell, increase bunny's row and col by 2
                if (game.getCells()[bunnyCol][bunnyRow].isTeleporter() && touchCol == 2 && touchRow == 2) {
                    bunnyCol += 2;
                    bunnyRow += 2;
                    levels = levels + 1;
                    startTime = 0; // reset the start time to start counting from 0
                    elapsedTime = 0; // reset the elapsed time to 0
                    return true;
                }
                int colDiff = touchCol - bunnyCol;
                int rowDiff = touchRow - bunnyRow;

                if (Math.abs(colDiff) > Math.abs(rowDiff)) {
                    if (colDiff > 0) {
                        game.moveBunny(directions.RIGHT);
                    } else {
                        game.moveBunny(directions.LEFT);
                    }
                } else {
                    if (rowDiff > 0) {
                        game.moveBunny(directions.DOWN);
                    } else {
                        game.moveBunny(directions.UP);
                    }
                }
                bunnyCol = game.getBunny().getCol();
                bunnyRow = game.getBunny().getRow();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    /**
     * This method optimizes memory usage by recycling the background image and stopping
     * the game thread when the game view is destroyed.
     */
    public void onDestroy() {
        if (backgroundimg != null) {
            backgroundimg.recycle();
            backgroundimg = null;
        }
        if (gameThread != null) {
            gameThread.stopThread();
            gameThread = null;
        }
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
