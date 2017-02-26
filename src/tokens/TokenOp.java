package tokens;


public class TokenOp extends Token{
    private String type = "operator";
    private String value;
    private String op;
    
    public TokenOp (String s, String o){
        value =s;
        op = o;
    }
    
    public String getOp(){
        return op;
    }
    public String toString(){
        return "[" + value + "="+op+"]";
    }
}
