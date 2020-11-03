package edu.njust.bpl.structure;

/**
 * Petir arc
 * 
 * @author zhen
 * 
 */
public class PetriArc {

	private PetriVertex source;

	private PetriVertex target;

	private String name;

	public PetriArc() {

	}

	public PetriArc(PetriVertex source, PetriVertex target) {
		super();
		this.source = source;
		this.target = target;
	}

	public PetriArc(PetriVertex source, PetriVertex target, String name) {
		super();
		this.source = source;
		this.target = target;
		this.name = name;
	}

	public PetriVertex getSource() {
		return source;
	}

	public void setSource(PetriVertex source) {
		this.source = source;
	}

	public PetriVertex getTarget() {
		return target;
	}

	public void setTarget(PetriVertex target) {
		this.target = target;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {

		return source.toString() + "-->" + target.toString();

	}

}
