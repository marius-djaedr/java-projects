package com.me.ttrpg.dungeonworld.gui;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.me.io.google.GoogleDocsIo;
import com.me.ttrpg.dungeonworld.generator.DungeonWorldGenerator;
import com.me.ttrpg.dungeonworld.generator.npc.AbstractNpcGenerator;
import com.me.ttrpg.dungeonworld.generator.steading.AbstractSteadingGenerator;
import com.me.util.utils.GuiUtils;
import com.me.util.utils.RollUtil;

@Component
public class GuiLaunchView extends JFrame {
	private static final Logger logger = LoggerFactory.getLogger(GuiLaunchView.class);

	private static final long serialVersionUID = -3633677732075626243L;
	private static final String DOCUMENT_ID = "1wl0olt_oer7ZNny6bY_ZYUS2VhPE_vBHmWYgkKyYnyo";

	public GuiLaunchView() {
		super();
		this.setTitle("Steading Generator");
		this.setSize(1000, 150);

		GuiUtils.centerFrameInWindow(this);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Autowired
	private ApplicationContext context;
	@Autowired
	private GoogleDocsIo googleDocsIo;

	@PostConstruct
	public void initialize() {
		final Collection<AbstractSteadingGenerator> steadingGenerators = context.getBeansOfType(AbstractSteadingGenerator.class).values();
		final Collection<AbstractNpcGenerator> npcGenerators = context.getBeansOfType(AbstractNpcGenerator.class).values();

		final JPanel panel = new JPanel();

		String buttonText = "Any Steading";
		JButton button = new JButton(buttonText);
		button.addActionListener(e -> run(RollUtil.randomElement(steadingGenerators)));
		panel.add(button);

		for(final AbstractSteadingGenerator generator : steadingGenerators) {
			buttonText = generator.getClass().getSimpleName();
			button = new JButton(buttonText);
			button.addActionListener(e -> run(generator));
			panel.add(button);
		}

		for(final AbstractNpcGenerator generator : npcGenerators) {
			buttonText = generator.getClass().getSimpleName();
			button = new JButton(buttonText);
			button.addActionListener(e -> run(generator));
			panel.add(button);
		}
		this.add(panel);
	}

	private <T> void run(final DungeonWorldGenerator<T> generator) {
		final T t = generator.generate();
		logger.info("\n" + t);
		try {
			googleDocsIo.appendToSheet("\n\n" + t.toString(), DOCUMENT_ID);
		} catch(final IOException e) {
			logger.error("Unable to write to google doc", e);
		}
	}

}
