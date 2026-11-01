import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {}

class SampleTests {
    @Run
    public void firstTest() {
        System.out.println("First test ran");
    }

    @Run
    public void secondTest() {
        System.out.println("Second test ran");
    }

    public void helperMethod() {
        System.out.println("This method is not a test");
    }
}

public class MiniTestRunner {
    public static void main(String[] args) throws ReflectiveOperationException {
        SampleTests tests = new SampleTests();
        int ran = 0;

        for (Method method : tests.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Run.class)) {
                if (method.getParameterCount() != 0) {
                    throw new IllegalStateException(
                            method.getName() + " must not have parameters");
                }

                method.invoke(tests);
                ran++;
            }
        }

        System.out.println("Methods run: " + ran);
    }
}
