package Bibliotheque;

/**
 * Système de gestion d'une bibliothèque
 *
 * <pre>
 *
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Ce programme permet de gérer les transaction de base d'une
 * bibliothèque.  Il gère des livres, des membres et des
 * réservations. Les données sont conservées dans une base de
 * données NoSQL accédée avec MongoDBClient.
 *
 * Pré-condition
 *   Aucune
 *
 * Post-condition
 *   Le programme effectue les maj associées à chaque
 *   transaction
 * </pre>
 */
public class GestionBibliotheque
{
    private Connexion cx;
    private Livres livres;
    private Membres membres;
    private Reservations reservation;
    private GestionLivre gestionLivre;
    private GestionMembre gestionMembre;
    private GestionPret gestionPret;
    private GestionReservation gestionReservation;

    /**
     * Ouvre une connexion avec la BD NoSQL et alloue les gestionnaires
     * de transactions et de tables.
     * 
     * <pre>
     * 
     * @param serveur NoSQL (local ou dinf)
     * @param bd nom de la base de données
     * @param user user id pour établir une connexion avec le serveur NoSQL
     * @param password mot de passe pour le user id
     * </pre>
     */
    public GestionBibliotheque(String serveur, String bd, String user, String password)
            throws BiblioException
    {
        // allocation des objets pour le traitement des transactions
        cx = new Connexion(serveur, bd, user, password);
        livres = new Livres(cx);
        membres = new Membres(cx);
        reservation = new Reservations(cx);
        setGestionLivre(new GestionLivre(livres, reservation));
        setGestionMembre(new GestionMembre(membres, reservation));
        setGestionPret(new GestionPret(livres, membres, reservation));
        setGestionReservation(new GestionReservation(livres, membres, reservation));
    }

    public void fermer()
    {
        // fermeture de la connexion
        cx.fermer();
    }

    /**
     * @return the gestionLivre
     */
    public GestionLivre getGestionLivre()
    {
        return gestionLivre;
    }

    /**
     * @param gestionLivre the gestionLivre to set
     */
    private void setGestionLivre(GestionLivre gestionLivre)
    {
        this.gestionLivre = gestionLivre;
    }

    /**
     * @return the gestionMembre
     */
    public GestionMembre getGestionMembre()
    {
        return gestionMembre;
    }

    /**
     * @param gestionMembre the gestionMembre to set
     */
    private void setGestionMembre(GestionMembre gestionMembre)
    {
        this.gestionMembre = gestionMembre;
    }

    /**
     * @return the gestionPret
     */
    public GestionPret getGestionPret()
    {
        return gestionPret;
    }

    /**
     * @param gestionPret the gestionPret to set
     */
    private void setGestionPret(GestionPret gestionPret)
    {
        this.gestionPret = gestionPret;
    }

    /**
     * @return the gestionReservation
     */
    public GestionReservation getGestionReservation()
    {
        return gestionReservation;
    }

    /**
     * @param gestionReservation the gestionReservation to set
     */
    private void setGestionReservation(GestionReservation gestionReservation)
    {
        this.gestionReservation = gestionReservation;
    }
}
