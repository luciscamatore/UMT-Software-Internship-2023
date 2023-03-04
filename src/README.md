
Acest fisier o sa contina explicatia algoritmului abordat in fisierul **main.java**

Presupunerile pe care le-am facut sunt:
  - orele din calendare sunt doar din 30 in 30 de minute sau din ora in ora;
  - intervalele primite sunt cele ocupate;
  - timpul de intalnire este doar in minute;
  
Ideea pe care am abordat-o este urmatoarea:
  - dupa primirea input-ulu, formatam textul, eliminand parantezele, apostroafele, virgulele si spatiile care nu sunt necesare;
  - dupa formatare o sa cream o lista de tip **LocalTime** in care stocam intervalele ocupate;
  - in acea lista o sa cautam intervalele de timp libere;
  - o sa comparam intervalele de timp din primul calendar cu fiecare interval din al doilea calendar pentru a gasit intervalele care se intersecteaza;
  - creeam lista finala care contine intrevalele in care cele doua persoane se pot intalni;

Primul pas a fost sa citim de la tastatura datele primite, acest lucru se poate realiza cu functia **read()** .
```JAVA
public static List<LocalTime> read(){  
	Scanner scanner = new Scanner(System.in);
	String text = scanner.nextLine();
	return convertInput(text);  
}
```
Aceasta functie citeste textul sub forma de **String**, care mai apoi este trimis ca si parametru functiei **convertInput()** pentru formatare.
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
Formatarea consta in eliminarea tuturor caracterelor care nu sunt necesare, cum ar fi:
- parantezele patrate;
- virgulele;
- apostroafele;
- spatiile in plus ;

In final obtinem un string de forma:
[['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00']] &rarr; 9:00 10:30 12:00 13:00 16:00 18:00
```JAVA
input.add(LocalTime.parse(s, DateTimeFormatter.ofPattern("H:mm")));
```
Putem converti fiecare element de tip **String** in tipul **LocalTime** cu functia **LocalTime.parse()**, iar formatul ales este *H:mm*, in final obtinand o lista de variabile de tip **LocalTime** pe care o sa o prelucram in continuare.

Aceste liste o sa treaca prin functia **intervalLiber()** pentru a fi convertite dintr-o lista de *intervale ocupate* intr-o lista de *intervale libere*.
```JAVA
public static List<LocalTime> intervalLiber(List<LocalTime> calendar, List<LocalTime> limita){
	List<LocalTime> calendarLiber = new ArrayList<>();
	
	if(limita.get(0).compareTo(calendar.get(0)) < 0) calendarLiber.add(limita.get(0)); 
```
Daca exista timp liber intre limita inferioara si prima ora din calendar, o sa adaugam aceasta limita in lista finala.
Exemple:
 - limite: [9:00&rarr;20:00]
 - prima ora din calendar: 10:00
 - avem timp liber intre [9:00&rarr;10:00]
```JAVA 
	for(int i = 0; i< calendar.size()-1; i++) {  
		if (calendar.get(i).equals(calendar.get(i + 1))) {  
			i++;
```
Daca exista duplicate, le eliminam sarind peste doua elemente, deoarece nu sunt necesare, de exemplu intervalul [12:30&rarr;14:30], [14:30&rarr;15:00] este echivalent cu intervalul [12:30&rarr;15:00].
```JAVA
		} else if(calendar.get(i).equals(limita.get(0))) {  
			i++;
			calendarLiber.add(calendar.get(i));
```
Daca nu avem timp liber intre limita inferioara si prima ora din calendar, nu o sa adaugam nici limita nici ora in lista, sarind peste un element.
```JAVA
		}else {  
			calendarLiber.add(calendar.get(i));
			}	
		}
```
Daca nici o conditie nu este indeplinita adaugam elementul in lista.
```JAVA
		calendarLiber.add(calendar.get(calendar.size()-1));   
```
Deoarece am parcurs lista pana la **calendar.size()-2**, nu o sa ajungem la ultimul element asa ca o sa il adaugam la final.
```JAVA		
	if(limita.get(1).compareTo(calendar.get(calendar.size() - 1)) >0) calendarLiber.add(limita.get(1));
	
	return calendarLiber;
}
```
Continuam prin a verifica daca avem timp liber la finalul calendarului comparand limita superioara cu ultima ora din calendar.

In final, dupa aceasta functie, o sa primim o lista de intervale libere:
9:00 10:30 12:00 13:00 16:00 18:00 &rarr; 10:30 12:00 13:00 16:00 18:00 20:00
Intervalele libere fiind:
- 10:30&rarr;12:00
- 13:00&rarr;16:00
-  18:00&rarr;20:00

Cu lista de intervale libere creata, putem lua intervalele din primul calendar si sa le comparam cu fiecare interval din cel de-al doilea interval. Realizam acest lucru cu ajutorul functiei **findAvailableTime()**, care primeste ca parametrii listele de intervale libere si timpul intalnirii.
```JAVA
public static List<LocalTime> findAvailableTime(List<LocalTime> calendar1, List<LocalTime> calendar2, long meetingTime){ 
	List<LocalTime> availableTime = new ArrayList<>();

	for(int j=0;j<calendar1.size()-1;j+=2)
	{  
		LocalTime limitaJos = calendar1.get(j);
		LocalTime limitaSus = calendar1.get(j+1);
```
Parcurgem primul interval, din doi in doi, stocand limitele fiecarui interval in variabilele **limitaJos** si **limitaSus**, de exemplu, pentru acest interval 10:30 12:00 13:00 16:00 18:00 20:00, limitele o sa fie:
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
Pentru fiecare interval din primul calendar parcurgem intervalele din al doilea calendar pentru a gasi intervale care se intersecteaza. Ca doua intervale sa se intersecteze maximul dintre limitele inferioare trebuie sa fie mai mic decat minimul dintre limitele superioare. De exemplu, pentru intervalele [10:30&rarr;12:00] si [11:30&rarr;12:30]
- limitele inferioare sunt 10:30 si 11:30, maximul fiind **11:30**;
- limitele superioare sunt 12:00 si 12:30, minimul fiind **12:00**;

Deoarece **11:30** este mai mic decat **12:00**, intervalele se intersecteaza.

De fiecare data cand aceasta conditie este indeplinita si diferenta de timp dintre aceste limite este mai mare decat timpul de intalnire, adaugam aceste limite in lista finala de intervale.

Ultimul pas este afisarea pe ecran a intervalelor de timp in care cele doua persoane se pot intalni, acest lucru este realizat cu functia **afisare()**.
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
Formatam lista finala adaugand parantezele patrate, virgulele, apostroafele si spatiile necesare, afisand rezultatul pe ecran.
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
In functia main citim fiecare calendar impreuna cu limitele sale si timpul de intalnire. Deoarece functia **until()** ne retureneaza o valoare de tip **long**, timpul de intalnire trebuie sa fie si el la randul lui de tip **long**.
