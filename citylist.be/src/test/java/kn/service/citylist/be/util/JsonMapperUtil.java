package kn.service.citylist.be.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class JsonMapperUtil {

	private static final Logger LOGGER = LogManager.getLogger(JsonMapperUtil.class.getName());

	/**
	 * Converts JSON from the class path file to Object.
	 *
	 * @param          <T> the generic type
	 * @param fileName the file name
	 * @param clazz    the clazz
	 * @return the t
	 */
	public static <T> T convertJsonToObject(String fileName, Class<T> clazz) {
		Resource resource = new ClassPathResource(fileName);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(resource.getInputStream(), clazz);
		} catch (IOException e) {
			LOGGER.error("{}", e.getMessage());
		}
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException
				| IllegalAccessException e) {
			LOGGER.error("{}", e.getMessage());
		}
		return null;
	}

	public static <T> List<T> convertJsonToObject(String fileName, TypeReference<List<T>> clazz) {
		Resource resource = new ClassPathResource(fileName);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(resource.getInputStream(), clazz);
		} catch (IOException e) {
			LOGGER.error("{}", e.getMessage());
		}

		return new ArrayList<>();
	}

	public static <T> T convertParameterizedTypeReferenceJsonToObject(String fileName, TypeReference<T> clazz) {
		Resource resource = new ClassPathResource(fileName);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(resource.getInputStream(), clazz);
		} catch (IOException e) {
			LOGGER.error("{}", e.getMessage());
		}

		return null;
	}

	public static <T> T convertStringToObject(String jsonString, TypeReference<T> typeRef) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(jsonString, typeRef);
		} catch (IOException e) {
			LOGGER.error("{}", e.getMessage());
		}
		return null;
	}

	public static <T> T convertJsonStringToObject(String jsonString, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			LOGGER.error("{}", e.getMessage());
		}
		return null;
	}

}
