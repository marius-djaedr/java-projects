package com.me.io.latex;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.me.io.google.GoogleDriveIo;

public class MiscCompileAndTransfer {
	private static final Logger logger = LoggerFactory.getLogger(MiscCompileAndTransfer.class);

	private static final File MAIN_DIR = new File("src/main/resources/library/misc");
	private static final List<String> TO_COMPILE = new ArrayList<>(Arrays.asList("questionnaire.tex"));

	private static final List<String> DRIVE_DIR = Arrays.asList("TTRPG", "Ttrpg notes");

	public static void main(final String[] args) throws IOException, GeneralSecurityException {
		try {
			final Map<File, List<File>> fileMap = new HashMap<>();
			for(final String toCompile : TO_COMPILE) {
				fileMap.put(new File(MAIN_DIR, toCompile), new ArrayList<>());
			}

			logger.info("Beginning build");
			final LatexCompileCommands latex = new LatexCompileCommands();
			final List<File> compiledFiles = latex.compile(fileMap);
			logger.info("Built files {}", compiledFiles);

			final GoogleDriveIo drive = new GoogleDriveIo();
			drive.initialize();
			drive.copyFiles(compiledFiles, DRIVE_DIR);
			logger.info("Job's done!");
		} catch(final Throwable t) {
			logger.error("Shit broke", t);
			throw t;
		}
	}
}
