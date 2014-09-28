package com.kashsoft.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RotationGenerator {
	Random random;
	Rotation[] allRotations;
	
	public RotationGenerator(){
		random = new Random();
		allRotations = Rotation.values();
	}
	
	public Rotation[] generateRandomRotations(int num){
		List<Rotation> rotationList = generateArrayOfRotations(num);
		Rotation[] rotationArray = new Rotation[rotationList.size()];
		for (int i = 0; i < rotationList.size(); i++){
			rotationArray[i] = rotationList.get(i);
		}
		return rotationArray;
	}
	
	private List<Rotation> generateArrayOfRotations(int num){
		if (num == 0) return new ArrayList<Rotation>();
		List<Rotation> result = generateArrayOfRotations(num - 1);
		Rotation newRotation;
		if (result.size() != 0){
			Rotation last = result.get(result.size() - 1);
			newRotation = generateNextRotation(last);
		} else {
			newRotation = generateNextRotation();
		}
		result.add(newRotation);
		return result;
	}
	
	public Rotation generateNextRotation(Rotation last){
		Rotation r = generateNextRotation();
		while (r.isSameSide(last)){
			r = generateNextRotation();
		}
		return r;
	}
	
	public Rotation generateNextRotation(){
		int randomInt = this.random.nextInt(allRotations.length);
		return allRotations[randomInt];
	}
	
}
