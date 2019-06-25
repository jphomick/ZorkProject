import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static int health;
    private static ArrayList<Item> inventory;
    private static int money;
    private static ArrayList<Room> rooms;
    private static int currentRoom;
    private static Room room;

    public static void main(String[] args) {
        initialize();
        Scanner read = new Scanner(System.in);
        boolean gameOn = true;
        while (gameOn) {
            room = rooms.get(currentRoom - 1);
            System.out.print("You are in the " + room.name + ".");
            for (Item item : room.items) {
                System.out.print("\nThere is a " + item.name);
            }
            System.out.println("\nType 'c' for the possible commands.");

            String[] commands = {""};

            while (!commands[0].contains("refresh")) {
                commands = read.nextLine().toLowerCase().split(" ");

                if (commands.length > 0) {
                    if (commands[0].equals("c")) {
                        showCommands();
                    } else {
                        if (commands.length > 1 && !commands[0].contains("refresh")) {
                            processCommands(commands);
                        } else {
                            System.out.println("Please select a target for '" + commands[0] + "'");
                        }
                    }
                }
            }
        }
    }

    private static void processCommands(String[] commands) {
        StringBuilder concat = new StringBuilder();
        for (int i = 1; i < commands.length; i++) {
            concat.append(commands[i] + " ");
        }
        if (commands[0].equals("look") && commands[1].equals("room")) {
            int exits = 0;
            if (room.getNorth() > 0) {
                System.out.println("The " + getRoom(room.getNorth()).getName()
                        + " is north.");
                exits++;
            }
            if (room.getSouth() > 0) {
                System.out.println("The " + getRoom(room.getSouth()).getName()
                        + " is south.");
                exits++;
            }
            if (room.getEast() > 0) {
                System.out.println("The " + getRoom(room.getEast()).getName()
                        + " is east.");
                exits++;
            }
            if (room.getWest() > 0) {
                System.out.println("The " + getRoom(room.getWest()).getName()
                        + " is west.");
                exits++;
            }
            if (exits == 0) {
                System.out.println("No exits!");
            }
        } else if (commands[0].equals("move")) {
            if (commands[1].equals("north") && room.getNorth() > 0) {
                room = getRoom(room.getNorth());
                System.out.println("You moved to the " + room.getName() + "!");
            } else if (commands[1].equals("south") && room.getSouth() > 0) {
                room = getRoom(room.getSouth());
                System.out.println("You moved to the " + room.getName() + "!");
            } else if (commands[1].equals("east") && room.getEast() > 0) {
                room = getRoom(room.getEast());
                System.out.println("You moved to the " + room.getName() + "!");
            } else if (commands[1].equals("west") && room.getWest() > 0) {
                room = getRoom(room.getWest());
                System.out.println("You moved to the " + room.getName() + "!");
            }
            for (Item item : room.items) {
                System.out.print("\nThere is a " + item.name);
            }
        } else {
            aReturn result = room.interact(concat.toString().trim(), commands[0]);
            if (result == null) {
                for (Item item : inventory) {
                    if (item.name.toLowerCase().equals(commands[0])) {
                        result = item.use(concat.toString());
                    }
                }
            }
            if (result != null) {
                System.out.println(result.getMessage());
                process(result.code, result.value);
            } else {
                System.out.println("The item '" + concat + "' does not exist here.");
            }
        }
    }

    private static void initialize() {
        health = 10;
        money = 0;
        inventory = new ArrayList<>();
        rooms = new ArrayList<>();
        currentRoom = 1;
        int alternateRoom = 7;
        if (new Random().nextInt(1) == 0) {
            alternateRoom = 8;
        }
        Room room = new Room(1, "foyer", 2, 0, 0, 0);
        Item item = new Item("Dead Scorpion");
        item.addAction("take", "You unlodged the claw of the dead scorpion!", "claw", 0);
        item.addAction("look", "You look at the scorpion. It looks scary.");
        room.addItem(item);
        rooms.add(room);
        room = new Room(2, "front room", 0, 1, 4, 3);
        rooms.add(room);
        room = new Room(3, "library", 5, 0, 2, 0);
        rooms.add(room);
        room = new Room(4, "kitchen", 7, 0, 0, 2);
        rooms.add(room);
        room = new Room(5, "dining room", 0, 3, 0, 0);
        rooms.add(room);
        room = new Room(6, "vault", 0, 0, alternateRoom, 0);
        rooms.add(room);
        if (alternateRoom == 8) {
            room = new Room(8, "secret room", 0, 0, 0, 6);
            rooms.add(room);
        }
        room = new Room(7, "parlor", 0, 4, 0, 6);
        rooms.add(room);

    }

    private static void showCommands() {
        System.out.println("'move [t]' 'take [t]' 'look [t]' 'refresh'\n[t] indicates target required.");
    }

    private static Room getRoom(int id) {
        for (Room room : rooms) {
            if (room.getId() == id) {
                return room;
            }
        }
        return null;
    }

    private static void process(String code, int value) {
        if (code.equals("claw")) {
            Item item = new Item("Scorpion Claw");
            item.addAction("use", "You attacked with the claw!", "attack", 2);
            inventory.add(item);
            room.removeItem("Dead Scorpion");
        } else if (code.equals("gold")) {
            Item item = new Item("Scorpion Claw");
            item.addAction("use", "You attacked with the claw!", "attack", 2);
            inventory.add(item);
            room.removeItem("Dead Scorpion");
        }
    }
}
