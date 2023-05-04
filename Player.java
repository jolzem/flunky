
/**
 * Beschreiben Sie hier die Klasse Player.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Player
{
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen
    private String ip;
    private int port;
    private String name;
    private FlunkyServer server;

    /**
     * Konstruktor für Objekte der Klasse Player
     */
    public Player(String pIP, int pPort, FlunkyServer pServer) {
        ip = pIP;
        port = pPort;
        server = pServer;
        name = "Player" + Math.random()*1000;
    }

    /**
     * Gibt den Port des Spielers
     * 
     * @return  die variable port
     */
    public int getPort() {
        return port;
    }
    
    /**
     * Gibt die IP Adresse des Spielers
     * 
     * @return  die variable IP
     */
    public String getIP() {
        return ip;
    }
    
    /**
     * Sendet die gegebene Nachricht an den Spieler
     * 
     * @param msg   Die Nachricht, die an den Spieler gesendet werden soll
     */
    public void send(String msg) {
        server.send(ip, port, msg);
    }
    
    public void setName(String pName) {
        name = pName;
    }
    
    public String getName() {
        return name;
    }
}
