package org.apache.cloud.debugger.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author yiji@apache.org
 */
public class TypeUtil {

    public static String convertToClassName(String resourcePath) {
        return resourcePath.replace("/", ".");
    }

    public static String join(Iterator iterator, String separator) {
        if (iterator == null) return "";
        if (!iterator.hasNext()) return "";

        Object first = iterator.next();

        if (!iterator.hasNext()) {
            String result = (first == null ? "" : first.toString());
            return result;
        } else {
            StringBuilder buf = new StringBuilder(256);
            if (first != null) {
                buf.append(first);
            }

            while (iterator.hasNext()) {
                if (separator != null) {
                    buf.append(separator);
                }

                Object obj = iterator.next();
                if (obj != null) {
                    buf.append(obj);
                }
            }

            return buf.toString();
        }
    }

    public static String join(Collection collection, String separator) {
        return collection != null ? join(collection.iterator(), separator) : "";
    }
}
