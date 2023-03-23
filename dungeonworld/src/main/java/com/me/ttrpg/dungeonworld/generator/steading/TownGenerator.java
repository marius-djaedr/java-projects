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
public class TownGenerator extends AbstractSteadingGenerator {

	private static final List<Consumer<Steading>> MISC = Arrays.asList(Functions::doNothing, TownGenerator::booming,
			TownGenerator::crossroads, TownGenerator::underProtection, TownGenerator::church, TownGenerator::craft,
			TownGenerator::military);

	private static final List<Consumer<Steading>> PROBLEMS = Arrays.asList(Functions::doNothing, TownGenerator::tooBig,
			TownGenerator::overProtection, TownGenerator::outlaw, TownGenerator::cornered, TownGenerator::diseased,
			TownGenerator::popular);

	@Override
	protected void mutateSteading(Steading steading) {
		steading.setType("Town");
		steading.setProsperity(Prosperity.MODERATE);
		steading.setPopulation(Population.STEADY);
		steading.setDefense(Defense.WATCH);
		steading.addOther(OtherTag.TRADE, RollUtil.existingOrNew());
		steading.addOther(OtherTag.TRADE, RollUtil.existingOrNew());
		RollUtil.randomElement(MISC).accept(steading);
		RollUtil.randomElement(PROBLEMS).accept(steading);
	}

	// ==========================================================================================
	// MISC

	private static void booming(final Steading steading) {
		steading.setPopulation(Population.BOOMING);
		steading.addOther(OtherTag.LAWLESS, null);
		steading.addDescriptor("booming");
	}

	private static void crossroads(final Steading steading) {
		Prosperity.alter(steading, -1);
		steading.addOther(OtherTag.MARKET, null);
		steading.addDescriptor("on crossroads");
	}

	private static void underProtection(final Steading steading) {
		Defense.alter(steading, 1);
		steading.addOther(OtherTag.OATH, RollUtil.existingOrNew());
		steading.addDescriptor("protected by OATH");
	}

	private static void church(final Steading steading) {
		steading.addOther(OtherTag.POWER, "divine");
		steading.addDescriptor("built around church");
	}

	private static void craft(final Steading steading) {
		steading.addOther(OtherTag.CRAFT, "choice");
		steading.addOther(OtherTag.RESOURCE, "based on craft");
		steading.addDescriptor("built around CRAFT");
	}

	private static void military(final Steading steading) {
		Defense.alter(steading, 1);
		steading.addDescriptor("built around military post");
	}

	// ==========================================================================================
	// PROBLEMS

	private static void tooBig(final Steading steading) {
		steading.addOther(OtherTag.TRADE, RollUtil.existingOrNew() + " (for NEED)");
		steading.addOther(OtherTag.NEED, "resource");
		steading.addDescriptor("too big for NEEDed resource");
	}

	private static void overProtection(final Steading steading) {
		Defense.alter(steading, -1);
		steading.addOther(OtherTag.OATH, RollUtil.existingOrNew());
		steading.addDescriptor("protects OATH");
	}

	private static void outlaw(final Steading steading) {
		steading.addOther(OtherTag.PERSONAGE, "outlaw");
		steading.addOther(OtherTag.ENMITY, RollUtil.existingOrNew() + " (where crimes committed)");
		steading.addDescriptor("houses outlaw");
	}

	private static void cornered(final Steading steading) {
		steading.addOther(OtherTag.EXOTIC, "good or service");
		steading.addOther(OtherTag.ENMITY, RollUtil.existingOrNew() + " (ambitious)");
		steading.addDescriptor("cornered market on EXOTIC");
	}

	private static void diseased(final Steading steading) {
		Population.alter(steading, -1);
		steading.addDescriptor("diseased");
	}

	private static void popular(final Steading steading) {
		Population.alter(steading, 1);
		steading.addOther(OtherTag.LAWLESS, null);
		steading.addDescriptor("popular meeting place");
	}
}
