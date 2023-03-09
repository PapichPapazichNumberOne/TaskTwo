import java.util.*;
import java.util.concurrent.TimeUnit;

interface IRobot {
    void moveToRoom(String room);
    void cleanRoom(String room);
    boolean isTimeToClean(String room);
}

interface IHouse {
    void addRoom(String room);
    List<String> getRooms();
    void updateLastCleaned(String room);
    long getLastCleaned(String room);
}

interface IUserInput {
    String getInput(String prompt);
    String getRandomRoom(List<String> rooms);
}

public class RobotHousekeeper implements IRobot, IHouse, IUserInput {

    private Map<String, Long> lastCleanedTimes;
    private List<String> rooms;
    private String currentRoom;

    public RobotHousekeeper() {
        lastCleanedTimes = new HashMap<>();
        rooms = new ArrayList<>();
    }

    public void addRoom(String room) {
        rooms.add(room);
        lastCleanedTimes.put(room, 0L);
    }

    public List<String> getRooms() {
        return rooms;
    }

    public void updateLastCleaned(String room) {
        lastCleanedTimes.put(room, System.currentTimeMillis());
    }

    public long getLastCleaned(String room) {
        return lastCleanedTimes.get(room);
    }

    public void moveToRoom(String room) {
        System.out.println("Moving to " + room);
        currentRoom = room;
    }

    public void cleanRoom(String room) {
        System.out.println("Cleaning " + room);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateLastCleaned(room);
        System.out.println("Finished cleaning " + room);
    }

    public boolean isTimeToClean(String room) {
        long lastCleaned = getLastCleaned(room);
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastCleaned) >= 60000;
    }

    public String getInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public String getRandomRoom(List<String> rooms) {
        Random random = new Random();
        int index = random.nextInt(rooms.size());
        return rooms.get(index);
    }

    public void run() {
        System.out.println("Robot housekeeper started");

// Add rooms to the house
        addRoom("kitchen");
        addRoom("bedroom");
        addRoom("bathroom");
        addRoom("living room");
        addRoom("study");

// Clean all rooms
        while (true) {
            boolean allRoomsCleaned = true;

            for (String room : rooms) {
                if (isTimeToClean(room)) {
                    allRoomsCleaned = false;


                    moveToRoom(room);


                    cleanRoom(room);


                    String input = getInput("Нажмите Enter, чтобы очистить другую комнату, или нажмите «r», чтобы выбрать случайную комнату: ");
                    if (!input.isEmpty()) {

                        String randomRoom = getRandomRoom(rooms);
                        while (randomRoom.equals(room)) {
                            randomRoom = getRandomRoom(rooms);
                        }
                        moveToRoom(randomRoom);
                    }
                }
            }

            if (allRoomsCleaned) {
                System.out.println("Все номера убраны менее чем за 1 минуту");
                break;
            }
        }

        System.out.println("Робот-домработница закончила");
    }
}