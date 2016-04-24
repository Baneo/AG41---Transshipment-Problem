import com.sun.org.apache.xml.internal.utils.StringToIntTable;

import java.io.*;
import java.util.Scanner;

/**
 * Created by delan on 22/04/16.
 */
public class Main
{
    private static Graph graph = new Graph();
    private static int nb_nodes = -1;
    private static int nb_edges = -1;
    private static int time = -1;
    private static Node node;
    private static Edge edge;

    public static void main(String[] args) throws IOException {
        System.out.println("Entrez le nom du fichier \n");
        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine();
        System.out.println("Fichier choisi : " + fileName);
        Input(fileName);
    }

    public static void Input(String fileName) throws IOException
    {
        //lecture du fichier texte
        try{
            InputStream ips = new FileInputStream(fileName);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne=br.readLine())!=null){
                if (ligne.contains("NAME"))
                {
                    System.out.println(ligne);
                    String[] st = ligne.split(" ");
                    System.out.println("" + st[2]);
                    graph.setName(st[2]);

                }
                else if (ligne.contains("NBR_NODES"))
                {
                    System.out.println(ligne);
                    String[] st = ligne.split(" ");
                    nb_nodes = Integer.parseInt(st[1]);
                }
                else if (ligne.contains("NBR_EDGES"))
                {
                    System.out.println(ligne);
                    String[] st = ligne.split(" ");
                    nb_edges = Integer.parseInt(st[1]);
                }
                else if (ligne.contains("T:"))
                {
                    System.out.println("Ligne du temps !" + ligne);
                    String[] st = ligne.split(" ");
                    time = Integer.parseInt(st[1]);
                }
                else if (ligne.contains("#"))
                {
                    System.out.println("Ligne de commentaire BRUH !" + ligne);
                }
                else if (ligne.contains("EDGE:"))
                {
                    System.out.println(ligne);
                    String[] st = ligne.split(" ");
                    //Attention ! dans le fichier les nodes doivent apparaitre avant les edges sinon ca va faire de la merde
                    edge = new Edge(Integer.parseInt(st[1]),graph.getNode(Integer.parseInt(st[2])),graph.getNode(Integer.parseInt(st[3])),Integer.parseInt(st[4]),Integer.parseInt(st[5]),Integer.parseInt(st[6]),Integer.parseInt(st[7]));
                    graph.addEdge(edge);
                }
                else if (ligne.contains("NODE:"))
                {
                    System.out.println(ligne);
                    String[] st = ligne.split(" ");
                    node = new Node(Integer.parseInt(st[1]),Integer.parseInt(st[2]),Integer.parseInt(st[3]),Integer.parseInt(st[4]),Integer.parseInt(st[5]),Integer.parseInt(st[6]));
                    graph.addNode(node);
                }
                else
                {
                    //cas du EOF
                    System.out.println("END OF FILE REACHED");
                }

            }
            br.close();

        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        /* Maintenant on a le graph avec les nodes et les edges correctement
        implémentées normalement, avec en plus de ca le nombre de nodes et
        d'edges et le nom du graph et le temps d'execution (je sais pas a quoi servent
        les X et Y dans les nodes :/)

        Normalement y'a plus qu'a implémenter l'algo trouvé dans le rapport
        préliminaire :3

        Soit dans une nouvelle classe ou alors direct ici comme un sale mais c'est
        pas si grave ^^
          */

        graph.printNode(10);
    }
}
