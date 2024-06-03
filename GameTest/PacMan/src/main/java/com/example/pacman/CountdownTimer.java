package com.example.pacman;

import com.example.pacman.UI.Model;

public class CountdownTimer extends Thread {
    private final Model model;

    public CountdownTimer(Model model) {
        this.model = model;
    }

    @Override
    public void run() {
        while (model.getCountdown() > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            model.decrementCountdown();
            model.repaint();
        }
        model.startTimer();
    }
}
