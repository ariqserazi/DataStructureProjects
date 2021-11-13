package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	
    public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
        ArrayList<String> shortChain = new ArrayList<>();
        if (g == null || p1 == null || p2 == null || p1.length() == 0 || p2.length() == 0) {
            return null;
        }
        p1 = p1.toLowerCase();
        p2 = p2.toLowerCase();
        if (p1.equals(p2)) {
            shortChain.add(g.members[g.map.get(p1)].name);
            return shortChain;
        }
        if (g.map.get(p1) == null || g.map.get(p2) == null) {
            return null;
        }
        Queue<Integer> q = new Queue<>();
        int[] high = new int[g.members.length];
        int[] low = new int[g.members.length];
        boolean[] vertex = new boolean[g.members.length];
        for (int i = 0; i < vertex.length; i++) {
            vertex[i] = false;
            high[i] = Integer.MAX_VALUE; 
            low[i] = -1;
        }
        int fp = g.map.get(p1);
        vertex[fp] = true;
        high[fp] = 0; 
        q.enqueue(fp);
        while (!q.isEmpty()) {
            int dq = q.dequeue(); 
            Person nextPerson = g.members[dq];
            for (Friend f = nextPerson.first; f != null; f=f.next) {
                int fnum = f.fnum;
                if (!vertex[fnum]){
                    high[fnum] = high[dq]+1; 
                    low[fnum] = dq;
                    vertex[fnum] = true;
                    q.enqueue(fnum); 
                }
            }
        }
        Stack<String> s = new Stack<>();
        int Sp = g.map.get(p2);
        if (!vertex[Sp]) {
            return null;
        }
        while(Sp != -1) {
            s.push(g.members[Sp].name);
            Sp = low[Sp];
        }
        while (!s.isEmpty()) {
            shortChain.add(s.pop());
        }
        return shortChain;
    }
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		  ArrayList<ArrayList<String>> Clique = new ArrayList<>();
		  if (g == null || school == null || school.length() == 0) {
			  return null;
		  }
		  school = school.toLowerCase();
		  boolean[] vertex = new boolean[g.members.length];
		  for (int i = 0; i < vertex.length; i++) {
			vertex[i] = false;
		  }
		  for (Person member : g.members) {
			  if (!vertex[g.map.get(member.name)] && member.school != null && member.school.equals(school)) {
				  Queue<Integer> q = new Queue<>();
				  ArrayList<String> clique2 = new ArrayList<>();
				  int start = g.map.get(member.name);
				  vertex[start] = true;
				  q.enqueue(start);
				  clique2.add(member.name);
				  while (!q.isEmpty()) {
					  Person nextPerson = g.members[q.dequeue()];
					  for (Friend f = nextPerson.first; f != null; f=f.next) {
						  int num = f.fnum;
						  Person p = g.members[num];
						  if (!vertex[num] && p.school != null && p.school.equals(school)){
							  vertex[num] = true;
							  q.enqueue(num);
							  clique2.add(g.members[num].name);
						  }
					  }
				  }
				  Clique.add(clique2);
			  }
		  }
  
		  return Clique;
	  }
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
    public static ArrayList<String> connectors(Graph g) {
        boolean[] vertex = new boolean[g.members.length]; 
        int[] dfsPos = new int[g.members.length];
        int[] returnPos = new int[g.members.length];
        ArrayList<String> connector = new ArrayList<>();
        for (Person member : g.members) {
            if (!vertex[g.map.get(member.name)]){
                dfsPos = new int[g.members.length];
                dfs(g.map.get(member.name), g.map.get(member.name), g, vertex, dfsPos, returnPos, connector);
            }
        }
        for (int i = 0; i < connector.size(); i++) {
            Friend f = g.members[g.map.get(connector.get(i))].first;
            int count = 0;
            while (f != null) {
                f = f.next;
                count++;
            }
            if (count == 0 || count == 1) {
                connector.remove(i);
            }
        }
        for (Person member : g.members) {
            if ((member.first.next == null && !connector.contains(g.members[member.first.fnum].name))) {
                connector.add(g.members[member.first.fnum].name);
            }
        }

        return connector;
    }
    private static void dfs(int member2, int member1, Graph g, boolean[] visited, int[] dfsPos, int[] returnPos, ArrayList<String> connector){
        Person p = g.members[member2];
        visited[g.map.get(p.name)] = true;
		int count = 0;
        for (int i = 0; i < dfsPos.length; i++) {
            if (dfsPos[i] != 0) {
                count++;
            }
        }
        count = count+1;
        if (dfsPos[member2] == 0 && returnPos[member2] == 0) {
            dfsPos[member2] = count;
            returnPos[member2] = dfsPos[member2];
        }
        for (Friend f = p.first; f != null; f = f.next) {
            if (!visited[f.fnum]) {
                dfs(f.fnum, member1, g, visited, dfsPos, returnPos, connector);
                if (dfsPos[member2] > returnPos[f.fnum]) {
                    returnPos[member2] = Math.min(returnPos[member2], returnPos[f.fnum]);
                } else {
                    if (Math.abs(dfsPos[member2]-returnPos[f.fnum]) < 1 && Math.abs(dfsPos[member2]-dfsPos[f.fnum]) <=1 && returnPos[f.fnum] ==1 && member2 == member1) {
                        continue;
                    }
                    if (dfsPos[member2] <= returnPos[f.fnum] && (member2 != member1 || returnPos[f.fnum] == 1 )) { 
                        if (!connector.contains(g.members[member2].name)) {
                            connector.add(g.members[member2].name);
                        }
                    }

                }
            } else {
                returnPos[member2] = Math.min(returnPos[member2], dfsPos[f.fnum]);
            }
        }
        return;
    }

}

