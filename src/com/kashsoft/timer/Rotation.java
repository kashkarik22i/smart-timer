package com.kashsoft.timer;

public enum Rotation {
	RIGHT_D ("R"), RIGHT2 ("R2"), RIGHT_R ("R'"),
	LEFT_D ("L"), LEFT2 ("L2"), LEFT_R ("L'"),
	UP_D ("U"), UP2 ("U2"), UP_R ("U'"),
	DOWN_D ("D"), DOWN2 ("D2"), DOWN_R ("D'"),
	BACK_D ("B"), BACK2 ("B2"), BACK_R ("B'"),
	FRONT_D ("F"), FRONT2 ("F2"), FRONT_R ("F'");
	
	private Side side;
	private String s;

	private Rotation(String s){
		this.s = s;
		if ((s.equals("R")) || (s.equals("R2")) || (s.equals("R'"))) this.side = Side.RIGHT_SIDE;
		if ((s.equals("L")) || (s.equals("L2")) || (s.equals("L'"))) this.side = Side.LEFT_SIDE;
		if ((s.equals("U")) || (s.equals("U2")) || (s.equals("U'"))) this.side = Side.UP_SIDE;
		if ((s.equals("D")) || (s.equals("D2")) || (s.equals("D'"))) this.side = Side.DOWN_SIDE;
		if ((s.equals("B")) || (s.equals("B2")) || (s.equals("B'"))) this.side = Side.BACK_SIDE;
		if ((s.equals("F")) || (s.equals("F2")) || (s.equals("F'"))) this.side = Side.FRONT_SIDE;
	}
	
	public boolean isSameSide(Rotation r){
		return (r.side == this.side);
	}
	
	public String toString(){
		return s;
	}
	
	private enum Side{
		RIGHT_SIDE, LEFT_SIDE, UP_SIDE, DOWN_SIDE, FRONT_SIDE, BACK_SIDE
	};
	
	public static Rotation stringToRotation(String s){
		for (Rotation r: values()){
			if (s.equals(r.s)) return r;
		}
		return null;
	}
};

