import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Homework {
	class Node{
		int lizardPlaced,index;
		private int[] lizardLoc = null;
		Node(int[] lizardLoc, int lizardPlaced, int index){
			this.lizardPlaced=lizardPlaced;
			this.index = index;
			this.lizardLoc = lizardLoc.clone();
		}
	}
	
	Map<Integer,List<Integer>> treeLoc = new HashMap<Integer,List<Integer>>();
	private final String inputfileName="input1.txt";
	private final String outputfileName="output.txt";

	private  String algoType;
	private  int matrixSize, lizards, DFSlizardPlaced,matrix[],DFSMatrix[];
	
	private int result[],DFSlizardLoc[] = null;

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		Homework hw = new Homework();
		hw.init();			
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
	}

	private void init() throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(inputfileName));
		int[] tempMatrix=null;
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
		if(algoType.equals("DFS")) {
			DFSlizardLoc = new int[lizards];
			DFSMatrix = matrix.clone();
			tempMatrix=runDFS(0);
		}else if(algoType.equals("BFS")) {
			tempMatrix=runBFS(new Node(new int[lizards],0,0));
		}else if(algoType.equals("SA")) {
			tempMatrix=runSA();
		}		
		writeFile(tempMatrix!=null, tempMatrix);
	}
	private void writeFile(boolean result, int[] lizardLoc) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputfileName));
		if(!result)bw.write("FAIL");
		else {
			bw.write("OK");
			for(int pos:lizardLoc)
				matrix[pos]=1;			
			
			for(int i=0;i<matrix.length;i++) {
				if(i%matrixSize==0)bw.newLine();

				if(matrix[i]==-1) bw.write("0");
				else bw.write(""+matrix[i]);
			}
		}
		bw.flush();
		bw.close();
	}

		private int[] invalidPlaces(int[] matrix, int index) {
		int row, locUpdated[], count=0;
		row = ((index/matrixSize)+1)*matrixSize;
		locUpdated = new int[row-index+ 3*(matrixSize - (index/matrixSize))];
		//horizontal right
		for(int j = (int)index+1;j<row;j++)
			if(matrix[j]==2)break;
			else if(matrix[j]==0) {
				matrix[j]=-1;
				locUpdated[count++]=j;
			}
					
		//vertical bottom
		for(int j= (int)index+matrixSize;j<matrix.length;j+=matrixSize)
			if(matrix[j]==2)break;
			else if(matrix[j]==0) {
				matrix[j]=-1;				
				locUpdated[count++]=j;
			}	

		for(int x = (int)(index%matrixSize)+1, y=(int)(index/matrixSize)+1,pos=0;x<matrixSize && y<matrixSize;x++,y++) {
			pos=y*matrixSize+x;
			if(matrix[pos]==2)break;
			else if(matrix[pos]==0) {
				matrix[pos]=-1;				
				locUpdated[count++]=pos;
			}
		}

		for(int x = (int)(index%matrixSize)-1, y=(int)(index/matrixSize)+1, pos=0;x>=0 && y<matrixSize;x--,y++) {
			pos = y*matrixSize+x;
			if(matrix[pos]==2)break;
			else if(matrix[pos]==0) {
				matrix[pos]=-1;				
				locUpdated[count++]=pos;
			}
		}
		return locUpdated;
	}
		
	private int[] runDFS(int startIndex) {
		int[] locUpdated=null;		
		for(int i = startIndex,index=0;i<matrix.length;i++) {
			if(DFSMatrix[i]==0) {
				DFSlizardLoc[DFSlizardPlaced]=i;
				DFSlizardPlaced++;
				if(DFSlizardPlaced== lizards) {
					return DFSlizardLoc;
				}
				index = treeLoc.containsKey(i/matrixSize)?i+1:((i/matrixSize)+1)*matrixSize;
				locUpdated = invalidPlaces(DFSMatrix, i);
				result = runDFS(index);
				for(int pos:locUpdated)DFSMatrix[pos]=0;
				if(result!=null)return result;
				DFSlizardPlaced--;				
			}
		}
		return null;
	}
	
	private int[] runBFS(Node node) {
		int rowEnd,tempMatrix[]=null;
		Queue<Node> queue = new LinkedList<Node>();
		queue.add(node);
		while(!queue.isEmpty()) {
			node = queue.poll();
			tempMatrix=matrix.clone();
			for(Integer loc:node.lizardLoc)invalidPlaces(tempMatrix, loc.intValue());			
			for(int i=node.index;i<matrix.length;i++) {
				if(tempMatrix[i]==0) {
					node.lizardLoc[node.lizardPlaced] =i;
					if((node.lizardPlaced+1)== lizards) {
						return node.lizardLoc;
					}
					if(treeLoc.containsKey(i/matrixSize)) {
						for(Integer loc:treeLoc.get(i/matrixSize)) {
							if(loc>i)
								queue.add(new Node(node.lizardLoc,node.lizardPlaced+1,loc));
						}
					}
					rowEnd = ((i/matrixSize)+1)*matrixSize;
					queue.add(new Node(node.lizardLoc,node.lizardPlaced+1,rowEnd));					
				}
				
			}
		}
		return null;
	}

	
	
	//=========================================Simulated Annealing=================================
	private int getConlicts(int[] randomMatrix,int index) {
		int rowend , rowStart,count=0;
		rowend = ((index/matrixSize)+1)*matrixSize;
		rowStart=((index/matrixSize))*matrixSize;
		//horizontal right
		for(int j = (int)index+1;j<rowend;j++)
			if(randomMatrix[j]==2)break;
			else if(randomMatrix[j]==1) count++;
		
		//horizontal left
		for(int j = (int)index-1;j>rowStart;j--)
			if(randomMatrix[j]==2)break;
			else if(randomMatrix[j]==1) count++;
		
		
		//vertical bottom
		for(int j= (int)index+matrixSize;j<matrix.length;j+=matrixSize)
			if(randomMatrix[j]==2)break;
			else if(randomMatrix[j]==1) count++;
		
		
		//vertical up
		for(int j= (int)index-matrixSize;0<=j;j-=matrixSize)
			if(randomMatrix[j]==2)break;
			else if(randomMatrix[j]==1) count++;
		
		//diagonal down right
		for(int x = (int)(index%matrixSize)+1, y=(int)(index/matrixSize)+1,pos=0;x<matrixSize && y<matrixSize;x++,y++) {
			pos=y*matrixSize+x;
			if(randomMatrix[pos]==2)break;
			else if(randomMatrix[pos]==1) count++;
		}
		
		//diagonal up left
		for(int x = (int)(index%matrixSize)-1, y=(int)(index/matrixSize)-1,pos=0;0<=x && 0<=y;x--,y--) {
			pos=y*matrixSize+x;
			if(randomMatrix[pos]==2)break;
			else if(randomMatrix[pos]==1) count++;
		}
		
		//diagonal up right
		for(int x = (int)(index%matrixSize)+1, y=(int)(index/matrixSize)-1, pos=0;x<matrixSize && y>=0;x++,y--) {
			pos = y*matrixSize+x;
			if(randomMatrix[pos]==2)break;
			else if(randomMatrix[pos]==1) count++;
		}
		
		//diagonal down left
		for(int x = (int)(index%matrixSize)-1, y=(int)(index/matrixSize)+1, pos=0;x>=0 && y<matrixSize;x--,y++) {
			pos = y*matrixSize+x;
			if(randomMatrix[pos]==2)break;
			else if(randomMatrix[pos]==1) count++;
		}
		return count;
	}
	
	private int getBoardConflicts(int[] randomMatrix, List<Integer> lizardpositions, List<Integer> tempNewConflictposition) {
		int runningconflicts,conflicts;
		runningconflicts=conflicts=0;
		for(Integer lizardPosition:lizardpositions) {			
			conflicts = getConlicts(randomMatrix,lizardPosition);
			if(conflicts>0)tempNewConflictposition.add(lizardPosition);
			runningconflicts+=conflicts;
		}
		return runningconflicts;
	}
	
	
	private int[] runSA() {
		long currentTime = System.currentTimeMillis();
		int conflicts,oldTotalConflicts,newTotalConflicts,conflictIndex, lizardCount, randomMatrix[];
		double iteration,probability;
		List<Integer> conflictingPos = new ArrayList<Integer>();
		List<Integer> lizardposition = new ArrayList<Integer>();
		List<Integer> tempNewConflictposition = new ArrayList<Integer>();
		
		
		lizardCount=lizards;
		randomMatrix = matrix.clone();
		Integer oldPosition, newPosition;
		iteration =1;
		newTotalConflicts=0;
		
		//get board
		while(lizardCount!=0) {
			newPosition = (int)(Math.random()*(randomMatrix.length-1));
			if(randomMatrix[newPosition]==0) {
				randomMatrix[newPosition]=1;
				lizardposition.add(newPosition);
				lizardCount--;
			}	
		}
		//======================================================
		
		//get conflicts first time
		for(Integer position : lizardposition) {			
			conflicts=getConlicts(randomMatrix,position);
			if(conflicts>0) {
				newTotalConflicts+=conflicts;
				conflictingPos.add(position);	
			}			
		}
		//getBoardConflicts		
		oldTotalConflicts = newTotalConflicts;
		
		//loop till all conflict resolved
		do {
			if(System.currentTimeMillis()-currentTime > 280*1000)return null;
			iteration++;	
			conflictIndex =  (int)(Math.random()*(conflictingPos.size()));
			oldPosition = conflictingPos.get(conflictIndex);
			newPosition = (int)(Math.random()*(randomMatrix.length));
			if(randomMatrix[newPosition.intValue()]!=0)
				continue;
			randomMatrix[oldPosition.intValue()]=0;
			randomMatrix[newPosition.intValue()]=1;
			lizardposition.remove(Integer.valueOf(oldPosition));
			lizardposition.add(Integer.valueOf(newPosition));			
			tempNewConflictposition.clear();
			newTotalConflicts=getBoardConflicts(randomMatrix,lizardposition,tempNewConflictposition);//getConlicts(randomMatrix,newPosition);
			if(newTotalConflicts-oldTotalConflicts>0) {
				probability = Math.exp(((oldTotalConflicts-newTotalConflicts)*iteration)/100);
				iteration = iteration>=5000?1:iteration;
				if(probability<Math.random()) {
					randomMatrix[oldPosition.intValue()]=1;
					randomMatrix[newPosition.intValue()]=0;
					lizardposition.remove(Integer.valueOf(newPosition));
					lizardposition.add(Integer.valueOf(oldPosition));	
					continue;
				}
			}
			conflictingPos.clear();
			conflictingPos = new ArrayList<Integer>(tempNewConflictposition);
			oldTotalConflicts = newTotalConflicts;//getConlicts(randomMatrix,oldPosition);	
		}while(!conflictingPos.isEmpty());
		int[] result = new int[lizards];
		for(int i=0;i<lizardposition.size();i++)result[i]=lizardposition.get(i);
		return result;
	}
}
