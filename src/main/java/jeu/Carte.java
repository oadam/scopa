package jeu;

import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

// Referenced classes of package jeu:
//            Couleur, BoutonCarte

public enum Carte
{
	CARREAU_1(1, Couleur.carreau),
	CARREAU_2(2, Couleur.carreau),
	CARREAU_3(3, Couleur.carreau),
	CARREAU_4(4, Couleur.carreau),
	CARREAU_5(5, Couleur.carreau),
	CARREAU_6(6, Couleur.carreau),
	CARREAU_7(7, Couleur.carreau),
	CARREAU_8(8, Couleur.carreau),
	CARREAU_9(9, Couleur.carreau),
	CARREAU_10(10, Couleur.carreau),
	PIQUE_1(1, Couleur.pique),
	PIQUE_2(2, Couleur.pique),
	PIQUE_3(3, Couleur.pique),
	PIQUE_4(4, Couleur.pique),
	PIQUE_5(5, Couleur.pique),
	PIQUE_6(6, Couleur.pique),
	PIQUE_7(7, Couleur.pique),
	PIQUE_8(8, Couleur.pique),
	PIQUE_9(9, Couleur.pique),
	PIQUE_10(10, Couleur.pique),
	TREFLE_1(1, Couleur.trefle),
	TREFLE_2(2, Couleur.trefle),
	TREFLE_3(3, Couleur.trefle),
	TREFLE_4(4, Couleur.trefle),
	TREFLE_5(5, Couleur.trefle),
	TREFLE_6(6, Couleur.trefle),
	TREFLE_7(7, Couleur.trefle),
	TREFLE_8(8, Couleur.trefle),
	TREFLE_9(9, Couleur.trefle),
	TREFLE_10(10, Couleur.trefle),
	COEUR_1(1, Couleur.coeur),
	COEUR_2(2, Couleur.coeur),
	COEUR_3(3, Couleur.coeur),
	COEUR_4(4, Couleur.coeur),
	COEUR_5(5, Couleur.coeur),
	COEUR_6(6, Couleur.coeur),
	COEUR_7(7, Couleur.coeur),
	COEUR_8(8, Couleur.coeur),
	COEUR_9(9, Couleur.coeur),
	COEUR_10(10, Couleur.coeur),
	;
	
    private final int valeur;
    private final Couleur couleur;
    
    
    
    private static Carte paquet[][];
    static {
    	paquet = new Carte[10][Couleur.values().length];
    	for (Carte carte : Carte.values()) {
			paquet[carte.valeur - 1][carte.couleur.ordinal()] = carte;
		}
    }
    public static Carte obtenirCarte(int a, Couleur couleur)
    {
        return paquet[a - 1][couleur.ordinal()];
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
