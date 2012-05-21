package com.drunk;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class ShapesView extends View {
	
	/*Handles to textViews*/
	public TextView time_read;
	public TextView prompt;
	
	/*Path of line drawn by finger*/
	private Path draw;
	
	private final Paint mPaint = new Paint();
	private Path mPath; //Temp path 
	
	private final Paint shape_paint = new Paint(); //paint to draw shapes
	
	/*Center of screen*/
	private float centerx;
	private float centery;
	
	/*Bit array where if true means shape was drawn*/
	private boolean [] shapes_drawn;
	
	public int shape_index; //index of shape to be drawn
	/*shape_index: 0=circle, 1=square, 2=triangle, 3=pentagon, 4=diamond, 5=octagon*/
	
	public Timer game_timer; //Game Timer
	public ShapeTimer st; //Timer to swap out shapes
	
	public ShapesView sv; //instances of this view
	
	private Vector<RectF> dots = new Vector<RectF>();
	
	private int dots_done;
	

	public ShapesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		sv = this;
		
		
		draw = new Path();
		
		mPaint.setDither(true);
		mPaint.setColor(Color.rgb(124, 252, 0));
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(5);
		mPaint.setAntiAlias(true);
		
		shapes_drawn = new boolean [6];
		
		//Random rndGen = new Random();
		//shape_index= rndGen.nextInt(6);
		//shapes_drawn[shape_index] = true;
		shape_index = 0;
		dots_done = 0;
		
	}
	
	public void startgame(TextView time, TextView p) {
		time_read = time;
		prompt = p;
		game_timer = new Timer(30000, 5000);
		game_timer.start();
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int old){
		centerx = w/2;
		centery = h/2;
		new_shape();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		canvas.drawColor(Color.rgb(255, 140, 0));
		
		for(int i = 0; i < dots.size(); i++){
			if(i == dots_done) {
				shape_paint.setColor(Color.rgb(0, 0, 255));
				//Log.d("Dots_done", ""+dots_done);
			}
			else {
				shape_paint.setColor(Color.BLACK);
			}
			canvas.drawRect(dots.get(i), shape_paint);
		}
		
		/*if(shape_index == 0) {
			canvas.drawCircle(centerx, centery, 100, shape_paint);
		}
		else if(shape_index == 1) {
			Rect r = new Rect((int)(centerx - (centerx/2)), (int)(centery - (centerx/2)),
						(int)(centerx + (centerx/2)), (int)(centery + (centerx/2)));
			canvas.drawRect(r, shape_paint);
		}
		else if(shape_index == 2) {
			Path triangle = new Path();
			triangle.moveTo(centerx, centery-(centerx/2));
			triangle.lineTo(centerx, centery-(centerx/2));
			triangle.lineTo(centerx-(centerx/2), centery + (centery/2));
			triangle.lineTo(centerx+(centerx/2), centery + (centery/2));
			triangle.lineTo(centerx, centery-(centerx/2));
			canvas.drawPath(triangle, shape_paint);
		}
		else if(shape_index == 3){
			Path pentagon = new Path();
			pentagon.moveTo(centerx, centery - (centery/2));
			pentagon.lineTo(centerx, centery - (centery/2));
			pentagon.lineTo(centerx - (centerx/2), centery - (centery/3));
			pentagon.lineTo(centerx - (centerx/2), centery + (centery/3));
			pentagon.lineTo(centerx + (centerx/2), centery + (centery/3));
			pentagon.lineTo(centerx + (centerx/2), centery - (centery/3));
			pentagon.lineTo(centerx, centery - (centery/2));
			canvas.drawPath(pentagon, shape_paint);
		}
		else if(shape_index == 4) {
			Path diamond = new Path();
			diamond.moveTo(centerx, centery - (centery/2));
			diamond.lineTo(centerx, centery - (centery/2));
			diamond.lineTo(centerx - (centerx/2), centery);
			diamond.lineTo(centerx, centery + (centery/2));
			diamond.lineTo(centerx + (centerx/2), centery);
			diamond.lineTo(centerx, centery - (centery/2));
			canvas.drawPath(diamond, shape_paint);
		}
		else if(shape_index == 5){
			Path octagon = new Path();
			octagon.moveTo(centerx - (centerx/3), centery - (centery/2));
			octagon.lineTo(centerx - (centerx/2), centery - (centery/2));
			octagon.lineTo(centerx - (centerx/3), centery - (centery/3));
			octagon.lineTo(centerx - (centerx/3), centery + (centery/3));
			octagon.lineTo(centerx - (centerx/2), centery + (centery/2));
			octagon.lineTo(centerx + (centerx/2), centery + (centery/2));
			octagon.lineTo(centerx + (centerx/3), centery + (centery/3));
			octagon.lineTo(centerx + (centerx/3), centery - (centery/3));
			octagon.lineTo(centerx + (centerx/2), centery - (centery/2));
			octagon.lineTo(centerx - (centerx/2), centery - (centery/2));
			canvas.drawPath(octagon, shape_paint);
		}*/
		
		if(!draw.isEmpty()){
			canvas.drawPath(draw, mPaint);	
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			if(dots.get(dots_done).contains(event.getX(), event.getY())) {
				Log.d("In", "Action_Down");
				dots_done++;
			}
			mPath = new Path();
			mPath.moveTo(event.getX(), event.getY());
			mPath.lineTo(event.getX(), event.getY());
			draw.addPath(mPath);
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE){
			if(dots.get(dots_done).contains(event.getX(), event.getY())) {
				Log.d("In", "Action_Move");
				dots_done++;
			}
			mPath.lineTo(event.getX(), event.getY());
			draw.addPath(mPath);
		}
		else if(event.getAction() == MotionEvent.ACTION_UP) {
			mPath.lineTo(event.getX(), event.getY());
			draw.addPath(mPath);
		}
		this.invalidate();
		return true;
	}
	
	public boolean new_shape(){
		boolean all = true;
		for(boolean b : shapes_drawn) {
			if(!b) {
				all = false;
				break;
			}
		}
		
		if(!all) {
			/*Random rndGen = new Random();
			shape_index= rndGen.nextInt(6);
			while(shapes_drawn[shape_index]) {
				shape_index = rndGen.nextInt(6);
			}*/
			
			/*Fill in dots array*/
			float left = (centerx - (centerx/2));
			float top = (centery - (centerx/2));
			float bottom = (centery + (centerx/2));
			float right = (centerx + (centerx/2));
			dots.clear();
			RectF tempr = new RectF(left, top, left + 100, top + 10);
			dots.add(tempr);
			tempr = new RectF(right - 15, top, right, top + 15);
			dots.add(tempr);
			tempr = new RectF(left, bottom - 15, left + 15, bottom);
			dots.add(tempr);
			tempr = new RectF(right - 15, bottom - 15, right, bottom);
			dots.add(tempr);
			
			draw.reset();
			sv.invalidate();
			//shapes_drawn[shape_index] = true;
		}
		return !all;
	}
	
	
	
	public class Timer extends CountDownTimer {

		public Timer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			/*st = new ShapeTimer(6000, 1000);
			st.start();	*/
			
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTick(long millisUntilFinished) {

		}
		
	}
	
	public class ShapeTimer extends CountDownTimer {

		public ShapeTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			time_read.setText("Time: 0:0" + (millisInFuture/1000));
		}

		@Override
		public void onFinish() {
			prompt.setVisibility(INVISIBLE);
			time_read.setText("Time: 0:00");
			if(new_shape()) {
				this.start();
			}
			

		}

		@Override
		public void onTick(long millisUntilFinished) {
			time_read.setText("Time: 0:0" + (millisUntilFinished/1000));
			
		}
		
	}
	
}