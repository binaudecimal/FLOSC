package tokens;


public class TokenIdentifier extends Token{
    //ID value
    private String value;
    private String type = "ID";
    public TokenIdentifier(String s) {
        value = s;
    }
    
//    public String getValue(){
//        return value;
//    }
    
    public String toString(){
        return "[ID=" + value + "]";
    }
}
