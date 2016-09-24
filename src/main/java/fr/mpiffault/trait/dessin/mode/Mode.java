package fr.mpiffault.trait.dessin.mode;

import fr.mpiffault.trait.dessin.Table;
import lombok.Getter;

import java.awt.event.MouseEvent;
import java.util.HashMap;

public abstract class Mode {

    @Getter
    private HashMap<String, Mode> availableModes;

    private Mode currentSubMode;

    private Table table;

    abstract void clickAction(MouseEvent event);

    // les opération s'appliquent dans le sens inverse
    // ex. contrainte sur X : on contraint sur X puis on créé le point
    public final void onClick(MouseEvent event) {
        if (hasCurrentSubMode()) this.currentSubMode.onClick(event);
        this.clickAction(event);
    }

    abstract void onMoveAction(MouseEvent event);

    public void onMove(MouseEvent event) {
        if (hasCurrentSubMode()) this.currentSubMode.onMove(event);
        this.onMoveAction(event);
    }

    private boolean hasCurrentSubMode() {
        return this.currentSubMode != null;
    }
}
