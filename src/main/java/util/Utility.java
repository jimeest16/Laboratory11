package util;

import domain.EdgeWeight;
import domain.Vertex;

import java.text.DecimalFormat;
import java.util.Random;

public class Utility {
    public static String format(double value){
        return new DecimalFormat("###,###,###.##").format(value);
    }
    public static String $format(double value){
        return new DecimalFormat("$###,###,###.##").format(value);
    }

    public static void fill(int[] a, int bound) {
        for (int i = 0; i < a.length; i++) {
            a[i] = new Random().nextInt(bound);
        }
    }

    public static int random(int bound) {
        return new Random().nextInt(bound);
    }
    public static int randomMinMax(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static int compare(Object a, Object b) {
        switch (instanceOf(a, b)){
            case "Integer":
                Integer int1 = (Integer)a; Integer int2 = (Integer)b;
                return int1 < int2 ? -1 : int1 > int2 ? 1 : 0; //0 == equal
            case "String":
                String st1 = (String)a; String st2 = (String)b;
                return st1.compareTo(st2)<0 ? -1 : st1.compareTo(st2) > 0 ? 1 : 0;
            case "Character":
                Character c1 = (Character)a; Character c2 = (Character)b;
                return c1.compareTo(c2)<0 ? -1 : c1.compareTo(c2)>0 ? 1 : 0;
            case "EdgeWeight":
                EdgeWeight eW1 = (EdgeWeight) a;
                EdgeWeight eW2 = (EdgeWeight) b;
                return compare(eW1.getEdge(),eW2.getEdge());
            case "Vertex":
                Vertex v1 = (Vertex) a;
                Vertex v2 = (Vertex) b;
                return compare(v1.data,v2.data);
        }
        return 2; //Unknown
    }

    public static String instanceOf(Object a, Object b) {
        if(a instanceof Integer && b instanceof Integer) return "Integer";
        if(a instanceof String && b instanceof String) return "String";
        if(a instanceof Character && b instanceof Character) return "Character";
        if(a instanceof EdgeWeight && b instanceof EdgeWeight) return "EdgeWeight";
        if(a instanceof Vertex && b instanceof Vertex) return "Vertex";
        return "Unknown";
    }
    //PARA Adjenency Matrix Graph TEST:
    public static String getColor() {
        String[] colors = {
                "Red", "Blue", "Green", "Yellow", "Purple", "Orange", "Pink",
                "Brown", "Black", "White", "Gray", "Violet", "Indigo", "Cyan",
                "Magenta", "Turquoise", "Gold", "Silver", "Beige", "Maroon",
                "Navy", "Teal", "Lime", "Olive", "Coral"
        };
        return colors[random(colors.length - 1)];
    }
    public static int maxArray(int[] a) {
        int max = a[0]; //first element
        for (int i = 1; i < a.length; i++) {
            if(a[i]>max){
                max=a[i];
            }
        }
        return max;
    }

    public static int[] getIntegerArray(int n) {
        int[] newArray = new int[n];
        for (int i = 0; i < n; i++) {
            newArray[i] = random(9999);
        }
        return newArray;
    }


    public static int[] copyArray(int[] a) {
        int n = a.length;
        int[] newArray = new int[n];
        for (int i = 0; i < n; i++) {
            newArray[i] = a[i];
        }
        return newArray;
    }

    public static String show(int[] a, int n) {
        String result="";
        for (int i = 0; i < n; i++) {
            result+=a[i]+" ";
        }
        return result;
    }

    public static char getLetters() {
        char[] letters = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z'
        };
        return letters[random(letters.length - 1)];
    }

    public static String getName() {
        String[] names = {
                "Alana", "Pablo", "Ana", "María", "Victoria", "Nicole",
                "Mateo", "Fabiana", "Natalia", "Valeria",
                "Luis", "Elena", "Raúl", "César", "Lucas",
                "Clara", "Diego", "Sara", "Iván", "Julia",
                "David", "Noa", "Bruno", "Emma", "Luz",
                "Gael", "Iris", "Hugo", "Vera", "Leo"
        };
        return names[random(names.length-1)];
    }
}
