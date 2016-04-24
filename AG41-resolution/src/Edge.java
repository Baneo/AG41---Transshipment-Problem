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
    private int fixed_cost; //cost for using the edge
    private int unit_cost; //cost per object travelling through the edge
    private int delivery_time; //travelling time

    public Edge(int number, Node start, Node end, int capacity, int fixed_cost, int unit_cost, int delivery_time)
    {
        this.number = number;
        this.start = start;
        this.end = end;
        this.capacity = capacity;
        this.fixed_cost = fixed_cost;
        this.unit_cost = unit_cost;
        this.delivery_time = delivery_time;
    }
}
