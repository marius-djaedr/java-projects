package com.me.gui.swing.views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Collection;
import java.util.Stack;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.me.gui.swing.utils.GuiUtils;
import com.me.gui.swing.views.crud.AbstractCrudFrame;

@Component
public class GuiLaunchView extends JFrame {
	private static final Logger logger = LoggerFactory.getLogger(GuiLaunchView.class);

	private static final long serialVersionUID = -3633677732075626243L;

	@Autowired
	private ApplicationContext context;
	@Autowired(required = false)
	private ActionStateProvider stateProvider;

	private JTextArea console;
	private JScrollPane consoleScroll;
	private JButton undoButton, redoButton;

	private final Stack<Object> redoStates = new Stack<>();
	private final Stack<Object> undoStates = new Stack<>();
	private Object currentState;

	public GuiLaunchView(@Autowired final String title) {
		super();
		this.setTitle(title);
		this.setSize(1700, 500);

		final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		final int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@PostConstruct
	public void initialize() {
		final Collection<GuiFrame> frameList = context.getBeansOfType(GuiFrame.class).values();
		this.add(buildPanel(frameList));
	}

	public void appendToConsole(final String toAppend) {
		console.append(toAppend);
		consoleScroll.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
	}

	private JPanel buildPanel(final Collection<GuiFrame> frameList) {
		final GridBagConstraints c = new GridBagConstraints();
		final JPanel panel = GuiUtils.commonBuildPanel(c);

		if(stateProvider != null) {
			c.weighty = 0.0;
			panel.add(buildTop(), c);
		}

		c.weighty = 1.0;
		c.gridy++;
		panel.add(buildTabs(frameList), c);

		c.gridy++;
		panel.add(buildConsole(), c);

		return panel;
	}

	private java.awt.Component buildTop() {
		//		final GridBagConstraints c = new GridBagConstraints();
		//		final JPanel panel = GuiUtils.commonBuildPanel(c);

		final JPanel panel = new JPanel(new GridLayout(1, 0));

		undoButton = new JButton("UNDO");
		undoButton.addActionListener(e -> undo());
		undoButton.setEnabled(false);

		redoButton = new JButton("REDO");
		redoButton.addActionListener(e -> redo());
		redoButton.setEnabled(false);

		panel.add(undoButton);
		panel.add(redoButton);

		return panel;
	}

	private JPanel buildTabs(final Collection<GuiFrame> frameList) {
		final GridBagConstraints c = new GridBagConstraints();
		final JPanel panel = GuiUtils.commonBuildPanel(c);

		final JTabbedPane tabbedPane = new JTabbedPane();
		frameList.stream().sorted((f1, f2) -> Double.compare(f1.order(), f2.order()))
				.forEachOrdered(f -> tabbedPane.addTab(f.tabName(), f.buildPanel()));
		panel.add(tabbedPane, c);
		return panel;
	}

	private JPanel buildConsole() {
		final GridBagConstraints c = new GridBagConstraints();
		final JPanel panel = GuiUtils.commonBuildPanel(c);

		console = new JTextArea();
		console.setEditable(false);
		consoleScroll = new JScrollPane(console);
		panel.add(consoleScroll, c);
		return panel;
	}

	public void initializeActionState() {
		if(stateProvider != null) {
			this.currentState = stateProvider.getState();
		} else {
			logger.warn("No state provider specified");
		}
	}

	public void actionDone() {
		logger.info("action");
		if(stateProvider != null) {
			undoStates.push(currentState);
			this.currentState = stateProvider.getState();
			redoStates.clear();
			undoButton.setEnabled(true);
			redoButton.setEnabled(false);
		}
		refreshData();
	}

	public void undo() {
		logger.info("undo");
		redoStates.push(currentState);
		currentState = undoStates.pop();
		stateProvider.setState(currentState);
		undoButton.setEnabled(undoStates.size() > 0);
		redoButton.setEnabled(true);
		refreshData();
	}

	public void redo() {
		logger.info("redo");
		undoStates.push(currentState);
		currentState = redoStates.pop();
		stateProvider.setState(currentState);
		undoButton.setEnabled(true);
		redoButton.setEnabled(redoStates.size() > 0);
		refreshData();
	}

	private void refreshData() {
		context.getBeansOfType(AbstractCrudFrame.class).values().forEach(AbstractCrudFrame::setData);
	}

	public static interface ActionStateProvider<T> {
		T getState();

		void setState(T state);
	}

}
