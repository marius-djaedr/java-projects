package com.me.util.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class FileUtils {
	public static void recursiveDelete(final File file) throws IOException {
		if(file.isDirectory()) {
			//delete all children
			for(final File child : file.listFiles()) {
				recursiveDelete(child);
			}
		}
		Files.deleteIfExists(file.toPath());
	}

	public static Pair<String, String> getFileNameAndExtension(final File file) {
		final String[] fileParts = file.getName().split("\\.");
		String fileName = "";
		for(int i = 0 ; i < fileParts.length - 1 ; i++) {
			fileName += fileParts[i];
		}
		return Pair.of(fileName, fileParts[fileParts.length - 1]);
	}

	/**
	 * gets the latest file, assuming file names end with timestamp
	 * 
	 * @param parentDir
	 * @return
	 */
	public static File getLatestFile(final File parentDir) {
		final List<File> latest = getLatestNFiles(parentDir, 1);
		if(latest.size() > 0) {
			return latest.get(0);
		}
		return null;
	}

	/**
	 * gets the latest N files, assuming file names end with timestamp
	 * 
	 * @param parentDir
	 * @return
	 */
	public static List<File> getLatestNFiles(final File parentDir, final int number) {
		parentDir.mkdirs();

		final List<File> files = new ArrayList<>(Arrays.asList(parentDir.listFiles()));
		files.removeIf(File::isDirectory);
		Collections.sort(files, Collections.reverseOrder());
		if(number < 0) {
			return new ArrayList<>(files);
		}
		return new ArrayList<>(files.subList(0, Math.min(files.size(), number)));
	}

	/**
	 * gets the latest file, assuming file names end with timestamp
	 * 
	 * @param parentDir
	 * @return
	 */
	public static File getLatestDirectory(final File parentDir) {
		final List<File> latest = getLatestNDirectories(parentDir, 1);
		if(latest.size() > 0) {
			return latest.get(0);
		}
		return null;
	}

	/**
	 * gets the latest N files, assuming file names end with timestamp
	 * 
	 * @param parentDir
	 * @return
	 */
	public static List<File> getLatestNDirectories(final File parentDir, final int number) {
		parentDir.mkdirs();

		final List<File> files = new ArrayList<>(Arrays.asList(parentDir.listFiles()));
		files.removeIf(f -> !f.isDirectory());
		Collections.sort(files, Collections.reverseOrder());
		if(number < 0) {
			return new ArrayList<>(files);
		}
		return new ArrayList<>(files.subList(0, Math.min(files.size(), number)));
	}
}
