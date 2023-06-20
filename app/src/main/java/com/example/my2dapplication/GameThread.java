package com.example.my2dapplication;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//GameThread class for implementing the threading capabilities
public class GameThread implements Runnable {
    private boolean running;
    private final GameView gameView;
    private final int targetFPS = 120;
    private final long targetTime = 1000 / targetFPS;
    private final Object lock = new Object();
    private ScheduledThreadPoolExecutor executor;
    private boolean paused = false;

    //initialises gamethread within the current gameview
    public GameThread(GameView gameView) {
        this.gameView = gameView;
    }
    //setter for running variable
    public void setRunning(boolean running) {
        this.running = running;
    }

    public void pauseThread() {
        synchronized (lock) {
            paused = true;
        }
    }
    // cleans up resources when the thread is destroyed
    //memory optimisation here
    public void destroyThread() {
        stopThread();
        gameView.onDestroy();
    }

    // resumes the thread when it is paused, synchronized keyword ensures that only one thread can execute this method at a time,
    // preventing race conditions or other thread safety issues.
    public void resumeThread() {
        synchronized (lock) {
            paused = false;
            lock.notify();
        }
    }

    // starts the thread using ScheduledThreadPoolExecutor
    public void startThread() {
        if (executor == null || executor.isShutdown()) {
            executor = new ScheduledThreadPoolExecutor(5);
            executor.scheduleAtFixedRate(this, 0, targetTime, TimeUnit.MILLISECONDS);
        }
    }

    //stops the thread stopping ScheduledThreadPoolExecutor
    public void stopThread() {
        if (executor != null) {
            executor.shutdown();
            try {
                executor.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                // Handle the interruption
            }
            executor = null;
        }
    }

    /**
     *  Gamethread main loop, updates the gameview and the FPS
     */
    @Override
    public void run() {
        if (running) {
            synchronized (lock) {
                while (paused) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        // Handle the interruption
                    }
                }
            }
            long startTime = System.nanoTime();
            gameView.postInvalidate();
            long endTime = System.nanoTime();
            int fps = (int) (1000000000 / (endTime - startTime));
            fps = fps /10;
            gameView.update(fps);
        }
    }
    //getter
    public GameView getGameView() {
        return gameView;
    }
}
