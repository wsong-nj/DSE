package edu.njust.bpl.algorithms;

import java.util.ArrayList;
import java.util.List;

import edu.njust.bpl.notation.SetofEvents;
import edu.njust.bpl.structure.EventFlow;

/**
 * Algorithm 1: Preprocess event log
 * 
 * @author zhen
 * 
 */
public class PreprocessEventLog {

	private List<EventFlow> L;

	private List<EventFlow> L1;

	private List<EventFlow> L2;

	private String endEvent;

	public PreprocessEventLog(List<EventFlow> allTraces, String endEvevt) {

		this.L = allTraces;

		this.endEvent = endEvevt;

		mainBody();

	}

	public void mainBody() {

		L1 = new ArrayList<EventFlow>();

		L2 = new ArrayList<EventFlow>();

		for (EventFlow eFlow : L) {

			if (!eFlow.get(eFlow.size() - 1).equals("te"))

				L1.add(eFlow);

			String[] events = eFlow.toArray(new String[eFlow.size()]);

			for (int i = 0; i < events.length - 1; i++) {

				if (events[i].equals(endEvent)
						|| events[i].equals(events[i + 1])) {

					L2.add(eFlow);

					break;

				}

			}

		}

		expendLog();

		L1 = eliminate(L1);

		L2 = eliminate(L2);

	}

	private void expendLog() {

		List<EventFlow> log1 = L;

		log1.removeAll(L2);

		SetofEvents setofEvents = new SetofEvents(L2);

		List<String> events = setofEvents.getEvents();

		if (events.size() != 0) {

			List<EventFlow> addEventFlows = reflect(log1, events);

			L2.addAll(addEventFlows);

		} else {

			L2.addAll(log1);

		}

	}

	private List<EventFlow> reflect(List<EventFlow> log, List<String> events) {

		List<EventFlow> res = new ArrayList<EventFlow>();

		for (EventFlow eFlow : log) {

			EventFlow temp = new EventFlow();

			for (String str : eFlow) {

				if (events.contains(str))

					temp.add(str);

			}

			res.add(temp);

		}

		return res;

	}

	private List<EventFlow> eliminate(List<EventFlow> log) {

		List<EventFlow> newlist = new ArrayList<EventFlow>();

		for (EventFlow eFlow : log) {

			EventFlow temp = new EventFlow();

			for (String str : eFlow) {

				if (!temp.isContainEvent(str))

					temp.add(str);
			}

			newlist.add(temp);
		}

		return newlist;
	}

	public List<EventFlow> getL1() {
		return L1;
	}

	public List<EventFlow> getL2() {
		return L2;
	}

}
