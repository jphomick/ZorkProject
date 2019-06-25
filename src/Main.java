import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static int health;
    private static ArrayList<Item> inventory;
    private static int money;
    private static ArrayList<Room> rooms;
    private static Room room;
    private static boolean gameOn;

    public static void main(String[] args) {
        initialize();
        Scanner read = new Scanner(System.in);
        gameOn = true;
        room = rooms.get(0);
        while (gameOn) {
            System.out.print("You are in the " + room.name + ".");
            for (Item item : room.items) {
                System.out.print("\nThere is a " + item.name);
            }
            System.out.println("\nType 'c' for the possible commands.");

            String[] commands = {""};

            while (commands.length > 0 && !commands[0].contains("refresh") && gameOn) {
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
        System.out.println("You left the house!");
        if (new Random().nextInt(4) == 0) {
            System.out.println("A ghost is following you...");
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
            boolean moved = false;
            if (commands[1].equals("north") && room.getNorth() > 0) {
                room = getRoom(room.getNorth());
                System.out.println("You moved to the " + room.getName() + "!");
                moved = true;
            } else if (commands[1].equals("south") && room.getSouth() > 0) {
                room = getRoom(room.getSouth());
                System.out.println("You moved to the " + room.getName() + "!");
                moved = true;
            } else if (commands[1].equals("east") && room.getEast() > 0) {
                room = getRoom(room.getEast());
                System.out.println("You moved to the " + room.getName() + "!");
                moved = true;
            } else if (commands[1].equals("west") && room.getWest() > 0) {
                room = getRoom(room.getWest());
                System.out.println("You moved to the " + room.getName() + "!");
                moved = true;
            } else if (commands[1].equals("south") && room.getId() == 1) {
                gameOn = false;
            }
            if (moved) {
                for (Item item : room.items) {
                    System.out.print("\nThere is a " + item.name);
                }
                System.out.println();
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
        item = new Item("Piano");
        item.addAction("play",
                "You play the piano. You're not sure how well you're playing, but you hear music!");
        item.addAction("look", "You look at the piano. It looks worn out but maybe you can play it.");
        room.addItem(item);
        rooms.add(room);
        room = new Room(3, "library", 5, 0, 2, 0);
        item = new Item("Spiders");
        room.addItem(item);
        item = new Item("Spiders");
        room.addItem(item);
        rooms.add(room);
        room = new Room(4, "kitchen", 7, 0, 0, 2);
        item = new Item("Bat");
        room.addItem(item);
        item = new Item("Bat");
        room.addItem(item);
        rooms.add(room);
        room = new Room(5, "dining room", 0, 3, 0, 0);
        item = new Item("Dust");
        item.addAction("take", "You try to take the dust but it just floats around the room.");
        room.addItem(item);
        item = new Item("Empty Box");
        item.addAction("take", "You take that empty box.", "box", 0);
        room.addItem(item);
        rooms.add(room);
        room = new Room(6, "vault", 0, 0, alternateRoom, 0);
        item = new Item("Walking Skeleton");
        room.addItem(item);
        item = new Item("Walking Skeleton");
        room.addItem(item);
        item = new Item("Walking Skeleton");
        room.addItem(item);
        rooms.add(room);
        if (alternateRoom == 8) {
            room = new Room(8, "secret room", 0, 0, 0, 6);
            item = new Item("Piles of Gold");
            item.addAction("take", "You found 500 money!", "gold", 500);
            item.addAction("look", "You look at the money. It sparkles brightly!");
            room.addItem(item);
            rooms.add(room);
        }
        room = new Room(7, "parlor", 0, 4, 0, 6);
        item = new Item("Treasure Chest");
        item.addAction("take", "You need a key to unlock the chest", "", 0);
        item.addAction("look", "You look at the money. It sparkles brightly!");
        room.addItem(item);
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

    private static Item getItem(String name, ArrayList<Item> list) {
        for (Item item : list) {
            if (item.name.toLowerCase().equals(name.toLowerCase())) {
                return item;
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
            money += value;
            room.removeItem("Piles of Gold");
            gameOn = false;
            System.out.println("A strange door opens up!");
        } else if (code.equals("key") && getItem("Key", inventory) != null
                && getItem("Treasure Chest", room.items) != null) {
            money += 500;
            inventory.remove(getItem("Key", inventory));
            room.removeItem("Treasure Chest");
            System.out.println("You open the chest and got 500 money!");
        } else if (code.equals("box")) {
            Item item = new Item("Empty Box");
            inventory.add(item);
            room.removeItem("Empty Box");
        }
    }
}
