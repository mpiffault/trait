package fr.mpiffault.trait.main;

import fr.mpiffault.trait.dessin.KeyboardHandler;
import fr.mpiffault.trait.dessin.MouseHandler;
import fr.mpiffault.trait.dessin.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


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
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Table table = new Table(WIDTH, HEIGHT);

        MouseHandler mouseHandler = new MouseHandler(table);
        table.addMouseListener(mouseHandler);
        table.addMouseMotionListener(mouseHandler);

        table.setFocusable(true);
        KeyboardHandler keyboardHandler = new KeyboardHandler(table);
        table.addKeyListener(keyboardHandler);

        // table.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        Image cursorImage = new BufferedImage(1,1,BufferedImage.TYPE_3BYTE_BGR);
        ((BufferedImage)cursorImage).setRGB(0,0,0x000000);
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0,0), "pointCursor");

        table.setCursor(cursor);

        frame.add(table);
        frame.setContentPane(table);
        frame.setSize(WIDTH + 1, HEIGHT + 38);
        frame.setVisible(true);

        System.out.println("---\n<space> for SELECTION (default)\n'p' for POINT\n's' for SEGMENT\n'c' for CONSTRUCTION");
    }
}
