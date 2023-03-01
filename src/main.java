import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class main {
    private static double[] parseInput(String text) {
        text = text.replaceAll("[\\[\\]]", ""); // eliminam parantezele patrate
        text = text.replaceAll("'", ""); // eliminam apostroafele
        text = text.replaceAll(", ", " ");
        text = text.replaceAll(",", " ");

        String[] str = text.split(" "); // separam orele la feicare virgula
        double[] input = new double[str.length]; // creeam un array pentru a stoca orele

        for (int i = 0; i < str.length; i++) { //pargurgem tot sirul citit
            input[i] = Double.parseDouble(str[i].replace(":", "."));
        }
        return input; //returnam 2D
    }
    public static double[] read(){
        /*o sa presupunem ca input-ul o sa fie de forma [['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00]],
        textul pentru fiecare input fiind deja prezent pe ecran*/
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        double[] input = parseInput(text);
        return input;
    }
    public static double[] duplicat(double[] c1)
    {
        ArrayList<Double> rez = new ArrayList<Double>();
        for(int i=0;i<c1.length-1;i++)
        {
            if(c1[i] == c1[i+1])
                i++;
            else
                rez.add(c1[i]);
        }
        rez.add(c1[c1.length-1]);
        double[] x = new double[rez.size()];
        for(int i=0;i<x.length;i++)
        {
            x[i] = rez.get(i);
            System.out.println(x[i]);
        }
        return x;
    }
    public static double[] intervalLiber(double[] c1, double[] l1){
        int lungime = c1.length-1;

        if(l1[0] < c1[0]) lungime++;
        if(l1[1] > c1[c1.length-1]) lungime++;

        double[] lib = new double[lungime];

        for(int i=0;i<c1.length-1;i++)
        {
            lib[i] = c1[i+1];
        }
        lib[lungime-1] = l1[1];
        //System.out.println(" ");
        return lib;
    }
    public static void comparare(double[] c1, double[] c2)
    {
        //c1 = Arrays.stream(c1).distinct().toArray();
        //c2 = Arrays.stream(c2).distinct().toArray();
        //c1 = duplicat(c1);
        //c2 = duplicat(c2);
        double[] rez = new double[Math.min(c1.length,c2.length)];
        for(int i=0;i<rez.length;i+=2)
        {
            //System.out.println("comparam pe: " + c1[i] + " " + c2[i] + " si " + c1[i+1] + " " + c2[i+1]);
            rez[i] = Math.max(c1[i], c2[i]);
            rez[i+1] = Math.min(c1[i+1], c2[i+1]);
            //System.out.println(rez[i] + " " + rez[i+1]);
        }
    }
    public static void main(String[] args) {
        //System.out.print("[['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00]]: ");
        //double[] calendar1 = read();
        double[] calendar1 = parseInput("[['9:00','10:30'], ['12:00','13:00'], ['16:00','18:00]]");
        double[] calendar2 = parseInput("[['10:00','11:30'], ['12:30','14:30'], ['14:30','15:00], ['16:00','17:00']]");
        //System.out.print("['9:00','20:00']: ");
        //double[] limite1 = read();
        double[] limite1 = parseInput("['9:00','20:00']");
        double[] limite2 = parseInput("['10:00','18:30']");
        //System.out.print("[['10:00','11:30'], ['12:30','14:30'], ['14:30','15:00], ['16:00','17:00']]: ");
        //double[] calendar2 = read();
        //System.out.println("Introduceti limitele pentru al doilea calendar");
        //double[] limite2 = read();
        //comparare(intervalLiber(calendar1,limite1), intervalLiber(calendar2,limite2));
        duplicat(calendar2);
    }
}
