package com.osm2xp.classification.output;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.osm2xp.classification.annotations.Present;

public class ARFFWriter<T> extends StringDelimitedWriter<T> {

	public ARFFWriter(File file, String id) throws IOException {
		super(file, id);
	}

	protected void writeHeader() {
		writer.println("@RELATION " + id);
		writer.println();
		for (Field numFld : numFields) {
			writer.println("@ATTRIBUTE " + numFld.getName() + " NUMERIC");
			if (numFld.getAnnotation(Present.class) != null) {
				writer.println("@ATTRIBUTE " + numFld.getName() + "_present" + " NUMERIC");	
			}
		}
		for (Field boolFld : boolFields) {
			writer.println("@ATTRIBUTE " + boolFld.getName() + " NUMERIC");
			if (boolFld.getAnnotation(Present.class) != null) {
				writer.println("@ATTRIBUTE " + boolFld.getName() + "_present" + " NUMERIC");	
			}
		}
		for (Field stringFld : stringFields) {
			writer.println("@ATTRIBUTE " + stringFld.getName() + " string");
			if (stringFld.getAnnotation(Present.class) != null) {
				writer.println("@ATTRIBUTE " + stringFld.getName() + "_present" + " NUMERIC");	
			}
		}
		
		Object[] enumConstants = classField.getType().getEnumConstants();
		String constList = Arrays.asList(enumConstants).stream().map(constant -> constant.toString()).collect(Collectors.joining(","));
		writer.println("@ATTRIBUTE class {" + constList + "}");
		writer.println("");
		writer.println("@DATA");
	}

}
