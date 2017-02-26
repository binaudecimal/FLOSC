package tokens;


public class Token {
    //Parent token class
    private String type = "token";
    private String value;
    
    public Token(){           
    }
    
    public String getType(){
        return type;
    }
    
    public String getValue(){
        return value;
    }
    
    public String toString(){
        return "[" + getValue() + "]";
    }
    
}
