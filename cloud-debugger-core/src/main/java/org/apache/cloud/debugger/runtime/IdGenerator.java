package org.apache.cloud.debugger.runtime;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yiji@apache.org
 */
public class IdGenerator {

    private static final Map<String /** class name */, Integer /** order */> classNameMapper = new ConcurrentHashMap<>();

    private static final Map<ClassLoader /** classloader */, Integer /** order */> classLoaderMapper = new ConcurrentHashMap<>();

    private static final Map<String /** method sign */, Integer /** order */> methodsignMapper = new ConcurrentHashMap<>();

    private static final Map<Integer /** order */, ClassLoader /** classloader */> classLoaderIdMapper = new ConcurrentHashMap<>();

    private static final AtomicInteger classNameCounter = new AtomicInteger(1);

    private static final AtomicInteger classLoaderCounter = new AtomicInteger(1);

    private static final AtomicInteger methodSignCounter = new AtomicInteger(1);

    public static int getClassloaderId(ClassLoader classloader) {
        if (classloader == null) {
            return 0;
        }

        Integer classloaderId = (Integer) classLoaderMapper.get(classloader);
        if (classloaderId == null) {

            synchronized (classLoaderMapper) {

                classloaderId = (Integer) classLoaderMapper.get(classloader);
                if (classloaderId != null) {
                    return classloaderId.intValue();
                }

                classloaderId = classLoaderCounter.getAndIncrement();
                classLoaderMapper.put(classloader, classloaderId);

                classLoaderIdMapper.put(classloaderId, classloader);
            }

        }

        return classloaderId.intValue();
    }


    public static int getClassNameId(String className) {
        if (className == null) {
            return 0;
        }

        Integer classNameId = (Integer) classNameMapper.get(className);
        if (classNameId == null) {

            synchronized (classNameMapper) {

                classNameId = (Integer) classNameMapper.get(className);
                if (classNameId != null) {
                    return classNameId.intValue();
                }

                classNameId = classNameCounter.getAndIncrement();
                classNameMapper.put(className, classNameId);
            }

        }

        return classNameId.intValue();
    }

    public static int getMethodSignId(String methodSign) {
        if (methodSign == null) {
            return 0;
        }

        Integer methodSignId = (Integer) methodsignMapper.get(methodSign);
        if (methodSignId == null) {

            synchronized (methodsignMapper) {

                methodSignId = (Integer) methodsignMapper.get(methodSign);
                if (methodSignId != null) {
                    return methodSignId.intValue();
                }

                methodSignId = methodSignCounter.getAndIncrement();
                methodsignMapper.put(methodSign, methodSignId);
            }

        }

        return methodSignId.intValue();
    }

    public static long newId(ClassLoader classloader, String className, String methodSign) {

        int classLoaderId = getClassloaderId(classloader);
        int classNameId = getClassNameId(className);
        int methodSignId = getMethodSignId(methodSign);

        return classLoaderId << 50 | classNameId << 32 | methodSignId;
    }

    public static ClassLoader getClassLoader(long id) {
        int classLoaderId = (int) (id >> 50) & 0x3fff;
        return classLoaderIdMapper.get(classLoaderId);
    }
}
