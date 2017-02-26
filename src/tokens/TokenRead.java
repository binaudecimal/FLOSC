
package tokens;


public class TokenRead extends Token{
    private String type = "read";
    private String value;
    
    public TokenRead(String s){
        value = s;
    }
    
    public String toString(){
        return "[NUM=" + value + "]";
    }
}
