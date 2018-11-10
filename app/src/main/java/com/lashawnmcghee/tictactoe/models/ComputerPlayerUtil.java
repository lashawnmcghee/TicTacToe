/*
 * Copyright (C) 2018 LLM Tic Tac Toe Demo
 */
package com.lashawnmcghee.tictactoe.models;

import com.lashawnmcghee.tictactoe.interfaces.IGameDefines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Handler;

public class ComputerPlayerUtil implements IGameDefines {
    private static final String TAG = ComputerPlayerUtil.class.getSimpleName();

    private static ComputerPlayerUtil instance = null;


    //Supplied by the caller...
    private int mComputerLevel = Player.Computer.LEVEL_EASY;
    private int[] mMyCounts;
    private int[] mOtherCounts;
    private boolean[] mPlays;
    private Handler mListenerHandler;

    /**
     * Hidden class constructor.
     */
    private ComputerPlayerUtil() {
        init();
    }

    /**
     * Get the one and only instance of this class.
     * The first calling thread will create an initial instance.
     * This method will only be synchronized on the first call,
     * thus it will not affect speed of our app.
     * @return Returns and instance of this class.
     */
    public static ComputerPlayerUtil getInstance() {
        synchronized(ComputerPlayerUtil.class) {
            if (instance == null) {
                instance = new ComputerPlayerUtil();
            }
        }
        return instance;
    }

    /**
     * Release the current instance of this manager.
     */
    public static void release() {
        if(instance != null) {
            instance = null;
        }
    }

    /**
     * Initialization of class workers.
     */
    private void init() {
        //Do nothing...
    }

    /**
     * Gets the next computer move based on level settings.
     * @param level Computer level as defined by Player.Computer
     * @param plays Board positions where true = played and false = open.
     * @param mine Move maker's combo counts
     * @param other Opponents combo counts
     * @return
     */
    public String getNextMove(int level, boolean[] plays, int[] mine, int[] other) {
        String sNextMove = Game.INVALID_MOVE;

        if(checkPlays(plays)) {
            switch (level) {
                case Player.Computer.LEVEL_EASY:
                    sNextMove = getNextEasyMove(plays);
                    break;
//                case Player.Computer.LEVEL_MEDIUM:
//                    sNextMove = getNextMediumMove(plays, mine);
//                    break;
                case Player.Computer.LEVEL_HARD:
                    sNextMove = getNextHardMove(plays, mine, other);
                    break;
                default:
                    sNextMove = Game.INVALID_MOVE;
                    break;
            }
        }

        return sNextMove;
    }

    /**
     * Validate board plays. If all are true then there are no available moves.
     * This will prevent ANR.
     * @param plays Board plays.
     * @return Returns true if moves are available and false otherwise.
     */
    private boolean checkPlays(boolean[] plays) {
        for(boolean bPlayed : plays) {
            if(!bPlayed) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the next computer play by random function.
     * @param plays Board positions where true = played and false = open.
     * @return Returns a string representing the board position to play next.
     */
    private String getNextEasyMove(boolean[] plays) {
        String sNextMove = Game.INVALID_MOVE;
        Random random = new Random(System.currentTimeMillis());
        boolean bDone = false;

        while(!bDone) {
            int iMove = random.nextInt(plays.length);
            if(!plays[iMove]) {
                bDone = true;
                sNextMove = Game.VALID_MOVES[iMove];
            }
        }

        return sNextMove;
    }

//    /**
//     * Get the next computer play by using best scoring and/or blocking player.
//     * @param plays Board positions where true = played and false = open.
//     * @param mine Move maker's combo counts
//     * @return Returns a string representing the board position to play next.
//     */
//    private String getNextMediumMove(boolean[] plays, int[] mine) {
//        int iMoveScore = -1;
//        String sMove = Game.INVALID_MOVE;
//
//        //TODO: add same score moves to a list and randomize those...
//        for(int idx = 0; idx < plays.length; idx++) {
//            if(!plays[idx]) {
//                int iTemp = getMoveScore(Game.VALID_MOVES[idx], mine);
//                if(iTemp > iMoveScore) {
//                    iMoveScore = iTemp;
//                    sMove = Game.VALID_MOVES[idx];
//                }
//            }
//        }
//
//        return sMove;
//    }

    /**
     * Get the next computer play by attempting to win, block player, best scoring, or random.
     * @param plays Board positions where true = played and false = open.
     * @param mine Move maker's combo counts
     * @param other Opponents combo counts
     * @return Returns a string representing the board position to play next.
     */
    private String getNextHardMove(boolean[] plays, int[] mine, int[] other) {
        int iMyMoveScore = -1;
        int iOppMoveScore = -1;
        List<String> sMoveList = null;
        String sMove = Game.INVALID_MOVE;

        //Gather a list of moves with highest same score
        for(int idx = 0; idx < plays.length; idx++) {
            if(!plays[idx]) {
                int iTemp = getMoveScore(Game.VALID_MOVES[idx], mine);
                if(iTemp > iMyMoveScore) {
                    iMyMoveScore = iTemp;
                    sMoveList = new ArrayList<>(9);
                    sMoveList.add(Game.VALID_MOVES[idx]);
                } else if (iTemp == iMyMoveScore) {
                    sMoveList.add(Game.VALID_MOVES[idx]);
                }
            }
        }

        //Now chose a move that benefits us
        if(sMoveList.size() > 1) {
            Random rnd = new Random(System.currentTimeMillis());
            int iRandomIndex = rnd.nextInt(sMoveList.size());
            sMove = sMoveList.get(iRandomIndex);
        } else {
            sMove = sMoveList.get(0);
        }

        //Gather a list of opponent moves with highest same score
        for(int idx = 0; idx < plays.length; idx++) {
            if(!plays[idx]) {
                int iTemp = getMoveScore(Game.VALID_MOVES[idx], other);
                if(iTemp > iOppMoveScore) {
                    iOppMoveScore = iTemp;
                    sMoveList = new ArrayList<>(9);
                    sMoveList.add(Game.VALID_MOVES[idx]);
                } else if (iTemp == iOppMoveScore) {
                    sMoveList.add(Game.VALID_MOVES[idx]);
                }
            }
        }

        //did we find an opponent winner?
        if((iOppMoveScore == Game.WINNING_COUNT) && (iMyMoveScore < Game.WINNING_COUNT)) {
            sMove = sMoveList.get(0);
        }

        return sMove;
    }

    /**
     * Gets the score for a proposed move.
     * @param move Move as defined by Game.VALID_MOVES
     * @param counts Current counts from the player making the proposed move.
     * @return
     */
    private int getMoveScore(String move, int[] counts) {
        int iScore = 0;
        for(int idx = 0; idx < WinningCombos.COMBOS.length; idx++) {
            if(WinningCombos.COMBOS[idx].contains(move)) {
                int iTemp = counts[idx] + 1;
                if(iTemp > iScore) {
                    iScore = iTemp;
                }
            }
        }
        return iScore;
    }
}
