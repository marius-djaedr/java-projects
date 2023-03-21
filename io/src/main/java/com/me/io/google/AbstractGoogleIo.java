package com.me.io.google;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

public abstract class AbstractGoogleIo<S extends AbstractGoogleJsonClient> {
	private static final Logger logger = LoggerFactory.getLogger(GoogleDocsIo.class);
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final int CONNECT_TIMEOUT = 1 * 60 * 1000;//1 minute

	protected S service;

	@PostConstruct
	public void initialize() throws GeneralSecurityException, IOException {
		logger.info("Initializing");
		final GoogleIoSpecs annotation = this.getClass().getAnnotation(GoogleIoSpecs.class);

		final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		// Load client secrets.
		final InputStream in = AbstractGoogleIo.class.getResourceAsStream(annotation.credentialsFilePath());
		if(in == null) {
			throw new FileNotFoundException("Resource not found: " + annotation.credentialsFilePath());
		}
		final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		final GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
				Arrays.asList(annotation.scopes())).setDataStoreFactory(new FileDataStoreFactory(new File(annotation.tokensDirectoryFolder())))
						.setAccessType("offline").build();
		final LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		final Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
		final HttpRequestInitializer timeoutRequestInitializer = (request) -> {
			credential.initialize(request);
			request.setConnectTimeout(CONNECT_TIMEOUT);
			request.setReadTimeout(CONNECT_TIMEOUT);
		};

		service = build(httpTransport, JSON_FACTORY, timeoutRequestInitializer);
		logger.info("Initialized");
	}

	protected abstract S build(HttpTransport transport, JsonFactory jsonFactory, HttpRequestInitializer httpRequestInitializer);
}
