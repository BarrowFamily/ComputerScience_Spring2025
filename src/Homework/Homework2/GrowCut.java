package Homework.Homework2;
//You should not modify or turn in this file for HW2.
//Original version for CSC-153 HW2, first written by Jon Juett on 1/29/24
//Updated 8/28/24 to move a graphics update so GrowCutGrid doesn't have to call it
//Please excuse the lack of comments throughout.

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.*;

public class GrowCut extends JFrame implements KeyListener{
    private GrowCutGrid grid;
    private JPanel pictureArea;
    private JPanel buttonArea;
    private static final int PIXEL_SIZE = 5;
    private BufferedImage img;
    private boolean done;
    private int width;
    private int height;
    private String outputFilename;

    public GrowCut(String[] args){
        setTitle("Background subtraction!");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        done = false;

        img = null;
        try{
            img = ImageIO.read(new File(args[0]));
        }
        catch(IOException e){
            System.out.println("Failed to open input image file.");
            System.exit(1);
        }
        outputFilename = args[1];

        setSize(img.getWidth() * PIXEL_SIZE ,img.getHeight() * PIXEL_SIZE);
        pictureArea = new JPanel();
        add(pictureArea,BorderLayout.CENTER);
        setupPictureArea();
        setVisible(true);
        addKeyListener(this);
        revalidate();
        repaint();
    }

    public void setupPictureArea(){
        height = img.getHeight();
        width = img.getWidth();
        grid = new GrowCutGrid(height,width);
        pictureArea.setSize(width*PIXEL_SIZE, height*PIXEL_SIZE);
        pictureArea.setLayout(new GridLayout(height, width));
        for(int row = 0; row < height; ++row){
            //System.out.println(row);
            for(int col = 0; col < width; ++col){
                //System.out.println(row + " " + col);
                Color rgb = new Color(img.getRGB(col,row));
                GrowCutPixel p = grid.getPixel(row,col);
                p.setupPixel(rgb,this);
                p.setSize(PIXEL_SIZE,PIXEL_SIZE);
                pictureArea.add(p);
            }
        }
    }

    public void keyPressed(KeyEvent e){
        if(!done && e.getKeyCode() == KeyEvent.VK_SHIFT){
            grid.attackAll();
            for(int row = 0; row < height; ++row){
                for(int col = 0; col < width; ++col){
                    grid.getPixel(row,col).update();
                }
            }
            System.out.println("One step complete!");
        }

        if(!done && e.getKeyCode() == KeyEvent.VK_SPACE){
            while(grid.attackAll()){
                for(int row = 0; row < height; ++row){
                    for(int col = 0; col < width; ++col){
                        grid.getPixel(row,col).update();
                    }
                }
            }
            done = true;
            System.out.println("Segmentation finished.");
        }

        if(done && e.getKeyCode() == KeyEvent.VK_ENTER){
            for(int row = 0; row < height; ++row){
                for(int col = 0; col < width; ++col){
                    GrowCutPixel p = grid.getPixel(row,col);
                    if(p.getLabel() == 1){
                        Color rgb = new Color(img.getRGB(col,row));
                        p.setBackground(rgb);
                    }
                    else{
                        Color c = new Color(255,255,255,255);
                        p.setBackground(c);
                        img.setRGB(col,row,c.getRGB());
                    }

                }
            }
            File f = new File(outputFilename);
            try{
                ImageIO.write(img, "JPG", f);
                System.out.println("Finished writing to file!");
            }
            catch(Exception ex){
                System.out.println("Failed to save to file.");
            }
        }
    }

    public void keyReleased(KeyEvent e){
        //required to implement KeyListener; does nothing
    }

    public void keyTyped(KeyEvent e){
        //required to implement KeyListener; does nothing
    }

    public static void main(String[] args){
        if(args.length != 2){
            System.out.println("Please specify the input and output filenames using args.");
            System.out.println("Attempting to use \"Benny.jpg\" and \"BennyOnly.jpg\" instead.");
            args = new String[2];
            args[0] = "Benny.jpg";
            args[1] = "BennyOnly.jpg";
        }
        new GrowCut(args);
    }
}