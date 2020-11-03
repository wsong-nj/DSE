package edu.njust.bpl.log;

public class Dot {

	public static final int Dot_Place = 1;
	public static final int Dot_Transition = 2;
	public static final int Dot_Arc = 3;

	private int type;
	private String id;
	private String source;
	private String target;

	public Dot(int type) {
		setType(type);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
