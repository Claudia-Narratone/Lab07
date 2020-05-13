package it.polito.tdp.poweroutages.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		System.out.println(model.getNercList());
		System.out.println(model.getEventList());
		//System.out.println(model.doWorstCase(6, 40, new Nerc(1, "ERCOT")));
	}

}
