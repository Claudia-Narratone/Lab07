package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO podao;
	
	private NercIdMap nercIdMap;
	private List<Nerc> nercList;
	private List<PowerOutages> eventList;
	private List<PowerOutages> eventListFiltered;
	private List<PowerOutages> solution;
	
	private int maxAffectedPeople;
	
	public Model() {
		podao = new PowerOutageDAO();
		nercIdMap = new NercIdMap();
		nercList = podao.getNercList(nercIdMap);
		eventList=podao.getPowerOutageList(nercIdMap);
	}
	
	public List<Nerc> getNercList() {
		return this.nercList;
	}

	public List<PowerOutages> doWorstCase(int maxNumberOfYears, int maxHoursOfOutage, Nerc nerc){
		
		solution=new ArrayList<>();
		maxAffectedPeople=0;
		
		for(PowerOutages p:eventList) {
			if(p.getNerc().equals(nerc)) {
				eventListFiltered.add(p);
			}
		}
		Collections.sort(eventListFiltered);

		System.out.println("Event list filtered size: " + eventListFiltered.size());

		recursive(new ArrayList<PowerOutages>(), maxNumberOfYears, maxHoursOfOutage);

		return solution;
	}

	private void recursive(ArrayList<PowerOutages> partial, int maxNumberOfYears, int maxHoursOfOutage) {
		//caso terminale
		if(sommaAffectedPeople(partial)>maxAffectedPeople) {
			maxAffectedPeople=sommaAffectedPeople(partial);
			solution= new ArrayList<PowerOutages>(partial);
		}
		
		//caso intermedio
		for(PowerOutages event : eventListFiltered) {
			if(!partial.contains(event)) {
				partial.add(event);
				
				if(checkMaxYears(partial, maxNumberOfYears) && checkHoursOfOutage(partial, maxHoursOfOutage))
					recursive(partial, maxNumberOfYears, maxHoursOfOutage);
				
				partial.remove(event);
			}
		}
		
	}

	private boolean checkHoursOfOutage(ArrayList<PowerOutages> partial, int maxHoursOfOutage) {
		long somma=0;
		for(PowerOutages event:partial) {
			somma+=event.getOutageDuration();
		}
		if(somma>maxHoursOfOutage)
			return false;
		return true;
	}

	private boolean checkMaxYears(ArrayList<PowerOutages> partial, int maxNumberOfYears) {
		if(partial.size()>=2) {
			int y1=partial.get(0).getYear();
			int y2=partial.get(partial.size()-1).getYear();
			if((y2-y1+1)>maxNumberOfYears)
				return false;
		}
		return true;
	}


	public int sommaAffectedPeople(ArrayList<PowerOutages> partial) {
		int somma=0;
		for(PowerOutages event:partial) {
			somma+=event.getAffectedPeople();
		}
		return somma;
	}

	public List<PowerOutages> getEventList() {
		return this.eventList;
	}
	
}
