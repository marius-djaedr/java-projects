package com.me.gui.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationEntryPoint {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationEntryPoint.class);

	private static ApplicationContext context;

	public static void execute(final Class<?> springConfigClass, final String[] args) throws Throwable {
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(springConfigClass)) {
			ApplicationEntryPoint.context = ctx;
			ctx.getBean(LaunchWrapper.class).launch();
		} catch(final Throwable th) {
			logger.error("FATAL ERROR", th);
			throw th;
		} finally {
			// Hard stop the java run instance, as is done when Eclipse runs tests. See
			// https://www.cct.lsu.edu/~rguidry/ecl31docs/api/src-html/org/eclipse/jdt/internal/junit/runner/RemoteTestRunner.html#line.193
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=14434
			System.exit(0);
		}
	}

	public static ApplicationContext getSpringContext() {
		return context;
	}
}
