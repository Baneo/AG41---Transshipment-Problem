/**
 * Created by delan on 22/04/16.
 */



public class Node
{
    private int number; //node id
    //private Type type; utilité ?
    private int demand; //<0 -> fournisseur, >0 -> client, =0 -> plateforme
    private int current_solution_demand;
    private int cost; //unit transshiping cost (for plateforms)
    private int time; //time
    private int X;
    private int Y;
    private boolean dirty; //true si la plateforme est utilisée au moins une fois, false si non ou si ce n'est pas une plateforme

    public Node(int number, int X, int Y, int demand, int cost, int time)
    {
        this.number = number;
        this.demand = demand;
        this.cost = cost;
        this.time = time;
        this.X = X;
        this.Y = Y;
        dirty = false;
        current_solution_demand = demand;
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
    public int getCurrentSolutionDemand(){ return current_solution_demand;}
    public void setDemand(int value) {
        if(demand>0)
                current_solution_demand = current_solution_demand - value;
        else {
            if (demand<0)
                current_solution_demand = current_solution_demand + value;
        }
    }
    public int getCost()
    {
        return cost;
    }
    public int getTime()
    {
        return time;
    }
    public boolean isDirty(){ return demand!=current_solution_demand;}
    public boolean isFDirty(){return dirty;}
    public void setDirty(boolean newValue){ this.dirty = newValue;}
    public String toString(){return new String(" " + number +" "+ demand);}
}
