package com.zzc.nettyapi.argument.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Map;

/**
 * @author zhengzechao
 * @date 2018/4/26
 * Email ooczzoo@gmail.com
 */
public class ReflectionTool {

    /** set方法前缀*/
    public static final String SET_PRE = "set";
    /** get方法前缀*/
    public static final String GET_PRE = "get";


    public static void resolveMethods(BeanWrapper beanWrapper) {
        Class targetClass = beanWrapper.getClass();
        Map<String,Method> writeMethodMap = Maps.newHashMap();
        Map<String,Method> readMethodMap = Maps.newHashMap();

        Method[] allMethods = targetClass.getMethods();
        for (Method method : allMethods) {
            String methodName = method.getName();
            if(methodName.startsWith(SET_PRE)){
                String propertyName = methodName.substring(3);
                if (!Strings.isNullOrEmpty(propertyName)){

                    propertyName = propertyName.substring(0,1).toLowerCase()+
                            propertyName.substring(1);
                    writeMethodMap.put(propertyName,method);
                }

            }else if(methodName.startsWith(GET_PRE)){
                String propertyName = methodName.substring(3);
                if (!Strings.isNullOrEmpty(propertyName)){

                    propertyName = propertyName.substring(0,1).toLowerCase()+
                            propertyName.substring(1);
                    readMethodMap.put(propertyName,method);
                }
            }
        }


        beanWrapper.setReadMethodMap(readMethodMap);
        beanWrapper.setWriteMethodMap(writeMethodMap);

    }


    /**
     * 解析出对应的propertyHandler 并且将该propertyHandler放入到指定的BeanWrapper;
     * @param name
     * @param type
     * @return
     */

    public static PropertyHandler resolveProperty(String name,Class type,BeanWrapper beanWrapper){

        //先从类型中查看是否能找到该name对应的field
        PropertyHandler  propertyHandler = new PropertyHandler(name);
        Field field = findField(name, type,propertyHandler);
        if(Objects.nonNull(field)){

            Class propertyType =field.getType();

            propertyHandler.setPrimitive(propertyType.isPrimitive());
            propertyHandler.setArray(propertyType.isArray());


            Map<String,Method> writeMethodMap = beanWrapper.getWriteMethodMap();
            Map<String,Method> readMethodMap = beanWrapper.getReadMethodMap();
            if (Objects.isNull(writeMethodMap)|| Objects.nonNull(readMethodMap)){
                resolveMethods(beanWrapper);

                writeMethodMap = beanWrapper.getWriteMethodMap();
                readMethodMap= beanWrapper.getReadMethodMap();
                Method writeMethod = writeMethodMap.get(name);
                Method readMethod = readMethodMap.get(name);
                propertyHandler.setWritable(Objects.nonNull(writeMethod));
                propertyHandler.setWriteMethod(writeMethod);
                propertyHandler.setReadMethod(readMethod);
            }
        }else{
            return null;
        }

        return propertyHandler;
    }

    public static Field findField(String name, Class type, PropertyHandler propertyHandler) {

        Class queryClass = type;
        Field[] fields ;
        Field targetField = null;
        while(queryClass != null){
            fields = queryClass.getDeclaredFields();
            targetField = Arrays.stream(fields)
                        .filter(t->name.equals(t.getName()))
                        .findFirst()
                        .orElse(null);
            if(Objects.nonNull(targetField)){
                propertyHandler.setBelongType(queryClass);
                break;
            }
            queryClass = queryClass.getSuperclass();
        }

        return targetField;
    }

}
