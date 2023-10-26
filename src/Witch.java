import java.util.UUID;

public class Witch extends Thread {

    private final int witchNumber;

    public Witch(int witchNumber) {
        this.witchNumber = witchNumber;
    }

    public void run() {

        while (true) {
            Main.getMutex().acquireUninterruptibly();

            String guid = UUID.randomUUID().toString();

            if (Main.getNumberOfWitchesInHouse() == 0
                    && Main.getNumberOfCatsInHouse() >= 3
                    && Main.getNumberOfHuntersInHouse() == 0) {

                enterTheHouse(guid);
            } else {
                Main.setNumberOfWitchesWaiting(Main.getNumberOfWitchesWaiting() + 1);
                Main.getMutex().release();
                Main.getWitch().acquireUninterruptibly();

                Main.getMutex().acquireUninterruptibly();
                int acquiredMutex = 1;
                while (isNotAllowedToEnter()) {
                    if (acquiredMutex == 1) {
                        Main.getMutex().release();
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

            if (Main.getNumberOfWitchesInHouse() > 0) {

                Main.setNumberOfWitchesInHouse(Main.getNumberOfWitchesInHouse() - 1);

                System.out.printf(
                        "Witch %s has left, current cat count: %s, current hunter count: %s, with GUID: %s %n",
                        witchNumber,
                        Main.getNumberOfCatsInHouse(),
                        Main.getNumberOfHuntersInHouse(),
                        guid
                );

                if (Main.getNumberOfWitchesInHouse() == 0) {
                    if (Main.getNumberOfCatsWaiting() > 0) {
                        Main.setNumberOfCatsWaiting(0);
                        Main.getCat().release();
                    }
                    if (Main.getNumberOfWitchesWaiting() > 0) {
                        Main.setNumberOfWitchesWaiting(0);
                        Main.getWitch().release();
                    }
                    if (Main.getNumberOfHuntersWaiting() > 0) {
                        Main.setNumberOfHuntersWaiting(0);
                        Main.getHunter().release();
                    }
                }

                Main.getMutex().release();
            } else {
                Main.getMutex().release();
            }
        }
    }

    private boolean isNotAllowedToEnter() {
        return Main.getNumberOfWitchesInHouse() > 0
                || Main.getNumberOfCatsInHouse() < 3
                || Main.getNumberOfHuntersInHouse() > 0;
    }

    private void enterTheHouse(String guid) {
        Main.setNumberOfWitchesInHouse(Main.getNumberOfWitchesInHouse() + 1);

        System.out.printf(
                "Witch %s has entered, current cat count: %s, current hunter count: %s, with GUID: %s %n",
                witchNumber,
                Main.getNumberOfCatsInHouse(),
                Main.getNumberOfHuntersInHouse(),
                guid
        );

        Main.getMutex().release();
    }
}
