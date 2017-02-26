package tokens;


public class TokenDelimiter extends Token{
    private String type = "DEL";
    private String value;
    
    public TokenDelimiter(String s){
        value = s;
    }
    
//    public String getValue(){
//        return value;
//    }
    
    public String toString(){
        return "[DEL=" + value + "]";
    }
}
