package com.me.gui.swing.logging;

import java.io.Serializable;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.springframework.context.ApplicationContext;

import com.me.gui.swing.ApplicationEntryPoint;
import com.me.gui.swing.views.GuiLaunchView;

@Plugin(name = "GuiAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class GuiAppender extends AbstractAppender {

	private static boolean ready;

	protected GuiAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout) {
		super(name, filter, layout, true, Property.EMPTY_ARRAY);
		ready = false;
	}

	@PluginFactory
	public static GuiAppender createAppender(@PluginAttribute("name") final String name, @PluginElement("Filter") final Filter filter,
			@PluginElement("Layout") final Layout<? extends Serializable> layout) {
		return new GuiAppender(name, filter, layout);
	}

	@Override
	public void append(final LogEvent event) {
		if(ready) {
			final String toAppend = new String(getLayout().toByteArray(event));
			final GuiLaunchView view = getView();
			view.appendToConsole(toAppend);
		}
	}

	public static void ready() {
		ready = true;
	}

	private GuiLaunchView view;

	private synchronized GuiLaunchView getView() {
		if(view == null) {
			final ApplicationContext springContext = ApplicationEntryPoint.getSpringContext();
			view = springContext.getBean(GuiLaunchView.class);
		}
		return view;
	}
}
