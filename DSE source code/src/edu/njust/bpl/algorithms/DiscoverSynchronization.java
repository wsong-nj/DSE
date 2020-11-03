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

public class DiscoverSynchronization {

	private List<Relation> casuals;

	private List<Relation> transitions;

	private RelationType[][] relations;

	private List<String> allevents;

	private List<String> targetsList;

	private List<String> orjoinList;

	// PTF
	private List<PetriArc> F;

	private List<PetriNet> petriNets = new ArrayList<>();

	public DiscoverSynchronization(DiscoverEventRelations discoverEventRelations) {

		this.casuals = discoverEventRelations.getCausal();

		this.transitions = discoverEventRelations.getTransitveClosureofCausal();

		this.relations = discoverEventRelations.getRelations();

		this.allevents = discoverEventRelations.getEvents();

	}

	public List<PetriNet> getPetriNets() {
		return petriNets;
	}

	public void mainBody() {

		for (String event : allevents) {

			if (findSynchronzation(event)) {

				PetriNet petriNet = new PetriNet();

				F = new ArrayList<>();

				List<String> Tf = new ArrayList<>();

				for (Relation relation : transitions) {

					if (relation.getLeftTransition().equals(event)
							&& !relation.getRightTransition().equals("te"))

						Tf.add(relation.getRightTransition());

				}

				anotherToGetTs(Tf);

				AerfaMiner aerfaMiner = new AerfaMiner(Tf, allevents,
						relations, casuals);

				petriNet = aerfaMiner.miner();

				PetriPlace xor_join = new PetriPlace("XOR-join");

				petriNet.addPlace(xor_join);

				for (String string : orjoinList) {

					PetriArc arc = new PetriArc(new PetriTransition(string),
							xor_join);

					F.add(arc);

				}

				petriNet.addEdges(F);

				petriNets.add(petriNet);

			}

		}

	}

	private void anotherToGetTs(List<String> tf) {

		boolean hasNext;

		orjoinList = new ArrayList<>();

		for (String tString : tf) {

			hasNext = false;

			for (Relation relation : transitions) {

				if (relation.getLeftTransition().equals(tString)
						&& !relation.getRightTransition().equals(tString)
						&& tf.contains(relation.getRightTransition())) {

					hasNext = true;
					break;
				}

			}

			if (!hasNext) {
				orjoinList.add(tString);
			}
		}

	}

	private boolean findSynchronzation(String event) {

		targetsList = new ArrayList<String>();

		for (Relation relation : casuals) {

			if (event.equals(relation.getLeftTransition())) {

				targetsList.add(relation.getRightTransition());

			}
		}

		if (targetsList.size() > 1) {

			if (interleavingList(targetsList)) {

				for (String strs : allevents) {

					List<String> matchList = new ArrayList<String>();

					for (Relation relation : casuals) {

						if (strs.equals(relation.getRightTransition())) {

							matchList.add(relation.getLeftTransition());

						}
					}

					if (interleavingList(matchList)) {

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

	private boolean interleavingList(List<String> targetsList2) {

		String[] strings = targetsList.toArray(new String[targetsList.size()]);

		for (int i = 0; i < strings.length; i++)

			for (int j = i + 1; j < strings.length; j++) {

				int x = allevents.indexOf(strings[i]);

				int y = allevents.indexOf(strings[j]);

				if (relations[x][y].getType() != Constant.INTERLEAVING_RELATION)

					return false;

			}

		return true;
	}

}
