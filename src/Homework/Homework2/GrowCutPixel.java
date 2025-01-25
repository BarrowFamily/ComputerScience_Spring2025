package Homework.Homework2;
//You should not modify or turn in this file for HW2.
//Original version for CSC-153 HW2, last updated by Jon Juett on 1/29/24
//Please excuse the lack of comments throughout.

import javax.swing.*;
import java.awt.*;

public class GrowCutPixel extends JPanel{
    private Color rgb;
    private int r;
    private int g;
    private int b;
    private int label;
    private int newLabel;
    private double strength;
    private double newStrength;
    private static final double MAX_DIFF = Math.sqrt(255.0*255.0 * 3.0);

    public GrowCutPixel(){
        label = 0;
        newLabel = 0;
        strength = 0.0;
        newStrength = 0.0;
    }

    public void setupPixel(Color c, GrowCut win){
        rgb = c;
        r = c.getRed();
        g = c.getGreen();
        b = c.getBlue();
        setBackground(c);
        addMouseListener(new GrowCutMouseAdapter(this, win));
    }

    public void setLabel(int l){
        label = l;
        newLabel = l;
    }

    public void update(){
        boolean colorNeedsToChange = label != newLabel || strength != newStrength;
        label = newLabel;
        strength = newStrength;
        if(colorNeedsToChange){
            updateColor();
        }
    }

    public int getLabel(){
        return label;
    }

    public double getStrength(){
        return strength;
    }

    public void setStrength(double s){
        strength = s;
        newStrength = s;
    }

    public void updateColor(){
        int x = (int) (255 * strength);
        if(x > 255) x = 255;
        if(label == 1){
            setBackground(new Color(x, 0, 0));
        }
        else if(label == 2){
            setBackground(new Color(0, 0, x));
        }
    }

    public void conquer(GrowCutPixel defender, double newStrength){
        defender.newLabel = label;
        defender.newStrength = newStrength;
    }

    public static double distance(GrowCutPixel a, GrowCutPixel b){
        return Math.sqrt((a.r - b.r) * (a.r - b.r) + (a.g - b.g) * (a.g - b.g) + (a.b - b.b) * (a.b - b.b)) / MAX_DIFF;
    }
}
