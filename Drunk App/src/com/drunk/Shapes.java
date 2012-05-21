package com.drunk;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Shapes extends Activity {
	
	private ShapesView mShapesView; 
	
	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shapes_layout);
        
        mShapesView = (ShapesView) findViewById(R.id.shapes_view);
        mShapesView.startgame((TextView) findViewById(R.id.shapesTime),(TextView) findViewById(R.id.shapesprompt));
	}
}