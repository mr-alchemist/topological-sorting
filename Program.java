

import java.util.Arrays;

import storage.FactorArray;

public class Program {

	public static void main(String[] args) {
		Program program = new Program();
		program.run();

	}
	
	void run() {
		int[][] vector = new int[][] {
			{ 2,12,-1,-1},
			{12,-1,-1,-1},
			{-1,-1,-1,-1},
			{ 2,-1,-1,-1},
			{ 2, 8, 9,-1},
			{ 3,10,11,-1},
			{10,-1,-1,-1},
			{ 1, 3, 5, 6},//7
			{ 0,13,-1,-1},
			{ 0, 6,11,-1},
			{ 2,-1,-1,-1},//10
			{-1,-1,-1,-1},
			{ 2,-1,-1,-1},
			{ 5,-1,-1,-1}
		};
		
		int[][] matrix = getMatrixByVectorDirected(vector);
		int[][] demLevels = demukron(matrix);//используем алгоритм Демукрона
		int[] tarjanRes = tarjan(vector);//алгоритм Тарьяна
		
		
		System.out.println("Demucron:");
		for(int i = 0; i < demLevels.length ;i++) {
			System.out.println(Arrays.toString(demLevels[i]) );
		}
		System.out.println("");
		System.out.println("Tarjan:");
		for(int i = 0; i < tarjanRes.length ;i++) {
			System.out.println( tarjanRes[i] );
		}
		
	}
	
	int[][] getMatrixByVectorDirected(int[][] vector){
		int N = vector.length;
		int[][] res = new int[N][N];
		for(int i = 0; i < N;i++)
			Arrays.fill(res[i], 0);
		
		for(int v = 0; v < N; v++) {
			for(int j = 0; j < vector[v].length; j++) {
				if(vector[v][j] == -1)break;
				int p = vector[v][j];
				res[v][p] = 1;
			}
		}
		
		return res;
	}
	
	int[][] demukron(int[][] matrix) {
		int N = matrix.length;
		boolean[] deleted = new boolean[N];
		Arrays.fill(deleted, false);
		int[] sIn = new int[N];
		for(int i = 0; i < N; i++) {//перебор столбцов матрицы
			int s = 0;
			for(int j = 0; j < N; j++) {
				s+= matrix[j][i];
			}
			sIn[i] = s;
		}
		FactorArray<FactorArray<Integer>> faLevels = new FactorArray<FactorArray<Integer>>();
		
		int delCnt = 0;
		while(delCnt < N) {
			FactorArray<Integer> curLevel = new FactorArray<Integer>();
			faLevels.add(curLevel);
			for(int i = 0; i < N ; i++) {
				if(sIn[i] == 0 && !deleted[i]) {
					curLevel.add(i);
					deleted[i] = true;
					delCnt++;
				}
			}
			
			for(int i = 0; i < curLevel.size(); i++) {//"вычитаем" из sIn строки матрицы, соответствующие вершинам текущего уровня
				int v = curLevel.get(i);
				for(int j = 0; j < N ; j++) {//идем по "столбцам"
					sIn[j] -= matrix[v][j];
				}
			}
		}
		
		//преобразуем результат типа FactorArray<FactorArray<Integer>> в int[][]
		int maxLevelSize = 0;
		for(int i = 0;i < faLevels.size();i++) {
			int curSize = faLevels.get(i).size();
			if(curSize > maxLevelSize) maxLevelSize = curSize;
		}
		int[][] levels = new int[faLevels.size()][maxLevelSize];
		for(int i = 0;i < faLevels.size();i++) {
			FactorArray<Integer> curLevel = faLevels.get(i);
			for(int j = 0;j < curLevel.size(); j++) 
				levels[i][j] = curLevel.get(j);
			
			for(int j = curLevel.size(); j < maxLevelSize; j++)
				levels[i][j] = -1;
		}
		return levels;
		
	}
	
	int[] tarjan(int[][] vector) {
		FactorArray<Integer> faRes = walkWithDFS(vector);
		int[] res = new int[faRes.size()];
		for(int i = 0; i < res.length; i++)
			res[i] = faRes.get(res.length - i - 1);
		return res;
	}
	
	FactorArray<Integer> walkWithDFS(int[][] vector) {
		
		boolean[] visited = new boolean[vector.length];
		Arrays.fill(visited, false);
		FactorArray<Integer> order = new FactorArray<Integer>();
		for(int v = 0; v < visited.length; v++) {
			if(!visited[v])
				DFS(v, vector, visited, order);
		}
		return order;
	}
	
	void DFS(int u, int[][] vector, boolean[] visited, FactorArray<Integer> order) {
		visited[u] = true;
		for(int i = 0; i < vector[0].length && vector[u][i] != -1 ; i++) {
			int w = vector[u][i];
			if( !visited[w] )
				DFS(w, vector, visited, order);
		}
		//System.out.println(u);
		order.add(u);//вершина u добавляется в список order(т.е. закрашивается чёрным) только после того, когда обработаны (закрашены чёрным) все зависимые от неё вершины
	}

}
