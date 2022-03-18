package pack;

public class Main {
    public static void main(String[] args) {
        int WIDTH = 1200, HEIGHT = 800, POINT_COUNT = 50000;

        Img map = new Img(WIDTH, HEIGHT, POINT_COUNT, true);

        while (true){
            map.updatePicture();
        }
    }
}
