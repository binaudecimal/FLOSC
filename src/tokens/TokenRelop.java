package tokens;

public class TokenRelop extends Token {
    private String type = "relop";
    private String value;
    private String rel;
    
    public TokenRelop(String s, String r){
        value = s;
        rel = r;
    }
    
    public String toString(){
        return "[" + value + "]";
    }
}
