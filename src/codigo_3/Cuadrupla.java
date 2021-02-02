package codigo_3;

import java.io.DataInputStream;
import java.io.DataOutputStream;


public class Cuadrupla {
    private int opCode;         //Código de Operación (usa las constantes definidas en la class OPCODE).
    private int[] dir;          //Direcciones de memoria.
    Codigo3 c3;                 //c3 propietario de esta Cuadrupla.
        
    public Cuadrupla(Codigo3 c3){
       dir = new int[3];
       set(OPCODE.NOP, 0, 0, 0);
       this.c3 = c3;
    }

    public Cuadrupla(int opCode, int Dir1, int Dir2, int Dir3, Codigo3 c3){
        dir = new int[3]; 
        set(opCode, Dir1, Dir2, Dir3);
        this.c3 = c3;
    }
    

    public void setOpCode(int opCode) { //Setter del Código de Operación
        this.opCode = validar(opCode);
    }
    
    public void setDir(int index, int dir){  //Setter de la Dir de Memoria Index (1<=Index<=3)
        if (1 <= index && index <= 3)        //e.g  C.setDir(1, 2), actualizará la 1era. Dir. de Mem con 2.
            this.dir[index-1] = dir;
    }

    
    public int getOpCode() { //Getter del Tipo de Operación
        return opCode;
    }
    
    public int getDir(int index){  //Getter de la Dir de Memoria Index (1<=index<=3)
        if (1 <= index && index <= 3)
            return dir[index-1];
        
        return 0;
    }

    
    public final void set(int opCode, int Dir1, int Dir2, int Dir3){  //Actualiza TODOS los campos de la Cuadrupla.
       this.opCode = opCode;
       this.dir[0] = Dir1;
       this.dir[1] = Dir2;
       this.dir[2] = Dir3;
    }

    
    private int validar(int OpCode){ //Valida el Codigo de Operación
        if (OPCODE.isOpCodeValido(OpCode))
            return OpCode;
        
        return OPCODE.NOP;
    }
    
 
    boolean Save(DataOutputStream F){
        try{
            F.writeInt(opCode);
            F.writeInt(getDir(1));
            F.writeInt(getDir(2));
            F.writeInt(getDir(3));
            return true;
        } catch(Exception e){}
        
        return false;
    }

    
    boolean Open(DataInputStream F){
        try{
            opCode = F.readInt();
            setDir(1, F.readInt());
            setDir(2, F.readInt());
            setDir(3, F.readInt());
            return true;
        }catch (Exception e){}
        
        return false;
    }
    
//*******    
    private static final String DESCONOCIDO = OPCODE.DESCONOCIDO;
    
    @Override
    public String toString(){   //Devuelve la cuadrupla as String
        switch (OPCODE.getCantDirs(opCode)){
            case 1 : return toString1();
            case 2 : return toString2();
            case 3 : return toString3();                
        }
        
        return OPCODE.opCodeToStr(opCode);      //El OpCode de la Cuadrupla usa 0 direcciones.        
    }    
    

        
    private String toString1(){ //Devuelve la Cuadrupla as String sii su OpCode usa una sola Dir.
        int Dir1  = getDir(1);
        String op = OPCODE.opCodeToStr(opCode);     //op = lexema de la Operacion (e.g. op="CALL")
        switch (opCode){
            case OPCODE.CALL     : return op+" "+getNombreProc(Dir1);
            case OPCODE.ETIQUETA : return etiqueta(Dir1);
            case OPCODE.GOTO     : return op +" "+etiquetaGoto(Dir1);
            case OPCODE.WRITES   : return op +"("+getStrCtte(Dir1)+")";
        }
        
        String Var = getNombreVar(Dir1);
        
        if (opCode==OPCODE.WRITE || opCode==OPCODE.READ)
            return op + "("+Var+")";
        
        return op+" "+Var;      //Para INC y DEC.
    }
    
    
    private String toString2(){ //Devuelve la Cuadrupla as String sii su OpCode usa 2 Dir's.
        int Dir2     = getDir(2);
        String SDir1 = getNombreVar( getDir(1) );
        String SDir2 = getNombreVar( Dir2 );
        
        switch (opCode){
            case OPCODE.IF0      : return "IF ("+SDir1+"=0) => GOTO "+etiquetaGoto(Dir2);
            case OPCODE.IF1      : return "IF ("+SDir1+"=1) => GOTO "+etiquetaGoto(Dir2);
            case OPCODE.ASIGNNUM : return SDir1+" = "+Dir2;    
        }
        
            //Producir el String para OPASIGNID, OPMINUS y OPNOT.
        String op = OPCODE.opCodeToStr(opCode)+" ";
        if (opCode == OPCODE.ASIGNID)
            op ="";
        
        return SDir1+ " = "+ op + SDir2;
    }   
    
    
    private String toString3(){ //Devuelve la Cuadrupla as String sii su OpCode usa 3 Dir's.
        String op = OPCODE.opCodeToStr(opCode);     //Símbolo de la Operacion (e.g "AND")
        
        String SDir1 = getNombreVar( getDir(1) );
        String SDir2 = getNombreVar( getDir(2) );
        String SDir3 = getNombreVar( getDir(3) );
        
        if (OPCODE.AND <= opCode && opCode <=OPCODE.MOD)
            return SDir1+" = "+SDir2+" "+op+" "+SDir3;
        
        return SDir1+" = ("+SDir2+" "+op+" "+SDir3+")";     //Para las OPREL (<, >, =, <=, >=, !=).
    }
     
    
    private String etiqueta(int Dir){   //Corrutina de toString1 
        if (Dir<=0)
            return "E "+DESCONOCIDO;
        
        String warning = (c3.frecEtiqueta(Dir) > 1 ? "  //¡ETIQUETA DUPLICADA!" : "");
        return "E" + Dir + ":" + warning;
    }
    
    private String etiquetaGoto(int Dir){   //Corrutina de toString1 y toString2. 
       if (Dir<=0)
            return "E "+DESCONOCIDO;
       
        return "E" + Dir; 
    }
    
    private String getNombreVar(int Dir){   //Devuelve el Nombre de la variable dada por su Dir.
        if (Dir > 0)
            return "t"+Dir;     //La variable es un temporal
        
        Dir = -Dir;
        
        TSID TsID = c3.getTSID();   //Obtener la TSID.
        
        if (TsID.isVar(Dir))    //Verificar si en la posicion Dir de la TSID hay una Variable
            return TsID.getNombreID(Dir);
        
        return DESCONOCIDO;
    }

    
    
    private String getNombreProc(int dir){  //Devuelve el Nombre del Proc dado por su Dir.  
        TSID tsID = c3.getTSID();   //Obtener la TSID.
        dir = -dir;                 //Hacer positivo a dir
        
        if (tsID.isProc(dir))
           return tsID.getNombreID(dir);

        return DESCONOCIDO;        
    }

    
    private String getStrCtte(int index){
        TSS tss = c3.getTSS();      //Obtener la tabla de StringCtte´'s
        index   = -index;            //Hacer positivo a index.
         
        if (tss.posValida(index))
            return '"'+tss.getStr(index)+'"';
   
        return DESCONOCIDO;                
    }
    
    
}


