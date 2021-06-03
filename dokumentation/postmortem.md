# Chatprogram

Pierre Lundström 2021-06-03.

## Inledning

Jag har programmerat ett chattprogram. Det första som jag gjorde var att planera vad jag skulle göra och vad som var viktigast att hinna göra och hur jag ska dela upp tiden. Jag använde ett projektbräde i hela arbetet för att hålla koll på vad som redan är gjort och ska göras mm. Sedan så skapade jag ett jks certifikat/keystore med lösenord osv genom att använda java keystore kommandot i terminalen. Efter det så började jag skriva koden för servern och skriva ett test för att test ansluta till servern (ett terminal program). JKS-keystoren används för att kryptera datan som skickas. Sedan när grunden i servern var gjord så skapade jag klient. Sedan så fortsatte jag koda på klienten och servern och jobbade som vanligt inom MVC (model, view, controller). Jag använde samma DBManager som jag kodat sedan innan, ändrade DB filen så att den skapar databas med de kolumner som ska finnas. Jag använde också samma login- och registersystem som jag tidigare använt i andra projekt. Jag fortsatte lägga till mer och mer funktioner. Skapade också draw.io skiss av databasen. Jag skrev också JavaDocs kommentarer till de flesta metoder (så mycket som jag hann).

## Bakgrund

Chattprogrammet består av två program: en server och en klient. Jag börjar med att förklara hur servern fungerar lite kortfattat: 
* Användaren startar filen MainServer.java. Den filen i sig öppnar anslutning till databasen direkt och om det fungerar så skapar den mvc för servern och visar dess view för användaren. 
* När användaren klickar på "Start Chat Server" i JMenuBaren så anropas metod i ChatServerModel och där så startas en ny tråd som är i en annan klass som heter ChatServer (om servern inte redan är startad, isåfall visas felmeddelande).
* I ChatServer tråden så skapas en ServerSocket utifrån SSLSocketFactory.getDefault().createServerSocket(serverPort). Sedan en oändlig loop (förutom då StopChatServer boolean är true). I loopen skapas en ny socket utifrån serversocket.accept() (vilket är blockerande, så loopen väntas till en klient ansluts). Då skapas en ny tråd (ChatServerMainReceiver) med Socket argumentet, ett ID beroende på hur många klienter som är anslutna, samt ChatServerController klassen. Klient 1 får ID 0 klient 2 id 1 osv. I ChatServerMainReceiver tråden så läser servern meddelanden från klienter i en loop så länge socketen inte är frånkopplad. Meddelandet avkodas till sträng från hex mha en metod i Passutils klassen. Meddelanden skrivs ut i en JTextPane genom html (HTMLDocument, HTMLEditorKit, StyledDocument osv) i appendToPane metoden i viewn. StyleConstants.setIcon används för bilder med sökväg till bilden (i appendtopane metoden i viewn).
* Finns mycket mer än såhär att förklara men orkar inte förklara mer utförligt just nu. Detta är basically grunden för servern.

Klienten fungerar ungefär såhär: 
* Användaren startar filen MainClient.java. Då öppnar login- och registersystemet där användaren får logga in/registrera sig. När klienten är inloggad så visas klientprogrammets view och klienten kan ansluta till servern, skicka meddelanden/bilder mm. 
* När klienten ansluter till servern så skickas klientens användarnamn och ip till servern vilket sparas i serverns ChatServerMainReceiver.  Klientens meddelanden kodas till hex innan det skickas via PassUtils klassen.
* Klienten får meddelanden och bilder genom ChatClientMainReceiver. Meddelanden skrivs ut i en JTextPane genom html (HTMLDocument, HTMLEditorKit, StyledDocument osv) i appendToPane metoden i viewn. StyleConstants.setIcon används för bilder med sökväg till bilden (i appendtopane metoden i viewn).
* Finns mycket mer än såhär att förklara men orkar inte förklara mer utförligt just nu. Detta är basically grunden för klienten.

Chattprogrammet har följande funktioner:

* Servern är multithreadad alltså stödjer flera klienter samtidigt. Kan sätta en gräns på max klienter samtidigt. 
* All nätverkstrafik via servern och klienterna är helt krypterad med ett jks certifikat/keystore och SSLSocket som jag använt. 
* Klienter kan skicka meddelanden till alla klienter eller bara till en specific klient.
* Klienter kan skicka bilder till alla klienter (hann inte göra för specifik klient). 
* Alla meddelanden och bilder sparas i en MySQL databas. När en klient loggar in och ansluter till servern så ser klienten de 50 senaste meddelanden som den har tillåtelse att se. De meddelanden som klienten har tillåtelse att se är de som har "Receiver" som innehåller "All" eller klientens användarnamn. Det betyder att en klient inte kan se meddelanden & bilder som andra klienter skickat privat mellan varandra, vilket är bra säkerhet. Bilderna sparas som base64 sträng i databasen och skickas som det. Det som sparas i databasen och som visas är klient som skrev, tid&datum då det skickades, mottagare, bild/meddelande.
* Klienter kan koppla från servern.
* Servern kan kicka en specifik klient antingen genom dess ID eller genom dess användarnamn. Detta är inte en bannlysning utan klienten kan återansluta igen.
* Servern kan stoppa och starta sig själv.
* Gränsnittet är JFrame guis för både server och klient som jag skrivit själv, men det finns även test program som jag skrev för att testa en del av serverns funktionalitet, och som har terminal gränssnitt.
* Använt MVC och OOP för hela programmet.
* Klient får fel dialog ruta om klienten försöker skicka meddelande till sig själv, dock så tillåts det.
* Med mera.

Det var några funktioner som jag inte hann lägga till på grund av mycket annat i skolan att göra, men som jag vet hur man lätt skulle kunna lägga till dessa funktioner eftersom jag programmerat avancerare funktioner förut. Dessa funktioner var:
* Klienter skicka bild till annan specifik klient.
* Använd IP lookups så att det står vilket land en klient kommer ifrån.
* Bannlys specifik IP eller användarnamn från att ansluta till servern.
* Lägga till så att klienter kan ladda upp en profilbild (avatar) och att den följer med i chatten.
* Skicka filer mellan klienter och till alla.
* Göra så att användare inte kan ansluta till servern om en användare med exakt samma användarnamn redan är ansluten. Visa isåfall error ruta med info.
* Skicka emojis i meddelanden.
* Göra så att servern kan skicka meddelanden till alla klienter samt specifik klient.
* Gör så att klienten får en varningsdialogruta om klienten försöker skicka ett meddelande till en annan klient som inte är ansluten till servern just nu, men att dialogrutan också säger att klienten som inte är ansluten just nu kommer kunna se meddelandet sen när den ansluter till servern. Tillåt att skicka meddelanden till oanslutna klienter, men kolla så att användarnamnet finns i databasen under users, annars neka.

Jag har använt en dator med IntelliJ och det är där jag skrivit koden. Jag har använt keytool-kommandot i terminalen för att skapa ett jks certifikat/keystore. Den keystoren har jag sedan använt för att kryptera nätverkstrafiken.

## Positiva erfarenheter
Allt med arbetet gick bra tycker jag, även fast jag inte hann lägga till alla de funktioner som jag tänkt. Men jag hann de flesta. Jag lärde mig hur man lägger till bilder i JTextPane via html då man har en sökväg. Det mesta andra visste jag redan sedan innan, men det var ett roligt projekt.


## Negativa erfarenheter
Inget negativt med arbetet tycker jag. 


## Sammanfattning
Jag tycker att arbetet gick bra. Flera nya funktioner kan läggas till och jag hann inte lägga till alla som jag tänkt men de flesta. 

