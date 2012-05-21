package com.drunk;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Bubblepop extends Activity {
	private BubbleView mBubbleView;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bubble_layout);
        
        mBubbleView = (BubbleView) findViewById(R.id.bubble);
        mBubbleView.setup((TextView) findViewById(R.id.timer), this, (TextView) findViewById(R.id.start));
        mBubbleView.setScore((TextView) findViewById(R.id.score), 
        		(TextView) findViewById(R.id.highscore));
	}
	
	public void onPause() {
		super.onPause();
		mBubbleView.stopgame();
	}
	
	public void End(int score, int popped) {
		Intent next_intent = new Intent(getApplicationContext(), com.drunk.Results.class);
		next_intent.putExtra("Game", 0);
		next_intent.putExtra("Score", score);
		next_intent.putExtra("Popped", popped);
		startActivity(next_intent);
		finish();
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
	
	protected int readInt(String filename) {
		FileInputStream fin = null;
		InputStreamReader isr = null;
		String data = null;
		int intOut = -1;
		try {
			fin = openFileInput(filename);
			char[] inputBuffer = new char[fin.available()];
			isr = new InputStreamReader(fin);
			isr.read(inputBuffer);
			data = new String(inputBuffer);
			intOut = Integer.parseInt(data);
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