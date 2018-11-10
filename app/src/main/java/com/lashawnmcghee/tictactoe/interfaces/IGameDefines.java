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

        interface Computer {
            int LEVEL_EASY    = 0;
            int LEVEL_HARD    = 1;
        }
    }

    /**
     * Gameboard
     */
    interface Game {
        int WINNING_COUNT = 3;
        String INVALID_MOVE = "-1";
        String[] VALID_MOVES = new String[] {"0","1","2","3","4","5","6","7","8"};

        interface Types {
            int PLAYER_VS_PLAYER        = 0;
            int PLAYER_VS_COMPUTER_EASY = 1;
            int PLAYER_VS_COMPUTER_HARD = 2;
        }
    }

    /**
     * Winning Combinations (zero based)
     */
    interface WinningCombos {
        int INVALID_COMBO = -1;
        String[] COMBOS = new String[] { "012","345","678","036","147","258","048","246"};
    }
}
