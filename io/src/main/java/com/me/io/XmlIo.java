package com.me.io;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

import org.simpleframework.xml.core.Persister;

import com.me.io.XmlIo.XmlParams;
import com.me.io.exceptions.ReadException;

public class XmlIo extends AbstractIo<XmlParams> {

	@Override
	protected <T> T readOne(final Class<T> objectClass, final Reader reader, final XmlParams params) {
		try {
			return new Persister().read(objectClass, reader, false);
		} catch(final Exception e) {
			throw new ReadException("Could not read xml", e);
		}
	}

	@Override
	protected <T> List<T> readList(final Class<T> objectClass, final Reader reader, final XmlParams params) {
		throw new UnsupportedOperationException("Read XML list not defined!");
	}

	@Override
	protected <T> void writeOne(final T object, final Writer writer, final XmlParams params) throws Exception {
		throw new UnsupportedOperationException("Write XML  not defined!");
	}

	@Override
	protected <T> void writeList(final List<T> object, final Writer writer, final XmlParams params) throws Exception {
		throw new UnsupportedOperationException("Write XML list not defined!");
	}

	public static class XmlParams implements AdditionalParams {
		private XmlParams() {}
	}
}
