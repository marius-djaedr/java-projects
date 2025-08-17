package com.me.ttrpg.dungeonworld.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.me.io.google.GoogleDocsIo;
import com.me.ttrpg.dungeonworld.generator.DungeonWorldGenerator;
import com.me.ttrpg.dungeonworld.generator.npc.AbstractNpcGenerator;
import com.me.ttrpg.dungeonworld.generator.steading.AbstractSteadingGenerator;
import com.me.ttrpg.dungeonworld.service.IConfigService;
import com.me.ttrpg.dungeonworld.service.IConfigService.ConfigSingleType;
import com.me.util.utils.GuiUtils;
import com.me.util.utils.RollUtil;

@Component
public class GuiLaunchView extends JFrame {
	private static final Logger logger = LoggerFactory.getLogger(GuiLaunchView.class);

	private static final long serialVersionUID = -3633677732075626243L;
	private static final String DOC_ID_PLACEHOLDER = "Please enter Doc ID";
	//	private static final String DOCUMENT_ID = "1wl0olt_oer7ZNny6bY_ZYUS2VhPE_vBHmWYgkKyYnyo";

	@Autowired
	private ApplicationContext context;
	@Autowired
	private IConfigService configService;
	@Autowired
	private GoogleDocsIo googleDocsIo;

	private JTextArea generated;
	private JTextField docId;

	public GuiLaunchView() {
		super();
		this.setTitle("Steading Generator");
		this.setSize(1500, 500);

		GuiUtils.centerFrameInWindow(this);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@PostConstruct
	public void initialize() {
		final Collection<AbstractSteadingGenerator> steadingGenerators = context.getBeansOfType(AbstractSteadingGenerator.class).values();
		final Collection<AbstractNpcGenerator> npcGenerators = context.getBeansOfType(AbstractNpcGenerator.class).values();

		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.;
		c.weighty = 1.;
		c.gridy = -1;

		final JPanel generatorButtonPanel = new JPanel();

		String buttonText = "Any Steading";
		JButton button = new JButton(buttonText);
		button.addActionListener(e -> generateAndShow(RollUtil.randomElement(steadingGenerators)));
		generatorButtonPanel.add(button);

		for(final AbstractSteadingGenerator generator : steadingGenerators) {
			buttonText = generator.getClass().getSimpleName();
			button = new JButton(buttonText);
			button.addActionListener(e -> generateAndShow(generator));
			generatorButtonPanel.add(button);
		}

		for(final AbstractNpcGenerator generator : npcGenerators) {
			buttonText = generator.getClass().getSimpleName();
			button = new JButton(buttonText);
			button.addActionListener(e -> generateAndShow(generator));
			generatorButtonPanel.add(button);
		}

		c.gridy++;
		mainPanel.add(generatorButtonPanel, c);

		final JPanel textPanel = new JPanel();
		this.generated = new JTextArea(20, 120);
		generated.setMaximumSize(getMaximumSize());

		textPanel.add(new JScrollPane(generated));
		c.gridy++;
		mainPanel.add(textPanel, c);

		final JButton writeDocButton = new JButton("Write To Doc");
		writeDocButton.addActionListener(e -> writeToDoc());
		c.gridy++;
		mainPanel.add(writeDocButton, c);

		final JPanel bottomButtons = new JPanel();

		String existingDocId = configService.getConfigSingle(ConfigSingleType.DOC_ID);
		existingDocId = existingDocId == null || existingDocId.isBlank() ? DOC_ID_PLACEHOLDER : existingDocId;
		docId = new JTextField(existingDocId, 80);
		bottomButtons.add(docId);

		final JButton saveDocIdButton = new JButton("Save Doc ID");
		saveDocIdButton.addActionListener(e -> saveDocId());
		bottomButtons.add(saveDocIdButton);

		final JButton reloadButton = new JButton("Reload Configs");
		reloadButton.addActionListener(e -> reloadConfigs());
		bottomButtons.add(reloadButton);

		c.gridy++;
		mainPanel.add(bottomButtons, c);
		this.add(mainPanel);
	}

	private <T> void generateAndShow(final DungeonWorldGenerator<T> generator) {
		final T t = generator.generate();
		logger.info("GENERATED: \n" + t);
		generated.setText(t.toString());
	}

	private void writeToDoc() {
		logger.info("Writing to doc");
		final String documentId = docId.getText();
		if(documentId == null || documentId.isBlank() || DOC_ID_PLACEHOLDER.equals(documentId)) {
			logger.error("No doc ID given");
			return;
		}

		try {
			googleDocsIo.appendToSheet("\n\n" + LocalDateTime.now().toString() + "\n" + generated.getText(), documentId);
		} catch(final IOException e) {
			logger.error("Unable to write to google doc", e);
		}
	}

	private void reloadConfigs() {
		logger.info("Reloading configs");
		configService.reloadConfigs();
	}

	private void saveDocId() {
		logger.info("Saving Doc ID");
		configService.updateSingleConfig(ConfigSingleType.DOC_ID, docId.getText());
	}
}
