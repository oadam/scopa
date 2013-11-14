package scopa;

import java.awt.*;
import java.util.Iterator;
import javax.swing.*;
import jeu.Carte;
import jeu.Tas;

// Referenced classes of package scopa:
//            Scopa, Joueur

public class Fenetre extends JFrame
{

    private static final long serialVersionUID = 1L;
    JMenuItem modeSixCartes;
    JMenuItem modeTroisCartes;
    JMenuItem rejouer;
    JMenuItem statistiques;
    JMenuItem RAZ;
    JMenuItem quitter;
    JMenuItem facile;
    JMenuItem moyen;
    JMenuItem difficile;
    JMenuItem expert;
    JMenuItem aPropos;
    JMenuItem regles;
    JPanel panel;
    JPanel haut;
    JPanel milieu;
    JPanel bas;
    private JMenuBar menu;
    private JMenu fichier;
    private JMenu difficulte;
    private JMenu aide;

    public Fenetre(Scopa scopa,boolean modeApplet)
    {
        modeSixCartes = new JMenuItem("Mode 6 cartes");
        modeTroisCartes = new JMenuItem("Mode 3 cartes");
        rejouer = new JMenuItem("Rejouer");
        statistiques = new JMenuItem("Statistiques");
        RAZ = new JMenuItem("RAZ Statistiques");
        quitter = new JMenuItem("Quitter");
        facile = new JMenuItem("Facile");
        moyen = new JMenuItem("Moyen");
        difficile = new JMenuItem("Difficile");
        expert = new JMenuItem("Expert");
        aPropos = new JMenuItem("A propos");
        regles = new JMenuItem("R\350gles");
        panel = new JPanel();
        haut = new JPanel();
        milieu = new JPanel();
        bas = new JPanel();
        menu = new JMenuBar();
        fichier = new JMenu("Fichier");
        difficulte = new JMenu("Difficult\351");
        aide = new JMenu("Aide");
        setTitle("Scopa");
        setSize(780, 390);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(3);
        rejouer.addActionListener(scopa);
        statistiques.addActionListener(scopa);
        RAZ.addActionListener(scopa);
        quitter.addActionListener(scopa);
        modeTroisCartes.addActionListener(scopa);
        modeSixCartes.addActionListener(scopa);
        facile.addActionListener(scopa);
        moyen.addActionListener(scopa);
        difficile.addActionListener(scopa);
        expert.addActionListener(scopa);
        aPropos.addActionListener(scopa);
        regles.addActionListener(scopa);
        fichier.add(rejouer);
        if(!modeApplet){
        	fichier.add(statistiques);
        	fichier.add(RAZ);
        }
        fichier.add(quitter);
        difficulte.add(facile);
        difficulte.add(moyen);
        difficulte.add(difficile);
        difficulte.add(expert);
        difficulte.add(modeTroisCartes);
        difficulte.add(modeSixCartes);
        aide.add(regles);
        aide.add(aPropos);
        menu.add(fichier);
        menu.add(difficulte);
        menu.add(aide);
        setJMenuBar(menu);
        panel.setLayout(new GridLayout(3, 1));
        haut.setLayout(new FlowLayout());
        milieu.setLayout(new FlowLayout());
        bas.setLayout(new FlowLayout());
        haut.setBackground(new Color(0, 150, 0));
        milieu.setBackground(new Color(0, 130, 0));
        bas.setBackground(new Color(0, 150, 0));
    }

    void dessiner(Scopa scopa)
    {
        setContentPane(buildContentPane(scopa));
    }

    private JPanel buildContentPane(Scopa scopa)
    {
        StringBuilder titre = new StringBuilder();
        titre.append((new StringBuilder("SCOPA (")).append(scopa.difficulteString()).append(")          Score: ").toString());
        titre.append((new StringBuilder(String.valueOf(scopa.homme.score))).append(" / ").append(scopa.ordi.score).toString());
        titre.append((new StringBuilder("          A ")).append(scopa.tourDeHomme ? "vous" : "l'adversaire").append(" de jouer").toString());
        if(scopa.booleanScopa)
        {
            titre.append("          !!!!!!   SCOPA   !!!!!!   SCOPA   !!!!!!   SCOPA   !!!!!!");
        }
        setTitle(titre.toString());
        haut.removeAll();
        for(int i = 0; i < scopa.ordi.jeu.size(); i++)
        {
            haut.add(new JLabel(new ImageIcon(getClass().getResource("0.png"))));
        }

        milieu.removeAll();
        if(!scopa.central.isEmpty())
        {
            Carte carte;
            for(Iterator<Carte> iter = scopa.central.iterator(); iter.hasNext(); milieu.add(carte.image()))
            {
                carte = (Carte)iter.next();
            }

        }
        bas.removeAll();
        Carte carte;
        for(Iterator<Carte> iter = scopa.homme.jeu.iterator(); iter.hasNext(); bas.add(carte.bouton(scopa)))
        {
            carte = (Carte)iter.next();
        }

        panel.add(haut);
        panel.add(milieu);
        panel.add(bas);
        panel.setBackground(new Color(0, 150, 0));
        return panel;
    }

    public void dessinerPli(Scopa scopa, Carte carte, Tas ramassees)
    {
        milieu.removeAll();
        if(!scopa.tourDeHomme)
        {
            haut.remove(0);
        }
        if(!scopa.central.isEmpty())
        {
            Carte courante;
            for(Iterator<?> iter = scopa.central.iterator(); iter.hasNext(); milieu.add(ramassees.contains(courante) ? ((java.awt.Component) (courante.imageGrisee())) : ((java.awt.Component) (courante.image()))))
            {
                courante = (Carte)iter.next();
            }

        }
        milieu.add(carte.image());
    }
}
