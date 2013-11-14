package scopa;

import java.applet.Applet;

public class App extends Applet {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() {
		Scopa scopa=new Scopa(true);
		add(scopa.fenetre.panel);
		validate();
		scopa.rejouer();
	}
}
