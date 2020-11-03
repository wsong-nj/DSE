package edu.njust.bpl.structure;

public class Relation {

	private String leftTransition;

	private String rightTransition;

	private RelationType relationType;

	public Relation() {
	}

	public Relation(String left, String right, RelationType type) {

		this.leftTransition = left;

		this.rightTransition = right;

		this.relationType = type;

	}

	public String getLeftTransition() {

		return leftTransition;

	}

	public void setLeftTransition(String leftTransition) {

		this.leftTransition = leftTransition;

	}

	public String getRightTransition() {

		return rightTransition;

	}

	public void setRightTransition(String rightTransition) {

		this.rightTransition = rightTransition;

	}

	public RelationType getRelationType() {

		return relationType;

	}

	public void setRelationType(RelationType relationType) {

		this.relationType = relationType;

	}

	public String toString() {

		return leftTransition + relationType.toString() + rightTransition;

	}

}
