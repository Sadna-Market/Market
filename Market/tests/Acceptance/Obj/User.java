package Acceptance.Obj;

public class User {
    public String username;
    public  String password;
    public String name;
    public String phone_number;
    public Address addr;
    public User(String name,String usern, String p, Address adr, String num){
        username = usern;
        password = p;
        this.name = name;
        phone_number = num;
    }
}
