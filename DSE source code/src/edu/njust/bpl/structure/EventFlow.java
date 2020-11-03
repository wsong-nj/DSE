package edu.njust.bpl.structure;

import java.util.ArrayList;

/**
 * 用来存放事件流
 * 
 * @author zhen
 */
public class EventFlow extends ArrayList<String> {

	private static final long serialVersionUID = 1L;

	private String traceId;

	private int count = 1;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	/**
	 * 向eventflow 添加 event
	 * 
	 * @param event
	 */
	public void addEvent(String event) {
		this.add(event);
	}

	/**
	 * 判断当前eventflow是否包含event
	 * 
	 * @param event
	 * @return
	 */
	public boolean isContainEvent(String event) {
		for (String e : this) {
			if (e.equals(event))
				return true;
		}
		return false;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String str : this) {
			sb.append(str + "->");
		}
		return (String) sb.toString().subSequence(0, sb.lastIndexOf("-"));
	}

	public boolean equals(Object obj) {
		if (obj instanceof EventFlow) {
			EventFlow eventFlow = (EventFlow) obj;
			if (eventFlow.toString().equals(this.toString()))
				return true;
		}
		return false;
	}

	public int hashCode() {
		return this.toString().hashCode();
	}

}
