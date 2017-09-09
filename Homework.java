import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Homework {
	class Node{
		int lizardPlaced,index;//, lizardPosition[];
		private Set<Integer> lizardLoc = null;
		Node(Set<Integer> lizardLoc, int lizardPlaced, int index){
			this.lizardPlaced=lizardPlaced;
			this.index = index;
			this.lizardLoc = new HashSet<Integer>(lizardLoc);
		}
	}
	
	Map<Integer,List<Integer>> treeLoc = new HashMap<Integer,List<Integer>>();
	private final String inputfileName="input7.txt";
	private final String outputfileName="output.txt";

	private  String algoType;
	private  int matrixSize, lizards,matrix[],DFSMatrix[];
	


	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		Homework hw = new Homework();
		hw.init();			
	}

	private void init() throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(inputfileName));
		Set<Integer> tempMatrix=null;
		List<Integer> tempList= null;
		String temp=null;	
		algoType  = br.readLine().trim();
		matrixSize= Integer.parseInt(br.readLine()); 
		lizards   =	Integer.parseInt(br.readLine());
		matrix    = new int[matrixSize*matrixSize];
		for(int i=0;i<matrixSize;i++) {
			temp = br.readLine();
			for(int j=0;j<matrixSize;j++) {
				if(temp.charAt(j)=='2') {
					matrix[i*matrixSize+j]=2;
					if(treeLoc.containsKey(i))tempList= treeLoc.get(i);
					else tempList = new ArrayList<Integer>();
					tempList.add(i*matrixSize+j);
					treeLoc.put(i,tempList);
				}
			}
		}
		br.close();
		DFSMatrix = matrix.clone();
		tempMatrix=runDFS(new Node(new HashSet<Integer>(),0,0));
		writeFile(tempMatrix!=null, tempMatrix);
	}
	private void writeFile(boolean result, Set<Integer> lizardLoc) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputfileName));
		if(!result)bw.write("FAIL");
		else {
			bw.write("OK");
			Iterator<Integer> itr = lizardLoc.iterator();
			while(itr.hasNext())matrix[itr.next()]=1;
			
			for(int i=0;i<matrix.length;i++) {
				if(i%matrixSize==0)bw.newLine();

				if(matrix[i]==-1) bw.write("0");
				else bw.write(""+matrix[i]);
			}
		}
		bw.flush();
		bw.close();
	}

		private List<Integer> invalidPlaces(int[] matrix, double index) {
		//horizontal right
		List<Integer> locUpdated= new ArrayList<Integer>();
		double row = Math.ceil((index+1)/matrixSize)*matrixSize;		
		for(int j = (int)index+1;j<row;j++)
			if(matrix[j]==2)break;
			else if(matrix[j]==0) {
				matrix[j]=-1;
				locUpdated.add(j);
			}
					

		for(int j= (int)index+matrixSize;j<matrix.length;j+=matrixSize)
			if(matrix[j]==2)break;
			else if(matrix[j]==0) {
				matrix[j]=-1;
				locUpdated.add(j);
			}	

		for(int x = (int)(index%matrixSize)+1, y=(int)(index/matrixSize)+1,pos=0;x<matrixSize && y<matrixSize;x++,y++) {
			pos=y*matrixSize+x;
			if(matrix[pos]==2)break;
			else if(matrix[pos]==0) {
				matrix[pos]=-1;
				locUpdated.add(pos);
			}
		}

		for(int x = (int)(index%matrixSize)-1, y=(int)(index/matrixSize)+1, pos=0;x>=0 && y<matrixSize;x--,y++) {
			pos = y*matrixSize+x;
			if(matrix[pos]==2)break;
			else if(matrix[pos]==0) {
				matrix[pos]=-1;
				locUpdated.add(pos);
			}
		}
		return locUpdated;
	}

	private boolean isSafe(Set<Integer> lizardLoc, int index) {
		//left side of position
		if(matrix[index]==2)return false;
		double row = (index/matrixSize)*matrixSize;	
		for(int i = (int)index-1;row<=i;i--)
			if(lizardLoc.contains(i))return false;
			else if(matrix[i]==2)break;

		//top of position
		for(int i= (int)index-matrixSize;0<=i;i-=matrixSize)
			if(lizardLoc.contains(i))return false;
			else if(matrix[i]==2)break;

		//left top of position
		for(int x = (int)(index%matrixSize)-1, y=(int)(index/matrixSize)-1;0<=x && 0<=y;x--,y--)
			if(lizardLoc.contains(x+y*matrixSize))return false;
			else if(matrix[x+y*matrixSize]==2)break;

		//right top of position
		for(int x = (int)(index%matrixSize)+1, y=(int)(index/matrixSize)-1;x<matrixSize && 0<=y;x++,y--)
			if(lizardLoc.contains(x+y*matrixSize))return false;
			else if(matrix[x+y*matrixSize]==2)break;
		
		return true;
	}

	private Set<Integer> runDFS(Node node) {		
		Set<Integer> result, lizardLoc=node.lizardLoc;
		List<Integer>locUpdated=null;		
		for(int i = node.index,index=0;i<matrix.length;i++) {
			if(DFSMatrix[i]==0) {
				lizardLoc.add(i);
				if((node.lizardPlaced+1)== lizards) {
					return lizardLoc;
				}
				index = treeLoc.containsKey(i/matrixSize)?i+1:(int)Math.ceil(((float)i+1)/matrixSize)*matrixSize;
				locUpdated = invalidPlaces(DFSMatrix, i);
				result = runDFS(new Node(lizardLoc,node.lizardPlaced+1,index));
				for(Integer pos:locUpdated)DFSMatrix[pos]=0;
				if(result!=null)return result;
				lizardLoc.remove(i);
			}
		}
		return null;
	}
	
	private Set<Integer> runBFS(Node node) {
		int rowEnd,tempMatrix[]=null;
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(node);
		while(!queue.isEmpty()) {
			node = queue.poll();
			tempMatrix=matrix.clone();
			for(Integer loc:node.lizardLoc)invalidPlaces(tempMatrix, loc);
			rowEnd = (int)Math.ceil(((float)node.index+1)/matrixSize)*matrixSize;
			for(int i=node.index;i<rowEnd && rowEnd<=matrix.length;i++) {
				if(tempMatrix[i]==0) {
					node.lizardLoc.add(i);
					if((node.lizardPlaced+1)== lizards) {
						return node.lizardLoc;
					}
					if(treeLoc.containsKey(i/matrixSize)) {
						for(Integer loc:treeLoc.get(i/matrixSize)) {
							if(loc>i)
								queue.add(new Node(node.lizardLoc,node.lizardPlaced+1,loc));
						}
					}
					queue.add(new Node(node.lizardLoc,node.lizardPlaced+1,rowEnd));
					node.lizardLoc.remove(i);
				}
				
			}
		}
		return null;
	}

	/*if(isSafe(lizardLoc,i)) {
	lizardLoc.add(i);
	if((node.lizardPlaced+1)== lizards) {
		return lizardLoc;
	}
	if(treeLoc.containsKey(i/matrixSize) && i<treeLoc.get(i/matrixSize)+(i/matrixSize)*matrixSize) {					
		result = runDFS(new Node(lizardLoc,node.lizardPlaced+1,i+1));
	}else 
		result = runDFS(new Node(lizardLoc,node.lizardPlaced+1,(int)Math.ceil((float)(i+1)/matrixSize)*matrixSize));
	if(result!=null)return result;
	lizardLoc.remove(i);
}*/
	
/*	private int[] runBFS(Node node) {		
		int rowEnd,initialGrid[]=null;	
		List<Integer> lizardLoc=null;
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(node);
		boolean rowHasTree=false;
		while(!queue.isEmpty()) {			
			node = queue.poll();
			rowHasTree=false;
			initialGrid=matrix.clone();
			lizardLoc=node.lizardLoc;
			for(Integer loc : lizardLoc) {
				initialGrid[loc]=1;
				invalidPlaces(initialGrid, loc);
			}
			rowEnd = (matrixSize*((node.index/matrixSize)+1));
			for(int i = node.index;i<rowEnd && !rowHasTree;i++) rowHasTree = (initialGrid[i]==2);
			for(int i = node.index;i<rowEnd && rowEnd<initialGrid.length;i++) {
				if(initialGrid[i]==0) {
					if((node.lizardPlaced+1)== lizards) {
						initialGrid[i]=1;
						return initialGrid;
					}	
					lizardLoc.add(i);
					if(rowHasTree)queue.add(new Node(lizardLoc, node.lizardPlaced+1,i+1));	
					queue.add(new Node(lizardLoc, node.lizardPlaced+1,rowEnd));					
					lizardLoc.remove(lizardLoc.size()-1);
				}
			}
		}	
		return null;
	}*/

}
