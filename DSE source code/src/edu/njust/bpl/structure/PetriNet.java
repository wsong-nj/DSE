package edu.njust.bpl.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * Petri net
 * 
 * @author zhen
 * 
 */
public class PetriNet {

	private List<PetriPlace> places = new ArrayList<>();

	private List<PetriTransition> transitions = new ArrayList<PetriTransition>();

	private List<PetriArc> edges = new ArrayList<>();

	public PetriNet() {
		super();
	}

	public PetriNet(List<PetriTransition> transitions) {
		super();
		this.transitions = transitions;
	}

	public void setPlaces(List<PetriPlace> places) {
		this.places = places;
	}

	public void setTransitions(List<PetriTransition> transitions) {
		this.transitions = transitions;
	}

	public void setEdges(List<PetriArc> edges) {
		this.edges = edges;
	}

	public void addPlace(PetriPlace place) {

		this.places.add(place);

	}

	public void addPlaces(List<PetriPlace> places) {

		this.places.addAll(places);

	}

	public void addTransition(PetriTransition transition) {

		this.transitions.add(transition);

	}

	public void addTransitions(List<PetriTransition> transitions) {

		this.transitions.addAll(transitions);

	}

	public void addEdge(PetriArc edge) {

		this.edges.add(edge);

	}

	public void addEdges(List<PetriArc> edges) {

		this.edges.addAll(edges);

	}

	public String toString() {

		return "P:" + places + "\n" + "T:" + transitions + "\n" + "F:" + edges;

	}

	public List<PetriTransition> getTransitions() {
		return transitions;
	}

	public List<PetriPlace> getPlaces() {
		return places;
	}

	public List<PetriArc> getEdges() {
		return edges;
	}

}
