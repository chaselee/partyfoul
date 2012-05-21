package com.drunk;

public class Simulation {
	
	public int num_lines;
	public Line [] lines;
	public int simTime;
	
	public Simulation(int num_l, int l){
		num_lines = num_l;
		simTime = l;
		lines = new Line [num_lines];
	}
	
	public void updateSim(int MaxX, int MaxY){
		for(int i = 0; i < num_lines; i++){
			if(lines[i].orientation){
				if(lines[i].direction == 2){
					if((lines[i].startx - 1) > 0) {
						lines[i].startx--;
						lines[i].stopx--;	
					}
					else{
						lines[i].direction = 3;
					}
					
				}
				else {
					if((lines[i].startx + 1) < MaxX){
						lines[i].startx++;
						lines[i].stopx++;	
					}
					else {
						lines[i].direction = 2;
					}
					
				}
			}
			else{
				if(lines[i].direction == 0){
					if((lines[i].starty - 1) > 0) {
						lines[i].starty--;
						lines[i].stopy--;	
					}
					else {
						lines[i].direction = 1;
					}
				}
				else {
					if((lines[i].starty + 1) < MaxY){
						lines[i].starty++;
						lines[i].stopy++;
					}
					else {
						lines[i].direction = 0;
					}
				}
			}
		}
	}
	
}
