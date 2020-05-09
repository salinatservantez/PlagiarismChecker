/**
 * Description: This program checks source code for possible plagiarism by reading in two source files,
 * compare them for similarity and publish a score on how similar they are.
 *
 * @author Salina Servantez
 * @edu.uwp.cs.340.section001
 *  * @edu.uwp.cs.340.assignment4-PlagiarismChecker
 *  * @bugs none
 */

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;


public class PlagiarismChecker {

    /**
     * This method calculates the lcs_length of the two programs
     * @param prog1
     * @param prog2
     * @return
     */
    public static int lcsLength( String prog1, String prog2)
    {
        int m = prog1.length();
        int n = prog2.length();
        int previous;
        int current = 0;

        int L[][] = new int[2][n+1];

        for (int i=0; i<=m; i++)
        {
            for (int j=0; j<=n; j++)
            {
                if(i % 2 == 0){
                    previous = 1;
                    current = 0;
                } else {
                    previous = 0;
                    current = 1;
                }
                if (i == 0 || j == 0)
                    L[current][j] = 0;
                else if (prog1.charAt(i-1) == prog2.charAt(j-1))
                    L[current][j] = L[previous][j-1] + 1;
                else
                    L[current][j] = max(L[previous][j], L[current][j-1]);
            }
        }
        return L[current][n];
    }

    /**
     * finds max
     * @param a
     * @param b
     * @return max int
     */
    public static int max(int a, int b){
        return (a > b) ? a : b;
    }

    /**
     * his method uses lcsLength()to calculate the plagiarism score for the two files
     * @param filename1
     * @param filename2
     * @return plagiarism score
     */
    public static double plagiarismScore(String filename1, String filename2){
        double score = 0;
        String contents1;
        String contents2;
        contents1 = readFile(filename1);
        contents2 = readFile(filename2);
        score = 200 * lcsLength(contents1, contents2) / (double)(contents1.length() + contents2.length());

        return score;
    }

    /**
     *This method does a pairwise comparison of all the files in the array and prints a neatly formatted report listing any suspicious pairs.
     * The report includes their plagiarism score
     * @param filenames
     * @param threshold
     */
    public static void plagiarismChecker(String[] filenames, double threshold){
        if (filenames.length < 2){
            System.out.println("Not enough files to test");
            return;
        }
        for(int i = 0;i < filenames.length - 1; i++){
            for (int j = i + 1; j < filenames.length; j++){
                double score = plagiarismScore(filenames[i], filenames[j]);
                if (score >= threshold){
                    System.out.println("Suspicious pair found: " + filenames[i] + " and " + filenames[j]);
                    System.out.println("Score: " + score);
                }
            }
        }
    }

    /**
     * main method
     * @param args
     */
    public static void main(String[] args){
        ArrayList<String> files = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        String filename;
        double threshhold = 0;

        while(true){
            System.out.print("Enter a file ( enter \"no\" to stop ): ");
            filename = input.nextLine();
            if (filename.equals("no"))
                break;
            files.add(filename);
        }

        String[] files1 = new String[files.size()];

        System.out.print("Enter the threshold: ");
        threshhold = input.nextDouble();
        files.toArray(files1);
        plagiarismChecker( files1, threshhold);
    }

    /**
     * reads file
     * @param fileName
     * @return contents
     */
    public static String readFile(String fileName) { String contents = "";
        try {
            RandomAccessFile fin = new RandomAccessFile( new File(fileName), "r");
            int b = fin.read();
            while (b != -1) {
                contents = contents + (char) b;
                b = fin.read();
            }
        } catch (Exception e) {
            System.err.println("Trouble reading from: " + fileName);
        }
        return contents;
    }
}
