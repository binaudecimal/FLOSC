
package tokens;


public class TokenArrayIdentifier extends Token{
    private String type;
    private String value;
    
    public TokenArrayIdentifier(String s){
        value = s;
    }
    
    public String toString(){
        return "[AR=" + value + "]";
    }
    
}
