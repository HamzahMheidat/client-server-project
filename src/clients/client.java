package clients;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class client {

    /**
     * @throws java.io.IOException
     */
    static ObjectOutputStream out_M_A;
    static ObjectOutputStream out_M_B;
    static ObjectInputStream in_C_Part1;
    static ObjectInputStream in_C_Part2;
    static ObjectInputStream in_C_Part3;
    static ObjectInputStream in_C_Part4;
    public static int rowOfA, ColOFArowB, colOFB;
    static int A_Matrix[][];
    static int B_Matrix[][];
    static int C_Matrix[][];

    static int C_Matrix_part1[][];
    static int C_Matrix_part2[][];
    static int C_Matrix_part3[][];
    static int C_Matrix_part4[][];

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // TODO code application logic here

        getSize();//generate random size of matrix
        A_Matrix = new int[rowOfA][ColOFArowB];
        B_Matrix = new int[ColOFArowB][colOFB];
        C_Matrix = new int[rowOfA][colOFB];

        System.out.println("client.main()");

        //To fillMatrix(A_Matrix)
        new Thread(
                () -> {

                    System.out.println("The Matrix A is \n" + printMatrix(fillMatrix(A_Matrix)));

                }).start();

        //To fillMatrix(A_Matrix)
        new Thread(
                () -> {

                    System.out.println("The Matrix B is \n" + printMatrix(fillMatrix(B_Matrix)));
                }).start();

        try (Socket socket = new Socket("localhost", 5555)) {

            //Start Time
            Instant start = Instant.now();
            out_M_A = new ObjectOutputStream(socket.getOutputStream());
            out_M_B = new ObjectOutputStream(socket.getOutputStream());
            out_M_A.writeObject(A_Matrix);
            out_M_B.writeObject(B_Matrix);

            //Part1
            in_C_Part1 = new ObjectInputStream(socket.getInputStream());
            System.out.println("Part 1 of C");
            C_Matrix_part1 = (int[][]) in_C_Part1.readObject();
            System.out.println(printMatrix(C_Matrix_part1));

            //Part2
            in_C_Part2 = new ObjectInputStream(socket.getInputStream());
            System.out.println("Part 2 of C");
            C_Matrix_part2 = (int[][]) in_C_Part2.readObject();
            System.out.println(printMatrix(C_Matrix_part2));

            //Part3
            in_C_Part3 = new ObjectInputStream(socket.getInputStream());
            System.out.println("Part 3 of C");
            C_Matrix_part3 = (int[][]) in_C_Part3.readObject();
            System.out.println(printMatrix(C_Matrix_part3));

            //Part4
            in_C_Part4 = new ObjectInputStream(socket.getInputStream());
            System.out.println("Part 4 of C");
            C_Matrix_part4 = (int[][]) in_C_Part4.readObject();
            System.out.println(printMatrix(C_Matrix_part4));

            System.out.println("Join All C Matrix");
            C_Matrix = Join(C_Matrix_part1, C_Matrix_part2, C_Matrix_part3, C_Matrix_part4);

            //End Time
            Instant finish = Instant.now();
            System.out.println("C Itself ");
            System.out.println(printMatrix(C_Matrix));

            long timeElapsed = Duration.between(start, finish).toMillis();

            System.out.println("The total time needed is -> " + timeElapsed + " milliseconds ");

        }
    }

    public static int[][] fillMatrix(int[][] a) {
        Random rand = new Random();
        for (int[] a1 : a) {
            for (int col = 0; col < a1.length; col++) {
                a1[col] = rand.nextInt(100) + 1;
            }
        }
        return a;
    }

    public static String printMatrix(int x[][]) {
        String line = "";
        for (int[] x1 : x) {
            for (int col = 0; col < x1.length; col++) {
                line += x1[col] + "  ";
            }

            line += "\n";
        }
        return line;
    }

    public static int[][] Join(int[][] P1, int[][] P2, int[][] P3, int[][] P4) {
        int m1[][] = new int[P1.length + P2.length][];
        System.arraycopy(P1, 0, m1, 0, P1.length);
        System.arraycopy(P2, 0, m1, P1.length, P2.length);

        int m2[][] = new int[P3.length + P4.length][];
        System.arraycopy(P3, 0, m2, 0, P3.length);
        System.arraycopy(P4, 0, m2, P3.length, P4.length);

        int mall[][] = new int[m1.length + m2.length][];
        System.arraycopy(m1, 0, mall, 0, m1.length);
        System.arraycopy(m2, 0, mall, m1.length, m2.length);

        return mall;
    }

    public static void getSize() {
        Random rand = new Random();
        rowOfA = rand.nextInt(50) + 40; //40-90(50+40) min -> 40 max -> 90 change if U want
        ColOFArowB = rand.nextInt(50) + 40;
        colOFB = rand.nextInt(50) + 40;

    }

}
