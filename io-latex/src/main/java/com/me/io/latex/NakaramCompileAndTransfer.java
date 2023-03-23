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

public class NakaramCompileAndTransfer {
	private static final Logger logger = LoggerFactory.getLogger(NakaramCompileAndTransfer.class);

	private static final File MAIN_DIR = new File("src/main/resources/library/nakaram_newspaper");
	private static final List<String> TO_COMPILE = new ArrayList<>(Arrays.asList("iss-02-09.tex"));

	private static final List<String> DRIVE_DIR = Arrays.asList("TTRPG", "Ttrpg notes", "nakaram", "newspaper");

	public static void main(final String[] args) throws IOException, GeneralSecurityException {
		try {
			final File imagesDir = new File(MAIN_DIR, "images");
			final List<File> supportingFiles = new ArrayList<>(Arrays.asList(imagesDir.listFiles()));
			supportingFiles.add(new File(MAIN_DIR, "newspaper-me.sty"));

			final Map<File, List<File>> fileMap = new HashMap<>();
			fileMap.put(new File(MAIN_DIR, TO_COMPILE.remove(0)), supportingFiles);
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
