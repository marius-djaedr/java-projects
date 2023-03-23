package com.me.ttrpg.dungeonworld.generator.steading;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.me.ttrpg.dungeonworld.constant.Functions;
import com.me.ttrpg.dungeonworld.constant.steading.Defense;
import com.me.ttrpg.dungeonworld.constant.steading.OtherTag;
import com.me.ttrpg.dungeonworld.constant.steading.Population;
import com.me.ttrpg.dungeonworld.constant.steading.Prosperity;
import com.me.ttrpg.dungeonworld.dto.Steading;
import com.me.util.utils.RollUtil;

@Component
public class VillageGenerator extends AbstractSteadingGenerator {

	private static final List<Consumer<Steading>> MISC = Arrays.asList(Functions::doNothing,
			VillageGenerator::naturallyDefended, VillageGenerator::abundantResource, VillageGenerator::underProtection,
			VillageGenerator::road, VillageGenerator::wizard, VillageGenerator::significant);
	private static final List<Consumer<Steading>> PROBLEMS = Arrays.asList(Functions::doNothing, VillageGenerator::arid,
			VillageGenerator::deity, VillageGenerator::battle, VillageGenerator::monster, VillageGenerator::absorbed,
			VillageGenerator::unwelcoming);

	@Override
	protected void mutateSteading(Steading steading) {
		steading.setType("Village");
		steading.setProsperity(Prosperity.POOR);
		steading.setPopulation(Population.STEADY);
		steading.setDefense(Defense.MILITIA);
		steading.addOther(OtherTag.RESOURCE, "choice");
		steading.addOther(OtherTag.OATH, RollUtil.existingOrNew());
		RollUtil.randomElement(MISC).accept(steading);
		RollUtil.randomElement(PROBLEMS).accept(steading);
	}

	// ==========================================================================================
	// MISC

	private static void naturallyDefended(final Steading steading) {
		Defense.alter(steading, -1);
		steading.addOther(OtherTag.SAFE, null);
		steading.addDescriptor("naturally defended");
	}

	private static void abundantResource(final Steading steading) {
		Prosperity.alter(steading, 1);
		steading.addOther(OtherTag.ENMITY, RollUtil.existingOrNew());
		steading.addDescriptor("has abundance of RESOURCE");
	}

	private static void underProtection(final Steading steading) {
		Defense.alter(steading, 1);
		steading.addDescriptor("protected by OATH");
	}

	private static void road(final Steading steading) {
		Prosperity.alter(steading, 1);
		steading.addOther(OtherTag.TRADE, RollUtil.existingOrNew());
		steading.addDescriptor("on major road");
	}

	private static void wizard(final Steading steading) {
		steading.addOther(OtherTag.PERSONAGE, "wizard");
		steading.addOther(OtherTag.BLIGHT, "arcane creatures");
		steading.addDescriptor("around wizard tower");
	}

	private static void significant(final Steading steading) {
		steading.addOther(OtherTag.HISTORY, RollUtil.randomElement("battle", "miracle", "myth", "romance", "tragedy"));
		steading.addOther(OtherTag.DIVINE, null);
		steading.addDescriptor("of religious significance");
	}

	// ==========================================================================================
	// PROBLEMS
	private static void arid(final Steading steading) {
		steading.addOther(OtherTag.NEED, "food");
		steading.addDescriptor("arid/uncultivable");
	}

	private static void deity(final Steading steading) {
		steading.addOther(OtherTag.RELIGION, "choice");
		steading.addOther(OtherTag.ENMITY, RollUtil.existingOrNew());
		steading.addDescriptor("dedicated to RELIGION");
	}

	private static void battle(final Steading steading) {
		Population.alter(steading, -1);
		final String hard = RollUtil.randomElement("", " and hard fought");
		if (!hard.isEmpty()) {
			Prosperity.alter(steading, -1);
		}
		final String lose = RollUtil.randomElement("", " and losing");
		if (!lose.isEmpty()) {
			Defense.alter(steading, -1);
		}
		steading.addDescriptor("recent" + hard + lose + " battle");
	}

	private static void monster(final Steading steading) {
		steading.addOther(OtherTag.BLIGHT, "monster");
		steading.addOther(OtherTag.NEED, "adventurers");
		steading.addDescriptor("monster problem");
	}

	private static void absorbed(final Steading steading) {
		Population.alter(steading, 1);
		steading.addOther(OtherTag.LAWLESS, null);
		steading.addDescriptor("absorbed other village");
	}

	private static void unwelcoming(final Steading steading) {
		Prosperity.alter(steading, -1);
		steading.addOther(OtherTag.UNIQUE_RACE, "choice");
		steading.addDescriptor("remote or unwelcoming");
	}

}
