package com.sixtel.eggtimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    SeekBar timerBar;
    TextView timerDisplay;
    Button timerButton;
    Boolean timerIsRunning = false;
    CountDownTimer countDownTimer;


    public void setTimerDisplay(int progress) {
        int minutes = progress / 60;
        int seconds = progress - minutes * 60;

        String minutesString = String.valueOf(minutes);
        String secondsString = String.valueOf(seconds);
        minutesString = (minutesString.length() == 1) ? "0" + minutesString : minutesString;
        secondsString = (secondsString.length() == 1) ? "0" + secondsString : secondsString;

        timerDisplay.setText(minutesString + ":" + secondsString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        timerButton = (Button) findViewById(R.id.setTimerButton);  //get the go button
        timerBar = (SeekBar) findViewById(R.id.timerBar);  //get the seekbar that sets time
        timerDisplay = (TextView) findViewById(R.id.timerDisplay); //get the textview that displays timer

        /* set up the seekbar */
        timerBar.setMax(600);
        timerBar.setProgress(30);
        timerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setTimerDisplay(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startTimer(View view) {
        if (timerIsRunning == false) {

            final int time = timerBar.getProgress() * 1000; //time to count down to in milliseconds
            timerBar.setEnabled(false);
            timerButton.setText("Stop");
            timerIsRunning = true;


            countDownTimer = new CountDownTimer(time + 100, 1000) {  //adding a little buffer java to call process
                @Override
                public void onTick(long millisUntilFinished) {

                    long timeLeftInSeconds = millisUntilFinished / 1000;

                    setTimerDisplay((int) timeLeftInSeconds);

                }

                @Override
                public void onFinish() {

                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.horn);
                    timerDisplay.setText("00:00");
                    mp.start();
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(500);
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    timerDisplay.startAnimation(anim);

                    new CountDownTimer(5000, 5000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }
                        @Override
                        public void onFinish() {
                            timerDisplay.clearAnimation();
                            timerReset();
                        }
                    }.start();
                }
            }.start();
        } else {
            countDownTimer.cancel();
            timerReset();
        }

    }

    //resets timer back to defaults
    public void timerReset() {
        timerDisplay.setText("00:30");
        timerBar.setProgress(30);
        timerIsRunning = false;
        timerButton.setText("GO!");
        timerBar.setEnabled(true);
    }



}
