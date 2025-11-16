package uebung1;


public class IterativeFibonacci {
    private int a[];
    private int n;
    public IterativeFibonacci(int n){
        this.n = n;
        a = new int[n];
        fibonacci();
    }
    public void fibonacci(){
        for(int i=0; i<n; i++){
            if(i == 0){
                a[i] = 0;
            }
            else if(i == 1){
                a[i] = 1;
            }
            else{
                a[i] = a[i-1] + a[i-2];
            }
        }
    }
    public String toString(){
        String s = "";
        for (int i = 0; i < a.length; i++) {
            s += a[i];
            s += (i != a.length-1)? ", ":".";
        }
        return s;
    }

    public int getFibonacci(){
        return a[n-1];
    }
    public static void main(String[] args) {
        IterativeFibonacci fib = new IterativeFibonacci(10);
        System.out.println(fib);
        System.out.println(fib.getFibonacci());
    }
}
