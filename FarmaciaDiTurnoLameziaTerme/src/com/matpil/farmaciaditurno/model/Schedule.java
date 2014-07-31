package com.matpil.farmaciaditurno.model;

import java.util.ArrayList;
import java.util.List;

public class Schedule {

	private List<Farmacia> festivi;
	private List<Farmacia> notturno;

	public Schedule() {
		this.festivi = new ArrayList<Farmacia>();
		this.notturno = new ArrayList<Farmacia>();
	}

	public void addFarmaciaInGiorniFestivi(Farmacia f) {
		this.festivi.add(f);
	}

	public void addFarmaciaNotturno(Farmacia f) {
		this.notturno.add(f);
	}

	public List<Farmacia> getFestivi() {
		return festivi;
	}

	public void setFestivi(List<Farmacia> festivi) {
		this.festivi = festivi;
	}

	public List<Farmacia> getNotturno() {
		return notturno;
	}

	public void setNotturno(List<Farmacia> notturno) {
		this.notturno = notturno;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();	
		sb.append("Festivi: ");
		for (Farmacia farmacia : getFestivi()) {			
			sb.append(farmacia.toString());
			sb.append(";");
		}
		for (Farmacia farmacia : getNotturno()) {
			sb.append(" - Notturno: ");
			sb.append(farmacia.toString());
		}
		return sb.toString();
	}
}
