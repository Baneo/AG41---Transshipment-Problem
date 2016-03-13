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
package gsolver;

import gio.GLog;
import gproblem.GTransshipmentProblem;
import gsolution.GTransshipmentSolution;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * A solver
 * 
 * @author Olivier Grunder
 * @version 0.03
 * @date 11 mai 2011
 *
 */
public abstract class GSolver extends Thread {

	
	protected GTransshipmentProblem problem ;
	protected GTransshipmentSolution bestSolution=null ;
	protected GTransshipmentSolution currentSolution=null ;
	
	protected long startTime=0;
	private long endTime=0;
	private long elapsedTime=0;
	
	/**
	 * maximum time to run the solver
	 */
	protected long solvingTime=5000 ;
	
	/**
	 * random generator to use for stochastic techniques
	 */
	protected Random rand ;
	
	protected Thread thread = null ;
	
	static public GLog log= new GLog("gsolver") ;
 

	/**
	 * Constructor
	 */
	public GSolver() {
		rand = new Random() ;
	}

	/**
	 * Constructor
	 * @param problem
	 */
	public GSolver(GTransshipmentProblem problem) {
		this.problem = problem ;
		rand = new Random() ;
	}

	/**
	 * Constructor
	 * @param problem
	 * @param bestSolution
	 */
	public GSolver(GTransshipmentProblem problem, GTransshipmentSolution bestSolution) {
		this.problem = problem ; this.bestSolution = bestSolution ;
		rand = new Random() ;
	}
	
	/**
	 * @return the timeElapse
	 */
	public long getElapsedTime() {
		long time = new Date().getTime() ; 
		elapsedTime = time-startTime ;
		return elapsedTime;
	}

	/**
	 * @return the solvingTime
	 */
	public long getSolvingTime() {
		return solvingTime;
	}

	/**
	 * @param solvingTime the solvingTime to set
	 */
	public void setSolvingTime(long solvingTime) {
		this.solvingTime = solvingTime;
	}

	/**
	 * @return the problem
	 */
	public GTransshipmentProblem getProblem() {
		return problem;
	}


	/**
	 * @param problem the problem to set
	 */
	public void setProblem(GTransshipmentProblem problem) {
		this.problem = problem;
	}


	/**
	 * @return the bestSolution
	 */
	public GTransshipmentSolution getBestSolution() {
		return bestSolution;
	}


	/**
	 * @param bestSolution the bestSolution to set
	 */
	public void setBestSolution(GTransshipmentSolution bestSolution) {
		this.bestSolution = bestSolution;
	}


	/**
	 * @return the rand
	 */
	public Random getRand() {
		return rand;
	}


	/**
	 * @param rand the rand to set
	 */
	public void setRand(Random rand) {
		this.rand = rand;
	}
	
	/**
	 * Stops the solver after a given duration
	 * 
	 * @author ogrunder
	 *
	 */
	public class GStopSolver extends Thread {
		public void run() {
			boolean encore=true ;
			while (encore) {
				if (thread==null || !thread.isAlive()) encore = false ;
				long elapsedTime = getElapsedTime() ;
				if (elapsedTime>=solvingTime) 
					encore = false ;
				else {
					try {
						Thread.currentThread().sleep(200) ;
					} catch (Exception e) {}						
				}				
			}
			stopSolver() ;
		}
	}

	@Override
	public void run() {
		thread = this ;
		startTime = new Date().getTime() ; 
		new GStopSolver().start() ;
		solve() ;
	}

	/**
	 * solves the problem
	 */
	abstract protected void solve()  ;
	
	/**
	 * stops the solver
	 */
	protected void stopSolver() {
		this.stop();
		log.println ("END OF THE SOLVING TIME : " + getElapsedTimeString()) ;
		assert bestSolution!=null : "stopSolver(): bestSolution="+bestSolution ;
		log.println ("bestSolution = "+bestSolution.getEvaluation()) ;
	}
	
	/**
	 * Get the elapsed time in the format hr min sec 
	 * @return the 
	 */
	public String getElapsedTimeString() {
		return getTimeString(getElapsedTime()) ;
	  }
	
	/**
	 * Get a string from a duration in milliseconds
	 * 
	 * @param ms
	 * @return string
	 */
	public String getTimeString(long ms) {
	    long sec  = ms / 1000 ;
	    ms -= sec * 1000 ;
	  
	    if (sec==0) return new String (ms+"ms") ;
	  
	    long min =  sec / 60 ;
	    sec -= min * 60 ;
	    if (min==0) return new String (sec + "s " + ms+"ms") ;
	  
	    long heure = min / 60 ;
	    min -= heure * 60 ;
	    if (heure==0) return new String (min + "m " + sec + "s") ;
	
	    long jour = heure / 24 ;
	    heure -= jour * 24 ;
	    if (jour==0) return new String (heure + "h " + min + "m" ) ;
	    
	    return new String (jour + "d " + heure + "h" ) ;
	}



}
