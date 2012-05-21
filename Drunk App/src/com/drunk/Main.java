package com.drunk;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main extends Activity {
	
	private Button start_button;
	private long start_time;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        start_time = readLong("time");
        if(start_time < 0) {
        	start_time = 0;
        }
        
        start_button = (Button) findViewById(R.id.start);
        start_button.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				Long current_time = System.currentTimeMillis();
				if((current_time - start_time) > 21600000 || start_time == 0) {
					writeLong("time", current_time);
					writeInt("progress", -1);
				}
				
				Intent next_intent = new Intent(getApplicationContext(), com.drunk.Drink.class);
				startActivity(next_intent);
				
			}
		});
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
    
	protected void writeLong(String filename, Long longToWrite) {
		FileOutputStream fout = null;
		OutputStreamWriter osw = null;
		try {
			fout = openFileOutput(filename, MODE_PRIVATE);
			osw = new OutputStreamWriter(fout);
			StringBuilder sb = new StringBuilder();
			sb.append(longToWrite);
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
	
	protected Long readLong(String filename) {
		FileInputStream fin = null;
		InputStreamReader isr = null;
		String data = null;
		Long intOut = (long) -1;
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