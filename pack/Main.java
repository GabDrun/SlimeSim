package pack;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        int WIDTH = 1200, HEIGHT = 800, POINT_COUNT = 50000;

        Img map = new Img(WIDTH,HEIGHT);
        Point[] points = new Point[POINT_COUNT];
        for(int i = 0; i < points.length; i++){
            points[i] = new Point(map);
        }
        long oldtime = System.currentTimeMillis(), time;
        while (true){
            for (Point point : points) {
                point.update();
            }
            map.updatePicture();
            time = System.currentTimeMillis();
            System.out.println(1000/(time-oldtime));
            oldtime = time;
        }

    }

}
