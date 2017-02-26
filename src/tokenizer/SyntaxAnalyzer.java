package tokenizer;
import tokens.*;
import java.io.FileNotFoundException;
import java.util.HashMap;


public class SyntaxAnalyzer {
    
    FileReader reader;
    HashMap<String, Token> reservedWords;
    HashMap<String, Token> keywords;
    
    public SyntaxAnalyzer(){
        try{
            reader = new FileReader("src/sampleProgram");
        }
        catch(FileNotFoundException e){
            System.err.println("File is not found on location");
        }
        System.out.println("Initializing maps...");
        initMaps();
    }
    
    
    public void initMaps(){
        //Initialize RW maps
        reservedWords = new HashMap();
        reservedWords.put("return",             new TokenReservedWord("return"));
        reservedWords.put("main",               new TokenReservedWord("main"));
        reservedWords.put("null",               new TokenReservedWord("null"));
        reservedWords.put("do",                 new TokenReservedWord("do"));
        reservedWords.put("if",                 new TokenReservedWord("if"));
        reservedWords.put("else",               new TokenReservedWord("else"));
        reservedWords.put("true",               new TokenReservedWord("true"));
        reservedWords.put("false",              new TokenReservedWord("false"));
        reservedWords.put("break",              new TokenReservedWord("break"));
        reservedWords.put("while",              new TokenReservedWord("while"));
        reservedWords.put("elseif",             new TokenReservedWord("elseif"));
        reservedWords.put("end",                new TokenReservedWord("end"));
        reservedWords.put("final",              new TokenReservedWord("final"));
        reservedWords.put("display",            new TokenReservedWord("display"));
        reservedWords.put("enum",               new TokenReservedWord("enum"));
        reservedWords.put("end",                new TokenReservedWord("end"));
        reservedWords.put("for",                new TokenReservedWord("for"));
        reservedWords.put("function",           new TokenReservedWord("function"));
        reservedWords.put("parseDecimal",       new TokenReservedWord("parse"));
        reservedWords.put("parseNumber",        new TokenReservedWord("parse"));
        reservedWords.put("findLength",         new TokenReservedWord("length"));
        //dels
        reservedWords.put(";",                  new TokenDelimiter("statedel"));
        reservedWords.put("(",                  new TokenDelimiter("leftparen"));
        reservedWords.put(")",                  new TokenDelimiter("rightparen"));
        reservedWords.put("[",                  new TokenDelimiter("leftbracket"));
        reservedWords.put("]",                  new TokenDelimiter("rightbracket"));
        reservedWords.put(",",                  new TokenDelimiter("argdel"));
        reservedWords.put(":",                  new TokenDelimiter("casedel"));
        //Other reserves
        reservedWords.put("+",                  new TokenOp("sumdiff"));
        reservedWords.put("-",                  new TokenOp("sumdiff"));
        reservedWords.put("/",                  new TokenOp("prodquo"));
        reservedWords.put("*",                  new TokenOp("prodquo"));
        reservedWords.put("%",                  new TokenOp("mod"));
        reservedWords.put("^",                  new TokenOp("exp"));
        reservedWords.put("++",                 new TokenOp("inc"));
        reservedWords.put("dec",                new TokenOp("--"));
        reservedWords.put("=",                  new TokenOp("assign"));
        reservedWords.put("<",                  new TokenRelop("relop"));
        reservedWords.put("<=",                 new TokenRelop("relop"));
        reservedWords.put(">",                  new TokenRelop("relop"));
        reservedWords.put("<>",                 new TokenRelop("relop"));
        reservedWords.put(">=",                 new TokenRelop("relop"));
        reservedWords.put("==",                 new TokenRelop("relop"));
        reservedWords.put("&",                  new TokenLogOp("and"));
        reservedWords.put("|",                  new TokenLogOp("or"));
        reservedWords.put("!",                  new TokenLogOp("not"));
        
        //Whitespace
        reservedWords.put(" ",                  new TokenWhitespace("space"));
        reservedWords.put("\n",                  new TokenWhitespace("eol"));
        
        keywords = new HashMap();
        //Initialize KW maps
    }
        public Token getNextToken(){
            if(reader.hasNext()== false) return null;
            else{
                char ch = reader.getNextChar();
//                if(ch == ';') return reservedWords.get(ch);
//                else if(ch == ' ') return reservedWords.get(ch);
//                else return new Token();
                switch(ch){
                    case ';' : System.out.println("; matches");return reservedWords.get(ch);
                    case ' ' : System.out.println("space matches");return reservedWords.get(ch);
                    case '=' : System.out.println("assign matches");return reservedWords.get(ch);
                    case '\n': System.out.println("\\n matches");return reservedWords.get(ch);
                    default : System.out.println("{" +ch+ "}" );return new Token();
                }
            }
        }
    
}
