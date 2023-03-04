import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    private static List<LocalTime> convertInput(String text) {
        text = text.replaceAll("[\\[\\]]", ""); // eliminam parantezele patrate
        text = text.replaceAll("'", ""); // eliminam apostroafele
        text = text.replaceAll(", ", " ");
        text = text.replaceAll(",", " ");

        String[] str = text.split(" "); // separam orele la feicare spatiu
        List<LocalTime> input = new ArrayList<>();

        for (String s : str) { //pargurgem tot sirul citit
            input.add(LocalTime.parse(s, DateTimeFormatter.ofPattern("H:mm")));
        }
        return input; //returnam 2D
    }
    public static List<LocalTime> read(){
        //o sa presupunem ca input-ul o sa fie de forma [['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00]],
        //textul pentru fiecare input fiind deja prezent pe ecran
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        return convertInput(text);
    }
    private static long readMeetingTime() {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        return Long.parseLong(text);
    }
    public static List<LocalTime> intervalLiber(List<LocalTime> calendar, List<LocalTime> limita){
        List<LocalTime> calendarLiber = new ArrayList<>();

        if(limita.get(0).compareTo(calendar.get(0)) < 0) calendarLiber.add(limita.get(0));

        for(int i = 0; i< calendar.size()-1; i++) {
            if (calendar.get(i).equals(calendar.get(i + 1))) {
                i++;
            } else if(calendar.get(i).equals(limita.get(0))) {
                i++;
                calendarLiber.add(calendar.get(i));
            }else {
                calendarLiber.add(calendar.get(i));
            }
        }
        calendarLiber.add(calendar.get(calendar.size()-1));

        if(limita.get(1).compareTo(calendar.get(calendar.size() - 1)) > 0) calendarLiber.add(limita.get(1));
        return calendarLiber;
    }
    public static LocalTime timeMax(LocalTime t1, LocalTime t2){
        if(t1.compareTo(t2) > 0) return t1;
        if(t1.equals(t2)) return t1;
        return t2;
    }
    public static LocalTime timeMin(LocalTime t1, LocalTime t2){
        if(t1.compareTo(t2) < 0) return t1;
        if(t1.equals(t2)) return t1;
        return t2;
    }
    public static List<LocalTime> findAvailableTime(List<LocalTime> calendar1, List<LocalTime> calendar2, long meetingTime){
        List<LocalTime> availableTime = new ArrayList<>();
        for(int j=0;j<calendar1.size()-1;j+=2)
        {
            LocalTime limitaJos = calendar1.get(j);
            LocalTime limitaSus = calendar1.get(j+1);
            for(int i=0;i<calendar2.size()-1;i+=2)
            {
                LocalTime max = timeMax(limitaJos,calendar2.get(i));
                LocalTime min = timeMin(limitaSus,calendar2.get(i+1));
                if((max.compareTo(min) < 0) && max.until(min, ChronoUnit.MINUTES) >= meetingTime) {
                    availableTime.add(max);
                    availableTime.add(min);
                }
            }
        }
        return availableTime;
    }
    public static void afisare(List<LocalTime> rez) {
        StringBuilder availableTime = new StringBuilder();
        availableTime.append("[");
        for (int i = 0; i < rez.size() - 1; i += 2) {
            availableTime.append("['").append(rez.get(i)).append("','").append(rez.get(i + 1)).append("']");
            if (i < rez.size() - 2) availableTime.append(",");
        }
        availableTime.append("]");
        System.out.println(availableTime);
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
        long meetingTime = readMeetingTime();

        afisare(findAvailableTime(intervalLiber(calendar1,limit1), intervalLiber(calendar2,limit2), meetingTime));

    }
}
