package uebung1;

public class aufgabe4 {
    private int i = 0;
    public int add(int j){
        i += j;
        return i;
    }
    public static void main(String[] args) {
        aufgabe4 a = new aufgabe4();
        System.out.println(a.add(1));  //output:1
        System.out.println(a.add(1));  //output:2
    }
}
