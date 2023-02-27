import java.util.Arrays;
import java.util.Scanner;

public class main {
    private static float[][] parseInput(String text) {
        text = text.replaceAll("[\\[\\]]", ""); // eliminam parantezele patrate
        text = text.replaceAll("'", ""); // eliminam apostroafele
        String[] pairs = text.split(", "); // separam perechile de ore la feicare virgula
        float[][] input = new float[pairs.length][2]; // creeam un array 2D pentru a stoca orele
        for (int i = 0; i < pairs.length; i++) { //pargurgem tot sirul citit
            String[] times = pairs[i].split(","); // separam fiecare pereche in 2 ore separate
            input[i][0] = Float.parseFloat(times[0].replace(":", ".")); //le transformam din ore in numere float
            input[i][1] = Float.parseFloat(times[1].replace(":", "."));
        }
        return input; //returnam 2D
    }
    public static float[][] read(){
        /*o sa presupunem ca input-ul o sa fie de forma [['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00]],
        textul pentru fiecare input fiind deja prezent pe ecran*/
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        float[][] input = parseInput(text);

        return input;
    }

    public static float[][] liber(float[][] calendar){
        float limita1 = 8;
        float limita2 = 20;
        float[][] lib = new float[calendar.length+2][2];
        /*for(int i=0;i< calendar.length;i++){
            System.out.println("i "+i);
            System.out.println("caldendar 0 "+calendar[i][0]);
            System.out.println("caldendar 1 "+calendar[i][1]);
        }*/
        if(calendar[0][0] > limita1){
            lib[0][0] = limita1;
        }else{
            lib[0][0] = calendar[0][0];
        }
        for(int i=0; i<calendar.length-1; i++){
            lib[i][0] = calendar[i][1];
            lib[i+1][1] = calendar[i+1][0];
        }
        if(calendar[calendar.length-1][1] < limita2)
        {
            lib[lib.length-1][1] = limita2;
        }
        for(int i=0;i< calendar.length;i++)
        {
            System.out.println(lib[i][0]);
            System.out.println(lib[i][1]);
        }
        return lib;
    }

    public static void main(String[] args) {
        System.out.print("Introduceti datele pentru primul calendar: ");
        float[][] calendar1 = read();
        /*System.out.println("Introduceti limitele pentru primul calendar");
        float[][] limite1 = read();
        System.out.print("Introduceti datele pentru al doilea calendar: ");
        float[][] calendar2 = read();
        System.out.println("Introduceti limitele pentru al doilea calendar");
        float[][] limite2 = read();*/
        System.out.println(Arrays.deepToString(calendar1));
       /* System.out.println(Arrays.deepToString(calendar2));
        System.out.println(Arrays.deepToString(limite1));
        System.out.println(Arrays.deepToString(limite2));*/
        System.out.println(liber(calendar1));
    }
}
