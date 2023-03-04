
Codul se găsește în fișierul **main.java**

Presupunerile pe care le-am făcut:

- orele din calendare sunt doar din 30 în 30 de minute sau din ora în ora;
- intervalele primite sunt cele ocupate;
- timpul de întâlnire este doar în minute;

Ideea pe care am abordat-o este următoarea:
- după primirea input-ului, formatăm textul, eliminând parantezele, apostroafele, virgulele și spațiile care nu sunt necesare;
- după formatare o să creăm o lista de tip **LocalTime** în care stocăm intervalele de timp ocupate;
- în acea lista o să căutăm intervalele de timp libere;
- o să comparăm intervalele de timp din primul calendar cu fiecare interval din al doilea calendar pentru a găsit intervalele care se intersectează;
- creăm lista finală care conține intrevalele în care cele două persoane se pot întâlni;

Primul pas a fost să citim de la tastatură datele primite, acest lucru se poate realiza cu funcția **read()** .
```JAVA
public static List<LocalTime> read(){  
	Scanner scanner = new Scanner(System.in);
	String text = scanner.nextLine();
	return convertInput(text);  
}
```
Această funcție citește textul sub formă de **String**, care mai apoi este trimis că și parametru funcției **convertInput()** pentru formatare.
```JAVA
private static List<LocalTime> convertInput(String text) {  
	text = text.replaceAll("[\\[\\]]", "");
	text = text.replaceAll("'", ""); 
	text = text.replaceAll(", ", " ");
	text = text.replaceAll(",", " ");  
	
	String[] str = text.split("  ");  
	
	List<LocalTime> input = new ArrayList<>();
  
	for (String s : str) { 
	  input.add(LocalTime.parse(s, DateTimeFormatter.ofPattern("H:mm")));  
	}	  
    return input;
}
```
Formatarea constă în eliminarea tuturor caracterelor care nu sunt necesare, cum ar fi:
- parantezele pătrate;
- virgulele;
- apostroafele;
- spațiile in plus ;

In final obținem un string de forma:
[['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00']] &rarr; 9:00 10:30 12:00 13:00 16:00 18:00
```JAVA
input.add(LocalTime.parse(s, DateTimeFormatter.ofPattern("H:mm")));
```
Putem converti fiecare element de tip **String** în tipul **LocalTime** cu funcția **LocalTime.parse()**, iar formatul ales este *H:mm*, în final obținând o listă de variabile de tip **LocalTime** pe care o să o prelucrăm in continuare.

Aceste liste o să treacă prin funcția **intervalLiber()** pentru a fi convertite dintr-o listă de *intervale ocupate* într-o lista de *intervale libere*.
```JAVA
public static List<LocalTime> intervalLiber(List<LocalTime> calendar, List<LocalTime> limita){
	List<LocalTime> calendarLiber = new ArrayList<>();
	
	if(limita.get(0).compareTo(calendar.get(0)) < 0) calendarLiber.add(limita.get(0)); 
```
Dacă există timp liber între limita inferioară și prima ora din calendar, o să adăugăm această limita în lista finală.

Exemple:
- limite: [9:00&rarr;20:00]
- prima oră din calendar: 10:00
- avem timp liber intre [9:00&rarr;10:00]
```JAVA 
	for(int i = 0; i< calendar.size()-1; i++) {  
		if (calendar.get(i).equals(calendar.get(i + 1))) {  
			i++;
```
Dacă există duplicate, le eliminăm sărind peste două elemente, deoarece nu sunt necesare, de exemplu intervalul [12:30&rarr;14:30], [14:30&rarr;15:00] este echivalent cu intervalul [12:30&rarr;15:00].
```JAVA
		} else if(calendar.get(i).equals(limita.get(0))) {  
			i++;
			calendarLiber.add(calendar.get(i));
```
Dacă nu avem timp liber între limita inferioară și prima oră din calendar, nu o să adăugăm nici limita nici ora în listă, sărind peste un element.
```JAVA
		}else {  
			calendarLiber.add(calendar.get(i));
			}	
		}
```
Dacă nici o condiție nu este îndeplinită adăugăm elementul în listă.
```JAVA
		calendarLiber.add(calendar.get(calendar.size()-1));   
```
Deoarece am parcurs lista până la **calendar.size()-2**, nu o să ajungem la ultimul element așa că o să îl adăugăm la final.
```JAVA		
	if(limita.get(1).compareTo(calendar.get(calendar.size() - 1)) >0) calendarLiber.add(limita.get(1));
	
	return calendarLiber;
}
```
Continuăm prin a verifica dacă avem timp liber la finalul calendarului comparând limita superioară cu ultima oră din calendar.

În final, după această funcție, o să primim o listă de intervale libere:
9:00 10:30 12:00 13:00 16:00 18:00 &rarr; 10:30 12:00 13:00 16:00 18:00 20:00
Intervalele libere fiind:
- 10:30&rarr;12:00
- 13:00&rarr;16:00
-  18:00&rarr;20:00

Cu lista de intervale libere creată, putem lua intervalele din primul calendar și să le comparăm cu fiecare interval din cel de-al doilea calendar. Realizăm acest lucru cu ajutorul funcției **findAvailableTime()**, care primește ca parametrii listele de intervale libere și timpul întâlnirii.
```JAVA
public static List<LocalTime> findAvailableTime(List<LocalTime> calendar1, List<LocalTime> calendar2, long meetingTime){ 
	List<LocalTime> availableTime = new ArrayList<>();

	for(int j=0;j<calendar1.size()-1;j+=2)
	{  
		LocalTime limitaJos = calendar1.get(j);
		LocalTime limitaSus = calendar1.get(j+1);
```
Parcurgem primul interval, din doi în doi, stocând limitele fiecărui interval în variabilele **limitaJos** și **limitaSus**, de exemplu, pentru acest interval 10:30 12:00 13:00 16:00 18:00 20:00, limitele o să fie:
- **limitaJos** = 10:30, **limitaSus** = 12:00;
-  **limitaJos** = 13:00, **limitaSus** = 16:00;
-  **limitaJos** = 18:00, **limitaSus** = 20:00;
```JAVA
		for(int i=0;i<calendar2.size()-1;i+=2) 
		{  
			LocalTime max = timeMax(limitaJos,calendar2.get(i));
			LocalTime min = timeMin(limitaSus,calendar2.get(i+1));
			if((max.compareTo(min) < 0) && max.until(min, ChronoUnit.MINUTES) >= meetingTime) 			
			{  
				availableTime.add(max);  
				availableTime.add(min);
			}  
		}  
	}  
	return availableTime;  
}
```
Pentru fiecare interval din primul calendar parcurgem intervalele din al doilea calendar pentru a găsi intervale care se intersectează. Ca două intervale să se intersecteze maximul dintre limitele inferioare trebuie să fie mai mic decât minimul dintre limitele superioare. De exemplu, pentru intervalele [10:30&rarr;12:00] si [11:30&rarr;12:30]
- limitele inferioare sunt 10:30 si 11:30, maximul fiind **11:30**;
- limitele superioare sunt 12:00 si 12:30, minimul fiind **12:00**;

Deoarece **11:30** este mai mic decât **12:00**, intervalele se intersecteaza.

De fiecare dată când această condiție este îndeplinită și diferența de timp dintre aceste limite este mai mare decât timpul de întâlnire, adăugăm aceste limite în lista finală de intervale.

Ultimul pas este afișarea pe ecran a intervalelor de timp în care cele două persoane se pot întâlni, acest lucru este realizat cu funcția **afișare()**.
```JAVA
public static void afisare(List<LocalTime> availableTime) {  
	StringBuilder availableTimeFormat = new StringBuilder();//in continuare formatam array-ul  
	
	availableTimeFormat.append("[");
	
	for (int i = 0; i < availableTime.size() - 1; i += 2) {
		availableTimeFormat.append("['").append(availableTime.get(i)).append("','").append(availableTime.get(i + 1)).append("']");  
		if (i < availableTime.size() - 2) availableTimeFormat.append(",");
	}  
	
	availableTimeFormat.append("]");
	
	System.out.println(availableTimeFormat);  
}
```
Formatăm lista finală adăugând parantezele pătrate, virgulele, apostroafele și spațiile necesare, afișând rezultatul pe ecran.
```JAVA
public static void main(String[] args) {  
	System.out.println("Introduceti datele pentru primul calendar: "); 
	List<LocalTime> calendar1 = read();  
  
	System.out.println("Introduceti limitele pentru primul calendar: "); 
	List<LocalTime> limit1 = read();  
	
	System.out.println("Introduceti datele pentru al doilea calendar: ");  
	List<LocalTime> calendar2 = read();  

	System.out.println("Introduceti limitele pentru al doilea calendar: ");  
	List<LocalTime> limit2 = read();  
  
	System.out.println("Introduceti timpul minim poentru o intalnire: ");  
	Scanner scanner = new Scanner(System.in);
	long meetingTime = scanner.nextLong();  
  
	afisare(findAvailableTime(intervalLiber(calendar1,limit1), intervalLiber(calendar2,limit2), meetingTime));  
  
}
```
În funcția **main()** citim fiecare calendar împreună cu limitele sale și timpul de întâlnire. Deoarece funcția **until()** ne retureneaza o valoare de tip **long**, timpul de întâlnire trebuie să fie și el la rândul lui de tip **long**.

În continuare sunt prezentate câteva teste realizate cu acest algoritm.
```JAVA
Calendar1: [['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00']]
Limite1: ['9:00','20:00']
        
Calendar2: [['10:00','11:30'], ['12:30','14:30'], ['14:30','15:00], ['16:00','17:00']]
Limite2: ['10:00','18:30']
        
Timp de intalnire: 30
        
Rezultat: [['11:30','12:00'],['15:00','16:00'],['18:00','18:30']]
```

```JAVA
Calendar1: [['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00']]
Limite1: ['8:00','20:00']
        
Calendar2: [['10:00','11:30'], ['12:30','14:30'], ['14:30','15:00], ['16:00','17:00']]
Limite2: ['8:00','18:30']
        
Timp de intalnire: 30
        
Rezultat: [['08:00','09:00'],['11:30','12:00'],['15:00','16:00'],['18:00','18:30']]
```

```JAVA
Calendar1: [['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00], ['19:00','19:30']]
Limite1: ['8:00','20:00']
        
Calendar2: [['10:00','11:30'], ['12:30','14:30'], ['14:30','15:00], ['16:00','17:00']]
Limite2: ['8:00','20:00']
        
Timp de intalnire: 30
        
Rezultat: [['08:00','09:00'],['11:30','12:00'],['15:00','16:00'],['18:00','19:00'],['19:30','20:00']]
```

```JAVA
Calendar1: [['12:00','13:00'], ['16:00','18:00], ['19:00','19:30']]
Limite1: ['8:00','19:30']
        
Calendar2: [['11:00','11:30'], ['12:30','14:30'], ['14:30','15:00], ['16:00','17:00']]
Limite2: ['8:00','20:00']
        
Timp de intalnire: 30
        
Rezultat: [['08:00','11:00'],['11:30','12:00'],['15:00','16:00'],['18:00','19:00']]
```

```JAVA
Calendar1: [['12:00','12:30'], ['16:00','17:00], ['19:00','19:30']]
Limite1: ['8:00','21:00']
        
Calendar2: [['11:00','11:30'], ['12:30','13:00], ['13:30','14:00'], ['15:00','16:00'], ['17:30','18:00'], ['19:30','20:00']]
Limite2: ['8:00','21:00']
        
Timp de intalnire: 30
        
Rezultat: [['08:00','11:00'],['11:30','12:00'],['13:00','13:30'],['14:00','15:00'],['17:00','17:30'],['18:00','19:00'],['20:00','21:00']]
```