package Bibliotheque;

import org.bson.Document;

/**
 * Permet de représenter un membre.
 * 
 * <pre>
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 *
 * </pre>
 */

public class Membre
{
    private int m_idMembre;
    private String m_nom;
    private long m_telephone;
    private int m_limitePret;

    private int m_nbPret;

    public Membre()
    {
    }
    
    public Membre(Document d)
    {
    	m_idMembre = d.getInteger("idMembre");
    	m_nom = d.getString("nom");
    	m_telephone = d.getLong("telephone");
    	m_limitePret = d.getInteger("limitePret");
    	m_nbPret = d.getInteger("nbPret");
    }

    public Membre(int idMembre, String nom, long telephone, int limitePret)
    {
        m_idMembre = idMembre;
        m_nom = nom;
        m_telephone = telephone;
        m_limitePret = limitePret;
        m_nbPret = 0;
    }
    
    public int getIdMembre()
    {
        return m_idMembre;
    }

    public String getNom()
    {
        return m_nom;
    }

    public long getTelephone()
    {
        return m_telephone;
    }

    public int getLimitePret()
    {
        return m_limitePret;
    }
    
    public int getNbPret()
    {
        return m_nbPret;
    }
    
    public void ajoutePret()
    {
        m_nbPret++;
    }
    
    public void retournerEmprunt()
    {
        m_nbPret--;
    }
    
    public Document toDocument()
    {
    	return new Document().append("idMembre", m_idMembre)
    			             .append("nom", m_nom)
    			             .append("telephone", m_telephone)
    			             .append("limitePret", m_limitePret)
    			             .append("nbPret", m_nbPret);
    }
}
