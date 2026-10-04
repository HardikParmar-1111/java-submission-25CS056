import java.util.Scanner;
abstract class Shape {
    abstract double area();

}
class Circle extends Shape{
    double radius;
    Circle(double radius){
        this.radius = radius;

    }
    double area(){
        return 3.14 * radius * radius;
    }
}
class Rectangle extends Shape{
    double length;
    double breadth;
    Rectangle(double length, double breadth){
        this.length = length;
        this.breadth = breadth;
    }
    double area(){
        return length * breadth;
    }
}
class Triangle extends Shape{
    double base;
    double height;
    Triangle(double base, double height){
        this.base = base;
        this.height = height;
    }
    double area(){
        return 0.5 * base * height;
    }
}

public class ShapeDemo{
    public static void main(String[] args){
        Shape[] shapes = {
            new Circle (5),
            new Rectangle(5,6 ),
            new Triangle(5,6)
        };
        double total=0;
        double largest=-1;
        String label="";

        for(Shape s: shapes){
            total += s.area();
            System.out.println("Area: " + s.area());
            if(largest < s.area()){
                largest = s.area();
                label = s.getClass().getSimpleName();
            }
        }
        double largestArea = largest;
        System.out.println("Total Area: " + total);
        System.out.println("Largest Area: " + largestArea + " of shape " + label);
        
    }
}
