package cn.hd.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

public final class EntityUtil {

    private static Map<Class, Type> typeMap = new HashMap<>();

    static {
        typeMap.put(String.class, new StringType());
        typeMap.put(Integer.class, new IntegerType());
        typeMap.put(Date.class, new DateType());
        typeMap.put(Long.class, new LongType());
        typeMap.put(Boolean.class, new BooleanType());
        typeMap.put(Double.class, new DoubleType());
    }

    private interface Type<T>{
        T getValue(Object val);
    }

    private static class IntegerType implements Type<Integer>{
        @Override
        public Integer getValue(Object val) {
            return Integer.valueOf(val.toString());
        }
    }

    private static class StringType implements Type<String>{
        @Override
        public String getValue(Object val) {
            return val.toString();
        }
    }

    private static class DateType implements Type<Date>{
        @Override
        public Date getValue(Object val) {
            try {
                return DateUtil.formatDateString(val.toString());
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private static class LongType implements Type<Long>{
        @Override
        public Long getValue(Object val) {
            return Long.valueOf(val.toString());
        }
    }

    private static class BooleanType implements Type<Boolean>{
        @Override
        public Boolean getValue(Object val) {
            return Boolean.valueOf(val.toString());
        }
    }

    private static class DoubleType implements Type<Double>{
        @Override
        public Double getValue(Object val) {
            return Double.valueOf(val.toString());
        }
    }

    /**
     * 将map转化为对应的实体类
     * @param clazz 实体类类对象
     * @param map 属性map
     * @param <T>
     * @return 实体类
     */
    public static <T> T convert(Class<T> clazz, Map<String, Object> map) throws Exception {
        T t = clazz.newInstance();
        List<Field> fields = getClassField(clazz);
        Object val = null;
        for(Field field : fields){
            field.setAccessible(true);
            if((val = map.get(field.getName())) != null){
                field.set(t, typeMap.get(field.getType()).getValue(val));
            }
        }
        return t;
    }

    private static List<Field> getClassField(Class clazz){
        List<Field> fieldList = new ArrayList<>() ;
        Class tempClass = clazz;
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass .getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        return fieldList;
    }

}
