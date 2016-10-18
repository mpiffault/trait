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
            
        };
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        Table table = new Table(WIDTH, HEIGHT);

        MouseHandler mouseHandler = new MouseHandler(table);
        table.addMouseListener(mouseHandler);
        table.addMouseMotionListener(mouseHandler);

        table.setFocusable(true);
        KeyboardHandler keyboardHandler = new KeyboardHandler(table);
        table.addKeyListener(keyboardHandler);

        Image cursorImage = new BufferedImage(1,1,BufferedImage.TYPE_3BYTE_BGR);
        ((BufferedImage)cursorImage).setRGB(0,0,Color.BLACK.getRGB());
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0,0), "pointCursor");

        table.setCursor(cursor);

        frame.add(table, BorderLayout.CENTER);

        JPanel tools = new JPanel();
        frame.add(tools, BorderLayout.EAST);



        frame.setSize(WIDTH + 1, HEIGHT + 38);
        frame.setVisible(true);

        System.out.println("---\n<space> for SELECTION (default)\n" +
                "'p' for POINT\n" +
                "'s' for SEGMENT\n" +
                "'c' for CONSTRUCTION");
    }
}
