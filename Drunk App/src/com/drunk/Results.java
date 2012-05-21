package com.drunk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Results extends Activity {
	
	private Button addDrink;
	private Button play;
	
	private TextView status;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        
        addDrink = (Button) findViewById(R.id.adddrink);
        addDrink.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent next_intent = new Intent(getApplicationContext(), com.drunk.Drink.class);
				startActivity(next_intent);
				finish();
			}
		});
        
        play = (Button) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent next_intent = new Intent(getApplicationContext(), com.drunk.Bubblepop.class);
				startActivity(next_intent);
				finish();
				
			}
		});
        
        
        status = (TextView) findViewById(R.id.status);
        
        Intent myIntent = getIntent();
        int game = myIntent.getIntExtra("Game", 0);
        
        LinearLayout ll = (LinearLayout) findViewById(R.id.blankll);
        
        final Context context = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        if(game == 0) {
        	int score = myIntent.getIntExtra("Score", 0);
        	int popped = myIntent.getIntExtra("Popped", 0);
        	
        	inflater.inflate(R.layout.bubblescore, ll);
        	
        	TextView scoreview = (TextView) findViewById(R.id.bblscore);
        	TextView popview = (TextView) findViewById(R.id.popped);
        	
        	Log.d("popped", "" + popped);
        	if(score > 0) {
        		status.setText("Yay!");
        	}
        	
        	scoreview.setText("Score: " + score);
        	float percent = (((float) popped)/52) * 100;
        	popview.setText("Pop %: " + (int) percent +"%");
        }
	}
}