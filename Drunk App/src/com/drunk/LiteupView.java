package com.drunk;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class LiteupView extends View {
	private int centerX;
	private int centerY;
	
	private Rect TL;
	private Rect TR;
	private Rect BL;
	private Rect BR;
	
	private int TOP_LEFT = 0;
	private int TOP_RIGHT = 1;
	private int BOTTOMW_LEFT = 2;
	private int BOTTOM_RIGHT = 3;
	private int current_square = 0;
	
	public int duration = 1000;
	public Timeout next;
	
	public LiteupView lv;
	
	public LiteupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		lv = this;
		GameTimer gt = new GameTimer(30000, 30000);
		gt.start();
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		centerX = w/2;
		centerY = h/2;
		
		TL = new Rect(0, 50, centerX - 10, centerY);
		TR = new Rect(centerX + 10, 50, w, centerY);
		BL = new Rect(0, centerY+20, centerX - 10, h - 30);
		BR = new Rect(centerX + 10, centerY+20, w, h - 30);
	}
	
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Paint p = new Paint();
		if(current_square == TOP_LEFT) {
			p.setColor(Color.rgb(0, 0, 255));
		}
		else {
			p.setColor(Color.rgb(255, 255, 255));
		}
		canvas.drawRect(TL, p);
		
		if(current_square == TOP_RIGHT) {
			p.setColor(Color.rgb(0, 0, 255));
		}
		else {
			p.setColor(Color.rgb(255, 255, 255));
		}
		canvas.drawRect(TR, p);
		
		if(current_square == BOTTOMW_LEFT) {
			p.setColor(Color.rgb(0, 0, 255));
		}
		else {
			p.setColor(Color.rgb(255, 255, 255));	
		}
		canvas.drawRect(BL, p);
		
		if(current_square == BOTTOM_RIGHT) {
			p.setColor(Color.rgb(0, 0, 255));
		}
		else {
			p.setColor(Color.rgb(255, 255, 255));	
		}
		canvas.drawRect(BR, p);
	}
	
	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
		}
	}*/
	
	
	
	
	public class GameTimer extends CountDownTimer {

		public GameTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			Timeout time = new Timeout(duration, duration);
			time.start();
		}

		@Override
		public void onFinish() {
			Log.d("Done", "here");
			
		}

		@Override
		public void onTick(long millisUntilFinished) {
			
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
			lv.invalidate();
			duration-=14;
			if(duration > 0) {
				next = new Timeout(duration, duration);
				next.start();
			}
			
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	
}