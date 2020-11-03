package edu.njust.bpl.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.njust.bpl.notation.Constant;
import edu.njust.bpl.structure.AB;
import edu.njust.bpl.structure.PetriArc;
import edu.njust.bpl.structure.PetriNet;
import edu.njust.bpl.structure.PetriPlace;
import edu.njust.bpl.structure.PetriTransition;
import edu.njust.bpl.structure.Relation;
import edu.njust.bpl.structure.RelationType;

/**
 * ¦Á algorithm
 * 
 * @author zhen
 * 
 */
public class AerfaMiner {

	private List<String> targetEvents, allEvents;

	private RelationType[][] relations;

	private List<Relation> casuals;

	private List<AB> resultAB;

	private PetriNet petriNet;// Petir net

	private List<PetriPlace> places;// P

	private List<PetriTransition> transitions;// T

	private List<PetriArc> arcs;// F

	public AerfaMiner(List<String> targetEvents, List<String> allEvents,
			RelationType[][] relations, List<Relation> casuals) {

		this.targetEvents = targetEvents;

		this.allEvents = allEvents;

		this.relations = relations;

		this.casuals = casuals;

	}

	public PetriNet miner() {

		addTransition();

		getAB();

		getPF();

		makePetri();

		return petriNet;

	}

	/**
	 * add PTF to PetriNet
	 */
	private void makePetri() {

		petriNet = new PetriNet(transitions);

		petriNet.addPlaces(places);

		petriNet.addEdges(arcs);

	}

	private void addTransition() {

		transitions = new ArrayList<>();

		for (String transition : targetEvents) {

			PetriTransition t = new PetriTransition(transition);

			transitions.add(t);

		}

	}

	private void getAB() {

		resultAB = new ArrayList<>();

		for (String event : targetEvents) {

			List<String> tempList = new ArrayList<String>();

			for (Relation casual : casuals) {

				if (casual.getLeftTransition().equals(event)
						&& targetEvents.contains(casual.getRightTransition())) {

					tempList.add(casual.getRightTransition());

				}

			}

			if (tempList.size() > 1) {

				if (independenceList(tempList)) {

					AB ab = new AB(0, tempList);

					ab.addA(event);

					resultAB.add(ab);

				} else {

					boolean flag[] = new boolean[tempList.size()];

					for (int i = 0; i < tempList.size(); i++) {

						if (!flag[i]) {

							flag[i] = true;

							AB ab = new AB();
							ab.addA(event);
							ab.addB(tempList.get(i));

							for (int j = i + 1; j < tempList.size(); j++) {
								if (independenceList(Arrays.asList(
										tempList.get(i), tempList.get(j)))) {
									ab.addB(tempList.get(j));
									flag[j] = true;
								}
							}

							resultAB.add(ab);
						}
					}

				}

			}

			if (tempList.size() == 1) {

				String right = tempList.get(0);

				if (hasLeft(right) == 1) {

					if (independenceList(tempList)) {

						AB ab = new AB(0, tempList);

						ab.addA(event);

						resultAB.add(ab);

					} else {

						for (String string : tempList) {

							AB ab = new AB();

							ab.addA(event);

							ab.addB(string);

							resultAB.add(ab);

						}

					}

				}

			}

			tempList = new ArrayList<String>();

			for (Relation casual : casuals) {

				if (casual.getRightTransition().equals(event)
						&& targetEvents.contains(casual.getLeftTransition())) {

					tempList.add(casual.getLeftTransition());

				}

			}

			if (tempList.size() > 1) {

				if (independenceList(tempList)) {

					AB ab = new AB(tempList, 1);

					ab.addB(event);

					resultAB.add(ab);

				} else {

					boolean flag[] = new boolean[tempList.size()];

					for (int i = 0; i < tempList.size(); i++) {

						if (!flag[i]) {

							flag[i] = true;

							AB ab = new AB();
							ab.addB(event);
							ab.addA(tempList.get(i));

							for (int j = i + 1; j < tempList.size(); j++) {
								if (independenceList(Arrays.asList(
										tempList.get(i), tempList.get(j)))) {
									ab.addA(tempList.get(j));
									flag[j] = true;
								}
							}

							resultAB.add(ab);
						}
					}

				}

			}

		}

	}

	private int hasLeft(String right) {

		int num = 0;

		for (Relation casual : casuals) {
			if (casual.getRightTransition().equals(right)
					&& targetEvents.contains(casual.getLeftTransition())) {
				num++;
			}
		}
		return num;

	}

	private void getPF() {

		places = new ArrayList<>();

		arcs = new ArrayList<>();

		for (AB ab : resultAB) {

			PetriPlace p = new PetriPlace(ab.getPlaceName());

			places.add(p);

			List<String> A = ab.getA();

			List<String> B = ab.getB();

			for (String a : A) {

				PetriArc arc = new PetriArc(new PetriTransition(a), p);

				arcs.add(arc);

			}

			for (String b : B) {

				PetriArc arc = new PetriArc(p, new PetriTransition(b));

				arcs.add(arc);

			}

		}

	}

	private boolean independenceList(List<String> targetsList) {

		String[] strings = targetsList.toArray(new String[targetsList.size()]);

		for (int i = 0; i < strings.length; i++)

			for (int j = i + 1; j < strings.length; j++) {

				int x = allEvents.indexOf(strings[i]);

				int y = allEvents.indexOf(strings[j]);

				if (relations[x][y].getType() != Constant.INDEPENDENCE_RELATION)

					return false;

			}

		return true;
	}

}
