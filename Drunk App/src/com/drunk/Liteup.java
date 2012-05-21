package com.drunk;

import android.app.Activity;
import android.os.Bundle;

public class Liteup extends Activity {
	
	private LiteupView mLiteupView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liteup_layout);
		
		mLiteupView = (LiteupView) findViewById(R.id.liteup_view);
	}
}