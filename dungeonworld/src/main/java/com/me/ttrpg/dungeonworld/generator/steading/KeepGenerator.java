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
public class KeepGenerator extends AbstractSteadingGenerator {

	private static final List<Consumer<Steading>> MISC = Arrays.asList(Functions::doNothing, KeepGenerator::noble,
			KeepGenerator::commander, KeepGenerator::trade, KeepGenerator::special, KeepGenerator::fertile,
			KeepGenerator::border);

	private static final List<Consumer<Steading>> PROBLEMS = Arrays.asList(Functions::doNothing,
			KeepGenerator::naturallyDefended, KeepGenerator::conquest, KeepGenerator::haven, KeepGenerator::specific,
			KeepGenerator::bloody, KeepGenerator::worst);

	@Override
	protected void mutateSteading(Steading steading) {
		steading.setType("Keep");
		steading.setProsperity(Prosperity.POOR);
		steading.setPopulation(Population.SHRINKING);
		steading.setDefense(Defense.GUARD);
		steading.addOther(OtherTag.NEED, "supplies");
		steading.addOther(OtherTag.TRADE, RollUtil.existingOrNew() + " (for NEED)");
		steading.addOther(OtherTag.OATH, RollUtil.existingOrNew());
		RollUtil.randomElement(MISC).accept(steading);
		RollUtil.randomElement(PROBLEMS).accept(steading);
	}

	// ==========================================================================================
	// MISC

	private static void noble(final Steading steading) {
		Prosperity.alter(steading, 1);
		steading.addOther(OtherTag.POWER, "political");
		steading.addDescriptor("belongs to noble");
	}

	private static void commander(final Steading steading) {
		Defense.alter(steading, 1);
		steading.addOther(OtherTag.PERSONAGE, "commander");
		steading.addDescriptor("skilled commander");
	}

	private static void trade(final Steading steading) {
		Prosperity.alter(steading, 1);
		steading.addOther(OtherTag.GUILD, "trade");
		steading.addDescriptor("watches over trade road");
	}

	private static void special(final Steading steading) {
		Population.alter(steading, -1);
		steading.addOther(OtherTag.ARCANE, null);
		steading.addDescriptor("trains special troops");
	}

	private static void fertile(final Steading steading) {
		steading.getOthers().removeIf(std -> OtherTag.NEED.equals(std.getTag()) && "supplies".equals(std.getDetail()));
		steading.getOthers()
				.removeIf(std -> OtherTag.TRADE.equals(std.getTag()) && std.getDetail().endsWith(" (for NEED)"));
		steading.addDescriptor("in fertile land");
	}

	private static void border(final Steading steading) {
		Defense.alter(steading, 1);
		steading.addOther(OtherTag.ENMITY, RollUtil.existingOrNew());
		steading.addDescriptor("on border with ENMITY");
	}

	// ==========================================================================================
	// PROBLEMS
	private static void naturallyDefended(final Steading steading) {
		Population.alter(steading, -1);
		steading.addOther(OtherTag.SAFE, null);
		steading.addDescriptor("naturally defended");
	}

	private static void conquest(final Steading steading) {
		steading.addOther(OtherTag.ENMITY, RollUtil.existingOrNew());
		steading.addDescriptor("conquest from ENMITY");
	}

	private static void haven(final Steading steading) {
		steading.addOther(OtherTag.LAWLESS, null);
		steading.addDescriptor("safe haven for brigands");
	}

	private static void specific(final Steading steading) {
		steading.addOther(OtherTag.BLIGHT, "choice");
		steading.addDescriptor("built to defend against BLIGHT");
	}

	private static void bloody(final Steading steading) {
		steading.addOther(OtherTag.HISTORY, "battle");
		steading.addOther(OtherTag.BLIGHT, "restless spirits");
		steading.addDescriptor("has seen horrible and bloody war");
	}

	private static void worst(final Steading steading) {
		steading.addOther(OtherTag.NEED, "skilled recruits");
		steading.addDescriptor("given the worst of the worst");
	}
}
