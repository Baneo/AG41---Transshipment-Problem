/**
 * Created by delan on 22/04/16.
 */



public class Node
{
    private int number; //node id
    //private Type type; utilité ?
    private int demand; //<0 -> fournisseur, >0 -> client, =0 -> plateforme
    private int cost; //unit transshiping cost (for plateforms)
    private int time; //time
    private int X;
    private int Y;

    public Node(int number, int X, int Y, int demand, int cost, int time)
    {
        this.number = number;
        this.demand = demand;
        this.cost = cost;
        this.time = time;
        this.X = X;
        this.Y = Y;
    }
    public Node()
    {

    }

    public int getNumber()
    {
        return number;
    }

    public int getX()
    {
        return X;
    }
    public int getY()
    {
        return Y;
    }
    public int getDemand()
    {
        return demand;
    }
    public int getCost()
    {
        return cost;
    }
    public int getTime()
    {
        return time;
    }
}
