package Bibliotheque;

/**
 * L'exception BiblioException est levée lorsqu'une transaction est inadéquate.
 * Par exemple -- livre inexistant
 */
public final class BiblioException extends Exception
{
	private static final long serialVersionUID = 1L;

	public BiblioException(String message)
	{
		super(message);
	}
}