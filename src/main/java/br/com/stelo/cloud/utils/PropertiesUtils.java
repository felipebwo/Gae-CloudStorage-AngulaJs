package br.com.stelo.cloud.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * 
 * @author felipeb
 *
 */
public class PropertiesUtils {

	/**
	 * LOG.
	 */
	private static final Logger LOG = Logger.getLogger(PropertiesUtils.class.getName());

	private static Properties PROPERTIES = null;

	static {
		readBundle();
	}

	/**
	 * Retorna o valor do properties passando o key.
	 *
	 * @param key
	 *            as String
	 * @return String
	 */
	public static String getMessage(final String key) {
		return PROPERTIES.getProperty(key);
	}

	/**
	 * Leitura do properties.
	 */
	private static void readBundle() {
		if (PROPERTIES == null) {
			PROPERTIES = new Properties();
		}

		try {
			PROPERTIES.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("message.properties"));
		} catch (final FileNotFoundException e) {
			LOG.severe(e.getMessage());
		} catch (final IOException e) {
			LOG.severe(e.getMessage());
		}
	}
}
