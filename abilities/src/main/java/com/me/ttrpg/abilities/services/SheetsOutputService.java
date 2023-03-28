package com.me.ttrpg.abilities.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.me.io.google.GoogleSheetsIo;
import com.me.ttrpg.abilities.dto.ArrayOutputBlock;
import com.me.ttrpg.abilities.dto.ArrayOutputRow;

@Service
public class SheetsOutputService implements OutputService {
	private static final Logger logger = LoggerFactory.getLogger(SheetsOutputService.class);
	private static final String SHEET_ID = "1aQ0Oxf9i85LDhjHonFjvJAypA2n0SuxUC6pIGee4GHM";
	private static final List<String> HEADERS = ArrayOutputRow.getHeaderFields();
	private static final int ROW_LENGTH = HEADERS.size() + 5;

	@Autowired
	private GoogleSheetsIo sheetsIo;

	@Override
	public void writeOutput(final Map<String, List<ArrayOutputBlock>> outputMap) {
		for(final String sheet : outputMap.keySet()) {
			logger.info("Writing sheet {}", sheet);
			final List<List<String>> values = buildValues(outputMap.get(sheet));
			try {
				//				sheetsIo.addTabToSheet(SHEET_ID, sheet);
				sheetsIo.writeToSheet(values, SHEET_ID, "'" + sheet + "'!A1");
			} catch(final IOException e) {
				logger.error("Could not write to sheet {}", sheet, e);
			}
		}
	}

	private List<List<String>> buildValues(final List<ArrayOutputBlock> list) {
		final List<List<String>> values = new ArrayList<>();
		for(final ArrayOutputBlock block : list) {
			values.add(hfill(Arrays.asList(block.getBlockName())));
			values.add(hfill(HEADERS));
			for(final ArrayOutputRow row : block.getRows()) {
				values.add(hfill(row.getRowFields()));
			}
			values.add(hfill(Arrays.asList("")));
		}
		for(int i = 0 ; i < 5 ; i++) {
			values.add(hfill(Arrays.asList("")));
		}
		return values;
	}

	private List<String> hfill(final List<String> oldList) {
		final List<String> list = new ArrayList<>(oldList);
		for(int i = list.size() ; i < ROW_LENGTH ; i++) {
			list.add("");
		}
		return list;
	}

}
