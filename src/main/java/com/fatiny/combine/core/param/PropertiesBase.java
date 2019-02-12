package com.fatiny.combine.core.param;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * 读取配置文件并且解析, 以键值对的方式缓存到临时map
 * 
 * @auth Jeremy
 * @date 2019年2月12日下午5:06:53
 */
public class PropertiesBase {

	public static final Logger logger = LoggerFactory.getLogger(PropertiesBase.class);

	private static final String configPath = "config/config.properties";
	// public static Map<String, String> propertiesMap = new
	// ConcurrentHashMap<String, String>(20);
	private static Properties properties;
	private static Multimap<String, String> prefixMap = ArrayListMultimap.create();

	public static void main(String[] args) {
		properties();
	}

	/**
	 * 解析配置表, 并且以键值对的方式缓存到map中
	 * 
	 * @return void
	 * @date 2019年2月12日下午5:10:22
	 */
	public static void properties() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(configPath));
			for (Object key : properties.keySet()) {
				String k = key.toString();
				String prefix = k.substring(0, k.lastIndexOf("."));
				prefixMap.put(prefix, properties.getProperty(k));
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		logger.info("prefixMap:" + prefixMap);
	}

	public static String getValue(String key) {
		return properties.get(key).toString();
	}

	public static Collection<String> getValueByPrefix(String key) {
		return prefixMap.get(key);
	}

	public static boolean containsByPrefix(String key) {
		return prefixMap.containsKey(key);
	}

}
