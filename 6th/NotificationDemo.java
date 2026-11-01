@FunctionalInterface
interface Notifier {
    void send(String message);

    default void sendUrgent(String message) {
        send(format(message));
    }

    static boolean isValid(String message) {
        return message != null && !message.isBlank();
    }

    private String format(String message) {
        return "URGENT: " + message;
    }
}

interface Urgent {}

public class NotificationDemo {
    public static void main(String[] args) {
        Notifier email = (Notifier & Urgent) message ->
                System.out.println("Email: " + message);
        Notifier sms = message ->
                System.out.println("SMS: " + message);
        Notifier[] senders = {email, sms};

        for (Notifier sender : senders) {
            String message = "Meeting starts at 10.";
            if (!Notifier.isValid(message)) {
                continue;
            }
            sender.send(message);
            if (sender instanceof Urgent) {
                sender.sendUrgent(message);
            }
        }
    }
}
