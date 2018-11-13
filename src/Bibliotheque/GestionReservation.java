package Bibliotheque;

import java.util.Date;

/**
 * Gestion des transactions de reliées aux réservations de livres par les
 * membres dans une bibliothèque.
 *
 * <pre>
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 *
 * Ce programme permet de gérer les transactions réserver,
 * prendre et annuler.
 *
 * Pré-condition
 *   la base de données de la bibliothèque doit exister
 *
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 * </pre>
 */

public class GestionReservation
{
    private Livres livres;
    private Membres membres;
    private Reservations reservations;

    /**
     * Creation d'une instance. La connection de l'instance de livre et de
     * membre doit être la même que cx, afin d'assurer l'intégrité des
     * transactions.
     */
    public GestionReservation(Livres livres, Membres membres, Reservations reservations) throws BiblioException
    {
        if (livres.getConnexion() != membres.getConnexion() || reservations.getConnexion() != membres.getConnexion())
            throw new BiblioException("Les collections d'objets n'utilisent pas la même connexion au serveur");
        this.livres = livres;
        this.membres = membres;
        this.reservations = reservations;
    }

    /**
     * Réservation d'un livre par un membre. Le livre doit être prêté.
     */
    public void reserver(int idReservation, int idLivre, int idMembre, Date dateReservation)
            throws BiblioException, Exception
    {
        try
        {            
            // Verifier que le livre est preté
            Livre tupleLivre = livres.getLivre(idLivre);
            if (tupleLivre == null)
                throw new BiblioException("Livre inexistant: " + idLivre);
            if (tupleLivre.emprunteurNull())
                throw new BiblioException("Livre " + idLivre + " n'est pas prêté");
            if (tupleLivre.getEmprunteurId() == idMembre)
                throw new BiblioException("Livre " + idLivre + " déjà prêté à ce membre");

            // Vérifier que le membre existe
            Membre tupleMembre = membres.getMembre(idMembre);
            if (tupleMembre == null)
                throw new BiblioException("Membre inexistant: " + idMembre);

            // Verifier si date reservation >= datePret
            if (dateReservation.before(tupleLivre.getDatePret()))
                throw new BiblioException("Date de reservation inferieure à la date de pret");

            // Vérifier que la réservation n'existe pas
            if (reservations.existe(idReservation))
                throw new BiblioException("Réservation " + idReservation + " existe déjà");

            // Création de la reservation
            reservations.reserver(idReservation, idLivre, idMembre, dateReservation);
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * Prise d'une réservation. Le livre ne doit pas être prêté. Le membre ne
     * doit pas avoir dépassé sa limite de pret. La réservation doit être la
     * première en liste.
     */
    public void prendreRes(int idReservation, Date datePret) throws BiblioException, Exception
    {
        try
        {
            // Vérifie s'il existe une réservation pour le livre
            Reservation reservation = reservations.getReservation(idReservation);
            if (reservation == null)
                throw new BiblioException("Réservation inexistante : " + idReservation);

            // Vérifie que c'est la première réservation pour le livre
            Reservation tupleReservationPremiere = reservations.getReservationLivre(reservation.getIdLivre());
            if (reservation.getIdReservation() != tupleReservationPremiere.getIdReservation())
                throw new BiblioException("La réservation n'est pas la première de la liste "
                        + "pour ce livre; la premiere est " + tupleReservationPremiere.getIdReservation());

            // Vérifier si le livre est disponible
            Livre livre = livres.getLivre(reservation.getIdLivre());
            if (livre == null)
                throw new BiblioException("Livre inexistant: " + reservation.getIdLivre());
            if (!livre.emprunteurNull())
                throw new BiblioException("Livre " + livre.getIdLivre() + " déjà prêté à " + livre.getEmprunteurId());

            // Vérifie si le membre existe et sa limite de pret
            Membre membre = membres.getMembre(reservation.getIdMembre());
            if (membre == null)
                throw new BiblioException("Membre inexistant: " + reservation.getIdMembre());
            if (membre.getNbPret() >= membre.getLimitePret())
                throw new BiblioException("Limite de prêt du membre " + reservation.getIdMembre() + " atteinte");

            // Verifier si datePret >= tupleReservation.dateReservation
            if (datePret.before(reservation.getDateReservation()))
                throw new BiblioException("Date de prêt inférieure à la date de réservation");

            // Enregistrement du pret.
            livres.preter(reservation.getIdLivre(), reservation.getIdMembre(), datePret);
            membres.preter(reservation.getIdMembre());

            // Eliminer la réservation
            if(!reservations.annulerRes(idReservation))
                throw new BiblioException("Erreur lors de l'annulation de la réservation");
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * Annulation d'une réservation. La réservation doit exister.
     */
    public void annulerRes(int idReservation) throws BiblioException, Exception
    {
        try
        {
            // Vérifier que la réservation existe
            Reservation r = reservations.getReservation(idReservation);
            if(r == null)
                throw new BiblioException("Réservation " + idReservation + " inexistante.");
            
            if (!reservations.annulerRes(idReservation))
                throw new BiblioException("Erreur lors de l'annulation de la réservation " + idReservation);
        }
        catch (Exception e)
        {
            throw e;
        }
    }
}
