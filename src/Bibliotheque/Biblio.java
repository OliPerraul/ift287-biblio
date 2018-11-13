package Bibliotheque;

import java.io.*;
import java.util.Date;
import java.util.StringTokenizer;
import java.text.*;

/**
 * Interface du système de gestion d'une bibliothèque
 *
 * <pre>
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Ce programme permet d'appler les transactions de base d'une
 * bibliothèque.  Il gère des livres, des membres et des
 * réservations. Les données sont conservées dans une base de
 * données NoSQL accédée avec MongoDB. Pour une liste des
 * transactions traitées, voir la méthode afficherAide().
 *
 * Paramètres
 * 0- serveur
 * 1- nom de la BD
 * 2- user id pour établir une connexion avec le serveur MongoDB
 * 3- mot de passe pour le user id
 * 4- fichier de transaction [optionnel]
 *           si non spécifié, les transactions sont lues au
 *           clavier (System.in)
 *
 * Pré-condition
 *   La base de données de la bibliothèque doit exister
 *
 * Post-condition
 *   Le programme effectue les maj associées à chaque
 *   transaction
 * </pre>
 */
public class Biblio
{
    private GestionBibliotheque gestionBiblio;
    private boolean echo;

    /**
     * Ouverture de la BD, traitement des transactions et fermeture de la BD.
     */
    public static void main(String argv[]) throws Exception
    {
        // validation du nombre de paramètres
        if (argv.length < 4)
        {
            System.out.println("Usage: java Biblio <serveur> <bd> <user> <password>  [<fichier-transactions>]");
            return;
        }

        Biblio bibliothequeInstance = null;
        
        try
        {
            // ouverture du fichier de transactions
            // s'il est spécifié comme argument
            boolean lectureAuClavier = true;
            InputStream sourceTransaction = System.in;
            if (argv.length > 4)
            {
                sourceTransaction = new FileInputStream(argv[4]);
                lectureAuClavier = false;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(sourceTransaction));

            bibliothequeInstance = new Biblio(argv[0], argv[1], argv[2], argv[3]);
            bibliothequeInstance.setFaireEcho(!lectureAuClavier);
            bibliothequeInstance.traiterTransactions(reader);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
        finally
        {
            if(bibliothequeInstance != null)
                bibliothequeInstance.fermer();
        }
    }
    
    public Biblio(String serveur, String bd, String user, String pass) throws Exception
    {
        gestionBiblio = new GestionBibliotheque(serveur, bd, user, pass);
    }
    
    public void setFaireEcho(boolean echo)
    {
        this.echo = echo;
    }
    
    public void fermer() throws Exception
    {
        gestionBiblio.fermer();
    }

    /**
     * Traitement des transactions de la bibliothèque
     */
    public void traiterTransactions(BufferedReader reader) throws Exception
    {
        afficherAide();
        String transaction = lireTransaction(reader);
        while (!finTransaction(transaction))
        {
            // découpage de la transaction en mots
            StringTokenizer tokenizer = new StringTokenizer(transaction, " ");
            if (tokenizer.hasMoreTokens())
                executerTransaction(tokenizer);
            transaction = lireTransaction(reader);
        }
    }

    /**
     * Lecture d'une transaction
     */
    private String lireTransaction(BufferedReader reader) throws IOException
    {
        System.out.print("> ");
        String transaction = reader.readLine();
        // echo si lecture dans un fichier
        if (echo)
            System.out.println(transaction);
        return transaction;
    }

    /**
     * Décodage et traitement d'une transaction
     */
    private void executerTransaction(StringTokenizer tokenizer) throws Exception
    {
        try
        {
            String command = tokenizer.nextToken();

            // *******************
            // HELP
            // *******************
            if (command.equals("aide"))
            {
                afficherAide();
            }
            // *******************
            // ACQUERIR
            // *******************
            else if (command.equals("acquerir"))
            {
                int idLivre = readInt(tokenizer);
                String titre = readString(tokenizer);
                String auteur = readString(tokenizer);
                Date dateAcquisition = readDate(tokenizer);
                gestionBiblio.getGestionLivre().acquerir(idLivre, titre, auteur, dateAcquisition);
            }
            // *******************
            // VENDRE
            // *******************
            else if (command.equals("vendre"))
            {
                int idLivre = readInt(tokenizer);
                gestionBiblio.getGestionLivre().vendre(idLivre);
            }
            // *******************
            // PRETER
            // *******************
            else if (command.equals("preter"))
            {
                int idLivre = readInt(tokenizer);
                int idMembre = readInt(tokenizer);
                Date dateEmprunt = readDate(tokenizer);
                gestionBiblio.getGestionPret().preter(idLivre, idMembre, dateEmprunt);
            }
            // *******************
            // RENOUVELER
            // *******************
            else if (command.equals("renouveler"))
            {
                int idLivre = readInt(tokenizer);
                Date dateRenouvellement = readDate(tokenizer);
                gestionBiblio.getGestionPret().renouveler(idLivre, dateRenouvellement);
            }
            // *******************
            // RETOURNER
            // *******************
            else if (command.equals("retourner"))
            {
                int idLivre = readInt(tokenizer);
                Date dateRetour = readDate(tokenizer);
                gestionBiblio.getGestionPret().retourner(idLivre, dateRetour);
            }
            // *******************
            // INSCRIRE
            // *******************
            else if (command.equals("inscrire"))
            {
                int idMembre = readInt(tokenizer);
                String nom = readString(tokenizer);
                long tel = readLong(tokenizer);
                int limitePret = readInt(tokenizer);
                gestionBiblio.getGestionMembre().inscrire(idMembre, nom, tel, limitePret);
            }
            // *******************
            // DESINSCRIRE
            // *******************
            else if (command.equals("desinscrire"))
            {
                int idMembre = readInt(tokenizer);
                gestionBiblio.getGestionMembre().desinscrire(idMembre);
            }
            // *******************
            // RESERVER
            // *******************
            else if (command.equals("reserver"))
            {
                int idReservation = readInt(tokenizer);
                int idLivre = readInt(tokenizer);
                int idMembre = readInt(tokenizer);
                Date dateReservation = readDate(tokenizer);
                gestionBiblio.getGestionReservation().reserver(idReservation, idLivre, idMembre, dateReservation);
            }
            // *******************
            // PRENDRE RESERVATION
            // *******************
            else if (command.equals("prendreRes"))
            {
                int idReservation = readInt(tokenizer);
                Date dateReservation = readDate(tokenizer);
                gestionBiblio.getGestionReservation().prendreRes(idReservation, dateReservation);
            }
            // *******************
            // ANNULER RESERVATION
            // *******************
            else if (command.equals("annulerRes"))
            {
                int idReservation = readInt(tokenizer);
                gestionBiblio.getGestionReservation().annulerRes(idReservation);
            }
            // *********************
            // AFFICHER LA LISTE DE tous les livres
            // *********************
            else if (command.equals("listerLivres"))
            {
                gestionBiblio.getGestionLivre().listerLivres();
            }
            // *********************
            // commentaire : ligne débutant par --
            // *********************
            else if (command.equals("--"))
            { 
                // ne rien faire, c'est un commentaire
            }
            // ***********************
            // TRANSACTION INCONNUE
            // ***********************
            else
            {
                System.out.println("  Transactions non reconnue.  Essayer \"aide\"");
            }
        }
        catch (BiblioException e)
        {
            System.out.println("** " + e.toString());
        }
    }

    /** Affiche le menu des transactions acceptées par le système */
    private static void afficherAide()
    {
        System.out.println();
        System.out.println("Chaque transaction comporte un nom et une liste d'arguments");
        System.out.println("separes par des espaces. La liste peut etre vide.");
        System.out.println(" Les dates sont en format yyyy-mm-dd.");
        System.out.println("");
        System.out.println("Les transactions sont:");
        System.out.println("  aide");
        System.out.println("  exit");
        System.out.println("  acquerir <idLivre> <titre> <auteur> <dateAcquisition>");
        System.out.println("  preter <idLivre> <idMembre> <dateEmprunt>");
        System.out.println("  renouveler <idLivre> <dateRenouvellement>");
        System.out.println("  retourner <idLivre> <dateRetour>");
        System.out.println("  vendre <idLivre>");
        System.out.println("  inscrire <idMembre> <nom> <telephone> <limitePret>");
        System.out.println("  desinscrire <idMembre>");
        System.out.println("  reserver <idReservation> <idLivre> <idMembre> <dateReservation>");
        System.out.println("  prendreRes <idReservation> <dateEmprunt>");
        System.out.println("  annulerRes <idReservation>");
        System.out.println("  listerLivresRetard <dateCourante>");
        System.out.println("  listerLivresTitre <mot>");
        System.out.println("  listerLivres");
    }

    /**
     * Vérifie si la fin du traitement des transactions est atteinte.
     */
    private boolean finTransaction(String transaction)
    {
        // fin de fichier atteinte
        if (transaction == null)
            return true;

        StringTokenizer tokenizer = new StringTokenizer(transaction, " ");

        // ligne ne contenant que des espaces
        if (!tokenizer.hasMoreTokens())
            return false;

        // commande "exit"
        String commande = tokenizer.nextToken();
        return commande.equals("exit");

    }

    /** lecture d'une chaîne de caractères de la transaction entrée à l'écran */
    private String readString(StringTokenizer tokenizer) throws BiblioException
    {
        if (tokenizer.hasMoreElements())
        {
            return tokenizer.nextToken();
        }
        else
        {
            throw new BiblioException("autre paramètre attendu");
        }
    }

    /**
     * lecture d'un int java de la transaction entrée à l'écran
     */
    private int readInt(StringTokenizer tokenizer) throws BiblioException
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                return Integer.valueOf(token).intValue();
            }
            catch (NumberFormatException e)
            {
                throw new BiblioException("Nombre attendu à la place de \"" + token + "\"");
            }
        }
        else
        {
            throw new BiblioException("autre paramètre attendu");
        }
    }

    /**
     * lecture d'un long java de la transaction entrée à l'écran
     */
    private long readLong(StringTokenizer tokenizer) throws BiblioException
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                return Long.valueOf(token).longValue();
            }
            catch (NumberFormatException e)
            {
                throw new BiblioException("Nombre attendu à la place de \"" + token + "\"");
            }
        }
        else
        {
            throw new BiblioException("autre paramètre attendu");
        }
    }

    /**
     * lecture d'une date en format YYYY-MM-DD
     */
    private Date readDate(StringTokenizer tokenizer) throws BiblioException
    {
        if (tokenizer.hasMoreElements())
        {
            String token = tokenizer.nextToken();
            try
            {
                Date d = FormatDate.convertirDate(token);
                return d;
            }
            catch (ParseException e)
            {
                throw new BiblioException("Date en format YYYY-MM-DD attendue à la place  de \"" + token + "\"");
            }
        }
        else
        {
            throw new BiblioException("autre paramètre attendu");
        }
    }
}// class
