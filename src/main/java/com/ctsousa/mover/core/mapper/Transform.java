package com.ctsousa.mover.core.mapper;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Transform {

    private Transform() { }

    public static <S, T> T toMapper(S source, Class<T> targetClass, String ... ignoreAttrs) {
        try {
            Constructor<T> constructor = findMatchingConstructor(targetClass, source);
            T target = constructor.newInstance(extractConstructorArguments(source, constructor, ignoreAttrs));
            return mappedField(source, target, ignoreAttrs);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new NotificationException(e.getMessage());
        }
    }

    public static <S, T> List<T> toCollection(List<S> sources, Class<T> targetClass, String ... ignoreAttrs) {
        List<T> list = new ArrayList<>();

        for (S source : sources) {
            try {
                list.add(toMapper(source, targetClass, ignoreAttrs));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return list;
    }

    private static <S, T> T mappedField(S source, T target, String ... ignoreAttrs) {
        Field [] sourceFields = getAllFields(source.getClass());
        Field [] targetFields = getAllFields(target.getClass());

        for (Field targetField : targetFields) {
            if (contains(ignoreAttrs, targetField.getName())) continue;

            Boolean mapped = directMapping(sourceFields, targetField, source, target);

            if (!mapped) {
                mapped = mapperByAssociation(sourceFields, targetField, source, target);

                if (!mapped) {
                    mapped = mapperByInvertAssociation(sourceFields, targetField, source, target);

                    if (!mapped) {
                        mapperByComplexAssociation(sourceFields, targetField, source, target);
                    }
                }
            }
        }

        return target;
    }

    private static <S, T> void mapperByComplexAssociation(Field [] sourceFields, Field targetField, S source, T target) {
        for (Field field : sourceFields) {
            field.setAccessible(true);
            try {
                if (targetField.getName().equalsIgnoreCase(field.getName())) {
                    targetField.setAccessible(true);
                    Object sourceValue = field.get(source);

                    if (sourceValue != null) {
                        Object nestedTargetInstance = targetField.getType().getConstructor().newInstance();
                        Field[] nestedTargetFields = getAllFields(targetField.getType());

                        for (Field nestedTargetField : nestedTargetFields) {
                            directMapping(getAllFields(sourceValue.getClass()), nestedTargetField, sourceValue, nestedTargetInstance);
                        }

                        targetField.set(target, nestedTargetInstance);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                targetField.setAccessible(false);
                field.setAccessible(false);
            }
        }
    }

    private static <S, T> Boolean mapperByInvertAssociation(Field [] sourceFields, Field targetField, S source, T target) {
        boolean mapped = false;
        Object nestedInstance = null;
        for (Field field : sourceFields) {
            try {
                if (nestedInstance == null) {
                    nestedInstance = targetField.getType().getDeclaredConstructor().newInstance();
                }

                if (field.getName().startsWith(targetField.getName()) && isComplexObject(targetField)) {
                    String nestedFieldName = field.getName();
                    field.setAccessible(true);
                    Object valueObject = field.get(source);

                    if (valueObject != null) {
                        Field[] nestedFields = getAllFields(targetField.getType());

                        for (Field nestedField : nestedFields) {
                            if (targetField.getName().concat(nestedField.getName()).equalsIgnoreCase(nestedFieldName)) {
                                nestedField.setAccessible(true);
                                setProperty(nestedInstance, nestedField.getName(), valueObject);
                                nestedField.setAccessible(false);
                                targetField.setAccessible(true);
                                targetField.set(target, nestedInstance);
                                mapped = true;
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                field.setAccessible(false);
                targetField.setAccessible(false);
            }
        }
        return mapped;
    }

    private static <S, T> Boolean mapperByAssociation(Field [] sourceFields, Field targetField, S source, T target) {
        boolean mapped = false;
        for (Field field : sourceFields) {
            try {
                if (targetField.getName().startsWith(field.getName())) {
                    String nestedFieldName = targetField.getName().substring(field.getName().length());
                    field.setAccessible(true);
                    Object nestedObject = field.get(source);
                    if (nestedObject != null) {
                        Field [] nestedFields = getAllFields(nestedObject.getClass());
                        for (Field nestedField : nestedFields) {
                            if (nestedField.getName().equalsIgnoreCase(nestedFieldName)) {
                                nestedField.setAccessible(true);
                                targetField.setAccessible(true);
                                setProperty(target, targetField.getName(), nestedField.get(nestedObject));
                                nestedField.setAccessible(false);
                                mapped = true;
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                field.setAccessible(false);
                targetField.setAccessible(false);
            }
        }
        return mapped;
    }

    private static <S, T> Boolean directMapping(Field [] sourceFields, Field targetField, S source, T target) {
        boolean mapped = false;
        for (Field field : sourceFields) {
            try {
                if (field.getName().equals(targetField.getName())) {
                    field.setAccessible(true);
                    targetField.setAccessible(true);
                    Object valueObject = field.get(source);
                    setProperty(target, field.getName(), valueObject);
                    mapped = true;
                    break;
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                field.setAccessible(false);
                targetField.setAccessible(false);
            }
        }
        return mapped;
    }

    private static <T, V> void setProperty(T instance, String propertyName, V value) {
        try {
            String methodName = "set" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
            Method method = instance.getClass().getMethod(methodName, value.getClass());
            method.invoke(instance, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new NotificationException(e.getMessage());
        }
    }

    private static Field[] getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(new Field[0]);
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor <T> findMatchingConstructor(Class<T> targetClass, Object source) throws NoSuchMethodException {
        for (Constructor<?> constructor : targetClass.getConstructors()) {
            Class<?> [] parameterTypes = constructor.getParameterTypes();
            boolean isMatch = true;

            for (Class<?> parameterType : parameterTypes) {
                boolean hasField = false;
                for (Field sourceField : source.getClass().getDeclaredFields()) {
                    if (parameterType.isAssignableFrom(sourceField.getType())) {
                        hasField = true;
                        break;
                    }
                }
                if (!hasField) {
                    isMatch = false;
                    break;
                }
            }

            if (isMatch) return (Constructor<T>) constructor;
        }

        throw new NoSuchMethodException("No matching constructor found for" + targetClass.getName());
    }

    private static Object[] extractConstructorArguments(Object source, Constructor<?> constructor, String... ignoreFields) throws IllegalAccessException {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            for (Field sourceField : source.getClass().getDeclaredFields()) {
                if (parameterTypes[i].isAssignableFrom(sourceField.getType()) &&
                        !contains(ignoreFields, sourceField.getName())) {

                    sourceField.setAccessible(true);
                    arguments[i] = sourceField.get(source);
                    sourceField.setAccessible(false);
                }
            }
        }

        return arguments;
    }

    private static boolean contains(String [] fields, String fieldname) {
        for (String field : fields) {
            if (field.equalsIgnoreCase(fieldname)) return true;
        }
        return false;
    }

    private static boolean isComplexObject(Field field) {
        return !field.getType().isPrimitive() && !field.getType().equals(String.class);
    }
}
