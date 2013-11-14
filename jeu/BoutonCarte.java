package jeu;

import javax.swing.ImageIcon;
import javax.swing.JButton;

// Referenced classes of package jeu:
//            Carte

public class BoutonCarte extends JButton
{

    private static final long serialVersionUID = 1L;
    public Carte carte;

    public BoutonCarte(ImageIcon icon, Carte carte0)
    {
        super(icon);
        carte = carte0;
    }
}
