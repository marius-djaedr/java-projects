package com.me.io.google;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.BatchUpdateDocumentRequest;
import com.google.api.services.docs.v1.model.BatchUpdateDocumentResponse;
import com.google.api.services.docs.v1.model.Document;
import com.google.api.services.docs.v1.model.InsertTextRequest;
import com.google.api.services.docs.v1.model.Location;
import com.google.api.services.docs.v1.model.Request;
import com.google.api.services.docs.v1.model.StructuralElement;
import com.google.api.services.docs.v1.model.WriteControl;

@GoogleIoSpecs(credentialsFilePath = "/credentials-docs.json", scopes = DocsScopes.DOCUMENTS, tokensDirectoryFolder = "tokens-docs")
public class GoogleDocsIo extends AbstractGoogleIo<Docs> {
	private static final Logger logger = LoggerFactory.getLogger(GoogleDocsIo.class);

	@Override
	protected Docs build(final HttpTransport transport, final JsonFactory jsonFactory, final HttpRequestInitializer httpRequestInitializer) {
		return new Docs.Builder(transport, jsonFactory, httpRequestInitializer).setApplicationName("Google Docs API Java Quickstart").build();
	}

	public void appendToSheet(final String toWrite, final String docId) throws IOException {
		writeControl(docId, d -> appendToSheetFunction(toWrite, docId, d));
	}

	private List<Request> appendToSheetFunction(final String toWrite, final String docId, final Document document) {
		final List<StructuralElement> bodyElements = document.getBody().getContent();
		final Integer index = bodyElements.get(bodyElements.size() - 1).getEndIndex() - 1;

		return Arrays.asList(new Request().setInsertText(new InsertTextRequest().setText(toWrite).setLocation(new Location().setIndex(index))));
	}

	private synchronized void writeControl(final String docId, final Function<Document, List<Request>> requestFunction) throws IOException {
		logger.info("Writing");
		final String revisionId = service.documents().get(docId).execute().getRevisionId();
		final Document document = service.documents().get(docId).execute();

		final BatchUpdateDocumentRequest content = new BatchUpdateDocumentRequest().setRequests(requestFunction.apply(document))
				.setWriteControl(new WriteControl().setRequiredRevisionId(revisionId));
		final BatchUpdateDocumentResponse response = service.documents().batchUpdate(docId, content).execute();
		// TODO error handling if response comes back with bad revision
		logger.info("Written");
	}

}
