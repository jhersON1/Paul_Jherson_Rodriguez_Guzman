package codigo_3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

/** Esta class manipula la Tabla de Simbolos de los ID's.
 *  No instancie esta class, porque la class TS lo hace.
 */

public class TSID {   
        //CONSTANTES QUE DEFINEN LOS TIPOS DE LAS VARIABLES
    public static final int TIPOINT     = -2;
    public static final int TIPOBOOLEAN = -3;
               
    private ArrayList <Tupla> L;
       
    
    TSID(){     //Construye una tabla vacía
        L = new ArrayList<>();
    }
    
    
    public void init(){ //Inicializa la TSID.  Es decir, TSID=(Vacía).
        L.clear();
    }
    
    public int lenght(){    //Devuelve la cantidad de elementos de la tabla.
        return L.size();
    }
    
    
    /**
     * @param nombreID lexema del identificador.
     * @return la posición (índice) donde se encuentra alojado el ID cuyo nombre es nombreID. 
     *         Si no existe, devuelve -1.
     */
    public int indexOf(String nombreID){ //Devuelve el índice de L, donde se encuentra el ID con NombreID.
        nombreID = toNameID(nombreID);
        
        for (int i=0; i<L.size(); i++){
            if (L.get(i).getNombreID().equals(nombreID))
                return i;
        }
        return -1;      //El ID no existe en la tabla.
    }
    
    public int getIndexOfMain(){    //Devuelve el índice de L, donde se encuentra el $MAIN.
        return indexOf(MAINid);     //(si no existe, devuelve -1)
    }
    
    public int addProc(String NombreID){    //Inserta un nuevo Proc a la TSID con Valor=CantTmp=0.                                             
        return addTupla(NombreID, 0, 0);    //Devuelve la posición donde está/fue alojado.
    }
    
    public int addProc(String NombreID, int inicioc3, int cantTmp){
        return addTupla(NombreID, inicioc3, cantTmp);    //Devuelve la posición donde está/fue alojado.
    }
    
    
    public int addVar(String NombreID, int Tipo){ //Inserta una nueva Var con su Tipo  a la TSID. Devuelve la posición donde está/fue alojada.
        if (TipoCorrecto(Tipo))
            return addTupla(NombreID, 0, Tipo);
        
        return -1;      //Devolver -1 para que el llamante sepa que la Variable no se insertó a la tabla.        
    }
    
    
    public boolean isProc(int Index){  //Devuelve true sii la tupla de la posición Index es un Proc
        return (posValida(Index) && L.get(Index).isProc());
    }

    
    public boolean isVar(int Index){  //Devuelve true sii la tupla de la posición Index es una Var.
        return (posValida(Index) && L.get(Index).isVar());
    }

    
    public boolean TipoCorrecto(int Tipo){ //Devuelve true sii el int especificado, corresponde a TIPOBOOLEAN o TIPOINT.
        return (TIPOBOOLEAN <= Tipo && Tipo <= TIPOINT);
    }  

    
    public boolean posValida(int index){
        return (0 <= index && index < L.size());
    }
   
    
    public String getNombreProc(int inicioC3){
        for (int i=0; i<L.size(); i++){
            if (L.get(i).getValor() == inicioC3){
                if (isProc(i))
                    return getNombreID(i);
                
                return "";
            }    
        }
       
        return "";
    }
    
//**************** SETTER's & GETTER's de la Tupla Index ********************
    public void setValor(int Index, int Valor){   //Actualiza el campo Valor de la Tupla Index (sii es una Var).
        if (isVar(Index))
            L.get(Index).setValor(Valor);        
    }
    
    public void setTipo(int Index, int Tipo){   //Actualiza el campo CantTmp de la Tupla Index (sii es una Var).
        if (isVar(Index) && TipoCorrecto(Tipo))
            L.get(Index).setCantTmp(Tipo);        
    }
    
    
    public void setDirC3(int Index, int dirC3){   //Actualiza el campo Valor de la Tupla Index (sii es un Proc).
        if (isProc(Index))
            L.get(Index).setValor(Math.abs(dirC3) );
    }
    

    public void setCantTmp(int Index, int cantTmp){   //Actualiza el campo CantTmp de la Tupla Index (sii es un Proc).
        if (isProc(Index))
            L.get(Index).setCantTmp( Math.abs(cantTmp) );        
    }
    
    public void setBoundsProc(int index, int inicioC3, int cantTmp){
        if (isProc(index)){
            L.get(index).setValor(Math.abs(inicioC3) );
            L.get(index).setCantTmp( Math.abs(cantTmp) );
        }        
    }
    
    public Tupla getTupla(int index){ //Devuelve una COPIA de la Tupla de la posición Index.
        if (posValida(index))
            return L.get(index).clone();
        
        return null;
    }
  
    
    public String getNombreID(int Index){ //Devuelve el nombre del ID alojado en la posición Index
        if (posValida(Index))
            return L.get(Index).getNombreID();
        
        return "";
    }
    

    public int getValor(int Index){ //Devuelve el campo Valor de la tupla Index
        if (posValida(Index))
            return L.get(Index).getValor();
        
        return 0;
    }
    
    public int getCantTmp(int index){ //Devuelve el campo CantTmp de la tupla Index
        if (posValida(index))
            
            return L.get(index).getCantTmp();
        
        return -1;
    }
//****** END Setter's & Getter's de la tupla especificada en la pTS Index ******    
    
    
    
    
    boolean save(DataOutputStream F){    //Guarda la TSID al Flujo F. Si hubo error al guardar, devuelve false.        
        try{
                //Guardar la Cant. de Tuplas.
            F.writeInt( lenght() );

                //Guardar las Tuplas.
            boolean b = true;
            int i=0;
            while (b && i<L.size()){
                b = L.get(i).save(F);   //Guarda la tupla.
                i++;
            }
            
            return b;                
        }catch(Exception e){}
        
        return false;
    }

    
    static TSID open(DataInputStream F){    //Lee una TSID desde el Flujo F.
        TSID tabla = new TSID();
        boolean b  = true;
        
        try{            
                //Leer la Cantidad de tuplas almacenadas.            
            int n = F.readInt();
            
                //Leer las tuplas y depositarlas en la lista L de Tabla.            
            for (int i=1; i<= n && b; i++){
                Tupla t = new Tupla();
                b       = t.open(F);
                tabla.L.add(t);
            } 
        }
        catch(Exception e){ 
            b = false;
        }
        
        if (!b){
            tabla = null;   //Hubo error al leer la Tabla.
            System.err.println("TSID.open: Error al leer la tabla de ID's");
        }    
        
        return tabla;    
    }

    
 //--------------------------Pantalleo -----------------------------------------
    private static final String TITLE="TSID";
    private static final String HEADER[]={"Nombre", "Valor", "CantTmp"};
    
    @Override
    public String toString(){ //Para mostrar la TSID usando System.out.print
        if (lenght()==0)
            return "("+TITLE+" Vacía)";
        
        final char   LF ='\n';
        final String PADDLEFT = "   ";
        
        String Result;
        int FieldNombre  = FieldLength(0);
        int FieldValor   = FieldLength(1);
        int FieldCantTmp = FieldLength(2);
        int Total        = FieldNombre + FieldValor + FieldCantTmp;
        
        String Borde = PADDLEFT+"+"+Utils.RL('-', Total)+"+";
        
        String Title = PADDLEFT+Utils.FieldCenter(TITLE, Total);
        
        Result =  Title + LF;
        String Header    = PADDLEFT+"|"+
                           Utils.FieldCenter(HEADER[0], FieldNombre)+
                           Utils.FieldCenter(HEADER[1], FieldValor)+
                           Utils.FieldCenter(HEADER[2], FieldCantTmp)+"|";
        
        Result += Borde + LF +Header + LF + Borde + LF;
            
        for (int i=0; i<L.size(); i++){
            String Posicion = Utils.FieldRight(""+i, PADDLEFT.length())+"|";
            String Nombre   = Utils.FieldLeft(" "+getElem(i, 0), FieldNombre);
            String Valor    = Utils.FieldCenter(getElem(i, 1), FieldValor);
            String CantTmp  = Utils.FieldCenter(getElem(i, 2), FieldCantTmp)+"|";
            Result += Posicion + Nombre + Valor + CantTmp + LF;
        } //End For 
        
        return Result + Borde + LF;
    }
    
    private int FieldLength(int Col){   //Obtener la mayor length de los string's de la Columna Col.
        int May = HEADER[Col].length();
        for (int i=0; i< L.size(); i++){
            int Lon = getElem(i, Col).length();
            if (Lon > May)
                May = Lon;
        }
        return May+2;
    }
    
    private String getElem(int Fila, int Col){       
        return L.get(Fila).fieldToString(Col);
    }
//*******
    
    
    private int addTupla(String NombreID, int Valor, int CantTmp){ //Inserta una nueva tupla, validando NombreID.
        NombreID = toNameID(NombreID);
        
         if (NombreID.length() == 0)    //Si el NombreID es vacio...
            return -1;                  //...devolver -1, indicando que la tupla no se insertó.
         
        int Pos = indexOf(NombreID);
        
        if (Pos != -1){  //El ID ya existe en la TSID.
            L.get(Pos).set(NombreID, Valor, CantTmp);
            return Pos;             
        }    
        
        L.add( new Tupla(NombreID, Valor, CantTmp) );
       
        return L.size()-1;   //Devolver la posición donde fue insertada la nueva tupla.
    }
    
  
    
    private static final String MAINid="$MAIN";
    
    private String toNameID(String nombreID){ //Realiza un semi validación del NombreID. (Si el usuario de esta class fuese el Compilador, esta función es innecesaria).
        if (nombreID == null)
            return "";
        
        nombreID = nombreID.trim();    //Eliminar espacios iniciales y finales
        if (nombreID.charAt(0)=='$')
            nombreID = MAINid;      
        
        return nombreID.replace(' ', '_');
    }
    
}
