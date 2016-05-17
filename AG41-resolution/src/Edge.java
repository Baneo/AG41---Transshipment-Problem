/**
 * Created by delan on 22/04/16.
 *
 */
public class Edge
{
    private int number; //id of the edge
    private Node start; //starting node
    private Node end; //ending node
    private int capacity; //capacity of the edge
    private int current_solution_capacity;
    private int fixed_cost; //cost for using the edge
    private int unit_cost; //cost per object travelling through the edge
    private int delivery_time; //travelling time
    private boolean dirty; // true si l'edge est utilisé au moins une fois, false sinon

    public Edge()
    {
        number = -1;
    }

    public Edge(int number, Node start, Node end, int capacity, int fixed_cost, int unit_cost, int delivery_time)
    {
        this.number = number;
        this.start = start;
        this.end = end;
        this.capacity = capacity;
        this.fixed_cost = fixed_cost;
        this.unit_cost = unit_cost;
        this.delivery_time = delivery_time;
        dirty = false;
        current_solution_capacity = capacity;
    }
    public Node getStart()
    {
        return start;
    }
    public Node getEnd()
    {
        return end;
    }
    public int getCapacity()
    {
        return capacity;
    }
    public int getCurrent_solution_capacity(){ return current_solution_capacity; }
    public void setCapacity(int value) {current_solution_capacity = current_solution_capacity - value;}
    public int getUnitCost()
    {
        return unit_cost;
    }
    public int getFixedCost()
    {
        return fixed_cost;
    }
    public int getTime()
    {
        return delivery_time;
    }
    public boolean isDirty(){ return capacity!=current_solution_capacity;}
    public void setDirty(boolean newValue){ this.dirty = newValue;}
}
