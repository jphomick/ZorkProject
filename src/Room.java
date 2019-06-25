import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

public class Room {
    private int id, north, south, east, west;
    String name;
    ArrayList<Item> items;

    Room(int id, String name) {
        this.id = id;
        this.name = name;
        north = 0;
        south = 0;
        east = 0;
        west = 0;
        items = new ArrayList<>();
    }

    Room(int id, String name, int north, int south, int east, int west) {
        this.id = id;
        this.name = name;
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        items = new ArrayList<>();
    }

    public aReturn interact(String name, String command) {
        for (Item it : items) {
            if (it.name.toLowerCase().equals(name.toLowerCase())) {
                return it.use(command);
            }
        }
        return null;
    }

    public void removeItem(String name) {
        int idx = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).name.toLowerCase().equals(name.toLowerCase())) {
                idx = i;
            }
        }
        items.remove(idx);
    }

    public void addItem(Item it) {
        items.add(it);
    }

    public int getId() {
        return id;
    }

    public int getNorth() {
        return north;
    }

    public int getSouth() {
        return south;
    }

    public int getEast() {
        return east;
    }

    public int getWest() {
        return west;
    }

    public String getName() {
        return name;
    }

    public void setNorth(int north) {
        this.north = north;
    }

    public void setSouth(int south) {
        this.south = south;
    }

    public void setEast(int east) {
        this.east = east;
    }

    public void setWest(int west) {
        this.west = west;
    }
}


