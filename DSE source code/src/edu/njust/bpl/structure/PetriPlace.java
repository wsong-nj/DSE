package edu.njust.bpl.structure;

/**
 * Petri place
 * 
 * @author zhen
 * 
 */
public class PetriPlace extends PetriVertex {

	private String flag = "place";

	public PetriPlace(String name) {
		super(name);
	}

	public String getFlag() {

		return flag;

	}

	public String toString() {

		return name;

	}

}
