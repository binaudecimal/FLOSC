
package tokens;


public class TokenFunctionIdentifier extends Token{
    private String type="function";
    private String value;
    
    public TokenFunctionIdentifier(String s){
        value = s;
    }
    
    public String toString(){
        return "[FC=" + value + "]";
    }
    
}
