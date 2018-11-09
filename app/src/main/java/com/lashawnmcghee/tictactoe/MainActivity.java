/*
 * Copyright (C) 2018 LLM Tic Tac Toe Demo
 */
package com.lashawnmcghee.tictactoe;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lashawnmcghee.tictactoe.interfaces.IGameDefines;
import com.lashawnmcghee.tictactoe.interfaces.IGameListener;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity implements IGameListener, IGameDefines {
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();

    private GameManager mGameManager = GameManager.getInstance();
    private boolean mIsGameOver = false;

    @BindView(R.id.fullscreen_content)
    View mContentView;

    @BindView(R.id.iv_player_one_icon)
    ImageView mPlayerOneIcon;
    @BindView(R.id.iv_player_two_icon)
    ImageView mPlayerTwoIcon;

    @BindView(R.id.tv_reset)
    TextView mResetButton;

    @OnClick(R.id.tv_reset)
    public void onResetButton(View view) {
        view.setVisibility(View.GONE);
        resetGameBoard();
    }

    @BindViews({R.id.iv_one, R.id.iv_two, R.id.iv_three,
            R.id.iv_four, R.id.iv_five, R.id.iv_six, R.id.iv_seven, R.id.iv_eight, R.id.iv_nine})
    List<ImageView> mBoardPositions;

    @OnClick({R.id.iv_one, R.id.iv_two, R.id.iv_three,
            R.id.iv_four, R.id.iv_five, R.id.iv_six, R.id.iv_seven, R.id.iv_eight, R.id.iv_nine})
    public void spaceSelect(ImageView view) {
        if(!mIsGameOver) {
            //get current player for icon
            int iCurrentPlayer = GameManager.getInstance().getCurrentPlayer();

            //let the game manager know what space was selected
            boolean bGood = GameManager.getInstance().addPlayerMove(view.getContentDescription().toString());

            if(bGood) {
                //set the view source to the player icon
                if (iCurrentPlayer == Player.ONE) {
                    // X
                    view.setImageResource(R.drawable.baseline_close_24);
                } else {
                    // O
                    view.setImageResource(R.drawable.baseline_panorama_fish_eye_24);
                }
            }
        }
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mVisible = true;

        //bind ui elements
        ButterKnife.bind(this);
        init();
    }

    /**
     * Init ui functionality
     */
    private void init() {
        for(int index = 0; index < mBoardPositions.size(); index ++) {
            ImageView view = mBoardPositions.get(index);
            view.setContentDescription(Game.VALID_MOVES[index]);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        populateGameBoard();
    }

    @Override
    protected void onStart() {
        super.onStart();

        GameManager.getInstance().addListener(this);
        populateGameBoard();
    }

    @Override
    protected void onStop() {
        super.onStop();

        GameManager.getInstance().removeListener(this);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    /**
     * Resets the game and the visual game board
     */
    private void resetGameBoard() {
        //remove images from the board
        for(ImageView iv : mBoardPositions) {
            iv.setImageResource(R.drawable.baseline_holder_24);
            iv.setBackgroundResource(R.drawable.rounded_primary);
        }

        //reset the game
        mGameManager.resetGame();

        //set flag
        mIsGameOver = false;
    }

    /**
     * Populate game board with existing game state
     */
    private void populateGameBoard() {
        List<String> lstPlayer1 = mGameManager.getPlayerMoves(Player.ONE);
        List<String> lstPlayer2 = mGameManager.getPlayerMoves(Player.TWO);

        //show player 1 moves
        for(String move : lstPlayer1) {
            int iMoveIndex = mGameManager.getIndexOfMove(move);
            ImageView iv = mBoardPositions.get(iMoveIndex);
            iv.setImageResource(R.drawable.baseline_close_24);
        }

        //show player 2 moves
        for(String move : lstPlayer2) {
            int iMoveIndex = mGameManager.getIndexOfMove(move);
            ImageView iv = mBoardPositions.get(iMoveIndex);
            iv.setImageResource(R.drawable.baseline_panorama_fish_eye_24);
        }

        onPlayerSwitch();
    }

    @Override
    public void onPlayerSwitch() {
        //after a move, we need to change the highlighted player icon
        if(GameManager.getInstance().getCurrentPlayer() == Player.ONE) {
            mPlayerOneIcon.setBackgroundResource(R.drawable.rounded_button);
            mPlayerTwoIcon.setBackgroundResource(android.R.color.transparent);
        } else {
            mPlayerTwoIcon.setBackgroundResource(R.drawable.rounded_button);
            mPlayerOneIcon.setBackgroundResource(android.R.color.transparent);
        }
    }

    /**
     * Highlight cells that are part of winning combo
     */
    private void showWinningCombo() {
        String winningCombo = mGameManager.getWinningCombo();
        for(ImageView view : mBoardPositions) {
            String sDesc = view.getContentDescription().toString();
            if(winningCombo.contains(sDesc)) {
                view.setBackgroundResource(R.drawable.rounded_white);
            }
        }
    }

    @Override
    public void onGameDraw() {
        mIsGameOver = true;

        String sDraw = getString(R.string.noone_wins);

        Toast toast = Toast.makeText(this, sDraw, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        mResetButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGameWinner() {
        mIsGameOver = true;

        showWinningCombo();

        String sWinner;
        if(mGameManager.getCurrentPlayer() == Player.ONE) {
            sWinner = getString(R.string.player_1_wins);
        } else {
            sWinner = getString(R.string.player_2_wins);
        }

        Toast toast = Toast.makeText(this, sWinner, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        mResetButton.setVisibility(View.VISIBLE);
    }
}
