package pack;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {
        int WIDTH = 1200, HEIGHT = 800, POINT_COUNT = 50000;


        Img map = new Img(WIDTH, HEIGHT, POINT_COUNT);

        long oldtime = System.currentTimeMillis(), time;

        while (true){
            map.updatePicture();
            time = System.currentTimeMillis();
//            System.out.println(1000/(time-oldtime));
            oldtime = time;
        }
    }
}
