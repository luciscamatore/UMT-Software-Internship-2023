
Acest fisier o sa contina explicatia codului din fisierul "main.java"

Presupunerile pe care le-am facut sunt:
  - orele din calendare sunt doar din 30 in 30 de minute sau din ora in ora
  - intervalele primite sunt cele ocupate
  
Ideea pe care am abordat-o este urmatoarea:
  - dupa primirea input-ulu, formatam textul, eliminand parantezele, apostroafele, virgulele si spatiile care nu sunt necesare
  - dupa formatare o sa cream o lista de tip **LocalTime** in care o sa stocam intervalele ocupate
  - in acea lista o sa cautam intervalele de timp libere
  - o sa comparam intervalele de timp din primul calendar cu fiecare interval din al doilea calendar pentru a gasit intervalele care se intersecteaza
  - creeam lista finala care contine intrevalele in care cele doua persoane se pot intalni

Primul pas a fost sa citim de la tastatura datele primite, acest lucru se poate realiza cu functia **read()** 
```JAVA
public static List<LocalTime> read(){  
	Scanner scanner = new Scanner(System.in);
	String text = scanner.nextLine();
	return convertInput(text);  
}
```
Aceasta functie citeste textul sub forma de **String**, care mai apoi este trimis ca si parametru functiei **convertInput()**
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
    return input; //returnam array-ul final  
}
```
