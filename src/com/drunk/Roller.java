package com.drunk;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;

public class Roller extends Activity implements SensorEventListener{
	private RollerView mRollerView;
	private SensorManager SM = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);
        setContentView(R.layout.roller_layout);
        
        mRollerView = (RollerView) findViewById(R.id.roller_view);
        mRollerView.start((Chronometer)findViewById(R.id.RollerTime), (TextView)findViewById(R.id.RollerbestTime), (TextView)findViewById(R.id.rollerprompt), this);
	}
	
	public void End(long time){
		Intent next_intent = new Intent(getApplicationContext(), com.drunk.Results.class);
		next_intent.putExtra("Game", 3);
		next_intent.putExtra("Time", time);
		startActivity(next_intent);
		finish();
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		SM.registerListener(this, SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SM.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		SM.unregisterListener(this);
		mRollerView.stop();
		finish();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				mRollerView.theBall.moveTo(event.values[0], event.values[1]);
			}
		}	
	}
	
	protected void writeLong(String filename, long intToWrite) {
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
	
	protected long readLong(String filename) {
		FileInputStream fin = null;
		InputStreamReader isr = null;
		String data = null;
		long intOut = (long)-1;
		try {
			fin = openFileInput(filename);
			char[] inputBuffer = new char[fin.available()];
			isr = new InputStreamReader(fin);
			isr.read(inputBuffer);
			data = new String(inputBuffer);
			intOut = Long.parseLong(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				isr.close();
				fin.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return intOut;
	}
}