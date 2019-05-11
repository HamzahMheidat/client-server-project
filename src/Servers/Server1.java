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
public class Server1 implements Runnable {

    static int counter = 1;
    static int ID = 0;
    static Thread T;
    static ServerSocket server;
    static Socket client;
    static ObjectInputStream in_A_Matrix, in_B_Matrix;
    static ObjectOutputStream out_C;
    static int A_Matrix[][], B_Matrix[][], C_Matrix[][];

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // TODO code application logic here
        System.out.println("Server1.main()");

        server = new ServerSocket(1111);

        while (counter < 5) {//change number to more request
            T = new Thread(new Server1(), "Thread #" + ID);// request number
            T.run();
        }

    }

    public Server1() throws IOException {
        client = server.accept();
        ID++;
    }

    @Override
    synchronized public void run() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

       

        try {
            in_A_Matrix = new ObjectInputStream(client.getInputStream());
            in_B_Matrix = new ObjectInputStream(client.getInputStream());
            out_C = new ObjectOutputStream(client.getOutputStream());
            A_Matrix = (int[][]) in_A_Matrix.readObject();
            System.out.println("Matrix A Part 1 values from Arbitrator : are :\n \n" + printMatrix(A_Matrix));
            B_Matrix = (int[][]) in_B_Matrix.readObject();
            System.out.println("Matrix B values from Arbitrator  are :\n \n" + printMatrix(B_Matrix));

            C_Matrix = multiplication(A_Matrix, B_Matrix);

            out_C.writeObject(C_Matrix);

            counter++;

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Server1.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    public static int[][] multiplication(int[][] a, int[][] b) {
        int[][] result = new int[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < a[0].length; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        return result;
    }

}
