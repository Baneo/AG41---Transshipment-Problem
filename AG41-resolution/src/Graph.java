import java.util.ArrayList;

/**
 * Created by delan on 22/04/16.
 */
public class Graph
{
    private String name;
    private int time;
    private int nb_nodes; //nombre de nodes
    private int nb_edges; //nombre d'edges
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    public Graph()
    {
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
    }


    public void setName(String name)
    {
        this.name = name;
    }

    public void addEdge(Edge edge)
    {
        edges.add(edge);
    }

    public void addNode(Node node)
    {
        nodes.add(node);
    }
    public Node getNode(int nbr)
    {
        System.out.println("Envoi du node " + nbr);
        return nodes.get(nbr - 1);/* On met -1 sinon ca merde avec
        la récupération, les nodes vont de 1 à 10 alors que l'arraylist
        va de 0 a 9, du coup voila :)
        */
    }
    public void printNode(int nbr)
    {
        Node node = getNode(nbr);
        System.out.println("Données du node" + (nbr)+ ":\n" +
                "Number :" +node.getNumber() + "\n X :"+ node.getX() +
                "\nY :" + node.getY() + "\n demand:" + node.getDemand() +
        "\n cost: " + node.getCost() + "\n time: " + node.getTime());
    }

}
