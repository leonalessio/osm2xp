package com.osm2xp.classification.output;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import com.osm2xp.classification.annotations.Present;

public class CSVWriter<T> extends StringDelimitedWriter<T> {

	public CSVWriter(File file, String id) throws IOException {
		super(file, id);
	}

	public CSVWriter(File file, String id, String resultFieldName) throws IOException {
		super(file, id, resultFieldName);
	}

	@Override
	protected void writeHeader() {
		writer.println(buildString(analyzedFields));
	}

	protected String buildString(List<Field> fields) {
		return buildHeaderString(fields, 0) ;
	}
	protected String buildHeaderString(List<Field> fields, int idx) {
		StringBuilder builder = new StringBuilder();
		for (Field field : fields) {
			if (builder.length() > 0) {
				builder.append(',');
			}
			String name = field.getName();
			if (idx > 0) {
				name += idx;
			}
			builder.append(name);
			if (field.getAnnotation(Present.class) != null) {
				builder.append(',');
				builder.append(name+"_present");
			}
		}
		return builder.toString();
	}

}
