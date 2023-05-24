/**
 * Write a description of class FlunkyClient here.
 *
 * @author Johannes Olzem
 * @version 0.1
 */
public class FlunkyClient extends Client {
    private FlunkyGUI gui;
    private boolean askedForP1;

    /**
     * Constructor for objects of class FlunkyClient
     */
    public FlunkyClient(String ip, FlunkyGUI gui) {
        super(ip, 2400); // connect to ip on port 2400
        this.gui = gui;
    }

    public void processMessage(String msg) {
        int code = Integer.parseInt(msg.split(" ")[0]);
        switch(code) {
            case 101: // Game Started
                gui.gameOngoing(true);
                new Thread(() -> {
                    gui.refreshScore();
                }).start();
                gui.setImage("start.png"); // both players in normal position
                break;
            case 102: // Chars to type
                gui.lInfo.setText("Type: " + msg.split(" ")[1]);
                if(msg.split(" ")[1].length() < 2) gui.isPickingUp(false);
                else gui.isPickingUp(true);
                gui.setImage("p1throw.png");
                break;
            case 103: // Player Score
                if(askedForP1) gui.setScore(Double.parseDouble(msg.split(" ")[1]));
                else gui.setEnemyScore(Double.parseDouble(msg.split(" ")[1]));
                break;
            case 104: // Winner
                gui.gameOngoing(false);
                break;
            case 107: // Enemy Name
                gui.setP2Name(msg.split(" ")[1]);
                break;
            case 203: // Target Hit
                gui.setImage("p1drink.png");
                gui.lInfo.setText("Hit!");
                break;
            case 301: // Session full
                gui.setVisible(false);
                break;
            case 303: // Enemy has hit
                gui.setImage("p2drink.png");
                break;
            case 400: // Bad request
                gui.lInfo.setText("");
                break;
            case 401: // Not enough players
                gui.lInfo.setText("Player disconnected");
                gui.setImage("connect.png");
                break;
            case 402: // Target not hit
                gui.lInfo.setText("Missed");
                gui.setImage("p2throw.png");
                break;
            case 404:
                break;
            default:
                // remove response code from info
                String info = msg.substring(msg.indexOf(' ')+1);
                gui.lInfo.setText(info);
        }
    }

    public void askForP1(boolean value) {
        this.askedForP1 = value;
    }
}
