package app.programming;

import java.util.*;

public class UniqNumSeq {
    public static void main(String[] args){
        List<List<Integer>> seq = new ArrayList<>();
        seq.add(Arrays.asList(1,2,15,8));
        seq.add(null);
        seq.add(Arrays.asList(2,4,7,9));

        Map<Integer, Set<Integer>> graph = new HashMap<>();

        for(List<Integer> s : seq){
            if(s==null) continue;
            for(int i=0;i<s.size()-1;i++){
                int src = s.get(i);
                int dest = s.get(i+1);

                graph.putIfAbsent(src,new HashSet<>());
                graph.putIfAbsent(dest,new HashSet<>());
                graph.get(src).add(dest);
            }
        }

        Set<Integer> visited = new HashSet<>();
        Set<Integer> visiting = new HashSet<>();
        List<Integer> ans = new ArrayList<>();
        boolean flag = true;
        for(int v : graph.keySet()){
            if(!visited.contains(v)&&!isTopOrder(graph,v,visiting,visited,ans)){
                flag=false;
                break;
            }
        }
        System.out.println(flag);
        if(flag){
            Collections.reverse(ans);
            System.out.println(ans);
        }
    }

    private static boolean isTopOrder(Map<Integer, Set<Integer>> graph, int v, Set<Integer> visiting, Set<Integer> visited,List<Integer> ans) {
        if(visiting.contains(v))
            return  false;
        visiting.add(v);
        for(int adj : graph.get(v)){
            if(!visited.contains(adj)&&!isTopOrder(graph,adj,visiting,visited,ans))
                return false;
        }

        visited.add(v);
        visiting.add(v);
        ans.add(v);
        return true;
    }


}
