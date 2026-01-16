package app.example;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class TaskScheduler {
    List<Task> taskList = new ArrayList<>();
    public static void main(String[] args){
        TaskScheduler taskScheduler = new TaskScheduler();
        taskScheduler.taskList.add(new Task("mk",123));
        taskScheduler.taskList.add(new Task("ab",123));
        taskScheduler.taskList.add(new Task("mk",64));

        List<Task> ans= taskScheduler.getOrderByDurationDescNameAsc(3);

        for(Task t : ans){
            System.out.println(t.name + " : " + t.duration);
        }

    }

    List<Task> getOrderByDurationDescNameAsc(int limit){
        PriorityQueue<Task> pq = new PriorityQueue<>((a,b)->{
            if(a.duration!=b.duration)
                return -b.duration+a.duration;
            return b.name.compareTo(a.name);
        });
        int i;
        for(i=0;i<taskList.size();i++){
            pq.add(taskList.get(i));
            if(pq.size()==limit)
                break;
        }

        for(;i<taskList.size();i++){
            if(pq.peek().duration < taskList.get(i).duration)
                pq.poll();
            else if(pq.peek().duration==taskList.get(i).duration && pq.peek().name.compareTo(taskList.get(i).name) > 0)
                pq.poll();
            if(pq.size()<limit)
                pq.add(taskList.get(i));
        }

        List<Task> ans = new ArrayList<>();

        while(!pq.isEmpty()){
            ans.add(pq.poll());
        }

        return ans.reversed();
    }
}

class Task{
    String name;
    int duration;

    public Task(String n, int d){
        name = n;
        duration = d;
    }
}
