package fr.mpiffault.trait.dessin;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardHandler extends KeyAdapter {

    private Table table;

    public KeyboardHandler(Table table) {
        super();
        this.table = table;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        this.table.setCurrentMode(Mode.fromEvent(e.getKeyCode()));
    }
}
