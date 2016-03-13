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
package gproblem;

import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import gio.FichierEntree;
import gio.FichierSortie;

/**
 * 
 * Transshipment problem for the challenge AG41 of spring 2016
 * 
 * @author Olivier Grunder
 * @version 0.01
 * @date 7 march 2016
 *
 */
public class GTransshipmentProblem {
	
	public static final String TOKEN_NAME="NAME" ;
	public static final String TOKEN_NBR_NODES="NBR_NODES" ;
	public static final String TOKEN_NBR_EDGES="NBR_EDGES" ;
	public static final String TOKEN_NODE="NODE" ;
	public static final String TOKEN_EDGE="EDGE" ;
	public static final String TOKEN_T="T" ;
	
	/**
	 * Name of the instance
	 */
	protected String instanceName ;

	/**
	 * Vector of nodes
	 */
	protected GNode tabNodes[] ;

	/**
	 * Vector of edges
	 */
	protected GEdge tabEdges[] ;
	
	
	/**
	 * Maximum time to finish the deliveries of all products to the clients
	 */
	protected double t ;


	
	public GTransshipmentProblem(String problemFilename) {
		tabNodes = null ; 
		tabEdges = null ;
		t=-1.0 ;
		
		load (problemFilename) ;
		
		System.out.println("problem="+toString()) ; 
	}


	/**
	 * Open a problem description file
	 * 
	 * @param problemFilename name of the problem description file
	 * 
	 */
	public int load(String problemFilename) {
		System.out.println("GTransshipmentProblem.load("+problemFilename+")") ;

		if (problemFilename==null) return 1 ;
		
		int nbrNodes = 0 ;
		int nbrEdges = 0 ;
		tabNodes = null ; 
		tabEdges = null ;
		this.t=-1.0 ;

		FichierEntree fe = new FichierEntree (problemFilename) ;

		int itabnodes = 0 ;
		int itabedges = 0 ;
		int encore = 1 ;
		int lineNumber = 0 ;
		while (encore == 1) {
			lineNumber++ ;
			String s = fe.lireLigne() ;
			if (s==null) {
				break ;
			}

			if (s.charAt(0)!='#') {
				StringTokenizer st = new StringTokenizer(s, new String(":")) ;
				String sg = st.nextToken().trim() ;
	
				if (sg.compareToIgnoreCase(TOKEN_NAME)==0 ) { 
					instanceName = st.nextToken().trim()  ;
				}
	
				if (sg.compareToIgnoreCase(TOKEN_NBR_NODES)==0 ) { 
					nbrNodes = Integer.parseInt(st.nextToken().trim()) ;
					tabNodes = new GNode[nbrNodes] ;
				}
	
				if (sg.compareToIgnoreCase(TOKEN_NBR_EDGES)==0 ) { 
					nbrEdges = Integer.parseInt(st.nextToken().trim()) ;
					tabEdges = new GEdge[nbrEdges] ;
				}
	
				if (sg.compareToIgnoreCase(TOKEN_T)==0 ) { 
					this.t = Double.parseDouble(st.nextToken().trim()) ;
				}
	
				if (sg.compareToIgnoreCase(TOKEN_NODE)==0 ) { 
					String params = st.nextToken().trim() ;
					StringTokenizer st2 =  new StringTokenizer(params, new String(" ")) ;
					int nodeind = Integer.parseInt(st2.nextToken().trim()) ;
					int nodex = Integer.parseInt(st2.nextToken().trim()) ;
					int nodey = Integer.parseInt(st2.nextToken().trim()) ; 
					int nodeb = Integer.parseInt(st2.nextToken().trim()) ; 
					int nodeg = Integer.parseInt(st2.nextToken().trim()) ;
					int nodes = Integer.parseInt(st2.nextToken().trim()) ;
					
					tabNodes[itabnodes++] = new GNode(nodeind, nodex, nodey, nodeb, nodeg, nodes) ;
				}
	
				if (sg.compareToIgnoreCase(TOKEN_EDGE)==0 ) { 
					String params = st.nextToken().trim() ;
					StringTokenizer st2 =  new StringTokenizer(params, new String(" ")) ;
					
					int edgeind = Integer.parseInt(st2.nextToken().trim()) ;
					
					int edgei = Integer.parseInt(st2.nextToken().trim()) ;
					GNode nodei = getNodeWithIndiceAttribute(edgei) ;
					if (nodei==null) 
						printParsingError ("node with indice "+edgei+" does not exist in "+problemFilename+", line "+lineNumber) ;
	
					int edgej = Integer.parseInt(st2.nextToken().trim()) ;
					GNode nodej = getNodeWithIndiceAttribute(edgej) ;
					if (nodej==null) 
						printParsingError ("node with indice "+edgej+" does not exist in "+problemFilename+", line "+lineNumber) ;
					
					int edgeu = Integer.parseInt(st2.nextToken().trim()) ;
					int edgec = Integer.parseInt(st2.nextToken().trim()) ; 
					int edgeh = Integer.parseInt(st2.nextToken().trim()) ; 
					int edget = Integer.parseInt(st2.nextToken().trim()) ;
					
					tabEdges[itabedges++] = new GEdge(edgeind, nodei, nodej, edgeu, edgec, edgeh, edget) ;
				}
	
				if (sg.compareToIgnoreCase("EOF")==0 ) {
					encore=0 ;
				}
			}
		}

		fe.fermer() ;
		
		buildEdgesForNodes() ;

		return 0 ;

	}



	/**
	 * Define starting edges for every node
	 */
	private void buildEdgesForNodes() {
		for (GNode node: tabNodes) {
			int n = 0 ;
			for (GEdge edge: tabEdges) {
				if (edge.getStartingNode()==node) 
					n++ ;
			}
			node.setNbrEdges(n) ;

			int i = 0 ;
			for (GEdge edge: tabEdges) {
				if (edge.getStartingNode()==node) 
					node.setEdge(i++, edge) ;
			}
			
			
		}
		
	}


	/**
	 * Search for a node which indice attribute is "nodeindice"
	 * returns null if node does not exist
	 * 
	 * @param nodeindice : indice attribute of the node : 
	 * @return
	 */
	private GNode getNodeWithIndiceAttribute(int nodeindice) {
		for (GNode node: tabNodes) {
			if (node.getIndice()==nodeindice)
				return node ;
		}
		return null;
	}


	/**
	 * Error parsing instance file 
	 * @param msg
	 */
	private void printParsingError(String msg) {
		System.out.println("ERROR parsing instance file: "+msg) ;
		System.exit(0) ;
		
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GTransshipmentProblem [instanceName=" + instanceName
				+ ", tabNodes=" + Arrays.toString(tabNodes) + ", tabEdges="
				+ Arrays.toString(tabEdges) + ", T="+t + "]";
	}


	/**
	 * 
	 * @return
	 */
	public GNode[] getTabNodes() {
		return tabNodes ;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public GEdge[] getTabEdges() {
		return tabEdges ;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNbrNodes() {
		return tabNodes.length ;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public int getNbrEdges() {
		return tabEdges.length ;
	}


	public GEdge getEdge(int i) {
		return tabEdges[i];
	}
	
	public GNode getNode(int i) {
		return tabNodes[i];
	}


	/**
	 * @return the t
	 */
	public double getT() {
		return t;
	}


	/**
	 * @param t the t to set
	 */
	public void setT(double t) {
		this.t = t;
	}
	
	
	


}
