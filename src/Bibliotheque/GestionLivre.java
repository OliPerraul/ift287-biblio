package Bibliotheque;

import java.util.Date;

/**
 * Gestion des transactions de reliées à la création et suppresion de livres
 * dans une bibliothèque.
 *
 * <pre>
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 *
 * Ce programme permet de gérer les transaction reliées à la 
 * création et suppresion de livres.
 *
 * Pré-condition
 *   la base de données de la bibliothèque doit exister
 *
 * Post-condition
 *   le programme effectue les maj associées à chaque
 *   transaction
 * </pre>
 */
public class GestionLivre
{
    private Livres livres;
    private Reservations reservations;

    /**
     * Creation d'une instance
     */
    public GestionLivre(Livres livres, Reservations reservations) throws BiblioException
    {
        if (livres.getConnexion() != reservations.getConnexion())
            throw new BiblioException("Les collections d'objets n'utilisent pas la même connexion au serveur");
        this.livres = livres;
        this.reservations = reservations;
    }

    /**
     * Ajout d'un nouveau livre dans la base de données. S'il existe deja, une
     * exception est levée.
     */
    public void acquerir(int idLivre, String titre, String auteur, Date dateAcquisition)
            throws BiblioException, Exception
    {
        try
        {            
            // Vérifie si le livre existe déja
            if (livres.existe(idLivre))
                throw new BiblioException("Livre existe deja: " + idLivre);

            // Ajout du livre dans la table des livres
            livres.acquerir(idLivre, titre, auteur, dateAcquisition);
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * Vente d'un livre.
     */
    public void vendre(int idLivre) throws BiblioException, Exception
    {
        try
        {            
            Livre livre = livres.getLivre(idLivre);
            if (livre == null)
                throw new BiblioException("Livre inexistant: " + idLivre);
            if (!livre.emprunteurNull())
                throw new BiblioException("Livre " + idLivre + " prete a " + livre.getEmprunteurId());
            if (reservations.getReservationLivre(idLivre) != null)
                throw new BiblioException("Livre " + idLivre + " réservé ");

            // Suppression du livre.
            if(!livres.vendre(idLivre))
                throw new BiblioException("Livre " + idLivre + " inexistant");
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    
    public void listerLivres()
    {
    	livres.listerLivres();
    }
}
