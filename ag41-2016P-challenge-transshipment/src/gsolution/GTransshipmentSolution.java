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
package gsolution;

import gio.FichierEntree;
import gproblem.GEdge;
import gproblem.GNode;
import gproblem.GTransshipmentProblem;

import java.util.*;


/**
 * 
 * @author Olivier Grunder
 * @version 0.01
 * @date 7 march 2016
 *
 */
public class GTransshipmentSolution {

	protected GTransshipmentProblem problem;

	private int[] tabAssignment=null ;

	protected double evaluation;

	/**
	 * @return the slpb
	 */
	public GTransshipmentSolution(GTransshipmentProblem pb) {
		assert pb!=null : "GTransshipmentSolution(): pb="+problem ;
		
		problem = pb ;
		evaluation = -1 ;
		if (problem.getNbrEdges()>0)
			tabAssignment = new int[problem.getNbrEdges()+1];
		else
			tabAssignment = null ;
	}

	/**
	 * 
	 * @param gTransshipmentSolution
	 */
	public GTransshipmentSolution(GTransshipmentSolution sol) {
		problem = sol.problem ;
		evaluation = sol.evaluation ;
		tabAssignment = new int[sol.tabAssignment.length] ;
		for (int i=0;i<sol.tabAssignment.length;i++) {
			tabAssignment[i] = sol.tabAssignment[i] ;
		}
	}

	/**
	 * 
	 */
	public Object clone() {
		return new GTransshipmentSolution(this);
		
	}

	/**
	 * @return the slpb
	 */
	public GTransshipmentProblem getSupplyLinkProblem() {
		return problem;
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
	 * @return the evaluation
	 */
	public double getEvaluation() {
		return evaluation;
	}


	/**
	 * @param evaluation the evaluation to set
	 */
	public void setEvaluation(double evaluation) {
		this.evaluation = evaluation;
	}


	/**
	 *  Warning : Edge indices start from 1!!!
	 * @param i
	 * @param qty
	 */
	public void setAssignement(int i, int qty) {
		if (qty<0) qty=-qty ;
		tabAssignment[i]=qty ;		
	}

	/**
	 *  Warning : Edge indices start from 1!!!
	 * @param i
	 */
	public int getAssignement(int i) {
		return tabAssignment[i] ;		
	}


	/**
	 * Evaluation of a solution
	 * @return
	 */
	public double evaluate() {
		evaluation = 0 ;
		
		assert tabAssignment!=null: "GTransshipmentSolution.evaluate(): tabAssignment=null" ;
		
		// Edges costs
		for (int i=0;i<problem.getNbrEdges();i++) {
			GEdge edge=problem.getEdge(i) ;
			assert edge.getIndice()<tabAssignment.length: "GTransshipmentSolution.evaluate(): edge.getIndice()="+edge.getIndice()+" < tabAssignment.length("+tabAssignment.length+")" ;
			int qty = tabAssignment[edge.getIndice()] ;
			if (qty>0) 
				evaluation += edge.getFixedCost() + edge.getUnitCost()*qty ;
		}

		// Platforms costs
		for (int i=0;i<problem.getNbrNodes();i++) {
			GNode node=problem.getNode(i) ;
			// if it is a platform
			if (node.getDemand()==0) {
				// How many products cross the platform
				int qty = 0 ;
				for (GEdge edge: node.getTabEdges()) {
					qty += tabAssignment[edge.getIndice()] ;
				}
				if (qty>0) 
					evaluation += node.getCost()*qty ;
				
			}
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GTransshipmentSolution [evaluation=" + evaluation + ", tabAssignment="
				+ Arrays.toString(tabAssignment) + " ]";
	}

	
	/**
	 * Warning : Edge indices start from 1!!!
	 * @param tab
	 */
	public void setTabAssignment(int[] tab) {
		this.tabAssignment = tab ;
		
	}

	
	


}