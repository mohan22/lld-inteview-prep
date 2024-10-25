package app.example;

public class GenericTesting {
    public static void main(String[] args){
        String str = "${a}";
        str = str.replaceAll("\\{","");

        System.out.println(str);
    }
}
