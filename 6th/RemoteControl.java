interface Switchable {
    void on();
    void off();
    boolean isOn();

    default void toggle() {
        if (isOn()) {
            off();
        } else {
            on();
        }
    }
}

class Fan implements Switchable {
    private boolean on;

    public void on() {
        on = true;
        System.out.println("Fan is on");
    }

    public void off() {
        on = false;
        System.out.println("Fan is off");
    }

    public boolean isOn() {
        return on;
    }
}

class Light implements Switchable {
    private boolean on;

    public void on() {
        on = true;
        System.out.println("Light is on");
    }

    public void off() {
        on = false;
        System.out.println("Light is off");
    }

    public boolean isOn() {
        return on;
    }
}

@FunctionalInterface
interface SwitchRule {
    boolean maySwitchOn(Switchable device, int hour);
}

public class RemoteControl {
    public static void main(String[] args) {
        Fan fan = new Fan();
        Light light = new Light();
        Switchable[] devices = {fan, light};

        for (Switchable device : devices) {
            device.toggle();
        }

        SwitchRule daytimeRule = new SwitchRule() {
            public boolean maySwitchOn(Switchable device, int hour) {
                return (device instanceof Fan || device instanceof Light)
                        && hour >= 8 && hour < 22;
            }
        };

        SwitchRule eveningRule = (device, hour) ->
                (device instanceof Fan || device instanceof Light)
                        && hour >= 8 && hour < 22;

        System.out.println("Anonymous rule: " + daytimeRule.maySwitchOn(fan, 10));
        System.out.println("Lambda rule: " + eveningRule.maySwitchOn(light, 10));
    }
}
