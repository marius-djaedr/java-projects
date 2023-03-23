package com.me.ttrpg.dungeonworld;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.me.ttrpg.dungeonworld.gui.GuiLaunchView;

@Component
public class LaunchWrapper {
	private static final Logger logger = LoggerFactory.getLogger(LaunchWrapper.class);

	@Autowired
	private GuiLaunchView guiLaunchMenu;

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		guiLaunchMenu.setVisible(true);
	}

	public void launch() throws InterruptedException {
		while (guiLaunchMenu.isDisplayable()) {
			Thread.sleep(10000);
			logger.info("Still there?");// log to know still working
		}
	}

}
