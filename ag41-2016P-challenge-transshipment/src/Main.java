/**
 * 
 *     This file is part of ag41-2016P-challenge-transshipment
 *     
 *     ag41-2016P-challenge-transshipment is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     
 *     ag41-2016P-challenge-transshipment is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     
 *     You should have received a copy of the GNU General Public License
 *     along with ag41-2016P-challenge-transshipment.  If not, see <http://www.gnu.org/licenses/>.
 *     
 */

import gproblem.GTransshipmentProblem;
import gsolution.GTransshipmentSolution;
import gsolver.*;

/**
 * 
 * @author Olivier Grunder
 * @version 0.03
 * @date 22 avril 2011
 *
 */
public class Main {
	
	// challenge file names
	public static final String[] challengeFilenames = {
		"data/transshipment1.txt"} ;
	
	
	//	- 10 secondes pour les petites instances 
	//	- 30 secondes pour les moyennes instances 
	//	- 1 minutes pour les grandes instances 
	public static final long[] solvingTime = {
		10000} ;


	/**
	 * 
	 */
	private static void help() {
		System.out.println("") ;
		System.out.println("Usage: script/run n") ;
		System.out.println("") ;
		System.out.println("with:") ;
		
		for (int i=0;i<challengeFilenames.length;i++) {
			System.out.println("      n="+i+" for instance: "+challengeFilenames[i]) ;
		}
		System.out.println("") ;
		
	}


	
	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		if (args.length>0) {
			System.out.println("args[0]="+args[0]) ;
			int c = new Integer(args[0]).intValue() ;
			
			if (c>=0 && c<challengeFilenames.length) {
				System.out.println ("Loading challenge file: "+challengeFilenames[c] ) ;
				GTransshipmentProblem pb = new GTransshipmentProblem(challengeFilenames[c]) ;
				
				// Evaluation of a solution
				GTransshipmentSolution sol = new GTransshipmentSolution(pb) ;
				int[] tabAssignt = {-1,2,0,2,4,3,1,1,2,3,2,0,4,1,1,0,3,1,1,5,0,0} ; // Edge indices start from 1 indice!!!
				sol.setTabAssignment(tabAssignt) ;
				sol.evaluate() ;
				System.out.println("\nsolution sol="+sol.toString() +"\n") ;

				// New solver
				GSolver solv = 	new MySolver(pb) ;
				solv.setSolvingTime(solvingTime[c]);
				solv.start() ;
				
				
			}
			else
				help() ;
		}
		else
			help() ;
		
	}



}
