package com.me.ttrpg.dungeonworld.generator.npc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;

import com.me.ttrpg.dungeonworld.dto.Npc;
import com.me.ttrpg.dungeonworld.generator.DungeonWorldGenerator;
import com.me.util.utils.RollUtil;

public abstract class AbstractNpcGenerator implements DungeonWorldGenerator<Npc> {
	private static final List<String> NAME1 = Arrays.asList("", "", "", "", "A", "BE", "DE", "EL", "FA", "JO", "KI", "LA", "MA", "NA", "O", "PA",
			"RE", "SI", "TA", "VA");
	private static final List<String> NAME2 = Arrays.asList("BAR", "CHED", "DELL", "FAR", "GRAN", "HAL", "JEN", "KEL", "LIM", "MOR", "NET", "PENN",
			"QUIL", "ROND", "SARK", "SHEN", "TUR", "VASH", "YOR", "ZEN");
	private static final List<String> NAME3 = Arrays.asList("", "A", "AC", "AI", "AL", "AM", "AN", "AR", "EA", "EL", "ER", "ESS", "ETT", "IC", "ID",
			"IL", "IN", "IS", "OR", "US");

	private static final List<String> CHARACTERISTICS = Arrays.asList("Absentminded", "Arrogant", "Boorish", "Chews something", "Clumsy", "Curious",
			"Dim-witted", "Fiddles and fidgets nervously", "Frequently uses	the wrong word", "Friendly", "Irritable",
			"Prone to predictions of certain doom", "Pronounced scar", "Slurs words, lisps, or stutters", "Speaks loudly or whispers", "Squints",
			"Stares into the distance", "Suspicious", "Uses colerful oaths and exclamations", "Uses flowery speech or long words");
	private static final List<String> IDEALS = Arrays.asList("Aspiration (any)", "Charity (good)", "Community (lawful)", "Creativity (chaotic)",
			"Discovery (any)", "Fairness (lawful)", "Freedom (chaotic)", "Glory (any)", "Greater good (good)", "Greed (evil)", "Honor (lawful)",
			"Independence (chaotic)", "Knowledge (neutral)", "Life (good)", "Live and let live (neutral)", "Might (evil)", "Nation (any)",
			"People (neutral)", "Power (evil)", "Redemption (any)");
	private static final List<String> FLAWS = Arrays.asList("Forbidden love or romantic susceptibility", "Decadence", "Arrogance",
			"Envy of another person's possessions or station", "Overpowering greed", "Prone to rage", "Powerful enemy", "Specific phobia",
			"Shameful or scandalous history", "Secret crime or misdeed", "Possession of forbidden lore", "Foolhardy bravery");
	private static final List<String> BONDS = Arrays.asList("Personal goal or achievement", "Family members", "Colleagues or compatriots",
			"Benefactor, patron, or employer", "Romantic interest", "Special place", "Keepsake", "Valuable possession", "Revenge");

	private static final List<String> SEXES = Arrays.asList("Male", "Female");

	@Value("#{'${races.common}'.split(',')}")
	private List<String> commonRaces;
	@Value("#{'${races.uncommon}'.split(',')}")
	private List<String> uncommonRaces;

	@Override
	public Npc generate() {
		final Npc npc = new Npc();

		npc.setName(generateName() + " " + generateName());
		npc.setSex(RollUtil.randomElement(SEXES));
		npc.setRace(generateRace(commonRaces, uncommonRaces));

		npc.setCharacteristic(RollUtil.randomElement(CHARACTERISTICS));
		npc.setIdeal("believes in " + RollUtil.randomElement(IDEALS));
		npc.setFlaw("is plagued by " + RollUtil.randomElement(FLAWS));
		IntStream.range(0, RollUtil.numToGenerate()).mapToObj(i -> "bonded to " + RollUtil.randomElement(BONDS)).forEach(npc::addBond);

		return npc;
	}

	private static String generateName() {
		return RollUtil.randomElement(NAME1) + RollUtil.randomElement(NAME2) + RollUtil.randomElement(NAME3);
	}

	protected abstract String generateRace(List<String> common, List<String> uncommon);
}
