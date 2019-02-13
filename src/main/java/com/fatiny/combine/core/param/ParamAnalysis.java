package com.fatiny.combine.core.param;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParamAnalysis {

	public static final Logger logger = LoggerFactory.getLogger(ParamAnalysis.class);

	/**
	 * 0. 解析配置文件<br>
	 * 1. 找出所有Params注解的Javabean <br>
	 * 2. 把注解内容添加到JavaBean中
	 * 
	 * @return void
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @date 2019年2月12日下午2:43:14
	 */
	public static void analysis() throws Exception {
		Set<Class<?>> classes = PackageBase.getAllClassByPackage("com.fatiny.combine.business");
		for (Class<?> clazz : classes) {
			Params params = clazz.getAnnotation(Params.class);
			if (params == null) {
				continue;
			}
			// 判断前缀是否相同
			if (!PropertiesBase.containsByPrefix(params.prefix())) {
				continue;
			}
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers())) {
					String fullKey = params.prefix().concat(".").concat(field.getName());
					setValue(field, PropertiesBase.getValue(fullKey));
				}
			}
		}
	}

	/**
	 * 基础属性直接数据赋值
	 * 
	 * @param field
	 * @param value
	 * @return
	 * @throws Exception
	 * @return boolean
	 * @date 2019年2月13日下午6:22:16
	 */
	public static boolean setValue(Field field, Object value) throws Exception {
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		Class<?> clz = field.getType();
		field.setAccessible(true);
		modifiersField.setAccessible(true);
		modifiersField.set(field, field.getModifiers() & ~Modifier.FINAL);
		if (clz == Boolean.TYPE || clz == Boolean.class) {
			field.set(null, Boolean.valueOf(value.toString()));
		} else if (clz == Integer.TYPE || clz == Integer.class) {
			field.set(null, Integer.valueOf(value.toString()));
		} else if (clz == Long.TYPE || clz == Long.class) {
			field.set(null, Long.valueOf(value.toString()));
		} else if (clz == Byte.TYPE || clz == Byte.class) {
			field.set(null, Byte.valueOf(value.toString()));
		} else if (clz == Double.TYPE || clz == Double.class) {
			field.set(null, Double.valueOf(value.toString()));
		} else if (clz == Short.TYPE || clz == Short.class) {
			field.set(null, Short.valueOf(value.toString()));
		} else if (clz == Float.TYPE || clz == Float.class) {
			field.set(null, Float.valueOf(value.toString()));
		} else if (clz == String.class) {
			field.set(null, value);
		} else {
			logger.info("不支持的类型");
		}
		return false;
	}

	/**
	 * 判断是否为基础类型以及基础类型的封装类型
	 *
	 * @see java.lang.Boolean#TYPE
	 * @see java.lang.Character#TYPE
	 * @see java.lang.Byte#TYPE
	 * @see java.lang.Short#TYPE
	 * @see java.lang.Integer#TYPE
	 * @see java.lang.Long#TYPE
	 * @see java.lang.Float#TYPE
	 * @see java.lang.Double#TYPE
	 */
	public static boolean isPrimitive(Class<?> clz) {
		if ((clz == Boolean.TYPE || clz == Boolean.class)//
				|| (clz == Character.TYPE || clz == Character.class)//
				|| (clz == Integer.TYPE || clz == Integer.class)//
				|| (clz == Long.TYPE || clz == Long.class)//
				|| (clz == Byte.TYPE || clz == Byte.class)//
				|| (clz == Double.TYPE || clz == Double.class)//
				|| (clz == Short.TYPE || clz == Short.class) //
				|| (clz == Float.TYPE || clz == Float.class)//
				|| clz == String.class) {
			return true;
		}
		return false;
	}

}
