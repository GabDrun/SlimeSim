package pack;

import java.awt.*;
import java.util.Random;

public class Point {
    private static final Random rand = new Random();
    private static final boolean reverse = true;
    private static float speed = 1f;
    private static float turnAngle = 0.1f;
    private static final float checkAngle = 0.1f;
    private static final float randomTurnAngle = 0.01f;
    private static int maxWidth;
    private static int maxHeight;
    private static Img map;
    //private Color white = new Color(255,255,255);
    //private static final Color green = new Color(0,204,0);
    //private static final Color reddish = new Color(245,147,115);
    private static final Color yellow = new Color(250,227,119);
    private static final int myColor = yellow.getRGB();
    private static final int SPAWN_RADIUS = 1;

    private float angle;
    private float x = 0;
    private float y = 0;



    Point(Img map){
        Point.map = map;
        maxWidth = map.getWidth()-1;
        maxHeight = map.getHeight()-1;
        this.x = rand.nextInt(0, maxWidth);
        this.y = rand.nextInt(0, maxHeight);
        this.x = rand.nextInt(maxWidth/2 -SPAWN_RADIUS, maxWidth/2 +SPAWN_RADIUS);
        this.y = rand.nextInt(maxHeight/2 -SPAWN_RADIUS, maxHeight/2 +SPAWN_RADIUS);
        this.angle = rand.nextFloat(0,6.28318f);
        //this.angle = 0f;

    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    private void sense(){
        float leftAngle = angle+checkAngle;
        float rightAngle = angle-checkAngle;
        int left = map.checkRegionBrightness(x,y,leftAngle);//& 0xFF;
        int straight = map.checkRegionBrightness(x,y,angle);//& 0xFF;
        int right = map.checkRegionBrightness(x,y,rightAngle);//& 0xFF;

        if(left>straight && left>right)
            angle += turnAngle;                 // flip signs for avoiding effect
        else if(right>straight && right>left)
            angle -= turnAngle;                 // flip signs for avoiding effect

        angle += 25*rand.nextFloat(-randomTurnAngle,randomTurnAngle);
    }

    private void move(){
        // TODO sense();
        sense();
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
        if((int)x >= maxWidth){
            x = 2*maxWidth-x;
            angle = (float) 3.14159-angle;
        }else if((int)x <= 0){
            x = -x;
            angle = (float) 3.14159-angle;
        }
        if((int)y >= maxHeight){
            y = 2*maxHeight-y;
            angle = -angle;
        }else if((int)y <= 0){
            y = -y;
            angle = -angle;
        }
    }

    public void update(){
        move();
        map.brighten((int)x, (int)y, myColor);
    }
}
