package tokens;


public class TokenOp extends Token{
    private String type = "operator";
    private String value;
    
    public TokenOp (String s){
        value =s;
    }
    
    public String toString(){
        return "[" + value + "]";
    }
}
