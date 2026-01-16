package app.designpattern.compositepattern;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

interface Item{
    void print();
}

class MenuItem implements Item{
    String name;
    Double price;

    public MenuItem(String n, Double p){
        name = n;
        price = p;
    }

    public void print(){
        System.out.println(name);
    }
}
class Menu implements Item{
     List<Item> menuItems;

     public Menu(List<Item> items){
         menuItems = items;
     }

     public void print(){
         menuItems.forEach(Item::print);
     }
}
public class CompPatternTest {
    public static void main(String[] args){
        List<Item> itemList = new ArrayList<>();
        Item item1 = new MenuItem("aa",1.0);
        Item item2 = new MenuItem("bb",1.0);
        Item item3 = new MenuItem("cc",1.0);

        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);

        Item menu = new Menu(itemList);
        Item item4 = new MenuItem("dd",1.0);
        Item subMenu = new Menu(Arrays.asList(item4));

        itemList.add(subMenu);

        menu.print();

    }
}
