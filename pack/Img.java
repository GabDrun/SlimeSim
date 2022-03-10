package pack;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class Img {
    private JFrame frame;
    private JLabel label;
    private BufferedImageOp opBlur;

    private Kernel blur3x3 = new Kernel(3, 3, new float[]
            { 0.111f, 0.111f, 0.111f,
                    0.111f, 0.111f, 0.111f,
                    0.111f, 0.111f, 0.111f });

    private int width;
    private int height;
    private BufferedImage myImage;


    Img(int width, int height){
        init(width, height, blur3x3);
    }

    Img(int width, int height, Kernel k){
        init(width, height, k);
    }

    private void init(int width, int height, Kernel k){
        setWidth(width);
        setHeight(height);
        setMyImage(new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB));
        setOpBlur(k);
    }

    public void brighten(int x, int y, int color){
        getMyImage().setRGB(x, y, color);
    }

    private void blur(){
        setMyImage(opBlur.filter(getMyImage(),null));
    }

    public int checkRegionBrightness(float x, float y, float angle, int visionRange){
        x += visionRange * Math.cos(angle);
        y += visionRange * Math.sin(angle);

        int val = 0;
        for(int i = -1; i < 1; i++){
            for(int j = -1; j < 1; j++){
                if(x + i >= getWidth() || y + j >= getHeight() || x + i <= 0 || y + j <= 0){
                    continue;
                }
                val += getMyImage().getRGB((int)x + i,(int)y + j);
            }
        }
        return val;
    }

    private void display(){
        if(frame == null){
            frame = (new JFrame());
            frame.setTitle("My Image");
            frame.setSize(getMyImage().getWidth(), getMyImage().getHeight());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            label = (new JLabel());
            label.setIcon(new ImageIcon(getMyImage()));
            frame.getContentPane().add(label, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);
        }else {
            label.setIcon(new ImageIcon(getMyImage()));
        }
    }

    public void updatePicture(){
        blur();
        display();
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

    public BufferedImage getMyImage() {
        return myImage;
    }

    private void setMyImage(BufferedImage myImage) {
        this.myImage = myImage;
    }

    public Kernel getBlur3x3() {
        return blur3x3;
    }

    private void setOpBlur(Kernel k) {
        this.opBlur = new ConvolveOp(k);
    }
}
