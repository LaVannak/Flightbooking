/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightbooking;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author j250519
 */
public class Flightbooking {

    /**
     * @param args the command line arguments
     */
    private static customer[][] Seats = new customer[20][6];
    private static List<String> bookedSeatsList = new ArrayList<String>();
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
//  
//        Seats[0][0] = new customer("A","0", "M", 19);
//        Seats[0][1] = new customer("A","1", "M", 17);
//        Seats[0][2] = new customer("A","2", "M", 15);
//        Seats[1][0] = new customer("B","0", "M", 50); 
//        
//        Seats[1][3] = new customer("B","0", "M", 50);

        loadBookedRecFromfile();
        
        
        Scanner reader = new Scanner(System.in);
        char answer = 'Y';
        
        
        while(answer =='Y' || answer =='y'){
            System.out.println("*****************************************************");
            System.out.println("*************Welcome to My Flight Booking************");
            System.out.println("*****************************************************");
            System.out.println();
            System.out.println();
            System.out.println("Please choise one of the following option:");
            System.out.println("1- Booking, 2-View Seat 3-Search");

            int Opt = reader.nextInt();
            switch(Opt){
                case 1:
                    
                    System.out.println("First Name  :");
                    String f_name=reader.next();
                    
                    System.out.println("Last Name   :");
                    String l_name=reader.next();
                    
                    System.out.println("Sex         :");
                    String sex= reader.next();
                    
                    System.out.println("Age         :");
                    int age = reader.nextInt();
                    boolean added=false;
                   // BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
                    while(!added){
                        System.out.println("Select Seat");
                        String Seat= reader.next();
                        added=Booked(Seat,new customer(f_name, l_name,sex, age));
                        saveBookingRecords();
                        ViewSeat();
                    }
                    break;
                    
                case 2:
                    ViewSeat();
                    break;
                case 3:
 
                    
                    break;
                case 9:
                    Seats = new customer[20][6];
                    
                    saveBookingRecords();
                    break;
                default:
                    System.out.println("Selection Not found");
            }
            System.out.print("Do you wish to countinue [Y/y]?");
            answer = reader.next().charAt(0);
        }    

    }
    
    private static void ViewSeat(){
        String seat ="*";
        System.out.println("    A B C   D E F");
        for(int i=0; i<20; i++){
            //Classify The Class
            if(i==0){
                System.out.println("-----------------");
                System.out.println("    Business");
                System.out.println("-----------------");
            }
            if(i==9){
                System.out.println("-----------------");
                System.out.println("    Ecomonic");
                System.out.println("-----------------");
            }
                        
            //This condition is to aliagn the culum properly
            if(i <9)System.out.print((i+1)+" ");
            else System.out.print((i+1)+"");
            //-------------------------------------


            
            for(int j=0; j<6; j++){

                if(Seats[i][j]==null){
                    //Making Space between culum ABC and DEF
                    seat = (j%3==0)? "  * ": "* ";
                    System.out.print(seat);
                }
                    
                else {
                    //Making Space between culum ABC and DEF
                    seat = (j%3==0)? "  "+ Seats[i][j].getClassify()+" ": Seats[i][j].getClassify()+" ";
                    System.out.print(seat);
                }                
            }
            
            System.out.println();
        }
    }
    
    private static boolean Booked(String SN, customer cust ){
        boolean booked=false;
        int Row= Integer.parseInt(SN.substring(1));
        String Col= SN.substring(0,1);
        int j=convertCharToIndex(Col);

        if(j<=Seats[0].length && Row<= Seats.length){
            if(Seats[Row-1][j]==null){
                Seats[Row-1][j]= cust;
                   booked=true;    
                }else{
                    System.out.println("Seat is not valible");
                    booked=false;    
                }
            }
        return booked;
    }
    private static String convertIndexToChar(int ind){
        if(ind==0)return "A";
        else if(ind==1) return "B";
        else if(ind==2) return "C";
        else if(ind==3) return "D";
        else if(ind==4) return "E";
        else if(ind==5) return "F";
        else return "Z";
    }
    private static int convertCharToIndex(String Chr){
        int Ind;
        
        if(Chr.equals("A")| Chr.equals("a"))Ind=0;
        else if(Chr.equals("B")| Chr.equals("b"))Ind=1;
        else if(Chr.equals("C")| Chr.equals("c"))Ind=2;
        else if(Chr.equals("D")| Chr.equals("d"))Ind=3;
        else if(Chr.equals("E")| Chr.equals("e"))Ind=4;
        else if(Chr.equals("F")| Chr.equals("f"))Ind=5;
        else Ind=10;
        return Ind;  
    }
    private static void saveBookingRecords() throws IOException{
        BufferedWriter booking=null;
        booking = new BufferedWriter(new FileWriter("Booking.txt"));
        bookedSeatsList.clear();
        
        // Save to Booking Record.

        for(int i=0; i<Seats.length; i++){                 
           for(int j=0; j<Seats[i].length;j++){                                
                String CharCol = convertIndexToChar(j);
                String bookedSeatNum;
                if(Seats[i][j] !=null){
                    String seatNum =CharCol +(i+1);
                    bookedSeatNum = seatNum+ "-"+ Seats[i][j].getFirst_Name()+";"+ 
                            Seats[i][j].getLast_Name()+";"+ Seats[i][j].getGender()+";"+
                            Seats[i][j].getAge();  
                    //Write to File
                    booking.write(bookedSeatNum);
                    booking.newLine();
    
                    
                }
                            
            }                    
        }
        //Close the file to Save the records
        booking.close();        
    }
    
    private static void loadBookedRecFromfile() throws FileNotFoundException{
        Scanner inF = new Scanner(new File("Booking.txt"));
        String line;
        
        while(inF.hasNext()){
            line= inF.nextLine();
            String temp[] =line.split("-");
            if(temp.length >1){
                
                // Slipt the Customer info
                String customerInfo[]= temp[1].split(";");
                
                // Assign Seat Number to Customer
                Booked(temp[0], new customer(customerInfo[0],customerInfo[1],
                        customerInfo[2],Integer.parseInt(customerInfo[3])));              
            }
            
        }
    }
 
}
