package org.yy.framework.service.util;

import java.util.Map;

/**  
 */
public class MapUtils {
    
    @SuppressWarnings("rawtypes")
    public static Boolean getBoolean(final Map map, final Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                if (answer instanceof Boolean) {
                    return (Boolean)answer;
                    
                }
                else if (answer instanceof String) {
                    return new Boolean((String)answer);
                    
                }
                else if (answer instanceof Number) {
                    Number n = (Number)answer;
                    return (n.intValue() != 0) ? Boolean.TRUE : Boolean.FALSE;
                }
            }
        }
        return null;
    }
    
    @SuppressWarnings("rawtypes")
    public static Boolean getBoolean(Map map, Object key, Boolean defaultValue) {
        Boolean answer = getBoolean(map, key);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }
    
}