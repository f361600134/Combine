package com.fatiny.combine.core.param;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

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
	/**
	 * 还是要先绑定类文件和配置文件, 这样不需要循环查询类文件. 获取到类就直接拿配置,
	 * 并且根据前缀+ParamAnalysis中multiMap的key组装成可以,最后获取到value key: fullName value:
	 * ParamField
	 */
	// public static Map<String, ParamField> paramFieldMap = Maps.newHashMap();

	public static Map<String, TreeMap<String, ParamField>> paramMap = Maps.newHashMap();

	public static void main(String[] args) {
		// properties();
		String k = "com.a.aa[0].b";
		// System.out.println(k.contains("\\["));
		int from = k.indexOf("[");
		int to = k.indexOf("]");
		String keyIndex = k.substring(from + 1, to);
		System.out.println("keyIndex:" + keyIndex);

		String field = k.substring(k.lastIndexOf(".") + 1, k.length());
		System.out.println("field:" + field);

		String temp = k.substring(0, to);
		String mainWord = temp.substring(temp.lastIndexOf(".") + 1, from);
		System.out.println("mainWord:" + mainWord);

		String temp1 = temp.substring(0, temp.lastIndexOf("."));
		System.out.println("prefix:" + temp1);

		TreeMap<String, Integer> map = new TreeMap<String, Integer>();
		map.put("a[3]", 3);
		map.put("a[0]", 0);
		map.put("a[5]", 5);
		System.out.println(map);
	}

	/**
	 * 解析配置表, 并且以键值对的方式缓存到map中
	 * 
	 * @return void
	 * @date 2019年2月12日下午5:10:22
	 */
	public static void init() {
		try {
			properties = new Properties();
			properties.load(new FileInputStream(configPath));
		} catch (Exception e) {
			logger.error("", e);
		}
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
				// 判断是否为集合
				ParamField paramField = split(k);
				try {
					TreeMap<String, ParamField> fieldMap = paramMap.get(paramField.getMainWord());
					if (fieldMap == null) {
						fieldMap = Maps.newTreeMap();
						fieldMap.put(paramField.getFullName(), paramField);
					} else {
						ParamField olderField = fieldMap.get(paramField.getFullName());
						if (olderField != null) {
							olderField.putFields(paramField.getField2value());
						} else {
							fieldMap.put(paramField.getFullName(), paramField);
						}
					}
					paramMap.put(paramField.getMainWord(), fieldMap);
				} catch (Exception e) {
					logger.error("paramField:{}", paramField);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		logger.info("paramMap:" + paramMap);
	}

	private static ParamField split(String k) {
		ParamField paramField;
		if (k.contains("[")) {
			int from = k.indexOf("[");
			int to = k.indexOf("]");
			// 解析出中括号内的内容, 作为key或index
			String keyIndex = k.substring(from + 1, to);
			// 解析出字段内容
			String field = (to == k.length() - 1) ? null : k.substring(k.lastIndexOf(".") + 1, k.length());
			// 解析出关键字
			String temp = k.substring(0, to);
			String mainWord = temp.substring(temp.lastIndexOf(".") + 1, from);
			// 解析出前缀
			String prefix = temp.substring(0, temp.lastIndexOf("."));
			// 解析出全名
			String fullName = k.substring(0, to + 1);
			// 解析出值
			String value = properties.getProperty(k);
			paramField = new ParamField(prefix, mainWord, keyIndex, fullName, field, value);
		} else {
			String mainWord = k.substring(k.lastIndexOf(".") + 1, k.length());
			// 解析出前缀
			String prefix = k.substring(0, k.lastIndexOf("."));
			String value = properties.getProperty(k);
			paramField = new ParamField(prefix, mainWord, value);
		}
		return paramField;
	}

	/**
	 * 解析配置表, 并且以键值对的方式缓存到map中
	 * 
	 * @return void
	 * @date 2019年2月12日下午5:10:22
	 */
	public static void properties2() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(configPath));
			for (Object key : properties.keySet()) {
				String k = key.toString();
				// 判断是否为集合
				if (k.contains("[")) {
					int from = k.indexOf("[") + 1;
					int to = k.indexOf("]");
					String keyIndex = k.substring(from, to);
					String value = properties.getProperty(k);
				}
				// String prefix = k.substring(0, k.lastIndexOf("."));
				// prefixMap.put(prefix, properties.getProperty(k));
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public static boolean containsKey(String key) {
		return paramMap.containsKey(key);
	}

	/**
	 * 根据mainWord获取到treemap
	 * 
	 * @param key
	 * @return
	 * @return ParamField
	 * @date 2019年3月3日上午12:40:23
	 */
	public static TreeMap<String, ParamField> getValue(String key) {
		return paramMap.get(key);
	}

}
