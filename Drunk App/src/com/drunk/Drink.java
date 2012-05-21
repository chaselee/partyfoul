package com.drunk;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class Drink extends Activity {
	
	private Button yes_button;
	private TextView num_drinks;
	private TextView taunter;
	private SeekBar slider;
	private int drinks;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drink_layout);
        
        yes_button = (Button) findViewById(R.id.yes);
        num_drinks = (TextView) findViewById(R.id.num_drinks);
        taunter = (TextView) findViewById(R.id.taunt);
        slider = (SeekBar) findViewById(R.id.drink_bar);
        
        if((drinks = readInt("progress")) < 0) {
        	drinks = 0;
        }
        num_drinks.setText(String.valueOf(drinks));
        slider.setProgress(drinks);
        
		if(drinks < 10) {
			taunter.setText("");
		}
		else if(drinks <=20) {
			taunter.setText("Look at the big drinker");
			taunter.setTextColor(Color.GREEN);
		}
		else if(drinks <= 30) {
			taunter.setText("Still walking?");
			taunter.setTextColor(Color.YELLOW);
		}
		else if(drinks <= 50) {
			taunter.setText("Why are you not dead?");
			taunter.setTextColor(Color.RED);
		}
        
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				num_drinks.setText(String.valueOf(progress));
				
				if(progress < 10) {
					taunter.setText("");
				}
				else if(progress <=20) {
					taunter.setText("Look at the big drinker");
					taunter.setTextColor(Color.GREEN);
				}
				else if(progress <= 30) {
					taunter.setText("Still walking?");
					taunter.setTextColor(Color.YELLOW);
				}
				else if(progress <= 50) {
					taunter.setText("Why are you not dead?");
					taunter.setTextColor(Color.RED);
				}
				drinks = progress;
			}
		});
        
        yes_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				writeInt("progress", drinks);
				Intent next_intent = new Intent(getApplicationContext(), com.drunk.Liteup.class);
				startActivity(next_intent);
				finish();
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