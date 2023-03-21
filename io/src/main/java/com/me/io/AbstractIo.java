package com.me.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.me.io.exceptions.ReadException;
import com.me.io.exceptions.WriteException;
import com.me.io.functions.ExceptionConsumer;

public abstract class AbstractIo<P extends AdditionalParams> {
	private static final Logger logger = LoggerFactory.getLogger(AbstractIo.class);

	public <T> T readOneFromFile(final Class<T> objectClass, final File file) {
		return readOneFromFile(objectClass, file, null);
	}

	public <T> T readOneFromFile(final Class<T> objectClass, final File file, final P params) {
		return commonReadFromFile(r -> readOne(objectClass, r, params), file);
	}

	public <T> List<T> readListFromFile(final Class<T> objectClass, final File file) {
		return readListFromFile(objectClass, file, null);
	}

	public <T> List<T> readListFromFile(final Class<T> objectClass, final File file, final P params) {
		return commonReadFromFile(r -> readList(objectClass, r, params), file);
	}

	public <T> void writeOneToFile(final T object, final File file) {
		writeOneToFile(object, file, null);
	}

	public <T> void writeOneToFile(final T object, final File file, final P params) {
		commonWriteToFile(w -> writeOne(object, w, params), file);
	}

	public <T> void writeListToFile(final List<T> list, final File file) {
		writeListToFile(list, file, null);
	}

	public <T> void writeListToFile(final List<T> list, final File file, final P params) {
		commonWriteToFile(w -> writeList(list, w, params), file);
	}

	private <R> R commonReadFromFile(final Function<Reader, R> func, final File file) {
		if(file == null) {
			logger.warn("Null file, returning null");
			return null;
		}
		file.getParentFile().mkdirs();
		if(!file.exists()) {
			logger.warn("File [" + file.getAbsolutePath() + "] can't be found, returning null");
			return null;
		}
		try(FileReader reader = new FileReader(file)) {
			return func.apply(reader);
		} catch(final Exception e) {
			throw new ReadException("Unable to read from file " + file.getAbsolutePath(), e);
		}

	}

	private void commonWriteToFile(final ExceptionConsumer<Writer> cons, final File file) {
		if(file == null) {
			logger.warn("Null file");
			return;
		}
		file.getParentFile().mkdirs();
		file.delete();
		try(FileWriter writer = new FileWriter(file)) {
			cons.accept(writer);
		} catch(final Exception e) {
			throw new WriteException("Unable to write to file " + file.getAbsolutePath(), e);
		}
	}

	protected abstract <T> T readOne(final Class<T> objectClass, Reader reader, final P params);

	protected abstract <T> List<T> readList(final Class<T> objectClass, Reader reader, final P params);

	protected abstract <T> void writeOne(T object, Writer writer, final P params) throws Exception;

	protected abstract <T> void writeList(List<T> object, Writer writer, final P params) throws Exception;

}
