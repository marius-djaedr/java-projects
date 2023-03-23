package com.me.ttrpg.dungeonworld.constant.steading;

public enum OtherTag implements SteadingTag {
	SAFE("Outside trouble doesn't come here until the players bring it. Idyllic and often hidden, if the steading would lose or degrade another beneficial tag get rid of safe instead."),
	RELIGION("The listed deity is revered here."),
	EXOTIC("There are goods and services available here that aren't available anywhere else nearby. List them."),
	RESOURCE(
			"The steading has easy access to the listed resource (e.g., a spice, a type of ore, fish, grapes). That resource is significantly cheaper."),
	NEED("The steading has an acute or ongoing need for the listed resource. That resource sells for considerably more."),
	OATH("The steading has sworn oaths to the listed steadings. These oaths are generally of fealty or support, but may be more specific."),
	TRADE("The steading regularly trades with the listed steadings."),
	MARKET("Everyone comes here to trade. On any given day the available items may be far beyond their prosperity. +1 to supply."),
	ENMITY("The steading holds a grudge against the listed steadings."),
	HISTORY("Something important once happened here, choose one and detail or make up your own: battle, miracle, myth, romance, tragedy."),
	ARCANE("Someone in town can cast arcane spells for a price. This tends to draw more arcane casters, +1 to recruit when you put out word you're looking for an adept."),
	DIVINE("There is a major religious presence, maybe a cathedral or monastery. They can heal and maybe even raise the dead for a donation or resolution of a quest. Take +1 to recruit priests here."),
	GUILD("The listed type of guild has a major presence (and usually a fair amount of influence). If the guild is closely associated with a type of hireling, +1 to recruit that type of hireling."),
	PERSONAGE(
			"There's a notable person who makes their home here. Give them a name and a short note on why they're notable."),
	CRAFT("The steading is known for excellence in the listed craft. Items of their chosen craft are more readily available here or of higher quality than found elsewhere."),
	LAWLESS("Crime is rampant; authority is weak."),
	BLIGHT("The steading has a recurring problem, usually a type of monster."),
	POWER("The steading holds sway of some type. Typically political, divine, or arcane."), UNIQUE_RACE(
			"The steading is significantly or entirely a unique race. Goods of that race are more common and less expensive than they typically are, and the steading will have a unique culture.");

	private final String description;

	private OtherTag(final String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return super.toString() + ": " + description;
	}
}
