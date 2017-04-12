package com.rpsnet.game.screens;

import com.badlogic.gdx.Screen;

/**
 * An interface that should be implemented by all screens in the game
 * Contains methods which enable the display of connection and error notifications
 */
public interface NetScreen extends Screen
{
    void updateConnectionInfo(boolean connected);

    void displayErrorMessage(String message);
}
