package app.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

class LogEntry{
    String line;
    LocalDateTime timestamp;

    public LogEntry(String l, LocalDateTime t){
        line = l;
        timestamp = t;
    }
}

class LogProcessor{
    PriorityQueue<LogEntry> pq = new PriorityQueue<>((a,b) -> a.timestamp.compareTo(b.timestamp));

    void ingest(String line, LocalDateTime t1){
        pq.add(new LogEntry(line,t1));
    }

    List<LogEntry> getLogs(){
        deleteOld();
        return new ArrayList<>(pq);
    }

    void deleteOld(){
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusSeconds(10);

        while(!pq.isEmpty()&&pq.peek().timestamp.isBefore(start)){
            pq.poll();
        }
    }
}

public class LogFetch {

    public static void main(String[] args){

    }

}
