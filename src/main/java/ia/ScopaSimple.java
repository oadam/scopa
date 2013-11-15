package ia;

import java.util.Iterator;
import java.util.LinkedList;

import jeu.Carte;
import jeu.Tas;
import scopa.Joueur;
import scopa.Scopa;

public class ScopaSimple
{

    Tas donne;
    Tas central;
    public Joueur ordi;
    public Joueur homme;
    Tas donne0;
    Tas central0;
    Joueur ordi0;
    Joueur homme0;
    int difficulte;
    boolean tourDeHomme;
    private boolean fini;
    private boolean modeSixCarte;

    public ScopaSimple(Tas _donne, Tas _central, Joueur _ordi, Joueur _homme, int difficulte0, boolean modeSixCarte0)
    {
        donne0 = _donne;
        central0 = _central;
        ordi0 = _ordi;
        homme0 = _homme;
        difficulte = difficulte0;
        modeSixCarte = modeSixCarte0;
        reset();
    }

    public void reset()
    {
        donne = donne0.clone();
        central = central0.clone();
        ordi = ordi0.clone();
        homme = homme0.clone();
        tourDeHomme = true;
        fini = false;
    }

    private void distribuer()
    {
        if(difficulte != Scopa.EXPERT)
        {
            for(int i = 0; i < (modeSixCarte ? 6 : 3); i++)
            {
                ordi.jeu.add(donne.retireCarteAleatoire());
                homme.jeu.add(donne.retireCarteAleatoire());
            }

        } else
        {
            for(int i = 0; i < (modeSixCarte ? 6 : 3); i++)
            {
                ordi.jeu.add(donne.removeFirst());
                homme.jeu.add(donne.removeFirst());
            }

        }
    }

    public void toutJouer()
    {
        if(difficulte != Scopa.EXPERT)
        {
            int nombreDeCarteHomme = homme.jeu.size();
            donne.addAll(homme.jeu);
            homme.jeu.clear();
            for(int i = 0; i < nombreDeCarteHomme; i++)
            {
                homme.jeu.add(donne.retireCarteAleatoire());
            }

        }
        while(!fini) 
        {
            if(tourDeHomme)
            {
                jouer(homme, homme.jeu.retireCarteAleatoire());
            } else
            {
                jouer(ordi, ordi.jeu.retireCarteAleatoire());
            }
        }
    }

    private void jouer(Joueur joueur, Carte carte)
    {
        if(carte.getValeur() == 1)
        {
            jouerPourDeBon(joueur, carte, central.clone());
        } else
        {
            LinkedList<Tas> possibilitees = new LinkedList<Tas>();
            boolean possibleDeRamasserUn = false;
            Iterator<Tas> iterator = central.combinaisons().iterator();
            while(iterator.hasNext()) 
            {
                Tas element = (Tas)iterator.next();
                if(element.somme() != carte.getValeur())
                {
                    continue;
                }
                if(element.size() == 1)
                {
                    possibleDeRamasserUn = true;
                } else
                if(possibleDeRamasserUn)
                {
                    break;
                }
                possibilitees.add(element);
            }
            switch(possibilitees.size())
            {
            case 0: // '\0'
                jouerPourDeBon(joueur, carte, null);
                break;

            case 1: // '\001'
                jouerPourDeBon(joueur, carte, possibilitees.getFirst());
                break;

            default:
                jouerPourDeBon(joueur, carte, indecision(carte, possibilitees));
                break;
            }
        }
    }

    public void jouerPourDeBon(Joueur joueur, Carte carte, Tas ramasse)
    {
        joueur.jeu.remove(carte);
        tourDeHomme = !joueur.equals(homme);
        if(ramasse == null)
        {
            central.add(carte);
        } else
        {
            central.removeAll(ramasse);
            joueur.ramassees.add(carte);
            joueur.ramassees.addAll(ramasse);
            if(central.isEmpty() && carte.getValeur() != 1)
            {
                joueur.nombreDeScopa++;
            }
        }
        if(homme.jeu.isEmpty() && ordi.jeu.isEmpty())
        {
            if(donne.isEmpty())
            {
                fini = true;
            } else
            {
                distribuer();
            }
        }
    }

    private Tas indecision(Carte carte, LinkedList<Tas> possibilitees)
    {
        Tas retour = null;
        for(Iterator<Tas> iterator = possibilitees.iterator(); iterator.hasNext();)
        {
            Tas courant = iterator.next();
            if(courant.containsAll(central))
            {
                System.out.println("\347a marche");
                retour = courant;
                break;
            }
        }

        if(retour != null)
        {
            return retour;
        } else
        {
            return possibilitees.get((int)((double)possibilitees.size() * Math.random()));
        }
    }

    public int deltaScore()
    {
        return ordi.score() - homme.score()+homme.nombreDeScopa;//par ce que c'est mieux
    }
}
