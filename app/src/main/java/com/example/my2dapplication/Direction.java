package com.example.my2dapplication;


public class Direction {
    //declares
    private boolean isWall;
    private int col;
    private int row;
    private boolean visited;
    private boolean exit;
    private boolean bunny;
    private boolean enemy;
    private boolean point;
    private boolean topWall;
    private boolean bottomWall;
    private boolean rightWall;
    private boolean leftWall;
    private boolean simpleEnemy;
    private boolean movingEnemy;


    /**
     *
     * @param col
     * @param row
     */
    public Direction(int col, int row) {
        this.col = col;
        this.row = row;
        this.visited = false;
        this.exit = false;
        this.bunny = false;
        this.enemy = false;
        this.point = false;
        this.topWall = true;
        this.bottomWall = true;
        this.rightWall = true;
        this.leftWall = true;
    }

    //getters and setters
    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isExit() {
        return exit;
    }
    public boolean isTeleporter() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public void setTeleporter(boolean exit) {
        this.exit = exit;
    }
    public boolean isBunny() {
        return bunny;
    }

    public void setBunny(boolean bunny) {
        this.bunny = bunny;
    }

    public boolean isEnemy() {
        return enemy;
    }
    public void setEnemy(boolean enemy) {
        this.enemy = enemy;
    }

    public boolean isPoint() {
        return point;
    }

    public void setPoint(boolean point) {
        this.point = point;
    }

    public boolean isTopWall() {
        return topWall;
    }

    public void setTopWall(boolean topWall) {
        this.topWall = topWall;
    }

    public boolean isBottomWall() {
        return bottomWall;
    }

    public void setBottomWall(boolean bottomWall) {
        this.bottomWall = bottomWall;
    }

    public boolean isLeftWall() {
        return leftWall;
    }

    public void setLeftWall(boolean leftWall) {
        this.leftWall = leftWall;
    }

    public boolean isRightWall() {
        return rightWall;
    }

    public void setRightWall(boolean rightWall) {
        this.rightWall = rightWall;
    }

    public Direction(boolean isWall) {
        this.isWall = isWall;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }

    public void setSimpleEnemy(boolean b) {
    }

    public void setMovingEnemy(boolean b) {
    }
}

