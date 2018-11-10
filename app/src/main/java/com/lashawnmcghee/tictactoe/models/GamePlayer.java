/*
 * Copyright (C) 2018 LLM Tic Tac Toe Demo
 */
package com.lashawnmcghee.tictactoe.models;

import com.lashawnmcghee.tictactoe.interfaces.IGameDefines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a player of a Game.
 * Each player keeps track of their own moves and whether they won.
 * The object may be reused each new game by calling reset().
 *
 * When a new game starts:
 * 1. Call reset()
 * 2. Set the player type setType()
 * 3. When a move is made, call addMove()
 * 4. After adding a move, check if the player won by calling isWinner()
 */
public class GamePlayer implements IGameDefines {
    private static final String TAG = GamePlayer.class.getSimpleName();

    private int mType;
    private boolean mIsWinner;
    private int mWinningCombo;
    private List<String> mMoves;
    private int[] mCounts = new int[] {0,0,0,0,0,0,0,0};

    /**
     * Default constructor.
     */
    public GamePlayer() {
        reset();
    }

    /**
     * Resets the player to default with no moves.
     */
    public void reset() {
        // player has not won
        mIsWinner = false;

        // there is no winning combo
        mWinningCombo = -1;

        // player is human
        mType = Player.Types.PLAYER_TYPE_HUMAN;

        // no moves have been made
        if(mMoves == null) {
            mMoves = new ArrayList<>(10);
        } else {
            mMoves.clear();
        }

        // all winning combo counts are zero
        for(int idx = 0; idx < mCounts.length ; idx++) {
            mCounts[idx] = 0;
        }
    }

    /**
     * Set the player type safely.
     * @param type Type of player as defined in Player.Types.
     *             Human by default anc computer otherwise.
     */
    public void setType(int type) {
        switch (type) {
            case Player.Types.PLAYER_TYPE_HUMAN:
                mType = Player.Types.PLAYER_TYPE_HUMAN;
                break;
            case Player.Types.PLAYER_TYPE_COMPUTER:
            default:
                mType = Player.Types.PLAYER_TYPE_COMPUTER;
                break;
        }
    }

    /**
     * Get the player type.
     * @return Returns and integer for the player type as defined in Player.Types.
     */
    public int getType() {
        return mType;
    }

    /**
     * Add a string representing a player move.
     * @param move String representing an available move the game board. It is up to the caller to
     *             ensure the move is valid before adding it.
     */
    public void addMove(String move) {
        mMoves.add(move);

        //update combo counts and check for win
        for(int idx = 0; idx < WinningCombos.COMBOS.length; idx++) {
            if(WinningCombos.COMBOS[idx].contains(move)) {
                mCounts[idx]++;

                //WE WON!!!
                if(mCounts[idx] >= Game.WINNING_COUNT) {
                    mIsWinner = true;
                    mWinningCombo = idx;
                    break;
                }
            }
        }
    }

    /**
     * Get the list of player moves.
     * @return
     */
    public List<String> getMoves() {
        return Collections.unmodifiableList(mMoves);
    }

    /**
     * Lets caller know if this play has won.
     * @return Returns true if play has a winning combination and false otherwise.
     */
    public boolean isWinner() {
        return mIsWinner;
    }

    /**
     * Gets the index of the winning combination as defined in WinningCombos.
     * @return
     */
    public int getWinningCombo() {
        return mWinningCombo;
    }

    /**
     * Gets the players combo counts.
     * The index into these counts matches with the index of combos in WinningCombos.
     * A count of 3 or more is a winner.
     * This is mainly used as a UI helper.
     * @return
     */
    public int[] getCounts() {
        return Arrays.copyOf(mCounts, mCounts.length);
    }
}
