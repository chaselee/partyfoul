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

public class Liteup extends Activity {
	
	private LiteupView mLiteupView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liteup_layout);
		
		mLiteupView = (LiteupView) findViewById(R.id.liteup_view);
		mLiteupView.setUp((TextView) findViewById(R.id.liteuptimer), (TextView) findViewById(R.id.liteupscore)
				, (TextView) findViewById(R.id.lthighscore), (TextView) findViewById(R.id.liteupprompt), this); 
	}
	
	public void onPause(){
		super.onPause();
		if(mLiteupView.pt != null) {
			mLiteupView.pt.onFinish();
			mLiteupView.pt.cancel();	
		}
		
		if(mLiteupView.gt != null){
			mLiteupView.gt.cancel();
		}
		
		finish();
	}
	
	public void End(int score, int appeared) {
		Intent next_intent = new Intent(getApplication(), com.drunk.Results.class);
		next_intent.putExtra("Game", 1);
		next_intent.putExtra("Score", score);
		next_intent.putExtra("appeared", appeared);
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