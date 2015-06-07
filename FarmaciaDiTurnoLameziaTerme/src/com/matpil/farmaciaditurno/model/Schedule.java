package com.matpil.farmaciaditurno.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Schedule {

	private Map<String, Farmacia> schedule;

	public Schedule() {
		this.schedule = new HashMap<String, Farmacia>();
	}

	public void addFarmaciaInGiorniFestivi(Farmacia f) {

		String key = "FESTIVO";
		if (this.schedule.containsKey(key)) {
			key = "_" + key;
		}
		this.schedule.put(key, f);
	}

	public void addFarmaciaNotturno(Farmacia f) {
		String key = "NOTTURNO";
		if (this.schedule.containsKey(key)) {
			key = "_" + key;
		}
		this.schedule.put(key, f);
	}

	public Map<String, Farmacia> getSchedule() {
		return schedule;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<String> keySet = this.schedule.keySet();
		for (String key : keySet) {
			sb.append(key).append(" - ");
			sb.append(this.schedule.get(key)).append("\n");
		}
		return sb.toString();
	}
}
