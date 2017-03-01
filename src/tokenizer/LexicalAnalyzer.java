package tokenizer;
import tokens.*;
import java.io.FileNotFoundException;
import java.util.HashMap;
import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;


public class LexicalAnalyzer {
    
    FileReader reader;
    HashMap<String, Token> reservedWords;
    HashMap<String, Token> keywords;
    final static String ALPHAlow = "abcdefghjiklmnopqrstuvwxyz";
    final static String ALPHAup = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final static String ALPHA = ALPHAlow + ALPHAup;
    final static String NUM = "0123456789";
    final static String ALPHANUM = ALPHA + NUM + "_";
    final static String DELIMITERSclose= " \n\t)}];&|!,:+-*/^%";
    final static String DELIMITERSopen = "({[";
    
    public LexicalAnalyzer(){
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
        //reads
        reservedWords.put("readCharacter",          new TokenReservedWord("readCharacter"));
        reservedWords.put("readString",             new TokenReservedWord("readString"));
        reservedWords.put("readNumber",             new TokenReservedWord("readNumber"));
        reservedWords.put("readDecimal",            new TokenReservedWord("readDecimal"));
        //datatypes
        
        reservedWords.put("boolean",            new TokenDatatype("boolean"));
        reservedWords.put("string",             new TokenDatatype("string"));
        reservedWords.put("number",             new TokenDatatype("number"));
        reservedWords.put("decimal",            new TokenDatatype("decimal"));
        reservedWords.put("character",          new TokenDatatype("character"));
        
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
        reservedWords.put("parseDecimal",       new TokenReservedWord("parseDecimal"));
        reservedWords.put("parseNumber",        new TokenReservedWord("parseNumber"));
        reservedWords.put("findLength",         new TokenReservedWord("length"));
        //dels
        reservedWords.put(";",                  new TokenDelimiter("statedel"));
        reservedWords.put("(",                  new TokenDelimiter("leftparen"));
        reservedWords.put(")",                  new TokenDelimiter("rightparen"));
        reservedWords.put("[",                  new TokenDelimiter("leftbracket"));
        reservedWords.put("]",                  new TokenDelimiter("rightbracket"));
        reservedWords.put("{",                  new TokenDelimiter("leftbrace"));
        reservedWords.put("}",                  new TokenDelimiter("rightbrace"));
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
        reservedWords.put("comment",            new TokenWhitespace("comment"));
        
        keywords = new HashMap();
        //Initialize KW maps
    }
    public Token getNextToken(){
        if(reader.hasNext()== false) return null;
        else{
            char ch = reader.getNextChar();
            switch(ch){
                case '~': return readComment();
                case '\"': return readString();
                case '\'': return readChar();
                case '.': escapeError(); return new TokenError("Illegal start");
                case ' ' :
                case '\n':
                case ';' :
                case '(':
                case ')':
                case '[':
                case ']':
                case '{':
                case '}':
                case ',':
                case ':':
                case '!':
                case '&':
                case '|':     
                case '^':
                case '%':
                case '/':
                case '*': return reservedWords.get(ch+"");
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
                            if(NUM.indexOf(ch)>=0){
                                reader.backread();
                                return tokenNum();
                            }
                            //if it starts lower case, could be keyword
                            switch(ch){
                                case 'b':if(reader.hasNext()){                  //check if has next on 'b'
                                            ch = reader.getNextChar();
                                            if(ch=='r'){                        //nest into b-reak
                                                if(reader.hasNext() && (ch=reader.getNextChar()) == 'e'){    //nest into 'bre'-ak
                                                    if(reader.hasNext() && (ch=reader.getNextChar())=='a'){ //nest into "brea"
                                                        if(reader.hasNext() && (ch = reader.getNextChar()) == 'k'){ //nest into 'break'
                                                            if(reader.hasNext() && ALPHANUM.indexOf((ch=reader.getNextChar())) >=0){     //nest into break-anything else
                                                                reader.backread();
                                                                return verify(identifyWords("break"));
                                                            }
                                                            else return reservedWords.get("break");                                     //else its a break
                                                        }
                                                        else{                                                   //if brea-anything else
                                                            reader.backread();
                                                            return verify(identifyWords("brea"));
                                                        }
                                                    }
                                                    else{                                  //if not a or no next char
                                                        reader.backread();
                                                        return verify(identifyWords("bre"));
                                                    }
                                                }
                                                else{                             //if no 'br'hasnext !!
                                                    reader.backread();
                                                    return verify(identifyWords("br"));
                                                }
                                            }
                                            
                                            if(ch =='o'){                       //Nesting boolean
                                                if(reader.hasNext() && (ch=reader.getNextChar()) == 'o'){ 
                                                    if(reader.hasNext() && (ch=reader.getNextChar())=='l'){ 
                                                        if(reader.hasNext() && (ch = reader.getNextChar()) == 'e'){
                                                            if(reader.hasNext() && (ch=reader.getNextChar()) == 'a'){
                                                                if(reader.hasNext() && (ch=reader.getNextChar()) == 'n'){
                                                                    if(reader.hasNext() && ALPHANUM.indexOf((ch=reader.getNextChar())) >=0){   
                                                                        reader.backread();
                                                                        return verify(identifyWords("boolean"));
                                                                    }
                                                                    else return reservedWords.get("boolean");   
                                                                }
                                                                else{
                                                                    reader.backread();
                                                                    return verify(identifyWords("boolea"));
                                                                }
                                                            }   
                                                            else{
                                                                reader.backread();
                                                                return verify(identifyWords("boole"));
                                                            }
                                                        }
                                                        else{                                                 
                                                            reader.backread();
                                                            return verify(identifyWords("bool"));
                                                        }
                                                    }
                                                    else{                                  
                                                        reader.backread();
                                                        return verify(identifyWords("boo"));
                                                    }
                                                }
                                                else{                             
                                                    reader.backread();
                                                    return verify(identifyWords("bo"));
                                                }
                                            }
                                            else{                               //if b-any other
                                                reader.backread();
                                                return verify(identifyWords(ch+""));
                                            }
                                        }
                                        else {
                                            keywords.put("b", new TokenIdentifier("b"));    //if no next char on 'b'
                                            return keywords.get("b");   
                                        }
                                    //--------------------------------------------------------------------------------------------end b nest    
                                case 'c':if(reader.hasNext() && (ch = reader.getNextChar()) == 'h'){                                                          //Nesting decimal
                                            if(reader.hasNext() && (ch=reader.getNextChar()) == 'a'){ 
                                                if(reader.hasNext() && (ch=reader.getNextChar())=='r'){ 
                                                    if(reader.hasNext() && (ch = reader.getNextChar()) == 'a'){
                                                        if(reader.hasNext() && (ch=reader.getNextChar()) == 'c'){
                                                            if(reader.hasNext() && (ch=reader.getNextChar()) == 't'){
                                                                if(reader.hasNext() && (ch=reader.getNextChar()) == 'e'){
                                                                    if(reader.hasNext() && (ch=reader.getNextChar())=='r'){
                                                                        if(reader.hasNext() && ALPHANUM.indexOf((ch=reader.getNextChar())) >=0){   
                                                                            reader.backread();
                                                                            return verify(identifyWords("character"));
                                                                        }
                                                                        else return reservedWords.get("character");   
                                                                    }
                                                                    else {
                                                                        reader.backread();
                                                                        return verify(identifyWords("characte"));
                                                                    }
                                                                }
                                                            }
                                                            else{
                                                                reader.backread();
                                                                return verify(identifyWords("charact"));
                                                            }
                                                        }   
                                                        else{
                                                            reader.backread();
                                                            return verify(identifyWords("charac"));
                                                        }
                                                    }
                                                    else{                                                 
                                                        reader.backread();
                                                        return verify(identifyWords("chara"));
                                                    }
                                                }
                                                else{                                  
                                                    reader.backread();
                                                    return verify(identifyWords("char"));
                                                }
                                            }
                                            else{                             
                                                reader.backread();
                                                return verify(identifyWords("cha"));
                                            }
                                            
                                        }
                                else {
                                    return verify(identifyWords(ch+""));
                                }
                                    //------------------------====================================================================end c nest
                                case 'd': if(reader.hasNext()&&(ch = reader.getNextChar()) == 'o' | ch =='i' | ch=='e'){
                                        if(ch =='e'){                                                           //Nesting decimal
                                            if(reader.hasNext() && (ch=reader.getNextChar()) == 'e'){ 
                                                if(reader.hasNext() && (ch=reader.getNextChar())=='c'){ 
                                                    if(reader.hasNext() && (ch = reader.getNextChar()) == 'i'){
                                                        if(reader.hasNext() && (ch=reader.getNextChar()) == 'm'){
                                                            if(reader.hasNext() && (ch=reader.getNextChar()) == 'a'){
                                                                if(reader.hasNext() && (ch=reader.getNextChar()) == 'l'){
                                                                    if(reader.hasNext() && ALPHANUM.indexOf((ch=reader.getNextChar())) >=0){   
                                                                        reader.backread();
                                                                        return verify(identifyWords("decimal"));
                                                                    }
                                                                    else return reservedWords.get("decimal");   
                                                                }
                                                            }
                                                            else{
                                                                reader.backread();
                                                                return verify(identifyWords("decima"));
                                                            }
                                                        }   
                                                        else{
                                                            reader.backread();
                                                            return verify(identifyWords("decim"));
                                                        }
                                                    }
                                                    else{                                                 
                                                        reader.backread();
                                                        return verify(identifyWords("deci"));
                                                    }
                                                }
                                                else{                                  
                                                    reader.backread();
                                                    return verify(identifyWords("dec"));
                                                }
                                            }
                                            else{                             
                                                reader.backread();
                                                return verify(identifyWords("de"));
                                            }
                                        }
                                        if(ch =='i'){                                           //Nesting display
                                                if(reader.hasNext() && (ch=reader.getNextChar()) == 's'){ 
                                                    if(reader.hasNext() && (ch=reader.getNextChar())=='p'){ 
                                                        if(reader.hasNext() && (ch = reader.getNextChar()) == 'l'){
                                                            if(reader.hasNext() && (ch=reader.getNextChar()) == 'a'){
                                                                if(reader.hasNext() && (ch=reader.getNextChar()) == 'y'){
                                                                    if(reader.hasNext() && ALPHANUM.indexOf((ch=reader.getNextChar())) >=0){   
                                                                        reader.backread();
                                                                        return verify(identifyWords("display"));
                                                                    }
                                                                    else return reservedWords.get("display");   
                                                                }
                                                                else{
                                                                    reader.backread();
                                                                    return verify(identifyWords("displa"));
                                                                }
                                                            }   
                                                            else{
                                                                reader.backread();
                                                                return verify(identifyWords("displ"));
                                                            }
                                                        }
                                                        else{                                                 
                                                            reader.backread();
                                                            return verify(identifyWords("disp"));
                                                        }
                                                    }
                                                    else{                                  
                                                        reader.backread();
                                                        return verify(identifyWords("dis"));
                                                    }
                                                }
                                                else{                             
                                                    reader.backread();
                                                    return verify(identifyWords("di"));
                                                }
                                            }
                                        if(ch=='o'){
                                            if(reader.hasNext() && ALPHANUM.indexOf((ch=reader.getNextChar())) >=0){   
                                                reader.backread();
                                                return verify(identifyWords("do"));
                                            }
                                            else return reservedWords.get("do"); 
                                        }
                                }
                                else {
                                    return verify(identifyWords(ch+""));
                                }
                                    
                                    //------------------------------------------------------------------------------------------end D nest
                                case 'e':
                                        if(reader.hasNext() && (ch=reader.getNextChar())=='l' | (ch=reader.getNextChar()) == 'n'){
                                            if(ch=='l'){                                                        //nesting else
                                                if(reader.hasNext() && (ch=reader.getNextChar()) == 's'){    
                                                    if(reader.hasNext() && (ch=reader.getNextChar())=='e'){
                                                        if(reader.hasNext() && (ch = reader.getNextChar()) == 'i'){ 
                                                            if(reader.hasNext()&&(ch=reader.getNextChar())=='f'){
                                                                if(reader.hasNext() && ALPHANUM.indexOf((ch=reader.getNextChar())) >=0){     
                                                                    reader.backread();
                                                                    return verify(identifyWords("elseif"));
                                                                }
                                                                else return reservedWords.get("elseif");
                                                            }
                                                            else{
                                                              reader.backread();
                                                              return verify(identifyWords("elsei"));
                                                            }
                                                        }
                                                        else if(ALPHANUM.indexOf((ch=reader.getNextChar()))>=0){                                                  
                                                            reader.backread();
                                                            return verify(identifyWords("else"));
                                                        }
                                                        else {
                                                            reader.backread();
                                                            return reservedWords.get("else");
                                                        }
                                                    }
                                                    else{                                  
                                                        reader.backread();
                                                        return verify(identifyWords("els"));
                                                    }
                                                }
                                                else{                             
                                                    reader.backread();
                                                    return verify(identifyWords("el"));
                                                }
                                            }
                                            if(ch=='n'){
                                                if(reader.hasNext() && (ch = reader.getNextChar()) == 'u' | ch=='d'){ 
                                                    if(ch=='u'){
                                                        if(reader.hasNext()&&(ch=reader.getNextChar())=='m'){
                                                            if(reader.hasNext() && ALPHANUM.indexOf((ch=reader.getNextChar())) >=0){     
                                                                reader.backread();
                                                                return verify(identifyWords("enum"));
                                                            }
                                                            else return reservedWords.get("enum");
                                                        }
                                                        else{
                                                          reader.backread();
                                                          return verify(identifyWords("enu"));
                                                        }
                                                    }
                                                    else if(ch=='d'){
                                                        if(reader.hasNext() && ALPHANUM.indexOf((ch=reader.getNextChar())) >=0){
                                                            reader.backread();
                                                            return verify(identifyWords("end"));
                                                        }
                                                        else{
                                                            return reservedWords.get("end");
                                                        }
                                                    }
                                                }
                                                else{
                                                    return reservedWords.get("en");
                                                }
                                            }
                                        }
                                        else {
                                            reader.backread();
                                            return verify(identifyWords("e"));
                                        }
                                    
                                    //===========================================================================================end e nest
                                case 'f':if(reader.hasNext()){
                                            
                                            if(reader.getNextChar()=='a'){      //false
                                                if(reader.hasNext()){
                                                    if(reader.getNextChar()=='l'){
                                                        if(reader.hasNext()){
                                                            if(reader.getNextChar()=='s'){
                                                                if(reader.hasNext()){
                                                                    if(reader.getNextChar()=='e'){
                                                                        if(reader.hasNext()){
                                                                            return backVerify("false");
                                                                        }
                                                                        else return reservedWords.get("false");
                                                                    }
                                                                    else return backVerify("fals");
                                                                }
                                                                else return verify(identifyWords("fals"));
                                                            }
                                                            else return backVerify("fal");
                                                        }
                                                        else return verify(identifyWords("fal"));
                                                    }
                                                    else return backVerify("fa");
                                                }
                                                else return verify(identifyWords("fa"));
                                            }
                                            else if(reader.getNextChar()=='i'){ //final, findLength
                                                if(reader.hasNext()){
                                                    if(reader.getNextChar()=='n'){
                                                        if(reader.hasNext()){
                                                            if(reader.getNextChar()=='a'){      //@final
                                                                if(reader.hasNext()){
                                                                    if(reader.getNextChar()=='l'){
                                                                        if(reader.hasNext()){
                                                                            return backVerify("final");
                                                                        }
                                                                        else return reservedWords.get("final");
                                                                    }
                                                                    else return backVerify("fina");
                                                                }
                                                                else return verify(identifyWords("fina"));
                                                            }
                                                            else if(reader.getNextChar()=='d'){ //findLength
                                                                if(reader.hasNext()){
                                                                    if(reader.getNextChar()=='L'){
                                                                        if(reader.hasNext()){
                                                                            if(reader.getNextChar()=='e'){
                                                                                if(reader.hasNext()){
                                                                                    if(reader.getNextChar()=='n'){
                                                                                        if(reader.hasNext()){
                                                                                            if(reader.getNextChar()=='g'){
                                                                                                if(reader.hasNext()){
                                                                                                    if(reader.getNextChar()=='t'){
                                                                                                        if(reader.hasNext()){
                                                                                                            if(reader.getNextChar()=='h'){
                                                                                                                if(reader.hasNext()){
                                                                                                                    return backVerify("findLength");
                                                                                                                }
                                                                                                                else return reservedWords.get("findLength");
                                                                                                            }
                                                                                                            else return backVerify("findLengt");
                                                                                                        }
                                                                                                        else return verify(identifyWords("findLengt"));
                                                                                                    }
                                                                                                    else return backVerify("findLeng");
                                                                                                }
                                                                                                else return verify(identifyWords("findLeng"));
                                                                                            }
                                                                                            else return backVerify("findLen");
                                                                                        }
                                                                                        else return verify(identifyWords("findLen"));
                                                                                    }
                                                                                    else return backVerify("findLe");
                                                                                }
                                                                                else return verify(identifyWords("findLe"));
                                                                            }
                                                                            else return backVerify("findL");
                                                                        }
                                                                        else return verify(identifyWords("findL"));
                                                                    }
                                                                    else return backVerify("find");
                                                                }
                                                                else return verify(identifyWords("find"));
                                                            }
                                                            else return backVerify("fin");
                                                        }
                                                        else return verify(identifyWords("fin"));
                                                    }
                                                    else return backVerify("fi");
                                                }
                                                else return verify(identifyWords("fi"));
                                                
                                            }
                                            else if(reader.getNextChar()=='o'){ //for
                                                if(reader.hasNext()){
                                                    if(reader.getNextChar()=='r'){
                                                        if(reader.hasNext()){
                                                            return backVerify("for");
                                                        }
                                                        else return reservedWords.get("for");
                                                    }
                                                    else return backVerify("fo");
                                                }
                                                else return verify(identifyWords("fo"));
                                            }
                                            else if(reader.getNextChar()=='u'){ //function
                                                if(reader.hasNext()){
                                                    if(reader.getNextChar()=='n'){
                                                        if(reader.hasNext()){
                                                            if(reader.getNextChar()=='c'){
                                                                if(reader.hasNext()){
                                                                    if(reader.getNextChar()=='t'){
                                                                        if(reader.hasNext()){
                                                                            if(reader.getNextChar()=='i'){
                                                                                if(reader.hasNext()){
                                                                                    if(reader.getNextChar()=='o'){
                                                                                        if(reader.hasNext()){
                                                                                            if(reader.getNextChar()=='n'){
                                                                                                if(reader.hasNext()){
                                                                                                    return backVerify("function");                                                                                                    }
                                                                                                else return reservedWords.get("function");
                                                                                            }
                                                                                            else return backVerify("functio");
                                                                                        }
                                                                                        else return verify(identifyWords("functio"));
                                                                                    }
                                                                                    else return backVerify("functi");
                                                                                }
                                                                                else return verify(identifyWords("functi"));
                                                                            }
                                                                            else return backVerify("funct");
                                                                        }
                                                                        else return verify(identifyWords("funct"));
                                                                    }
                                                                    else return backVerify("func");
                                                                }
                                                                else return verify(identifyWords("func"));
                                                            }
                                                            else return backVerify("fun");
                                                        }
                                                        else return verify(identifyWords("fun"));
                                                    }
                                                    else return backVerify("fu");
                                                }
                                                else return verify("fu");
                                            }
                                            else return backVerify("f");
                                        }
                                        else return verify(identifyWords("f"));
                                case 'i':if(reader.hasNext()){
                                            ch = reader.getNextChar();
                                            if(ch == 'f'){
                                                if(reader.hasNext() && ALPHANUM.indexOf(reader.getNextChar())>=0){
                                                    reader.backread();
                                                    return verify(identifyWords("if"));
                                                }
                                                else{
                                                    reader.backread();
                                                    return reservedWords.get("if");
                                                }
                                                    
                                            }
                                            else{
                                                reader.backread();
                                                return verify(identifyWords("i"));
                                            }
                                        }
                                        else {
                                            System.out.println(ch);
                                            
                                            return verify(identifyWords("i"));
                                        }
                                ///==================================================================================================end I nest
                                case 'm': if(reader.hasNext()){
                                            if((ch=reader.getNextChar())=='a'){
                                                if(reader.hasNext()){
                                                    if(reader.getNextChar()=='i'){
                                                        if(reader.hasNext()){
                                                            if(reader.getNextChar()=='n'){
                                                                if(reader.hasNext()){
                                                                    if(ALPHANUM.indexOf(reader.getNextChar())>=0){
                                                                        reader.backread();
                                                                        return verify(identifyWords("main"));
                                                                    }
                                                                    else{
                                                                        reader.backread();
                                                                        return reservedWords.get("main");
                                                                    }
                                                                }
                                                                else{
                                                                    return reservedWords.get("main");
                                                                }
                                                            }
                                                            else{
                                                                reader.backread();
                                                                return verify(identifyWords("mai"));
                                                            }
                                                        }
                                                        else{
                                                            return verify(identifyWords("mai"));
                                                        }
                                                    }
                                                    else{
                                                        reader.backread();
                                                        return verify(identifyWords("ma"));
                                                    }
                                                }
                                                else{
                                                    return verify(identifyWords("ma"));
                                                }
                                            }
                                            else{
                                                reader.backread();
                                                return verify(identifyWords("m"));
                                            }
                                        }
                                        else{
                                            return verify(identifyWords("m"));
                                        }
                                    //===================================================================================================end M nest
                                case 'n':if(reader.hasNext()){
                                            if(reader.getNextChar()=='u'){
                                                if(reader.hasNext()){
                                                    if(reader.getNextChar()=='l'){
                                                        if(reader.hasNext()){
                                                            if(reader.getNextChar()=='l'){
                                                                if(reader.hasNext()){
                                                                    if(ALPHANUM.indexOf(reader.getNextChar()) >=0){
                                                                        return backVerify("null");
                                                                    }
                                                                    else return backVerify("null");
                                                                }
                                                                else return reservedWords.get("null");
                                                            }
                                                            else return backVerify("nul");
                                                        }
                                                        else return verify(identifyWords("nul"));
                                                    }
                                                    else if(reader.getNextChar()=='m'){
                                                        if(reader.hasNext()){
                                                            if(reader.getNextChar()=='b'){
                                                                if(reader.hasNext()){
                                                                    if(reader.getNextChar()=='e'){
                                                                        if(reader.hasNext()){
                                                                            if(reader.getNextChar() == 'r'){
                                                                                if(reader.hasNext()){
                                                                                    if(ALPHANUM.indexOf(reader.getNextChar())>=0){
                                                                                        backVerify("number");
                                                                                    }
                                                                                    else return backVerify("number");
                                                                                }
                                                                                else return reservedWords.get("number");
                                                                            }
                                                                            else return backVerify("numbe");
                                                                        }
                                                                        else return verify(identifyWords("numbe"));
                                                                    }
                                                                    else return backVerify("numb");
                                                                }
                                                                else return verify(identifyWords("numb"));
                                                            }
                                                            else backVerify("num");
                                                        } 
                                                        else return verify(identifyWords("num"));
                                                    }
                                                    else{
                                                        backVerify("nu");
                                                    }
                                                }
                                                else{
                                                    return verify(identifyWords("nu"));
                                                }
                                            }
                                            else{
                                                reader.backread();
                                                return verify(identifyWords("n"));
                                            }
                                        }
                                        else{
                                            return verify(identifyWords("n"));
                                        }
                                            
                                    //====================================================================================================end N nest
                                case 'p':if(reader.hasNext()){
                                            if(reader.getNextChar()=='a'){
                                                if(reader.hasNext()){
                                                    if(reader.getNextChar()=='r'){
                                                        if(reader.hasNext()){
                                                            if(reader.getNextChar()=='s'){
                                                                if(reader.hasNext()){
                                                                    if(reader.getNextChar()=='e'){
                                                                        if(reader.hasNext()){
                                                                            //@start parsing
                                                                            if(reader.getNextChar()=='N'){
                                                                                if(reader.hasNext()){
                                                                                    if(reader.getNextChar()=='u'){
                                                                                        if(reader.hasNext()){
                                                                                            if(reader.getNextChar() =='m'){
                                                                                                if(reader.hasNext()){
                                                                                                    if(reader.getNextChar()=='b'){
                                                                                                        if(reader.hasNext()){
                                                                                                            if(reader.getNextChar()=='e'){
                                                                                                                if(reader.hasNext()){
                                                                                                                    if(reader.getNextChar()=='r'){
                                                                                                                        if(reader.hasNext()){
                                                                                                                            return backVerify("parseNumber");
                                                                                                                        }
                                                                                                                        else return reservedWords.get("parseNumber");
                                                                                                                    }
                                                                                                                    else return backVerify("parseNumbe");
                                                                                                                }
                                                                                                                else return verify(identifyWords("parseNumbe"));
                                                                                                            }
                                                                                                            else return backVerify("parseNumb");
                                                                                                        }
                                                                                                        else return verify(identifyWords("parseNumb"));
                                                                                                    }
                                                                                                    else return backVerify("parseNum");
                                                                                                }
                                                                                                else return verify(identifyWords("parseNum"));
                                                                                            }
                                                                                            else return backVerify("parseNu");
                                                                                        }
                                                                                        else return verify(identifyWords("parseNu"));
                                                                                    }
                                                                                    else return backVerify("parseN");
                                                                                }
                                                                            }//@end number
                                                                            //@@decimal
                                                                            else if (reader.getNextChar()=='D'){
                                                                                if(reader.hasNext()){
                                                                                    if(reader.getNextChar()=='e'){
                                                                                        if(reader.hasNext()){
                                                                                            if(reader.getNextChar()=='c'){
                                                                                                if(reader.hasNext()){
                                                                                                    if(reader.getNextChar()=='i'){
                                                                                                        if(reader.hasNext()){
                                                                                                            if(reader.getNextChar()=='m'){
                                                                                                                if(reader.hasNext()){
                                                                                                                    if(reader.getNextChar()=='a'){
                                                                                                                        if(reader.hasNext()){
                                                                                                                            if(reader.getNextChar()=='l'){
                                                                                                                                if(reader.hasNext()){
                                                                                                                                    return backVerify("parseDecimal");
                                                                                                                                }
                                                                                                                                else return reservedWords.get("parseDecimal");
                                                                                                                            }
                                                                                                                            else return backVerify("parseDecima");
                                                                                                                        }
                                                                                                                        else return verify(identifyWords("parseDecima"));
                                                                                                                    }
                                                                                                                    else return backVerify("parseDecim");
                                                                                                                }
                                                                                                                else return verify(identifyWords("parseDecim"));
                                                                                                            }
                                                                                                            return backVerify("parseDeci");
                                                                                                        }
                                                                                                        else return verify(identifyWords("parseDeci"));
                                                                                                    }
                                                                                                    else return backVerify("parseDec");
                                                                                                }
                                                                                                else return verify(identifyWords("parseDec"));
                                                                                            }
                                                                                            else return backVerify("parseDe");
                                                                                        }
                                                                                        else return verify(identifyWords("parseDe"));
                                                                                    }
                                                                                    else return backVerify("parseD");
                                                                                }
                                                                                else return verify(identifyWords("parseD"));
                                                                            }
                                                                            //@end parsing
                                                                            else return backVerify("parse");
                                                                        }
                                                                        else return verify("parse");
                                                                    }
                                                                    else return backVerify("pars");
                                                                }
                                                                else return verify(identifyWords("pars"));
                                                            }
                                                            else return backVerify("par");
                                                        }
                                                        else return verify(identifyWords("par"));
                                                    }
                                                    else return backVerify("pa");
                                                }
                                                else return verify(identifyWords("pa"));
                                            }
                                            else return backVerify("p");
                                        }
                                        else return verify(identifyWords("p"));
                                    
                                case 'r':if(reader.hasNext()){
                                            if(reader.getNextChar()=='e'){
                                                if(reader.hasNext()){
                                                    if(reader.getNextChar() == 'a'){
                                                        if(reader.hasNext()){
                                                            if(reader.getNextChar() == 'd'){
                                                                if(reader.hasNext()){
                                                                    //start of reading
                                                                    //@@number
                                                                    if(reader.getNextChar()=='N'){
                                                                        if(reader.hasNext()){
                                                                            if(reader.getNextChar()=='u'){
                                                                                if(reader.hasNext()){
                                                                                    if(reader.getNextChar() =='m'){
                                                                                        if(reader.hasNext()){
                                                                                            if(reader.getNextChar()=='b'){
                                                                                                if(reader.hasNext()){
                                                                                                    if(reader.getNextChar()=='e'){
                                                                                                        if(reader.hasNext()){
                                                                                                            if(reader.getNextChar()=='r'){
                                                                                                                if(reader.hasNext()){
                                                                                                                    return backVerify("readNumber");
                                                                                                                }
                                                                                                                else return reservedWords.get("readNumber");
                                                                                                            }
                                                                                                            else return backVerify("readNumbe");
                                                                                                        }
                                                                                                        else return verify(identifyWords("readNumbe"));
                                                                                                    }
                                                                                                    else return backVerify("readNumb");
                                                                                                }
                                                                                                else return verify(identifyWords("readNumb"));
                                                                                            }
                                                                                            else return backVerify("readNum");
                                                                                        }
                                                                                        else return verify(identifyWords("readNum"));
                                                                                    }
                                                                                    else return backVerify("readNu");
                                                                                }
                                                                                else return verify(identifyWords("readNu"));
                                                                            }
                                                                            else return backVerify("readN");
                                                                        }
                                                                    }
                                                                        
                                                                    
                                                                    
                                                                    //@@string
                                                                    else if(reader.getNextChar()=='S'){
                                                                        if(reader.hasNext()){
                                                                            if(reader.getNextChar()=='t'){
                                                                                if(reader.hasNext()){
                                                                                    if(reader.getNextChar() == 'r'){
                                                                                        if(reader.hasNext()){
                                                                                            if(reader.getNextChar()=='i'){
                                                                                                if(reader.hasNext()){
                                                                                                    if(reader.getNextChar()=='n'){
                                                                                                        if(reader.hasNext()){
                                                                                                            if(reader.getNextChar()=='g'){
                                                                                                                if(reader.hasNext()){
                                                                                                                    return backVerify("readString");
                                                                                                                }
                                                                                                                else return reservedWords.get("readString");
                                                                                                            }
                                                                                                            else return backVerify("readStrin");
                                                                                                        }
                                                                                                        else return verify("readStrin");
                                                                                                    }
                                                                                                    else return backVerify("readStri");
                                                                                                }
                                                                                                else return verify(identifyWords("readStri"));
                                                                                            }
                                                                                            else return backVerify("readStr");
                                                                                        }
                                                                                        else return verify(identifyWords("readStr"));
                                                                                    }
                                                                                    else return backVerify("readSt");
                                                                                }
                                                                                else return verify(identifyWords("readSt"));
                                                                            }
                                                                            else return backVerify("readS");
                                                                        }
                                                                    }
                                                                     
                                                                    
                                                                    //@@decimal
                                                                    else if (reader.getNextChar()=='D'){
                                                                        if(reader.hasNext()){
                                                                            if(reader.getNextChar()=='e'){
                                                                                if(reader.hasNext()){
                                                                                    if(reader.getNextChar()=='c'){
                                                                                        if(reader.hasNext()){
                                                                                            if(reader.getNextChar()=='i'){
                                                                                                if(reader.hasNext()){
                                                                                                    if(reader.getNextChar()=='m'){
                                                                                                        if(reader.hasNext()){
                                                                                                            if(reader.getNextChar()=='a'){
                                                                                                                if(reader.hasNext()){
                                                                                                                    if(reader.getNextChar()=='l'){
                                                                                                                        if(reader.hasNext()){
                                                                                                                            return backVerify("readDecimal");
                                                                                                                        }
                                                                                                                        else return reservedWords.get("readDecimal");
                                                                                                                    }
                                                                                                                    else return backVerify("readDecima");
                                                                                                                }
                                                                                                                else return verify(identifyWords("readDecima"));
                                                                                                            }
                                                                                                            else return backVerify("readDecim");
                                                                                                        }
                                                                                                        else return verify(identifyWords("readDecim"));
                                                                                                    }
                                                                                                    return backVerify("readDeci");
                                                                                                }
                                                                                                else return verify(identifyWords("readDeci"));
                                                                                            }
                                                                                            else return backVerify("readDec");
                                                                                        }
                                                                                        else return verify(identifyWords("readDec"));
                                                                                    }
                                                                                    else return backVerify("readDe");
                                                                                }
                                                                                else return verify(identifyWords("readDe"));
                                                                            }
                                                                            else return backVerify("readD");
                                                                        }
                                                                        else return verify(identifyWords("readD"));
                                                                    }
                                                                    
                                                                    //@character
                                                                    
                                                                    else if(reader.getNextChar()=='C'){
                                                                        if(reader.hasNext()){
                                                                            if(reader.getNextChar()=='h'){
                                                                                if(reader.hasNext()){
                                                                                    if(reader.getNextChar()=='a'){
                                                                                        if(reader.hasNext()){
                                                                                            if(reader.getNextChar()=='r'){
                                                                                                if(reader.hasNext()){
                                                                                                    if(reader.getNextChar()=='a'){
                                                                                                        if(reader.hasNext()){
                                                                                                            if(reader.getNextChar()=='c'){
                                                                                                                if(reader.hasNext()){
                                                                                                                    if(reader.getNextChar()=='t'){
                                                                                                                        if(reader.hasNext()){
                                                                                                                            if(reader.getNextChar()=='e'){
                                                                                                                                if(reader.hasNext()){
                                                                                                                                    if(reader.getNextChar()=='r'){
                                                                                                                                        if(reader.hasNext()){
                                                                                                                                            return backVerify("readCharacter");
                                                                                                                                        }
                                                                                                                                        else return reservedWords.get("readCharacter");
                                                                                                                                    }
                                                                                                                                    else return backVerify("readCharacte");
                                                                                                                                }
                                                                                                                                else return verify(identifyWords("readCharacte"));
                                                                                                                            }
                                                                                                                            else return backVerify("readCharact");
                                                                                                                        }
                                                                                                                        else return verify(identifyWords("readCharact"));
                                                                                                                    }
                                                                                                                    else return backVerify("readCharac");
                                                                                                                }
                                                                                                                else return verify(identifyWords("readCharac"));
                                                                                                            }
                                                                                                            else return backVerify("readChara");
                                                                                                        }
                                                                                                        else return verify(identifyWords("readChara"));
                                                                                                    }
                                                                                                    else return backVerify("readChar");
                                                                                                }
                                                                                                else return verify(identifyWords("readChar"));
                                                                                            }
                                                                                            else return backVerify("readCha");
                                                                                        }
                                                                                        else return verify(identifyWords("readCha"));
                                                                                    }
                                                                                    else return backVerify("readCh");
                                                                                }
                                                                                else return verify(identifyWords("readCh"));
                                                                            }
                                                                            else return backVerify("readC");
                                                                        }
                                                                        else return verify(identifyWords("readC"));
                                                                    }
                                                                    //end of reading
                                                                    else return verify(identifyWords("readN"));
                                                                        
                                                                }
                                                                else return verify(identifyWords("read"));
                                                            }
                                                            else return backVerify("rea");
                                                        }
                                                        else return verify(identifyWords("rea"));
                                                    }
                                                    //@@return
                                                    else if(reader.getNextChar()=='t'){
                                                        if(reader.hasNext()){
                                                            if(reader.getNextChar()=='u'){
                                                                if(reader.hasNext()){
                                                                    if(reader.getNextChar()=='r'){
                                                                        if(reader.hasNext()){
                                                                            if(reader.getNextChar()=='n'){
                                                                                if(reader.hasNext()){
                                                                                    return backVerify("return");
                                                                                }
                                                                                else return reservedWords.get("return");
                                                                            }
                                                                            else return backVerify("retur");
                                                                        }
                                                                        else return verify(identifyWords("retur"));
                                                                    }
                                                                    else return backVerify("retu");
                                                                }
                                                                else return verify(identifyWords("retu"));
                                                            }
                                                            else return backVerify("ret");
                                                        }
                                                        else return verify(identifyWords("ret"));
                                                    }
                                                    else return backVerify("re");
                                                }
                                                else return verify(identifyWords("re"));
                                            }
                                            else return backVerify("r");
                                        }
                                        else return verify(identifyWords("r"));
                                    
                                    //=====================================================================================================end of R nest
                                case 's':if(reader.hasNext()){
                                            if(reader.getNextChar()=='t'){
                                                if(reader.hasNext()){
                                                    if(reader.getNextChar() == 'r'){
  
                                                        if(reader.hasNext()){
                                                            if(reader.getNextChar()=='i'){
                                                                if(reader.hasNext()){
                                                                    if(reader.getNextChar()=='n'){
                                                                        if(reader.hasNext()){
                                                                            if(reader.getNextChar()=='g'){
   
                                                                                if(reader.hasNext()){
                                                                                    return backVerify("string");
                                                                                }
                                                                                else return reservedWords.get("string");
                                                                            }
                                                                            else return backVerify("strin");
                                                                        }
                                                                        else return verify("strin");
                                                                    }
                                                                    else return backVerify("stri");
                                                                }
                                                                else return verify(identifyWords("stri"));
                                                            }
                                                            else return backVerify("str");
                                                        }
                                                        else return verify(identifyWords("str"));
                                                    }
                                                    else return backVerify("st");
                                                }
                                                else return verify(identifyWords("st"));
                                            }
                                            else return backVerify("s");
                                        }
                                        else return verify(identifyWords("s"));
                                    
                                case 't':if(reader.hasNext()){
                                            if(reader.getNextChar()=='r'){
                                                if(reader.hasNext()){
                                                    if(reader.getNextChar() =='u'){
                                                        if(reader.hasNext()){
                                                            if(reader.getNextChar()=='e'){
                                                                if(reader.hasNext()){
                                                                    return backVerify("true");
                                                                }
                                                                else return reservedWords.get("true");
                                                            }
                                                            else return verify(identifyWords("tru"));
                                                        }
                                                        else return verify(identifyWords("tru"));
                                                    }
                                                    else return backVerify("tr");
                                                }
                                                else return verify(identifyWords("tr"));
                                            }
                                            else return backVerify("t");
                                        }
                                        else return verify(identifyWords("t"));
                                    
                                case 'w':if(reader.hasNext()){
                                            if(reader.getNextChar() == 'h'){
                                                if(reader.hasNext()){
                                                    if(reader.getNextChar() =='i'){
                                                        if(reader.hasNext()){
                                                            if(reader.getNextChar() =='l'){
                                                                if(reader.hasNext()){
                                                                    if(reader.getNextChar()=='e'){
                                                                        if(reader.hasNext()){
                                                                            return backVerify("while");
                                                                        }
                                                                        else return reservedWords.get("while");
                                                                    }
                                                                    else return backVerify("whil");
                                                                }
                                                                else return verify(identifyWords("whil"));
                                                            }
                                                            else return backVerify("whi");
                                                        }
                                                        else return verify(identifyWords("whi"));
                                                    }
                                                    else return backVerify("wh");
                                                }
                                                else return verify(identifyWords("wh"));
                                            }
                                            else return backVerify("w");
                                        }
                                        else return verify(identifyWords("w"));
                                    
                                default :if(ALPHAlow.indexOf(ch)>=0){           //if it starts with any other letter, it must be an ID
                                            return verify(identifyWords(ch + ""));
                                        }
                            }

                            return null;
            }
        }
    }
        
    public String identifyWords(String str){
        String s = new String();
        while(reader.hasNext()){
            char ch = reader.getNextChar();
            if(ALPHANUM.indexOf(ch) >=0 | DELIMITERSopen.indexOf(ch)>=0 && DELIMITERSclose.indexOf(ch)<0){
                s+= ch;
                if(DELIMITERSopen.indexOf(ch)>=0){
                    reader.backread();
                    break;
                }
            }
            else {
                reader.backread();
                break;
            }
        }
        return str + s;
    }
    
    public Token tokenNum(){
        String s = new String();
        int dotted = 0;
        while(reader.hasNext()){
            char ch = reader.getNextChar();
            if((NUM + '.').indexOf(ch) >=0 & (DELIMITERSclose + DELIMITERSopen).indexOf(ch) <0){
                //if no dot found
                if(dotted==0 && ch!= '.') s+= ch;
                //if dot found once
                else if(dotted == 0 && ch =='.'){
                    dotted++;
                    s+= ch;
                }
                else if(dotted==1 && ch!= '.') s+= ch;
                else if (dotted == 1 && ch =='.') {
                    reader.backread();
                    break;
                }
                //if dot found twice
            }
            else {  //if not a num anymore
                reader.backread();
                break;
            }
        }
        if(dotted == 0) return new TokenLiteral(s);
        else return new TokenLiteral(s);
    }
    public Token verify(String str){
        Token t;
        //check if func, array, or enum

        if(str.indexOf('(')>=0 | str.indexOf('{')>=0 | str.indexOf('[')>=0){
            if(str.indexOf('(')>=0){             //must be a function
                str = str.replace('(', '\0');
                if(keywords.get(str) == null) keywords.put(str, new TokenFunctionIdentifier(str));
            }
            else if(str.indexOf('[')>=0){        //must be an array
                str = str.replace('[', '\0');
                if(keywords.get(str) == null) keywords.put(str, new TokenArrayIdentifier(str));
            }
            else if(str.indexOf('{')>=0){                               //should be enums
                str = str.replace('{', '\0');
                if(keywords.get(str) == null) keywords.put(str, new TokenEnumIdentifier(str));
            }
        }

        if((t=reservedWords.get(str)) != null) return t;
        else if((t=keywords.get(str)) != null) return t;
        else{
            keywords.put(str, new TokenIdentifier(str));
            return keywords.get(str);
        }
        
    }
    
    public Token backVerify(String str){
        reader.backread();
        return verify(identifyWords(str));
    }
    
    public Token readComment(){
            while(reader.hasNext()){
                if(reader.getNextChar()=='~') break;
                else reader.skip();
            }
        return reservedWords.get("comment");
    }
    
    public Token readString(){
        String s = "";
        boolean hasnext = true;
        while((hasnext = reader.hasNext())){
            if(reader.getNextChar()!='\"') s+=reader.getNextChar();
            else break;
        }
        if(hasnext == false) return new TokenError("Reached end of File");
        else return new TokenLiteral(s);
    }
    
    public void escapeError(){
        System.out.print("[Error occured at row " + (reader.getRow()-1) + " col " + (reader.getCol()-1)+"]");
        while(reader.hasNext()){
            if((DELIMITERSclose + DELIMITERSopen).indexOf(reader.getNextChar()) >=0){
                break;
            }
            else reader.skip();
        }
    }
    
    public Token readChar(){
        if(reader.hasNext()){
            if(reader.getNextChar()=='\\'){ //escape chars
                if(reader.hasNext()){
                    char ch;
                    switch(reader.getNextChar()){
                        case 'n': 
                        case 't': 
                        case '\\':
                        case '\'':
                        case '\"': ch = reader.getNextChar();
                                    if(reader.hasNext()){
                                        if(reader.getNextChar()!= '\''){
                                            escapeError();
                                            return new TokenError("Bad character");
                                        }
                                        else return new TokenLiteral("\\" + ch);
                                    }
                                    else return new TokenLiteral("\\" + ch);
                        default: return new TokenError("Bad character");
                    }
                }
                else return new TokenError("Dangling escape character");
            }
            else{   //regular chars
                char ch = reader.getNextChar();
                if(reader.hasNext()){
                    if(reader.getNextChar()!='\''){ //if it doesnt end there
                        escapeError();
                        return new TokenError("Bad character");
                    }
                    else return new TokenLiteral(ch + "");
                }
                else return new TokenLiteral(ch+"");
            }
        }
        else return new TokenError("Reached end of File");
    }
    
}
