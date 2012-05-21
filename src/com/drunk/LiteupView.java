package com.drunk;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class LiteupView extends View {
	private int centerX;
	private int centerY;
	
	TextView time;
	TextView score;
	TextView highscore;
	TextView prompt;
	
	private int current_score = 0;
	private int hs;
	
	Liteup LT;
	
	private Rect [] squares;
	private int current_square = 0;
	private int changed = 0;
	
	public int duration = 1000;
	public Timeout next;
	
	public LiteupView lv;
	
	public GameTimer gt;
	public Prompttimer pt;
	
	private boolean tapped;
	
	public LiteupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		lv = this;
		squares = new Rect [4];
		tapped = false;

	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		centerX = w/2;
		centerY = h/2;
		
		squares[0] = new Rect(0, 60, centerX - 10, centerY);
		squares[1] = new Rect(centerX + 10, 60, w, centerY);
		squares[2] = new Rect(0, centerY+20, centerX - 10, h - 40);
		squares[3] = new Rect(centerX + 10, centerY+20, w, h - 40);
	}
	
	
	public void setUp(TextView t, TextView s, TextView highs, TextView p, Liteup lt){
		time = t;
		t.setText("Time: 0:30");
		score = s;
		score.setText("Score: " + current_score);
		highscore = highs;
		LT = lt;
		hs = LT.readInt("lthighscore");
		if(hs < 0) {
			hs = 0;
		}
		highscore.setText("Highscore: " + hs);
		prompt = p;
		pt = new Prompttimer(3000, 3000);
		pt.start();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		Paint p = new Paint();
		for(int i = 0; i < 4; i++){
			if(current_square == i) {
				p.setColor(Color.rgb(0, 0, 255));
			}
			else {
				p.setColor(Color.rgb(255, 255, 255));
			}
			canvas.drawRect(squares[i], p);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(!tapped && squares[current_square].contains((int)event.getX(), (int)event.getY())) {
				tapped = true;
				current_score+=10;
				score.setText("Score: " + current_score);
				if(current_score > hs) {
					hs = current_score;
					highscore.setText("Highscore: " + hs);
				}
			}
			else if(!tapped){
				for(int i = 0; i < 4; i++){
					if(i == current_square){
						continue;
					}
					
					if(squares[i].contains((int)event.getX(), (int)event.getY())){
						current_score-=10;
						tapped = true;
						score.setText("Score: " + current_score);
					}
				}
			}
		}
		return true;
	}
	

	
	public class Prompttimer extends CountDownTimer {

		public Prompttimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			prompt.setVisibility(INVISIBLE);
			gt = new GameTimer(30000, 1000);
			gt.start();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			
		}
		
	}
	
	public class GameTimer extends CountDownTimer {

		public GameTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			time.setText("Time: 0:" + (millisInFuture/1000));
			Timeout time = new Timeout(duration, duration);
			time.start();
		}

		@Override
		public void onFinish() {
			time.setText("Time: 0:00");
			LT.writeInt("lthighscore", hs);
			LT.End(current_score, changed + 1);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			if((millisUntilFinished/1000) < 10){
				time.setText("Time: 0:0" + (millisUntilFinished/1000));
			}
			else {
				time.setText("Time: 0:" + (millisUntilFinished/1000));
			}
		}
		
	}
	
	public class Timeout extends CountDownTimer {

		public Timeout(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			
		}

		@Override
		public void onFinish() {
			Random rndGen = new Random();
			int x = rndGen.nextInt(4);
			while (x == current_square) {
				 x = rndGen.nextInt(4);
			}
			current_square = x;
			changed++;
			tapped = false;
			lv.invalidate();
			if(duration <= 400) {
				duration = 400;
			}
			else {
				duration-=14;
			}
			
			next = new Timeout(duration, duration);
			next.start();
			
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	
}