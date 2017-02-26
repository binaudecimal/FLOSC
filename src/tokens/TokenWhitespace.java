package tokens;


public class TokenWhitespace extends Token{
    private String type = "whitespace";
    private String value;
    
    public TokenWhitespace(String s){  
        value = s;
    }
    
    public String toString(){
        return value;
    }
}
