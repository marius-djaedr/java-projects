package com.me.gui.swing;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.me.gui.swing.logging.GuiAppender;
import com.me.gui.swing.views.GuiLaunchView;

@Component
public class LaunchWrapper {
	private static final Logger logger = LoggerFactory.getLogger(LaunchWrapper.class);

	@Autowired
	private GuiLaunchView guiLaunchMenu;

	private final Set<Thread> altRunning = new HashSet<>();

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		guiLaunchMenu.setVisible(true);
	}

	public void launch() throws InterruptedException {
		guiLaunchMenu.initializeActionState();
		GuiAppender.ready();
		logger.info("Hello there!");
		while(guiLaunchMenu.isDisplayable()) {
			Thread.sleep(10000);
			//TODO something every 10 seconds
			logger.info("Still alive");
		}

		if(!altRunning.isEmpty()) {
			logger.info("Beginning exit, checking for alt threads");

			while(!altRunning.isEmpty()) {
				try {
					Thread.sleep(1000);
				} catch(final InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				altRunning.removeIf(t -> !t.isAlive());
				logger.info("Waiting on {} threads", altRunning.size());
			}
		}
	}

	public void executeOnNewThread(final Runnable toRun) {
		final Thread thread = new Thread(toRun);
		altRunning.add(thread);
		thread.start();
	}
}
