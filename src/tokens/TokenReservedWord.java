package tokens;


public class TokenReservedWord extends Token{
    
    private String kind = "RW";
    private String value;
    
    public TokenReservedWord(String s){
        value = s;
    }
    
    public String toString(){
        return "[RW=" + value + "]";
    }
}
