
package tokens;


public class TokenLogOp extends Token{
    private String type = "logoperator";
    private String value;
    
    public TokenLogOp(String s){
        value = s;
    }
    
    public String toString(){
        return "[" + value + "]";
    }
}
