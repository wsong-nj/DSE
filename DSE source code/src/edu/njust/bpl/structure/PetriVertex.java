package edu.njust.bpl.structure;

/**
 * Petri vertex
 * 
 * @author zhen
 * 
 */
public abstract class PetriVertex {

	protected String name;

	public PetriVertex(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFlag() {

		return "";

	}

	public String toString() {

		return "PetriVertex{name='" + name + '\'' + '}';

	}

}
