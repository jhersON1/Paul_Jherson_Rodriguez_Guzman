package codigo_3;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/** Esta class implementa el MODULO Tabla de Simbolos.  Desde esta
   class se manipulan todas las Tablas.
   
   Esta class, en realidad trabaja como un objeto.  Por lo tanto, no se debe instanciar esta class.
 */
public class TS {   
    private static TSID tsId;
    private static TSS  tss;
    
    static {
       create();             
    }
    
    
    public static void create(){
        if (tsId == null){
            tsId = new TSID();
            tss  = new TSS();
        }
    }
    
    /** Inicializa TODAS las Tablas de Símbolos del Compilador. 
     *  Es decir, hace TSS = TSID = (vacía)
     */
    static public void init(){  //Inicializar las tablas.     
        tsId.init();
        tss.init();
    }
    
    
    static public TSID getTSID(){
        return tsId;
    }
    
    
    static public TSS getTSS(){
        return tss;
    }
    
    
    static public void println(){   //Para mostrar las Tablas en consola.
        System.out.println(tsId);
        System.out.println(tss);
    }

    
    
//*******    
    static public boolean save(DataOutputStream F){    //Guarda las Tablas al Flujo F. Si hubo error al guardar, devuelve false.
        return tsId.save(F) && tss.save(F);
    }
    
    
    private static boolean Clipboard = false;
    private static TSID AuxTsID;
    private static TSS AuxTss;
            
    static public boolean Copy(DataInputStream F){  //Lee Tablas TSID y TSS del flujo F y las deposita en auxiliares.
        AuxTsID = TSID.open(F);
        AuxTss  = TSS.open(F);
        
        Clipboard = (AuxTsID != null && AuxTss != null);
        return Clipboard;
    }
    
    
    static public void Paste(){     //Copia las Tablas Auxiliares a las originales.
        if (Clipboard){
            tsId = AuxTsID;
            tss  = AuxTss;
            Clipboard = false;
        }
    }
    
}
