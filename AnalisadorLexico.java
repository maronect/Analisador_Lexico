import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalisadorLexico {
    static String word = "";
    static List<String> tabelaDeSimbolos = new ArrayList<>();
    static List<String> listaDeTokens = new ArrayList<>();

    static String stringHolder = "";
    static boolean isDecimal = false;


    static boolean buildingWd = false;
    static boolean buildingNb = false;
    static boolean buildingOp = false;

    static Map <String, String> lex = new HashMap<>();
    static Map <Character, String> lexChar = new HashMap<>();


    public static void main(String[] args) {
        
        lex.put( "int",   "Palavra reservada");
        lex.put( "float", "Palavra reservada");
        lex.put( "char",  "Palavra reservada");
        lex.put( "boolean", "Palavra reservada");
        lex.put( "void", "Palavra reservada");
        lex.put( "if",    "Palavra reservada");
        lex.put( "else",  "Palavra reservada");
        lex.put( "for",   "Palavra reservada");
        lex.put( "while", "Palavra reservada");
        lex.put( "scanf", "Palavra reservada");
        lex.put( "println" ,"Palavra reservada");
        lex.put( "main", "Palavra reservada");
        lex.put( "return", "Palavra reservada");

        lex.put( "&&", "Operadores");
        lex.put( "||", "Operadores");

        lex.put( ">=", "Operadores de comparacao");
        lex.put( "<=", "Operadores de comparacao");
        lex.put( "!=", "Operadores de comparacao");
        lex.put( "==", "Operadores de comparacao");


        lexChar.put( '=', "OperadoresBi");
        lexChar.put( '+', "Operadores");
        lexChar.put( '-', "Operadores");
        lexChar.put( '*', "Operadores");
        lexChar.put( '/', "Operadores");
        lexChar.put( '%', "Operadores");
        lexChar.put( '!', "OperadoresBi");
        lexChar.put( '>', "Operadores de comparacao");
        lexChar.put( '<', "Operadores de comparacao");

        lexChar.put( '(', "Simbolos");
        lexChar.put( ')', "Simbolos");
        lexChar.put( '[', "Simbolos");
        lexChar.put( ']', "Simbolos");
        lexChar.put( '{', "Simbolos");
        lexChar.put( '}', "Simbolos");
        lexChar.put( ',', "Simbolos");
        lexChar.put( ';', "Simbolos");
        

        String teste1 = "// Este é um comentário de linha\n" + //
                " int multiplicar(int x, int y) {\n" + //
                " /* Este é um comentário\n" + //
                " de múltiplas linhas */\n" + //
                " return x * y;\n" + //
                " }";
                
        String teste2 = " int a2a() {\n" + //
                "    int num1 = 123;\n" + //
                "    float num2 = 45.67;\n" + //
                "    char letra = 'A';\n" + //
                "    if (num1 > 100) {\n" + //
                "        println(\"Número maior que 100\");\n" + //
                "    }\n" + //
                "    return 0;\n" + //
                "}";


        reading(teste2);
        System.out.println("Tabela de simbolo\n\n");
        for (int index = 0; index < tabelaDeSimbolos.size(); index++) {
            System.out.println(tabelaDeSimbolos.get(index));
            
        }

        System.out.println("\n\nLista de token\n\n");
        for (int index = 0; index < listaDeTokens.size(); index++) {
            System.out.println(listaDeTokens.get(index));
            
        }
    }

    static void reading(String frase){
        
        int status = 0;
        
        for(int i = 0; i < frase.length(); i++) {
            char letter = frase.charAt(i);

            if(letter == '/' && frase.charAt(i + 1) == '/'){
                while(letter != '\n'){
                    i++;
                    letter = frase.charAt(i);
                }
            }

            if(letter == '/' && frase.charAt(i + 1) == '*'){
                while(!(letter == '*' && frase.charAt(i + 1) == '/')){
                    i++;
                    letter = frase.charAt(i);
                    
                }
                i = i + 2;
                letter = frase.charAt(i);
            }

            if( letter == '\n' ||letter == '\'' || letter == ' ' && (buildingWd == false && buildingOp == false && buildingNb == false ) ){
                
            }else{
                if(word == ""){

                    status = checkingStatus(letter);
                    
                }
                switch (status) {
                    case 0:
                        buildingWd = true;
                        wordCreator(letter);
                        // status = checkingStatus(letter);
                        if(!buildingWd && !buildingOp && !buildingNb && letter != ' '){
                            status = checkingStatus(letter);
                        }
                        break;
                    case 1:
                        buildingNb = true;
                        numberCreator(letter);
                        if(!buildingWd && !buildingOp && !buildingNb && letter != ' '){
                            status = checkingStatus(letter);
                        }
                        
                        break;
                    case 2:
                        buildingOp = true;
                        operatorCreator(letter);
                        
                        if(word == "" && letter != ' '){
                            status = checkingStatus(letter);
                            word += letter;

                        }
                        
                        break;
                
                    default:
                        break;
                }
            }
            

            char c = frase.charAt(i);

            if(lex.containsKey(c)){
                System.out.println("TEM C");
            }

            if(frase.charAt(i) == '"'){
                
                i++;
                while(frase.charAt(i) != '"'){
                    stringHolder += frase.charAt(i);
                    i++;
                }
                tabelaDeSimbolos.add(stringHolder);
                System.out.println(stringHolder);
                stringHolder = "";

            }
        }
    }

    static int checkingStatus(char let){
        if(Character.isLetter(let)){
            return 0;
        }else if(Character.isDigit(let)){
            return 1;
        }else{
            return 2;
        }
    }

    static void wordCreator(char letter){

        if(lexChar.get(letter) == "Operadores" || lexChar.get(letter) == "Operadores de comparacao"|| lexChar.get(letter) == "Simbolos" || letter == ' ' || letter == '\''){
            if(lex.containsKey(word) || lexChar.containsKey(word) ){
                listaDeTokens.add(word + "-" + lex.get(word));
            }else{
                if(!tabelaDeSimbolos.contains(word)){
                    tabelaDeSimbolos.add(word  );
                }
                int pos = tabelaDeSimbolos.indexOf(word) + 1;                             
                listaDeTokens.add("Id, " + pos + " - Identificador");
                buildingWd = false;

            }
                
            if(letter == ' '){
                buildingWd = false;
                word = "";
            }else{
                word = letter + "";
            }

        }else{
            word += letter;
        }
    }

    static void numberCreator(char letter){
        if(lexChar.get(letter) == "Operadores" || lexChar.get(letter) == "Operadores de comparacao"|| lexChar.get(letter) == "Simbolos" || Character.isLetter(letter) || letter == ' '){
            if(isDecimal){
                listaDeTokens.add("NUM_DEC " + word);
                isDecimal = false;

            }else{
                listaDeTokens.add("NUM_INT " + word);
            }

            if(letter == ' '){
                buildingNb = false;
                word = "";
            }else{
                word = letter + "";
                buildingNb = false;
            }

        }else{
            if(letter == '.' ){
                isDecimal = true;
            }
            word += letter;
        }
    }


    static void operatorCreator(char letter){
        if(lexChar.get(letter) == "Simbolos" || lexChar.get(letter) == "Operadores" || Character.isLetterOrDigit(letter)){
            
            if(word != ""){

                listaDeTokens.add(word + " - " + lexChar.get(word.charAt(0)));
            }
            word = "";
            buildingOp = false;

        }else if(lexChar.get(letter) == "OperadoresBi" || lexChar.get(letter) == "Operadores de comparacao"){
            word += letter;
            if(word.length() >= 2){
                listaDeTokens.add(word + " - " + lex.get(word));
                word = "";
                buildingOp = false;

            }
        }
    }
}