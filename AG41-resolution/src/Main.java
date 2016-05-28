import java.io.*;
import java.util.Scanner;

/**
 * Created by delan on 22/04/16.
 */
public class Main extends Thread
{
    private static Graph default_graph = new Graph();
    private static Graph graph = new Graph();
    private static int nb_nodes;
    private static int nb_edges;
    private static int nb_clients = 0;
    private static int nb_fournisseurs = 0;
    private static int nb_plateformes = 0;
    private static double time = -1;
    private static Node node;
    private static Edge edge;
    private static int nb_trajets_valides;

    private static String fileName;

    private static double default_cost;
    private static double total_cost;

    // Cube contenant les paquets déplacés pour la solution par défaut
    private static int[][][] default_solution;
    //Cube des coûts des trajets
    private static double[][][] cout;
    //Cube contenant les paquets déplacés pour la solution fmcm
    private static int[][][] solution;

    private static long time_start;
    private static long time_end;

    private static boolean fmcm_solution_found = false;
    private static boolean solution_not_displayed = true;

    /*
        Variables de débugging
        - Les booleens sont des variables pour contrôler l'affichage console des différentes opérations
        /!\ PRINT_FULL_INFO à désactiver pour des problèmes de plus de 10 noeuds, sinon l'affichage ne suit
        - Les entiers servent à sélectionner en dur le modèle du problème que doit résoudre le thread ainsi que le temps imparti
     */

    private static boolean PRINT_FULL_INFO = true;
    private static boolean PRINT_PATH = false;
    private static boolean PRINT_SOLUTION = true;
    private static boolean PRINT_FIRST_PATH_INFO = false;
    private static boolean TIME_CONSTRAINT_ON = true;
    private static boolean PRINT_DEMAND_INFO = false;

    public void run(){

            try {
                Input(fileName);
                init_default_solution();
                init_fmcm();
                if (solution_not_displayed){
                    solution_not_displayed = false;
                    display_solution();
                }

            }

            catch(IOException ex)
            {
                Thread.currentThread().interrupt();

            }
        }

    public static void display_solution() {
        time_end = System.currentTimeMillis();
        if(fmcm_solution_found) {
            System.out.println("________________________________________________________________________________________________________________________\n");
            System.out.println(" ----------------------------------  SOLUTION OBTENUE  ------------------------");
            System.out.println("Type de solution : FMCM");
            System.out.println("Coût total : " + total_cost);
            System.out.println("Temps d'exécution : " +(((double)time_end - (double)time_start) / 1000));
            if (PRINT_SOLUTION || PRINT_FULL_INFO) {
                System.out.println(" ----------------------------------  RECAPITULATIF  ----------------------------------");
                for (int a = 0; a < nb_fournisseurs; a++) {
                    for (int b = 0; b < nb_plateformes; b++) {
                        for (int c = 0; c < nb_clients; c++) {
                            if (solution[a][b][c] > 0)
                                System.out.println("\tFournisseur  no " + a + ";  plateforme no " + b + "; client no " + c + " ---> " + solution[a][b][c] + " paquets transmis");
                        }
                    }
                }
            }
        }else{
            System.out.println("________________________________________________________________________________________________________________________\n");
            System.out.println(" ----------------------------------  SOLUTION OBTENUE  ------------------------");
            System.out.println("Type de solution : par défaut");
            System.out.println("Coût total : " + default_cost);
            System.out.println("Temps d'exécution : " +(((double)time_end - (double)time_start) / 1000));
            if (PRINT_SOLUTION || PRINT_FULL_INFO) {
                System.out.println(" ----------------------------------  RECAPITULATIF  ----------------------------------");
                for (int a = 0; a < nb_fournisseurs; a++) {
                    for (int b = 0; b < nb_plateformes; b++) {
                        for (int c = 0; c < nb_clients; c++) {
                            if (default_solution[a][b][c] > 0)
                                System.out.println("\tFournisseur  no " + a + ";  plateforme no " + b + "; client no " + c + " ---> " + default_solution[a][b][c] + " paquets transmis");
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("Entrez le nom du fichier \n");
        Scanner sc = new Scanner(System.in);
        fileName = sc.nextLine();
        System.out.println("Fichier choisi : " + fileName);


        System.out.println("Entrez le nombre de secondes au bout duquel le programme doit afficher la solution\n");
        Scanner nb = new Scanner(System.in);
        int nb_sec = nb.nextInt();
        nb_sec = nb_sec *1000;
        Thread t = new Main();
        time_start = System.currentTimeMillis();
        t.start();
        Thread.sleep(nb_sec/*timer*/);
        if (solution_not_displayed){
            solution_not_displayed = false;
            display_solution();
        }
        t.interrupt();
    }


    //méthode parser : initialise les instances Graph nommées graph et default_graph
    public static void Input(String fileName) throws IOException
    {
        //lecture du fichier texte
        try{
            InputStream ips = new FileInputStream(fileName);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            System.out.println("Reading File...");
            while ((ligne=br.readLine())!=null){
                if (ligne.contains("NAME"))
                {
                    if(PRINT_FULL_INFO) System.out.println(ligne);
                    String[] st = ligne.split(" ");
                    if(PRINT_FULL_INFO) System.out.println("" + st[2]);
                    graph.setName(st[2]);
                    default_graph.setName(st[2]);

                }
                else if (ligne.contains("NBR_NODES"))
                {
                    if(PRINT_FULL_INFO) System.out.println(ligne);
                    String[] st = ligne.split(" ");
                    nb_nodes = Integer.parseInt(st[1]);
                }
                else if (ligne.contains("NBR_EDGES"))
                {
                    if(PRINT_FULL_INFO) System.out.println(ligne);
                    String[] st = ligne.split(" ");
                    nb_edges = Integer.parseInt(st[1]);
                }
                else if (ligne.contains("T:"))
                {
                    if(PRINT_FULL_INFO) System.out.println("Ligne du temps !" + ligne);
                    String[] st = ligne.split(" ");
                    time = Double.parseDouble(st[1]);
                }
                else if (ligne.contains("#"))
                {
                    if(PRINT_FULL_INFO) System.out.println("Ligne de commentaire BRUH !" + ligne);
                }
                else if (ligne.contains("EDGE:"))
                {
                    if(PRINT_FULL_INFO) System.out.println(ligne);
                    if (ligne.contains("  ")) {
                        if(PRINT_FULL_INFO) System.out.println("avant st et st2");
                        String[] st2 = ligne.split("  ");
                        String[] st = st2[1].split(" ");
                        edge = new Edge(Integer.parseInt(st[0]),graph.getNode(Integer.parseInt(st[1])),graph.getNode(Integer.parseInt(st[2])),Integer.parseInt(st[3]),Double.parseDouble(st[4]),Double.parseDouble(st[5]),Double.parseDouble(st[6]));

                    }
                    else
                    {
                        String[] st = ligne.split(" ");
                        edge = new Edge(Integer.parseInt(st[1]),graph.getNode(Integer.parseInt(st[2])),graph.getNode(Integer.parseInt(st[3])),Integer.parseInt(st[4]),Double.parseDouble(st[5]),Double.parseDouble(st[6]),Double.parseDouble(st[7]));

                    }
                    //Attention ! dans le fichier les nodes doivent apparaitre avant les edges sinon ca va faire de la merde
                    graph.addEdge(edge);
                    default_graph.addEdge(edge);

                }
                else if (ligne.contains("NODE:"))
                {
                    if(PRINT_FULL_INFO) System.out.println(ligne);
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
                        node = new Node(Integer.parseInt(st[0]),Integer.parseInt(st[1]),Integer.parseInt(st[2]),Integer.parseInt(st[3]),Double.parseDouble(st[4]),Double.parseDouble(st[5]));

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
                        node = new Node(Integer.parseInt(st[1]),Integer.parseInt(st[2]),Integer.parseInt(st[3]),Integer.parseInt(st[4]),Double.parseDouble(st[5]),Double.parseDouble(st[6]));

                    }


                    graph.addNode(node);
                    default_graph.addNode(node);
                }
                else
                {
                    //cas du EOF
                    System.out.println("END OF FILE REACHED");
                }

            }
            br.close();
            ips.close();
            ipsr.close();

            System.out.println("File red succesfully. Now launching");
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }



    public static void init_default_solution(){
        //Solution initiale
        default_cost=0;
        default_solution = new int[nb_fournisseurs][nb_plateformes][nb_clients];
        boolean end = false;
        int nb_max_paquets;
        default_cost = 0;

        //Pour chaque trajet
        for(int i=0; i<nb_fournisseurs;i++) {
            for (int j = 0; j < nb_plateformes; j++) {
                for (int k = 0; k < nb_clients; k++) {
                    Node fournisseur = default_graph.getFournisseurs(i);
                    Node plateforme = default_graph.getPlateformes(j);
                    Node client =  default_graph.getClients(k);
                    Edge edgeij = default_graph.getEdge(fournisseur, plateforme);
                    Edge edgejk = default_graph.getEdge(plateforme, client);

                    System.out.println(edgeij+" - "+edgejk+" - "+fournisseur+" - "+plateforme+" - "+client);

                    //Si on peut faire passer des paquets
                    if ((nb_max_paquets = max_paquets(edgeij, edgejk)) > 0) {
                        //On met à jour le cube de solution et le cout total de la solution
                        default_solution[i][j][k] = nb_max_paquets;
                        default_cost += (edgeij.getUnitCost() + edgejk.getUnitCost() + plateforme.getCost()) * nb_max_paquets;
                        if (!edgeij.isDirty()) default_cost += edgeij.getFixedCost();
                        if (!edgejk.isDirty()) default_cost += edgejk.getFixedCost();

                        //On signifie le passage des paquets
                        fournisseur.setDemand(nb_max_paquets);
                        client.setDemand(nb_max_paquets);
                        edgeij.setCapacity(nb_max_paquets);
                        edgejk.setCapacity(nb_max_paquets);

                        //Si tous les paquest de tous les fournisseurs ont été transbordés
                        //On arrête le parcours des trajets
                        end = !something_to_give(default_graph);
                        if (end) break;
                    }
                    if (end) break;
                }
                if (end) break;
            }
        }
    }



    /*------------------------------------------------------------------------------------------------------------------
           Méthode de construction en s'inspirant du flux maximal à coût minimal (FMCM)

           1) Construction d'un cube des coûts
           2) évaluation de la meilleure solution
           3) choix binaire : on prend la solution la moins cher
           4) On applique la chaine améliorante (pas implémenté)
     */

    public static void init_fmcm(){
        System.out.println("Starting init_fmcm...");

        // On initialise les attributs
        cout = new double[nb_fournisseurs][nb_plateformes][nb_clients];
        solution = new int[nb_fournisseurs][nb_plateformes][nb_clients];
        if(PRINT_FULL_INFO) System.out.println("\t cout[][][] created");
        nb_trajets_valides = 0;
        total_cost=0;
        double coutMin = -2;
        int[] meilleurChoix = {0,0,0};
        int meilleurChoix_nb_paquets = 0;
        if(PRINT_FULL_INFO) System.out.println("\t other variables created\n starting for loops");

        //Pour chaque trajet
        for(int i=0; i<nb_fournisseurs; i++){
            for(int j=0; j<nb_plateformes; j++){
                for(int k=0; k<nb_clients; k++){
                    Node fournisseur = graph.getFournisseurs(i);
                    Node plateforme = graph.getPlateformes(j);
                    Node client = graph.getClients(k);
                    Edge edgeij = graph.getEdge(fournisseur, plateforme);
                    Edge edgejk = graph.getEdge(plateforme, client);

                    if(PRINT_FULL_INFO) System.out.println("fr" + fournisseur + "pl" + plateforme + "cl" + client + "e1" + edgeij + "e2" + edgejk);
                    //Si le chemin est valide
                    if(cout[i][j][k] != -1) {
                        int nb_max_paquets = max_paquets(edgeij, edgejk);


                        //Si le chemin est trop long
                        if ((edgeij.getTime() + edgejk.getTime() + plateforme.getTime()) > time && TIME_CONSTRAINT_ON) {
                            cout[i][j][k] = -1;

                        } else {
                            //Si on ne peut pas faire passer de paquets dedans
                            if (nb_max_paquets < 1)
                                cout[i][j][k] = -1;
                            else {
                                cout[i][j][k] = edgeij.getFixedCost() + edgejk.getFixedCost() + (edgeij.getUnitCost() + edgejk.getUnitCost() + plateforme.getCost()) * nb_max_paquets;
                                nb_trajets_valides++;

                                // Si le trajet actuel est le premier trajet valide ou si il est meilleur que le précédent trajet le moins cher
                                if (coutMin > cout[i][j][k] || coutMin == -2) {
                                    coutMin = cout[i][j][k];
                                    meilleurChoix[0] = i;
                                    meilleurChoix[1] = j;
                                    meilleurChoix[2] = k;
                                    meilleurChoix_nb_paquets = nb_max_paquets;
                                }
                            }//Fin else


                        }
                    }
                }//Fin for index k
            }//Fin for index j
        }//Fin for index i

        if(PRINT_FIRST_PATH_INFO) {
            System.out.println("nb trajets valides :" + nb_trajets_valides);
            System.out.println("meilleur premier trajet :" + meilleurChoix[0] + " " + meilleurChoix[1] + " " + meilleurChoix[2]);
            System.out.println("cout associé : " + cout[meilleurChoix[0]][meilleurChoix[1]][meilleurChoix[2]]);
            System.out.println("nb paquets associés : " + meilleurChoix_nb_paquets);
        }
        if(PRINT_FULL_INFO) System.out.println(" ----------------------------------  TRAJETS CHOISIS  ----------------------------------");

        //Si un trajet est valide, on élit le meilleur
        if (nb_trajets_valides > 0)
        {
            fmcm_fill(meilleurChoix, meilleurChoix_nb_paquets);
        }

        while(something_to_give(graph) && nb_trajets_valides > 0){

            coutMin=-2;
            nb_trajets_valides = 0;
            meilleurChoix[0] = 0;
            meilleurChoix[1] = 0;
            meilleurChoix[2] = 0;
            meilleurChoix_nb_paquets = 0;

            //On parcours l'ensemble des trajets pour déterminer le moins cher
            if(PRINT_FULL_INFO) System.out.println("something_to_give is true");
            for(int i=0; i<nb_fournisseurs; i++) {
                for (int j = 0; j < nb_plateformes; j++) {
                    for (int k = 0; k < nb_clients; k++){
                        if(PRINT_FULL_INFO) System.out.println("\t\t vérification du trajet ijk = "+i+j+k);
                        Node fournisseur = graph.getFournisseurs(i);
                        Node plateforme = graph.getPlateformes(j);
                        Node client = graph.getClients(k);
                        Edge edgeij = graph.getEdge(fournisseur, plateforme);
                        Edge edgejk = graph.getEdge(plateforme, client);
                        int nb_max_paquets = max_paquets(edgeij, edgejk);

                        if(PRINT_FULL_INFO) System.out.println("\t\t\tcoût = " + cout[i][j][k]);
                        //Si le trajet n'a pas déjà été pris ou éliminé plus tôt dans l'algorithme
                        if(cout[i][j][k]!=-1){
                            nb_trajets_valides++;
                            //coutMin vaut -2 si il n'a pas été modifié depuis sa création
                            if(coutMin==-2 || coutMin>cout[i][j][k]){
                                coutMin = cout[i][j][k];
                                meilleurChoix[0]=i;
                                meilleurChoix[1]=j;
                                meilleurChoix[2]=k;
                                meilleurChoix_nb_paquets = nb_max_paquets;
                                if(PRINT_FULL_INFO) System.out.println("\t\t\tNouveau meilleur trajet ");
                            }

                        }else
                        if(PRINT_FULL_INFO) System.out.println("\t\t\tTrajet invalide");

                    }//end for k
                }//end for j
            }//end for i

            //Si un trajet a été trouvé
            if(coutMin!=-2){
                System.out.println("nb trajets valides :" + nb_trajets_valides);
                fmcm_fill(meilleurChoix, meilleurChoix_nb_paquets);
            }else{
                System.out.println("Aucun trajet valide");
            }
        }//end while something_to_give()

        //Si l'algo s'est terminé parce que tous les paquets ont été transbordé
        if(!something_to_give(graph)){
            fmcm_solution_found = true;
        }

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
        int maximum_paquets_transbordables = -1*edgeij.getStart().getCurrentSolutionDemand(); //On initialise la valeur à celle de l'offre du fournisseur

        if(maximum_paquets_transbordables > edgejk.getEnd().getCurrentSolutionDemand())
            maximum_paquets_transbordables = edgejk.getEnd().getCurrentSolutionDemand(); //Si la demande du client est inférieure, elle devient la valeur

        //Si la limite de saturation d'un edge requiert moins de paquets, ce nombre devient la valeur
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
            if(PRINT_FULL_INFO || PRINT_PATH) System.out.println("\t -- "+trajet[0]+" "+trajet[1]+" "+trajet[2]+" avec un cout de "+cout[trajet[0]][trajet[1]][trajet[2]]+" pour "+nombre_paquets+" paquets transmis");
            //On met à jour le cube de solution et le coût total de la solution
                solution[trajet[0]][trajet[1]][trajet[2]] = nombre_paquets;
                total_cost += cout[trajet[0]][trajet[1]][trajet[2]];

            //On récupère ensuite les 3 nodes du trajet élu, ainsi que les 2 edges qui le compose
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

            //On parcourt l'ensemble des trajets du cube de coût
            for (int a = 0; a < nb_fournisseurs; a++) {
                for (int b = 0; b < nb_plateformes; b++) {
                    for (int c = 0; c < nb_clients; c++) {
                        //Si le trajet n'a pas déjà été invalidé
                        if (cout[a][b][c] != -1) {
                            //On récupère les edges courants
                            Edge edgeab = graph.getEdge(graph.getFournisseurs(a), graph.getPlateformes(b));
                            Edge edgebc = graph.getEdge(graph.getPlateformes(b), graph.getClients(c));
                            //Si le nombre de paquets transbordables est nul
                            if(max_paquets(edgeab, edgebc)==0)
                                cout[a][b][c] = -1; //On invalide le trajet
                            else{
                                //Sinon, on met le coût du trajet uniquement avec les coûts unitaires
                                cout[a][b][c] = (edgeab.getUnitCost() + edgebc.getUnitCost() + graph.getPlateformes(b).getCost()) * max_paquets(edgeab, edgebc);

                                //Si l'edge ab n'est pas utilisé
                                if (!edgeab.isDirty()) {
                                    cout[a][b][c] = cout[a][b][c] + edgeab.getFixedCost(); //On rajoute le coût fixe de l'edge
                                }
                                //Si l'edge bc n'est pas utilisé
                                if (!edgebc.isDirty()) {
                                    cout[a][b][c] = cout[a][b][c] + edgebc.getFixedCost();//On rajoute le coût fixe de l'edge
                                }
                            }// end if cout != -1
                        }// end if cout != -1

                    }// end for each client
                }// end for each plateforme
            }// end for each fournisseur
        }//enf if nb_paquets >0
    }//end fmcm_fill()


    //Permet de savoir si, dans un graph, tous les paquets ont été transbordés
    //Renvoie true si il reste des paquets, false sinon
    private static boolean something_to_give(Graph graph){
        int total_paquets_restants_fournisseurs=0;
        int total_paquets_restants_clients=0;

        for(Node current : graph.getFournisseurs()){
            total_paquets_restants_fournisseurs = total_paquets_restants_fournisseurs+current.getCurrentSolutionDemand();
            if(PRINT_DEMAND_INFO) System.out.print(current.getCurrentSolutionDemand()+" -- ");
        }
        if(PRINT_DEMAND_INFO) System.out.print("\n");
        for(Node current : graph.getClients()){
            total_paquets_restants_clients = total_paquets_restants_clients+current.getCurrentSolutionDemand();
            if(PRINT_DEMAND_INFO) System.out.print(current.getCurrentSolutionDemand()+" -- ");
        }
        if(PRINT_DEMAND_INFO) System.out.print("\n");

        if(PRINT_PATH) System.out.println("  ---  Encore "+total_paquets_restants_fournisseurs*-1+" paquets disponible chez les fournisseurs");
        if(PRINT_PATH) System.out.println("  ---  Encore "+total_paquets_restants_clients+" paquets demandés par les clients");
        return total_paquets_restants_fournisseurs<0 && total_paquets_restants_clients>0;
    }
}
