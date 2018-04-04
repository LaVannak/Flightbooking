/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightbooking;

/**
 *
 * @author j250519
 */
public class customer {
    private String First_Name;
    private String Last_Name;
    private String Gender;
    private int Age;
    private String Classify="";

    public String getClassify() {
        return Classify;
    }


    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String First_Name) {
        this.First_Name = First_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String Last_Name) {
        this.Last_Name = Last_Name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int Age) {
        this.Age = Age;
    }
    
    public customer(String First_Name, String Last_Name, String Gender, int Age) {
        this.First_Name = First_Name;
        this.Last_Name = Last_Name;
        this.Gender = Gender;
        this.Age = Age;
        this.Classify =((Age >18)? "A": "C"); 
    }

}
