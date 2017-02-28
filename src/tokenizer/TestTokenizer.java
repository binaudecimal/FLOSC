
package tokenizer;
import tokens.*;

public class TestTokenizer {
    public static void main(String[] args){
        LexicalAnalyzer sa = new LexicalAnalyzer();
        Token t;
        while((t=sa.getNextToken())!= null){
            System.out.print(t.toString());
        }
    }
}
