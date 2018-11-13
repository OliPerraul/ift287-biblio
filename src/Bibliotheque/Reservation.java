package Bibliotheque;

import java.util.Date;

import org.bson.Document;

/**
 * Permet de représenter une réservation.
 * 
 * <pre>
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 *
 * </pre>
 */

public class Reservation
{    
    private int m_idReservation;
    private int m_idLivre;
    private int m_idMembre;
    private Date m_dateReservation;
    
    public Reservation()
    {
    }
    
    public Reservation(Document d)
    {
    	m_idReservation = d.getInteger("idReservation");
    	m_idMembre = d.getInteger("idMembre");
    	m_idLivre = d.getInteger("idLivre");
    	m_dateReservation = d.getDate("dateReservation");
    }

    public Reservation(int idReservation, int idMembre, int idLivre, Date dateReservation)
    {
        m_idReservation = idReservation;
        m_idMembre = idMembre;
        m_idLivre = idLivre;
        m_dateReservation = dateReservation;
    }
    
    public int getIdReservation()
    {
        return m_idReservation;
    }

    public int getIdMembre()
    {
        return m_idMembre;
    }

    public int getIdLivre()
    {
        return m_idLivre;
    }

    public Date getDateReservation()
    {
        return m_dateReservation;
    }
    
    public Document toDocument()
    {
    	return new Document().append("idReservation", m_idReservation)
    			             .append("idMembre", m_idMembre)
    			             .append("idLivre", m_idLivre)
    			             .append("dateReservation", m_dateReservation);
    }
}
