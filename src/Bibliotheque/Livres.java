package Bibliotheque;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

/**
 * Permet d'effectuer les accès à la collection des Livres.
 * 
 * <pre>
 * 
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Cette classe gère tous les accès à la collection des Livres.
 * 
 * </pre>
 */

public class Livres
{
    private Connexion cx;
    private MongoCollection<Document> livresCollection;

    /**
     * Creation d'une instance.
     */
    public Livres(Connexion cx)
    {
        this.cx = cx;
        livresCollection = cx.getDatabase().getCollection("Livres");
    }

    /**
     * Retourner la connexion associée.
     */
    public Connexion getConnexion()
    {
        return cx;
    }

    /**
     * Verifie si un livre existe.
     */
    public boolean existe(int idLivre)
    {
    	return livresCollection.find(eq("idLivre", idLivre)).first() != null;
    }

    /**
     * Lecture d'un livre.
     */
    public Livre getLivre(int idLivre)
    {
    	Document l = livresCollection.find(eq("idLivre", idLivre)).first();
    	if(l != null)
    	{
    		return new Livre(l);
    	}
        return null;
    }

    /**
     * Ajout d'un nouveau livre dans la base de donnees.
     */
    public void acquerir(int idLivre, String titre, String auteur, Date dateAcquisition)
    {
        Livre l = new Livre(idLivre, titre, auteur, dateAcquisition);
        
        // Ajout du livre.
        livresCollection.insertOne(l.toDocument());
    }

    /**
     * Suppression d'un livre.
     */
    public boolean vendre(int idLivre)
    {
    	return livresCollection.deleteOne(eq("idLivre", idLivre)).getDeletedCount() > 0;
    }
    
    public void preter(int idLivre, int idMembre, Date datePret)
    {
    	livresCollection.updateOne(eq("idLivre", idLivre), combine(set("idMembre", idMembre), set("idMembreNull", false), set("datePret", datePret)));
    }
    
    public void retourner(int idLivre)
    {
    	livresCollection.updateOne(eq("idLivre", idLivre), set("idMembre", -1));
    	livresCollection.updateOne(eq("idLivre", idLivre), set("idMembreNull", true));
    	livresCollection.updateOne(eq("idLivre", idLivre), set("datePret", null));
    }

    public List<Livre> calculerListePret(int idMembre)
    {
    	List<Livre> liste = new LinkedList<Livre>();
        MongoCursor<Document> livres = livresCollection.find(eq("idMembre", idMembre)).iterator();
        try
        {
            while (livres.hasNext())
            {
                liste.add(new Livre(livres.next()));
            }
        }
        finally
        {
            livres.close();
        }
        
        return liste;
    }
    
    public void listerLivres()
    {        
        MongoCursor<Document> livres = livresCollection.find().iterator();
        try
        {
            while (livres.hasNext())
            {
            	Livre l = new Livre(livres.next());
            	System.out.println(l.toString());
            }
        }
        finally
        {
            livres.close();
        }
    }
}
