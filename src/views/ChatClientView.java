package views;

import config.Env;
import models.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * ChatClientView class that extends JFrame.
 */
public class ChatClientView extends JFrame {
    private JPanel mainPanel;
    private JMenu menuOptions;
    private JMenu menuSettings;
    private JMenu menuPreferences;
    private JMenu menuAbout;
    private JMenuBar mainMenuBar;
    private JMenuItem itemExitProgram;
    private JMenuItem itemSendMsgSpecificClient;
    private JMenuItem itemClearChatMessages;
    private JMenuItem itemAbout;
    public JTextPane txtLog;
    private JScrollPane logjsp;
    private Font myFont;
    private Font itemFont;
    private Font menuFont;
    private final int WIDTH = 1200;
    private final int HEIGHT = 800;
    private int fontSize = 14;
    User user;
    boolean DontUseTextColors;
    private JTextField txtMessage;
    private JTextField txtServerPort;
    private JTextField txtServerIP;
    private JLabel labelServerIP;
    private JLabel labelServerPort;
    private JLabel labelMessage;
    private JButton btnSendMessage;
    private JButton btnConnectToServer;
    private JButton btnDisconnectFromServer;
    private JComboBox emojiSelector;
    private JButton btnSendImageToAll;
    BufferedImage logo;
    BufferedImage resizedLogo;

    /**
     * ChatClientView constructor
     * @param user The user
     */
    public ChatClientView(User user){
        this.user = user;
        initComponents();
        myFont = new Font("Verdana", Font.PLAIN, 13);
        menuFont = new Font("Verdana", Font.BOLD, 20);
        itemFont = new Font("Verdana", Font.BOLD, 16);
        setStyles();
        addComponents();
        changeAllFont(mainPanel, myFont);
        initKeystrokes();
        initImages();
        initKeyListeners();
        Dimension dim = new Dimension(WIDTH, HEIGHT);
        setJMenuBar(mainMenuBar);
        setSize(dim);
        setPreferredSize(dim);
        setContentPane(mainPanel);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Env.ChatClientMessageBoxTitle + " - logged in as: " + user.getUsername());
        setLocationRelativeTo(null);
        pack();
    }

    /**
     * Initializes Images
     */
    private void initImages() {
        try {
            logo = ImageIO.read(
                    getClass().getResource("./img/logo.png"));
            setIconImage(logo);
            resizedLogo = resize(logo, 40, 40);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes all components in the JFrame
     */
    private void initComponents() {
        mainPanel = new JPanel(null);
        mainMenuBar = new JMenuBar();
        emojiSelector = new JComboBox<Character>();
        itemExitProgram = new JMenuItem("Exit program");
        itemSendMsgSpecificClient = new JMenuItem("Send Message To Specific Client");
        itemClearChatMessages = new JMenuItem("Clear Chat Messages");
        txtLog= new JTextPane();
        logjsp = new JScrollPane(txtLog, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);        itemAbout = new JMenuItem("About this program");
        menuOptions = new JMenu("Options");
        menuSettings = new JMenu("Settings");
        menuPreferences = new JMenu("Preferences");
        txtServerIP = new JTextField();
        txtServerPort = new JTextField();
        txtMessage = new JTextField();
        labelServerIP = new JLabel("Server IP:");
        labelServerPort = new JLabel("Server Port:");
        labelMessage = new JLabel("Message:");
        btnSendMessage = new JButton("Send Message To All");
        btnConnectToServer = new JButton("Connect To Server");
        btnDisconnectFromServer = new JButton("Disconnect From Server");
        btnSendImageToAll = new JButton("Send Image To All");
        menuAbout = new JMenu("About");
    }

    void setStyles(){
        itemExitProgram.setFont(itemFont);
        txtLog.setEditable(false);
        itemSendMsgSpecificClient.setFont(itemFont);
        itemSendMsgSpecificClient.setToolTipText("Click here to send message to specific client");
        itemClearChatMessages.setFont(itemFont);
        itemClearChatMessages.setToolTipText("Click here to clear all chat messages from the textarea");
        itemAbout.setFont(itemFont);
        menuOptions.setFont(menuFont);
        menuSettings.setFont(menuFont);
        menuPreferences.setFont(menuFont);
        menuAbout.setFont(menuFont);
        txtServerIP.setSelectedTextColor(Color.RED);
        txtServerPort.setSelectedTextColor(Color.RED);
        txtMessage.setSelectedTextColor(Color.RED);
        txtServerIP.setToolTipText("Enter server ip address here");
        txtServerPort.setToolTipText("Enter server port here");
        txtMessage.setToolTipText("Enter message here");
        btnSendMessage.setToolTipText("Click here to send message");
        btnConnectToServer.setToolTipText("Click here to connect to server");
        btnDisconnectFromServer.setToolTipText("Click here to disconnect from server");
        btnSendImageToAll.setToolTipText("Click here to send image to everyone");
        emojiSelector.setFont(myFont);
        labelServerIP.setLocation(10, 520 + 60);
        labelServerIP.setSize(100, 100);
        txtServerIP.setLocation(80, 560 + 60);
        txtServerIP.setSize(200, 20);
        labelServerPort.setLocation(290, 520 + 60);
        labelServerPort.setSize(100, 100);
        txtServerPort.setLocation(370, 560 + 60);
        txtServerPort.setSize(200, 20);
        labelMessage.setLocation(10, 570 + 60);
        labelMessage.setSize(100, 60);
        txtMessage.setLocation(80, 590 + 60);
        txtMessage.setSize(200, 20);
        btnSendMessage.setLocation(5, 620 + 60);
        btnSendMessage.setSize(300, 25);
        btnConnectToServer.setLocation(310, 620 + 60);
        btnConnectToServer.setSize(400, 25);
        btnDisconnectFromServer.setLocation(720, 620 + 60);
        btnDisconnectFromServer.setSize(400, 25);
        logjsp.setLocation(0, 0);
        logjsp.setSize(1180, 600);
        emojiSelector.setSize(100, 25);
        emojiSelector.setLocation(300, 590 + 60);
        btnSendImageToAll.setSize(300, 25);
        btnSendImageToAll.setLocation(300, 590 + 60);
        setFont(myFont);
        changeAllFont(mainPanel, myFont);
        changeAllButtonFont(mainPanel, myFont);
    }

    /**
     * Add item listeners.
     * @param itemListener Listening for item change events.
     */
    public void addItemListeners(ItemListener itemListener){
        emojiSelector.addItemListener(itemListener);
    }

    private void addComponents() {
        menuOptions.add(itemExitProgram);
        menuOptions.add(itemSendMsgSpecificClient);
        menuOptions.add(itemClearChatMessages);
        menuAbout.add(itemAbout);
        mainMenuBar.add(menuOptions);
        mainMenuBar.add(menuSettings);
        mainMenuBar.add(menuPreferences);
        mainMenuBar.add(menuAbout);
        mainPanel.add(labelServerIP);
        mainPanel.add(txtServerIP);
        mainPanel.add(labelServerPort);
        mainPanel.add(txtServerPort);
        mainPanel.add(labelMessage);
        mainPanel.add(txtMessage);
        mainPanel.add(btnSendMessage);
        mainPanel.add(btnConnectToServer);
        mainPanel.add(btnDisconnectFromServer);
        mainPanel.add(btnSendImageToAll);
        mainPanel.add(logjsp);
        //mainPanel.add(emojiSelector);
    }

    public void addListeners(ActionListener listener){
        itemAbout.addActionListener(listener);
        itemExitProgram.addActionListener(listener);
        itemSendMsgSpecificClient.addActionListener(listener);
        itemClearChatMessages.addActionListener(listener);
        btnSendMessage.addActionListener(listener);
        btnConnectToServer.addActionListener(listener);
        btnDisconnectFromServer.addActionListener(listener);
        btnSendImageToAll.addActionListener(listener);
    }

    public void addFrameWindowListener(WindowListener listener){
        addWindowListener(listener);
    }


    public void displayErrorMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, Env.ChatClientMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
    }


    /**
     * Changes all JButton fonts
     */
    private void changeAllButtonFont(Container c, Font f) {
        for (Component comp : c.getComponents()) {
            if (comp instanceof JButton) {
                ((JButton)comp).setFont(f);
            }
        }
    }

    void initKeyListeners(){
        txtMessage.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnSendMessage.doClick();
                }
            }
        });
        txtServerIP.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtServerPort.requestFocus();
                }
            }
        });
        txtServerPort.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtMessage.requestFocus();
                }
            }
        });
    }

    /**
     * Initializes Keystrokes
     */
    private void initKeystrokes() {
        itemSendMsgSpecificClient.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        itemExitProgram.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_0, java.awt.event.InputEvent.CTRL_MASK));
    }

    /**
     * Custom resize bufferedimage solution
     * @param image
     * @param width
     * @param height
     * @return
     */
    public BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }

    /**
     * Appends text to a JTextPane with color, font, styling and different content types support
     */
    public void appendToPane(JTextPane tp, String msg, String c, String imgpath)
    {
        if (!DontUseTextColors) {
            //StyleContext sc = StyleContext.getDefaultStyleContext();
            tp.setContentType("text/html");
            HTMLDocument doc = (HTMLDocument)tp.getDocument();
            HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
            SimpleAttributeSet attributeSet = new SimpleAttributeSet();
            StyleConstants.setItalic(attributeSet, true);
            tp.setCharacterAttributes(attributeSet, true);
            StyledDocument styledoc = (StyledDocument) tp.getDocument();
            Style style = styledoc.addStyle("StyleName", null);

            if (tp.getText().equals("")) {
                try {
                    if (msg != null) {
                        msg = msg.replace("\n", "<br>");
                        editorKit.insertHTML(doc, doc.getLength(), "<p style=margin:0;padding:0;color:" + c + ";font-size:" + fontSize + ";>" + msg + "</p>", 0, 0, null);
                    }
                    if (imgpath != null) {
                        StyleConstants.setIcon(style, new ImageIcon(imgpath));
                        styledoc.insertString(styledoc.getLength(), "inv", style);
                    }
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else {
                try {
                    if (msg != null) {
                        msg = msg.replace("\n", "<br>");
                        editorKit.insertHTML(doc, doc.getLength(), "<p style=margin:0;padding:0;color:" + c + ";font-size:" + fontSize + ";>" + msg + "</p>", 0, 0, null);
                    }
                    if (imgpath != null) {
                        StyleConstants.setIcon(style, new ImageIcon(imgpath));
                        styledoc.insertString(styledoc.getLength(), "inv", style);
                    }
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        else {
            tp.setContentType("text/plain");
            if (tp.getText().equals("")) {
                tp.setText(msg);
            } else {
                tp.setText(tp.getText() + "\n" + msg);
            }
        }
    }


    /**
     * Changes all Fonts in container
     */
    public static void changeAllFont ( Component component, Font font )
    {
        component.setFont ( font );
        if ( component instanceof Container )
        {
            for ( Component child : ( ( Container ) component ).getComponents () )
            {
                changeAllFont ( child, font );
            }
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JMenu getMenuOptions() {
        return menuOptions;
    }

    public void setMenuOptions(JMenu menuOptions) {
        this.menuOptions = menuOptions;
    }

    public JMenu getMenuSettings() {
        return menuSettings;
    }

    public void setMenuSettings(JMenu menuSettings) {
        this.menuSettings = menuSettings;
    }

    public JMenu getMenuPreferences() {
        return menuPreferences;
    }

    public void setMenuPreferences(JMenu menuPreferences) {
        this.menuPreferences = menuPreferences;
    }

    public JMenu getMenuAbout() {
        return menuAbout;
    }

    public void setMenuAbout(JMenu menuAbout) {
        this.menuAbout = menuAbout;
    }

    public JMenuBar getMainMenuBar() {
        return mainMenuBar;
    }

    public JMenuItem getItemSendMsgSpecificClient() {
        return itemSendMsgSpecificClient;
    }

    public void setItemSendMsgSpecificClient(JMenuItem itemSendMsgSpecificClient) {
        this.itemSendMsgSpecificClient = itemSendMsgSpecificClient;
    }

    public JMenuItem getItemClearChatMessages() {
        return itemClearChatMessages;
    }

    public void setItemClearChatMessages(JMenuItem itemClearChatMessages) {
        this.itemClearChatMessages = itemClearChatMessages;
    }


    public JButton getBtnDisconnectFromServer() {
        return btnDisconnectFromServer;
    }

    public JButton getBtnSendImageToAll() {
        return btnSendImageToAll;
    }

    public void setBtnSendImageToAll(JButton btnSendImageToAll) {
        this.btnSendImageToAll = btnSendImageToAll;
    }

    public void setBtnDisconnectFromServer(JButton btnDisconnectFromServer) {
        this.btnDisconnectFromServer = btnDisconnectFromServer;
    }

    public JComboBox getEmojiSelector() {
        return emojiSelector;
    }

    public void setEmojiSelector(JComboBox emojiSelector) {
        this.emojiSelector = emojiSelector;
    }

    public void setMainMenuBar(JMenuBar mainMenuBar) {
        this.mainMenuBar = mainMenuBar;
    }

    public JMenuItem getItemExitProgram() {
        return itemExitProgram;
    }

    public void setItemExitProgram(JMenuItem itemExitProgram) {
        this.itemExitProgram = itemExitProgram;
    }

    public JMenuItem getitemSendMsgSpecificClient() {
        return itemSendMsgSpecificClient;
    }

    public void setitemSendMsgSpecificClient(JMenuItem itemSendMsgSpecificClient) {
        this.itemSendMsgSpecificClient = itemSendMsgSpecificClient;
    }


    public JMenuItem getItemAbout() {
        return itemAbout;
    }

    public void setItemAbout(JMenuItem itemAbout) {
        this.itemAbout = itemAbout;
    }

    public JTextPane getTxtLog() {
        return txtLog;
    }

    public void setTxtLog(JTextPane txtLog) {
        this.txtLog = txtLog;
    }

    public JScrollPane getLogjsp() {
        return logjsp;
    }

    public void setLogjsp(JScrollPane logjsp) {
        this.logjsp = logjsp;
    }

    public Font getMyFont() {
        return myFont;
    }

    public void setMyFont(Font myFont) {
        this.myFont = myFont;
    }

    public Font getItemFont() {
        return itemFont;
    }

    public void setItemFont(Font itemFont) {
        this.itemFont = itemFont;
    }

    public Font getMenuFont() {
        return menuFont;
    }

    public void setMenuFont(Font menuFont) {
        this.menuFont = menuFont;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isDontUseTextColors() {
        return DontUseTextColors;
    }

    public void setDontUseTextColors(boolean dontUseTextColors) {
        DontUseTextColors = dontUseTextColors;
    }

    public JTextField getTxtMessage() {
        return txtMessage;
    }

    public void setTxtMessage(JTextField txtMessage) {
        this.txtMessage = txtMessage;
    }

    public JTextField getTxtServerPort() {
        return txtServerPort;
    }

    public void setTxtServerPort(JTextField txtServerPort) {
        this.txtServerPort = txtServerPort;
    }

    public JTextField getTxtServerIP() {
        return txtServerIP;
    }

    public void setTxtServerIP(JTextField txtServerIP) {
        this.txtServerIP = txtServerIP;
    }

    public JLabel getLabelServerIP() {
        return labelServerIP;
    }

    public void setLabelServerIP(JLabel labelServerIP) {
        this.labelServerIP = labelServerIP;
    }

    public JLabel getLabelServerPort() {
        return labelServerPort;
    }

    public void setLabelServerPort(JLabel labelServerPort) {
        this.labelServerPort = labelServerPort;
    }

    public JLabel getLabelMessage() {
        return labelMessage;
    }

    public void setLabelMessage(JLabel labelMessage) {
        this.labelMessage = labelMessage;
    }

    public JButton getBtnSendMessage() {
        return btnSendMessage;
    }

    public void setBtnSendMessage(JButton btnSendMessage) {
        this.btnSendMessage = btnSendMessage;
    }

    public JButton getBtnConnectToServer() {
        return btnConnectToServer;
    }

    public void setBtnConnectToServer(JButton btnConnectToServer) {
        this.btnConnectToServer = btnConnectToServer;
    }
}
