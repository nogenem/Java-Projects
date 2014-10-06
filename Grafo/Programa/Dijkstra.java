package Programa;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import Modelo.Label;
import Modelo.Vertice;

public class Dijkstra {
	private HashMap<Vertice, HashMap<Vertice, Label>> ligacoes;
	private HashMap<Vertice, Integer> valorAtual; //valor atual dos vertices durante a busca
	private Set<Vertice> taFechado; //vertices que estao 'fechados' na busca
	private HashMap<Vertice, Set<Vertice>> vsAtualiza; //vertices utilizados para 
											   	      //atualizar o valor do custo
	
	public Dijkstra(HashMap<Vertice, HashMap<Vertice, Label>> ligacoes){
		this.ligacoes = ligacoes;
	}
	
	public void setLigacoes(HashMap<Vertice, HashMap<Vertice, Label>> ligacoes){
		this.ligacoes = ligacoes;
	}
	
	public void cleanData(){
		valorAtual = new HashMap<>();
		taFechado = new HashSet<>();
		vsAtualiza = new HashMap<>();
	}
	
	/**
	 * Executa a busca do caminho de custo minimo partindo do 
	 * vertice Source.
	 * 
	 * @param source		vertice inicial da busca.
	 */
	public void executeCode(Vertice source){
		cleanData();
		
		Vertice fechado = null;
		Set<Vertice> adjacentes = null;
		int base, tmp = 0;
		
		for(Vertice v : ligacoes.keySet()){ //inicia os valores
			valorAtual.put(v, v.equals(source) ? 0 : Integer.MAX_VALUE);
			vsAtualiza.put(v, new HashSet<Vertice>());
		}
		
		//enquanto taFechado nao tiver todos os vertices do grafo...
		while(taFechado.size() < ligacoes.keySet().size()){
			fechado = getMenorValorAtual(); //pega o menor valor atual
			taFechado.add(fechado); //fecha o vertice com o menor valor atual
			base = valorAtual.get(fechado); //o valor dele vai ser usado como base do calculo
			adjacentes = ligacoes.get(fechado).keySet();
			
			for(Vertice v : adjacentes){
				if(!v.equals(fechado)){
					tmp = base + ligacoes.get(fechado).get(v).getDist();
					if(tmp == valorAtual.get(v))
						vsAtualiza.get(v).add(fechado);
					else if(tmp < valorAtual.get(v)){
						valorAtual.put(v, tmp);
						vsAtualiza.get(v).clear(); //remove os valores desatualizados
						vsAtualiza.get(v).add(fechado);
					}
				}
			}
		}
		
	}
	
	/**
	 * Pega o vertice que nao esteja fechado ainda e que 
	 * tenha o menor 'valorAtual'.
	 * 
	 * @return		vertice com o menor 'valorAtual' e que
	 * 				nao esteja fechado ainda.
	 */
	public Vertice getMenorValorAtual(){
		Vertice min = null;
		for(Vertice v : valorAtual.keySet()){ 
			if(!taFechado.contains(v)){
				if(min == null)
					min = v;
				else if(valorAtual.get(v) < valorAtual.get(min))
					min = v;
			}
		}
		return min;
	}
	
	/**
	 * Apos a busca do caminho, retorna uma lista com o caminho
	 * minimo da source ate o destino.
	 * 
	 * @param source		vertice inicial da busca.
	 * @param dest			vertice de destino da busca.
	 * @return				lista com o UM dos caminhos minimos entre
	 * 						source e dest.
	 */
	public LinkedList<Vertice> findMinimalDistanceTo(Vertice source, Vertice dest){
		LinkedList<Vertice> result = new LinkedList<>();
		Vertice tmp = dest;
		
		result.add(dest);
		while(!tmp.equals(source)){
			tmp = vsAtualiza.get(tmp).iterator().next();
			result.add(tmp);
		}
		
		Collections.reverse(result);
		
		//soh retorna um dos caminhos minimos...
		return result;
	}
}