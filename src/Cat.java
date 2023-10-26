import java.util.UUID;

public class Cat extends Thread {

    private final int catNumber;
    private int acquiredMutex = 0;

    public Cat(int catNumber) {
        this.catNumber = catNumber;
    }

    public void run() {

        while (true) {
            Main.getMutex().acquireUninterruptibly();
            acquiredMutex = 1;

            String guid = UUID.randomUUID().toString();

            System.out.println("cat " + catNumber + " is entering");

            Main.setNumberOfCatsInHouse(Main.getNumberOfCatsInHouse() + 1);

            System.out.println("cat " + catNumber + " has entered with GUID: " + guid);

            Main.getMutex().release();
            acquiredMutex = 0;
            Main.getMutex().acquireUninterruptibly();
            acquiredMutex = 1;

            if (Main.getNumberOfWitchesInHouse() == 0 || Main.getNumberOfCatsInHouse() > 3) {
                leaveTheHouse(guid);
            } else {
                Main.setNumberOfCatsWaiting(Main.getNumberOfCatsWaiting() + 1);
                Main.getMutex().release();
                acquiredMutex = 0;
                Main.getCat().acquireUninterruptibly();

                Main.getMutex().acquireUninterruptibly();
                acquiredMutex = 1;
                while (isNotAllowedToLeave()) {
                    if (acquiredMutex == 1) {
                        Main.getMutex().release();
                        acquiredMutex = 0;
                    }

                    Main.getCat().acquireUninterruptibly();
                    Main.getMutex().acquireUninterruptibly();
                    acquiredMutex = 1;

                    if (isNotAllowedToLeave()) {
                        Main.getMutex().release();
                        acquiredMutex = 0;
                    }
                }

                leaveTheHouse(guid);
            }
        }
    }

    private boolean isNotAllowedToLeave() {
        return Main.getNumberOfWitchesInHouse() > 0 && Main.getNumberOfCatsInHouse() == 3;
    }

    private void leaveTheHouse(String guid) {
        System.out.printf(
                "Cat %s is leaving, current cat count: %s, current witch count: %s %n",
                catNumber,
                Main.getNumberOfCatsInHouse(),
                Main.getNumberOfWitchesInHouse()
        );
//                System.out.println("cat " + catNumber + " is leaving");

        Main.setNumberOfCatsInHouse(Main.getNumberOfCatsInHouse() - 1);

        System.out.println("cat " + catNumber + " has left with GUID: " + guid);

        if (Main.getNumberOfHuntersWaiting() > 0 && Main.getNumberOfCatsInHouse() == 0) {
            Main.setNumberOfHuntersWaiting(0);
            Main.getHunter().release();
        }
        Main.getMutex().release();
        acquiredMutex = 0;
    }
}
