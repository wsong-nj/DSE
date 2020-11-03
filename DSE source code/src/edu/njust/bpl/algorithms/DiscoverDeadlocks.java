package edu.njust.bpl.algorithms;

import java.util.ArrayList;
import java.util.List;

import edu.njust.bpl.notation.Constant;
import edu.njust.bpl.structure.PetriArc;
import edu.njust.bpl.structure.PetriNet;
import edu.njust.bpl.structure.PetriPlace;
import edu.njust.bpl.structure.PetriTransition;
import edu.njust.bpl.structure.Relation;
import edu.njust.bpl.structure.RelationType;

public class DiscoverDeadlocks {

	private List<Relation> casuals;

	private List<Relation> transitions;

	private RelationType[][] relations;

	private RelationType[][] transitive;

	private List<String> allevents;

	private List<String> targetsList;

	// PTF
	private List<PetriPlace> P;

	private List<PetriArc> F;

	private List<PetriNet> petriNets = new ArrayList<>();

	public DiscoverDeadlocks(DiscoverEventRelations discoverEventRelations) {

		casuals = discoverEventRelations.getCausal();

		transitions = discoverEventRelations.getTransitveClosureofCausal();

		relations = discoverEventRelations.getRelations();

		allevents = discoverEventRelations.getEvents();

		transitive = discoverEventRelations.getTransitiveRelations();

	}

	public List<PetriNet> getPetriNets() {
		return petriNets;
	}

	public void mainBody() {

		for (String event : allevents) {

			if (!event.equals("te") && findDeadlock(event)) {

				List<String> or_spiltList = new ArrayList<>();
				for (Relation relation : casuals) {
					if (relation.getLeftTransition().equals(event))
						or_spiltList.add(relation.getRightTransition());
				}

				PetriNet petriNet = new PetriNet();

				P = new ArrayList<>();

				F = new ArrayList<>();

				List<String> Tf = new ArrayList<>();

				for (Relation relation : transitions) {

					if (relation.getLeftTransition().equals(event))

						Tf.add(relation.getRightTransition());

				}

				anotherToGetTs(Tf);
				removeRepetition();

				AerfaMiner aerfaMiner = new AerfaMiner(Tf, allevents,
						relations, casuals);

				petriNet = aerfaMiner.miner();

				PetriTransition and_join = new PetriTransition("AND-join");

				petriNet.addTransition(and_join);

				for (String string : or_spiltList) {

					PetriPlace pi = new PetriPlace("P" + string);
					P.add(pi);

					for (String target : targetsList) {
						int x = allevents.indexOf(string);
						int y = allevents.indexOf(target);
						int type = transitive[x][y].getType();
						if (type == Constant.TRANSITVE_CLOSURE_OF_CAUSAL_RELATION) {
							PetriArc arc = new PetriArc(new PetriTransition(
									target), pi);
							F.add(arc);
						}
					}

					PetriArc arc = new PetriArc(pi, and_join);
					F.add(arc);

				}

				petriNet.addPlaces(P);

				petriNet.addEdges(F);

				petriNets.add(petriNet);

			}

		}

	}

	private void anotherToGetTs(List<String> tf) {

		boolean hasNext;

		targetsList.removeAll(targetsList);

		for (String tString : tf) {

			hasNext = false;

			for (Relation relation : transitions) {

				if (relation.getLeftTransition().equals(tString)
						&& !relation.getRightTransition().equals(tString)) {
					hasNext = true;
					break;
				}

			}

			if (!hasNext) {
				targetsList.add(tString);
			}
		}

	}

	private void removeRepetition() {

		for (int i = 0; i < targetsList.size() - 1; i++) {

			for (int j = targetsList.size() - 1; j > i; j--) {

				if (targetsList.get(j).equals(targetsList.get(i))) {

					targetsList.remove(j);

				}
			}
		}

	}

	private boolean findDeadlock(String event) {

		targetsList = new ArrayList<String>();

		for (Relation relation : casuals) {

			if (event.equals(relation.getLeftTransition())) {

				targetsList.add(relation.getRightTransition());

			}

		}

		if (targetsList.size() > 1) {

			if (independenceList(targetsList)) {

				for (String strs : allevents) {

					List<String> matchList = new ArrayList<String>();

					for (Relation relation : casuals) {

						if (strs.equals(relation.getRightTransition())) {

							matchList.add(relation.getLeftTransition());

						}

					}

					if (independenceList(matchList)) {

						boolean match = true;

						for (String target : targetsList) {

							match = true;

							boolean end = false;

							for (Relation relation : transitions) {

								if (relation.getLeftTransition().equals(target)
										&& matchList.contains(relation
												.getRightTransition())) {

									end = true;

									break;

								}

							}

							if (!end) {

								match = false;

								break;
							}
						}
						if (match)
							return false;
					} else
						continue;
				}
				return true;
			} else
				return false;
		} else
			return false;
	}

	private boolean independenceList(List<String> targetsList) {

		String[] strings = targetsList.toArray(new String[targetsList.size()]);

		for (int i = 0; i < strings.length; i++)

			for (int j = i + 1; j < strings.length; j++) {

				int x = allevents.indexOf(strings[i]);

				int y = allevents.indexOf(strings[j]);

				if (relations[x][y].getType() != Constant.INDEPENDENCE_RELATION)

					return false;
			}
		return true;
	}
}
