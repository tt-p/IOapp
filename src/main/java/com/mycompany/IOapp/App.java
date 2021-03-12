package com.mycompany.IOapp;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Basic Input Output Program
 */

public class App {

    static String filename = "transactions.txt";
    static int limitRecord = 100;
    static Record[] records;
    static int lastIndex;

    static class Record {
        String name;
        Integer price, number;
    }

    public static void main(String[] args) {

        try{

            File file = new File(filename);
            if(!file.exists()) {
                if(!file.createNewFile())
                    throw new IOException();
            }
        }catch (IOException e){
            System.out.println("File does not exist and it could not created!");
            System.out.println("Please create a file named \" transactions.txt \"");
            System.out.println("And then restart the app.");
            System.exit(-1);
        }

        initialProcess();

        while(true) {  //  This is the main loop can only exit from switch(6) -> close option.

            // Prints the menu.
            System.out.println(" ____________________________________");
            System.out.println("|______________| MENU |______________|");
            System.out.println("|                                    |");
            System.out.println("|          1- ADD RECORD             |");
            System.out.println("|          2- REMOVE RECORD          |");
            System.out.println("|          3- SEARCH RECORD          |");
            System.out.println("|          4- LIST ALL               |");
            System.out.println("|          5- DELETE ALL             |");
            System.out.println("|          6- CLOSE                  |");
            System.out.println("|____________________________________|");

            Scanner scnInt;  //  Scanner obj. for Integer input. Assigned in try-catch blocks.
            Scanner scnStr = new Scanner(System.in);  //  Scanner obj. for String input.
            int inp;                                  //  This variable holds the users choice.

            while (true){  //  To guarantee the right menu choice.
                try{
                    scnInt = new Scanner(System.in);
                    //  Scanner obj. assigned for Menu input.

                    System.out.print("\nPlease make a choice (1-6) : ");
                    inp = scnInt.nextInt();

                    if( 0 < inp && inp < 7 ) break;
                    else throw new InputMismatchException();
                    //  If user choice is not in the menu it throws exception.

                }catch (InputMismatchException e){
                        System.out.println("Invalid entry!");
                }
            }

            //  Variables for holding the record properties.
            String name;
            Integer price, number;

            switch (inp){

                //  ADD RECORD -----------------------------------------------------------------------------------
                case 1:
                    //  If lastIndex equals to limit record then adding more record will cause an error.
                    if(lastIndex == limitRecord) {
                        System.out.println("You cannot add any items. Record limit is " + limitRecord);
                        break;
                    }
                    //  Printing for user interface, asking for inputs.
                    System.out.print("\n-> Please specify the properties of your record.\n");

                    System.out.print("\nEnter the name of the record : ");

                    name = scnStr.nextLine().trim();
                    //  name cannot contain spaces on both ends.

                    //  Loop for guarantee the appropriate 'price'.
                    while(true) {
                        try{
                            scnInt = new Scanner(System.in);
                            System.out.print("\nEnter the price of the record : ");
                            price = scnInt.nextInt();

                            if(price >= 0) break;
                            else throw new InputMismatchException();
                            //  If user choice is not appropriate it throws exception.

                        }catch (InputMismatchException e){
                            System.out.println("Invalid entry!");
                        }
                    }

                    //  Loop for guarantee the appropriate 'number'.
                    while(true) {
                        try{
                            scnInt = new Scanner(System.in);
                            System.out.print("\nEnter the number of the record : ");
                            number = scnInt.nextInt();

                            if(number >= 0) break;
                            else throw new InputMismatchException();
                            //  If user choice is not appropriate it throws exception.

                        }catch (InputMismatchException e){
                                System.out.println("Invalid entry!");
                        }
                    }

                    addRecord(name, price, number);
                    update();
                    System.out.println("\nRecord is added.");

                    break;  //  Breaks the switch/case.

                //  REMOVE RECORD --------------------------------------------------------------------------------
                case 2:
                    System.out.println("\nPlease enter the name of the record you want to remove : ");
                    name = scnStr.nextLine().trim();
                    //  name cannot contain spaces on both ends.

                    //  Calls removeRecord and checks if its removed.
                    if(removeRecord(name)){
                        System.out.println("Record is removed.");
                        update();
                    }else{
                        System.out.println("Record is not available.");
                    }
                    break;  //  Breaks the switch/case.

                // SEARCH RECORD ---------------------------------------------------------------------------------
                case 3:
                    System.out.println("\nPlease enter the name of the record you want to search : ");

                    name = scnStr.nextLine().trim();
                    //  name cannot contain spaces on both ends.
                    searchRecord(name);
                    break;  //  Breaks the switch/case.

                //  LIST ALL -------------------------------------------------------------------------------------
                case 4:
                    System.out.println();  //  For jumping the next line.
                    listRecord();
                    break;  //  Breaks the switch/case.

                //  DELETE ALL -----------------------------------------------------------------------------------
                case 5:
                    System.out.println();  //  For jumping the next line.

                    //  Loop for guarantee the right choice.
                    while (true) {
                        char ans;
                        System.out.println("Are you sure you want to delete all records ? (y/n)");
                        ans = scnStr.next().charAt(0);
                        //  This gets input as a first character of the string.

                        if (ans == 'y') {
                            deleteAll();
                            update();
                            System.out.println("All records are deleted.");
                            break;
                        } else if (ans == 'n') {
                            System.out.println("No action performed.");
                            break;
                        } else {
                            System.out.println("Invalid entry!");
                        }
                    }
                    break;  //  Breaks the switch/case.

                case 6:
                    System.out.println();  //  For jumping the next line.
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;  //  Breaks the switch/case.

                default:
                    System.out.println("ERROR!");  //  Normally this statement cannot be run.
                    System.out.println("Please restart the app.");
                    System.exit(1);         //  app needs to restart.
            }
        }
    }

    private static void listRecord() {

        int i = 0;             //  counter for loop also holds the number of records.
        while(i < lastIndex){  //  Loop for roving in the array.

            if (i == 0) System.out.println("Name" +" "+ "Price"  +" "+ "Number");
            //  Prints the head of list

            System.out.println(records[i].name +"\t"+ records[i].price +"\t"+ records[i].number);
            // Prints the properties of record.

            i++;
        }

        if(i == 0) System.out.println("No records available.");
        // If i = 0 that means there is no record.
    }

    private static void addRecord(String name, Integer price, Integer number) {

        records[lastIndex].name = name;
        records[lastIndex].price = price;
        records[lastIndex].number = number;
        lastIndex++;
    }

    private static void update() {
        /*
        -> This method may throw some exceptions related to file process.
        -> In my opinion best way to handle these exceptions is recalling the update method.
        -> Recalling will refresh the app by creating new file or deleting the old file
        and then creating a new one.
         */

        String output;  //  I'll use this string for writing records to txt file.

        try {           //  Try-catch block for IO exceptions.

            File file = new File(filename);

            if (file.exists()) {            //  Checks if the file exists.
                if(!file.delete()){         //  If file exists deletes it.
                    throw new Exception();  //  Throws exception if file could not deleted.
                }
            }

            if (!file.createNewFile()) {    //  Creates new file.
                throw new Exception();      //  Throws exception if file could not created.
            }

            PrintWriter pW = new PrintWriter(file);
            // printWriter object for editing the txt file.

            for (int i = 0; i < lastIndex; i++) {  //  Loop for roving in the array.

                output = records[i].name + "\t" + records[i].price + "\t" + records[i].number;
                pW.println(output.trim());  //  trims the both ends of the output before writes it.
            }
            pW.close();  // Closing the printWriter.

        }catch (Exception e) {
            update();
        }

    }

    private static boolean removeRecord(String name) {

        for (int i = 0; i < limitRecord; i++){  //  Loop for searching.

            if (name.equals(records[i].name)){  //  Statement for name comparison.

                records[i] = new Record();      //  Removing record.
                lastIndex--;                    //  Decreasing the lastIndex.

                //  -> If the last record is removed then mo need to shift the records.
                //  -> If it isn't the last record then the loop at the down will
                //   shift the records to fill the gap of removed one.

                if(i != limitRecord - 1){

                    //  This loop shifts every record to previous index.
                    for(int j = i + 1; j < limitRecord; j++)
                        records[j - 1] = records[j];

                    //  This statement deletes the last record which duplicated in the loop.
                    records[limitRecord - 1] = new Record();
                }
                return true;
            }
        }
        return false;
    }

    private static void searchRecord(String name) {

        int i;  // I need to use this variable out of brackets.

        for (i = 0; i < lastIndex; i++){      //  Loop for search.

            if (name.equals(records[i].name)){  //  Statement for name compare.

                System.out.println("\n" + records[i].name +"\t"+ records[i].price +"\t"+ records[i].number);
                // Prints the properties of record.

                break;
            }
        }

        if(i == lastIndex) System.out.println("Record is not available.");
        // If loop is complete then i = lastIndex. That means there is no record.
    }

    private static void deleteAll() {

        lastIndex = 0;
        //  Changing lastIndex because no records will be left.
        records = new Record[limitRecord];  //  Creating new array.

        for (int i = 0; i < limitRecord; records[i++] = new Record());
        // After this method hole array has empty record.
    }

    private static void initialProcess() {

        records = new Record[limitRecord];
        for (int i = 0; i < limitRecord; i++) {
            records[i]=new Record();
        }

        try {
            Reader reader = new InputStreamReader(new FileInputStream(filename), "windows-1254");
            BufferedReader br = new BufferedReader(reader);
            String strLine;

            int i = 0;
            while((strLine = br.readLine())!= null) {
                StringTokenizer tokens = new StringTokenizer(strLine,"\t");
                String [] t = new String[3];

                int j = 0;
                while (tokens.hasMoreTokens()) {
                    t[j] = tokens.nextToken();
                    j++;
                }

                records[i].name =t[0];
                records[i].price = Integer.valueOf(t[1]);
                records[i].number = Integer.valueOf(t[2]);
                i++;
            }

            lastIndex = i;
            reader.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}