package Bibliotheque;

import java.util.Date;

import org.bson.Document;

/**
 * Permet de représenter un livre.
 * 
 * <pre>
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * </pre>
 */

public class Livre
{
    private int m_idLivre;
    private String m_titre;
    private String m_auteur;
    private Date m_dateAcquisition;

    private int m_idMembre;
    private boolean m_membreNull;
    private Date m_datePret;

    public Livre()
    {
    }
    
    public Livre(Document d)
    {
    	m_idLivre = d.getInteger("idLivre");
    	m_titre = d.getString("titre");
    	m_auteur = d.getString("auteur");
    	m_dateAcquisition = d.getDate("dateAcquisition");
    	m_idMembre = d.getInteger("idMembre");
    	m_membreNull = d.getBoolean("idMembreNull");
    	m_datePret = d.getDate("datePret");
    }

    public Livre(int idLivre, String titre, String auteur, Date dateAcquisition)
    {
        m_idLivre = idLivre;
        m_titre = titre;
        m_auteur = auteur;
        m_dateAcquisition = dateAcquisition;
        m_idMembre = -1;
        m_membreNull = true;
        m_datePret = null;
    }
    
    public int getIdLivre()
    {
        return m_idLivre;
    }

    public String getTitre()
    {
        return m_titre;
    }

    public String getAuteur()
    {
        return m_auteur;
    }

    public Date getDateAcquisition()
    {
        return m_dateAcquisition;
    }
    
    public boolean emprunteurNull()
    {
    	return m_membreNull;
    }

    public void preter(int membreId, Date datePret)
    {
        m_idMembre = membreId;
        m_membreNull = false;
        m_datePret = datePret;
    }

    public int getEmprunteurId()
    {
        return m_idMembre;
    }

    public Date getDatePret()
    {
        return m_datePret;
    }

    public void retourner()
    {
        m_membreNull = true;
        m_idMembre = -1;
        m_datePret = null;
    }

    public String toString()
    {
        StringBuffer s = new StringBuffer(getTitre() + " " + getAuteur() + " " + getDateAcquisition());
        if (!emprunteurNull())
            s.append(" prêté à " + m_idMembre + " " + getDatePret());
        return s.toString();
    }
    
    public Document toDocument()
    {
    	return new Document().append("idLivre", m_idLivre)
    			             .append("titre", m_titre)
    			             .append("auteur", m_auteur)
    			             .append("dateAcquisition", m_dateAcquisition)
    			             .append("idMembre", m_idMembre)
    			             .append("idMembreNull", m_membreNull)
    			             .append("datePret", m_datePret);
    }
}
