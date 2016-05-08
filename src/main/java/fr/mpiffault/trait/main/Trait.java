package fr.mpiffault.trait.main;

import fr.mpiffault.trait.dessin.KeyboardHandler;
import fr.mpiffault.trait.dessin.MouseHandler;
import fr.mpiffault.trait.dessin.Table;

import javax.swing.*;


public class Trait {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "True");

        JFrame frame = new JFrame("Trait"){
            @Override
            public void validate() {
                super.validate();
            }
        };
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Table table = new Table(WIDTH, HEIGHT);

        MouseHandler mouseHandler = new MouseHandler(table);
        table.addMouseListener(mouseHandler);
        table.addMouseMotionListener(mouseHandler);

        table.setFocusable(true);
        KeyboardHandler keyboardHandler = new KeyboardHandler(table);
        table.addKeyListener(keyboardHandler);

        frame.add(table);
        frame.setContentPane(table);
        frame.setSize(WIDTH + 1, HEIGHT + 38);
        frame.setVisible(true);

        System.out.println("---\n<space> for SELECTION (default)\n'p' for POINT\n's' for SEGMENT\n'c' for CONSTRUCTION");
    }
}
