package repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.annotation.Id;

public class Repository<T> {
    private Map<String, T> items = new HashMap<>();

    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        String id = getId(entity);
        items.put(id, entity);
        return entity;
    }

    public T findById(String id) {
        return items.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(items.values());
    }

    public boolean delete(String id) {
        return items.remove(id) != null;
    }

    @SafeVarargs
    public final void saveAll(T... entities) {
        for (T entity : entities) {
            save(entity);
        }
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values cannot be empty");
        }

        double total = 0;
        for (N value : values) {
            if (value == null) {
                throw new IllegalArgumentException("Values cannot contain null");
            }
            total += value.doubleValue();
        }
        return total / values.length;
    }

    private String getId(T entity) {
        Class<?> current = entity.getClass();
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        if (value == null) {
                            throw new IllegalArgumentException("Id cannot be null");
                        }
                        return value.toString();
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Could not read entity id", e);
                    }
                }
            }
            current = current.getSuperclass();
        }
        throw new IllegalArgumentException(
                "Entity class must have a field marked with @Id");
    }
}
