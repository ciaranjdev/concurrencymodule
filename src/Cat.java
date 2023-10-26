import java.util.UUID;

public class Cat extends Thread {

    private final int catNumber;

    public Cat(int catNumber) {
        this.catNumber = catNumber;
    }

    public void run() {

        while (true) {
            Main.getMutex().acquireUninterruptibly();

            String guid = UUID.randomUUID().toString();

            Main.setNumberOfCatsInHouse(Main.getNumberOfCatsInHouse() + 1);

            System.out.println("cat " + catNumber + " has entered with GUID: " + guid);

            if (Main.getNumberOfWitchesWaiting() > 0 && Main.getNumberOfCatsInHouse() >= 3) {
                Main.setNumberOfWitchesWaiting(0);
                Main.getWitch().release();
            }

            Main.getMutex().release();
            Main.getMutex().acquireUninterruptibly();

            if (Main.getNumberOfWitchesInHouse() == 0 || Main.getNumberOfCatsInHouse() > 3) {
                leaveTheHouse(guid);
            } else {
                Main.setNumberOfCatsWaiting(Main.getNumberOfCatsWaiting() + 1);
                Main.getMutex().release();
                Main.getCat().acquireUninterruptibly();

                Main.getMutex().acquireUninterruptibly();
                int acquiredMutex = 1;
                while (isNotAllowedToLeave()) {
                    if (acquiredMutex == 1) {
                        Main.getMutex().release();
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
        Main.setNumberOfCatsInHouse(Main.getNumberOfCatsInHouse() - 1);

        System.out.printf(
                "Cat %s has left, current cat count: %s, current witch count: %s, with GUID: %s %n",
                catNumber,
                Main.getNumberOfCatsInHouse(),
                Main.getNumberOfWitchesInHouse(),
                guid
        );

        if (Main.getNumberOfHuntersWaiting() > 0 && Main.getNumberOfCatsInHouse() == 0) {
            Main.setNumberOfHuntersWaiting(0);
            Main.getHunter().release();
        }
        Main.getMutex().release();
    }
}
