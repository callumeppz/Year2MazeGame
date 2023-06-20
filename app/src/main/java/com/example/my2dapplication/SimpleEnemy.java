package com.example.my2dapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class SimpleEnemy extends Enemy {
    private int colour;

    public SimpleEnemy(int row, int col) {
        super(row, col);
        colour = Color.rgb(255, 0, 0); // Red
    }

    public int getColor() {
        return colour;
    }

    @Override
    public void draw(Canvas canvas, int cellSize, int paddingX, int paddingY, Paint paint) {

    }

    @Override
    public void move() {
        int newRow, newCol;
        boolean moved = false;

        while (!moved) {
            newRow = getRow();
            newCol = getCol();

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

            }
        }
    }


