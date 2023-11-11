package pers.jssd.mybatispluspro.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Apprentice系统Util
 *
 * @since JDK 1.8.0
 */
public class ApprenticeUtil {

    private static final Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LINE_PATTERN = Pattern.compile("_(\\w)");

    /**
     * 驼峰转下划线
     *
     * @param str 字符串
     * @return java.lang.String
     */
    public static String humpToLine(String str) {
        Matcher matcher = HUMP_PATTERN.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param str 字符串
     * @return java.lang.String
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = LINE_PATTERN.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取QueryWrapper
     *
     * @param entity 实体
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<E>
     */
    public static <E> QueryWrapper<E> getQueryWrapper(E entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        QueryWrapper<E> eQueryWrapper = new QueryWrapper<>();
        for (Field field : fields) {
            //忽略final字段
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object obj = field.get(entity);
                if (!ObjectUtils.isEmpty(obj)) {
                    String name = ApprenticeUtil.humpToLine(field.getName());
                    eQueryWrapper.eq(name, obj);
                }
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return eQueryWrapper;

    }

    /**
     * 反射获取字段值
     *
     * @param entity 实体
     * @param value  value 值为 "id" "name" 等
     * @return java.lang.Object
     */
    public static <E> Object getValueForClass(E entity, String value) {

        Field id = null;
        PropertyDescriptor pd = null;
        try {
            id = entity.getClass().getDeclaredField(value);
            pd = new PropertyDescriptor(id.getName(), entity.getClass());
        } catch (NoSuchFieldException | IntrospectionException e) {
            throw new RuntimeException();
        }
        //获取get方法
        Method getMethod = Objects.requireNonNull(pd).getReadMethod();
        return ReflectionUtils.invokeMethod(getMethod, entity);
    }
}