package tokens;


public class TokenDatatype extends Token{
    private String type = "datatype";
    private String value;
    
    public TokenDatatype(String s){
        value = s;
    }
    
    public String toString(){
        return "[" + value + "]";
    }
}
