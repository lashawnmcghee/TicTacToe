/*
 * Copyright (C) 2018 LLM Tic Tac Toe Demo
 */
package com.lashawnmcghee.tictactoe.interfaces;

public interface IGameListener {
    void onPlayerSwitch();
    void onGameDraw();
    void onGameWinner();
}
