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

import java.util.Vector;

import javax.swing.text.TabableView;

import gproblem.GNode;
import gproblem.GTransshipmentProblem;
import gsolution.GTransshipmentSolution;
import gsolver.GSolver;


/**
 * 
 * @author Olivier Grunder
 * @version 0.01
 * @date 7 march 2016
 *
 */
public class MySolver extends GSolver {
	

	/**
	 * 
	 */
	public MySolver() {
		super();
	}

	/**
	 * @param problem
	 */
	public MySolver(GTransshipmentProblem problem) {
		super(problem);
	}

	/**
	 * solves the problem
	 */
	/* (non-Javadoc)
	 * @see gsolver.GSolver#solve()
	 */
	protected void solve() {
		System.out.println("This is mysolver...") ;
		bestSolution = null ;
		currentSolution = null ;
	}

}
