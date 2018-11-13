package Bibliotheque;

import java.util.*;

/**
 * Gestion des transactions de reliées aux prêts de livres aux membres dans une
 * bibliothèque.
 *
 * <pre>
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 *
 * Ce programme permet de gérer les transactions prêter,
 * renouveler et retourner.
 *
 * Pré-condition
 *   la base de données de la bibliothèque doit exister
 *
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 * </pre>
 */

public class GestionPret
{
    private Livres livres;
    private Membres membres;
    private Reservations reservations;

    /**
     * Creation d'une instance. La connection de l'instance de livre et de
     * membre doit être la même que cx, afin d'assurer l'intégrité des
     * transactions.
     */
    public GestionPret(Livres livres, Membres membres, Reservations reservations) throws BiblioException
    {
        if (livres.getConnexion() != membres.getConnexion() || reservations.getConnexion() != membres.getConnexion())
            throw new BiblioException("Les collections d'objets n'utilisent pas la même connexion au serveur");
        this.livres = livres;
        this.membres = membres;
        this.reservations = reservations;
    }

    /**
     * Prêt d'un livre à un membre.
     *  Le livre ne doit pas être prêté. 
     *  Le membre ne doit pas avoir dépassé sa limite de pret.
     */
    public void preter(int idLivre, int idMembre, Date datePret) throws BiblioException, Exception
    {
        try
        {            
            // Verfier si le livre est disponible
            Livre livre = livres.getLivre(idLivre);
            if (livre == null)
                throw new BiblioException("Livre inexistant: " + idLivre);
            
            if (!livre.emprunteurNull())
                throw new BiblioException("Livre " + idLivre + " déjà prêté à " + livre.getEmprunteurId());

            // Vérifie si le membre existe et sa limite de pret
            Membre membre = membres.getMembre(idMembre);
            if (membre == null)
                throw new BiblioException("Membre inexistant: " + idMembre);
            if (membre.getNbPret() >= membre.getLimitePret())
                throw new BiblioException("Limite de prêt du membre " + idMembre + " atteinte");

            // Vérifie s'il existe une réservation pour le livre
            Reservation tupleReservation = reservations.getReservationLivre(idLivre);
            if (tupleReservation != null)
                throw new BiblioException("Livre réservé par : " + tupleReservation.getIdMembre() + " idReservation : "
                        + tupleReservation.getIdReservation());

            // Enregistrement du pret.
            livres.preter(idLivre, idMembre, datePret);
            membres.preter(idMembre);
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * Renouvellement d'un pret.
     *  Le livre doit être prêté.
     *  Le livre ne doit pas être réservé.
     */
    public void renouveler(int idLivre, Date datePret) throws BiblioException, Exception
    {
        try
        {
            // Verifier si le livre est prêté
            Livre livre = livres.getLivre(idLivre);
            if (livre == null)
                throw new BiblioException("Livre inexistant: " + idLivre);
            if (livre.emprunteurNull())
                throw new BiblioException("Livre " + idLivre + " n'est pas prêté");

            // Verifier si date renouvellement >= datePret
            if (datePret.before(livre.getDatePret()))
                throw new BiblioException("Date de renouvellement inferieure à la date de prêt");

            // Vérifie s'il existe une réservation pour le livre
            Reservation tupleReservation = reservations.getReservationLivre(idLivre);
            if (tupleReservation != null)
                throw new BiblioException("Livre réservé par : " + tupleReservation.getIdMembre() + " idReservation : "
                        + tupleReservation.getIdReservation());

            // Enregistrement du pret.
            livres.preter(idLivre, livre.getEmprunteurId(), datePret);
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * Retourner un livre prêté
     *  Le livre doit être prêté.
     */
    public void retourner(int idLivre, Date dateRetour) throws BiblioException, Exception
    {
        try
        {
            // Verifier si le livre est prêté
            Livre livre = livres.getLivre(idLivre);
            if (livre == null)
                throw new BiblioException("Livre inexistant: " + idLivre);
            if (livre.emprunteurNull())
                throw new BiblioException("Livre " + idLivre + " n'est pas prêté ");

            // Verifier si date retour >= datePret
            if (dateRetour.before(livre.getDatePret()))
                throw new BiblioException("Date de retour inferieure à la date de prêt");

            // Retour du pret.
            Membre emprunteur = membres.getMembre(livre.getEmprunteurId());
            if(emprunteur == null)
            	throw new BiblioException("Le membre est inexistant:" + livre.getEmprunteurId());
            livres.retourner(idLivre);
            membres.retourner(livre.getEmprunteurId());
        }
        catch (Exception e)
        {
            throw e;
        }
    }
}
