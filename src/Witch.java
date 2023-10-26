import java.util.UUID;

public class Witch extends Thread {

    private final int witchNumber;
    private int acquiredMutex = 0;

    public Witch(int witchNumber) {
        this.witchNumber = witchNumber;
    }

    public void run() {

        while (true) {
            Main.getMutex().acquireUninterruptibly();
            acquiredMutex = 1;

            String guid = UUID.randomUUID().toString();

            if (Main.getNumberOfWitchesInHouse() == 0
                    && Main.getNumberOfCatsInHouse() >= 3
                    && Main.getNumberOfHuntersInHouse() == 0) {

                enterTheHouse(guid);
            } else {
                Main.setNumberOfWitchesWaiting(Main.getNumberOfWitchesWaiting() + 1);
                Main.getMutex().release();
                acquiredMutex = 0;
                Main.getWitch().acquireUninterruptibly();

                Main.getMutex().acquireUninterruptibly();
                acquiredMutex = 1;
                while (isNotAllowedToEnter()) {
                    if (acquiredMutex == 1) {
                        Main.getMutex().release();
                        acquiredMutex = 0;
                    }

                    Main.getWitch().acquireUninterruptibly();
                    Main.getMutex().acquireUninterruptibly();
                    acquiredMutex = 1;

                    if (isNotAllowedToEnter()) {
                        Main.getMutex().release();
                        acquiredMutex = 0;
                    }
                }

                enterTheHouse(guid);
            }

            Main.getMutex().acquireUninterruptibly();
            acquiredMutex = 1;

            if (Main.getNumberOfWitchesInHouse() > 0) {
                System.out.printf(
                        "Witch %s is leaving, current cat count: %s, current hunter count: %s %n",
                        witchNumber,
                        Main.getNumberOfCatsInHouse(),
                        Main.getNumberOfHuntersInHouse()
                );
//                System.out.println("witch " + witchNumber + " is leaving");

                Main.setNumberOfWitchesInHouse(Main.getNumberOfWitchesInHouse() - 1);

                System.out.println("witch " + witchNumber + " has left with GUID: " + guid);

                if (Main.getNumberOfWitchesInHouse() == 0) {
                    if (Main.getNumberOfCatsWaiting() > 0) {
                        Main.setNumberOfCatsWaiting(0);
                        Main.getCat().release();
                    }
                    if (Main.getNumberOfWitchesWaiting() > 0) {
                        Main.setNumberOfWitchesWaiting(0);
                        Main.getWitch().release();
                    }
                }

                Main.getMutex().release();
                acquiredMutex = 0;
            } else {
                Main.getMutex().release();
                acquiredMutex = 0;
            }
        }
    }

    private boolean isNotAllowedToEnter() {
        return Main.getNumberOfWitchesInHouse() > 0
                || Main.getNumberOfCatsInHouse() < 3
                || Main.getNumberOfHuntersInHouse() > 0;
    }

    private void enterTheHouse(String guid) {
        System.out.printf(
                "Witch %s is entering, current cat count: %s, current hunter count: %s %n",
                witchNumber,
                Main.getNumberOfCatsInHouse(),
                Main.getNumberOfHuntersInHouse()
        );
//                System.out.println("witch " + witchNumber + " is entering");

        Main.setNumberOfWitchesInHouse(Main.getNumberOfWitchesInHouse() + 1);

        System.out.println("witch " + witchNumber + " has entered with GUID: " + guid);

        Main.getMutex().release();
        acquiredMutex = 0;
    }
}
