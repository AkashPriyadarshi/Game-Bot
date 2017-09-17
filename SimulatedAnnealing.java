import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulatedAnnealing {
	Map<Integer,List<Integer>> treeLoc = new HashMap<Integer,List<Integer>>();
	private final String inputfileName="input20.txt";
	private final String outputfileName="output.txt";

	private  String algoType;
	private  int matrixSize, lizards, DFSlizardPlaced,matrix[],DFSMatrix[];
	
	private int result[],DFSlizardLoc[] = null;
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub		
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing();
		simulatedAnnealing.init();			
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
		DFSlizardLoc = new int[lizards];
		DFSMatrix = matrix.clone();
		tempMatrix=runSA();
		//tempMatrix=runBFS(new Node(new int[lizards],0,0));
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
	
	private List<Integer> allConflictingPos(int[] randomMatrix,List<Integer> pos, List<Integer> conflictingPos){
		int conflicts;
		for(Integer lizardposition : pos) {
			conflicts=getConlicts(randomMatrix,lizardposition);
			if(conflicts>0)conflictingPos.add(lizardposition);				
		}
		return conflictingPos;
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
