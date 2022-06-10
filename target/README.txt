README/INSTRUKCJA OBSŁUGI

Poniżej znajduje się zestaw wskazówek i/lub przemyśleń związanych z praktyczną realizacją projektu:

Guziki play, pause, reset, zapisz zmiany i cofnij zmiany działają jak wszędzie i tłumaczą się same
Zmiana głośności przyjmuje wartość typu double, zwiększa/zmniejsza głośność x razy
Zmiana prędkości również przyjmuje wartość typu double, zmienia prędkość x-krotnie
Blur jest metodą bezparametrową, ale można to łatwo zmienić przy dalszym rozwoju projektu
Wycinanie również jest z punktu widzenia użytkownika metodą bezparametrową (nie licząc sliderów),
Wywołanie tej metody pozostawia fragment filmu zawarty pomiędzy sliderami, tworzą po drodze trzy pliki - przed, po i wycięcie
Lączenie wideo przyjmuje wartość typu int z zakresu 0-1 określającą czy dodawany plik ma zostać dołączony przed czy po edytowanym
Drugim parametrem jest nazwa pliku do wczytania
Balans kolorów przyjmuje trzy argumenty: kolor red/blue/green (poprawne są również litery r/g/b),
ton shadows/midtones/highlights (poprawne low albo l albo s/m/h) oraz double wartość zmiany z przedziału -1,1;
Wybieranie plików, a więc wybierz plik, wczytaj projekt i eksportuj potrzebują pełnej nazwy pliku wraz z rozszerzeniem

Uwagi techniczne do legacy code'u - w niektórych metodach jest dość nietypowe rozwiązanie z dodawaniem sekundowego bufora
na brzegach pliku. Wynika to ze specyficzności ffmpeg-a, który wywala się czasem na wywołaniu metod od brzegu do brzegu

Wprowadziliśmy zabezpieczenie przed użytkownikiem używającym sliderów "na odwrót" (górnym zaznaczanie prawej krawędzi,
dolnym lewej), stąd duża ilość or-ów (||) w if-ach w controllerze oraz specyficzna implementacja struktury danych dla sliderów
(tablica dwuelementowa, w której przy wywoływaniu metod znajdujemy element maksymalny i minimalny).

Staraliśmy się pisać tak, żeby projekt dawało się rozwijać, a metody w logiczny sposób dalej parametryzować

W Controllerze jest w zasadzie podwojony switch obsługujący wywoływanie metod - jedna implementacja jest na potrzeby GUI
i użytkownika, który w czasie rzeczywistym przekazuje dane, a druga (bardziej sparametryzowana) na potrzeby odczytu plików projektu

