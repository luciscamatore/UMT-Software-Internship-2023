import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    private static List<LocalTime> convertInput(String text) {
        //primim ca si argument un string de forma [['9:00','10:30'], ['16:00','18:00]]
        text = text.replaceAll("[\\[\\]]", ""); //eliminam parantezele patrate
        text = text.replaceAll("'", ""); //eliminam apostroafele
        text = text.replaceAll(", ", " "); //eliminam virgula si spatiul pentru a nu avea spatii duble
        text = text.replaceAll(",", " "); //eliminam virgula simpla
        //in final string-ul o sa fie de forma 9:00 10:30 16:00 18:00
        String[] str = text.split(" "); // in str introducem toate orele separate la feicare spatiu
        List<LocalTime> input = new ArrayList<>(); //lista in care o sa fie output-ul de tipul LocalTime

        for (String s : str) { //iteram prin fiecare element a lui str
            input.add(LocalTime.parse(s, DateTimeFormatter.ofPattern("H:mm")));//in input adaugam elementul din str convertit in tipul LocalTime dupa formatul "H:mm"
        }
        return input; //returnam lista finala
    }
    public static List<LocalTime> read(){
        //o sa presupunem ca input-ul o sa fie de forma [['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00]],
        //textul pentru fiecare input fiind deja prezent pe ecran
        Scanner scanner = new Scanner(System.in); //cream o variabila scanner pentru a putea citi liniile de la tastatura
        String text = scanner.nextLine(); //citim urmatoarea linie
        return convertInput(text); //returnam input-ul convertit din string in LocalTime
    }
    public static List<LocalTime> intervalLiber(List<LocalTime> calendar, List<LocalTime> limita){
        //primim ca si argumente o lista de intervale si o lista de limite
        List<LocalTime> calendarLiber = new ArrayList<>();//lista in care o sa adaugam intervalele libere

        if(limita.get(0).compareTo(calendar.get(0)) < 0) calendarLiber.add(limita.get(0));
        //daca limita inferioara este mai mica decat prima ora din calendar inseamna ca intre cele doua exista un interval liber
        //pe care il adaugam in lista

        for(int i = 0; i< calendar.size()-1; i++) {//iteram prin calendarul primit ca si argument
            if (calendar.get(i).equals(calendar.get(i + 1))) {
                i++;//daca exista duplicate le eliminam
                //de exemplu ['12:30','14:30'],['14:30','15:00] este echivalent cu ['12:30','15:00]
                //daca se gaseste astfel de duplicat sarim peste cele 2 elemente
            } else if(calendar.get(i).equals(limita.get(0))) {
                i++;//daca limita inferioara si prima ora din caledar sunt egale nu le adaugam in noul array
                calendarLiber.add(calendar.get(i));//sarim peste prima ora si o adaugam pe urmatoarea
            }else {
                calendarLiber.add(calendar.get(i));//daca nu este indeplinita nici o conditie adaugam ora in noua lista
            }
        }
        calendarLiber.add(calendar.get(calendar.size()-1));//deoarece mai sus a fost nevoie sa iteram pana la calendar.size()-2 nu o sa ajungem la ultimul element
        //il adaugam aici la final

        if(limita.get(1).compareTo(calendar.get(calendar.size() - 1)) > 0) calendarLiber.add(limita.get(1));
        //daca limita superioara este mai mare decat ultima ora din calendar o adaugam si pe aceasta in lista
        return calendarLiber;//in final am obtinut o lista cu intervale libere
    }
    public static LocalTime timeMax(LocalTime t1, LocalTime t2){//functie pentru a determina maximul dintre doua variabile de tip LocalTime
        //primim ca si parametrii doua variabile de tip LocalTime
        if(t1.compareTo(t2) > 0) return t1;//daca t1 este mai mare decat t2 returnam t1
        if(t1.equals(t2)) return t1;//daca sunt egale returnam t1
        return t2;//in caz contrar returnam t2
    }
    public static LocalTime timeMin(LocalTime t1, LocalTime t2){//identic cu timeMax doar ca aici cautam cea mai mica valoare
        if(t1.compareTo(t2) < 0) return t1;
        if(t1.equals(t2)) return t1;
        return t2;
    }
        public static List<LocalTime> findAvailableTime(List<LocalTime> calendar1, List<LocalTime> calendar2, long meetingTime){//aici se intampla magia
            //primim ca si parametrii doua liste cu intervalele libere si timpul de intalnire
            List<LocalTime> availableTime = new ArrayList<>();//lista in care stocam intervalele libere
            //in aceasta functie luam fiecare interval din primul calendar si verificam cu care interval se potriveste din al doilea calendar
            for(int j=0;j<calendar1.size()-1;j+=2)//parcurgem primul calendar din 2 in 2
            {
                LocalTime limitaJos = calendar1.get(j);//stocam limita de jos a primului interval liber
                LocalTime limitaSus = calendar1.get(j+1);//stocam limita de sus a primului interval liber
                for(int i=0;i<calendar2.size()-1;i+=2)//parcurgem cel de-al doilea calendar
                {
                    LocalTime max = timeMax(limitaJos,calendar2.get(i));//maximul dintre limitele de jos ale intervalelor
                    LocalTime min = timeMin(limitaSus,calendar2.get(i+1));//minimul dintre limitele de sus ale intervalelor
                    if((max.compareTo(min) < 0) && max.until(min, ChronoUnit.MINUTES) >= meetingTime) {
                        //daca maximul dintre limitele de jos ale intervalelor este mai mic decat minimul dintre limitele de sus ale intervalelor
                        //si diferenta dintre aceste doua intervale este mai mare sau egala cu timpul necesar de intalnire
                        //atunci avem un interval in care cele doua persoane se pot intalni
                        availableTime.add(max);
                        availableTime.add(min);//le adaugam in noua lista
                    }
                }
            }
            return availableTime;//returnam lista completa
        }
    public static void afisare(List<LocalTime> availableTime) {
        //primim ca si parametru lista cu intervalele in care cele doua persoane se pot intalni
        StringBuilder availableTimeFormat = new StringBuilder();//in continuare formatam lista
        availableTimeFormat.append("[");//prima paranteza patrata
        for (int i = 0; i < availableTime.size() - 1; i += 2) {//parcurgem lista pentru a adauga elementele necesare
            availableTimeFormat.append("['").append(availableTime.get(i)).append("','").append(availableTime.get(i + 1)).append("']");
            if (i < availableTime.size() - 2) availableTimeFormat.append(",");//ca sa nu punem virgula la final de tot
        }
        availableTimeFormat.append("]");//ultima paranteza patrata
        System.out.println(availableTimeFormat);//afisam pe ecran rezultatul
    }

    public static void main(String[] args) {
        System.out.println("Introduceti datele pentru primul calendar: ");
        //List<LocalTime> calendar1 = convertInput("[['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00]]");
        List<LocalTime> calendar1 = read();

        System.out.println("Introduceti limitele pentru primul calendar: ");
        //List<LocalTime> limit1 = convertInput("['9:00','20:00']");
        List<LocalTime> limit1 = read();

        System.out.println("Introduceti datele pentru al doilea calendar: ");
        //List<LocalTime> calendar2 = convertInput("[['10:00','11:30'], ['12:30','14:30'], ['14:30','15:00], ['16:00','17:00']]");
        List<LocalTime> calendar2 = read();

        System.out.println("Introduceti limitele pentru al doilea calendar: ");
        //List<LocalTime> limit2 = convertInput("['10:00','18:30']");
        List<LocalTime> limit2 = read();

        System.out.println("Introduceti timpul minim poentru o intalnire: ");
        Scanner scanner = new Scanner(System.in);//creeam o variabila scanner pentru a putea citi timpul de intalnire
        long meetingTime = scanner.nextLong();//citim urmatoarea linie

        afisare(findAvailableTime(intervalLiber(calendar1,limit1), intervalLiber(calendar2,limit2), meetingTime));
    }
}
