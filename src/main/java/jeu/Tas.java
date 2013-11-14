package jeu;

import java.util.Iterator;
import java.util.LinkedList;

// Referenced classes of package jeu:
//            Carte, Couleur

public class Tas extends LinkedList<Carte>
{

    private static final long serialVersionUID = 0L;

    public Tas()
    {
    }

    public Carte retireCarteAleatoire()
    {
        return (Carte)remove((int)(Math.random() * (double)size()));
    }

    public static Tas Donne()
    {
        Tas source = new Tas();
        for(int i = 1; i < 11; i++)
        {
            source.add(Carte.obtenirCarte(i, Couleur.carreau));
            source.add(Carte.obtenirCarte(i, Couleur.trefle));
            source.add(Carte.obtenirCarte(i, Couleur.pique));
            source.add(Carte.obtenirCarte(i, Couleur.coeur));
        }

        return source.cloneMelange();
    }

    public Tas cloneMelange()
    {
        Tas retour = new Tas();
        for(int i = 0; i < 40; i++)
        {
            retour.add(retireCarteAleatoire());
        }

        return retour;
    }

    public LinkedList<Tas> combinaisons()
    {
        LinkedList<Tas> retour = new LinkedList<Tas>();
        int size = size();
        Tas element = new Tas();
        for(int i = 0; i < size; i++)
        {
            element.clear();
            element.add((Carte)get(i));
            retour.add(element.clone());
        }

        if(size > 1)
        {
            for(int i = 0; i < size; i++)
            {
                for(int j = i + 1; j < size; j++)
                {
                    element.clear();
                    element.add((Carte)get(i));
                    element.add((Carte)get(j));
                    retour.add(element.clone());
                }

            }

        }
        if(size > 2)
        {
            for(int i = 0; i < size; i++)
            {
                for(int j = i + 1; j < size; j++)
                {
                    for(int k = j + 1; k < size; k++)
                    {
                        element.clear();
                        element.add((Carte)get(i));
                        element.add((Carte)get(j));
                        element.add((Carte)get(k));
                        retour.add(element.clone());
                    }

                }

            }

        }
        if(size > 3)
        {
            for(int i = 0; i < size; i++)
            {
                for(int j = i + 1; j < size; j++)
                {
                    for(int k = j + 1; k < size; k++)
                    {
                        for(int l = k + 1; l < size; l++)
                        {
                            element.clear();
                            element.add((Carte)get(i));
                            element.add((Carte)get(j));
                            element.add((Carte)get(k));
                            element.add((Carte)get(l));
                            retour.add(element.clone());
                        }

                    }

                }

            }

        }
        return retour;
    }

    public int score()
    {
        int carreau = 0;
        int score = 0;
        int nombreCarte = 0;
        double sept = 0.0D;
        boolean unDeuxTrois[] = new boolean[10];
        for(Iterator<?> iterator = iterator(); iterator.hasNext();)
        {
            Carte carte = (Carte)iterator.next();
            int valeur = carte.valeur;
            if(carte.couleur == Couleur.carreau)
            {
                carreau++;
                if(valeur == 7)
                {
                    score++;
                }
                unDeuxTrois[valeur - 1] = true;
            }
            nombreCarte++;
            if(valeur <= 7)
            {
                sept += Math.pow(10D, valeur - 7);
            }
        }

        if(carreau > 5)
        {
            score++;
        }
        if(nombreCarte > 20)
        {
            score++;
        }
        if(sept > 2.2222222199999999D)
        {
            score++;
        }
        int n;
        for(n = 0; unDeuxTrois[n];)
        {
            if(++n == 10)
            {
                return 21;
            }
        }

        if(n >= 3)
        {
            score += n;
        }
        return score;
    }

    public int[] resultat()
    {
        int retour[] = new int[4];
        int carreau = 0;
        int nombreCarte = 0;
        double sept = 0.0D;
        for(Iterator<?> iterator = iterator(); iterator.hasNext();)
        {
            Carte carte = (Carte)iterator.next();
            int valeur = carte.valeur;
            if(carte.couleur == Couleur.carreau)
            {
                carreau++;
                if(valeur == 7)
                {
                    retour[3] = 1;
                }
            }
            nombreCarte++;
            if(valeur <= 7)
            {
                sept += Math.pow(10D, valeur - 7);
            }
        }

        retour[0] = sept <= 2.2222222199999999D ? 0 : 1;
        retour[1] = nombreCarte;
        retour[2] = carreau;
        return retour;
    }

    public int somme()
    {
        int retour = 0;
        for(int i = 0; i < size(); i++)
        {
            retour += ((Carte)get(i)).valeur;
        }

        return retour;
    }

    public Tas clone()
    {
        return (Tas)super.clone();
    }

  
}
