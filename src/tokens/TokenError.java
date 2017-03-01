
package tokens;


public class TokenError extends Token{
    private String type = "error";
    private String value;
    
    public TokenError(String s){
        value = s;
    }
    
    public String toString(){
        return "[ERROR]";
    }
}
