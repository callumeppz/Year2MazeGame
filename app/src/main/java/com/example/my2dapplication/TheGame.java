package com.example.my2dapplication;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


/**
 * TheGame class contains the logic for the game, including maze creation, enemy movement, and game state management.
 */
public class TheGame {

    // variable initialising

    private boolean dragging = false;
    private boolean isDragging = false;
    private SimpleEnemy simpleEnemy;
    public static int COLS = 14; // initialises COLS
    public static int ROWS = 14; // initial ROWS
    public static int level = 0;
    private MovingEnemy movingEnemy;
    private Direction exit, bunny, enemy, point, teleporter;
    private Direction[][] cells; //direction array
    private Random random;
    private static final int SlowerMovement = 2; //slows movement
    private int MoveCount = 0;
    int life = 8; //sets up life

    private int bunnyCurrentCol; //variables for movement
    private int bunnyCurrentRow;
    private int bunnyTargetCol;
    private int bunnyTargetRow;
    private int currentCol;
    private int currentRow;
    int score = 0;
    public TheGame() {
        random = new Random(); // random variable declaration
        simpleEnemy = new SimpleEnemy(5, 5);
        movingEnemy = new MovingEnemy(COLS / 2, ROWS / 2);
        init();
    }

    private void init() { // initiaises the game methods
        Mazecreation();
        checkpoints();
        checkenemy();
    }

    /**
     *
     * @param cell
     * The getneighbour function would be used to check each of the four 'neighbours' topwalll,bototmwall,
     * lleftwall, rightwall then add the ones that are not visited to the arraylist, therefore returning an
     * unvisted random neighbour of a cell.
     *
     * @return
     */

    private Direction getneighbour(Direction cell) {
        ArrayList<Direction> neighbours = new ArrayList<>(); // declaring the list to save to
        if (cell.getCol() > 0 && !cells[cell.getCol() - 1][cell.getRow()].isVisited()) { // Check for an unvisited left neighbour and add it to the list if present
            neighbours.add(cells[cell.getCol() - 1][cell.getRow()]);
        }
        if (cell.getCol() < COLS - 1 && !cells[cell.getCol() + 1][cell.getRow()].isVisited()) { // Check for an unvisited right neighbour and add it to the list if present
            neighbours.add(cells[cell.getCol() + 1][cell.getRow()]);
        }
        if (cell.getRow() > 0 && !cells[cell.getCol()][cell.getRow() - 1].isVisited()) { // ^^ but top
            neighbours.add(cells[cell.getCol()][cell.getRow() - 1]);
        }
        if (cell.getRow() < ROWS - 1 && !cells[cell.getCol()][cell.getRow() + 1].isVisited()) { //^^ but bottom
            neighbours.add(cells[cell.getCol()][cell.getRow() + 1]);
        }
        if (!neighbours.isEmpty()) { // returns a random unvisited neighbour
            int index = random.nextInt(neighbours.size());
            return neighbours.get(index); // returns neighhbour
        }
        return null; // if no unvisited neighbours, null is returned
    }

    /**
     * removeWall is used to remove the wall between the current and next cell, checks the position of the two cells
     * then removes the appropriate walls between cells, creating a path between the cells.
     * @param current The current cell.
     * @param next The next cell.
     */

    private void removeWall(Direction current, Direction next) {
        // If the current cell is directly above/next to the next cell (same column, one row higher/lower/left/right),
        // remove the top wall of the current cell and the bottom wall of the next cell.
        if (current.getCol() == next.getCol() && current.getRow() == next.getRow() + 1) { // checks cell positions and removes
            current.setTopWall(false);
            next.setBottomWall(false);
        }
        if (current.getCol() == next.getCol() && current.getRow() == next.getRow() - 1) {
            current.setBottomWall(false);
            next.setTopWall(false);
        }
        if (current.getCol() == next.getCol() + 1 && current.getRow() == next.getRow()) {
            current.setLeftWall(false);
            next.setRightWall(false);
        }
        if (current.getCol() == next.getCol() - 1 && current.getRow() == next.getRow()) {
            current.setRightWall(false);
            next.setLeftWall(false);
        }
    }


    /**
     * Mazecreation initialises the maze with new cells using dfs (depth first search) by
     * by making a path through the cells and removing the walls between adjacent cells,allowing for
     * there always to be an exit. 2d Array of cells, there row and column potistions, Starts from the top left
     * then marks then as visited, which a stack will then store. If there is not any unvisited cells, the algorithm
     * would backtrack to the last visited cell. The exit is set to top left and player (bunny) bottom left.
     */

    private void Mazecreation() {
        cells = new Direction[COLS][ROWS]; // creating a 2d array of cells and direction objects to make up the maze
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                cells[col][row] = new Direction(col, row);
            }
        }
        Direction current = cells[0][0]; // initialises the mazecreation from the top left cell[0][0]
        current.setVisited(true);
        Stack<Direction> stack = new Stack<>(); // creating a stack data structure to store visited cells
        do { // dfs untill all visited
            // provides a random unvisited neighbor then marks it as visited
            Direction next = getneighbour(current);
            if (next != null) {
                stack.push(current);
                removeWall(current,
                        next);
                current = next; // set current as visited
                current.setVisited(true);
            } else { // if no neighbours, backtrack to the last cell, use that
                current = stack.pop();
            }
        } while (!stack.isEmpty());
        exit = cells[COLS - 1][ROWS - 1]; // exit cell
        exit.setExit(true);
        bunny = cells[0][ROWS - 1]; // bunny (player) start cell
        bunny.setBunny(true);
    }

    /**
     * checks if the move is valid (inside bounds of maze) returns false if not and true if so.
     * @param col
     * @param row
     * @return
     */
    protected boolean isValidMove(int col, int row) {
        if (col < 0 || col >= COLS || row < 0 || row >= ROWS) {
            return false;
        }
        return true;
    }

    /**
     * Generates a point at a random position within the maze.
     */
    private void checkpoints() {
        int pointCol = random.nextInt(COLS - 2) + 1;
        int pointRow = random.nextInt(ROWS - 2) + 1;
        point = cells[pointCol][pointRow];
        point.setPoint(true);
    }
    /**
     * Generates an enemy at a random position within the maze.
     */
    private void checkenemy() {
        int enemycol = random.nextInt(COLS - 3) + 1;
        int enemyrow = random.nextInt(ROWS - 3) + 1;
        enemy = cells[enemycol][enemyrow];
        enemy.setEnemy(true);
    }

    //getters and setters

    public int getScore() {
        return score;
    }
    public void setScore(int newScore) {
        score = newScore;
    }
    public Direction getExit() {
        return exit;
    }
    public Direction getTeleporter() {
        return teleporter;
    }
    public int getLevel() {
        return level;
    }
    public Direction getBunny() {
        return bunny;
    }
    public Direction getEnemy() {
        return enemy;
    }
    public int getLife() {
        return life;
    }

    public Direction getPoint() {
        return point;
    }
    public Direction[][] getCells() {
        return cells;
    }
    public void resetGame() { // restarts the game, recreated the maze, point locations and enemy
        Mazecreation();
        checkpoints();
        checkenemy();
    }

    /**
     * The moveenemy method moves the enemy in a random direction within the maze. It uses the MoveCount variable to
     * slow down the enemy's movement.
     */
    public void moveenemy() { // random movement for enemy
        if (MoveCount < SlowerMovement) { // delays, slows enemy movement
            MoveCount++; // Check if the MoveCount is less than SlowerMovement
            return;
        }
        MoveCount = 0; // Reset the move counter
        int direction2 = new Random().nextInt(4); // Generate a random direction for the enemy to move (0: up, 1: down, 2: left, 3: right)
        switch (direction2) {
            case 0:
                if (!enemy.isTopWall()) { // If there is no top wall, enemy moves, decreasing row
                    enemy = cells[enemy.getCol()][enemy.getRow() - 1];
                }
                break;
            case 1:
                if (!enemy.isBottomWall()) { // If there is no bottom wall, enemy moves, increasing row
                    enemy = cells[enemy.getCol()][enemy.getRow() + 1];
                }
                break;
            case 2:
                if (!enemy.isLeftWall()) { // If there is no left wall, enemy moves, decreasing col
                    enemy = cells[enemy.getCol() - 1][enemy.getRow()];
                }
                break;
            case 3:
                if (!enemy.isRightWall()) { // If there is no right wall, enemy moves, increasing col
                    enemy = cells[enemy.getCol() + 1][enemy.getRow()];
                }
                break;
        }
    }

    /**
     * The moveBunny method moves the bunny (player) within the maze in the specified direction, avoiding walls.
     * It also updates the game state (score, life, and level) based on the bunny's movement and interactions with
     * other game elements.
     *
     * @param direction the desired direction for the bunny to move
     */
    public void moveBunny(directions direction) {
        int newCol = bunny.getCol();
        int newRow = bunny.getRow();

        switch (direction) {
            case UP:
                // If no top wall, move bunny(player) up by decreasing the row
                if (!cells[bunny.getCol()][bunny.getRow()].isTopWall()) {
                    newRow--;
                }
                break;
            case DOWN:
                // If no bottom wall, move bunny(player) down by increasing the row
                if (!cells[bunny.getCol()][bunny.getRow()].isBottomWall()) {
                    newRow++;
                }
                break;
            case LEFT:
                // If no left wall, move bunny(player) left by decreasing the col
                if (!cells[bunny.getCol()][bunny.getRow()].isLeftWall()) {
                    newCol--;
                }
                break;
            case RIGHT:
                // If no right wall, move bunny(player) right by increasing the col
                if (!cells[bunny.getCol()][bunny.getRow()].isRightWall()) {
                    newCol++;
                }
                break;
        }
        //Check if the new position is valid
        if (isValidMove(newCol, newRow)) {
            if (bunny.getCol() == enemy.getCol() && bunny.getRow() == enemy.getRow()) {
                life--; // If bunny moves to the enemy's position, decrease life by 1
            } else {
                cells[bunny.getCol()][bunny.getRow()].setBunny(false);
                bunny.setCol(newCol);
                bunny.setRow(newRow);
                cells[bunny.getCol()][bunny.getRow()].setBunny(true);

                if (bunny.getCol() == point.getCol() && bunny.getRow() == point.getRow()) {
                    cells[point.getCol()][point.getRow()].setPoint(false);
                    score = score + 1; // if bunny and point on same position score and life increases, point moves
                    life =life + 1;
                    checkpoints();
                }
                if (bunny.getCol() == 0 && bunny.getRow() == 0) {
                    ROWS += 2; // if exit, increase rows and cols, and increase level by 1
                    COLS += 2;
                    level += 1;
                    init();
                }
                }
            }
        }

    /**
     * Getter method for the simpleEnemy object.
     *
     * @return the SimpleEnemy object
     */
    public Enemy getSimpleEnemy() {
        return simpleEnemy;
    }
}



