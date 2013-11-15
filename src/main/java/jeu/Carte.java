package jeu;

import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

// Referenced classes of package jeu:
//            Couleur, BoutonCarte

public class Carte
{

    private final int valeur;
    private final Couleur couleur;
    
    private static Carte paquet[][];
    static {
    	paquet = new Carte[10][4];
    	for(int i = 0; i < 10; i++)
    	{
    		paquet[i][0] = new Carte(i + 1, Couleur.carreau);
    		paquet[i][1] = new Carte(i + 1, Couleur.coeur);
    		paquet[i][2] = new Carte(i + 1, Couleur.pique);
    		paquet[i][3] = new Carte(i + 1, Couleur.trefle);
    	}
    }
    public static Carte obtenirCarte(int a, Couleur couleur)
    {
        int indiceCouleur;
        switch(couleur)
        {
        case carreau: // '\002'
            indiceCouleur = 0;
            break;

        case coeur: // '\003'
            indiceCouleur = 1;
            break;

        case pique: // '\004'
            indiceCouleur = 2;
            break;

        case trefle: // '\001'
            indiceCouleur = 3;
            break;

        default:
            indiceCouleur = 60;
            break;
        }
        return paquet[a - 1][indiceCouleur];
    }

    private Carte(int a, Couleur b)
    {
        valeur = a;
        couleur = b;
    }

    public String toString()
    {
        return (new StringBuilder(String.valueOf(Integer.toString(valeur)))).append(couleur).toString();
    }

    public BoutonCarte bouton(ActionListener actionListener)
    {
        BoutonCarte bouton = new BoutonCarte(obtenirImage(), this);
        bouton.setMargin(new Insets(0, 0, 0, 0));
        bouton.addActionListener(actionListener);
        return bouton;
    }

    public JLabel image()
    {
        return new JLabel(obtenirImage());
    }

    public ImageIcon obtenirImageGrisee()
    {
        StringBuilder sb = new StringBuilder();
        sb.append((new StringBuilder("Images/")).append(valeur).append(".").toString());
        switch(couleur)
        {
        case pique: // '\004'
            sb.append("spade");
            break;

        case coeur: // '\003'
            sb.append("heart");
            break;

        case carreau: // '\002'
            sb.append("dia");
            break;

        case trefle: // '\001'
            sb.append("club");
            break;
        }
        sb.append(".2");
        sb.append(".png");
        return new ImageIcon(getClass().getResource(sb.toString()));
    }

    private ImageIcon obtenirImage()
    {
        StringBuilder sb = new StringBuilder();
        sb.append((new StringBuilder("Images/")).append(valeur).append(".").toString());
        switch(couleur)
        {
        case pique: // '\004'
            sb.append("spade");
            break;

        case coeur: // '\003'
            sb.append("heart");
            break;

        case carreau: // '\002'
            sb.append("dia");
            break;

        case trefle: // '\001'
            sb.append("club");
            break;
        }
        sb.append(".png");
        return new ImageIcon(getClass().getResource(sb.toString()));
    }

    public JLabel imageGrisee()
    {
        return new JLabel(obtenirImageGrisee());
    }

	public int getValeur() {
		return valeur;
	}

	public Couleur getCouleur() {
		return couleur;
	}


}
