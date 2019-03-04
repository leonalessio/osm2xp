package com.osm2xp.classification.output;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CSVWithAdditionalsWriter<T> extends CSVWriter<T> {

	private int additionalsCount;

	public CSVWithAdditionalsWriter(File file, String id, int additionalsCount) throws IOException {
		super(file, id);
		this.additionalsCount = additionalsCount;
	}
	
	public void write(T data, List<T> additionals) {
		if (numFields == null) {
			init(data.getClass());
		}
		while (additionals.size() < additionalsCount) {
			additionals.add(null);
		}
		if (additionals.size() > additionalsCount) {
			additionals = additionals.subList(0, additionalsCount);
//			throw new IllegalArgumentException("Max " + additionalsCount + " additional items allowed");
		}
		String str = super.getString(data);
		int idx = str.lastIndexOf(',');
		String start = str.substring(0,idx);
		String target = str.substring(idx + 1);
		List<String> addList = new ArrayList<String>();
		for (T current : additionals) {
			if (current != null) {
				addList.add(getString(current));
			} else {
				addList.add(analyzedFields.stream().map(fld -> "?").collect(Collectors.joining(",")));
			}
		}
		
		writer.println(start + "," + addList.stream().collect(Collectors.joining(",")) + "," + target);
	}
	
	@Override
	protected void writeHeader() {
		List<Field> basic = new ArrayList<Field>(analyzedFields);
		basic.remove(classField);
		StringBuilder builder = new StringBuilder(buildString(basic));
		for (int i = 1; i <= additionalsCount; i++) {
			builder.append(',');
			builder.append(buildHeaderString(analyzedFields,i));
		}
		builder.append(',');
		builder.append(classField.getName());
		writer.println(builder.toString());
	}

}
