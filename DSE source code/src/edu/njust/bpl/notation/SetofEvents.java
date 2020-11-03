package edu.njust.bpl.notation;

import java.util.ArrayList;
import java.util.List;

import edu.njust.bpl.structure.EventFlow;

/**
 * The set of events
 * 
 * @author zhen
 * 
 */
public class SetofEvents {

	private List<String> events;

	private List<EventFlow> logeEventFlows;

	public SetofEvents(List<EventFlow> traces) {

		this.logeEventFlows = traces;

	}

	/**
	 * get the set of events in event log traces
	 * 
	 * @return
	 */
	public List<String> getEvents() {

		events = new ArrayList<String>();

		for (EventFlow eFlow : logeEventFlows) {

			for (String str : eFlow) {

				if (!events.contains(str))

					events.add(str);

			}

		}

		return events;

	}

}
