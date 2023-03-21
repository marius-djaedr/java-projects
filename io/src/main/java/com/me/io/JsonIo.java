package com.me.io;

import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.me.io.JsonIo.JsonParams;

public class JsonIo extends AbstractIo<JsonParams> {

	private final Gson gson = new Gson();

	@Override
	protected <T> T readOne(final Class<T> objectClass, final Reader reader, final JsonParams params) {
		return gson.fromJson(reader, objectClass);
	}

	@Override
	protected <T> List<T> readList(final Class<T> objectClass, final Reader reader, final JsonParams params) {
		return Arrays.asList(readOne(objectClass, reader, params));
	}

	@Override
	protected <T> void writeOne(final T object, final Writer writer, final JsonParams params) {
		gson.toJson(object, writer);
	}

	@Override
	protected <T> void writeList(final List<T> object, final Writer writer, final JsonParams params) {
		gson.toJson(object, writer);
	}

	public static class JsonParams implements AdditionalParams {
		private JsonParams() {}
	}

}
