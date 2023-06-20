package com.example.my2dapplication;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;


public class MovingEnemy extends Direction {
    private int currentCol;
    private int currentRow;

    /**
     *
     * @param col
     * @param row
     */
    public MovingEnemy(int col, int row) {
        super(col, row);
        currentCol = col;
        currentRow = row;
    }

    /**
     *
     * @param game
     */
    public void moveEnemy(TheGame game) {
        Random random = new Random();
        int direction = random.nextInt(4);
        int newCol = currentCol;
        int newRow = currentRow;

        switch (direction) {
            case 0:
                newRow--;
                if (newRow < 0 || game.getCells()[currentCol][currentRow].isTopWall() || game.getCells()[currentCol][newRow].isBottomWall()) {
                    newRow++; // Undo the change if not valid
                }
                break;
            case 1:
                newRow++;
                if (newRow >= TheGame.ROWS || game.getCells()[currentCol][currentRow].isBottomWall() || game.getCells()[currentCol][newRow].isTopWall()) {
                    newRow--; // Undo the change if not valid
                }
                break;
            case 2:
                newCol--;
                if (newCol < 0 || game.getCells()[currentCol][currentRow].isLeftWall() || game.getCells()[newCol][currentRow].isRightWall()) {
                    newCol++; // Undo the change if not valid
                }
                break;
            case 3:
                newCol++;
                if (newCol >= TheGame.COLS || game.getCells()[currentCol][currentRow].isRightWall() || game.getCells()[newCol][currentRow].isLeftWall()) {
                    newCol--; // Undo the change if not valid
                }
                break;
        }


        if (game.isValidMove(newCol, newRow)) {
            game.getCells()[currentCol][currentRow] = null;
            setCol(newCol);
            setRow(newRow);
            game.getCells()[currentCol][currentRow] = this;
            currentCol = newCol; // update current column value
            currentRow = newRow; // update current row value
        }
    }

    /**
     *
     * @param canvas
     * @param cellSize
     * @param paddingX
     * @param paddingY
     * @param paint
     */
    public void draw(Canvas canvas, int cellSize, int paddingX, int paddingY, Paint paint) {
        float centerX = currentCol * cellSize + cellSize / 2 + paddingX;
        float centerY = currentRow * cellSize + cellSize / 2 + paddingY;
        canvas.drawCircle(centerX, centerY, cellSize / 2, paint);
    }
}
