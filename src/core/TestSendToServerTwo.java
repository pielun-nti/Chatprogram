package core;

import config.Env;
import models.ChatClientMainReceiver;
import models.PassUtil;

import javax.net.ssl.*;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;

public class TestSendToServerTwo {
    public static void main(String[] args){
        System.setProperty("javax.net.ssl.keyStore", Env.SSLKeyStore);
        System.setProperty("javax.net.ssl.keyStorePassword", Env.SSLKeyStorePass);
        try {
            final char[] password = Env.SSLKeyStorePass.toCharArray();

            final KeyStore keyStore = KeyStore.getInstance(new File(Env.SSLKeyStore), password);

            final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("NewSunX509");
            keyManagerFactory.init(keyStore, password);

            final SSLContext context = SSLContext.getInstance("TLS");//"SSL" "TLS"
            context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            final SSLSocketFactory factory = context.getSocketFactory();

            SSLSocket socket = ((SSLSocket) factory.createSocket("127.0.0.1", 5900));

            ChatClientMainReceiver chatClientMainReceiver = new ChatClientMainReceiver(socket);
            chatClientMainReceiver.start();
        PassUtil passUtil = new PassUtil();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        writer.println(passUtil.toHexString("msg|split|annan|split|bay"));
        writer.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
