import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static int health;
    private static ArrayList<Item> inventory;
    private static int money, defeated, visited, objects, robber;
    private static ArrayList<Room> rooms;
    private static Room room;
    private static boolean gameOn;
    private static ArrayList<String> seen;

    public static void main(String[] args) {
        initialize();
        Scanner read = new Scanner(System.in);
        gameOn = true;
        room = rooms.get(0);
        while (gameOn) {
            System.out.print("You are in the " + room.name + ".");
            System.out.print("\nYour money: " + money + "\nThings of interest:");
            for (Item item : room.items) {
                System.out.print("\n" + item.name);
                if (!seen.contains(item.name)) {
                    seen.add(item.name);
                }
            }
            System.out.println("\nType 'c' for the possible commands.");

            String[] commands = {""};

            while (commands.length > 0 && !commands[0].contains("refresh") && gameOn) {
                if (robber == room.getId()) {
                    System.out.println("It's a robber! He stole your money!");
                    money = 0;
                    robber = -1;
                }

                commands = read.nextLine().toLowerCase().split(" ");

                if (commands.length > 0 && !commands[0].contains("refresh")) {
                    if (commands[0].equals("c")) {
                        showCommands();
                    } else {
                        if (commands[0].equals("bag")) {
                            System.out.println("Items in inventory:");
                            for (Item item : inventory) {
                                System.out.println(item.name);
                            }
                        } else if (commands.length > 1 && !commands[0].contains("refresh")) {
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
        System.out.println("Stats:" +
                "\nMoney collected:\t" + money +
                "\nEnemies defeated:\t" + defeated +
                "\nRooms visited:\t\t" + visited +
                "\nUnique objects:\t\t" + seen.size());
    }

    private static void processCommands(String[] commands) {
        StringBuilder concat = new StringBuilder();
        for (int i = 1; i < commands.length; i++) {
            concat.append(commands[i] + " ");
        }
        String concatFinal = concat.toString().trim();
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
            } else {
                System.out.println("You can't move that way!");
            }
            if (moved) {
                visited++;
                System.out.print("\nYour money: " + money + "\nThings of interest:");
                for (Item item : room.items) {
                    System.out.print("\n" + item.name);
                    if (!seen.contains(item.name)) {
                        seen.add(item.name);
                    }
                }
                System.out.println();
            }
        } else {
            aReturn result = room.interact(concatFinal, commands[0]);
            if (result == null) {
                for (Item item : inventory) {
                    if (item.name.toLowerCase().equals(concatFinal)) {
                        result = item.use(commands[0]);
                    }
                }
            }
            if (result != null) {
                System.out.println(result.getMessage());
                process(result.code, result.value);
            } else {
                System.out.println("The item '" + concatFinal + "' does not exist here.");
            }
        }
    }

    private static void showCommands() {
        System.out.println("'move [t]' 'take [t]' 'look [t]' 'use [t]' 'bag' 'refresh'\n[t] indicates target required.");
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
            Item item = new Item("Claw");
            item.addAction("use", "You attacked with the claw!", "attack", 2);
            inventory.add(item);
            seen.add("Claw");
            room.removeItem("Dead Scorpion");
        } else if (code.equals("money")) {
            money += value;
            room.removeItem("Money");
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
            System.out.println("You open the chest and got 200 money!");
        } else if (code.equals("box")) {
            Item item = new Item("Empty Box");
            inventory.add(item);
            room.removeItem("Empty Box");
        } else if (code.equals("attack")) {
            String text = "";
            if (room.removeItem("Walking Skeleton")) {
                text = "You defeated a walking skeleton!";
            }
            if (room.removeItem("Spiders")) {
                text = "You defeated the spiders!";
            }
            if (room.removeItem("Bats")) {
                text = "You defeated the bats!\nThey dropped a 'key'!";
                Item item = new Item("Key");
                item.addAction("use", "You attempt to open something nearby", "key", 0);
                inventory.add(item);
                seen.add("Key");
            }
            if (!text.equals("")) {
                System.out.println(text);
                int dropped = (new Random().nextInt(10) + 1) * 10;
                money += dropped;
                System.out.println(dropped + " money dropped!");
                defeated++;
            } else {
                System.out.println("You hit nothing!");
            }
        }
    }

    private static void initialize() {
        health = 10;
        money = 0;
        defeated = 0;
        visited = 1;
        objects = 1;
        seen = new ArrayList<>();
        inventory = new ArrayList<>();
        rooms = new ArrayList<>();
        Random r = new Random();
        robber = r.nextInt(8) + 1;
        int alternateRoom = 7;
        if (r.nextInt(4) == 0) {
            alternateRoom = 8;
        }
        Room room = new Room(1, "foyer", 2, 0, 0, 0);
        Item item = new Item("Dead Scorpion");
        item.addAction("take", "You unlodged the claw of the dead scorpion!\nYou now have a 'claw'!"
                , "claw", 0);
        item.addAction("look", "You look at the scorpion. It looks scary.");
        room.addItem(item);
        item = new Item("Money");
        int amount = r.nextInt(1000);
        item.addAction("take", "You take the " + amount + " money!"
                , "money", amount);
        item.addAction("look", "It's " + amount + " money!");
        room.addItem(item);
        rooms.add(room);
        room = new Room(2, "front room", 0, 1, 4, 3);
        item = new Item("Piano");
        item.addAction("play",
                "You play the piano. You're not sure how well you're playing, but you hear music!");
        item.addAction("use",
                "You play the piano. You're not sure how well you're playing, but you hear music!");
        item.addAction("look", "You look at the piano. It looks worn out but maybe you can play it.");
        room.addItem(item);
        item = new Item("Money");
        amount = r.nextInt(1000);
        item.addAction("take", "You take the " + amount + " money!"
                , "money", amount);
        item.addAction("look", "It's " + amount + " money!");
        room.addItem(item);
        rooms.add(room);
        room = new Room(3, "library", 5, 0, 2, 0);
        item = new Item("Spiders");
        room.addItem(item);
        item = new Item("Money");
        amount = r.nextInt(1000);
        item.addAction("take", "You take the " + amount + " money!"
                , "money", amount);
        item.addAction("look", "It's " + amount + " money!");
        room.addItem(item);
        rooms.add(room);
        room = new Room(4, "kitchen", 7, 0, 0, 2);
        item = new Item("Bats");
        room.addItem(item);
        item = new Item("Money");
        amount = r.nextInt(1000);
        item.addAction("take", "You take the " + amount + " money!"
                , "money", amount);
        item.addAction("look", "It's " + amount + " money!");
        room.addItem(item);
        rooms.add(room);
        room = new Room(5, "dining room", 0, 3, 0, 0);
        item = new Item("Dust");
        item.addAction("take", "You try to take the dust but it just floats around the room.");
        room.addItem(item);
        item = new Item("Empty Box");
        item.addAction("take", "You take that empty box.", "box", 0);
        room.addItem(item);
        item = new Item("Money");
        amount = r.nextInt(1000);
        item.addAction("take", "You take the " + amount + " money!"
                , "money", amount);
        item.addAction("look", "It's " + amount + " money!");
        room.addItem(item);
        rooms.add(room);
        room = new Room(6, "vault", 0, 0, alternateRoom, 0);
        item = new Item("Walking Skeleton");
        room.addItem(item);
        item = new Item("Walking Skeleton");
        room.addItem(item);
        item = new Item("Walking Skeleton");
        room.addItem(item);
        item = new Item("Money");
        amount = r.nextInt(1000);
        item.addAction("take", "You take the " + amount + " money!"
                , "money", amount);
        item.addAction("look", "It's " + amount + " money!");
        room.addItem(item);
        rooms.add(room);
        if (alternateRoom == 8) {
            room = new Room(8, "secret room", 0, 0, 0, 6);
            item = new Item("Piles of Gold");
            item.addAction("take", "You found 5000 money!", "gold", 5000);
            item.addAction("look", "You look at the money. It sparkles brightly!");
            room.addItem(item);
            rooms.add(room);
        }
        room = new Room(7, "parlor", 0, 4, 0, 6);
        item = new Item("Treasure Chest");
        item.addAction("take", "You need a key to unlock the chest", "", 0);
        item.addAction("look", "You look at the chest. It has an ornate lock on it.");
        room.addItem(item);
        item = new Item("Money");
        amount = r.nextInt(1000);
        item.addAction("take", "You take the " + amount + " money!"
                , "money", amount);
        item.addAction("look", "It's " + amount + " money!");
        room.addItem(item);
        rooms.add(room);

    }
}
