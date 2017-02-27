package tokenizer;
import tokens.*;
import java.io.FileNotFoundException;
import java.util.HashMap;


public class SyntaxAnalyzer {
    
    FileReader reader;
    HashMap<String, Token> reservedWords;
    HashMap<String, Token> keywords;
    final static String ALPHAlow = "abcdefghjiklmnopqrstuvwxyz";
    final static String ALPHAup = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final static String ALPHA = ALPHAlow + ALPHAup;
    final static String NUM = "0123456789";
    final static String ALPHANUM = ALPHA + NUM;
    
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
        reservedWords.put("+",                  new TokenOp("sumdiff","bad"));
        reservedWords.put("-",                  new TokenOp("sumdiff","bmin"));
        reservedWords.put("/",                  new TokenOp("prodquo","div"));
        reservedWords.put("*",                  new TokenOp("prodquo", "mult"));
        reservedWords.put("%",                  new TokenOp("mod", "mod"));
        reservedWords.put("^",                  new TokenOp("exp", "exp"));
        reservedWords.put("++",                 new TokenOp("inc", "inc"));
        reservedWords.put("--",                 new TokenOp("dec", "dec"));
        reservedWords.put("=",                  new TokenOp("assign", "ass"));
        reservedWords.put("<",                  new TokenRelop("relop", "l"));
        reservedWords.put("<=",                 new TokenRelop("relop","le"));
        reservedWords.put(">",                  new TokenRelop("relop","g"));
        reservedWords.put("<>",                 new TokenRelop("relop","ne"));
        reservedWords.put(">=",                 new TokenRelop("relop","ge"));
        reservedWords.put("==",                 new TokenRelop("relop","ee"));
        reservedWords.put("&",                  new TokenLogOp("and"));
        reservedWords.put("|",                  new TokenLogOp("or"));
        reservedWords.put("!",                  new TokenLogOp("not"));
        
        //Whitespace
        reservedWords.put(" ",                  new TokenWhitespace(" "));
        reservedWords.put("\n",                 new TokenWhitespace("\n"));
        
        keywords = new HashMap();
        //Initialize KW maps
    }
    public Token getNextToken(){
        if(reader.hasNext()== false) return null;
        else{
            char ch = reader.getNextChar();
            switch(ch){
                case ';' :
                case ' ' :
                case '(':
                case ')':
                case '[':
                case ']':
                case ',':
                case ':':
                case '^':
                case '%':
                case '/':
                case '*':
                case '!':
                case '&':
                case '|':
                case '\n':  return reservedWords.get(ch+"");
                
                case '+' :  if(reader.hasNext()){
                                ch = reader.getNextChar();
                                if(ch=='+') return reservedWords.get("++");
                                else {
                                    reader.backread();
                                    return reservedWords.get("+");
                                }
                            }
                            else return reservedWords.get("+");
                
                case '-' :  if(reader.hasNext()){
                                ch = reader.getNextChar();
                                if(ch=='-') return reservedWords.get("--");
                                else {
                                    reader.backread();
                                    return reservedWords.get("-");
                                }
                            }
                            else return reservedWords.get("-");
                
                case '=' :  if(reader.hasNext()){
                                ch = reader.getNextChar();
                                if(ch=='=') return reservedWords.get("==");
                                else {
                                    reader.backread();
                                    return reservedWords.get("=");
                                }
                            }
                            else return reservedWords.get("=");
                
                case '>' :  if(reader.hasNext()){
                                ch = reader.getNextChar();
                                if(ch=='=') return reservedWords.get(">=");
                                else {
                                    reader.backread();
                                    return reservedWords.get(">");
                                }
                            }
                            else return reservedWords.get(">");
                
                case '<' :  if(reader.hasNext()){
                                ch = reader.getNextChar();
                                if(ch=='>') return reservedWords.get("<>");
                                else if(ch=='=') return reservedWords.get("<=");
                                else {
                                    reader.backread();
                                    return reservedWords.get("<");
                                }
                            }
                            else return reservedWords.get("<");
                default : //if not a symbol, it could start with alphanum
                            if(NUM.indexOf(ch)<=0){
                                return new TokenLiteral(ch + identifyNum());
                            }
                            //if it starts lower case, could be keyword
                            switch(ch){
                                case 'b':
                                case 'd':
                                case 'e':
                                case 'f':
                                case 'i':
                                case 'm':
                                case 'n':
                                case 'p':
                                case 'r':
                                case 's':
                                case 't':
                                case 'w':
                                default :if(ALPHAlow.indexOf(ch)>=0){           //if it starts with any other letter, it must be an ID
                                            String s = ch + identifyWords();
                                            keywords.put(s, new TokenIdentifier(s));
                                            return keywords.get(s);
                                        }
                            }

                            return null;
            }
        }
    }
        
    public String identifyWords(){
        String s = new String();
        while(reader.hasNext()){
            char ch = reader.getNextChar();
            System.out.println("current char is [" + ch +  "]");
            if(ALPHANUM.indexOf(ch) >=0 & ch!= '\n' & ch!= ' '){
                s+= ch;
                System.out.println(s);
            }
            else {
                reader.backread();
                break;
            }
        }
        return s;
    }
    
    public String identifyNum(){
        String s = new String();
        while(reader.hasNext()){
            char ch = reader.getNextChar();
            System.out.println("current char is [" + ch +  "]");
            if(NUM.indexOf(ch) >=0 & ch!= '\n' & ch!= ' '){
                s+= ch;
                System.out.println(s);
            }
            else {
                reader.backread();
                break;
            }
        }
        return s;
    }
    
}
