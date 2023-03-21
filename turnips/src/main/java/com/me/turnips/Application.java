package com.me.turnips;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.me.turnips.spring.SpringConfig;

public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(final String[] args) {
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class)) {
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

}
