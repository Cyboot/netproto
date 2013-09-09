package net.tmt.client.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class Controls implements KeyListener {
	private static final Controls	instance	= new Controls();

	private Map<Integer, Boolean>	keymap		= new HashMap<Integer, Boolean>();

	@Override
	public void keyPressed(final KeyEvent ke) {
		keymap.put(ke.getKeyCode(), true);
	}

	@Override
	public void keyReleased(final KeyEvent ke) {
		keymap.put(ke.getKeyCode(), false);
	}

	@Override
	public void keyTyped(final KeyEvent ke) {

	}

	public boolean isKeyDown(final int keycode) {
		Boolean down = keymap.get(keycode);

		if (down == null || down == false)
			return false;
		else
			return true;
	}


	public static Controls getInstance() {
		return instance;
	}
}
