/*
 * Copyright (C) 2018 LLM Tic Tac Toe Demo
 */
package com.lashawnmcghee.tictactoe.interfaces;

public interface IGameDefines {

    String NULL_STRING = "";

    /**
     * Player defines
     */
    interface Player {
        int ONE = 1;
        int TWO = 2;

        interface Types {
            int PLAYER_TYPE_COMPUTER   = 0;
            int PLAYER_TYPE_HUMAN      = 1;
        }
    }

    /**
     * Gameboard
     */
    interface Game {
        String[] VALID_MOVES = new String[] {"0","1","2","3","4","5","6","7","8"};
    }

    /**
     * Winning Combinations (zero based)
     */
    interface WinningCombos {
        int INVALID_COMBO = -1;
        String[] COMBOS = new String[] { "012","345","678","036","147","258","048","246"};
    }
}
