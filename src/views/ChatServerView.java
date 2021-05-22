package views;

import config.Env;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ChatServerView extends JFrame {
    private JPanel mainPanel;
    private JMenu menuOptions;
    private JMenu menuSettings;
    private JMenu menuPreferences;
    private JMenu menuAbout;
    private JMenuBar mainMenuBar;
    private JMenuItem itemExitProgram;
    private JMenuItem itemSelectReceiver;
    private JMenuItem itemStartChatServer;
    private JMenuItem itemStopChatServer;
    private JMenuItem itemDisconnectClient;
    private JMenuItem itemSaveChatLog;
    private JMenuItem itemSomethinto;
    private JMenuItem itemClick;
    private JMenuItem itemSwipe;
    private JCheckBoxMenuItem itemSomethinthree;
    private JCheckBoxMenuItem itemSomethinfour;
    private JMenuItem itemAbout;
    public JTextPane txtLog;
    private JScrollPane logjsp;
    private Font myFont;
    private Font itemFont;
    private Font menuFont;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private int fontSize = 14;
    boolean DontUseTextColors = false;
    BufferedImage logo;
    BufferedImage resizedLogo;

    public ChatServerView(){
        initComponents();
        myFont = new Font("Verdana", Font.PLAIN, 13);
        menuFont = new Font("Verdana", Font.BOLD, 20);
        itemFont = new Font("Verdana", Font.BOLD, 16);
        setStyles();
        addComponents();
        changeAllFont(mainPanel, myFont);
        initKeystrokes();
        Dimension dim = new Dimension(WIDTH, HEIGHT);
        setJMenuBar(mainMenuBar);
        setSize(dim);
        setPreferredSize(dim);
        setContentPane(mainPanel);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Env.ChatServerMessageBoxTitle);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes all components in the JFrame
     */
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainMenuBar = new JMenuBar();
        itemExitProgram = new JMenuItem("Exit program");
        itemSelectReceiver = new JMenuItem("Not implemented yet");
        itemStartChatServer = new JMenuItem("Start Chat Server");
        itemStopChatServer = new JMenuItem("Stop Chat Server");
        itemDisconnectClient = new JMenuItem("Kick a client");
        itemSaveChatLog = new JMenuItem("Save Chat Log");
        itemSomethinto = new JMenuItem("See Live Connections");
        itemSomethinthree = new JCheckBoxMenuItem("Not implemented yet");
        itemSomethinfour = new JCheckBoxMenuItem("Not implemented yet");
        txtLog= new JTextPane();
        logjsp = new JScrollPane(txtLog, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);        itemAbout = new JMenuItem("About this program");
        menuOptions = new JMenu("Options");
        menuSettings = new JMenu("Settings");
        menuPreferences = new JMenu("Preferences");
        menuAbout = new JMenu("About");
    }

    void setStyles(){
        itemExitProgram.setFont(itemFont);
        txtLog.setEditable(false);
        itemSelectReceiver.setFont(itemFont);
        itemSelectReceiver.setToolTipText("Click here to select receiver for this GUI");
        itemStartChatServer.setFont(itemFont);
        itemStartChatServer.setToolTipText("Click here to start chat server");
        itemStopChatServer.setFont(itemFont);
        itemDisconnectClient.setFont(itemFont);
        itemSaveChatLog.setFont(itemFont);
        itemSomethinto.setFont(itemFont);
        itemSomethinthree.setFont(itemFont);
        itemSomethinfour.setFont(itemFont);
        itemAbout.setFont(itemFont);
        menuOptions.setFont(menuFont);
        menuSettings.setFont(menuFont);
        menuPreferences.setFont(menuFont);
        menuAbout.setFont(menuFont);
    }


    private void addComponents() {
        menuOptions.add(itemExitProgram);
        //menuOptions.add(itemSelectReceiver);
        menuOptions.add(itemStartChatServer);
        menuOptions.add(itemStopChatServer);
        menuOptions.add(itemDisconnectClient);
        menuOptions.add(itemSaveChatLog);
        menuOptions.add(itemSomethinto);
        menuOptions.add(itemSomethinthree);
        menuOptions.add(itemSomethinfour);
        menuAbout.add(itemAbout);
        mainMenuBar.add(menuOptions);
        mainMenuBar.add(menuSettings);
        mainMenuBar.add(menuPreferences);
        mainMenuBar.add(menuAbout);
        mainPanel.add(logjsp);
    }

    public void addListeners(ActionListener listener){
        itemAbout.addActionListener(listener);
        itemStartChatServer.addActionListener(listener);
        itemStopChatServer.addActionListener(listener);
        itemDisconnectClient.addActionListener(listener);
        itemSaveChatLog.addActionListener(listener);
        itemSomethinto.addActionListener(listener);
        itemSomethinthree.addActionListener(listener);
        itemSomethinfour.addActionListener(listener);
        itemExitProgram.addActionListener(listener);
        itemSelectReceiver.addActionListener(listener);
    }

    public void addFrameWindowListener(WindowListener listener){
        addWindowListener(listener);
    }


    public void displayErrorMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, Env.ChatServerMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
    }


    /**
     * Initializes Keystrokes
     */
    private void initKeystrokes() {
        itemStopChatServer.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        itemDisconnectClient.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_MASK));
        itemStartChatServer.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        itemSelectReceiver.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
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
                        styledoc.insertString(styledoc.getLength(), "", style);
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
                        styledoc.insertString(styledoc.getLength(), "", style);
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
}
