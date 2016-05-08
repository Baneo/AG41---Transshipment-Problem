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
    private static int nb_clients = 0;
    private static int nb_fournisseurs = 0;
    private static int nb_plateformes = 0;
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

    public static int sommeFlow(int [][] flow, boolean line, int index)
    {
        int somme = 0;
        int numline = 0;
        int numcolonne;
        for(int [] tab : flow)
        {
            numcolonne = 0;
            for(int num : tab)
            {
                if (line && numline == index)
                {
                    somme += flow[numline][numcolonne];
                }
                else if(!line && numcolonne == index)
                {
                    somme += flow[numline][numcolonne];
                }

                numcolonne ++;
            }
            numline ++;
        }
        return somme;
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
                    if (ligne.contains("  ")) {
                        System.out.println("avant st et st2");
                        String[] st2 = ligne.split("  ");
                        String[] st = st2[1].split(" ");
                        edge = new Edge(Integer.parseInt(st[0]),graph.getNode(Integer.parseInt(st[1])),graph.getNode(Integer.parseInt(st[2])),Integer.parseInt(st[3]),Integer.parseInt(st[4]),Integer.parseInt(st[5]),Integer.parseInt(st[6]));

                    }
                    else
                    {
                        String[] st = ligne.split(" ");
                        edge = new Edge(Integer.parseInt(st[1]),graph.getNode(Integer.parseInt(st[2])),graph.getNode(Integer.parseInt(st[3])),Integer.parseInt(st[4]),Integer.parseInt(st[5]),Integer.parseInt(st[6]),Integer.parseInt(st[7]));

                    }
                    //Attention ! dans le fichier les nodes doivent apparaitre avant les edges sinon ca va faire de la merde
                    graph.addEdge(edge);
                }
                else if (ligne.contains("NODE:"))
                {
                    System.out.println(ligne);
                    if (ligne.contains("  ")) {

                        String[] st2 = ligne.split("  ");
                        String[] st = st2[1].split(" ");
                        if (Integer.parseInt(st[3]) == 0)
                        {
                            nb_plateformes ++;
                        }
                        else if (Integer.parseInt(st[3]) < 0)
                        {
                            nb_fournisseurs ++;
                        }
                        else
                        {
                            nb_clients ++;
                        }
                        node = new Node(Integer.parseInt(st[0]),Integer.parseInt(st[1]),Integer.parseInt(st[2]),Integer.parseInt(st[3]),Integer.parseInt(st[4]),Integer.parseInt(st[5]));

                    }
                    else
                    {
                        String[] st = ligne.split(" ");
                        if (Integer.parseInt(st[4]) == 0)
                        {
                            nb_plateformes ++;
                        }
                        else if (Integer.parseInt(st[4]) < 0)
                        {
                            nb_fournisseurs ++;
                        }
                        else
                        {
                            nb_clients ++;
                        }
                        node = new Node(Integer.parseInt(st[1]),Integer.parseInt(st[2]),Integer.parseInt(st[3]),Integer.parseInt(st[4]),Integer.parseInt(st[5]),Integer.parseInt(st[6]));

                    }


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

        //demande du temps

        //Solution initiale

        int[][] currentFlow = new int[nb_fournisseurs + nb_plateformes][nb_plateformes + nb_clients];
        int[][] bestFlow = new int[nb_fournisseurs + nb_plateformes][nb_plateformes + nb_clients];
        int indexF = 0, indexP = 0, indexC = 0;
        for(Node fournisseur : graph.getFournisseurs())
        {
            indexP = 0;
            for(Node plateforme : graph.getPlateformes())
            {
                Edge edge_fp = graph.getEdge(fournisseur, plateforme);
                while (edge_fp.getCapacity() > currentFlow[indexF + nb_plateformes][indexP] && sommeFlow(currentFlow, true, indexF + nb_plateformes) < -(fournisseur.getDemand()))
                {
                    currentFlow[indexF + nb_plateformes][indexP] += 1;
                }
                if (!(sommeFlow(currentFlow, true, indexF + nb_plateformes) < -(fournisseur.getDemand())))
                    break;

                indexP++;
            }
            indexF ++;
        }
        indexP = 0;
        for(Node plateforme : graph.getPlateformes())
        {
            indexC = 0;
            for(Node client : graph.getClients())
            {
                Edge edge_pc = graph.getEdge(plateforme, client);
                while (edge_pc.getCapacity() > currentFlow[indexP][indexC + nb_plateformes] && sommeFlow(currentFlow, false, indexC + nb_plateformes) < client.getDemand())
                {
                    currentFlow[indexP][indexC + nb_plateformes] += 1;
                }
                if(!(sommeFlow(currentFlow, false, indexC + nb_plateformes) < client.getDemand()))
                    break;

                indexC ++;
            }
            indexP ++;
        }

        int cost = 0;
        Node start = new Node();
        Node end = new Node();
        int temps_ecoule = 0;
        for(int i = 0; i < nb_plateformes + nb_fournisseurs;i++)
        {
            for(int j = 0; j < nb_plateformes + nb_clients; j++)
            {
                if(currentFlow[i][j] != 0)
                {
                    if (i >= 0 && i <= nb_plateformes - 1)
                    {
                        indexF = i;
                        start = graph.getPlateformes(i);
                    }
                    else if (i >= nb_plateformes)
                    {
                        indexF = i - nb_plateformes;
                        start = graph.getFournisseurs(indexF);
                    }

                    if (j >= 0 && j <= nb_plateformes - 1)
                    {
                        indexC = j;
                        end = graph.getPlateformes(j);
                    }
                    else if (j >= nb_plateformes)
                    {
                        indexC = j - nb_plateformes;
                        end = graph.getClients(j);
                    }
                    cost += graph.getFixedCost(graph.getEdge(start,end)) + currentFlow[i][j] * graph.getUnitaryCost(graph.getEdge(start, end));
                    temps_ecoule += graph.getTime(graph.getEdge(start, end));
                }
            }
        }
        for(int k = 0 ; k < nb_plateformes ; k++)
        {
            if(sommeFlow(currentFlow, true, k) != 0)
            {
                cost += graph.getPlateformes(k).getCost();
                temps_ecoule += graph.getPlateformes(k).getTime();
            }
        }






        //optimisation avec little

    }
}
