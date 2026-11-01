class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

public class GenericContainersDemo {
    public static void main(String[] args) {
        Box<String> name = new Box<>();
        Box<Integer> number = new Box<>();
        name.set("Hardik");
        number.set(56);
        System.out.println(name.get());
        System.out.println(number.get());
    }
}
