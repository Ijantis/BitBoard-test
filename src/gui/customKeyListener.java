package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class customKeyListener implements KeyListener {

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {

		switch (arg0.getExtendedKeyCode()) {
		case 10:
			String input = MainFrame.getCommandLineText();
			MainFrame.clearCommandLine();
			Init.processInput(input);
			break;

		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
