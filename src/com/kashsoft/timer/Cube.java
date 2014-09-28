package com.kashsoft.timer;

public class Cube {
	private Rotation[] rotations;
	private static final String separator = " "; 
	
	public String getRotationsString(){
		StringBuilder sb = new StringBuilder();
		boolean firstFlag = true;
		for (Rotation r: rotations){
			if (firstFlag){
				firstFlag = false;
			} else {
				sb.append(separator);
			}
			sb.append(r.toString());
		}
		return sb.toString();
	}
	
	public Cube(){
		rotations = new RotationGenerator().generateRandomRotations(25);
	}
	
	public Cube(String input){
		String[] moves = input.split(separator);
		rotations = new Rotation[moves.length];
		for (int i = 0; i < rotations.length; i++){
			rotations[i] = Rotation.stringToRotation(moves[i]);
		}
	}

}
