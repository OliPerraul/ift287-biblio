package Bibliotheque;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

/**
 * Gestionnaire d'une connexion avec une BD NoSQL via MongoDB.
 * 
 * <pre>
 * 
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Ce programme permet d'ouvrir une connexion avec une BD via MongoDB.
 * 
 * Pre-condition
 *   La base de données MongoDB doit etre accessible.
 * 
 * Post-condition
 *   La connexion est ouverte.
 * </pre>
 */
public class Connexion
{
    private MongoClient client;
    private MongoDatabase database;

    /**
     * Ouverture d'une connexion
     * 
     * @serveur serveur à utiliser (local ou dinf)
     * @bd nom de la base de données
     * @user userid sur le serveur MongoDB pour la BD specifiée
     * @pass mot de passe sur le serveur MongoDB pour la BD specifiée
     */
    public Connexion(String serveur, String bd, String user, String pass) throws BiblioException
    {
    	if (serveur.equals("local"))
        {
            client = new MongoClient();
        }
        else if (serveur.equals("dinf"))
        {
        	MongoClientURI uri = new MongoClientURI("mongodb://"+user+":"+pass+"@bd-info2.dinf.usherbrooke.ca:27017/"+bd+"?ssl=false");
        	client = new MongoClient(uri);
        }
        else
        {
            throw new BiblioException("Serveur inconnu");
        }
        
    	database = client.getDatabase(bd);
    	
    	System.out.println("Ouverture de la connexion :\n"
                + "Connecté sur la BD MongoDB "
                + bd + " avec l'utilisateur " + user);
    }

    /**
     * fermeture d'une connexion
     */
    public void fermer()
    {
        client.close();
        System.out.println("Connexion fermée");
    }
    
    
    /**
     * retourne la Connection MongoDB
     */
    public MongoClient getConnection()
    {
        return client;
    }
    
    /**
     * retourne la DataBase MongoDB
     */
    public MongoDatabase getDatabase()
    {
        return database;
    }
}// Classe Connexion
