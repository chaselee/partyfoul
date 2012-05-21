package com.drunk;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class BubbleView extends View {
	
	public TextView time;
	public TextView scoreview;
	public TextView highscoreview;
	public TextView startview;
	
	private int score = 0;
	private int highscore;
	
	private int num_bubbles = 20; //bubble in array
	private int total_bubbles = 51; //bubbles until game ends
	private int bubbles_popped = 0; //bubbles that have been popped
	private int bubbles_created = 0; //number of bubbles created
	
	private Rect_data [] bubble_coords; //Rect_data array of each bubble on screen
	private Bitmap bubble; //Picture of bubble
	private Bitmap popped; //Bitmap of popped bubble
	
	private final Paint mPaint = new Paint();
	private static BubbleView bv;
	private static Bubblepop bp;
	
	/*Variables for bubbles placement on screen*/
	private float first_left;
	private float first_top;
	private int MaxX;
	private int MaxY;
	
	private gameTimer gt;
	
	public BubbleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		bubble = BitmapFactory.decodeResource(context.getResources(), R.drawable.bubble);
		popped = BitmapFactory.decodeResource(context.getResources(), R.drawable.splatter);
		
		bubble_coords = new Rect_data[num_bubbles];
		for(int i = 0; i < num_bubbles; i++) {
			bubble_coords[i] = new Rect_data();
		}
		
		bv = this;
		first_left = 0;
		first_top = 0;
		
	}
	
	public BubbleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		bubble = BitmapFactory.decodeResource(context.getResources(), R.drawable.bubble);
		popped = BitmapFactory.decodeResource(context.getResources(), R.drawable.splatter);
		
		bubble_coords = new Rect_data[num_bubbles];
		for(int i = 0; i < num_bubbles; i++) {
			bubble_coords[i] = new Rect_data();
		}
		
		bv = this;
		first_left = 0;
		first_top = 0;
	}
	
	public void stopgame() {
		gt.cancel();
	}
	
	public void setup(TextView tv, Bubblepop b, TextView strt) {
		bp = b;
		time = tv;
		startview = strt;
		gt = new gameTimer(30000, 1000);
		gt.start();
	}
	
	public void setScore(TextView sc, TextView hsc) {
		scoreview = sc;
		scoreview.setText("Score: "+ score);
		
		highscoreview = hsc;
		highscore = bp.readInt("bphighscore");
		if(highscore < 0) {
			highscore = 0;
		}
		highscoreview.setText("Highscore: " + highscore);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		MaxX = w;
		MaxY = h;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawColor(Color.rgb(30, 144, 255));
		
		for(int i = 0; i < num_bubbles; i++){
			if(!bubble_coords[i].rf.isEmpty()){
				if(bubble_coords[i].bitmap_index == 0) {
					canvas.drawBitmap(bubble, null, bubble_coords[i].rf, mPaint);
				}
				else {
					canvas.drawBitmap(popped, null, bubble_coords[i].rf, mPaint);
					bubble_coords[i].rf.setEmpty();
				}
			}
		}
	}
	
	private int points(RectF rf){
		int area = (int) (rf.width() * rf.height());
		
		if(area <= 1000) {
			return 10;
		}
		else if(area <= 2000) {
			return 9;
		}
		else if(area <= 3000) {
			return 8;
		}
		else if(area <= 4000) {
			return 7;
		}
		else if(area <= 5000) {
			return 6;
		}
		else if(area <= 6000) {
			return 5;
		}else if(area <= 7000) {
			return 4;
		}
		else if(area <= 8000) {
			return 3;
		}
		else if(area <= 9000) {
			return 2;
		}
		else if(area <= 10000) {
			return 1;
		}
		return 0;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			for(int i = 0; i < num_bubbles; i++) {
				if(!bubble_coords[i].rf.isEmpty()) {
					if(bubble_coords[i].rf.contains(event.getX(), event.getY())) {
						bubble_coords[i].bitmap_index = 1;
						score+= points(bubble_coords[i].rf);
						scoreview.setText("Score: " + score);
						bubbles_popped++;
						if(score > highscore) {
							highscore = score;
							highscoreview.setText("Highscore: " + highscore);
						}
						break;
					}
				}
			}
		}
		return true;
	}
	
	
	
	/*Rect_data class*/
	public class Rect_data {
		public RectF rf;
		public int bitmap_index;
		
		public Rect_data(){
			rf = new RectF();
			bitmap_index = 0;
		}
		
	}
	
	/*Bubble Timer Class */
	public class bubbleTimer extends CountDownTimer {

		public bubbleTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onFinish() {
			startview.setVisibility(INVISIBLE);
			int first_empty = 0;
			for(int i = 0; i < num_bubbles; i++){
				if(!bubble_coords[i].rf.isEmpty()) {
					if(i == first_empty) {
						first_empty++;
					}
				}
			}
			if((first_empty < num_bubbles) && (bubbles_created <= total_bubbles)) {
				Random rndGen = new Random();
				while(true){
					first_left = rndGen.nextFloat()* (MaxX - 50);
					first_top = rndGen.nextFloat() * (MaxY - 50);
					boolean no_collision = true;
					for(int i = 0; i < num_bubbles; i++) {
						if(bubble_coords[i].rf.isEmpty()) {
							continue;
						}
						else if(bubble_coords[i].rf.contains(first_left, first_top)) {
							no_collision = false;
							break;
						}
					}
					if(no_collision) {
						bubble_coords[first_empty].bitmap_index = 0;	
						bubble_coords[first_empty].rf.set(first_left, first_top, 
								                           first_left + 10, first_top + 10);
						bubbles_created++;
						break;
					}
				}
			}
			
			bv.invalidate();
			this.start();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			for(int i = 0; i < num_bubbles; i++){
				if(!bubble_coords[i].rf.isEmpty()) {
					bubble_coords[i].rf.right+=1;
					bubble_coords[i].rf.bottom+=1;
					if((bubble_coords[i].rf.width() * bubble_coords[i].rf.height()) > 10000){
						bubble_coords[i].bitmap_index = 1;
					}
				}
			}
			bv.invalidate();
			
		}
		
	}
	
	/*Game Timer class*/
	public class gameTimer extends CountDownTimer {

		public gameTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			time.setText("Time: 0:" + (millisInFuture/1000));
			bubbleTimer bt = new bubbleTimer(500, 50);
			bt.start();
		}

		@Override
		public void onFinish() {
			Log.d("Created", "" +bubbles_created);
			time.setText("Time: 0:00");
			bp.writeInt("bphighscore", highscore);
			bp.End(score, bubbles_popped);
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
}