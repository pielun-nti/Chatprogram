# Chatprogram
Jag har kommenterat de mesta klassernas metoder i koden, men jag hann inte göra för allt,
men de som jag inte hann är lika de som jag använt förut och kommenterat då.

JavaDocs comments - https://pielun-nti.github.io/Chatprogram/index.html

Servern är multithreadad alltså stödjer flera klienter
samtidigt och klienter kan skriva till alla eller bara till en specific klient med mera.
All nätverkstrafik via servern och klienterna är helt krypterad med ett jks certifikat/keystore och SSLSocket som jag använt.
Använt MVC och OOP. Gränsnittet är JFrame guis för både server och klient som jag skrivit själv,
men det finns även test program som jag skrev för att testa en del av serverns funktionalitet, och som har terminal gränssnitt.
Programmet fungerar bra, men kan utvecklats mycket om jag bara lade mer tid på det.