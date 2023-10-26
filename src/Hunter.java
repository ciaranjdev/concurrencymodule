import java.util.UUID;

public class Hunter extends Thread {

    private final int hunterNumber;
    private int acquiredMutex = 0;

    public Hunter(int hunterNumber) {
        this.hunterNumber = hunterNumber;
    }

    public void run() {

        while (true) {
            Main.getMutex().acquireUninterruptibly();
            acquiredMutex = 1;

            String guid = UUID.randomUUID().toString();

            if (Main.getNumberOfCatsInHouse() == 0 && Main.getNumberOfWitchesInHouse() == 0) {
                enterTheHouse(guid);
            } else {
                Main.setNumberOfHuntersWaiting(Main.getNumberOfHuntersWaiting() + 1);
                Main.getMutex().release();
                acquiredMutex = 0;
                Main.getHunter().acquireUninterruptibly();

                Main.getMutex().acquireUninterruptibly();
                acquiredMutex = 1;
                while (isNotAllowedToEnter()) {
                    if (acquiredMutex == 1) {
                        Main.getMutex().release();
                        acquiredMutex = 0;
                    }

                    Main.getHunter().acquireUninterruptibly();
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

            if (Main.getNumberOfHuntersInHouse() > 0) {
                System.out.printf(
                        "Hunter %s is leaving, current cat count: %s, current witch count: %s %n",
                        hunterNumber,
                        Main.getNumberOfCatsInHouse(),
                        Main.getNumberOfWitchesInHouse()
                );
//                System.out.println("hunter " + hunterNumber + " is leaving");

                Main.setNumberOfHuntersInHouse(Main.getNumberOfHuntersInHouse() - 1);

                System.out.println("hunter " + hunterNumber + " has left with GUID: " + guid);

                if (Main.getNumberOfWitchesWaiting() > 0 && Main.getNumberOfHuntersInHouse() == 0) {
                    Main.setNumberOfWitchesWaiting(0);
                    Main.getWitch().release();
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
        return Main.getNumberOfCatsInHouse() > 0 || Main.getNumberOfWitchesInHouse() > 0;
    }

    private void enterTheHouse(String guid) {
        System.out.printf(
                "Hunter %s is entering, current cat count: %s, current witch count: %s %n",
                hunterNumber,
                Main.getNumberOfCatsInHouse(),
                Main.getNumberOfWitchesInHouse()
        );
//                System.out.println("hunter " + hunterNumber + " is entering");

        Main.setNumberOfHuntersInHouse(Main.getNumberOfHuntersInHouse() + 1);

        System.out.println("hunter " + hunterNumber + " has entered with GUID: " + guid);

        Main.getMutex().release();
        acquiredMutex = 0;
    }
}
