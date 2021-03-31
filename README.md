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

Jag hade kunnat göra detta program mycket bättre med fler funktioner
som tex skicka filer, bilder, avatar, emojis, lägga till så servern kan skicka
meddelanden, göra så meddelanden och bilder sparas i databas och sedan skrivs de ut
till ny ansluten klient med mera. Kan även förbättra genom att
lägga till så man kan stoppa servern, disconnecta från servern, 
kicka klienter, göra ip lookups, och mycket mer men jag hade inte tid med det just denna gång. 
Jag vet hur man skulle göra allt detta med mera eftersom jag har gjort mycket bättre och avancerare nätverk program tidigare.
