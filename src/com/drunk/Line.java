package com.drunk;

public class Line {
	public float startx;
	public float starty;
	public float stopx;
	public float stopy;
	public boolean orientation; //True = vertical, False = Horizontal
	public int direction; //0=up, 1 = down, 2 = left, 3 = right
	
	
	public Line(float sx, float sy, float px, float py, boolean or, int d) {
		startx = sx;
		starty = sy;
		stopx = px;
		stopy = py;
		orientation = or;
		direction = d;
	}
	
	
}
