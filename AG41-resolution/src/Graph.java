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
    private static boolean DEBUG = false;

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
        edges.add(new Edge(edge));
    }

    public void addNode(Node node)
    {

        nodes.add(new Node(node));
    }
    public Node getNode(int nbr)
    {
        if(DEBUG) System.out.println("Envoi du node " + nbr);
        return nodes.get(nbr - 1);/* On met -1 sinon ca merde avec
        la récupération, les nodes vont de 1 à 10 alors que l'arraylist
        va de 0 a 9, du coup voila :)
        */
    }
    public void printNode(int nbr)
    {
        if(DEBUG) {
            Node node = getNode(nbr);
            System.out.println("Données du node" + (nbr) + ":\n" +
                    "Number :" + node.getNumber() + "\n X :" + node.getX() +
                    "\nY :" + node.getY() + "\n demand:" + node.getDemand() +
                    "\n cost: " + node.getCost() + "\n time: " + node.getTime());
        }
    }

    public ArrayList<Node> getFournisseurs()
    {
        ArrayList<Node> fournisseurs = new ArrayList<>();
        for(Node node : this.nodes)
        {
            if (node.getDemand() < 0)
            {
                fournisseurs.add(node);
            }

        }
        return fournisseurs;
    }

    public Node getFournisseurs(int index)
    {
        ArrayList<Node> fournisseurs = new ArrayList<>();
        for(Node node : this.nodes)
        {
            if (node.getDemand() < 0)
            {
                fournisseurs.add(node);
            }

        }
        return fournisseurs.get(index);
    }

    public ArrayList<Node> getClients()
    {
        ArrayList<Node> clients = new ArrayList<>();
        for(Node node : this.nodes)
        {
            if (node.getDemand() > 0)
            {
                clients.add(node);
            }

        }
        return clients;
    }

    public Node getClients(int index)
    {
        ArrayList<Node> clients = new ArrayList<>();
        for(Node node : this.nodes)
        {
            if (node.getDemand() > 0)
            {
                clients.add(node);
            }

        }
        return clients.get(index);
    }

    public ArrayList<Node> getPlateformes()
    {
        ArrayList<Node> plateforme = new ArrayList<>();
        for(Node node : this.nodes)
        {
            if (node.getDemand() == 0)
            {
                plateforme.add(node);
            }

        }
        return plateforme;
    }

    public Node getPlateformes(int index)
    {
        ArrayList<Node> plateforme = new ArrayList<>();
        for(Node node : this.nodes)
        {
            if (node.getDemand() == 0)
            {
                plateforme.add(node);
            }

        }
        return plateforme.get(index);
    }

    public Edge getEdge(Node start, Node end)
    {
        for(Edge edge : this.edges)
        {
            if(edge.getStart().getNumber()==start.getNumber() && edge.getEnd().getNumber()==end.getNumber())
            {
                return edge;
            }
        }
        return new Edge();
    }

    public double getUnitaryCost(Edge edge)
    {
        return edges.get(edges.indexOf(edge)).getUnitCost();
    }
    public double getFixedCost(Edge edge)
    {
        return edges.get(edges.indexOf(edge)).getFixedCost();
    }
    public double getTime(Edge edge)
    {
        return edges.get(edges.indexOf(edge)).getTime();
    }


}
