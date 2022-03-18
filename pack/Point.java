package pack;

import java.awt.*;
import java.util.Random;

public class Point {
    private static final Random rand = new Random();
    private static Img map;

    private static int spawnRadius = 1;
    private static float turnAngle = 0.1f;
    private static float checkAngle = 0.1f;
    private static float randomTurnAngle = 0.25f;
    private static int visionRange = 20;
    private static boolean reverse = false;

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
        setX(rand.nextInt(getMaxWidth()/2 - spawnRadius, getMaxWidth()/2 + spawnRadius));
        setY(rand.nextInt(getMaxHeight()/2 - spawnRadius, getMaxHeight()/2 + spawnRadius));
        setAngle(rand.nextFloat(0, 2 * (float)Math.PI));
        setSpeed(1f + rand.nextFloat(-0.7f,2f));
    }

    private void turnPoint(){
        float leftAngle = getAngle() + checkAngle;
        float rightAngle = getAngle() - checkAngle;
        int left = map.checkRegionBrightness(getX(), getY(), leftAngle, visionRange);//& 0xFF;
        int straight = map.checkRegionBrightness(getX(), getY(), getAngle(), visionRange);//& 0xFF;
        int right = map.checkRegionBrightness(getX(), getY(), rightAngle, visionRange);//& 0xFF;

        if(isReverse()){  // Turn away from light
            if (left < straight && left < right) {
                setAngle(getAngle() + turnAngle + rand.nextFloat(-randomTurnAngle, randomTurnAngle));
            } else if (right < straight && right < left) {
                setAngle(getAngle() - turnAngle + rand.nextFloat(-randomTurnAngle, randomTurnAngle));
            } else {
                setAngle(getAngle() + rand.nextFloat(-randomTurnAngle, randomTurnAngle));
            }
        }else {     // Follow the light
            if (left > straight && left > right) {
                setAngle(getAngle() + turnAngle + rand.nextFloat(-randomTurnAngle, randomTurnAngle));
            } else if (right > straight && right > left) {
                setAngle(getAngle() - turnAngle + rand.nextFloat(-randomTurnAngle, randomTurnAngle));
            } else {
                setAngle(getAngle() + rand.nextFloat(-randomTurnAngle, randomTurnAngle));
            }
        }
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
        if (speed <=10f && speed > 0.1)
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

    public static int getSpawnRadius() {
        return spawnRadius;
    }

    public static void setSpawnRadius(int spawnRadius) {
        Point.spawnRadius = spawnRadius;
    }

    public static float getTurnAngle() {
        return turnAngle;
    }

    public static void setTurnAngle(float turnAngle) {
        Point.turnAngle = turnAngle;
    }

    public static float getCheckAngle() {
        return checkAngle;
    }

    public static void setCheckAngle(float checkAngle) {
        Point.checkAngle = checkAngle;
    }

    public static float getRandomTurnAngle() {
        return randomTurnAngle;
    }

    public static void setRandomTurnAngle(float randomTurnAngle) {
        Point.randomTurnAngle = randomTurnAngle;
    }

    public static int getVisionRange() {
        return visionRange;
    }

    public static void setVisionRange(int visionRange) {
        Point.visionRange = visionRange;
    }

    public static boolean isReverse() {
        return reverse;
    }

    public static void setReverse(boolean reverse) {
        Point.reverse = reverse;
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
