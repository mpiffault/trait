package fr.mpiffault.trait.dessin;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardHandler extends KeyAdapter {

    private final Table table;

    public KeyboardHandler(Table table) {
        super();
        this.table = table;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        super.keyPressed(keyEvent);

        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE && table.ongoingAction()) {
            table.cancelCurrentAction();
        } else {
            performSpecialActions(keyEvent);
            switchMode(keyEvent);
        }
        this.table.repaint();
    }

    private void switchMode(KeyEvent keyEvent) {
        ModeEnum newModeEnum = ModeEnum.fromKeyEventAndMode(keyEvent.getKeyCode(), table.getCurrentMode());
        this.table.setCurrentMode(newModeEnum);
    }

    private void performSpecialActions(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_DELETE:
                this.table.deleteSelectedObjects();
                break;
            case KeyEvent.VK_X:
                table.clearConstructionLines();
                break;
            default:
                break;
        }
        this.table.repaint();
    }
}
