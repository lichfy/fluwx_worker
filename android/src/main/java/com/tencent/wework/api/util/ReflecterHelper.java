package com.tencent.wework.api.util;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 类的字段方法获取工具类，为了提高效率，采用了状态机的工作方式
 * 
 * @author boyliang
 */
public final class ReflecterHelper {

	private static final String LOG_TAG = "ReflecterHelper";

	public static Class<?> mCurrentClass;

	/**
	 * 设置
	 * 
	 * @param name
	 *            类的完整路径
	 * @return 是否设置成功
	 */
	public final static boolean setClass(String name) {
		Class<?> tmpClass = null;
		try {
			tmpClass = Class.forName(name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		mCurrentClass = tmpClass;
		return mCurrentClass != null;
	}

	public final static int getStaticIntValue(String name, int defvalue) {
		int result = defvalue;
		Field field = getField(name);

		if (field != null) {
			try {
				result = field.getInt(null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public final static int getIntValue(Object owner, String name, int defvalue) {
		int result = defvalue;
		setClass(owner.getClass().getName());
		Field field = getField(name);

		if (field != null) {
			try {
				result = field.getInt(owner);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private final static Field getField(String name) {
		Field field = null;
		try {
			field = mCurrentClass.getDeclaredField(name);
			field.setAccessible(true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return field;
	}

	/**
	 * 得到某个类的静态属性
	 * 
	 * @param className
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public static Object getStaticProperty(String className, String fieldName) {
		setClass(className);
		Field field = getField(fieldName);
		Object result = null;

		if (field != null) {
			try {
				result = field.get(null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public static void setStaticProperty(String className, String fieldName, Object value) {
		setClass(className);
		Field field = getField(fieldName);

		if (field != null) {
			try {
				field.set(null, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 实例化对象；； 只支持 没有null对象和 只有String对象是null的情况
	 * 
	 * @param className
	 * @param args
	 * @return
	 * @throws Exception
	 */
	static public Object newInstance(String className, Object[] args) throws Exception {
		Class<?> newoneClass = Class.forName(className);
		Constructor<?> cons = newoneClass.getDeclaredConstructor(getArgsClasses(args));
		cons.setAccessible(true);
		return cons.newInstance(args);
	}

	/**
	 * 实例化对象
	 * 
	 * @param className
	 * @param args
	 * @return
	 * @throws Exception
	 */
	static public Object newInstance(String className) throws Exception {
		return newInstance(className, (Object[]) null);
	}

	/**
	 * 执行某对象的方法； 只支持 没有null对象和 只有String对象是null的情况
	 * 
	 * @param owner
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Exception
	 */
	static public Object invokeMethod(Object owner, String methodName, Object[] args)
			throws Exception {
		return invokeMethod(owner, methodName, getArgsClasses(args), args);
	}

	/**
	 * 执行某对象的无参方法
	 * 
	 * @param owner
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Exception
	 */
	static public Object invokeMethod(Object owner, String methodName) throws Exception {
		return invokeMethod(owner, methodName, null);
	}

	/**
	 * 执行某对象的方法
	 * 
	 * @param owner
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Exception
	 */
	static public Object invokeMethod(Object owner, String methodName, Class<?> argsClass[],
			Object[] args) throws Exception {
		Class<?> ownerClass = owner.getClass();
		Method method = null;
		try {
			method = ownerClass.getDeclaredMethod(methodName, argsClass);
		} catch (Exception e) {
			if (method == null && ownerClass.getSuperclass() != null) {
				method = ownerClass.getSuperclass().getDeclaredMethod(methodName, argsClass);
			}
			if (method == null) {
				throw e;
			}
		}

		method.setAccessible(true);
		return method.invoke(owner, args);
	}

	/**
	 * 获取某对象的属性
	 * 
	 * @param owner
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public static Object getProperty(Object owner, String fieldName) throws Exception {

		Field field = getFieldInternal(owner, fieldName);
		field.setAccessible(true);
		Object property = field.get(owner);

		return property;
	}

	public static boolean setPropertyNoThrowable(Object owner, String fieldName, Object value) {
		try {
			setPropertyInternal(owner, fieldName, value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void setProperty(Object owner, String fieldName, Object value) throws Exception {
		setPropertyInternal(owner, fieldName, value);
	}

	private static void setPropertyInternal(Object owner, String fieldName, Object value)
			throws Exception {
		if (null == owner) {
			throw new NullPointerException("setProperty null owner");
		}

		Field field = getFieldInternal(owner, fieldName);

		try {
			field.setAccessible(true);
			field.set(owner, value);
		} catch (Exception e) {
			// protect
			throw e;
		}
	}

	private static Field getFieldInternal(Object owner, String fieldName)
			throws NoSuchFieldException {
		Class<?> cls = null;
		Field field = null;
		for (cls = owner.getClass(); null != cls; cls = cls.getSuperclass()) {
			try {
				field = cls.getDeclaredField(fieldName);
				break;
			} catch (NoSuchFieldException e) {
				field = null;
			}
		}

		if (null == field) {
			throw new NoSuchFieldException("setProperty field " + fieldName + " not found");
		}

		return field;
	}

	public static Object newInstance(String className, Object args[], Class<?> argsClass[])
			throws Exception {
		Class<?> newoneClass = Class.forName(className);
		Constructor<?> cons = newoneClass.getDeclaredConstructor(argsClass);
		cons.setAccessible(true);
		return cons.newInstance(args);
	}

	public static void dump(Class<?> cls) {
		Log.v(LOG_TAG, cls.getCanonicalName());

		Method[] methods = cls.getMethods();
		for (Method method : methods) {
			Class<?>[] params = method.getParameterTypes();
			StringBuilder sb = new StringBuilder();
			sb.append("  - ");
			sb.append(method.getReturnType());
			sb.append(' ');
			sb.append(method.getName());
			sb.append('(');
			final int length = params.length;
			for (int i = 0; i != length; ++i) {
				sb.append(params[i].getName());
				if (i != length - 1) {
					sb.append(", ");
				}
			}
			sb.append(')');
			Log.v(LOG_TAG, sb.toString());
		}
	}

	public static Method reflectStaticMethod(String className, String methodName,
			Class<?> argsClass[]) throws Exception {
		Class<?> cls = Class.forName(className);
		return reflectStaticMethod(cls, methodName, argsClass);
	}

	public static Method reflectStaticMethod(Class<?> cls, String methodName, Class<?> argsClass[])
			throws Exception {
		Method staticMethod = null;
		try {
			staticMethod = cls.getDeclaredMethod(methodName, argsClass);
		} catch (Exception e) {
			if (staticMethod == null && cls.getSuperclass() != null) {
				staticMethod = cls.getSuperclass().getDeclaredMethod(methodName, argsClass);
			}
			if (staticMethod == null) {
				throw e;
			}
		}
		staticMethod.setAccessible(true);
		return staticMethod;
	}

	public static Object invokeStaticMethod(String className, String methodName, Object args[],
			Class<?> argsClass[]) throws Exception {
		Class<?> cls = Class.forName(className);
		Method staticMethod = null;
		Object ret = null;
		try {
			staticMethod = cls.getDeclaredMethod(methodName, argsClass);
		} catch (Exception e) {
			if (staticMethod == null && cls.getSuperclass() != null) {
				staticMethod = cls.getSuperclass().getDeclaredMethod(methodName, argsClass);
			}
			if (staticMethod == null) {
				throw e;
			}
		}
		staticMethod.setAccessible(true);
		ret = staticMethod.invoke(cls, args);
		return ret;
	}

	/**
	 * 只支持 没有null对象和 只有String对象是null的情况
	 * 
	 * @param className
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Exception
	 * 
	 * @author yuanhanhu in 2013-5-10
	 */
	public static Object invokeStaticMethod(String className, String methodName, Object args[])
			throws Exception {
		return invokeStaticMethod(className, methodName, args, getArgsClasses(args));
	}

	public static Object invokeStaticMethod(String className, String methodName) throws Exception {
		return invokeStaticMethod(className, methodName, (Object[]) null);
	}

	private static Class<?>[] getArgsClasses(Object[] args) {
		Class<?>[] argsClass = (Class<?>[]) null;
		if (args != null) {
			argsClass = new Class<?>[args.length];

			for (int i = 0, j = args.length; i < j; i++) {
				if (args[i] != null) {
					argsClass[i] = args[i].getClass();
				} else {
					argsClass[i] = String.class;
				}
				if (argsClass[i] == Integer.class) {
					argsClass[i] = int.class;
				} else if (argsClass[i] == Boolean.class) {
					argsClass[i] = boolean.class;
				} else if (argsClass[i] == Long.class) {
					argsClass[i] = long.class;
				}
			}
		}
		return argsClass;
	}

	public static Field[] getDeclaredFields(Object clsObj) {

		Field[] fieldList = null;
		if (clsObj != null) {
			try {
				Class<?> ownerClass = clsObj.getClass();
				if (ownerClass != null) {
					fieldList = ownerClass.getDeclaredFields();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return fieldList;
	}

	public static Method[] getDeclaredMethods(Object clsObj) {

		Method[] methodList = null;
		if (clsObj != null) {
			try {
				Class<?> ownerClass = clsObj.getClass();
				if (ownerClass != null) {
					methodList = ownerClass.getDeclaredMethods();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return methodList;
	}

	public static Class<?> getClass(String name) {
		try {
			return Class.forName(name);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}
