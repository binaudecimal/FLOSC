package tokens;

public class TokenRelop extends Token {
    private String type = "relop";
    private String value;
    
    public TokenRelop(String s){
        value = s;
    }
    
    public String toString(){
        return "[" + value + "]";
    }
}
