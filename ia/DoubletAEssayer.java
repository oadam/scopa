package ia;

import jeu.Carte;
import jeu.Tas;

public class DoubletAEssayer
{

    public Carte carte;
    public Tas ramassees;

    public DoubletAEssayer(Carte carte0, Tas tas0)
    {
        carte = carte0;
        ramassees = tas0;
    }

    public String toString()
    {
        return (new StringBuilder(String.valueOf(carte.toString()))).append(ramassees).toString();
    }
}
