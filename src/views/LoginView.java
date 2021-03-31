package views;



import config.Env;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoginView extends JFrame {

    JFrame frame;
    JMenuBar menuBar;
    JMenu helpMenu;
    JMenuItem menuItemRegister;
    JMenuItem menuItemAnonymous;
    JTextField txtUsername;
    JPasswordField txtPassword;
    JLabel labelUsername;
    JLabel labelPassword;
    JButton btnLogin;
    JPanel mainPanel;
    private Font mainFont;
    int fontSize = 18;

    public LoginView(){
        initComponents();
        setFonts();
        initImages();
        setLocation();
        setToolTips();
        initKeystrokes();
        addComponents();
        Dimension res = new Dimension(1200, 800);
        frame.setPreferredSize(res);
        frame.setSize(res);
        frame.setContentPane(mainPanel);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
    }

    /**
     * Initializes Images
     */
    private void initImages() {
        try {
            BufferedImage logo = ImageIO.read(
                    getClass().getResource("../views/img/logo.png"));
            frame.setIconImage(logo);
            BufferedImage resizedLogo = resize(logo, 40, 40);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Custom resize bufferedimage solution
     * @param image
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }

    void initKeystrokes(){
        menuItemRegister.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
    }

    void setFonts(){
        frame.setFont(mainFont);
        menuBar.setFont(mainFont);
        helpMenu.setFont(mainFont);
        menuItemRegister.setFont(mainFont);
        menuItemAnonymous.setFont(mainFont);
        txtUsername.setFont(mainFont);
        txtPassword.setFont(mainFont);
        labelUsername.setFont(mainFont);
        labelPassword.setFont(mainFont);
        btnLogin.setFont(mainFont);
    }

    void setLocation(){
        txtUsername.setLocation(130, 10);
        txtUsername.setSize(300, 30);
        txtPassword.setLocation(130, 40);
        txtPassword.setSize(300, 30);
        labelUsername.setLocation(10, 10);
        labelUsername.setSize(200, 30);
        labelPassword.setLocation(10, 40);
        labelPassword.setSize(200, 30);
        btnLogin.setLocation(10, 70);
        btnLogin.setSize(200, 30);
    }

    void setToolTips(){
        txtUsername.setToolTipText("Enter username here");
        txtPassword.setToolTipText("Enter password here");
    }

    void initComponents(){
        frame = new JFrame("Login");
        mainPanel = new JPanel();
        menuBar = new JMenuBar();
        helpMenu = new JMenu("Help");
        mainFont = new Font("Verdana", Font.BOLD, fontSize);
        menuItemRegister = new JMenuItem("Register");
        menuItemAnonymous = new JMenuItem("Login as Anonymous");
        txtPassword = new JPasswordField();
        txtUsername = new JTextField();
        labelUsername = new JLabel("Username: ");
        labelPassword = new JLabel("Password: ");
        btnLogin = new JButton("Login");
    }

    void addComponents() {
        helpMenu.add(menuItemRegister);
        helpMenu.add(menuItemAnonymous);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);
        mainPanel.setLayout(null);
        txtPassword.setEchoChar('*');
        mainPanel.add(txtUsername);
        mainPanel.add(txtPassword);
        mainPanel.add(labelUsername);
        mainPanel.add(labelPassword);
        mainPanel.add(btnLogin);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public JMenu getHelpMenu() {
        return helpMenu;
    }

    public void setHelpMenu(JMenu helpMenu) {
        this.helpMenu = helpMenu;
    }

    public JMenuItem getMenuItemRegister() {
        return menuItemRegister;
    }

    public void setMenuItemRegister(JMenuItem menuItemRegister) {
        this.menuItemRegister = menuItemRegister;
    }

    public JMenuItem getMenuItemAnonymous() {
        return menuItemAnonymous;
    }

    public void setMenuItemAnonymous(JMenuItem menuItemAnonymous) {
        this.menuItemAnonymous = menuItemAnonymous;
    }

    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public void setTxtUsername(JTextField txtUsername) {
        this.txtUsername = txtUsername;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public void setTxtPassword(JPasswordField txtPassword) {
        this.txtPassword = txtPassword;
    }

    public JLabel getLabelUsername() {
        return labelUsername;
    }

    public void setLabelUsername(JLabel labelUsername) {
        this.labelUsername = labelUsername;
    }

    public JLabel getLabelPassword() {
        return labelPassword;
    }

    public void setLabelPassword(JLabel labelPassword) {
        this.labelPassword = labelPassword;
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public void setBtnLogin(JButton btnLogin) {
        this.btnLogin = btnLogin;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public Font getMainFont() {
        return mainFont;
    }

    public void setMainFont(Font mainFont) {
        this.mainFont = mainFont;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void addListeners(ActionListener listener){
        btnLogin.addActionListener(listener);
        menuItemAnonymous.addActionListener(listener);
        menuItemRegister.addActionListener(listener);
    }

    public void addKeyListeners(KeyListener username, KeyListener password){
        txtUsername.addKeyListener(username);
        txtPassword.addKeyListener(password);
    }

    public void addFrameWindowListener(WindowListener listener){
        frame.addWindowListener(listener);
    }

    public void displayErrorMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, Env.LoginMessageBoxTitle, JOptionPane.ERROR_MESSAGE);
    }
}

