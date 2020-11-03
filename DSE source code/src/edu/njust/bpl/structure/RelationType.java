package edu.njust.bpl.structure;

import edu.njust.bpl.notation.Constant;

public class RelationType {

	private int type;

	public RelationType() {

	}

	public RelationType(int type) {

		this.type = type;

	}

	public int getType() {

		return type;

	}

	public void setType(int type) {

		this.type = type;

	}

	public String toString() {

		switch (this.type) {

		case Constant.DIRECT_PRECEDENCE_RELATION:

			return ">";

		case Constant.CAUSAL_RELATION:

			return "->";

		case Constant.INTERLEAVING_RELATION:

			return "||";

		case Constant.INDEPENDENCE_RELATION:

			return "#";

		case Constant.TRANSITVE_CLOSURE_OF_CAUSAL_RELATION:

			return "-->";

		case Constant.REVERSE_CAUSAL_RELATION:

			return "<-";

		}

		return null;

	}

}
