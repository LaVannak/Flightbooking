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
import java.io.RandomAccessFile;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.nashorn.internal.objects.NativeString.trim;
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
    private static List<String> bookedList = new ArrayList<String>();
    private static int Row;
    private static int Col;
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
  
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
                    String f_name=(reader.next()).toUpperCase();
                    
                    System.out.println("Last Name   :");
                    String l_name=(reader.next()).toUpperCase();
                    
                    System.out.println("Sex         :");
                    String sex= (reader.next()).toUpperCase();
                    
                    System.out.println("Age         :");
                    int age = reader.nextInt();
                    boolean added=false;
                   // BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
                    while(!added){
                        System.out.println("Select Seat");
                        String Seat= (reader.next()).toUpperCase();
                        added=Booked(Seat,new customer(f_name, l_name,sex, age));
                        String seatDetail = Seat+"-"+f_name+";" +l_name+";"+sex+";"+ age;
                        saveBookingRecords(Seat,seatDetail);
                        ViewSeat();
                        PrintReciept(f_name, l_name, sex, age, Seat);
                        
                    }
                    break;
                    
                case 2:
                    ViewSeat();
                    break;
                case 3:
                    Scanner input = new Scanner(System.in);
                    System.out.print("Enter the Name : ");
                    String searchName = (input.nextLine()).toUpperCase();
                    String foundSeatNo= searchBinaryName(searchName);
                    if(foundSeatNo.equals("")){
                        System.out.println("Records Not found");
                    }else{
                        System.out.println(searchName +" @ "+ foundSeatNo);
                        System.out.println("Any Key - To Continue; X- To Cancel Booking");
                        String ans = input.next();
                        if(ans.equals("X")||ans.equals("x")){
                            cancelBooking(foundSeatNo);
                        }
                    }
                    break;
                    
                case 9:
                    Seats = new customer[20][6];

                    for(int i=0; i<Seats.length; i++){                 
                        for(int j=0; j<Seats[i].length;j++){                                
                            String SeatNo = convertIndexToChar(j)+(i+1);
                            saveBookingRecords(SeatNo, SeatNo+"-*");
                        }
                     }    
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
    
    private static void getRowCol(String SeatNo){
        Row= Integer.parseInt(SeatNo.substring(1))-1;
        String StrCol= SeatNo.substring(0,1);
        Col=convertCharToIndex(StrCol);
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
    private static void saveBookingRecords(String SeatNo, String SeatDetail) throws IOException{
        
        getRowCol(SeatNo);
        //Find Position
        int position = (Row*6*50)+(Col*50)+10;
        System.out.println(position);
        String empty=" ";
        RandomAccessFile raf = new RandomAccessFile("seatbooking.dat", "rw");
        raf.seek(position);
        raf.write(SeatDetail.getBytes());
        //To clear thing behind the text
	for(int i =0; i<50-SeatDetail.length(); i++){
           raf.write(empty.getBytes()); 
        }	
        raf.close();
    }

    private static void loadBookedRecFromfile() throws IOException  {
        int position=10;
        
        for(int i=0; i<119; i++){
            String line="";
            line = readBytes("seatbooking.dat",position,50); 

            String temp[] =line.split("-");
                // Slipt the Customer info
                if(trim(temp[1]).length()>1){
                String customerInfo[]= temp[1].split(";");
                // Assign Seat Number to Customer
               Booked(temp[0], new customer(customerInfo[0],customerInfo[1],
                       customerInfo[2],Integer.parseInt(trim(customerInfo[3])))); 
                }
            position +=50;
        }
    }

    private static boolean Booked(String SeatNo,  customer cust ){
        boolean booked=false;
        getRowCol(SeatNo);
        
        if(Col<=Seats[0].length && Row<= Seats.length){
            if(Seats[Row][Col]==null){
                Seats[Row][Col]= cust;
                   booked=true;    
                }else{
                    System.out.println("Seat is not valible");
                    booked=false;    
                }
        }
        return booked;
    }
  
    private static String readBytes(String file, int position, int length)  {
        String record = null; 
        try { 
            RandomAccessFile fileStore = new RandomAccessFile(file, "r"); 
            fileStore.seek(position); 
            byte[] bytes = new byte[length];
            fileStore.read(bytes); 
            record = new String(bytes);
            fileStore.close(); 
        } catch (IOException e) { 
        } 
        return record; 
    }

    private static String searchBinaryName(String inputName) {
        //Copy records from 2D Array to ArrayList
        bookedSeatsList.clear();
        for(int i=0; i<Seats.length; i++){                 
           for(int j=0; j<Seats[i].length;j++){                                
                String CharCol = convertIndexToChar(j);
                String bookedSeatNum;
                
                if(Seats[i][j] !=null){
                    String seatNum =CharCol +(i+1);
                    //Write to Single ArrayList
                    
                    bookedSeatsList.add(Seats[i][j].getFirst_Name()+" "+ Seats[i][j].getLast_Name()+";"+ seatNum);
                    
                }
                            
            }                    
        }
        //Sort Records In Array List
        Collections.sort(bookedSeatsList);
        
        //Transter Sorted Records in array list to single dimention arrays.
        String[] Name = new String[bookedSeatsList.size()];
        String[] Seat = new String[bookedSeatsList.size()];
        
        for(int i =0 ; i<bookedSeatsList.size(); i++){
            String temp[] =bookedSeatsList.get(i).split(";");
            Name[i] =temp[0];
            Seat[i] = temp[1];
            System.out.println(Name[i]);
        }
        //Binary Search
        int foundInd = binarySearch(Name, inputName);      
        if(foundInd !=-1){    
            return Seat[foundInd] ;
        }else {
            return "";
        }

    }

    public static int binarySearch(String a[], String name) {
       int low = 0;
        int high = a.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;
            if (a[mid].compareTo(name) < 0) {
                low = mid + 1;
                
            } else if (a[mid].compareTo(name) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    private static void cancelBooking(String foundSeatNo) throws IOException {
      getRowCol(foundSeatNo);
      Seats[Row][Col]=null;
      saveBookingRecords(foundSeatNo, foundSeatNo+"*-");
      ViewSeat();
      System.out.println("Your booking has been canceled");
    }

    private static void PrintReciept(String f_name, String l_name, String sex, int age, String SeatN) throws IOException {
        BufferedWriter line=null;
        line = new BufferedWriter(new FileWriter(f_name+ ".txt"));
        
        line.write("Customer :" + f_name + " " +l_name);line.newLine();
        line.write("Gender :" + sex +"     Age:"+ age); line.newLine();
        line.write("Seat No: "+ SeatN);line.newLine();line.newLine();
        String seat ="*";
        line.write("   A B C   D E F");
         line.newLine();
        for(int i=0; i<20; i++){
            //Classify The Class
            if(i==0){
                line.write("-----------------");line.newLine();
                line.write("    Business");line.newLine();
                line.write("-----------------");line.newLine();
            }
            if(i==9){
                line.write("-----------------");line.newLine();
                line.write("    Ecomonic");line.newLine();
                line.write("-----------------");line.newLine();
            }            
            //This condition is to aliagn the culum properly
            if(i <9){line.write((i+1)+"  ");line.newLine();} 
            else {line.write((i+1)+" "); line.newLine();}
            //-------------------------------------


            
            for(int j=0; j<6; j++){

                    if(Seats[i][j]==null){
                        //Making Space between culum ABC and DEF
                        seat = (j%3==0)? "   * ": "* ";
                        line.write(seat);
                    }

                    else {
                        //Making Space between culum ABC and DEF
                        seat = (j%3==0)? "   "+ Seats[i][j].getClassify()+"  ": Seats[i][j].getClassify()+" ";
                        line.write(seat);
                    }                
                }
            
             line.newLine();
            }
            line.close();
        }
    
        
    
}
