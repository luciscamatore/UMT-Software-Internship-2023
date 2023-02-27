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
        /*o sa presupunem ca inputul o sa fie de forma [['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00]],
        textul pentru fiecare input fiind deja prezent pe ecran*/
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        float[][] input = parseInput(text);
        return input;
    }


    public static void main(String[] args) {
        System.out.print("Introduceti datele pentru primul calendar: ");
        float[][] calendar1 = read();
        System.out.println("Introduceti limitele pentru primul calendar");
        float[][] limite1 = read();
        System.out.print("Introduceti datele pentru al doilea calendar: ");
        float[][] calendar2 = read();
        System.out.println("Introduceti limitele pentru al doilea calendar");
        float[][] limite2 = read();
        System.out.println(Arrays.deepToString(calendar1));
        System.out.println(Arrays.deepToString(calendar2));
        System.out.println(Arrays.deepToString(limite1));
        System.out.println(Arrays.deepToString(limite2));
    }
}
