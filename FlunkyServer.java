
/**
 * Beschreiben Sie hier die Klasse FlunkyServer.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class FlunkyServer extends Server {
    private Player player1;
    private Player player2;
    private char[] keys = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private boolean gameStarted = false;
    
    /**
     * Konstruktor für Objekte der Klasse FlunkyServer
     */
    public FlunkyServer() {
        super(7);
    }

    /**
     * Ein Beispiel einer Methode - ersetzen Sie diesen Kommentar mit Ihrem eigenen
     * 
     * @param  y    ein Beispielparameter für eine Methode
     * @return        die Summe aus x und y
     */
    public void beispielMethode(int y) {
        
    }
    
    /**
     * Fragt ab, ob noch ein player benoetigt wird, und 
     * 
     * @param ip    Die IP-Adresse des Senders
     * @param port  Der Port des Senders 
     */
    
    public void processNewConnection(String ip, int port) {
        send(ip, port, "Helo");
        if(player1 == null) player1 = new Player(ip, port, this);
        else if(player2 == null) player2 = new Player(ip, port, this);
        else send(ip, port, "401 Session schon voll");
    }
    
    /**
     * Entfernt den verlassenden von den Spielern
     * 
     * @param ip    Die IP-Adresse des Senders
     * @param port  Der Port des Senders 
     */
    public void processClosingConnection(String ip, int port) {
        if(player1.getIP().equals(ip) && player1.getPort() == port) player1 = null;
        else if(player2.getIP().equals(ip) && player2.getPort() == port) player2 = null;
    }
    
    public void processOpeningConnection(String ip, int port) {}
    
    /**
     * Verarbeitet Nachrichten, welche an den Server gesendet werden.
     * 
     * @param ip    Die IP-Adresse des Senders
     * @param port  Der Port des Senders
     * @param msg   Die Nachricht des Senders
     */
    public void processMessage(String ip, int port, String msg) {
        String verb = msg.split(" ")[0];
        
        switch(verb) {
            case "START":
                if(player1 == null || player2 == null) {
                    send(ip, port, "402 Nicht genug Spieler");
                    return;
                }
                startGame(player1, player2);
                break;
            case "NAME":
                if(player1.getIP().equals(ip) && player1.getPort() == port) player1.setName(msg.split(" ")[1]);
                else if(player2.getIP().equals(ip) && player2.getPort() == port) player2.setName(msg.split(" ")[1]);
            default:
                send(ip, port, "400 Verb nicht gefunden.");
        }
    }
    
    /**
     * 
     */
    public void startGame(Player p1, Player p2) {
        gameStarted = true;
        p1.send(p1.getName());
        p2.send(p2.getName());
    }
    
    
    
}
