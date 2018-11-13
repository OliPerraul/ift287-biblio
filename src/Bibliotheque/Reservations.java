package Bibliotheque;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import java.util.Date;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

/**
 * Permet d'effectuer les accès à la collections des réservations.
 * 
 * <pre>
 *
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Cette classe gère tous les accès à la collection des réservations.
 *
 * </pre>
 */

public class Reservations
{
	private MongoCollection<Document> reservationsCollection;
    private Connexion cx;

    /**
     * Creation d'une instance.
     */
    public Reservations(Connexion cx)
    {
        this.cx = cx;
        reservationsCollection = cx.getDatabase().getCollection("Reservations");
    }

    /**
     * Retourner la connexion associée.
     */
    public Connexion getConnexion()
    {
        return cx;
    }

    /**
     * Verifie si une reservation existe.
     */
    public boolean existe(int idReservation)
    {
    	return reservationsCollection.find(eq("idReservation", idReservation)).first() != null;
    }

    /**
     * Lecture d'une reservation.
     */
    public Reservation getReservation(int idReservation)
    {
    	Document d = reservationsCollection.find(eq("idReservation", idReservation)).first();
    	if(d != null)
    	{
    		return new Reservation(d);
    	}
        return null;
    }

    /**
     * Lecture de la première reservation d'un livre.
     */
    public Reservation getReservationLivre(int idLivre)
    {
    	Document d = reservationsCollection.find(eq("idLivre", idLivre)).sort(ascending("dateReservation")).first();
    	if(d != null)
    	{
    		return new Reservation(d);
    	}
        return null;
    }

    /**
     * Lecture de la première reservation d'un livre.
     */
    public Reservation getReservationMembre(int idMembre)
    {
    	Document d = reservationsCollection.find(eq("idMembre", idMembre)).sort(ascending("dateReservation")).first();
    	if(d != null)
    	{
    		return new Reservation(d);
    	}
        return null;
    }

    /**
     * Réservation d'un livre.
     */
    public void reserver(int idReservation, int idLivre, int idMembre, Date dateReservation)
    {
        Reservation r = new Reservation(idReservation, idMembre, idLivre, dateReservation);
        reservationsCollection.insertOne(r.toDocument());
    }

    /**
     * Suppression d'une reservation.
     */
    public boolean annulerRes(int idReservation)
    {
    	return reservationsCollection.deleteOne(eq("idReservation", idReservation)).getDeletedCount() > 0;
    }
}
