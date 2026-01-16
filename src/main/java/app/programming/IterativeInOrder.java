package app.programming;


import java.util.Stack;

class Node{
    int val;
    Node left,right;

    Node(int v){
        val = v;
        left=right=null;
    }
}

public class IterativeInOrder {

    public static void main(String[] args){
        Node root = new Node(3);
        root.left = new Node(1);
        Node cur = new Node(5);
        root.right = cur;
        cur.left = new Node(4);

        Stack<Node> stack = new Stack<>();

        stack.push(root);

        while(!stack.isEmpty()){
            while(stack.peek().left != null){
                stack.push(stack.peek().left);
            }

            while(!stack.isEmpty()&&stack.peek().right==null){
                System.out.println(stack.pop().val);
            }
            if(!stack.isEmpty()){
                Node tmp = stack.pop();
                System.out.println(tmp.val);
                stack.push(tmp.right);
            }
        }
    }
}
