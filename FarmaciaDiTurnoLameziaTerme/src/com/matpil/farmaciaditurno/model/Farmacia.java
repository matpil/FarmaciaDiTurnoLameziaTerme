package com.matpil.farmaciaditurno.model;

public class Farmacia {

	private String name;
	private String way;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object o) {
		Farmacia input = (Farmacia) o;
		return this.getName().equals(input.getName());
	}
}
