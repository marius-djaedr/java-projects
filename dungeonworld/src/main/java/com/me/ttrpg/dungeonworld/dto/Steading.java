package com.me.ttrpg.dungeonworld.dto;

import java.util.ArrayList;
import java.util.List;

import com.me.ttrpg.dungeonworld.constant.steading.Defense;
import com.me.ttrpg.dungeonworld.constant.steading.OtherTag;
import com.me.ttrpg.dungeonworld.constant.steading.Population;
import com.me.ttrpg.dungeonworld.constant.steading.Prosperity;

public class Steading {
	private String type;
	private String name;
	private Prosperity prosperity;
	private Population population;
	private Defense defense;
	private final List<String> descriptors = new ArrayList<>();
	private final List<SteadingTagDetail> others = new ArrayList<>();

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Prosperity getProsperity() {
		return prosperity;
	}

	public void setProsperity(final Prosperity prosperity) {
		this.prosperity = prosperity;
	}

	public Population getPopulation() {
		return population;
	}

	public void setPopulation(final Population population) {
		this.population = population;
	}

	public Defense getDefense() {
		return defense;
	}

	public void setDefense(final Defense defenses) {
		this.defense = defenses;
	}

	public List<String> getDescriptors() {
		return descriptors;
	}

	public boolean addDescriptor(final String descriptor) {
		return descriptors.add(descriptor);
	}

	public List<SteadingTagDetail> getOthers() {
		return others;
	}

	public boolean addOther(final OtherTag other, final String detail) {
		return others.add(new SteadingTagDetail(other, detail));
	}

	@Override
	public String toString() {
		String ret = type + ": " + name;
		ret += "\n[ul]";
		ret += "\n[li]Prosperity, " + prosperity + "[/li]";
		ret += "\n[li]Population, " + population + "[/li]";
		ret += "\n[li]Defense, " + defense + "[/li]";
		for(final String descriptor : descriptors) {
			ret += "\n[li]" + descriptor + "[/li]";
		}
		for(final SteadingTagDetail other : others) {
			ret += "\n[li]" + other.toString() + "[/li]";
		}
		ret += "\n[/ul]";
		return ret;

	}

}
