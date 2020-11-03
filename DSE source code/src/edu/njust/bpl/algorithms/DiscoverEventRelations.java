package edu.njust.bpl.algorithms;

import java.util.ArrayList;
import java.util.List;

import edu.njust.bpl.notation.Constant;
import edu.njust.bpl.notation.SetofEvents;
import edu.njust.bpl.structure.EventFlow;
import edu.njust.bpl.structure.Relation;
import edu.njust.bpl.structure.RelationType;

/**
 * Algorithm 2: Discover event relations
 * 
 * @author zhen
 * 
 */
public class DiscoverEventRelations {

	private List<Relation> directPrecedence = new ArrayList<Relation>(); // ">"relation

	private List<Relation> causal = new ArrayList<Relation>(); // "->"relation

	private List<Relation> reCausal = new ArrayList<Relation>(); // "<-"relation

	private List<Relation> interleaving = new ArrayList<Relation>(); // "||"relation

	private List<Relation> independence = new ArrayList<Relation>(); // "#"relation

	private List<Relation> transitveClosureofCausal = new ArrayList<Relation>(); // "-->"relation

	private RelationType[][] directedRelations;

	private RelationType[][] relations;

	private RelationType[][] transitiveRelations;

	private List<EventFlow> traces;

	private List<String> events = new ArrayList<String>();

	public DiscoverEventRelations(List<EventFlow> L) {

		this.traces = L;

		mainBody();

	}

	public void mainBody() {

		SetofEvents setofEvents = new SetofEvents(traces);

		events = setofEvents.getEvents();

		int length = events.size();

		directedRelations = new RelationType[length][length];

		relations = new RelationType[length][length];

		transitiveRelations = new RelationType[length][length];

		for (String event : events) {

			RelationType aType = new RelationType(
					Constant.TRANSITVE_CLOSURE_OF_CAUSAL_RELATION);

			Relation trans = new Relation(event, event, aType);

			transitveClosureofCausal.add(trans);

		}

		for (int i = 0; i < length; i++) {

			for (int j = 0; j < length; j++) {

				directedRelations[i][j] = new RelationType();

				if (i == j) {

					relations[i][j] = new RelationType(
							Constant.INDEPENDENCE_RELATION);

					transitiveRelations[i][j] = new RelationType(
							Constant.TRANSITVE_CLOSURE_OF_CAUSAL_RELATION);

				} else

					relations[i][j] = new RelationType();

				transitiveRelations[i][j] = new RelationType();

			}

		}

		for (EventFlow eFlow : traces) {

			String[] strs = eFlow.toArray(new String[eFlow.size()]);

			for (int i = 1; i < strs.length; i++) {

				RelationType type = new RelationType(
						Constant.DIRECT_PRECEDENCE_RELATION);

				Relation relation = new Relation(strs[i - 1], strs[i], type);

				if (!directPrecedence.contains(relation)) {

					directPrecedence.add(relation);

					int x = events.indexOf(strs[i - 1]);

					int y = events.indexOf(strs[i]);

					directedRelations[x][y] = type;

				}

			}

		}

		for (int i = 0; i < length; i++) {

			for (int j = 0; j < length; j++) {

				if (i != j) {

					String left = events.get(i);

					String right = events.get(j);

					if (directedRelations[i][j].getType() == Constant.DIRECT_PRECEDENCE_RELATION
							&& directedRelations[j][i].getType() != Constant.DIRECT_PRECEDENCE_RELATION) {

						relations[i][j] = new RelationType(
								Constant.CAUSAL_RELATION);

						RelationType relationType = new RelationType(
								Constant.CAUSAL_RELATION);

						Relation relation = new Relation(left, right,
								relationType);

						causal.add(relation);

					} else

					if (directedRelations[i][j].getType() != Constant.DIRECT_PRECEDENCE_RELATION
							&& directedRelations[j][i].getType() == Constant.DIRECT_PRECEDENCE_RELATION) {

						relations[i][j] = new RelationType(
								Constant.REVERSE_CAUSAL_RELATION);

						RelationType relationType = new RelationType(
								Constant.REVERSE_CAUSAL_RELATION);

						Relation relation = new Relation(left, right,
								relationType);

						reCausal.add(relation);

					} else

					if (directedRelations[i][j].getType() == Constant.DIRECT_PRECEDENCE_RELATION
							&& directedRelations[j][i].getType() == Constant.DIRECT_PRECEDENCE_RELATION) {

						RelationType relationType = new RelationType(
								Constant.INTERLEAVING_RELATION);

						Relation relation = new Relation(left, right,
								relationType);

						interleaving.add(relation);

						relations[i][j] = new RelationType(
								Constant.INTERLEAVING_RELATION);

					} else

					if (directedRelations[i][j].getType() != Constant.DIRECT_PRECEDENCE_RELATION
							&& directedRelations[j][i].getType() != Constant.DIRECT_PRECEDENCE_RELATION) {

						RelationType relationType = new RelationType(
								Constant.INDEPENDENCE_RELATION);

						Relation relation = new Relation(left, right,
								relationType);

						independence.add(relation);

						relations[i][j] = new RelationType(
								Constant.INDEPENDENCE_RELATION);

					}

				}

			}

		}

		for (Relation relation : causal) {

			String left = relation.getLeftTransition();

			String right = relation.getRightTransition();

			RelationType relationType = new RelationType(
					Constant.TRANSITVE_CLOSURE_OF_CAUSAL_RELATION);

			Relation relation2 = new Relation(left, right, relationType);

			transitveClosureofCausal.add(relation2);

			int x = events.indexOf(left);

			int y = events.indexOf(right);

			transitiveRelations[x][y] = new RelationType(
					Constant.TRANSITVE_CLOSURE_OF_CAUSAL_RELATION);

		}

		computeTransitve(length);

		for (int i = 0; i < events.size(); i++) {
			for (int j = 0; j < events.size(); j++) {
				if (i == j) {
					transitiveRelations[i][j] = new RelationType(
							Constant.TRANSITVE_CLOSURE_OF_CAUSAL_RELATION);
				}
			}
		}

	}

	private void computeTransitve(int length) {

		for (int i = 0; i < length; i++)

			for (int j = 0; j < length; j++)

				if (transitiveRelations[j][i].getType() == Constant.TRANSITVE_CLOSURE_OF_CAUSAL_RELATION) {

					for (int k = 0; k < length; k++) {

						if (j == k)

							continue;

						if (transitiveRelations[j][k].getType() == Constant.TRANSITVE_CLOSURE_OF_CAUSAL_RELATION)

							continue;

						if (transitiveRelations[i][k].getType() == Constant.TRANSITVE_CLOSURE_OF_CAUSAL_RELATION) {

							transitiveRelations[j][k] = new RelationType(
									Constant.TRANSITVE_CLOSURE_OF_CAUSAL_RELATION);

							RelationType relationType = new RelationType(
									Constant.TRANSITVE_CLOSURE_OF_CAUSAL_RELATION);

							String left = events.get(j);

							String right = events.get(k);

							Relation relation = new Relation(left, right,
									relationType);

							transitveClosureofCausal.add(relation);

						}

					}

				}
	}

	public List<String> getEvents() {
		return events;
	}

	public List<Relation> getDirectPrecedence() {
		return directPrecedence;
	}

	public List<Relation> getCausal() {
		return causal;
	}

	public List<Relation> getReCausal() {
		return reCausal;
	}

	public List<Relation> getInterleaving() {
		return interleaving;
	}

	public List<Relation> getIndependence() {
		return independence;
	}

	public List<Relation> getTransitveClosureofCausal() {
		return transitveClosureofCausal;
	}

	public RelationType[][] getDirectedRelations() {
		return directedRelations;
	}

	public RelationType[][] getRelations() {
		return relations;
	}

	public RelationType[][] getTransitiveRelations() {
		return transitiveRelations;
	}

}
