package com.me.io.google;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

@GoogleIoSpecs(credentialsFilePath = "/credentials-sheets.json", scopes = SheetsScopes.SPREADSHEETS, tokensDirectoryFolder = "tokens-sheets")
public class GoogleSheetsIo extends AbstractGoogleIo<Sheets> {
	private static final Logger logger = LoggerFactory.getLogger(GoogleSheetsIo.class);

	@Override
	protected Sheets build(final HttpTransport transport, final JsonFactory jsonFactory, final HttpRequestInitializer httpRequestInitializer) {
		return new Sheets.Builder(transport, jsonFactory, httpRequestInitializer).setApplicationName("Google Sheets API Java Quickstart").build();
	}

	public void writeToSheet(final List<List<String>> values, final String sheetId, final String startingPosition) throws IOException {
		writeToSheet(values, sheetId, startingPosition, true);
	}

	public void writeToSheet(final List<List<String>> values, final String sheetId, final String startingPosition, final boolean raw)
			throws IOException {
		final List<List<Object>> valueObject = values.stream().map(l -> l.stream().map(Object.class::cast).collect(Collectors.toList()))
				.collect(Collectors.toList());

		final String inputOption = raw ? "RAW" : "USER_ENTERED";

		final UpdateValuesResponse result = service.spreadsheets().values().update(sheetId, startingPosition, new ValueRange().setValues(valueObject))
				.setValueInputOption(inputOption).execute();

	}

}
