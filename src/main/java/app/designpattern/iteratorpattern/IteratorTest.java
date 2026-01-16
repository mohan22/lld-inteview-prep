package app.designpattern.iteratorpattern;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


class CustomIterator implements Iterator<Integer>{
    List<Integer> list;
    int curIndex;
    public CustomIterator(List<Integer> li){
        list = li;
        curIndex=0;
    }
    @Override
    public boolean hasNext() {
        return curIndex<list.size();
    }

    @Override
    public Integer next() {
        int p = list.get(curIndex);
        curIndex+=2;
        return p;

    }
}
public class IteratorTest {
    public static void main(String[] args){
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8);

        Iterator it = list.iterator();

        while(it.hasNext()){
            System.out.println(it.next());
        }

        CustomIterator ci = new CustomIterator(list);

        while(ci.hasNext()){
            System.out.println(ci.next());
        }
    }
}
