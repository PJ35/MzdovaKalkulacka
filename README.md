# Mzdová kalkulačka

## Technické specifikace

Aplikace běží v Javě a používá Oracle Open JDK version 21.

Grafické uživatelské rozhraní je vytvořené pomocí knihovny Swing.

## Popis funkce

Z hrubé mzdy počítá čistou mzdu a zobrazuje odvody na sociální a zdravotní pojištění a daň z příjmu, popřípadě daňový bonus jako zápornou daň, které souvisí s danou mzdou.

Dokáže do výpočtů zahrnout také služební auto, pokud jej má zaměstnanec pro soukromé účely, a od daně odečíst nezdanitelnou část a slevu na dani za děti v opatrovnictví.

Všechny slevy na dani a sazby jsou v souborech, které se při spuštění aplikace načtou, aby bylo snadné je aktualizovat.

## Pokyny pro aktualizaci souborů

Jednotlivá čísla pište na první řádek a oddělujte je středníkem. Ostatní řádky slouží pro popis a snadnější orientaci.

### Slevy na dani

Udávejte v celých Kč.

Zadávejte v pořadí Sleva na poplatníka, Sleva na první, druhé a další dítě v opatrovnictví.

### Sazby

Udávejte v tisícinách procenta, budou později v kódu převedena. Je to kvůli jednodušším výpočtům.

Zadávejte v pořadí Sazba na emisní, nízkoemisní a bezemisní auto, Sazba sociálního a zdravotního pojištění placených zaměstnancem a Sazba daně z příjmu.
