import java.io.*;
import java.util.Scanner;

/**
 * Created by delan on 22/04/16.
 */
public class Main extends Thread
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
    private static int nb_trajets_valides;

    private static int total_cost;

    public void run(){

            try {
                /*
        System.out.println("Entrez le nom du fichier \n");
        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine();
        System.out.println("Fichier choisi : " + fileName);
        Input(filename);
        */
                System.out.println("reading t.txt");

                Input("t.txt");

                System.out.println("file red succesfully. Now launching");
                init_fmcm();


                System.out.println(" ----------------------------------  RECAPITULATIF  ----------------------------------");
                for (int a = 0; a < nb_fournisseurs; a++) {
                    for (int b = 0; b < nb_plateformes; b++) {
                        for (int c = 0; c < nb_clients; c++) {
                            System.out.println("\tFournisseur  no " + a + ";  plateforme no " + b + "; client no " + c + " ---> " + solution[a][b][c] + " paquets transmis");
                        }
                    }
                }


            }

            catch(IOException ex)
            {
                Thread.currentThread().interrupt();

            }
        }


    public static void display_solution()
    {
        System.out.println("________________________________________________________________________________________________________________________");
        System.out.println("" + total_cost);
    }

    //Cube des coûts des trajets
    private static int[][][] cout;
    //Cube contenant les paquets déplacés pour la solution courante
    private static int[][][] solution;

    public static void main(String[] args) throws IOException, InterruptedException {

        Thread t = new Main();
        /*System.out.println("Entrez le nombre de secondes \n");
        Scanner nb = new Scanner(System.in);
        int nb_sec = nb.nextInt();
        nb_sec = nb_sec *1000;
*/
        t.start();
        Thread.sleep(3000);
        display_solution();
        t.interrupt();

        /*
        Node f1 = new Node(0, 0, 0, -2, 0, 0);
        Node f2 = new Node(1, 0, 0, -2, 0, 0);
        Node p = new Node(2, 0, 0, 0, 5, 0);
        Node c1 = new Node(3, 0, 0, 2, 0, 0);
        Node c2 = new Node(4, 0, 0, 2, 0, 0);
        System.out.println("Nodes created");

        Edge f1p = new Edge(0, f1, p, 2, 4, 2, 0);
        Edge f2p = new Edge(0, f2, p, 2, 1, 7, 0);
        Edge pc1 = new Edge(0, p, c1, 2, 6, 1, 0);
        Edge pc2 = new Edge(0, p, c2, 2, 1, 1, 0);
        System.out.println("Edges created");

        graph.addNode(f1);
        graph.addNode(f2);
        graph.addNode(p);
        graph.addNode(c1);
        graph.addNode(c2);
        System.out.println("Nodes added");

        graph.addEdge(f1p);
        graph.addEdge(f2p);
        graph.addEdge(pc1);
        graph.addEdge(pc2);
        System.out.println("Edges added");


        nb_nodes = 5;
        nb_edges = 4;
        nb_fournisseurs = 2;
        nb_plateformes = 1;
        nb_clients = 2;
        */


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
        //Solution initiale
        /*
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

    */
    }

    /*------------------------------------------------------------------------------------------------------------------
           Méthode de construction en s'inspirant du flux maximal à coût minimal (FMCM)

           1) Construction d'un cube des coûts
           2) évaluation de la meilleure solution
           3) choix binaire : on prend la solution, ou pas, et on run deux instances, une avec chaque choix
     */

    public static void  init_fmcm(){
        System.out.println("init_fmcm start");

        cout = new int[nb_fournisseurs][nb_plateformes][nb_clients];
        solution = new int[nb_fournisseurs][nb_plateformes][nb_clients];
        System.out.println("\t cout[][][] created");
        nb_trajets_valides = 0;
        total_cost=0;
        int coutMin = -2;
        int[] meilleurChoix = {0,0,0};
        int meilleurChoix_nb_paquets = 0;
        System.out.println("\t other variables created\n starting for loops");


        for(int i=0; i<nb_fournisseurs; i++){
            for(int j=0; j<nb_plateformes; j++){
                for(int k=0; k<nb_clients; k++){
                    Node fournisseur = graph.getFournisseurs(i);
                    Node plateforme = graph.getPlateformes(j);
                    Node client = graph.getClients(k);
                    Edge edgeij = graph.getEdge(fournisseur, plateforme);
                    Edge edgejk = graph.getEdge(plateforme, client);
                    System.out.println("fr" + fournisseur + "pl" + plateforme + "cl" + client + "e1" + edgeij + "e2" + edgejk);
                    int nb_max_paquets = max_paquets(edgeij, edgejk);

                    if ((edgeij.getTime() + edgejk.getTime() + plateforme.getTime()) > time)
                    {
                        cout[i][j][k] = -1;
                    }
                    else
                    {
                        cout[i][j][k] = edgeij.getFixedCost() + edgeij.getUnitCost()*nb_max_paquets + edgejk.getFixedCost() + edgejk.getUnitCost()*nb_max_paquets + plateforme.getCost();
                        nb_trajets_valides ++;
                    }

                    //Si le trajet n'a pas déjà été pris ou éliminé plus tôt dans l'algorithme
                    if(cout[i][j][k]!=-1){
                        //coutMin vaut -2 si il n'a pas été modifié depuis sa création
                        if(coutMin==-2){
                            coutMin = cout[i][j][k];
                            meilleurChoix_nb_paquets = max_paquets(edgeij, edgejk);
                        }
                        else{
                            //Si le cout du trajet ijk est plus faible que le trajet le moins cher précédement trouvé, on remplace le meilleur trajet par ijk
                            if(coutMin>cout[i][j][k]){
                                coutMin=cout[i][j][k];
                                meilleurChoix[0]=i;
                                meilleurChoix[1]=j;
                                meilleurChoix[2]=k;
                                meilleurChoix_nb_paquets = nb_max_paquets;
                            }
                        }
                    }

                }//Fin for index k
            }//Fin for index j
        }//Fin for index i

        System.out.println(" ----------------------------------  TRAJETS CHOISIS  ----------------------------------");
        if (nb_trajets_valides > 0)
        {
            fmcm_fill(meilleurChoix, meilleurChoix_nb_paquets);
        }
        while(something_to_give() && nb_trajets_valides > 0){

            coutMin=-2;
            nb_trajets_valides = 0;
            meilleurChoix[0] = 0;
            meilleurChoix[1] = 0;
            meilleurChoix[2] = 0;
            meilleurChoix_nb_paquets = 0;

            System.out.println("something_to_give is true");
            for(int i=0; i<nb_fournisseurs; i++) {
                for (int j = 0; j < nb_plateformes; j++) {
                    for (int k = 0; k < nb_clients; k++){
                        System.out.println("\t\t vérification du trajet ijk = "+i+j+k);
                        Node fournisseur = graph.getFournisseurs(i);
                        Node plateforme = graph.getPlateformes(j);
                        Node client = graph.getClients(k);
                        Edge edgeij = graph.getEdge(fournisseur, plateforme);
                        Edge edgejk = graph.getEdge(plateforme, client);
                        int nb_max_paquets = max_paquets(edgeij, edgejk);

                        System.out.println("\t\t\tcoût = " + cout[i][j][k]);
                        //Si le trajet n'a pas déjà été pris ou éliminé plus tôt dans l'algorithme
                        if(cout[i][j][k]!=-1){
                            //coutMin vaut -2 si il n'a pas été modifié depuis sa création
                            if(coutMin==-2 || (coutMin>cout[i][j][k] && cout[i][j][k]!=-1)){
                                coutMin = cout[i][j][k];
                                meilleurChoix[0]=i;
                                meilleurChoix[1]=j;
                                meilleurChoix[2]=k;
                                meilleurChoix_nb_paquets = nb_max_paquets;
                                System.out.println("\t\t\tNouveau meilleur trajet ");
                            }

                        }else
                            System.out.println("\t\t\tTrajet invalide");

                    }//end for k
                }//end for j
            }//end for i

            if(coutMin!=-2){
                fmcm_fill(meilleurChoix, meilleurChoix_nb_paquets);
            }

            Scanner sc = new Scanner(System.in);
        }//end while something_to_give()


    }//Fin init_fmcm()


    /*
        Méthode renvoyant le nombre maximum de paquets transbordables sur le trajet ijk.
        Correspond au minimum parmis les valeurs suivantes :
            * le nombre de paquets disponibles chez le fournisseur i
            * le nombre dde paquets demandés par le client k
            * la capacité de l'edge ij
            * la capacité de l'edge jk
     */
    public static int max_paquets(Edge edgeij, Edge edgejk){
        int maximum_paquets_transbordables = -1*edgeij.getStart().getCurrentSolutionDemand();

        if(maximum_paquets_transbordables > edgejk.getEnd().getCurrentSolutionDemand())
            maximum_paquets_transbordables = edgejk.getEnd().getCurrentSolutionDemand();

        if(maximum_paquets_transbordables > edgeij.getCurrent_solution_capacity())
            maximum_paquets_transbordables = edgeij.getCurrent_solution_capacity();

        if(maximum_paquets_transbordables > edgejk.getCurrent_solution_capacity())
            maximum_paquets_transbordables = edgejk.getCurrent_solution_capacity();

        return maximum_paquets_transbordables;
    }


    /*
        Méthode faisant passer le nombre de paquet passé en paramètre dans le trajet passé en paramètre
        et modifiant en conséquence le cube des coûts
     */
    public static void fmcm_fill(int[] trajet, int nombre_paquets){
        if(nombre_paquets>0) {
            System.out.println("\t -- "+trajet[0]+" "+trajet[1]+" "+trajet[2]+" avec un cout de "+cout[trajet[0]][trajet[1]][trajet[2]]+" pour "+nombre_paquets+" paquets transmis");
            //On met à jou le cube de solution et on invalide le trajet dans le cube de coût
            solution[trajet[0]][trajet[1]][trajet[2]] = nombre_paquets;
            total_cost += cout[trajet[0]][trajet[1]][trajet[2]];
            cout[trajet[0]][trajet[1]][trajet[2]] = -1;

            //On récupère ensuite les 3 nodes du trajet, ainsi que les 2 edges, juste pour que le code soit plus clair
            Node frnssr = graph.getFournisseurs(trajet[0]);
            Node pltfrm = graph.getPlateformes(trajet[1]);
            Node clnt = graph.getClients(trajet[2]);
            Edge edgeij = graph.getEdge(frnssr, pltfrm);
            Edge edgejk = graph.getEdge(pltfrm, clnt);

            //On diminue la demande du client, l'offre du fournisseur et la capacité actuelle des deux edges
            frnssr.setDemand(nombre_paquets);
            clnt.setDemand(nombre_paquets);
            edgeij.setCapacity(nombre_paquets);
            edgejk.setCapacity(nombre_paquets);

            pltfrm.setDirty(true);
            if(pltfrm.isFDirty())
                System.out.println("totto");

            //On parcourt l'ensemble des trajets du cube de coût
            for (int a = 0; a < nb_fournisseurs; a++) {
                for (int b = 0; b < nb_plateformes; b++) {
                    for (int c = 0; c < nb_clients; c++) {
                        //On récupère les edges courants
                        Edge edgeab = graph.getEdge(graph.getFournisseurs(a), graph.getPlateformes(b));
                        Edge edgebc = graph.getEdge(graph.getPlateformes(b), graph.getClients(c));

                        //Si le trajet n'a pas déjà été invalidé
                        if (cout[a][b][c] != -1) {
                            //Si le fournisseur du trajet choisi n'a plus rien à fournir
                            if (frnssr.getCurrentSolutionDemand() == 0 && frnssr.equals(graph.getFournisseurs(a))) {
                                cout[a][b][c] = -1; //On invalide tous les trajets qui en partent
                            }
                            //Si le client du trajet choisi a tous les paquets qu'il lui fallait
                            if (clnt.getCurrentSolutionDemand() == 0 && clnt.equals(graph.getClients(c))) {
                                cout[a][b][c] = -1;//On invalide tous les trajets qui y arrivent
                            }
                            //Si l'edge ij du trajet choisi est saturé
                            if (edgeij.getCurrent_solution_capacity() == 0 && edgeij.equals(edgeab)) {
                                cout[a][b][c] = -1;//On invalide tous les trajets y passant
                            }
                            //Si l'edge jk du trajet choisi est saturé
                            if (edgejk.getCurrent_solution_capacity() == 0 && edgejk.equals(edgebc)) {
                                cout[a][b][c] = -1;//On invalide tous les trajets y passant
                            }

                            //Si le trajet actuel n'est toujours pas invalidé
                            if (cout[a][b][c] != -1) {
                                nb_trajets_valides ++;
                                //on met le coût du trajet uniquement avec les coûts unitaires
                                cout[a][b][c] = (edgeab.getUnitCost() + edgebc.getUnitCost()) * max_paquets(edgeab, edgebc);

                                //Si l'edge ab n'est pas utilisé
                                if (!edgeab.isDirty()) {
                                    cout[a][b][c] = cout[a][b][c] + edgeab.getFixedCost(); //On rajoute le coût fixe de l'edge
                                }
                                //Si l'edge bc n'est pas utilisé
                                if (!edgebc.isDirty()) {
                                    cout[a][b][c] = cout[a][b][c] + edgebc.getFixedCost();//On rajoute le coût fixe de l'edge
                                }
                                //Si la plateforme b n'est pas utilisé
                                if (!graph.getPlateformes(b).isFDirty()) {
                                    cout[a][b][c] = cout[a][b][c] + graph.getPlateformes(b).getCost();//On rajoute le coût de transbordement de la plateforme

                                }
                            }// end if cout != -1
                        }// end if cout != -1

                    }// end for each client
                }// end for each plateforme
            }// end for each fournisseur
        }//enf if nb_paquets >0
    }//end fmcm_fill()



    private static boolean something_to_give(){
        int total_paquets_restants=0;

        for(Node current : graph.getFournisseurs()){
            total_paquets_restants = total_paquets_restants+current.getCurrentSolutionDemand();
        }

        System.out.println("Still have "+total_paquets_restants+" packets to deliver");
        return total_paquets_restants<0;
    }
}
