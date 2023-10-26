import java.util.concurrent.Semaphore;

public class Main {

    private final static Semaphore witch = new Semaphore(0);
    private final static Semaphore cat = new Semaphore(0);
    private final static Semaphore hunter = new Semaphore(0);
    private final static Semaphore mutex = new Semaphore(1);
    private static int numberOfCatsInHouse = 0;
    private static int numberOfCatsWaiting = 0;
    private static int numberOfHuntersInHouse = 0;
    private static int numberOfHuntersWaiting = 0;
    private static int numberOfWitchesInHouse = 0;
    private static int numberOfWitchesWaiting = 0;


    public static void main(String[] args) {

        System.out.println("Start of program.");

        Cat cat1 = new Cat(1);
        Cat cat2 = new Cat(2);
        Cat cat3 = new Cat(3);
        Cat cat4 = new Cat(4);

        Witch witch1 = new Witch(1);
        Witch witch2 = new Witch(2);
        Witch witch3 = new Witch(3);

        Hunter hunter1 = new Hunter(1);
        Hunter hunter2 = new Hunter(2);
        Hunter hunter3 = new Hunter(3);

        cat1.start();
        cat2.start();
        cat3.start();
        cat4.start();
        witch1.start();
        witch2.start();
        witch3.start();
        hunter1.start();
        hunter2.start();
        hunter3.start();
    }

    public static Semaphore getWitch() {
        return witch;
    }

    public static Semaphore getCat() {
        return cat;
    }

    public static Semaphore getHunter() {
        return hunter;
    }

    public static Semaphore getMutex() {
        return mutex;
    }

    public static int getNumberOfCatsInHouse() {
        return numberOfCatsInHouse;
    }

    public static void setNumberOfCatsInHouse(int _numberOfCats) {
        numberOfCatsInHouse = _numberOfCats;
    }

    public static int getNumberOfCatsWaiting() {
        return numberOfCatsWaiting;
    }

    public static void setNumberOfCatsWaiting(int _numberOfCatsWaiting) {
        numberOfCatsWaiting = _numberOfCatsWaiting;
    }

    public static int getNumberOfHuntersInHouse() {
        return numberOfHuntersInHouse;
    }

    public static void setNumberOfHuntersInHouse(int _numberOfHunters) {
        numberOfHuntersInHouse = _numberOfHunters;
    }

    public static int getNumberOfHuntersWaiting() {
        return numberOfHuntersWaiting;
    }

    public static void setNumberOfHuntersWaiting(int _numberOfHuntersWaiting) {
        numberOfHuntersWaiting = _numberOfHuntersWaiting;
    }

    public static int getNumberOfWitchesInHouse() {
        return numberOfWitchesInHouse;
    }

    public static void setNumberOfWitchesInHouse(int _numberOfWitches) {
        numberOfWitchesInHouse = _numberOfWitches;
    }

    public static int getNumberOfWitchesWaiting() {
        return numberOfWitchesWaiting;
    }

    public static void setNumberOfWitchesWaiting(int _numberOfWitchesWaiting) {
        numberOfWitchesWaiting = _numberOfWitchesWaiting;
    }
}