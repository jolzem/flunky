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
    private JLabel lP1Name = new JLabel();
    private JLabel lP2Name = new JLabel();
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
    private boolean isPickingUp = false;
    private int p1scoreY = 56;
    private int p1scoreHeight = 300;
    private int p2scoreY = 56;
    private int p2scoreHeight = 300;
    private double score = 0.0;
    private double enemyScore = 0.0;
    private boolean gameOngoing = false;
    private String p1name;
    private String p2name;

    /**
     * Constructor for objects of class FlunkyGUI
     */
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
        tfName.setText("test"); // DEBUG
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
        lImageField.setBounds(45, 56, 680, 360);
        lImageField.setText("");
        lImageField.setVisible(false);
        cp.add(lImageField);
        p1score.setBounds(8, p1scoreY, 24, p1scoreHeight);
        p1score.setBackground(Color.GREEN);
        p1score.setVisible(false);
        cp.add(p1score);
        lP1Name.setBounds(8, 420, 100, 14);
        lP1Name.setVisible(true);
        cp.add(lP1Name);
        p2score.setBounds(740, p2scoreY, 24, p2scoreHeight);
        p2score.setBackground(Color.RED);
        p2score.setVisible(false);
        cp.add(p2score);
        lP2Name.setBounds(740, 420, 100, 14);
        lP2Name.setVisible(true);
        cp.add(lP2Name);
        tfInput.setBounds(176, 504, 296, 32);
        tfInput.setVisible(false);
        tfInput.addKeyListener(this);
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
        lInfo.setFont(new Font("Dialog", Font.BOLD, 16));
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
        lP1Name.setVisible(true);
        lP2Name.setVisible(true);
        
        sleep(10);
        client.send("NAME " + tfName.getText());
        p1name = tfName.getText();
        lP1Name.setText(p1name);
        
        lImageField.setIcon(new ImageIcon("img/connect.png"));
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
        p1score.setVisible(false);
        p2score.setVisible(false);
        lP1Name.setVisible(false);
        lP2Name.setVisible(false);
        
        client.close(); // close connection
        client = null;
    }
    
    /**
     * When the user presses the start button, the verb START is sent to the server and
     * the focus switches to tfInput
     */
    public void bStart_ActionPerformed(ActionEvent evt) {
        if(connected()) client.send("START");
        tfInput.requestFocusInWindow(); // focus on tfInput
    }

    /**
     * When the user presses the submit button, the text from tfInput gets submitted
     * to the server as long as it's not empty by using the SUBMIT verb.
     * Afterwards tfInput is cleared.
     */
    public void bSubmit1_ActionPerformed(ActionEvent evt) {
        if(tfInput.getText().equals("") || !connected()) return;
        client.send("SUBMIT " + tfInput.getText());
        // System.out.println("SUBMIT " + tfInput.getText()); // DEBUG
        tfInput.setText("");
    }
    
    /**
     * If user presses Enter, submit.
     */
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) bSubmit1_ActionPerformed(null);
    }
    
    public void keyPressed(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    
    /**
     * This method is called as soon as the game starts. It handles the scorebars on the
     * sides by continously asking for the score of the currently scoring player and refreshing
     * the position and height of the bar.
     */
    public void refreshScore() {
        p1score.setVisible(true);
        p2score.setVisible(true);
        client.send("INFO NAME"); // ask for enemy player name
        // System.out.println("INFO NAME"); // DEBUG
        while(gameOngoing && connected()) {
            if(isPickingUp) {
                // refresh p2 score
                client.askForP1(false);
                client.send("INFO SCORE " + p2name);
                // System.out.println("INFO SCORE " + p2name); // DEBUG

                p2scoreY = (int)(enemyScore * 3 + 56);
                p2scoreHeight = (int)((enemyScore - 100) * -3.0);
                p2score.setBounds(750, p2scoreY, 24, p2scoreHeight);
            } else {
                // refresh p1 score (own score)
                client.askForP1(true);
                client.send("INFO SCORE " + p1name);
                // System.out.println("INFO SCORE " + p1name); // DEBUG

                p1scoreY = (int)(score * 3 + 56);
                p1scoreHeight = (int)((score - 100) * -3.0);
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

    /**
     * Set the score of the local player
     *
     * @param score The score to be set.
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Sets the score of the enemy player.
     *
     * @param score The score to be set.
     */
    public void setEnemyScore(double score) {
        this.enemyScore = score;
    }

    /**
     * Sets the name of the enemy player.
     *
     * @param name  The name to be set.
     */
    public void setP2Name(String name) {
        this.p2name = name;
        lP2Name.setText(name);
    }

    /**
     * Sets if the game is still going on. It also enables/disables the start button accordingly
     *
     * @param value If the game is still going on
     */
    public void gameOngoing(boolean value) {
        this.gameOngoing = value;
        bStart.setEnabled(!value);
    }

    /**
     * Sets if the local player is currently picking up the fallen target.
     *
     * @param value The value to be set
     */
    public void isPickingUp(boolean value) {
        this.isPickingUp = value;
    }

    /**
     * Returns true if the client can be accessed to avoid NullPointerExceptions
     *
     * @return true if the client can be accessed, else false
     */
    public boolean connected() {
        return client != null;
    }
    
    /**
     * Changes the icon of lImageField to the supplied image in the img/ directory
     * 
     * @param imgName   the new image
     */
    public void setImage(String imgName) {
        lImageField.setIcon(new ImageIcon("img/" + imgName));
    }
}
