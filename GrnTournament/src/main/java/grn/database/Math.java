package grn.database;

public class Math {

    public static int factorial (int n) {
        int f = 1;
        for (int i=2; i<=n; i++)
            f *=i;
        return f;
    }
}
