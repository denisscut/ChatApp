package com.example.chatApp.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    private final int V;
    private final LinkedList<Integer>[] adj ;
    List<ArrayList<Long>> components = new ArrayList<>();
    public Graph(int v, Iterable<Friendship> friendships, Iterable<User> users) {
        this.V = v;
        adj = new LinkedList[V + 1];
        for(User user: users) {
            int id = Math.toIntExact(user.getId());
            adj[id] = new LinkedList();
        }
        for(Friendship friendship: friendships) {
            int id1 = Math.toIntExact(friendship.getUserId1());
            int id2 = Math.toIntExact(friendship.getUserId2());
            adj[id1].add(id2);
            adj[id2].add(id1);
        }
        DFS(users);
    }

    void DFSUtil(int v, boolean[] visited, ArrayList<Long> al) {
        visited[v] = true;
        al.add((long) v);
        Iterator<Integer> it = adj[v].iterator();
        while(it.hasNext()) {
            int n = it.next();
            if(!visited[n])
                DFSUtil(n, visited, al);
        }
    }

    Long BFS(Long x)
    {
        boolean visited[] = new boolean[V + 1];
        Long dist[] = new Long[V + 1];
        int s = x.intValue();
        for(int i = 0; i < V; i++)
            dist[i] = 0l;
        dist[s] = 1l;
        LinkedList<Integer> queue = new LinkedList<Integer>();
        visited[s]=true;
        queue.add(s);

        while (queue.size() != 0)
        {
            s = queue.poll();

            Iterator<Integer> i = adj[s].listIterator();
            while (i.hasNext())
            {
                int n = i.next();
                if (!visited[n])
                {
                    visited[n] = true;
                    dist[n] = dist[s] + 1;
                    queue.add(n);
                }
            }
        }
        Long maxim = 0l;
        for(int i = 0; i < V; i++)
            if(maxim < dist[i])
                maxim = dist[i];
        return maxim;
    }

    void DFS(Iterable<User> users) {
        boolean[] visited = new boolean[V + 1];
        for(User user: users) {
            ArrayList<Long> al = new ArrayList<>();
            int id = Math.toIntExact(user.getId());
            if(!visited[id]) {
                DFSUtil(id, visited, al);
                components.add(al);
            }
        }
    }

    public int ConnectedComponents() {
        return components.size();
    }
    public Iterable BiggestComponent() {
        Long maxNumberOfElements = 0L;
        ArrayList<Long> biggestComponent = new ArrayList<>();
        for(int i = 0; i < components.size(); i++) {
            Long longestPathOfAComponent = BFS(components.get(i).get(0));
            for(int j = 1; j < components.get(i).size(); j++) {
                Long nr = BFS(components.get(i).get(j));
                if(nr > longestPathOfAComponent)
                    longestPathOfAComponent = nr;
            }
            if(longestPathOfAComponent > maxNumberOfElements) {
                maxNumberOfElements = longestPathOfAComponent;
                biggestComponent = components.get(i);
            }
        }
        return biggestComponent::iterator;
    }
}
