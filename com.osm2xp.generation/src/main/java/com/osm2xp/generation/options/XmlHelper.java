package com.osm2xp.generation.options;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;

/**
 * XmlHelper.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class XmlHelper {

	/**
	 * Saves settings bean to XML 
	 * @param <T> bean type
	 * @param file File to save to 
	 * @param bean Settings bean to save
	 * 
	 * @throws Osm2xpBusinessException
	 */
	@SuppressWarnings("unchecked")
	public static <T> void saveToXml(T bean, File file)
			throws Osm2xpBusinessException {
		try {
			file.getParentFile().mkdirs();
			JAXBContext jc = JAXBContext.newInstance(bean.getClass());
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			JAXBElement<T> jaxbElement = new JAXBElement<T>(new QName("", bean
					.getClass().getSimpleName()), (Class<T>) bean.getClass(), bean);
			marshaller.marshal(jaxbElement, file);
		} catch (JAXBException e) {
			throw new Osm2xpBusinessException(e.getMessage());
		}
	}

	/**
	 * @return
	 * @throws Osm2xpBusinessException
	 */
	public static Object loadFileFromXml(File file, Class<?> type)
			throws Osm2xpBusinessException {
		Object result = new Object();
		try {
			JAXBContext jc = JAXBContext.newInstance(type);
			Unmarshaller u = jc.createUnmarshaller();

			JAXBElement<?> root = u.unmarshal(new StreamSource(file), type);
			if (!root.getName().getLocalPart()
					.equalsIgnoreCase(type.getSimpleName())) {
				throw new Osm2xpBusinessException("File " + file.getName()
						+ " is not of type " + type.getSimpleName());
			}
			result = root.getValue();
		} catch (JAXBException e) {
			throw new Osm2xpBusinessException(e.getMessage(),e);
		}
		return result;
	}

}
