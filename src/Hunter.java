import java.util.UUID;

public class Hunter extends Thread {

    private final int hunterNumber;

    public Hunter(int hunterNumber) {
        this.hunterNumber = hunterNumber;
    }

    public void run() {

        while (true) {
            Main.getMutex().acquireUninterruptibly();

            String guid = UUID.randomUUID().toString();

            if (Main.getNumberOfCatsInHouse() == 0 && Main.getNumberOfWitchesInHouse() == 0) {
                enterTheHouse(guid);
            } else {
                Main.setNumberOfHuntersWaiting(Main.getNumberOfHuntersWaiting() + 1);
                Main.getMutex().release();
                Main.getHunter().acquireUninterruptibly();

                Main.getMutex().acquireUninterruptibly();
                int acquiredMutex = 1;
                while (isNotAllowedToEnter()) {
                    if (acquiredMutex == 1) {
                        Main.getMutex().release();
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

            if (Main.getNumberOfHuntersInHouse() > 0) {

                Main.setNumberOfHuntersInHouse(Main.getNumberOfHuntersInHouse() - 1);

                System.out.printf(
                        "Hunter %s has left, current cat count: %s, current witch count: %s, with GUID: %s %n",
                        hunterNumber,
                        Main.getNumberOfCatsInHouse(),
                        Main.getNumberOfWitchesInHouse(),
                        guid
                );

                if (Main.getNumberOfWitchesWaiting() > 0 && Main.getNumberOfHuntersInHouse() == 0) {
                    Main.setNumberOfWitchesWaiting(0);
                    Main.getWitch().release();
                }

                Main.getMutex().release();
            } else {
                Main.getMutex().release();
            }
        }
    }

    private boolean isNotAllowedToEnter() {
        return Main.getNumberOfCatsInHouse() > 0 || Main.getNumberOfWitchesInHouse() > 0;
    }

    private void enterTheHouse(String guid) {
        Main.setNumberOfHuntersInHouse(Main.getNumberOfHuntersInHouse() + 1);

        System.out.printf(
                "Hunter %s has entered, current cat count: %s, current witch count: %s, with GUID: %s %n",
                hunterNumber,
                Main.getNumberOfCatsInHouse(),
                Main.getNumberOfWitchesInHouse(),
                guid
        );

        Main.getMutex().release();
    }
}
