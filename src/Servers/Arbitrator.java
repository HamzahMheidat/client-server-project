package Servers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class Arbitrator implements Runnable {

    static ServerSocket server;
    static Socket client;
    static int counter = 1;
    static int ID = 0;
    static Thread T;
    static ObjectInputStream in_A_Matrix, in_B_Matrix;
    static int A_Matrix[][], B_Matrix[][], C_Matrix_Part1[][], C_Matrix_Part2[][], C_Matrix_Part3[][], C_Matrix_Part4[][];
    static int Row_parts;
    static int Extra_Row;
    static int part4;
    static int ColOFArowB;

    static Socket ToServer1, ToServer2, ToServer3, ToServer4;

    //Server1 needed
    static ObjectInputStream in_C_frome_S1;
    static ObjectOutputStream out_Part1_to_S1, out_B_ToS1, out_C_Part1_ot_c;
    //Server2 needed
    static ObjectInputStream in_C_frome_S2;
    static ObjectOutputStream out_Part2_to_S2, out_B_ToS2, out_C_ot_Part2_c;
    //Server3 needed
    static ObjectInputStream in_C_frome_S3;
    static ObjectOutputStream out_Part3_to_S3, out_B_ToS3, out_C_ot_Part3_c;
    //Server4 needed
    static ObjectInputStream in_C_frome_S4;
    static ObjectOutputStream out_Part4_to_S4, out_B_ToS4, out_C_ot_Part4_c;

    static int A_part1[][];
    static int A_part2[][];
    static int A_part3[][];
    static int A_part4[][];

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // TODO code application logic here
        System.out.println("Arbitrator.main()");

        server = new ServerSocket(5555);

        while (counter < 5)//change number to more client
        {
            T = new Thread(new Arbitrator(), "Thread #" + ID);//Thread #ID is Client ID 
            T.run();
        }

    }

    public Arbitrator() throws IOException {
        client = server.accept();
        ToServer1 = new Socket("localhost", 1111);
        ToServer2 = new Socket("localhost", 2222);
        ToServer3 = new Socket("localhost", 3333);
        ToServer4 = new Socket("localhost", 4444);

        ID++;
    }

    @Override
    synchronized public void run() {
        try {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

            in_A_Matrix = new ObjectInputStream(client.getInputStream());
            in_B_Matrix = new ObjectInputStream(client.getInputStream());

            A_Matrix = (int[][]) in_A_Matrix.readObject();
            System.out.println("Matrix A values from client :" + counter + " are :\n \n" + printMatrix(A_Matrix));

            B_Matrix = (int[][]) in_B_Matrix.readObject();
            System.out.println("Matrix B values from client :" + counter + " are :\n \n" + printMatrix(B_Matrix));

            Row_parts = (int) A_Matrix.length / 4;
            Extra_Row = (int) A_Matrix.length % 4;
            part4 = Row_parts + Extra_Row;
            ColOFArowB = B_Matrix.length;

            A_part1 = new int[Row_parts][ColOFArowB];
            A_part2 = new int[Row_parts][ColOFArowB];
            A_part3 = new int[Row_parts][ColOFArowB];
            A_part4 = new int[part4][ColOFArowB];

            // System.out.println("\n\nA row "+A_Matrix.length+" A colom "+ColOFArowB +" parts is "+Row_parts+" extra is "+Extra_Row+"\n\n\n");
            SplitTheMatrix(A_Matrix);

            /* To test
              System.out.println("Matrix A part 1  are :\n \n" + printMatrix(A_part1)+"\n\n");
              
              System.out.println("Matrix A part 2  are :\n \n" + printMatrix(A_part2)+"\n\n");
               
              System.out.println("Matrix A part 3  are :\n \n" + printMatrix(A_part3)+"\n\n");
              
              System.out.println("Matrix A part 4  are :\n \n" + printMatrix(A_part4)+"\n\n");
              
             */
            server1_Business();
            server2_Business();
            server3_Business();
            server4_Business();

            counter++;//# of Thread == Client

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Arbitrator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void server1_Business() throws IOException, ClassNotFoundException {

        //Server1
        out_Part1_to_S1 = new ObjectOutputStream(ToServer1.getOutputStream());
        out_B_ToS1 = new ObjectOutputStream(ToServer1.getOutputStream());
        in_C_frome_S1 = new ObjectInputStream(ToServer1.getInputStream());
        out_C_Part1_ot_c = new ObjectOutputStream(client.getOutputStream());
       

        out_Part1_to_S1.writeObject(A_part1);
        out_B_ToS1.writeObject(B_Matrix);
        C_Matrix_Part1 = (int[][]) in_C_frome_S1.readObject();
        System.out.println(printMatrix(C_Matrix_Part1));
        out_C_Part1_ot_c.writeObject(C_Matrix_Part1);

    }

    public static void server2_Business() throws IOException, ClassNotFoundException {

        //Server2
        out_Part2_to_S2 = new ObjectOutputStream(ToServer2.getOutputStream());
        out_B_ToS2 = new ObjectOutputStream(ToServer2.getOutputStream());
        in_C_frome_S2 = new ObjectInputStream(ToServer2.getInputStream());
        out_C_ot_Part2_c = new ObjectOutputStream(client.getOutputStream());
       

        out_Part2_to_S2.writeObject(A_part2);
        out_B_ToS2.writeObject(B_Matrix);
        C_Matrix_Part2 = (int[][]) in_C_frome_S2.readObject();
        System.out.println(printMatrix(C_Matrix_Part2));
        out_C_ot_Part2_c.writeObject(C_Matrix_Part2);

    }

    public static void server3_Business() throws IOException, ClassNotFoundException {

        //Server3
        out_Part3_to_S3 = new ObjectOutputStream(ToServer3.getOutputStream());
        out_B_ToS3 = new ObjectOutputStream(ToServer3.getOutputStream());
        in_C_frome_S3 = new ObjectInputStream(ToServer3.getInputStream());
        out_C_ot_Part3_c = new ObjectOutputStream(client.getOutputStream());
       

        out_Part3_to_S3.writeObject(A_part3);
        out_B_ToS3.writeObject(B_Matrix);
        C_Matrix_Part3 = (int[][]) in_C_frome_S3.readObject();
        System.out.println(printMatrix(C_Matrix_Part3));
        out_C_ot_Part3_c.writeObject(C_Matrix_Part3);

    }

    public static void server4_Business() throws IOException, ClassNotFoundException {

        //Server4
        out_Part4_to_S4 = new ObjectOutputStream(ToServer4.getOutputStream());
        out_B_ToS4 = new ObjectOutputStream(ToServer4.getOutputStream());
        in_C_frome_S4 = new ObjectInputStream(ToServer4.getInputStream());
        out_C_ot_Part4_c = new ObjectOutputStream(client.getOutputStream());
       

        out_Part4_to_S4.writeObject(A_part4);
        out_B_ToS4.writeObject(B_Matrix);
        C_Matrix_Part4 = (int[][]) in_C_frome_S4.readObject();
        System.out.println(printMatrix(C_Matrix_Part4));
        out_C_ot_Part4_c.writeObject(C_Matrix_Part4);

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

    public static void SplitTheMatrix(int[][] mrix) {

        int Row_parts = (int) A_Matrix.length / 4;

        for (int i = 0; i < Row_parts; i++) {
            System.arraycopy(A_Matrix[i], 0, A_part1[i], 0, A_Matrix[0].length);

        }

        int x = 0;
        for (int i = Row_parts; i < Row_parts * 2; i++) {
            System.arraycopy(A_Matrix[i], 0, A_part2[x], 0, A_Matrix[0].length);
            x++;

        }

        int y = 0;
        for (int i = Row_parts * 2; i < Row_parts * 3; i++) {
            System.arraycopy(A_Matrix[i], 0, A_part3[y], 0, A_Matrix[0].length);
            y++;
        }

        int z = 0;
        for (int B = Row_parts * 3; B < A_Matrix.length; B++) {
            System.arraycopy(A_Matrix[B], 0, A_part4[z], 0, A_Matrix[0].length);
            z++;

        }

    }

}
