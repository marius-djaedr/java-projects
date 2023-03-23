package com.me.io.latex;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.me.util.utils.FileUtils;

public class LatexCompileCommands {
	private static final Logger logger = LoggerFactory.getLogger(LatexCompileCommands.class);
	private static final String LATEX_COMMAND = "pdflatex -halt-on-error -file-line-error ";
//	private static final String LATEX_COMMAND = "D:\\other_games\\miktex\\miktex\\bin\\x64\\pdflatex -halt-on-error -file-line-error ";

	private static final File BUILD_DIR = new File("temp/build");

	public List<File> compile(final Map<File, List<File>> fileMap) throws IOException {
		logger.info("Clearing build dir {}", BUILD_DIR);
		if(BUILD_DIR.exists()) {
			FileUtils.recursiveDelete(BUILD_DIR);
		}
		BUILD_DIR.mkdirs();

		logger.info("Copying to build dir");
		final List<String> fileNameToCompile = new ArrayList<>();
		for(final Map.Entry<File, List<File>> entry : fileMap.entrySet()) {
			final File fileToCompile = entry.getKey();

			final File existingParent = fileToCompile.getParentFile();

			copyFile(fileToCompile, existingParent);
			for(final File file : entry.getValue()) {
				copyFile(file, existingParent);
			}
			final Pair<String, String> fileNameAndExtension = FileUtils.getFileNameAndExtension(fileToCompile);
			final String fileName = fileNameAndExtension.getLeft();

			fileNameToCompile.add(fileName);
		}

		logger.info("Running commands");
		final List<File> built = new ArrayList<>();
		for(final String fileName : fileNameToCompile) {
			final String command = LATEX_COMMAND + fileName;
			runCommandAndWait(command);
			runCommandAndWait(command);
			runCommandAndWait(command);
			built.add(new File(BUILD_DIR, fileName + ".pdf"));
		}
		return built;
	}

	/**
	 * 
	 * @param fileToCompile
	 * @return compiled PDF location
	 * @throws IOException 
	 */
	public File compile(final File fileToCompile, final List<File> supportingFiles) throws IOException {
		final Map<File, List<File>> fileMap = new HashMap<>();
		fileMap.put(fileToCompile, supportingFiles);
		final List<File> compiled = compile(fileMap);
		return compiled.get(0);
	}

	private void copyFile(final File file, final File masterParent) throws IOException {

		final File fileParent = file.getParentFile();
		File targetParent = BUILD_DIR;
		if(!fileParent.equals(masterParent)) {
			final List<String> pathToBuild = new ArrayList<>();
			File currentParent = fileParent;
			while(!currentParent.equals(masterParent)) {
				pathToBuild.add(currentParent.getName());
				currentParent = currentParent.getParentFile();
			}

			Collections.reverse(pathToBuild);
			for(final String dir : pathToBuild) {
				targetParent = new File(targetParent, dir);
			}
			targetParent.mkdirs();
		}

		final File targetFile = new File(targetParent, file.getName());

		logger.info("Copying {} to {}", file, targetFile);

		Files.copy(file.toPath(), targetFile.toPath());

	}

	private void runCommandAndWait(final String command) throws IOException {
		System.out.println(BUILD_DIR.getAbsolutePath());
		final Process p = Runtime.getRuntime().exec(command, null,BUILD_DIR.getAbsoluteFile());
		new Thread(() -> {
			try(BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
				String line = null;
				while((line = input.readLine()) != null) {
					logger.info(line);
				}
			} catch(final IOException e) {
				logger.error("Issue getting respone", e);
			}
		}).start();

		try {
			p.waitFor();
		} catch(final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
