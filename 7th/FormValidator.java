import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

class SignupForm {
    @NotBlank
    @MaxLength(20)
    private String name;

    @NotBlank
    private String email;

    SignupForm(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

public class FormValidator {
    public static List<String> validate(Object form) throws IllegalAccessException {
        Objects.requireNonNull(form, "form");
        List<String> errors = new ArrayList<>();

        for (Field field : form.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(form);

            if (field.isAnnotationPresent(NotBlank.class)
                    && (value == null || value.toString().isBlank())) {
                errors.add(field.getName() + " must not be blank");
            }

            MaxLength maxLength = field.getAnnotation(MaxLength.class);
            if (maxLength != null && value != null
                    && value.toString().length() > maxLength.value()) {
                errors.add(field.getName() + " must be at most "
                        + maxLength.value() + " characters");
            }
        }

        return errors;
    }

    public static void main(String[] args) throws IllegalAccessException {
        SignupForm form = new SignupForm(
                "This name is much too long to fit", " ");
        for (String error : validate(form)) {
            System.out.println(error);
        }
    }
}
