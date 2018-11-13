package Bibliotheque;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

/**
 * Permet d'effectuer les accès à la collection des Membres.
 * 
 * <pre>
 *
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Cette classe gère tous les accès à la collection des Membres.
 *
 * </pre>
 */

public class Membres
{
    private MongoCollection<Document> membresCollection;
    private Connexion cx;

    /**
     * Creation d'une instance.
     */
    public Membres(Connexion cx)
    {
        this.cx = cx;
        membresCollection = cx.getDatabase().getCollection("Membres");
    }

    /**
     * Retourner la connexion associée.
     */
    public Connexion getConnexion()
    {
        return cx;
    }

    /**
     * Verifie si un membre existe.
     */
    public boolean existe(int idMembre)
    {
    	return membresCollection.find(eq("idMembre", idMembre)).first() != null;
    }

    /**
     * Lecture d'un membre.
     */
    public Membre getMembre(int idMembre)
    {
    	Document d = membresCollection.find(eq("idMembre", idMembre)).first();
    	if(d != null)
    	{
    		return new Membre(d);
    	}
        return null;
    }

    /**
     * Ajout d'un nouveau membre.
     */
    public void inscrire(int idMembre, String nom, long telephone, int limitePret)
    {
        Membre m = new Membre(idMembre, nom, telephone, limitePret);
        membresCollection.insertOne(m.toDocument());
    }

    /**
     * Suppression d'un membre.
     */
    public boolean desinscrire(int idMembre)
    {
    	return membresCollection.deleteOne(eq("idMembre", idMembre)).getDeletedCount() > 0;
    }
    
    public void preter(int idMembre)
    {
    	membresCollection.updateOne(eq("idMembre", idMembre), inc("nbPret", 1));
    }
    
    public void retourner(int idMembre)
    {
    	membresCollection.updateOne(eq("idMembre", idMembre), inc("nbPret", -1));
    }
}
