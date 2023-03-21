package com.me.io;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

import com.me.io.CsvIo.CsvParams;
import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class CsvIo extends AbstractIo<CsvParams> {

	@Override
	protected <T> T readOne(final Class<T> objectClass, final Reader reader, final CsvParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> List<T> readList(final Class<T> objectClass, final Reader reader, final CsvParams params) {
		final CsvToBeanBuilder<T> builder = new CsvToBeanBuilder<T>(reader).withType(objectClass);
		if(params != null) {
			builder.withVerifier(params.verifier).withSkipLines(params.linesToStrip);
		}
		return builder.build().parse();
	}

	@Override
	protected <T> void writeOne(final T object, final Writer writer, final CsvParams params) throws Exception {
		new StatefulBeanToCsvBuilder<T>(writer).build().write(object);
	}

	@Override
	protected <T> void writeList(final List<T> object, final Writer writer, final CsvParams params) throws Exception {
		new StatefulBeanToCsvBuilder<T>(writer).build().write(object);
	}

	@SuppressWarnings("rawtypes")
	public static class CsvParams implements AdditionalParams {
		private final int linesToStrip;
		private final BeanVerifier verifier;

		public CsvParams(final int linesToStrip, final BeanVerifier verifier) {
			this.linesToStrip = linesToStrip;
			this.verifier = verifier;
		}
	}

}
