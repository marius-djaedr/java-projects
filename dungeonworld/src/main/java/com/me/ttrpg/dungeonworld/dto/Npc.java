package com.me.ttrpg.dungeonworld.dto;

import java.util.ArrayList;
import java.util.List;

public class Npc {
	private String name, sex, race, characteristic, ideal, flaw;
	private final List<String> bonds = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(final String sex) {
		this.sex = sex;
	}

	public String getRace() {
		return race;
	}

	public void setRace(final String race) {
		this.race = race;
	}

	public String getFlaw() {
		return flaw;
	}

	public void setFlaw(final String flaw) {
		this.flaw = flaw;
	}

	public List<String> getBonds() {
		return bonds;
	}

	public boolean addBond(final String bond) {
		return bonds.add(bond);
	}

	public String getIdeal() {
		return ideal;
	}

	public void setIdeal(final String ideal) {
		this.ideal = ideal;
	}

	public String getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(final String characteristic) {
		this.characteristic = characteristic;
	}

	@Override
	public String toString() {
		String ret = name + ": " + sex + " " + race;
		ret += "\n[ul]";
		ret += "\n[li]" + characteristic + "[/li]";
		ret += "\n[li]" + ideal + "[/li]";
		ret += "\n[li]" + flaw + "[/li]";
		for(final String bond : bonds) {
			ret += "\n[li]" + bond + "[/li]";
		}
		ret += "\n[/ul]";
		return ret;
	}
}
