/*
 * Copyright (C) 2018 LLM Tic Tac Toe Demo
 */
package com.lashawnmcghee.tictactoe.interfaces;

public interface IGameListener {
    void onPlayerSwitch();
    void onComputerMove(String move);
    void onGameDraw();
    void onGameWinner();
}
