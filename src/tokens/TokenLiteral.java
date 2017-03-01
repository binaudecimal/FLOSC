
package tokens;

public class TokenLiteral extends Token{
    private String type = "literal";
    private String value;
    
    public TokenLiteral(String s){
        value = s;
    }
    
    public String toString(){
        return "[LIT=" + value + "]";
    }
}
