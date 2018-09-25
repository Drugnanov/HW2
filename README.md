# Enehano Test

Cílem zadaní je implementovat jednoduchou micro-service, která se má starat o user managemant. Draft aplikace používá spring-boot.

##### Aplikace by měla splňoat minimálně tyto požadavky:
  - basic authorizace s uživately uloženými v dtb (stačí když použijete nějakou in-memory databázi)
  - implementovat metody pro CRUD operace nad uživatelem
  - implementovat načtení listu uživatelů, tak aby bylo defaultní sortování podle příjmení uživatele
  - pro vytvoření uživatele jsou potřeba tyto jméno, příjmení, email, telefon, heslo. Dále budou vyplněné systémové údaje datum vytvoření a id uživatele, který záznam vytvořil
  - získání listu uživatelů a detail uživatele bude možné získat bez authorizace
  - vytváření, update a mazaní pouze pro přihlášené uživatele (role řešit nemusíte)
  
##### Funkcionalita, která je jako bonus:
 - implementovat uživatelské role (jeden uživatel může mít více rolí)
 - upravit kód, tak aby uživatele mohl vytvářet uživatele pouze uživatel s rolí CREATE_USERS, upravovat s rolí UPDATE_USERS, a mazat s rolí DELETE_USERS.
 
#### Pár tipů k hodnocení implementace
 - první kritérium se funkčnost
 - velký duraz bude kladen na čistotu kódu a správný objektový a archtektonický návrh (tří vrstvá architektura)
 - správné použití HTTP metod
 - jUnit testy, případně integrační testy
 
#### Odevzdání kódu
 - forkem si vytvořte vlastní repozitář
 - až budete mít hotovo stačí vytvořit pull request na repozitář se zadaním (do jiné větve než master).