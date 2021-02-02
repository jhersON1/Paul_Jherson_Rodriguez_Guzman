package codigo_3;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Tupla { //Esta class representa la tupla de la TSID.
    private String nombreID;
    private int valor;      //Si es un proc. es el inicio de su c3; si es una variable aquí la VM almacena su valor.
    private int cantTmp;    //Si cantTmp >=0 es procedimiento; caso contrario es una variable.

    public Tupla(){
        set("", 0, 0);
    }
    
    public Tupla(String NombreID, int Valor, int CantTmp) {
        set(NombreID, Valor, CantTmp);
    }

    public void setNombreID(String NombreID) {
        this.nombreID = NombreID;
    }
    
    public void setValor(int Valor) {
        this.valor = Valor;
    }
    

    public void setCantTmp(int CantTmp) {
        this.cantTmp = CantTmp;
    }
    

    public String getNombreID() {
        return nombreID;
    }
  
    
    public int getValor() {
        return valor;
    }
    
    
    public int getCantTmp() {
        return cantTmp;
    }

    public final void set(String NombreID, int Valor, int CantTmp){  //Actualiza TODOS los campos.
        this.nombreID = NombreID;
        this.valor    = Valor;
        this.cantTmp  = CantTmp;
    }
    
    public boolean isProc(){    //Devuelve true sii la Tupla almacena un procedimiento.
        return (cantTmp >= 0);
    }
    
    public boolean isVar(){
        return !isProc();
    }
    
    
    
    boolean save(DataOutputStream F){    //Guarda la tupla al flujo F. Si hay error, return false.
        try{
            F.writeUTF(nombreID);
            F.writeInt(valor);
            F.writeInt(cantTmp);
            return true;
        } catch(Exception e){}
        
        return false;
    }

    
    boolean open(DataInputStream F){ //Lee del flujo F, una tupla.
        try{
            nombreID = F.readUTF();
            valor    = F.readInt();
            cantTmp  = F.readInt();
            return true;
        }catch (Exception e){}
        
        return false;
    }

  
    @Override
    public Tupla clone(){
        return new Tupla(nombreID, valor, cantTmp);
    }
    
    
    public String fieldToString(int nroCampo){     //Para pantalleo.       
        switch (nroCampo){
            case 0  :   return nombreID;      //Devolver el Campo 0 (NombreID)
            case 1  :   return ""+valor;      //Devolver el Campo 1 (Valor)
        }
        
            //Devolver el Campo 2 (CantTmp)
        if (isProc())   //La Tupla almacena un Proc.
            return ""+cantTmp;        
        
        return (cantTmp==TSID.TIPOBOOLEAN?"TIPOBOOLEAN":"TIPOINT");     //La Tupla almacena una Var.
    }
    
}
 