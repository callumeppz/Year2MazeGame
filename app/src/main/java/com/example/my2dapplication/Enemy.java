package com.example.my2dapplication;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

// abstractclass
public abstract class Enemy {
    protected int row;
    protected int col;
    protected Random random;

    /**
     *
     * @param row
     * @param col
     */
    public Enemy(int row, int col) {
        this.row = row;
        this.col = col;
        random = new Random();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void move(TheGame game) {
        randomMove(game);
    }

    /**
     *
     * @param game
     */
    protected void randomMove(TheGame game) {
        int newRow, newCol;
        boolean moved = false;

        while (!moved) {
            newRow = row;
            newCol = col;

            int direction = random.nextInt(4);

            switch (direction) {
                case 0: // Move up
                    newRow--;
                    break;
                case 1: // Move down
                    newRow++;
                    break;
                case 2: // Move left
                    newCol--;
                    break;
                case 3: // Move right
                    newCol++;
                    break;
            }

            if (isValidMove(game, newRow, newCol)) {
                row = newRow;
                col = newCol;
                moved = true;
            }
        }
    }

    /**
     *
     * @param game
     * @param newRow
     * @param newCol
     * @return
     */
    protected boolean isValidMove(TheGame game, int newRow, int newCol) {
        if (newRow < 0 || newRow >= TheGame.ROWS || newCol < 0 || newCol >= TheGame.COLS) {
            return false;
        }
        Direction cell = game.getCells()[newRow][newCol];
        return !(cell.isVisited() || cell.isExit() || cell.isWall());
    }

    public abstract int getColor();

    public abstract void draw(Canvas canvas, int cellSize, int paddingX, int paddingY, Paint paint);

    public abstract void move();
}
