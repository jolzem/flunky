import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * FLUNKY BALL
 * For this game, two players are needed.
 * In this implementation, the first player starts by getting a prompt to
 * press a random button. If they manage to do that within a specific time,
 * the imaginary bottle in the middle of the playing field will be hit and
 * will fall. The other player must now type a multitude of characters to
 * put the bottle back up. The longer it takes for player 2 to type, the
 * more points player 1 will receive. This is then repeated, alternating
 * between players until a player has gathered enough points.
 *
 * RESPONSE CODES:
 * The response codes are comparable to HTTP response codes.
 * 1XX Information:
 * - 100 Game ready
 * - 101 Game started
 * - 102 Character(s) to type
 * - 103 {int} Player score
 * - 104 {String} Winner
 * - 105 {String} Loser
 * - 106 {String} {String} (Player Names)
 *
 * 2XX Success:
 * - 200 Successfull Command (Default)
 * - 201 Connected
 * - 203 Target hit
 * - 204 Correct input
 *
 * 3XX Additional Actions required:
 * - 301 Session full
 * - 302 Player name(s) not changed
 *
 * 4XX Client Error:
 * - 400 Bad request
 * - 401 Not enough players
 * - 402 Target not hit
 * - 403 Wrong input
 *   (when player types wrong characters to put bottle back up)
 * - 404 Player not found
 *
 * @author Johannes Olzem
 * @version 0.1
 */
public class FlunkyServer extends Server {
    private static int MIN_CHARS = 4;
    private static int MAX_CHARS = 6;
    private static double SCORE_TO_ADD = 0.5;
    private static int TIME_TO_SUBMIT = 5000; // TODO change to a sensible number
    
    private Player player1;
    private Player player2;
    private char[] keys = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private boolean gameStarted = false;
    private String submittedString = "";

    /**
     * Constructor of class FlunkyServer
     */
    public FlunkyServer() {
        super(2400); // Start server on port 2400
    }

    /**
     * Assigns new connection to a player, if player isn't assigned yet.
     * If all players are assigned, it kicks any new connection.
     *
     * @param ip    IP adress of sender
     * @param port  Port of sender
     */

    public void processNewConnection(String ip, int port) {
        send(ip, port, "201 Connected");
        if(player1 == null) player1 = new Player(ip, port, this);
        else if(player2 == null) player2 = new Player(ip, port, this);
        else {
            send(ip, port, "301 Session full");
            closeConnection(ip, port);
        }
    }

    /**
     * Removes assignment from player, when a client disconnects
     *
     * @param ip    IP of sender
     * @param port  Port of sender
     */
    public void processClosingConnection(String ip, int port) {
        if(player1 != null && player1.getIP().equals(ip) && player1.getPort() == port) player1 = null;
        else if(player2 != null && player2.getIP().equals(ip) && player2.getPort() == port) player2 = null;
    }

    public void processOpeningConnection(String ip, int port) {}

    /**
     * Processes commands sent to server
     * VERBS:
     *  - INFO:
     *    - READY: returns 100 if the game is ready, otherwise a
     *      corresponding error
     *    - NAMES: returns 106 with both player names
     *    - SCORE: returns 103 <score>
     *    - OUTCOME <playername>: returns 104 if <playername> has won, 
     *      105 if <playername> has lost and 404 if <playername> wasn't
     *      found
     *  - START: starts the game by calling the game() method
     *  - NAME: <playername>: player can change their name if the game hasn't started
     *  - SUBMIT: submit the asked character or string
     *  - RESET: cleans everything from previous game
     *
     * @param ip    IP of sender
     * @param port  Port of sender
     * @param msg   Message of sender
     */
    public void processMessage(String ip, int port, String msg) {
        // do nothing if the message is empty
        if(msg.equals("") || msg.equals("")) return;
        String[] cmd = msg.split(" ");
        String verb = cmd[0].toUpperCase();
        // remove first element in array
        String[] args = Arrays.copyOfRange(cmd, 1, cmd.length);

        switch(verb) {
            case "INFO": // INFO <command>
                switch(args[0]) {
                    case "READY": // INFO READY
                        if(player1 == null || player2 == null) send(ip, port, "401 Not enough Players");
                        else if(player1.getName().equals("Player") || player2.getName().equals("Player")) send(ip, port, "302 Player Name(s) not changed");
                        else send(ip, port, "100 Game ready");
                        break;
                    case "NAMES":
                        if(player1 == null || player2 == null) send(ip, port, "401 Not enough Players");
                        else if(player1.getName().equals("Player") || player2.getName().equals("Player")) send(ip, port, "302 Player Name(s) not changed");
                        else send(ip, port, "106 " + player1.getName() + " " + player2.getName());
                        break;
                    case "SCORE":
                        if(player1 == null || player2 == null) send(ip, port, "401 Not enough Players");
                        else if(player1.getName().equals("Player") || player2.getName().equals("Player")) send(ip, port, "302 Player Name(s) not changed");
                        else if(player1.getName().equals(args[1])) send(ip, port, "103 " + player1.getScore());
                        else if(player2.getName().equals(args[1])) send(ip, port, "103 " + player2.getScore());
                        else send(ip, port, "404 Player not found");
                        break;  
                    case "OUTCOME": // INFO OUTCOME <playername>
                        if(player1.getName().equals(args[1]))
                            if(player1.hasWon()) send(ip, port, "104 " + player1.getName() + " Winner");
                            else send(ip, port, "105 " + player1.getName() + " Loser");
                        else if(player2.getName().equals(args[1]))
                            if(player2.hasWon()) send(ip, port, "104 " + player2.getName() + " Winner");
                            else send(ip, port, "105 " + player2.getName() + " Loser");
                        else send(ip, port, "404 Player not found");
                        break;
                }
                break;
            case "START": // START
                if(gameStarted) return;
                if(player1 == null || player2 == null) send(ip, port, "401 Not enough players");
                else if(player1.getName().equals("Player") || player2.getName().equals("Player")) send(ip, port, "302 Player Name(s) not changed");
                // start game in new thread so the player who called start can interact
                else new Thread(() -> {
                        game(player1, player2); 
                    }).start();
                break;
            case "NAME": // NAME <playername>
                if(gameStarted) return;
                if(player1.getIP().equals(ip) && player1.getPort() == port) player1.setName(msg.split(" ")[1]);
                else if(player2.getIP().equals(ip) && player2.getPort() == port) player2.setName(msg.split(" ")[1]);
                break;
            case "SUBMIT": // SUBMIT <string>
                submittedString = args[0];
                break;
            case "RESET": // RESET
                reset();
                break;
            default:
                send(ip, port, "400 Bad request");
        }
    }

    /**
     * While no one has won, calls the move() method with alternating first player.
     * Also sets results after the game has ended.
     * 
     * @param p1    The first player
     * @param p2    The second player
     */
    public void game(Player p1, Player p2) {
        gameStarted = true;
        boolean player1isThrowingPlayer = true;
        sendToAll("101 Game Started");
        
        // while no one has won
        while(player1.getScore() < 100.0 && player2.getScore() < 100.0) {
            if(player1isThrowingPlayer) move(p1, p2);
            else move(p2, p1);
            
            player1isThrowingPlayer = !player1isThrowingPlayer; // swap throwing player
            
            // sendToAll(p1.getName() + " score: " + p1.getScore()); // DEBUG
            // sendToAll(p2.getName() + " score: " + p2.getScore()); // DEBUG
        }
        
        if(p1.getScore() > p2.getScore()) {
            p1.hasWon(true);
            sendToAll("104 " + p1.getName() + " Winner");
        }
        else {
            p2.hasWon(true);
            sendToAll("104 " + p2.getName() + " Winner");
        }
        
        gameStarted = false;
    }
    
    /**
     * The method for each round.
     * Gives p1 a single letter prompt, which they have to submit within TIME_TO_SUBMIT 
     * milliseconds. If this is the case, the other player will get a prompt to submit
     * multiple letters while the first player is gaining points. The longer the other
     * player takes to submit the string, the more points p1 will get.
     * 
     * @param p1    The first player
     * @param p2    The second player
     */
    public void move(Player p1, Player p2) {
        char keyToPress = keys[(int)(Math.random() * keys.length)];
        p1.send("102 " + keyToPress);
        
        boolean hasHit = false;
        long submitTime = System.currentTimeMillis() + TIME_TO_SUBMIT;
        while(System.currentTimeMillis() < submitTime) {
            if(submittedString.toUpperCase().equals(keyToPress+"")) {
                hasHit = true;
                submittedString = "";
                p1.send("204 Correct input");
                break;
            }
            sleep(100);
        }
        
        if(hasHit) {
            sendToAll("203 Target hit");
            
            // generate random sequence of characters
            String charSequence = "";
            for (int i = 0; i < (int)(Math.random() * MAX_CHARS + MIN_CHARS); i++)
                charSequence += keys[(int)(Math.random() * keys.length)];
                
            p2.send("102 " + charSequence);
            // until p1 has won
            while(p1.getScore() < 100.0) {
                // when correct input was provided
                if(submittedString.toUpperCase().equals(charSequence)) {
                    p2.send("204 Correct input");
                    break;
                }

                // System.out.println(submittedString); // DEBUG
                p1.addScore(SCORE_TO_ADD);
                // System.out.println("drinking dude score: " + p1.getScore()); // DEBUG
                sleep(100);
            }
            submittedString = "";
        }
    }
    
    /**
     * Resets attributes of both players
     */
    public void reset() {
        player1.setName("Player");
        player2.setName("Player");
        player1.setScore(0);
        player2.setScore(0);
        player1.hasWon(false);
        player2.hasWon(false);
    }
    
    /**
     * Waits for a given amount of milliseconds
     * 
     * @param millis    the milliseconds that should be waited
     */
    public void sleep(int millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch(java.lang.InterruptedException e) {}
    }
}
