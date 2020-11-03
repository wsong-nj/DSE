package edu.njust.bpl.structure;

import java.util.ArrayList;
import java.util.List;

public class AB {

	private List<String> A;

	private List<String> B;

	public AB() {

		A = new ArrayList<String>();

		B = new ArrayList<String>();

	}

	public AB(List<String> A, int state) {

		this.A = A;

		B = new ArrayList<>();

	}

	public AB(int state, List<String> B) {

		A = new ArrayList<String>();

		this.B = B;

	}

	public AB(List<String> A, List<String> B) {

		this.A = A;

		this.B = B;

	}

	public void addA(String evevt) {

		A.add(evevt);

	}

	public void addB(String event) {

		B.add(event);

	}

	public List<String> getA() {
		return A;
	}

	public List<String> getB() {
		return B;
	}

	public String toString() {

		return A + " " + B;

	}

	public String getPlaceName() {

		String name = "P";

		for (String a : A)

			name += a;

		name += "_";

		for (String b : B)

			name += b;

		return name;

	}

	public AB copy() {

		AB ab = new AB();
		for (String string : A)
			ab.addA(string);
		for (String string : B)
			ab.addB(string);

		return ab;

	}

}