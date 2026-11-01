abstract class Employee {
    String name;
    int id;
    Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }
    abstract double monthlySalary();
}
class FullTime extends Employee{
    double salary;
    FullTime(String name, int id, double salary){
        super(name, id);
        this.salary = salary;
    }
    double monthlySalary(){
        return salary;
    }
}

class PartTime extends Employee{
    double hours;
    double rate;
    PartTime(String name, int id, double hours, double rate){
        super(name, id);
        this.hours = hours;
        this.rate = rate;
    }
    double monthlySalary(){
        return hours * rate;
    }
}

class Intern extends Employee {
    double stipend;
    Intern(String name, int id, double stipend){
        super(name, id);
        this.stipend = stipend;
    }
    double monthlySalary(){
        return stipend;
    }
}

class PayRoll{
    public static void main(String[] args){
        Employee[] employees = new Employee[3];
        employees[0] = new FullTime("Alice", 1, 5000);
        employees[1] = new PartTime("Bob", 2, 80, 20);
        employees[2] = new Intern("Charlie", 3, 1000);

        double total = 0;
        for(Employee emp : employees){
            double salary = emp.monthlySalary();
            total += salary;
            System.out.println(emp.name + " (ID: " + emp.id + ") - Monthly Salary: $" + salary);
            if (emp instanceof Intern) {
                System.out.println("Intern stipend: $" + ((Intern) emp).stipend);
            }
        }
        System.out.println("Total Salary: $" + total);
    }
}
