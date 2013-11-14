package scopa;

import jeu.Tas;

public class Joueur
{

    public Tas jeu;
    public Tas ramassees;
    public int score;
    public int nombreDeScopa;

    Joueur()
    {
        score = 0;
        nombreDeScopa = 0;
        jeu = new Tas();
        ramassees = new Tas();
    }

    public void clear()
    {
        jeu.clear();
        ramassees.clear();
        nombreDeScopa = 0;
    }

    public Joueur clone()
    {
        Joueur retour = new Joueur();
        retour.jeu = jeu.clone();
        retour.ramassees = ramassees.clone();
        retour.score = score;
        retour.nombreDeScopa = nombreDeScopa;
        return retour;
    }

    public int score()
    {
        return Math.min(ramassees.score() + nombreDeScopa, 21);
    }


}