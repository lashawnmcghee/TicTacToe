/*
 * Copyright (C) 2018 LLM Tic Tac Toe Demo
 */
package com.lashawnmcghee.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lashawnmcghee.tictactoe.interfaces.IGameDefines;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = MenuActivity.class.getSimpleName();

    @BindViews({R.id.tv_player_vs_player, R.id.tv_player_vs_computer_easy, R.id.tv_player_vs_computer_hard, R.id.tv_about})
    List<TextView> mButtonViews;

    @OnClick({R.id.tv_player_vs_player, R.id.tv_player_vs_computer_easy, R.id.tv_player_vs_computer_hard, R.id.tv_about})
    public void onButtonPress(TextView view) {
        switch(view.getId()) {
            case R.id.tv_player_vs_player:
                Intent intentPVP = new Intent(MenuActivity.this, GameActivity.class);
                intentPVP.putExtra("gameType", IGameDefines.Game.Types.PLAYER_VS_PLAYER);
                startActivity(intentPVP);
                break;
            case R.id.tv_player_vs_computer_easy:
                Intent intentPVCE = new Intent(MenuActivity.this, GameActivity.class);
                intentPVCE.putExtra("gameType", IGameDefines.Game.Types.PLAYER_VS_COMPUTER_EASY);
                startActivity(intentPVCE);
                break;
            case R.id.tv_player_vs_computer_hard:
                Intent intentPVCH = new Intent(MenuActivity.this, GameActivity.class);
                intentPVCH.putExtra("gameType", IGameDefines.Game.Types.PLAYER_VS_COMPUTER_HARD);
                startActivity(intentPVCH);
                break;
            case R.id.tv_about:
            default:
                Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                intent.putExtra("gameType", IGameDefines.Game.Types.PLAYER_VS_PLAYER);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind ui elements
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
