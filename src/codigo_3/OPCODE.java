package codigo_3;

public class OPCODE {
//******* CONSTANTES PARA el Codigo de Operación (usan el prefijo "OP" de "OPeration").  ¡¡NO MODIFIQUE SUS VALORES!!
    public static final int NOP     = 0;      //No-Operation  
    public static final int NL      = 1;      //Salto de línea.
    public static final int RET     = 2;      //return.
    
    public static final int CALL    = 3;      //e.g. CALL Proc
    public static final int GOTO    = 4;      //e.g. GOTO Etiqueta
    
    public static final int INC     = 5;      //e.g. INC Var
    public static final int DEC     = 6;      //e.g. DEC Var
    
    public static final int WRITE   = 7;      //e.g. WRITE(VAR)
    public static final int READ    = 8;      //e.g. READ(Var)
    public static final int WRITES  = 9;      //e.g. WRITES(StringCtte)
    
    public static final int ETIQUETA  = 10;     //e.g.  E1
    
    public static final int MINUS = 11;     // -  unario aritmético.
    public static final int NOT   = 12;     // !  (not)
    public static final int AND   = 13;     // &&   e.g. var = var and var
    public static final int OR    = 14;     // ||

    public static final int SUMA    = 15;     // +    e.g. var = var + var
    public static final int MENOS   = 16;     // -
    public static final int POR     = 17;     // *
    public static final int DIV     = 18;     // DIV  (división entera)
    public static final int MOD     = 19;     // MOD
    
    public static final int MEN     = 20;     // <    e.g. var = (var < var)
    public static final int MAY     = 21;     // >
    public static final int IGUAL   = 22;     // ==
    public static final int DIS     = 23;     // !=  (distinto)
    public static final int MEI     = 24;     // <=
    public static final int MAI     = 25;     // >=
    
    public static final int IF0     = 26;     //e.g. IF (Var=0) => GOTO Etiqueta
    public static final int IF1     = 27;     //e.g. IF (Var=1) => GOTO Etiqueta 
    
    public static final int ASIGNID = 28;     //e.g. Var=Var
    public static final int ASIGNNUM= 29;     //e.g. Var=Número
//*******    
    
    public static boolean isOpCodeValido(int OpCode){
        return (NOP <= OpCode && OpCode <= ASIGNNUM);
    }
    
    
    public static int getCantDirs(int OpCode){  //Devuelve la cantidad de Direcciones que usa la OpCode dada.            
        if (CALL <= OpCode && OpCode <=ETIQUETA)
            return 1;   //Estas OP usan una dirección.  Por ejemplo:  CALL Dir1. 
        
        if (AND <= OpCode && OpCode <=MAI)
            return 3;   //Estas OP usan 3 direcciones. (e.g. OPSUMA: Dir1 = Dir2 + Dir3)
                
        if ((IF0 <= OpCode && OpCode <=ASIGNNUM) || OpCode == MINUS || OpCode == NOT)
            return 2;   //Estas OP usan 3 direcciones. (e.g. NOT: Dir1 = NOT Dir2)
        
        return 0;   
    }    

    
//*******    
    public static final String DESCONOCIDO = "??";
    
    public static String Mnemonico(int OpCode){    //Devuelve el nombre de ctte del OpCode dado (vease arriba las "public static final int")
        final String M[]={"NOP", "NL", "RET", 
                          "CALL", "GOTO", "INC", "DEC", "WRITE", "READ", "WRITES", "ETIQUETA", 
                          "MINUS", "NOT", "AND", "POR", "SUMA", "MENOS", "POR", "DIV", "MOD", 
                          "MEN", "MAY", "IGUAL", "DIS", "MEI", "MAI", "IF0", "IF1", "ASIGNID", "ASIGNNUM"};
        
        if (isOpCodeValido(OpCode))
            return M[OpCode];
        
        return DESCONOCIDO;        
    }

    
    public static String opCodeToStr(int opCode){    //Devuelve el lexema que caracteriza al opCode dado.
        final String Simbolo[]={"NOP", "NL", "RET", "CALL", "GOTO", "INC", "DEC", "WRITE", "READ", "WriteS", "E", "-", 
                                "NOT", "AND", "OR", "+", "-", "*", "DIV", "MOD", "<", ">", "==", "!=", "<=", ">=", "IF", "IF"};        
        
        if (NOP <= opCode && opCode <= IF1)
            return Simbolo[opCode];
        
        return DESCONOCIDO;        
    }
    
}
