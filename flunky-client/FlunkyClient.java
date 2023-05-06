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
            case 101:
                gui.gameOngoing(true);
                new Thread(() -> {
                    gui.refreshScore();
                }).start();
                break;
            case 102:
                gui.lInfo.setText("Type: " + msg.split(" ")[1]);
                break;
            case 103:
                if(askedForP1) gui.setScore(Double.parseDouble(msg.split(" ")[1]));
                else gui.setEnemyScore(Double.parseDouble(msg.split(" ")[1]));
                break;
            case 104:
                gui.gameOngoing(false);
                break;
            case 301:
                gui.bDisconnect1_ActionPerformed(null);
                break;
            case 400:
                gui.lInfo.setText("");
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
