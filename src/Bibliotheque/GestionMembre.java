package Bibliotheque;

/**
 * Gestion des transactions de reliées à la création et suppresion de membres
 * dans une bibliothèque.
 *
 * <pre>
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 *
 * Ce programme permet de gérer les transaction reliées à la 
 * création et suppresion de membres.
 *
 * Pré-condition
 *   la base de données de la bibliothèque doit exister
 *
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 * </pre>
 */

public class GestionMembre
{
    private Membres membres;
    private Reservations reservations;

    /**
     * Creation d'une instance
     */
    public GestionMembre(Membres membres, Reservations reservations) throws BiblioException
    {
        if (membres.getConnexion() != reservations.getConnexion())
            throw new BiblioException("Les collections d'objets n'utilisent pas la même connexion au serveur");
        this.membres = membres;
        this.reservations = reservations;
    }

    /**
     * Ajout d'un nouveau membre dans la base de donnees. S'il existe deja, une
     * exception est levee.
     */
    public void inscrire(int idMembre, String nom, long telephone, int limitePret)
            throws BiblioException, Exception
    {
        try
        {            
            // Vérifie si le membre existe déja
            if (membres.existe(idMembre))
                throw new BiblioException("Membre existe deja: " + idMembre);

            // Ajout du membre.
            membres.inscrire(idMembre, nom, telephone, limitePret);
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * Suppression d'un membre de la base de donnees.
     */
    public void desinscrire(int idMembre) throws BiblioException, Exception
    {
        try
        {            
            // Vérifie si le membre existe et son nombre de pret en cours
            Membre membre = membres.getMembre(idMembre);
            if (membre == null)
                throw new BiblioException("Membre inexistant: " + idMembre);
            if (membre.getNbPret() > 0)
                throw new BiblioException("Le membre " + idMembre + " a encore des prêts.");
            if (reservations.getReservationMembre(idMembre) != null)
                throw new BiblioException("Membre " + idMembre + " a des réservations");

            // Suppression du membre
            if(!membres.desinscrire(idMembre))
                throw new BiblioException("Membre " + idMembre + " inexistant");
        }
        catch (Exception e)
        {
            throw e;
        }
    }
}// class
