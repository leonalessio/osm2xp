package com.osm2xp.classification.output;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.osm2xp.classification.annotations.Ignore;
import com.osm2xp.classification.annotations.Result;

public class ARFFWriter<T> implements Closeable{
	
	private static final String doubleFormat = "%.2f";
	
	private final static Set<Class<?>> NUMBER_REFLECTED_PRIMITIVES;
	static {
	    Set<Class<?>> s = new HashSet<>();
	    s.add(byte.class);
	    s.add(short.class);
	    s.add(int.class);
	    s.add(long.class);
	    s.add(float.class);
	    s.add(double.class);
	    NUMBER_REFLECTED_PRIMITIVES = s;
	}

	private PrintWriter writer;
	
	private List<Field> numFields;
	private List<Field> boolFields;
	private List<Field> stringFields;
	private Field classField;
	
	public ARFFWriter(File file, String id) throws IOException {
		writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		writer.println("@RELATION " + id);
		writer.println();
	}
	
	public void write (T data) {
		if (numFields == null) {
			init(data.getClass());
		}
		StringBuilder builder = new StringBuilder();
		for (Field fld : numFields) {
			try {
				if (builder.length() > 0) {
					builder.append(',');
				}
				double value = fld.getDouble(data);
				builder.append(String.format(Locale.ROOT, doubleFormat, value));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (Field fld : boolFields) {
			try {
				if (builder.length() > 0) {
					builder.append(',');
				}
				boolean value = fld.getBoolean(data);
				builder.append(value?1:0);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (Field fld : stringFields) {
			try {
				if (builder.length() > 0) {
					builder.append(',');
				}
				Object value = fld.get(data);
				String res = "?";
				if (value != null) {
					res = "'" + value.toString() + "'";
				}
				builder.append(res);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			builder.append(',');
			Object value = classField.get(data);
			builder.append(value.toString());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println(builder.toString());
	}
	
	private void init(Class<? extends Object> clazz) {
		List<Field> allFields = new ArrayList<Field>();
		getAllFields(allFields, clazz);
		for (Field fld : allFields) {
			fld.setAccessible(true);
		}
		allFields = allFields.stream().filter(field -> !isIgnored(field)).collect(Collectors.toList());
		numFields = allFields.stream().filter(field -> isNumeric(field)).collect(Collectors.toList());
		boolFields = allFields.stream().filter(field -> isBool(field)).collect(Collectors.toList());
		Optional<Field> classFieldOp = allFields.stream().filter(field -> isResult(field)).findFirst();
		if (!classFieldOp.isPresent()) {
			throw new IllegalArgumentException("No resulting class field specified in type " + clazz.getSimpleName() + ". Use @Result annotation on Enum value");
		}
		classField = classFieldOp.get();
		if (!classField.getType().isEnum()) {
			throw new IllegalArgumentException("Resulting class field (" + clazz.getSimpleName() + ") should be an enum.");
		}
		stringFields = new ArrayList<>(allFields);
		stringFields.removeAll(numFields);
		stringFields.removeAll(boolFields);
		stringFields.remove(classField);
		
		for (Field numFld : numFields) {
			writer.println("@ATTRIBUTE " + numFld.getName() + " NUMERIC");
		}
		for (Field boolFld : boolFields) {
			writer.println("@ATTRIBUTE " + boolFld.getName() + " NUMERIC");
		}
		for (Field stringFld : stringFields) {
			writer.println("@ATTRIBUTE " + stringFld.getName() + " string");
		}
		
		Object[] enumConstants = classField.getType().getEnumConstants();
		String constList = Arrays.asList(enumConstants).stream().map(constant -> constant.toString()).collect(Collectors.joining(","));
		writer.println("@ATTRIBUTE class {" + constList + "}");
		writer.println("");
		writer.println("@DATA");
	}

	private boolean isIgnored(Field field) {
		return field.isAnnotationPresent(Ignore.class);
	}

	protected boolean isNumeric(Field field) {
		return isReflectedAsNumber(field.getType());
	}
	
	protected boolean isBool(Field field) {
		Class<?> type = field.getType();
		return Boolean.class.isAssignableFrom(type) || boolean.class.equals(type);
	}

	protected boolean isResult(Field field) {
		return field.isAnnotationPresent(Result.class);
	}
	
	protected  static boolean isReflectedAsNumber(Class<?> type) {
	    return Number.class.isAssignableFrom(type) || NUMBER_REFLECTED_PRIMITIVES.contains(type);
	}

	protected static List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    fields.addAll(Arrays.asList(type.getDeclaredFields()));

	    if (type.getSuperclass() != null) {
	        getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}


	@Override
	public void close() throws IOException {
		writer.close();
	}
	
	
	
}
