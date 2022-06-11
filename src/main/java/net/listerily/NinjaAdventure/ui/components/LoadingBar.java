package net.listerily.NinjaAdventure.ui.components;

import javax.swing.*;

public class LoadingBar extends JLabel {
    private int currentPosition = 0;
    private String theText = "";
    private static final String text = ". . . . . .             ";

    public LoadingBar() {
        super("", JLabel.CENTER);
        new Timer(500, e -> updateText()).start();
    }

    @Override
    public String getText() {
        return theText;
    }

    private synchronized void updateText() {
        currentPosition = (text.length() + (currentPosition - 1)) % text.length();
        theText = text.substring(currentPosition) + text.substring(0, currentPosition);
        repaint(10);
    }
}
