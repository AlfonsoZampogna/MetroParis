package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	private Graph<Fermata,DefaultEdge> grafo;
	private MetroDAO dao;
	private Map<Integer,Fermata> fermate;
	private Map<Integer,Connessione> connessioni;
	
	public Model() {
		this.dao = new MetroDAO();
		fermate = new HashMap<Integer,Fermata>();
		connessioni = new HashMap<Integer,Connessione>();
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleDirectedGraph<Fermata,DefaultEdge>(DefaultEdge.class);
		
		//aggiungo i vertici
		for(Fermata f : this.dao.getAllFermate()) 
			fermate.put(f.getIdFermata(), f);
		
		Graphs.addAllVertices(grafo, fermate.values());
		
		//aggiungo gli archi
		
		//METODO 1 (DOPPIO LOOP SU OGNI FERMATA)
		//for(Fermata f1 : fermate.values()) {
			//for(Fermata f2 : fermate.values()) {
				
			//}
		//}
		
		for(Connessione c : this.dao.getAllConnessioni()) 
			connessioni.put(c.getIdConnessione(), c);
		
		for(Connessione c : connessioni.values())
			grafo.addEdge(c.getStazP(), c.getStazA());
		
		System.out.println("GRAFO CREATO!");
		System.out.println("# VERTICI: "+this.grafo.vertexSet().size());
		System.out.println("# ARCHI: "+this.grafo.edgeSet().size());
	}
	
	public /*List<Fermata>*/ void visitaGrafo(int IdFermataPartenza){
		List<Fermata> fermateAmpiezza = new ArrayList<Fermata>();
		List<Fermata> fermateProfondita = new ArrayList<Fermata>();
		Fermata partenza = fermate.get(IdFermataPartenza);
		
		//VISITA IN AMPIEZZA
		System.out.println("VISITA IN AMPIEZZA: \n");
		GraphIterator<Fermata,DefaultEdge> iteratore = 
				new BreadthFirstIterator <Fermata,DefaultEdge>(this.grafo, partenza);
		while(iteratore.hasNext()) {
			Fermata f = iteratore.next();
			fermateAmpiezza.add(f);
			System.out.println(f);
		}
		
		//VISITA IN PROFONDITA
		System.out.println("VISITA IN PROFONDITA: \n");
		GraphIterator<Fermata,DefaultEdge> visita =  
				new DepthFirstIterator<Fermata,DefaultEdge>(this.grafo, partenza);
		while(visita.hasNext()) {
			Fermata f = visita.next();
			fermateProfondita.add(f);
			System.out.println(f);
		}
		
		//return fermateProfondita;
	}

	public List<Fermata> getFermate() {
		return this.dao.getAllFermate();
	}

	public List<Connessione> getConnessioni() {
		return this.dao.getAllConnessioni();
	}

	public List<Fermata> calcolaPercorso(Fermata partenza, Fermata arrivo) {
		creaGrafo();
		List<Fermata> percorso = new ArrayList<Fermata>();
		
		BreadthFirstIterator<Fermata,DefaultEdge> iteratore = 
				new BreadthFirstIterator <Fermata,DefaultEdge>(this.grafo, partenza);
		//CONTROLLO CHE PARTENZA SIA CONNESSO AD ARRIVO (ANCHE SE SO CHE LO E')
		boolean trovato = false;
		while(iteratore.hasNext()) {
			Fermata f = iteratore.next();
			if(f.equals(arrivo))
				trovato = true;
		}
		
		//VERIFICATO QUANTO PRIMA OTTENGO IL PERCORSO
		if(trovato) {
			percorso.add(arrivo);
			Fermata precedenteFermata = iteratore.getParent(arrivo);
			while(!precedenteFermata.equals(partenza)) {
				percorso.add(0,precedenteFermata);
				precedenteFermata = iteratore.getParent(precedenteFermata);
			}
			percorso.add(0, partenza);
			return percorso;
		}else
		
		return null;
	}
	
	
}
