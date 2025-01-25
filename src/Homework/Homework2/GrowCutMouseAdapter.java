package Homework.Homework2;
//You should not modify or turn in this file for HW2.
//Original version for CSC-153 HW2, last updated by Jon Juett on 1/29/24
//Please excuse the lack of comments throughout.

import java.awt.event.*;

public class GrowCutMouseAdapter extends MouseAdapter{
    private GrowCutPixel p;
    private GrowCut win;

    public GrowCutMouseAdapter(GrowCutPixel g, GrowCut w){
        p = g;
        win = w;
    }

    public void mouseClicked(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1){
            System.out.println("Left clicked - Labeled as foreground!");
            p.setLabel(1);
            p.setStrength(1.0);
            p.updateColor();
            win.repaint();
        }

        if(e.getButton() == MouseEvent.BUTTON3){
            System.out.println("Right clicked - Labeled as background!");
            p.setLabel(2);
            p.setStrength(1.0);
            p.updateColor();
            win.repaint();
        }
    }
}