package com.drunk;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;



public class RollerView extends View {
	private TextView prompt;
	private Chronometer t;
	private TextView bt;
	
	private long bestTime;
	
	private Simulation [] sims;
	
	private int maxX;
	private int maxY;
	
	public Ball theBall;
	
	public RollerView rv;
	private Move m;
	
	private boolean collide = false;
	
	private int m_ontick = 25;
	private int current_sim;
	private int max_sims;
	
	private Inval iv;
	private PromptTimer pt;
	
	public Roller R;
	
	private int cycles;
	
	
	public RollerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		rv = this;
		max_sims = 5;
		sims = new Simulation [max_sims];
		current_sim = 0;
		cycles = 0;
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		maxX = w;
		maxY = h;
		GenSims();
		theBall = new Ball((float)w/2, (float)h/2, 8);
		
	}
	
	public void stop(){
		t.stop();
		if(iv == null){
			pt.cancel();
		}
		else{
			iv.onFinish();
			iv.cancel();			
		}

	}
	
	public void start(Chronometer th, TextView bth, TextView p, Roller r) {
		t = th;
		bt = bth;
		prompt = p;
		R = r;
		bestTime = R.readLong("RollTime");
		if(bestTime < 0 ) {
			bestTime = 0;
		}
		bt.setText("BestTime: " + bestTime/1000 + "s");
		pt = new PromptTimer(3000, 3000);
		pt.start();
	}
	
	private void GenSims(){
		sims[0] = new Simulation(1, 7000);
		sims[0].lines[0] = new Line(0, maxY/2-50, maxX, maxY/2-50, false, 1);
		
		sims[1] = new Simulation(3, 7000);
		sims[1].lines[0] = new Line(maxX/2 - 100, 0, maxX/2 + 50, 0, false, 1);
		sims[1].lines[1] = new Line(maxX/2 - 50, maxY, maxX/2 + 100, maxY, false, 0);
		sims[1].lines[2] = new Line(0, 0, 0, maxY/2 + 100, true, 3);
		
		sims[2] = new Simulation(2, 7000);
		sims[2].lines[0] = new Line(0, maxY/2-100, maxX/2-50, maxY/2-100, false, 1);
		sims[2].lines[1] = new Line(maxX/2 +50, maxY/2-100, maxX, maxY/2-100, false, 1);
		
		sims[3] = new Simulation(2, 7000);
		sims[3].lines[0] = new Line(0, 0, 0, maxY/2+50, true, 3);
		sims[3].lines[1] = new Line(maxX, maxY/2-50, maxX, maxY, true, 2);
		
		sims[4] = new Simulation(4, 7000);
		sims[4].lines[0] = new Line(0, maxY/2-100, maxX/2-50, maxY/2-100, false, 1);
		sims[4].lines[1] = new Line(maxX/2 +50, maxY/2-100, maxX, maxY/2-100, false, 1);
		sims[4].lines[2] = new Line(0, 0, 0, maxY/2+50, true, 3);
		sims[4].lines[3] = new Line(maxX, maxY/2-50, maxX, maxY, true, 2);
		
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		
		Paint mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(3);
		mPaint.setColor(Color.BLACK);
		for(Line l : sims[current_sim].lines){
			Log.d("l", "startx:" + l.startx + "starty:" + l.starty + "stopx:" + l.stopx + "stopy:" + l.stopy);
			canvas.drawLine(l.startx, l.starty, l.stopx, l.stopy, mPaint);
		}
		
		canvas.drawCircle(theBall.centerx, theBall.centery, theBall.radius, mPaint);
	}
	
	public class Ball {
		public float centerx;
		public float centery;
		public float radius;
		public float accelx;
		public float accely;
		
		public Ball(float x, float y, float r) {
			centerx = x;
			centery = y;
			radius = r;
			accelx = 0;
			accely = 0;
		}
		
		public void moveTo(float ax, float ay) {
			accelx = ax;
			accely = ay;
		}
	}

	public class Inval extends CountDownTimer {

		public Inval(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			t.setBase(SystemClock.elapsedRealtime());
			t.start();
			m = new Move(sims[current_sim].simTime, m_ontick);
			m.start();
		}

		@Override
		public void onFinish() {
			m.cancel();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			if((SystemClock.elapsedRealtime() - t.getBase()) > bestTime){
				bestTime = SystemClock.elapsedRealtime() - t.getBase();
			}
			bt.setText("BestTime: " + bestTime/1000 + "s");
			if(theBall.accelx < 0 && (theBall.centerx + 2) < maxX) {
				theBall.centerx+=2;
			}
			else if(theBall.accelx > 0 && (theBall.centerx - 2) > 0){
				theBall.centerx-=2;
			}
			
			if(theBall.accely < 0 && (theBall.centery - 2) > 0){
				theBall.centery-=2;
			}
			else if(theBall.accely > 0 && (theBall.centery + 2) < maxY){
				theBall.centery+=2;
			}
			//Check for collision with line
			float half_widthp = theBall.centerx + theBall.radius;
			float half_heightp = theBall.centery + theBall.radius;
			float half_widthm = theBall.centerx - theBall.radius;
			float half_heightm = theBall.centery - theBall.radius;
			for(Line l : sims[current_sim].lines){
				if(collide) {
					break;
				}
				if(l.orientation && ((int)l.startx > (int)half_widthm && (int)l.startx < (int)half_widthp)) {
					if((int)theBall.centery > (int)l.starty && (int)theBall.centery < (int)l.stopy){
						collide = true;
						t.stop();
						m.cancel();
						this.cancel();
						R.writeLong("RollTime", bestTime);
						R.End(SystemClock.elapsedRealtime() - t.getBase());
					}
				}
				else if(!l.orientation && ((int)l.starty > (int)half_heightm && (int)l.starty < (int)half_heightp)){
					if((int)theBall.centerx > (int)l.startx && (int)theBall.centerx < (int)l.stopx){
						collide=true;
						t.stop();
						m.cancel();
						this.cancel();
						R.writeLong("RollTime", bestTime);
						R.End(SystemClock.elapsedRealtime() - t.getBase());
					}
				}
			}
			rv.invalidate();
		}
		
	}
	
	public class Move extends CountDownTimer {

		public Move(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			/*if(current_sim + 1 >= max_sims){
				
				current_sim = 0;
				m_ontick-=5;
				if(m_ontick < 5) {
					m_ontick = 2;
				}
				GenSims();
			}
			else {
				current_sim++;
			}*/
			cycles++;
			if(cycles%max_sims == 0){
				m_ontick-=5;
				if(m_ontick < 5) {
					m_ontick = 2;
				}
			}
			
			Random rndGen = new Random();
			int next = rndGen.nextInt(max_sims);
			while(next == current_sim){
				next = rndGen.nextInt(max_sims);
			}
			current_sim = next;
			m= new Move(sims[current_sim].simTime, m_ontick);
			m.start();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			sims[current_sim].updateSim(maxX, maxY);
			//rv.invalidate();
		}
		
	}
	
	public class PromptTimer extends CountDownTimer{

		public PromptTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			prompt.setVisibility(INVISIBLE);
			iv = new Inval(1800000, 1);
			iv.start();
			
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	

}