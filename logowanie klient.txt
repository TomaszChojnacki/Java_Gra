Tworzenie Bazy danych w postgresql "PgAdmin 4"
Tworzymy bazę bomberman

CREATE TABLE Player (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    gamePoints INTEGER
);



następnie tworzymy urzytkowników:
INSERT INTO Player (id,username, password, gamePoints) VALUES ('1','ucze', '123', 100);
INSERT INTO Player (id,username, password, gamePoints) VALUES ('2','user', 'password', 20);



logowanie pierwszego klienta w konsoli 
Otwieramy cmd z ścieżka gdzie jest zapisany nasz katalog musimy być w katalogu Java_Gra
u nas wygląda to tak C:\Users\Tomasz Chojnacki\Desktop\Java_Gra
następnie wpisujemy 

java -jar Serwer.jar
 

Do zalogowania pierwszego klienta otwiramy nową konsolę i wprowadzamy poniższe komendy:
set DB_HOST=localhost
set DB_PORT=5432
set DB_DATABASE=bomberman
set DB_USERNAME=postgres
set DB_PASSWORD=[twoje haslo do bazy]
java -jar FirstClient.jar



Do zalogowania drugiego klienta otwiramy nową konsolę i wprowadzamy poniższe komendy:
set DB_HOST=localhost
set DB_PORT=5432
set DB_DATABASE=bomberman
set DB_USERNAME=postgres
set DB_PASSWORD=[twoje haslo do bazy]
java -jar SecondClient.jar


haslo do bazy danych jest takie jakie nadaliśmy podczas tworzenia serwera w postgresql
nazwa bazy powinna być taka sama

Do zatrzymania serwera można uruchomic kolejną konsolę 

wpisać polecenie: netstat -aon | find "1234"


następnie należy zabić PID

wykonujemy to poleceniem: taskkill /F /PID [PID]



Pełna prezentacja krok po kroku w pliku PDF




zmienne środowiskowe w plikach Serwer, FirstClient, SecondClient w Intellij 
DB_DATABASE=bomberman;DB_HOST=localhost;DB_PASSWORD=[twoje haslo do bazy];DB_PORT=5432;DB_URL=jdbc:mysql://localhost:5432/bomberman;DB_USERNAME=postgres
