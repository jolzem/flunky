import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.concurrent.TimeUnit;

/**
 * This is a GUI for the Flunky Server. It presents a graphical login and a graphical
 * representation of the game.
 *
 * @version 0.1
 * @author Johannes Olzem
 */

public class FlunkyGUI extends JFrame implements KeyListener {
    // GUI attributes
    private JLabel lFlunky1 = new JLabel();
    private JTextField tfIP = new JTextField();
    private JTextField tfName = new JTextField();
    private JLabel lIP = new JLabel();
    private JLabel lName = new JLabel();
    private JButton bConnect1 = new JButton();
    private JButton bStart = new JButton();
    private JButton bDisconnect1 = new JButton();
    private JLabel lImageField = new JLabel();
    private JPanel p1score = new JPanel();
    private JPanel p2score = new JPanel();
    private JTextField tfInput = new JTextField();
    private JButton bSubmit1 = new JButton();
    public JLabel lInfo = new JLabel();

    // Program attributes
    private FlunkyClient client;
    private boolean atLogin = true;
    private boolean isPickingUp = false;
    private int p1scoreY = 100;
    private int p1scoreHeight;
    private int p2scoreY = 100;
    private int p2scoreHeight;
    private double score = 0.0;
    private double enemyScore = 0.0;
    private boolean gameOngoing = false;

    public FlunkyGUI() {
        // frame init
        super();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        int frameWidth = 800; 
        int frameHeight = 600;
        setSize(frameWidth, frameHeight);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;
        setLocation(x, y);
        setTitle("Flunky");
        setResizable(false);
        Container cp = getContentPane();
        cp.setLayout(null);

        // GUI components
        lFlunky1.setBounds(350, 8, 78, 40);
        lFlunky1.setText("Flunky");
        lFlunky1.setFont(new Font("Dialog", Font.BOLD, 24));
        cp.add(lFlunky1);
        tfIP.setBounds(353, 216, 128, 24);
        tfIP.setText("192.168.178.85"); // DEBUG
        cp.add(tfIP);
        lIP.setBounds(320, 216, 40, 24);
        lIP.setText("IP");
        cp.add(lIP);
        tfName.setBounds(353, 248, 128, 24);
        cp.add(tfName);
        lName.setBounds(310, 248, 40 ,24);
        lName.setText("Name");
        cp.add(lName);
        bConnect1.setBounds(352, 280, 80, 24);
        bConnect1.setText("Connect");
        bConnect1.setMargin(new Insets(2, 2, 2, 2));
        bConnect1.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent evt) { 
                    bConnect1_ActionPerformed(evt);
                }
            });
        cp.add(bConnect1);
        bDisconnect1.setBounds(650, 12, 115, 32);
        bDisconnect1.setText("Disconnect");
        bDisconnect1.setMargin(new Insets(2, 2, 2, 2));
        bDisconnect1.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent evt) { 
                    bDisconnect1_ActionPerformed(evt);
                }
            });
        bDisconnect1.setVisible(false);
        cp.add(bDisconnect1);
        bStart.setBounds(8, 12, 115, 32);
        bStart.setText("Start");
        bStart.setMargin(new Insets(2, 2, 2, 2));
        bStart.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent evt) { 
                    bStart_ActionPerformed(evt);
                }
            });
        bStart.setVisible(false);
        cp.add(bStart);
        lImageField.setBounds(8, 56, 768, 360);
        lImageField.setText("");
        lImageField.setVisible(false);
        cp.add(lImageField);
        p1score.setBounds(8, p1scoreY, 24, p1scoreHeight);
        p1score.setBackground(Color.BLACK);
        p1score.setVisible(false);
        cp.add(p1score);
        p2score.setBounds(768, p2scoreY, 24, p2scoreHeight);
        p2score.setBackground(Color.BLACK);
        p2score.setVisible(false);
        cp.add(p1score);
        tfInput.setBounds(176, 504, 296, 32);
        tfInput.setVisible(false);
        cp.add(tfInput);
        bSubmit1.setBounds(488, 504, 152, 32);
        bSubmit1.setText("Submit");
        bSubmit1.setMargin(new Insets(2, 2, 2, 2));
        bSubmit1.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent evt) { 
                    bSubmit1_ActionPerformed(evt);
                }
            });
        bSubmit1.setVisible(false);
        cp.add(bSubmit1);
        lInfo.setBounds(168, 424, 464, 40);
        lInfo.setText("");
        lInfo.setVisible(false);
        lInfo.setHorizontalAlignment(JTextField.CENTER); // align center
        cp.add(lInfo);

        setVisible(true);
    }

    public static void main(String[] args) {
        new FlunkyGUI();
    }
    
    /**
     * When the user presses the connect button, the contents of tfIP get checked if they
     * match the pattern of an ip address and tfName gets checked if it isn't empty.
     * Next, if the connection is established the login elements are hidden and the game 
     * elements become visible. Finally the user gets logged in with the provided name.
     */
    public void bConnect1_ActionPerformed(ActionEvent evt) {
        // try to connect if text in tfIP has format of string
        if(tfIP.getText().matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$") && !tfName.getText().equals("")) client = new FlunkyClient(tfIP.getText(), this);
        else return;
        
        // check if connection succeeded
        if(!client.isConnected()) return;
        
        bConnect1.setVisible(false);
        tfIP.setText("");
        tfIP.setVisible(false);
        lIP.setVisible(false);
        tfName.setVisible(false);
        lName.setVisible(false);
        bDisconnect1.setVisible(true);
        bStart.setVisible(true);
        lImageField.setVisible(true);
        tfInput.setVisible(true);
        bSubmit1.setVisible(true);
        lInfo.setVisible(true);
        
        atLogin = false;
        
        sleep(10);
        client.send("NAME " + tfName.getText());
    }

    /**
     * When the user presses the disconnect button, the game elements are hidden and
     * the login elements become visible again. It also closes the connection and
     * removes the client.
     */
    public void bDisconnect1_ActionPerformed(ActionEvent evt) {
        bConnect1.setVisible(true);
        tfIP.setVisible(true);
        lIP.setVisible(true);
        tfName.setVisible(true);
        lName.setVisible(true);
        bDisconnect1.setVisible(false);
        bStart.setVisible(false);
        lImageField.setVisible(false);
        tfInput.setVisible(false);
        bSubmit1.setVisible(false);
        lInfo.setVisible(false);
        
        atLogin = true;
        
        client.close(); // close connection
        client = null;
    }
    
    /**
     * When the user presses the start button, the verb START is sent to the server and
     * the focus switches to tfInput
     */
    public void bStart_ActionPerformed(ActionEvent evt) {
        client.send("START");
        tfInput.requestFocusInWindow(); // focus on tfInput
    }

    /**
     * When the user presses the submit button, the text from tfInput gets submitted
     * to the server as long as it's not empty by using the SUBMIT verb.
     * Afterwards tfInput is cleared.
     */
    public void bSubmit1_ActionPerformed(ActionEvent evt) {
        if(tfInput.getText().equals("")) return;
        client.send("SUBMIT " + tfInput.getText());
        tfInput.setText("");
    }
    
    /**
     * If user presses Enter, connect when on login and submit when in game.
     */
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(atLogin) bConnect1_ActionPerformed(null);
            else bSubmit1_ActionPerformed(null);
        }
    }
    
    public void keyPressed(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    
    public void refreshScore() {
        p1score.setVisible(true);
        p2score.setVisible(true);
        while(gameOngoing) {
            if(isPickingUp) {
                p2scoreY = (int)(enemyScore * 3 + 56.0);
                p2scoreHeight = (int)(enemyScore * 3.0);
                p2score.setBounds(768, p2scoreY, 24, p2scoreHeight);
            } else {
                p1scoreY = (int)(score * 3 + 56.0);
                p1scoreHeight = (int)(score * 3.0);
                p1score.setBounds(8, p1scoreY, 24, p1scoreHeight);
            }
            sleep(70);
        }
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
    
    public void setScore(double score) {
        this.score = score;
    }
    
    public void setEnemyScore(double score) {
        this.enemyScore = score;
    }
    
    public void gameOngoing(boolean value) {
        this.gameOngoing = value;
    }
}
