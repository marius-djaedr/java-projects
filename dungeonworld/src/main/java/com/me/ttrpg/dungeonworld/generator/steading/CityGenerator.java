package com.me.ttrpg.dungeonworld.generator.steading;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.me.ttrpg.dungeonworld.constant.Functions;
import com.me.ttrpg.dungeonworld.constant.steading.Defense;
import com.me.ttrpg.dungeonworld.constant.steading.OtherTag;
import com.me.ttrpg.dungeonworld.constant.steading.Population;
import com.me.ttrpg.dungeonworld.constant.steading.Prosperity;
import com.me.ttrpg.dungeonworld.dto.Steading;
import com.me.util.utils.RollUtil;

@Component
public class CityGenerator extends AbstractSteadingGenerator {

	private static final List<Consumer<Steading>> MISC = Arrays.asList(Functions::doNothing, CityGenerator::defenses,
			CityGenerator::ruler, CityGenerator::diverse, CityGenerator::trade, CityGenerator::ancient,
			CityGenerator::learning);

	private static final List<Consumer<Steading>> PROBLEMS = Arrays.asList(Functions::doNothing,
			CityGenerator::outgrown, CityGenerator::designs, CityGenerator::theocracy, CityGenerator::democracy,
			CityGenerator::superDefense, CityGenerator::pop);

	@Override
	protected void mutateSteading(Steading steading) {
		steading.setType("City");
		steading.setProsperity(Prosperity.MODERATE);
		steading.setPopulation(Population.STEADY);
		steading.setDefense(Defense.GUARD);
		steading.addOther(OtherTag.MARKET, null);
		steading.addOther(OtherTag.GUILD, "choice");
		steading.addOther(OtherTag.OATH, RollUtil.existingOrNew());
		steading.addOther(OtherTag.OATH, RollUtil.existingOrNew());
		RollUtil.randomElement(MISC).accept(steading);
		RollUtil.randomElement(PROBLEMS).accept(steading);
	}

	// ==========================================================================================
	// MISC

	private static void defenses(final Steading steading) {
		Defense.alter(steading, 1);
		steading.addOther(OtherTag.OATH, RollUtil.existingOrNew());
		steading.addDescriptor("permanent defenses");
	}

	private static void ruler(final Steading steading) {
		steading.addOther(OtherTag.PERSONAGE, "ruler");
		steading.addOther(OtherTag.POWER, "political");
		steading.addDescriptor("single ruler");
	}

	private static void diverse(final Steading steading) {
		IntStream.range(1, RollUtil.roll(4) + 1).forEach(i -> steading.addOther(OtherTag.UNIQUE_RACE, "choice"));
		steading.addDescriptor("diverse");
	}

	private static void trade(final Steading steading) {
		Prosperity.alter(steading, 1);
		steading.addOther(OtherTag.TRADE, "all nearby");
		steading.addDescriptor("trade hub");
	}

	private static void ancient(final Steading steading) {
		steading.addOther(OtherTag.HISTORY, "choice");
		steading.addOther(OtherTag.DIVINE, null);
		steading.addDescriptor("ancient / built on top of its own ruins");
	}

	private static void learning(final Steading steading) {
		steading.addOther(OtherTag.ARCANE, null);
		steading.addOther(OtherTag.CRAFT, "choice");
		steading.addOther(OtherTag.POWER, "arcane");
		steading.addDescriptor("center of learning");
	}

	// ==========================================================================================
	// PROBLEMS
	private static void outgrown(final Steading steading) {
		Population.alter(steading, 1);
		steading.addOther(OtherTag.NEED, "food");
		steading.addDescriptor("outgrown resources");
	}

	private static void designs(final Steading steading) {
		Defense.alter(steading, 1);
		steading.addOther(OtherTag.ENMITY, RollUtil.existingOrNew());
		steading.addDescriptor("has designs on nearby territory");
	}

	private static void theocracy(final Steading steading) {
		Defense.alter(steading, -1);
		steading.addOther(OtherTag.POWER, "divine");
		steading.addDescriptor("theocracy");
	}

	private static void democracy(final Steading steading) {
		Defense.alter(steading, -1);
		Population.alter(steading, 1);
		steading.addDescriptor("ruled by the people");
	}

	private static void superDefense(final Steading steading) {
		Defense.alter(steading, 1);
		steading.addOther(OtherTag.BLIGHT, "supernatural creatures");
		steading.addDescriptor("supernatural defenses against BLIGHT");
	}

	private static void pop(final Steading steading) {
		steading.addOther(OtherTag.ARCANE, null);
		steading.addOther(OtherTag.PERSONAGE, "watcher");
		steading.addOther(OtherTag.BLIGHT, "arcane creatures");
		steading.addDescriptor("lies on place of power");
	}
}
