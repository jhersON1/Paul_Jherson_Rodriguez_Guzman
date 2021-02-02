package codigo_3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Codigo3{
    static final int MAXCUADRUPLAS=500;
    public String sInterfaz="";
    
    public Cuadrupla V[];
    public int n;          //Dimensión de V[].
           
    
    public Codigo3(){
        V = new Cuadrupla[MAXCUADRUPLAS+1];
        n = -1;
        TS.create();
    }   
 
    /** @return la tabla de StringCttes (TSS)*/
    public TSS getTSS(){
        return TS.getTSS();
    }
    
    /** @return la tabla de Identificadores (TSID)*/
    public TSID getTSID(){
        return TS.getTSID();
    }
           
    public final void init(){ //Reset. Pone el c3 vacío.
        n = -1;
    }
    
    
    public int lenght(){ //Devuelve la cantidad de cuadruplas insertados en el Codigo3.
        return n+1;
    }  
       
    
    public int add(int opCode, int Dir1, int Dir2, int Dir3){ //Inserta la Cuadrupla (TipoOp, Dir1, Dir2, Dir3) devolviendo la posición donde fue insertada.
        if (n < MAXCUADRUPLAS){
            n++;
            if (V[n]==null)
                V[n] = new Cuadrupla(opCode, Dir1, Dir2, Dir3, this);
            else
                V[n].set(opCode, Dir1, Dir2, Dir3);
            
            return n;    
        }
        
        return -1;  //La Cuadrupla no fue insertada.
    }
    
    public int add(int opCode){ //Inserta la Cuadrupla (TipoOp, 0, 0, 0) devolviendo la posición donde fue insertada.
        return add(opCode, 0, 0, 0); 
    }

    
    public int add(int opCode, int Dir1){ //Inserta la Cuadrupla (TipoOp, Dir1, 0, 0) devolviendo la posición donde fue insertada.
        return add(opCode, Dir1, 0, 0); 
    }

    
    public int add(int opCode, int Dir1, int Dir2){ //Inserta la Cuadrupla (TipoOp, Dir1, Dir2, 0) devolviendo la posición donde fue insertada.
        return add(opCode, Dir1, Dir2, 0); 
    }
    
    
    public int add(Cuadrupla C){ //Inserta la Cuadrupla C al Codigo3, devolviendo la posición donde fue insertada.
        if (n < MAXCUADRUPLAS && C!=null){
            n++;
            V[n]=C;
            V[n].c3 = this;
            return n;
        }
        
        return -1;  //La cuadrupla no fue insertada.
    }
    
    
    public Cuadrupla getCuadrupla(int index){   //Devuelve la Cuadrupla de la posición index (0<= Index <=length()-1)
        if (indexCorrecto(index))
            return V[index];
        
        return null;
    }

    
    public boolean indexCorrecto(int index){
        return (0 <= index && index <= n);
    }
    
        
    public void println(){          //Para mostrar éste c3 y las Tablas en consola.
        TS.println();
        System.out.println(this);
    }
    
    public boolean save(String Filename){  //Guarda el C3, con sus tablas TSID y TSS. Si hay error, devuelve false.
        DataOutputStream out = OpenToWrite(Filename);
        if (out == null)
            return false;
                
        boolean b = TS.save(out);   //Guardar las Tablas.
        if (b){ //Las tablas se guardaron correctamente.  Guardar el C3
            try{
                out.writeInt( lenght() );       //Guardar cantidad de casillas de V[].
            
                    //Guardar las cuadruplas           
                for (int i=0; i <=n; i++) 
                    V[i].Save(out);
                 
                out.close();
            }catch(Exception e){ 
                b=false;
            }
        } //End if
        
        if (!b)
            System.err.println("Codigo3.Save: Error al escribir en el archivo "+'"'+Filename+'"');
        
        return b;
    }
    
    
    public boolean open(String Filename){        
        DataInputStream in = OpenToRead(Filename);
        if (in==null)
            return false;
        
        boolean b = TS.Copy(in);    //Leer Tablas, pero no actualizarlas.
               
        if (b){ //Las tablas se leyeron correctamente.  Leer el C3.
            try{
                Codigo3 C3 = new Codigo3();
                
                C3.n = in.readInt() - 1;       //Leer cantidad de casillas.
            
                    //Leer las casillas (Cuadruplas)
                for (int i=0; i <=C3.n; i++){ 
                    C3.V[i] = new Cuadrupla(this);
                    C3.V[i].Open(in);
                }    
                            
                    
                in.close();
            
                    //Todo se leyo bien.  Cambiar a lo nuevo.
                this.n = C3.n;
                for (int i=0; i<=this.n; i++)
                    this.V[i]=C3.V[i];
                
                
                TS.Paste();     //Actualizar las tablas.
            
            }catch(Exception e){
                b=false;
            }
        } //End if
        
        if (!b)
            System.err.println("Codigo3.Open: El archivo "+'"'+Filename+'"'+ " no tiene el formato de un .c3");
        
        return b;
    
    }    
    
   
    public int frecEtiqueta(int nroLabel){  //Usado por la class Cuadrupla
        int c=0;
        for (int i=0; i<=n; i++){
            if (V[i].getOpCode()== OPCODE.ETIQUETA && V[i].getDir(1)==nroLabel)
                c++;
        }
        
        return c;
    }
    
    public String getLinea(int line){
        if (lenght()==0)
            return "(Código3 Vacio)";
        if(line < V.length){
            return Utils.FieldRight(""+line, 3)+" "+V[line]+"\n";
        }else{
            return "Fuera de rango";
        }
    }
    
    @Override
    public String toString(){  //Para usar con Sytem.out.Print.
        if (lenght()==0)
            return "(Código3 Vacio)";
        
        final char LF ='\n';
        String S = "";
        for (int i=0; i<=n; i++ ){
          S += Utils.FieldRight(""+i, 3)+" "+V[i];
          String nombreProc = getTSID().getNombreProc(i);
          if (nombreProc.length() > 0)
              S += "  //Inicio de " + nombreProc;
          
          S += LF;
        }
        return S;
    } 
    //--------------------------------------------------------------------------
    // Este string es para la interfaz, para que se ejecute paso por paso 
    //--------------------------------------------------------------------------
    
    public String toString(int i){  //Para usar con Sytem.out.Print.
        if (lenght()==0)
            return "(Código3 Vacio)";
        
        final char LF ='\n';
       // String S = "";
       //for (int i=0; i<=n; i++ ){
        if (i<=n){
          sInterfaz += Utils.FieldRight(""+i, 3)+" "+V[i];
          String nombreProc = getTSID().getNombreProc(i);
          if (nombreProc.length() > 0)
              sInterfaz += "  //Inicio de " + nombreProc;
          
          sInterfaz += LF;
        }
        return sInterfaz;
    } 

    
    
    private DataOutputStream OpenToWrite(String Filename){ //Abre, para escribir, el archivo FileName, retornando el flujo abierto.
        try{
            FileOutputStream F   = new FileOutputStream(Filename);    //Crear archivo (sobreescribiendo si existiese).
            DataOutputStream Out = new DataOutputStream(F);     
            return Out;
        }catch(IOException e){
            System.err.println("Codigo3.Save: Error al guardar "+e.getMessage());
        }

        return null;
    }
    
    private DataInputStream OpenToRead(String Filename){ //Abre, para leer, el archivo FileName, retornando el flujo abierto.
        try{
            FileInputStream F  = new FileInputStream(Filename);     
            DataInputStream in = new DataInputStream(F);
            return in;
        }catch(IOException e){
            System.err.println("Codigo3.Open: Error al abrir "+e.getMessage());
        }
        
        return null;
    }    
    
}


  
