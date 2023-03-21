package com.me.io.google;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Create;
import com.google.api.services.drive.Drive.Files.Delete;
import com.google.api.services.drive.DriveRequest;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

//https://developers.google.com/drive/api/v3/about-files
@GoogleIoSpecs(credentialsFilePath = "/credentials-drive.json", scopes = DriveScopes.DRIVE, tokensDirectoryFolder = "tokens-drive")
public class GoogleDriveIo extends AbstractGoogleIo<Drive> {
	private static final Logger logger = LoggerFactory.getLogger(GoogleDocsIo.class);
	private static final int BATCH_SIZE = 30;

	@Override
	protected Drive build(final HttpTransport transport, final JsonFactory jsonFactory, final HttpRequestInitializer httpRequestInitializer) {
		return new Drive.Builder(transport, jsonFactory, httpRequestInitializer).setApplicationName("Google Drive API Java Quickstart").build();
	}

	public void copyFiles(final List<java.io.File> files, final List<String> folderChain) throws IOException {
		logger.info("Finding directory for folder chain {}", String.valueOf(folderChain));
		final String folderId = drillIntoFolder(folderChain, false);

		final List<Create> list = new ArrayList<>();
		for(final java.io.File file : files) {
			deleteIfExists(folderId, file);
			list.add(addFileToQueue(file, folderId));
		}
		logger.info("Executing batch");
		batchExecute(list);
	}

	public void directoryReplace(final java.io.File parentDir, final List<String> folderChain) throws IOException {
		logger.info("Finding directory for folder chain {}", String.valueOf(folderChain));
		final String folderId = drillIntoFolder(folderChain, true);

		logger.info("Building batch");
		final List<Create> list = copyAllFiles(parentDir, folderId);
		logger.info("Executing batch");
		batchExecute(list);
	}

	private String drillIntoFolder(final List<String> folderChain, final boolean wipeIfExisting) throws IOException {
		String previousParent = null;
		String currentParent = "root";
		String finalName = null;
		boolean createdNew = false;

		for(final String link : folderChain) {
			previousParent = currentParent;
			final FileList result = service.files().list().setQ("mimeType = 'application/vnd.google-apps.folder' and name = '" + link + "' and '"
					+ currentParent + "' in parents and trashed = false").setSpaces("drive").setFields("files(id,name,parents)").execute();
			final List<File> foundFiles = result.getFiles();
			if(foundFiles.size() > 1) {
				throw new IllegalArgumentException("Multiple folders found with name " + link + " in parent " + currentParent);
			} else if(foundFiles.size() == 0) {
				currentParent = createFolder(currentParent, link);
				createdNew = true;
			} else {
				currentParent = foundFiles.get(0).getId();
			}
			finalName = link;
		}

		if(wipeIfExisting && !createdNew) {
			logger.info("Wiping out all existing in folder id [{}]", currentParent);
			service.files().delete(currentParent).execute();
			currentParent = createFolder(previousParent, finalName);
		}
		return currentParent;
	}

	private List<Create> copyAllFiles(final java.io.File parentDir, final String folderId) throws IOException {
		final java.io.File[] contents = parentDir.listFiles();
		final List<Create> list = new ArrayList<>();
		for(final java.io.File file : contents) {
			if(file.isDirectory()) {
				final String childFolder = createFolder(folderId, file.getName());
				list.addAll(copyAllFiles(file, childFolder));
			} else {
				list.add(addFileToQueue(file, folderId));
			}
		}
		return list;
	}

	private Create addFileToQueue(final java.io.File file, final String folderId) throws IOException {
		logger.info("Transferring file [{}] to folder id [{}]", file.getAbsolutePath(), folderId);

		final File fileMetadata = new File();
		fileMetadata.setName(file.getName());
		fileMetadata.setParents(Collections.singletonList(folderId));
		final FileContent content = new FileContent(getFileTypeFromFile(file), file);

		return service.files().create(fileMetadata, content).setFields("id, parents");
	}

	private String createFolder(final String parent, final String name) throws IOException {
		logger.info("Creating new folder [{}] in folder id [{}]", name, parent);

		final File newFolder = new File();
		newFolder.setName(name);
		newFolder.setMimeType("application/vnd.google-apps.folder");
		newFolder.setParents(Collections.singletonList(parent));

		final File created = service.files().create(newFolder).setFields("id").execute();
		return created.getId();
	}

	private String getFileTypeFromFile(final java.io.File file) throws IOException {
		return Files.probeContentType(file.toPath());
	}

	private void deleteIfExists(final String folderId, final java.io.File file) throws IOException {
		final FileList result = service.files().list().setQ("name = '" + file.getName() + "' and '" + folderId + "' in parents and trashed = false")
				.setSpaces("drive").setFields("files(id,name,parents)").execute();
		final List<Delete> deleteQueue = new ArrayList<>();
		for(final File found : result.getFiles()) {
			logger.info("Deleting found file {} ({})", found.getName(), found.getId());
			deleteQueue.add(service.files().delete(found.getId()));
		}

		batchExecute(deleteQueue);
	}

	private void batchExecute(final List<? extends DriveRequest<?>> list) {
		//TODO look into ExecutorService ex = Executors.newFixedThreadPool(4);
		final int numBatches = list.size() / BATCH_SIZE + 1;
		for(int batch = 0 ; batch < numBatches ; batch++) {
			logger.info("Batch {}", batch);
			final int minI = batch * BATCH_SIZE;
			final int maxI = Math.min(minI + BATCH_SIZE, list.size());
			final List<Thread> threads = new ArrayList<>();
			for(int i = minI ; i < maxI ; i++) {
				logger.info("index {} of {}", i, list.size());
				threads.add(spinUpThreadAndExecute(list.get(i)));
			}
			while(!threads.isEmpty()) {
				try {
					Thread.sleep(1000);
				} catch(final InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				threads.removeIf(t -> !t.isAlive());
				logger.info("Waiting on {} threads", threads.size());
			}
		}
	}

	private Thread spinUpThreadAndExecute(final DriveRequest<?> request) {

		final Thread thread = new Thread(() -> {
			try {
				request.execute();
			} catch(final IOException e) {
				logger.error("Something went wrong", e);
			}
		});
		thread.start();
		return thread;
	}

}
