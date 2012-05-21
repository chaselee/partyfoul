package com.drunk;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Results extends Activity {
	
	private Button addDrink;
	private Button play;
	
	private TextView status;
	private Resources res;
	
	private int game;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        res = this.getResources();
        
        addDrink = (Button) findViewById(R.id.adddrink);
        addDrink.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent next_intent = new Intent(getApplicationContext(), com.drunk.Drink.class);
				startActivity(next_intent);
				finish();
			}
		});
        
        status = (TextView) findViewById(R.id.status);
        
        Intent myIntent = getIntent();
        game = myIntent.getIntExtra("Game", 0);
        
        play = (Button) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Random rndGen = new Random();
				int next_game = rndGen.nextInt(3);
				while(next_game == game) {
					next_game = rndGen.nextInt(3);
				}
				
				Intent next_intent;
				next_intent = new Intent(getApplicationContext(), com.drunk.Bubblepop.class);
			
				if(next_game == 1){
					next_intent = new Intent(getApplicationContext(), com.drunk.Liteup.class);
				}
				else if(next_game == 2){
					next_intent = new Intent(getApplicationContext(), com.drunk.Roller.class);
				}
				writeInt("lastgame", next_game);
				startActivity(next_intent);
				finish();
				
			}
		});
        
        
        
        
        LinearLayout ll = (LinearLayout) findViewById(R.id.blankll);
        
        final Context context = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        if(game == 0 || game == 1) {

        	
        	inflater.inflate(R.layout.bubblescore, ll);
        	
        	TextView scoreview = (TextView) findViewById(R.id.bblscore);
        	TextView popview = (TextView) findViewById(R.id.popped);
        	
        	
        	
        	if(game == 0) {
            	int score = myIntent.getIntExtra("Score", 0);
            	int popped = myIntent.getIntExtra("Popped", 0);
            	if(score <= 0) {
            		status.setText(res.getString(R.string.Shitfaced));
            	}
            	else if(score <= 90) {
            		status.setText(res.getString(R.string.Drunk));
            	}
            	else if(score <= 180) {
            		status.setText(res.getString(R.string.slightly_drunk));
            	}
            	else if(score <= 270){
            		status.setText(res.getString(R.string.Buzzed));
            	}
            	else if(score <= 360){
            		status.setText(res.getString(R.string.Well));
            	}
            	else if(score <= 555) {
            		status.setText(res.getString(R.string.Sober));
            	}
            	
            	scoreview.setText("Score: " + score);
            	float percent = Math.round((((float) popped)/52) * 100);
            	popview.setText("Pop %: " + (int) percent +"%");
        	}
        	else if(game == 1) {
        		int score = myIntent.getIntExtra("Score", 0);
        		int appeared = myIntent.getIntExtra("appeared", 0);
        		Log.d("appeared", ""+ appeared);
            	if(score <= 0) {
            		status.setText(res.getString(R.string.Shitfaced));
            	}
            	else if(score <= 84) {
            		status.setText(res.getString(R.string.Drunk));
            	}
            	else if(score <= 168) {
            		status.setText(res.getString(R.string.slightly_drunk));
            	}
            	else if(score <= 252){
            		status.setText(res.getString(R.string.Buzzed));
            	}
            	else if(score <= 336){
            		status.setText(res.getString(R.string.Well));
            	}
            	else if(score >= 337) {
            		status.setText(res.getString(R.string.Sober));
            	}
            	scoreview.setText("Score: " + score);
            	float percent = Math.round(((float) (score/10)/appeared) *100);
            	
            	if(score < 0){
            		percent = 0;
            	}
            	
            	popview.setText("Tapped: " + percent +"%");
        	}

        }
        else if(game == 3) {
        	inflater.inflate(R.layout.rollerscore, ll);
        	
        	TextView time = (TextView) findViewById(R.id.Rolltimelasted);
        	
        	long timel = myIntent.getLongExtra("Time", 0);
        	
        	time.setText("Duration Lasted: " + timel/1000 + "s");
        	
        	
        	
        	if(timel/1000 <= 5) {
        		status.setText(res.getString(R.string.Shitfaced));
        	}
        	else if(timel/1000 <= 15) {
        		status.setText(res.getString(R.string.Drunk));
        	}
        	else if(timel/1000 <= 25) {
        		status.setText(res.getString(R.string.slightly_drunk));
        	}
        	else if(timel/1000 <= 30){
        		status.setText(res.getString(R.string.Buzzed));
        	}
        	else if(timel/1000 <= 40){
        		status.setText(res.getString(R.string.Well));
        	}
        	else if(timel/1000 >= 41) {
        		status.setText(res.getString(R.string.Sober));
        	}
        }
	}
	
	protected void writeInt(String filename, int intToWrite) {
		FileOutputStream fout = null;
		OutputStreamWriter osw = null;
		try {
			fout = openFileOutput(filename, MODE_PRIVATE);
			osw = new OutputStreamWriter(fout);
			StringBuilder sb = new StringBuilder();
			sb.append(intToWrite);
			osw.write(sb.toString());
			osw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				osw.close();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}