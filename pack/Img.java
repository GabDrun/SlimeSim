package pack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class Img {
    private JFrame frame;
    private JLabel label;
    private JPanel controlPanel;
    private BufferedImageOp opBlur;
    Point[] points;

    JFormattedTextField field;

    private Kernel blur3x3 = new Kernel(3, 3, new float[]
            { 0.111f, 0.111f, 0.111f,
                    0.111f, 0.111f, 0.111f,
                    0.111f, 0.111f, 0.111f });

    private int width;
    private int height;

    private int pointCount;
    private BufferedImage myImage;


    Img(int width, int height, int pointCount){
        init(width, height, pointCount, blur3x3);
    }

    Img(int width, int height, int pointCount, Kernel k){
        init(width, height, pointCount, k);
    }

    private void init(int width, int height, int pointCount, Kernel k){
        setWidth(width);
        setHeight(height);
        setPointCount(pointCount);

        setMyImage(new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB));

        points = new Point[getPointCount()];
        for(int i = 0; i < points.length; i++){
            points[i] = new Point(this);
        }
        if(k != null){
            setOpBlur(k);
        }
    }

    private void restartSim(){          // TODO fix: Restarting leaves after effect
        System.out.println("Restarting!");
        init(getWidth(),getHeight(),getPointCount(),null);
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

    private JPanel sliderToPanel(String name, DoubleJSlider slider){
        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.add(new JLabel(name),BorderLayout.WEST);
        imgPanel.add(slider.getText(),BorderLayout.NORTH);
        imgPanel.add(slider,BorderLayout.SOUTH);
        return imgPanel;
    }

    private JPanel createControls(){
        JPanel controlPanel = new JPanel(new GridLayout(2, 5));

        controlPanel.add(sliderToPanel("Vision range", new DoubleJSlider(1, 100, Point.getVisionRange(), 1){
            @Override
            public void setRealValue(float fl){
                Point.setVisionRange((int)fl);
            }
        } ));

        controlPanel.add(sliderToPanel("Trun Angle", new DoubleJSlider(0.0f, 6.28f, Point.getTurnAngle(), 100){
            @Override
            public void setRealValue(float fl){
                Point.setTurnAngle(fl);
            }
        } ));

        controlPanel.add(sliderToPanel("Check Angle", new DoubleJSlider(0.0f, 6.28f, Point.getCheckAngle(), 100){
            @Override
            public void setRealValue(float fl){
                Point.setCheckAngle(fl);
            }
        } ));

        controlPanel.add(sliderToPanel("Max Random Turn Angle", new DoubleJSlider(0.01f, 6.28f, Point.getRandomTurnAngle(), 100){
            @Override
            public void setRealValue(float fl){
                Point.setRandomTurnAngle(fl);
            }
        } ));

        controlPanel.add(sliderToPanel("Spawn Radius From Center", new DoubleJSlider(1, Math.min(getWidth(),getHeight())/2, Point.getSpawnRadius(), 1){
            @Override
            public void setRealValue(float fl){
                Point.setSpawnRadius((int)fl);
            }
        } ));

        controlPanel.add(sliderToPanel("Follow/Avoid", new DoubleJSlider(0, 1, Point.isReverse() ? 1 : 0, 1){
            @Override
            public void setRealValue(float fl){
                if((int)getScaledValue()==0)
                    Point.setReverse(false);
                else Point.setReverse(true);
            }
        } ));

        controlPanel.add(sliderToPanel("Color: Red", new DoubleJSlider(0, 255, Point.getR(), 1){
            @Override
            public void setRealValue(float fl){
                Point.setR((int)fl);
                Point.setMyColorInt();
            }
        } ));

        controlPanel.add(sliderToPanel("Color: Green", new DoubleJSlider(0, 255, Point.getG(), 1){
            @Override
            public void setRealValue(float fl){
                Point.setG((int)fl);
                Point.setMyColorInt();
            }
        } ));

        controlPanel.add(sliderToPanel("Color: Blue", new DoubleJSlider(0, 255, Point.getB(), 1){
            @Override
            public void setRealValue(float fl){
                Point.setB((int)fl);
                Point.setMyColorInt();
            }
        } ));

        JButton button = new JButton("Restart");
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                restartSim();
            }
        });
        controlPanel.add(button);

        return controlPanel;
    }

    private void display(){
        if(frame == null){
            frame = (new JFrame());
            label = (new JLabel());
            label.setIcon(new ImageIcon(getMyImage()));
            controlPanel = createControls();

            frame.setTitle("Slime Simulation");
            frame.setLayout(new BorderLayout());

            frame.getContentPane().add(label, BorderLayout.NORTH);
            frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);

//            frame.setLocationRelativeTo(null);
//            frame.setSize(getMyImage().getWidth()+26, getMyImage().getHeight()+130); // Maybe just set location?
            frame.setLocation(100, 30);

            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        }else {
            label.setIcon(new ImageIcon(getMyImage())); // is this slow?
        }
    }

    public void updatePicture(){
        for (Point point : points) {
            point.update();
        }
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

    public int getPointCount() {
        return pointCount;
    }

    private void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    @Override
    public String toString() {
        return "Img{" +
                "blur3x3=" + blur3x3 +
                ", width=" + width +
                ", height=" + height +
                ", pointCount=" + pointCount +
                '}';
    }
}
