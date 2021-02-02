package Machine;

import codigo_3.Utils;
import java.util.ArrayList;

public class Pila {
    private ArrayList <Integer> L; 
    
    public Pila(){
        L = new ArrayList<>();
    }
    
    /**Quita todos los elementos de la Pila (la deja vacía) */
    public void init(){
        L.clear();
    }
    
    public int length(){
        return L.size();
    }
    
    /**Inserta <code> valor </code> a la pila.
     * @param valor */
    public void push(int valor){
        L.add(valor);   //El tope de la pila es el último elemento de L.
    }
    
    
       /**Inserta <code> cant </code> veces <code> valor </code> a la pila.
        * @param valor número a insertar
        * @param cant cantidad de veces que se inserta el valor*/
    public void push(int valor, int cant){
        for (int i=1; i<=cant; i++){
            L.add(valor);   //push(valor)
        }
    }
    
    
    public int pop(){
        if (L.isEmpty())
            throw new RuntimeException("Pila.pop: No se puede sacar de una pila vacía.");
        
        int ultimoIndex = L.size()-1;
        return L.remove(ultimoIndex);
    }
    
    /**Quita los primeros <code> cant </code> elementos de la pila.
     * @param cant cantidad de elementos a quitar de la pila. */
    public void pop(int cant){
        cant = Math.min(L.size(), cant);    //No sacar más de L.size() elementos.
        
        for (int i=1; i<=cant; i++){
            L.remove(L.size()-1);       //pop()
        }
    }
    
    
    /** Por ejemplo:<br/>  
     * <blockquote> getValor(0) = elemento que está en el tope de la pila. </blockquote>
     * <blockquote> getValor(1) = elemento que está debajo del tope de la pila. </blockquote><br/>
     * <b>Nota:</b> Este método no remueve ningún elemento de la pila.
     * @param offset desplazamiento desde el tope de la pila.
     * @return el elemento (número entero) que está offset posiciones, abajo del tope de la Pila*/
    public int getValor(int offset){
        int index = getIndex(offset);
        
        try {
           return L.get(index);
        } catch (Exception e) {
            String msj = "Pila.getValor: offset="+offset+" inválido "+
                         "(la pila solo tiene "+length()+" elementos).";
            throw new RuntimeException(msj);
        }           
    }
    
    
    public void setValor(int offset, int valor){
        int index = getIndex(offset);
        
        try {
           L.set(index, valor);
        } catch (Exception e) {
            String msj = "Pila.setValor: offset="+offset+" inválido "+
                         "(la pila solo tiene "+length()+" elementos).";
            throw new RuntimeException(msj);
        }
    }
    
    private int getIndex(int offset){
            //Calcula el índice de la lista, desde la última posición (tope de la Pila),
            //menos offset posiciones.
        offset = Math.abs(offset);
        int ultimoIndex = L.size()-1;
        return ultimoIndex-offset;
    }
    
    
//-----------------------------------------------------------------------------    
    @Override
    public String toString(){  
        if (length()==0)
            return "(Pila vacía)";
        
        int tamCampo = maxLong();
        String s = "_" + Utils.espacios(tamCampo+3) + "_" + LF; 
  
        for (int i=0; i<L.size(); i++){
            s += " |" + Utils.FieldRight(""+L.get(i), tamCampo)+" |" + LF;
        }
        s = s + " '" + Utils.RL('-', tamCampo+1) + "'" + LF; 
        return s;
    }
    
    private int maxLong(){  //Devuelve el tamaño (en chars) del número + grande de la pila.
        int may = 0;
        for (int i=0; i<L.size(); i++){
            String s = ""+L.get(i);
            if (may < s.length())
                may = s.length();
        }
        
        return may+1;
    }
    
    private static final char LF = '\n';    //Salto de línea.
}
