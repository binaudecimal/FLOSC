
package tokens;


public class TokenEnumIdentifier extends Token{
    private String type = "enumID";
    private String value;
    public TokenEnumIdentifier(String s){
        value = s;
    }
    
    public String toString(){
        return "[EN=" + value + "]";
    }
    
}
