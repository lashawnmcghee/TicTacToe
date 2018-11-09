/*
 * Copyright (C) 2018 LLM Tic Tac Toe Demo
 */
package com.lashawnmcghee.tictactoe;

import com.lashawnmcghee.tictactoe.interfaces.IGameDefines;
import com.lashawnmcghee.tictactoe.interfaces.IGameListener;
import com.lashawnmcghee.tictactoe.models.GamePlayer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

public class GameManager implements IGameDefines {
    private static final String TAG = GameManager.class.getSimpleName();

    private static GameManager instance = null;

    //players
    private GamePlayer mPlayerOne;
    private GamePlayer mPlayerTwo;

    private int mCurrentPlayer;

    //valid plays (true = played, false = can play)
    private boolean[] mValidPlays;
    List<String> mValidMoves;

    private CopyOnWriteArraySet<IGameListener> mListeners;

    /**
     * Hidden class constructor.
     */
    private GameManager() {
        init();
    }

    /**
     * Get the one and only instance of this class.
     * The first calling thread will create an initial instance.
     * This method will only be synchronized on the first call,
     * thus it will not affect speed of our app.
     * @return Returns and instance of this class.
     */
    public static GameManager getInstance() {
        synchronized(GameManager.class) {
            if (instance == null) {
                instance = new GameManager();
            }
        }
        return instance;
    }

    /**
     * Release the current instance of this manager.
     */
    public static void release() {
        if(instance != null) {
            instance.mListeners = null;
            instance.mPlayerOne = null;
            instance.mPlayerTwo = null;
            instance.mValidPlays = null;
            instance.mValidMoves = null;

            instance = null;
        }
    }

    /**
     * Initialization of class workers.
     */
    private void init() {
        mPlayerOne = new GamePlayer();
        mPlayerTwo = new GamePlayer();
        mCurrentPlayer = Player.ONE;

        mListeners = new CopyOnWriteArraySet<>();
        mValidPlays = new boolean[] {false,false,false,false,false,false,false,false,false};
        mValidMoves = Arrays.asList(Game.VALID_MOVES);
    }

    /**
     * Resets the current game and resets players.
     */
    public void resetGame() {
        mPlayerOne.reset();
        mPlayerTwo.reset();
        mCurrentPlayer = Player.ONE;

        for(int idx = 0; idx < mValidPlays.length; idx++) {
            mValidPlays[idx] = false;
        }

        notifyGameListenersOfSwitch();
    }

    /**
     * Add a listener to know when a download or remove is complete.
     * @param listener A class which implements the IGameListener interface.
     */
    public void addListener(IGameListener listener) {
        mListeners.add(listener);
    }

    /**
     * Remove a listener from this tracker.
     * @param listener A class which implements the IGameListener interface.
     */
    public void removeListener(IGameListener listener) {
        mListeners.remove(listener);
    }

    /**
     * Notifies listeners that a game winner has been determined.
     */
    private void notifyGameListenersOfWinner() {
        for(IGameListener gl : mListeners) {
            gl.onGameWinner();
        }
    }

    /**
     * Notifies listeners that a game has resulted in a draw.
     */
    private void notifyGameListenersOfDraw() {
        for(IGameListener gl : mListeners) {
            gl.onGameDraw();
        }
    }

    /**
     * Notifies listeners of player turn switch.
     */
    private void notifyGameListenersOfSwitch() {
        for(IGameListener gl : mListeners) {
            gl.onPlayerSwitch();
        }
    }

    /**
     * Get the current active player value as defined by Player (ONE, TWO)
     * @return
     */
    public int getCurrentPlayer() {
        return mCurrentPlayer;
    }

    /**
     * Toggles the currently active player.
     */
    private void toggleCurentPlayer() {
        if(mCurrentPlayer == Player.ONE) {
            mCurrentPlayer = Player.TWO;
        } else {
            mCurrentPlayer = Player.ONE;
        }
    }

    /**
     * If we have a winner, provide the winning combo string.
     * @return String value of the winning combo. If there is no winner, a null string is passed.
     */
    public String getWinningCombo() {
        if(mPlayerOne.isWinner()) {
            int iOneIndex = mPlayerOne.getWinningCombo();
            return WinningCombos.COMBOS[iOneIndex];
        } else if(mPlayerTwo.isWinner()) {
            int iTwoIndex = mPlayerTwo.getWinningCombo();
            return WinningCombos.COMBOS[iTwoIndex];
        } else {
            return NULL_STRING;
        }
    }

    /**
     *
     * @return
     */
    private boolean isGameDraw() {
        boolean bGameDraw = true;

        for(boolean isPlayed : mValidPlays) {
            if(!isPlayed) {
                bGameDraw = false;
                break;
            }
        }

        return bGameDraw;
    }

    /**
     * Adds a move to the game.
     * It is up to this manager to determine who made the move and whether it is a winning move.
     * As well, this manager should determine if it should start a new game if there are no winning
     * moves left.
     * @param move
     * @return Returns true if move and play was valid and false otherwise.
     */
    public boolean addPlayerMove(String move) {
        boolean bValid = false;
        if(mValidMoves.contains(move)) {
            int iMove = mValidMoves.indexOf(move);
            if(!mValidPlays[iMove]) {
                mValidPlays[iMove] = true;

                //grab the current player object and add the move
                GamePlayer player = mCurrentPlayer == Player.ONE ? mPlayerOne : mPlayerTwo;
                player.addMove(move);

                if(player.isWinner()) {
                    notifyGameListenersOfWinner();
                } else if(isGameDraw()) {
                    notifyGameListenersOfDraw();
                } else {
                    toggleCurentPlayer();
                    notifyGameListenersOfSwitch();
                }

                bValid = true;
            }
        }

        return bValid;
    }

    /**
     * Gets a list of player moves.
     * @param player Value of player as defined in Player. (ONE, TWO)
     * @return
     */
    public List<String> getPlayerMoves(int player) {
        if(player == Player.ONE) {
            return mPlayerOne.getMoves();
        } else {
            return mPlayerTwo.getMoves();
        }
    }

    /**
     * Gets the index of a valid board move.
     * @param move
     * @return Returns a positive index. If the move is not a valid value, -1 is returned.
     */
    public int getIndexOfMove(String move) {
        return mValidMoves.indexOf(move);
    }
}
