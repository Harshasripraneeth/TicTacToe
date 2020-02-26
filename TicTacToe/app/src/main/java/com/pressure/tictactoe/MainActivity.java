package com.pressure.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import android.app.IntentService;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.pressure.tictactoe.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    MediaPlayer mediaPlayer ;
    //player 0: yellow, Player 1:red
    private int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7},
            {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    //-1 indicates that the position is not selected
    private int[] gamestate = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
    private int activePlayer =0;
    //game status = false means game is completed.
    boolean gamestatus = true;
    private int steps =0;
    boolean playerWon =true;
    public void setImage(View view)
    {
        ImageView iv = (ImageView) view;
        int imageTag = Integer.parseInt((String) iv.getTag());
        if(gamestate[imageTag] == -1 && gamestatus) {
            gamestate[imageTag] = activePlayer;
            iv.setTranslationY(-1500);
            if (activePlayer == 0) {
                iv.setImageResource(R.drawable.yellow);
                iv.animate().translationYBy(1500).rotation(3600).setDuration(200);
                activePlayer = 1;
                steps++;
            } else {
                iv.setImageResource(R.drawable.red);
                iv.animate().translationYBy(1500).rotation(3600).setDuration(200);
                activePlayer = 0;
                steps++;
            }

            for (int[] winningposition : winningPositions) {
                if (gamestate[winningposition[0]] != -1 && (gamestate[winningposition[0]] == gamestate[winningposition[1]]) &&
                        (gamestate[winningposition[1]] == gamestate[winningposition[2]])) {
                    gamestatus = false;
                    gameCompleted();
                    break;
                }
            }
            if(steps == gamestate.length)
            {
                playerWon = false;
                gameCompleted();
            }
        }
    }
    public void playAgain(View view)
    {
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        gamestatus = true;
        activePlayer =0;
        playerWon = true;
        for(int i=0; i< mainBinding.gridlayout.getChildCount(); i++) {

            ImageView imageView = (ImageView) mainBinding.gridlayout.getChildAt(i);
            imageView.setImageDrawable(null);
            gamestate[i]=-1;
            mainBinding.setWinner("");
        }
        mainBinding.setProgress(false);
    }
    void gameCompleted()
    {
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
        steps=0;
        if(playerWon) {
            if (activePlayer == 1)
                mainBinding.setWinner("Game won by yellow player");
            else
                mainBinding.setWinner("Game won by Red Player");
        }
        else
            mainBinding.setWinner("DRAW..");
         mainBinding.setProgress(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
      mainBinding.setProgress(false);
        mediaPlayer = new MediaPlayer().create(MainActivity.this,R.raw.cheering );
    }


}
