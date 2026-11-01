package model.annotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class AnnotationValidator {
    public static String[] validate(Object obj) throws IllegalAccessException {
        Objects.requireNonNull(obj, "obj");
        ArrayList<String> errors = new ArrayList<>();
        Class<?> current = obj.getClass();

        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(obj);

                Positive positive = field.getAnnotation(Positive.class);
                if (positive != null && value instanceof Number
                        && ((Number) value).longValue() <= 0) {
                    errors.add(field.getName() + " " + positive.message());
                }

                MaxLength maxLength = field.getAnnotation(MaxLength.class);
                if (maxLength != null && value instanceof String
                        && ((String) value).length() > maxLength.value()) {
                    errors.add(field.getName() + " must be at most "
                            + maxLength.value() + " characters");
                }
            }
            current = current.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}
