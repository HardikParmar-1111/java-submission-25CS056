import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Files;

class StudentRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private transient String sessionToken;

    StudentRecord(String name, String sessionToken) {
        this.name = name;
        this.sessionToken = sessionToken;
    }

    public String toString() {
        return name + " | " + sessionToken;
    }
}

public class SerializationDemo {
    public static void main(String[] args) {
        Path file = null;
        try {
            file = Files.createTempFile("student-record", ".dat");
            StudentRecord student = new StudentRecord("Hardik", "token-123");
            try (ObjectOutputStream out = new ObjectOutputStream(
                    Files.newOutputStream(file))) {
                out.writeObject(student);
            }

            try (ObjectInputStream in = new ObjectInputStream(
                    Files.newInputStream(file))) {
                StudentRecord saved = (StudentRecord) in.readObject();
                System.out.println(saved);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not save or load record: " + e.getMessage());
        } finally {
            if (file != null) {
                try {
                    Files.deleteIfExists(file);
                } catch (IOException e) {
                    System.out.println("Could not remove temporary file: " + e.getMessage());
                }
            }
        }
    }
}
