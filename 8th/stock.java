import java.util.Scanner;

class OutOfStockException extends Exception {
    public OutOfStockException(String message) {
        super(message);
    }
}
class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String message) {
        super(message);
    }
}

class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String message) {
        super(message);
    }
}

public class stock {
    int qty[] = {10, 20, 30};
    String item[]={"apple","banana","orange"};

    public void issue(String item, int qty) throws OutOfStockException,
            InvalidQuantityException, ItemNotFoundException {

        if (qty <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero");
        }

        int index = -1;
        for(int i=0;i<this.item.length;i++){
            if(this.item[i].equalsIgnoreCase(item)){
                index = i;
                break;
            }
        }
        if(index == -1){
            throw new ItemNotFoundException("Item not found");
        }
        if(this.qty[index] < qty){
            throw new OutOfStockException("Not enough stock available");
        }
        this.qty[index] -= qty;
    }
    public static void main(String[] args){
        stock s = new stock();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter item name:");
        String item = sc.nextLine().trim();
        System.out.println("Enter quantity:");
        int qty = Integer.parseInt(sc.nextLine().trim());
        try{
            s.issue(item, qty);
            System.out.println("Issued " + qty + " of " + item);
        }
        catch(OutOfStockException e){
            System.out.println(e.getMessage());
        }
        catch(InvalidQuantityException e){
            System.out.println(e.getMessage());
        }
        catch(ItemNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(NumberFormatException e){
            System.out.println("Invalid quantity format");
        }
        
}
}
