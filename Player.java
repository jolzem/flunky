
/**
 * The player class.
 * Saves the players IP, Port, Score and Name.
 * 
 * @author Johannes Olzem
 * @version 0.1
 */
public class Player {
    private String ip;
    private int port;
    private double score;
    private String name;
    private boolean hasWon;
    private FlunkyServer server;

    /**
     * Constructor of class Player
     */
    public Player(String pIP, int pPort, FlunkyServer pServer) {
        ip = pIP;
        port = pPort;
        server = pServer;
        name = "Player";
        score = 0;
    }

    /**
     * Returns port of player
     * 
     * @return  Port of the player
     */
    public int getPort() {
        return port;
    }
    
    /**
     * Returns IP Address 
     * 
     * @return  IP of the player
     */
    public String getIP() {
        return ip;
    }
    
    /**
     * Sends {msg} to the player
     * 
     * @param msg   The message being sent to the player
     */
    public void send(String msg) {
        server.send(ip, port, msg);
    }
    
    /**
     * Sets new name for player
     * 
     * @param pName The new name for the player
     */
    public void setName(String pName) {
        name = pName;
    }
    
    /**
     * Returns the name of the player
     * 
     * @return  Name of the player
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the players score
     * 
     * @param pScore    The players new score
     */
    public void setScore(double pScore) {
        score = pScore;
    }
    
    /**
     * Adds to the score of the player
     * 
     * @param   The amount being added to the score
     */
    public void addScore(double pAdd) {
        score += pAdd;
    }
    
    /**
     * Returns the players score
     * 
     * @return  the players score
     */
    public double getScore() {
        return score;
    }
    
    /**
     * returns true if the player has won
     * 
     * @return  true if the player has won, else false
     */
    public boolean hasWon() {
        return hasWon;
    }
    
    /**
     * Sets if the player has won
     * 
     * @param pHasWon   if the player has won
     */
    public void hasWon(boolean pHasWon) {
        hasWon = pHasWon;
    }
}
