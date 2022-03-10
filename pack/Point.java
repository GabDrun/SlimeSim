package pack;

import java.awt.*;
import java.util.Random;

public class Point {
    private static final Random rand = new Random();
    private static Img map;

    private static final int SPAWN_RADIUS = 1;
    private static final float TURN_ANGLE = 0.1f;
    private static final float CHECK_ANGLE = 0.1f;
    private static final float RANDOM_TURN_ANGLE = 0.25f;
    private static int VISION_RANGE = 20;

    private static int maxWidth;
    private static int maxHeight;

    private static final Color white = new Color(255,255,255);
    private static final Color green = new Color(0,204,0);
    private static final Color reddish = new Color(245,147,115);
    private static final Color yellow = new Color(250,227,119);
    private static final int myColor = yellow.getRGB();

    private float x;
    private float y;
    private float speed;
    private float angle;


    Point(Img map){
        Point.map = map;
        setMaxHeight(map.getHeight()-1);
        setMaxWidth(map.getWidth()-1);
//        setX(rand.nextInt(0, maxWidth));
//        setY(rand.nextInt(0, maxHeight));
        setX(rand.nextInt(getMaxWidth()/2 -SPAWN_RADIUS, getMaxWidth()/2 +SPAWN_RADIUS));
        setY(rand.nextInt(getMaxHeight()/2 -SPAWN_RADIUS, getMaxHeight()/2 +SPAWN_RADIUS));
        setAngle(rand.nextFloat(0, 2 * (float)Math.PI));
        setSpeed(1f + rand.nextFloat(-0.7f,2f));
    }

    private void turnPoint(){
        float leftAngle = getAngle() + CHECK_ANGLE;
        float rightAngle = getAngle() - CHECK_ANGLE;
        int left = map.checkRegionBrightness(getX(), getY(), leftAngle, VISION_RANGE);//& 0xFF;
        int straight = map.checkRegionBrightness(getX(), getY(), getAngle(), VISION_RANGE);//& 0xFF;
        int right = map.checkRegionBrightness(getX(), getY(), rightAngle, VISION_RANGE);//& 0xFF;

        if(left>straight && left>right)
            setAngle(getAngle() + TURN_ANGLE + rand.nextFloat(-RANDOM_TURN_ANGLE, RANDOM_TURN_ANGLE));               // flip signs for avoiding effect
        else if(right>straight && right>left)
            setAngle(getAngle() - TURN_ANGLE + rand.nextFloat(-RANDOM_TURN_ANGLE, RANDOM_TURN_ANGLE));               // flip signs for avoiding effect
        else setAngle(getAngle() + rand.nextFloat(-RANDOM_TURN_ANGLE, RANDOM_TURN_ANGLE));
    }

    private void collisionCheck(){
        if((int)getX() >= getMaxWidth()){
            setX(2 * getMaxWidth() - getX());
            setAngle((float) Math.PI - getAngle());
        }else if((int)getX() <= 0){
            setX(-getX());
            setAngle((float) Math.PI - getAngle());
        }

        if((int)getY() >= getMaxHeight()){
            setY(2 * getMaxHeight() - getY());
            setAngle(-getAngle());
        }else if((int)getY() <= 0){
            setY(-getY());
            setAngle(-getAngle());
        }
    }

    private void move(){
        setX(getX() + getSpeed() * (float)Math.cos(getAngle()));
        setY(getY() + getSpeed() * (float)Math.sin(getAngle()));
    }

    public void update(){
        turnPoint();
        move();
        collisionCheck();
        map.brighten((int)getX(), (int)getY(), myColor);
    }

    private float getX() {
        return x;
    }

    private void setX(float x) {
        this.x = x;
    }

    private float getY() {
        return y;
    }

    private void setY(float y) {
        this.y = y;
    }

    private float getSpeed() {
        return speed;
    }

    private void setSpeed(float speed) {
        this.speed = speed;
    }

    private float getAngle() {
        return angle;
    }

    private void setAngle(float angle) {
        this.angle = angle;
    }

    private static int getMaxWidth() {
        return maxWidth;
    }

    private static void setMaxWidth(int maxWidth) {
        Point.maxWidth = maxWidth;
    }

    private static int getMaxHeight() {
        return maxHeight;
    }

    private static void setMaxHeight(int maxHeight) {
        Point.maxHeight = maxHeight;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", speed=" + speed +
                ", angle=" + angle +
                '}';
    }
}
