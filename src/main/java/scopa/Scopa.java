package scopa;

import ia.DoubletAEssayer;
import ia.ScopaSimple;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.*;

import jeu.*;

// Referenced classes of package scopa:
//            Difficulte, Fenetre, Joueur

public class Scopa extends AbstractAction
{

	private static final long serialVersionUID = 1L;
	public static final int EXPERT = 1;
	public static final int DIFFICILE = 2;
	public static final int MOYEN = 4;
	public static final int FACILE = 8;
	Fenetre fenetre;
	Tas donne;
	Tas central;
	Carte dernierCoup;
	Tas dernieresRamassees;
	Joueur ordi;
	Joueur homme;
	boolean tourDeHomme;
	boolean modeSixCartes;
	public boolean booleanScopa;
	int difficulte;
	private boolean cEstLaMerde;
	private boolean dernierPliHomme;
	private Thread action;
	final String regles = "La couleur ne sert \340 rien.\n\nEn jouant un 8 on peut ramasser un 8; ramasser " +
	"un 5 et un 3 (8=5+3); ramasser un As, deux 2 et un 3 (8=1+2+2+3); etc\n\nL'As ra" +
	"masse tout.\n\nRamasser tout avec autre chose qu'un as s'appelle faire une \"sco" +
	"pa\" : elle vaut un point.\n\n\nA la fin on donne 1 point au joueur qui a ramass" +
	"\351:\n-le plus de cartes\n-le plus de carreaux\n-le 7 de carreau\n-le plus de 7" +
	", ou si \351galit\351 le plus de 6, ou si \351galit\351 le plus de 5... \n\nDe p" +
	"lus si le joueur a ramass\351 tous les carreaux de 1 \340 x:\n-si x est au moins" +
	" \351gal \340 3, le joueur marque x points\n-si x=10 (tous les carreaux ramass\351" +
	"s) le joueur marque 21 points (partie gagn\351e)\n\nOn redistribue jusqu'\340 ce" +
	" qu'un joueur d\351passe 21 points et l'emporte.\n\n\n\nLa carte retourn\351e da" +
	"ns le jeu de l'adversaire est la derni\350re carte qu'il a jou\351e.\n\nEn mode " +
	"expert l'adversaire triche: il connait votre jeu."
	;
	private boolean modeApplet;

	Scopa(boolean modeApplet0)
	{
		modeSixCartes = false;
		booleanScopa = false;
		difficulte = DIFFICILE;
		action = new Thread();
		fenetre = new Fenetre(this,modeApplet0);
		modeApplet=modeApplet0;
		ordi = new Joueur();
		homme = new Joueur();
		donne = new Tas();
		central = new Tas();
	}

	void distribuer()
	{
		for(int i = 0; i < (modeSixCartes ? 6 : 3); i++)
		{
			ordi.jeu.add((Carte)donne.removeFirst());
			homme.jeu.add((Carte)donne.removeFirst());
		}

		dessiner();
		if(donne.isEmpty())
		{
			popUp("Faîtes le dernier pli!", "Derni\350re distribution                             ");
		}
	}

	void initialiser()
	{
		cEstLaMerde = false;
		central.clear();
		homme.clear();
		ordi.clear();
		donne = Tas.Donne();
		for(int i = 0; i < 4; i++)
		{
			central.add((Carte)donne.removeFirst());
		}

		dernierCoup = null;
		dernieresRamassees = null;
		distribuer();
		dessiner();
		if(!tourDeHomme)
		{
			jouerOrdi();
		}
	}

	void rejouer()
	{
		ordi.score = 0;
		homme.score = 0;
		tourDeHomme = true;
		initialiser();
	}

	public static void main(String args[])
	{
		Scopa scopa = new Scopa(false);
		System.out.println("fenetre ouverte");
		scopa.rejouer();
	}

	public void stockerRecord(boolean gagne, boolean modeSixCartes2)
	{
		LinkedList<String> actuels = records();
		int i;
		for(i = 0; !actuels.get(i).equals(difficulteString()); i++) { }
		int rangAModifier = i + (gagne ? 1 : 2) + (modeSixCartes2 ? 2 : 0);
		actuels.set(rangAModifier, (new StringBuilder()).append(Integer.parseInt(actuels.get(rangAModifier)) + 1).toString());
		try
		{
			stockerRecord(actuels);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void RAZRecords()
	{
		LinkedList<String> vierge = new LinkedList<String>();
		vierge.add("facile");
		vierge.add("0");
		vierge.add("0");
		vierge.add("0");
		vierge.add("0");
		vierge.add("moyen");
		vierge.add("0");
		vierge.add("0");
		vierge.add("0");
		vierge.add("0");
		vierge.add("difficile");
		vierge.add("0");
		vierge.add("0");
		vierge.add("0");
		vierge.add("0");
		vierge.add("expert");
		vierge.add("0");
		vierge.add("0");
		vierge.add("0");
		vierge.add("0");
		try
		{
			stockerRecord(vierge);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	private void stockerRecord(LinkedList<String> records)
	throws IOException
	{
		File record = new File("records.dat");
		FileWriter f = new FileWriter(record);
		int cle = 0;
		for(int i = 0; i < records.size(); i++)
		{
			f.write((new StringBuilder(String.valueOf(records.get(i)))).append(" ").toString());
			cle += clef(records.get(i));
		}

		f.write((new StringBuilder()).append(cle).toString());
		f.close();
	}

	private int clef(String mot)
	{
		return (int)(Math.pow(mot.length(), 4D) + (double)(mot.contains("0") ? 1245 : '\uFD72') + 100D * Math.sin(mot.hashCode()));
	}

	private LinkedList<String> records()
	{
		LinkedList<String> retour = new LinkedList<String>();
		FileReader lecteur = null;
		try
		{
			lecteur = new FileReader("records.dat");
		}
		catch(FileNotFoundException e)
		{
			RAZRecords();
		}
		boolean lectureFinie = false;
		int cle = 0;
		String mot = "";
		while(!lectureFinie) 
		{
			try
			{
				mot = motSuivant(lecteur);
				retour.add(mot);
				cle += clef(mot);
			}
			catch(IOException e)
			{
				lectureFinie = true;
			}
		}
		cle -= clef(mot);
		if(cle != Integer.parseInt(retour.removeLast()))
		{
			RAZRecords();
			System.out.println("tricheur");
			return records();
		} else
		{
			return retour;
		}
	}

	private String motSuivant(FileReader lecteur)
	throws IOException
	{
		StringBuilder entree = new StringBuilder();
		for(int i = lecteur.read(); i != 32 && i != -1; i = lecteur.read())
		{
			entree.append((char)i);
		}

		if(entree.length() == 0)
		{
			throw new IOException();
		} else
		{
			return entree.toString();
		}
	}

	public void actionPerformed(final ActionEvent e)
	{
		if(action.isAlive())
		{
			return;
		} else
		{
			action = new Thread() {


				public void run()
				{
					Object source = e.getSource();
					if((source instanceof BoutonCarte) && tourDeHomme)
					{
						jouerHomme(((BoutonCarte)source).carte);
					}
					if(!tourDeHomme)
					{
						jouerOrdi();
					}
					if(source instanceof JMenuItem)
					{
						if(source.equals(fenetre.rejouer))
						{
							rejouer();
						}
						if(source.equals(fenetre.quitter))
						{
							System.exit(0);
						}
						if(source.equals(fenetre.modeSixCartes))
						{
							ordi.score = 0;
							homme.score = 0;
							modeSixCartes = true;
							initialiser();
						}
						if(source.equals(fenetre.modeTroisCartes))
						{
							ordi.score = 0;
							homme.score = 0;
							modeSixCartes = false;
							initialiser();
						}
						if(source.equals(fenetre.facile))
						{
							difficulte = FACILE;
							rejouer();
						}
						if(source.equals(fenetre.moyen))
						{
							difficulte = MOYEN;
							rejouer();
						}
						if(source.equals(fenetre.difficile))
						{
							difficulte = DIFFICILE;
							rejouer();
						}
						if(source.equals(fenetre.expert))
						{
							difficulte = EXPERT;
							rejouer();
						}
						if(source.equals(fenetre.statistiques))
						{
							LinkedList<String> records = records();
							StringBuilder cartes3 = new StringBuilder();
							StringBuilder cartes6 = new StringBuilder();
							for(int i = 0; i < records.size(); i += 5)
							{
								int victoires3 = Integer.parseInt(records.get(i + 1));
								int defaites3 = Integer.parseInt(records.get(i + 2));
								int victoires6 = Integer.parseInt(records.get(i + 3));
								int defaites6 = Integer.parseInt(records.get(i + 4));
								cartes3.append((new StringBuilder(String.valueOf(records.get(i)))).append(" : ").toString());
								cartes3.append((new StringBuilder(String.valueOf(victoires3))).append(" victoires ").toString());
								cartes3.append((new StringBuilder(String.valueOf(defaites3))).append(" d\351faites ").toString());
								cartes6.append((new StringBuilder(String.valueOf(records.get(i)))).append(" : ").toString());
								cartes6.append((new StringBuilder(String.valueOf(victoires6))).append(" victoires ").toString());
								cartes6.append((new StringBuilder(String.valueOf(defaites6))).append(" d\351faites ").toString());
								try
								{
									cartes3.append((new StringBuilder("soit ")).append((100 * victoires3) / (defaites3 + victoires3)).append("% de victoires ").toString());
								}
								catch(ArithmeticException arithmeticexception) { }
								try
								{
									cartes6.append((new StringBuilder("soit ")).append((100 * victoires6) / (defaites6 + victoires6)).append("% de victoires ").toString());
								}
								catch(ArithmeticException arithmeticexception1) { }
								cartes3.append('\n');
								cartes6.append('\n');
							}

							popUp("Statistiques", (new StringBuilder("Mode trois cartes:\n")).append(cartes3.toString()).append('\n').append("Mode six cartes:").append('\n').append(cartes6.toString()).toString());
						}
						if(source.equals(fenetre.RAZ))
						{
							int reponse = JOptionPane.showConfirmDialog(fenetre, "Voulez vous remettre d\351finitivement \340 z\351ro les records?", "Attention", 0);
							if(reponse == 0)
							{
								RAZRecords();
							}
						}
						if(source.equals(fenetre.aPropos))
						{
							popUp("A propos", "Scopa\ncr\351\351 du 8 au 20 Juillet 2008 par Olivier Adam\ndont une nuit blanch" +
									"e historique dans la nuit du 10 au 11 (8h du mat quand j'\351cris ces lignes)"
							);
						}
						if(source.equals(fenetre.regles))
						{
							popUp("R\350gles",regles);
						}
						dessiner();
					}
				}


				/*{
					this$0 = Scopa.this;
					e = actionevent;
					super();
				}*/
			};
			action.start();
			return;
		}
	}

	private void jouerOrdi()
	{
		
		try
		{
			Thread.sleep(1500L);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}

		LinkedList<DoubletAEssayer> aEssayer = new LinkedList<DoubletAEssayer>();
		for(Iterator<?> iterator = ordi.jeu.iterator(); iterator.hasNext();)
		{
			Carte carte = (Carte)iterator.next();
			if(carte.getValeur() == 1)
			{
				aEssayer.add(new DoubletAEssayer(carte, central.clone()));
			} else
			{
				boolean possibleDeRamasserUn = false;
				boolean impossibleDeRamasser = true;
				Iterator<?> iterator1 = central.combinaisons().iterator();
				while(iterator1.hasNext()) 
				{
					Tas element = (Tas)iterator1.next();
					if(element.somme() != carte.getValeur())
					{
						continue;
					}
					impossibleDeRamasser = false;
					if(element.size() == 1)
					{
						possibleDeRamasserUn = true;
					} else
						if(possibleDeRamasserUn)
						{
							break;
						}
					aEssayer.add(new DoubletAEssayer(carte, element));
				}
				if(impossibleDeRamasser)
				{
					aEssayer.add(new DoubletAEssayer(carte, null));
				}
			}
		}

		DoubletAEssayer intelligent = intelligenceArtificielle(aEssayer);
		jouerPourDeBon(ordi, intelligent.carte, intelligent.ramassees);
	}

	private DoubletAEssayer intelligenceArtificielle(LinkedList<DoubletAEssayer> aEssayer)
	{
		ScopaSimple scopaClone = new ScopaSimple(donne, central, ordi, homme, difficulte, modeSixCartes);
		double maxiObtenu = -50D;
		DoubletAEssayer meilleur = null;
		for(Iterator<DoubletAEssayer> iterator = aEssayer.iterator(); iterator.hasNext();)
		{
			DoubletAEssayer courant = iterator.next();
			double scoreTotal = 0.0D;
			int diff = 0;
			switch(difficulte)
			{
			case DIFFICILE: // '\003'
			case EXPERT: // '\004'
			diff = 50000;
			break;

			case MOYEN: // '\002'
				diff = 10;
				break;

			case FACILE: // '\001'
				diff = 2;
				break;
			}
			int n = difficulte != DIFFICILE && difficulte != EXPERT ? diff : diff / (aEssayer.size() * (donne.size() + 5));
			for(int i = 0; i < n; i++)
			{
				scopaClone.reset();
				scopaClone.jouerPourDeBon(scopaClone.ordi, courant.carte, courant.ramassees);
				scopaClone.toutJouer();
				scoreTotal += scopaClone.deltaScore();
			}

			scoreTotal /= n;
			if(scoreTotal > maxiObtenu)
			{
				maxiObtenu = scoreTotal;
				meilleur = courant;
			}
		}

		if(maxiObtenu > 4D && !cEstLaMerde && (difficulte & (EXPERT |DIFFICILE))!=0)
		{
			popUp("Mon Dieu!", "Votre adversaire pr\351voit une victoire totale!");
			cEstLaMerde = true;
		}
		return meilleur;
	}

	private void jouerHomme(Carte carte)
	{
		if(carte.getValeur() == 1)
		{
			jouerPourDeBon(homme, carte, central.clone());
		} else
		{
			LinkedList<Tas> possibilitees = new LinkedList<Tas>();
			boolean possibleDeRamasserUn = false;
			Iterator<?> iterator = central.combinaisons().iterator();
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
			jouerPourDeBon(homme, carte, null);
			break;

			case 1: // '\001'
			jouerPourDeBon(homme, carte, possibilitees.getFirst());
			break;

			default:
				jouerPourDeBon(homme, carte, indecisionHomme(carte, possibilitees));
			break;
			}
		}
	}

	private void jouerPourDeBon(Joueur joueur, Carte carte, Tas ramasse)
	{
		booleanScopa = false;
		joueur.jeu.remove(carte);
		dessinerPli(carte, ramasse);
		tourDeHomme = !joueur.equals(homme);
		dernieresRamassees = ramasse;
		dernierCoup = carte;
		if(ramasse == null)
		{
			central.add(carte);
		} else
		{
			dernierPliHomme = joueur.equals(homme);
			central.removeAll(ramasse);
			joueur.ramassees.add(carte);
			joueur.ramassees.addAll(ramasse);
			if(central.isEmpty() && carte.getValeur() != 1)
			{
				joueur.nombreDeScopa++;
				booleanScopa = true;
				Toolkit.getDefaultToolkit().beep();
			}
		}
		if(homme.jeu.isEmpty() && ordi.jeu.isEmpty())
		{
			if(donne.isEmpty())
			{
				finirTour();
			} else
			{
				distribuer();
			}
		}
		dessiner();
	}

	private Tas indecisionHomme(Carte carte, LinkedList<Tas> possibilitees)
	{
		Tas retour;
		for(retour = null; retour == null; retour = (Tas)JOptionPane.showInputDialog(fenetre, "Choississez un tas \340 ramasser", "Tas", 3, null, possibilitees.toArray(), possibilitees.getFirst())) { }
		return retour;
	}

	private void dessiner()
	{
		fenetre.dessiner(this);
		fenetre.setVisible(true);
	}

	private void dessinerPli(Carte carte, Tas ramassees)
	{
		if(ramassees == null)
		{
			return;
		}
		fenetre.dessinerPli(this, carte, ramassees);
		fenetre.setVisible(true);
		try
		{
			Thread.sleep(1500L);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private void finirTour()
	{
		tourDeHomme = !tourDeHomme;
		dessiner();
		try
		{
			Thread.sleep(1500L);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		if(dernierPliHomme)
		{
			homme.ramassees.addAll(central);
		} else
		{
			ordi.ramassees.addAll(central);
		}
		central.clear();
		
		
		int scoreHomme = homme.score();
		int scoreOrdi = ordi.score();
		int donnees[] = homme.ramassees.resultat();
		homme.score += scoreHomme;
		ordi.score += scoreOrdi;
		
		StringBuilder result = new StringBuilder();
		result.append((new StringBuilder(String.valueOf(dernierPliHomme ? "Vous r\351cuperez" : "L'adversaire r\351cup\350re"))).append(" le dernier pli.").append('\n').toString());
		if(donnees[2] > 5)
		{
			result.append("Vous avez les carreaux");
		}
		if(donnees[2] == 5)
		{
			result.append("Les carreaux sont ex aequo");
		}
		if(donnees[2] < 5)
		{
			result.append("L'adversaire a les carreaux");
		}
		result.append(", ");
		if(donnees[0] == 1)
		{
			result.append("vous avez les 7");
		} else
		{
			result.append("il a les 7");
		}
		result.append(", ");
		if(donnees[3] == 1)
		{
			result.append("vous avez le 7 de carreau");
		} else
		{
			result.append("il a le 7 de carreau");
		}
		result.append(" et ");
		if(donnees[1] > 20)
		{
			result.append("vous avez les cartes");
		}
		if(donnees[1] == 20)
		{
			result.append("les cartes sont ex aequo");
		}
		if(donnees[1] < 20)
		{
			result.append("il a les cartes");
		}
		result.append(".");
		int scoreSans123Carreaux = (donnees[2] <= 5 ? 0 : 1) + (donnees[1] <= 20 ? 0 : 1) + donnees[0] + donnees[3] + homme.nombreDeScopa;
		if(scoreHomme != scoreSans123Carreaux)
		{
			if(scoreHomme == 21)
			{
				result.append("\nVous avez tous les carreaux");
			} else
			{
				result.append((new StringBuilder("\nVous avez de l'as au ")).append(scoreHomme - scoreSans123Carreaux).append(" de carreau").toString());
			}
		}
		scoreSans123Carreaux = (((((donnees[2] >= 5 ? 0 : 1) + (donnees[1] >= 20 ? 0 : 1) + 1) - donnees[0]) + 1) - donnees[3]) + ordi.nombreDeScopa;
		if(scoreOrdi != scoreSans123Carreaux)
		{
			if(scoreOrdi == 21)
			{
				result.append("\nL'adversaire a tous les carreaux\n");
			} else
			{
				result.append((new StringBuilder("\nL'adversaire a de l'as au ")).append(scoreOrdi - scoreSans123Carreaux).append(" de carreau").toString());
			}
		}
		result.append('\n');
		result.append((new StringBuilder("\nVous avez ")).append(homme.nombreDeScopa).append(" scopa").toString());
		result.append((new StringBuilder("\nL'adversaire a ")).append(ordi.nombreDeScopa).append(" scopa").toString());
		result.append((new StringBuilder("\n\nScore de la manche : ")).append(scoreHomme).append(" / ").append(scoreOrdi).toString());
		result.append((new StringBuilder("\n\n")).append(tourDeHomme ? "A vous de commencer." : "A l'adversaire de commencer.").toString());
		
		/*experimental*/
		homme.jeu.addAll(homme.ramassees);
		dessiner();
		
		
		popUp((new StringBuilder("Vous ")).append(homme.score).append(" / Lui ").append(ordi.score).toString(), result.toString());
		if((homme.score >= 21 || ordi.score >= 21) && homme.score != ordi.score)
		{
			if(homme.score >= ordi.score)
			{
				if(!modeApplet)
					stockerRecord(true, modeSixCartes);
				if(difficulte != DIFFICILE)
				{
					popUp("Fin de partie", "Gagn\351");
				} else
				{
					popUp("Fin de partie", "Gagn\351 en mode difficile, tu m\351rites d'affronter Olivier, notre ma\356tre \340" +
							" tous."
					);
				}
			} else
			{
				if(!modeApplet)
					stockerRecord(false, modeSixCartes);
				popUp("Fin de partie", "Perdu, looser!");
			}
			homme.score = 0;
			ordi.score = 0;
		}
		initialiser();
	}

	protected void popUp(String titre, String texte)
	{
		JOptionPane.showMessageDialog(fenetre, texte, titre, 1);
	}

	public String difficulteString() {
		switch(difficulte){
		case FACILE:
			return "facile";
		case MOYEN:
			return "moyen";
		case DIFFICILE:
			return "difficile";
		case EXPERT:
			return "expert";
		default:
			return "bug difficulteString() scopa";	
		}
		
	}






}
