package app.languageexamples;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class MyTesting {

    static final class Node{
        int key;
        int val;
        List<Node> list = new ArrayList<>();
        Node(int a, int b){
            key = a;
            val = b;
        }

        void func(){
            Node n = this;
        }
    }
    public static void main(String[] args){
//          Node n =
//        System.out.println(oldTab!=null);

    }
}
