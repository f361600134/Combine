
package com.fatiny.combine.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class ReflectTest {
	public static void main(String[] args) throws Exception {
		// 反射List 集合
		reflectCollection();
	}

	/**
	 * 反射获取集合属性 和 集合中的 泛型
	 * 
	 * @throws Exception
	 */
	public static void reflectCollection() throws Exception {
		List<Integer> nums = new ArrayList<Integer>();
		nums.add(1);

		Person p = new Person();
		p.setAge(18);
		p.setName("luck");

		List<Person> list = new LinkedList<Person>();
		list.add(p);

		List<Address> address = new ArrayList<Address>();
		Address addr = new Address();
		addr.setAddName("上海人民广场");
		addr.setAddNo("add001");
		address.add(addr);

		p.setAddress(address);

		TestA a = new TestA();
		a.setNums(nums);
		a.setP(p);
		a.setAge(16);
		a.setBig(55);
		a.setData(list);

		TestB b = new TestB();
		copyProperties(a, b);
		// BeanUtils.copyProperties(b,a); //只能copy 类型相同的,
		System.out.println("基本类型:" + b.getBig());
		System.out.println("包装类型:" + b.getAge());
		System.out.println("自定义类" + b.getP().getAge());
		System.out.println("List<包装类型>:" + b.getNums().get(0));
		System.out.println("List<自定义类>:" + b.getData().get(0).getAge());
		System.out.println("List<自定义类.List>:" + b.getData().get(0).getAddress().get(0).getAddName());

	}

	/**
	 * 能 复制 相同类型的属性 也能复制 "属性名相同,类型不同" 的属性
	 * 
	 * @param src
	 *            原对象
	 * @param dest
	 *            目标对象
	 * @throws Exception
	 *             Person ==> People 注意:本程序省略 NoSuchMethodException 的判断 每获取
	 *             Method 都需要判断 NoSuchMethodException 后在 invoke
	 */
	public static void copyProperties(Object src, Object dest) {
		Class sclazz = src.getClass();
		Class dclazz = dest.getClass();

		// Object.class 为 stopClass 不反射的 Class
		PropertyDescriptor[] ps = new PropertyDescriptor[0];
		try {
			ps = Introspector.getBeanInfo(dclazz, Object.class).getPropertyDescriptors();
			for (PropertyDescriptor prop : ps) {
				Object srcVal = null;
				try {

					if (prop.getPropertyType() == Class.class) { // Class 不反射
						continue;
					} else if (prop.getPropertyType().isPrimitive()) { // 基本数据类型
						srcVal = getValue(src, sclazz, prop.getReadMethod().getName());
						if (srcVal != null) {
							prop.getWriteMethod().invoke(dest, srcVal);
						}
					} else if (isWarpType(prop.getPropertyType().getName())) { // 包装类型
						srcVal = getValue(src, sclazz, prop.getReadMethod().getName());
						if (srcVal != null) {
							if (prop.getPropertyType().getName().equals("java.lang.Double")) { // Double
								BigDecimal newVal = new BigDecimal(srcVal.toString());
								newVal.setScale(2);
								prop.getWriteMethod().invoke(dest, newVal);
							} else {
								prop.getWriteMethod().invoke(dest, srcVal);
							}
						}
					} else if (prop.getPropertyType().getInterfaces() != null && prop.getPropertyType().getInterfaces().length > 0 && prop.getPropertyType().getInterfaces()[0] == Collection.class) { // 集合类型
						Object obj = getValue(src, sclazz, prop.getReadMethod().getName());
						if (obj != null && obj instanceof Collection) {
							Collection srcList = (Collection) obj;
							Class collClazz = obj.getClass();
							ParameterizedType pt = (ParameterizedType) prop.getReadMethod().getGenericReturnType();
							Class type = (Class) pt.getActualTypeArguments()[0];
							Object destList = collClazz.newInstance();
							Method addMethod = collClazz.getMethod("add", Object.class);
							if (addMethod != null) {
								for (Object object : srcList) {
									Object item = null;
									if (isWarpType(type.getName())) {
										Class parmClazz = String.class;
										try {
											parmClazz = (Class) object.getClass().getField("TYPE").get(object);
										} catch (Exception e) {
										}
										item = type.getConstructor(parmClazz).newInstance(object);
									} else {
										item = type.newInstance();
										copyProperties(object, item);
									}
									addMethod.invoke(destList, item);
								}
							}
							prop.getWriteMethod().invoke(dest, destList);
						}
					} else {// 自定义类型 Class
						Object d = prop.getPropertyType().newInstance();
						copyProperties(getValue(src, sclazz, prop.getReadMethod().getName()), d);
						prop.getWriteMethod().invoke(dest, d);
					}
				} catch (Exception e) {
					System.out.println("copyProperties ERROR:" + e);
				}
			}
		} catch (Exception e) {
			System.out.println("copyProperties ERROR:" + e);
		}
	}

	/**
	 * 获取属性值
	 * 
	 * @param obj
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	public static Object getValue(Object obj, Class clazz, String methodName) {
		Object value = null;
		try {
			Method method = clazz.getMethod(methodName);
			if (method != null) {
				value = method.invoke(obj);
			}
		} catch (Exception e) {
			System.out.println("copyProperties.getValue method: " + methodName + ",ERROR" + e);
		}
		return value;
	}

	/**
	 * 是否是包装类型
	 * 
	 * @param typeName
	 * @return
	 */
	public static boolean isWarpType(String typeName) {
		String[] types = { "java.lang.Integer", "java.lang.Double", "java.lang.Float", "java.lang.Long", "java.lang.Short", "java.lang.Byte", "java.lang.Boolean", "java.lang.Character", "java.lang.String" };
		return ArrayUtils.contains(types, typeName);
	}

}
