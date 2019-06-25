import java.util.ArrayList;

public class Item {
    String name;
    ArrayList<Action> actions;

    Item(String name) {
        actions = new ArrayList<>();
        this.name = name;
        addAction("look", "You look at the " + name + ".");
    }

    public aReturn use(String command) {
        for (Action action : actions) {
            if (action.getCommand().equals(command.toLowerCase())) {
                return action.use();
            }
        }
        return new aReturn("'" + command + "' cannot be used on '" + name + "'");
    }

    public boolean addAction(String command, String message) {
        return addAction(command, message, "", 0);
    }

    public boolean addAction(String command, String message, String responseCode, int responseValue) {
        boolean exists = false;
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i).getCommand().equals(command.toLowerCase())) {
                exists = true;
                actions.get(i).changeMessage(message);
            }
        }
        if (exists) {
            return false;
        } else {
            actions.add(new Action(command, message, responseCode, responseValue));
            return true;
        }
    }
}