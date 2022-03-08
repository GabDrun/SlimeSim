package pack;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.concurrent.TimeUnit;

public class Img {
    private JFrame frame;
    private JLabel label;
    private int width;
    private int height;
    private int CHECK_REGION_SIZE;
    Graphics2D g2D;
    BufferedImage myImage, weightMap;
    Kernel blur3x3 = new Kernel(3, 3, new float[]
            { 0.04f, 0.08f, 0.04f,
                    0.08f, 0.52f, 0.08f,
                    0.04f, 0.08f, 0.04f });
    Kernel boxBlur3x3 = new Kernel(3, 3, new float[]
            { 0.111f, 0.111f, 0.111f,
                    0.111f, 0.111f, 0.111f,
                    0.111f, 0.111f, 0.111f });
    Kernel blurFade3x3 = new Kernel(3, 3, new float[]
            { 0.04f, 0.08f, 0.04f,
                    0.08f, 0.501f, 0.08f,
                    0.04f, 0.08f, 0.04f });
    Kernel gausianFade3x3 = new Kernel(3, 3, new float[]
            { 0.075f, 0.124f, 0.075f,
                    0.124f, 0.204f, 0.124f,
                    0.075f, 0.124f, 0.075f });
    Kernel blurSlowFade3x3 = new Kernel(3, 3, new float[]
            { 0.0083333f, 0.0166666f, 0.0083333f,
                    0.0166666f, 0.89f, 0.0166666f,
                    0.0083333f, 0.0166666f, 0.0083333f });
    BufferedImageOp opBlur = new ConvolveOp(boxBlur3x3);
    Kernel sum3x3 = new Kernel(3, 3, new float[]
            { 1, 1, 1,
                    1, 1, 1,
                    1, 1, 1 });
    BufferedImageOp sumFilter = new ConvolveOp(sum3x3);


    Img(int width, int height){
        setWidth(width);
        setHeight(height);
        myImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        //weightMap = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
    }

    public void brighten(int x, int y, int color){
        myImage.setRGB(x, y, color);
    }

    private void blur(){
        myImage = opBlur.filter(myImage,null);
    }

    private static int darkerColor(int color){
        float ratio = 0.9f;
        int a = (color >> 24) & 0xFF;
        int r = (int) (((color >> 16) & 0xFF) * ratio);
        int g = (int) (((color >> 8) & 0xFF) * ratio);
        int b = (int) ((color & 0xFF) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private void fade(){
        for(int cx = 0; cx < myImage.getWidth();cx++){
            for(int cy = 0; cy < myImage.getHeight();cy++){
                myImage.setRGB(cx,cy,darkerColor(myImage.getRGB(cx,cy)));
            }
        }
    }

    public int checkRegionBrightness(float x, float y, float angle){
        x += 20*Math.cos(angle);
        y += 20*Math.sin(angle);

        int val = 0;
        for(int i = -1; i < 1;i++){
            for(int j = -1; j < 1; j++){
                if(x + i >= width || y + j >= height || x + i <= 0 || y + j <= 0){
                    continue;
                }
                val += myImage.getRGB((int)x + i,(int)y + j);
            }
        }
        return val;
//        if(x >= width || y >= height || x <= 0 || y <= 0)
//            return 0;
//        return weightMap.getRGB((int)x,(int)y);

//        if(x >= width || y >= height || x <= 0 || y <= 0)
//            return 0;
//        return myImage.getRGB((int)x,(int)y);

    }

    public int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        this.height = height;
    }
    private int counter;
    public void updatePicture(){
        blur();   // really slow
        // fade // now blur fades
        display();
       // ++counter;
    }

    public void display(){
        if(frame==null){
            frame=new JFrame();
            frame.setTitle("Slime Simulation");
            frame.setSize(myImage.getWidth(), myImage.getHeight());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            label=new JLabel();
            label.setIcon(new ImageIcon(myImage));
            frame.getContentPane().add(label,BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);

//            for (int i = 0; i < 1000; i++)frame.setVisible(true);    // wait

        }else {
            label.setIcon(new ImageIcon(myImage));
        }
    }
}
