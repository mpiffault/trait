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
    public void keyPressed(KeyEvent keyEvent) {
        super.keyPressed(keyEvent);
        boolean actionCanceled = false;
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            actionCanceled = cancelActionIfNeeded();
        }
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                actionCanceled = cancelActionIfNeeded();
                break;
            case KeyEvent.VK_DELETE:
                this.table.deleteSelectedObjects();
                break;
            default:
                break;
        }
        if (!actionCanceled) {
            this.table.setCurrentMode(Mode.fromEventAndMode(keyEvent.getKeyCode(), table.getCurrentMode()));
        }
        this.table.repaint();
    }

    private boolean cancelActionIfNeeded() {
        if (table.ongoingAction()) {
            table.cancelCurrentAction();
            return true;
        }
        return false;
    }
}
