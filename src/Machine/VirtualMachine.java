package Machine;

import codigo_3.*;
import javax.swing.JOptionPane;

public class VirtualMachine {
        //Códigos de Error
    public static final int EXITO = 0;
    public static final int ERRORarchivo = 1;
    public static final int C3noCARGADO  = 2;
    public static final int NoMAIN = 3;
    int c = 0;
    public String sInterfaz ="";
    private String t = "";
        //"Memoria" de la VM
    public Codigo3 c3;  
    private TSID tsId;
    private TSS tss;
    
        //Registros de la "CPU" de la VM.
    public Pila pila;
    public int IP;
        // para saber donde empezar a mostrar el codigoc3 en la interfaz
    public int posicionIP=IP;
    public int contador=0;
  
    
    public VirtualMachine(){
        pila = new Pila();
    }
    
    public int open(String filename){
        Codigo3 aux = new Codigo3();
        
        boolean b = aux.open(filename);     //Cargar el archivo c3 y sus tablas.
        if (!b)
            return ERRORarchivo;    //No se pudo cargar el archivo.
        
        c3 = aux;
        tsId = c3.getTSID();
        tss = c3.getTSS();
        
        return EXITO;
    }
    
    public int run(){
        if (c3 == null)
            return C3noCARGADO;
        int i=tsId.getIndexOfMain();    //Índice de la TsId donde se encuentra el $Main.
        if (i==-1)
            return NoMAIN;      //No hay $Main en la tabla TsId
        pila.init();        //Vacíar la pila.
        IP = call(i, -1);   //Comenzar a ejecutar el $Main (la dir de retorno del $Main es -1). 
        
        while (0 <= IP && IP <= c3.lenght()-1){
            Cuadrupla C = c3.getCuadrupla(IP);
            traducir(C);
            if (IP==c3.lenght()-1){
                //System.exit(EXITO);
                IP=c3.lenght();
            }
        }
         //System.out.println(IP);   
        return EXITO;
    }
    
    private boolean iniciado = false;
    public int runPasoPaso(){
        if(iniciado == false){
            if (c3 == null)
                return C3noCARGADO;
            int i=tsId.getIndexOfMain();    //Índice de la TsId donde se encuentra el $Main.
            if (i==-1)
                return NoMAIN;      //No hay $Main en la tabla TsId
            pila.init();        //Vacíar la pila.
            IP = call(i, -1);   //Comenzar a ejecutar el $Main (la dir de retorno del $Main es -1). 
            iniciado = true;
        }
        if (IP==c3.lenght()-1){
            //System.exit(EXITO);
            IP=c3.lenght();
        }
        if(0 <= IP && IP <= c3.lenght()-1){
            Cuadrupla C = c3.getCuadrupla(IP);
            traducir(C);
        }
         //System.out.println(IP);   
        return EXITO;
    }
    
    
    private int call(int indexTsID, int dirReturn){
        int inicioC3 = tsId.getValor(indexTsID);    //Indice de la 1era. Cuadrupla del proc llamado.
        int cantTmp  = tsId.getCantTmp(indexTsID);
       // System.out.println(cantTmp);
        pila.push(0, cantTmp);  //Temporales a la pila, inicializados en 0.
        pila.push(cantTmp);  
        //pila.push(cantTmp); //Cantidad de temporales, a la pila
        pila.push(dirReturn);   //Dir de retorno a la pila.
        
        return inicioC3;
    }
    
    private int ret(){
        if (contador>0){
        for (int i=1;i<=contador-1;i++){
            pila.pop();
            contador--;
        }
        }
        int dirReturn = pila.pop();
        int cantTmp   = pila.pop();
        pila.pop(cantTmp);          //Quitar los temporales de la pila.
        
        return dirReturn;
    }
    
    private void traducir(Cuadrupla C){ 
        int indexTabla, dir, valor,valor1,valor2;
        posicionIP=IP;
        //System.out.println(posicionIP);
        
        switch (C.getOpCode()){
            case OPCODE.CALL    :       //e.g C=|CALL|-1|_|_|
                                    indexTabla = - C.getDir(1);   //e.g. IndexTabla = 1
                                    IP = call(indexTabla, IP+1);
                                   // System.out.println("posicione de ip:" +IP);
                                    return;
                                    
            case OPCODE.RET     :       //e.g C=|RET|-|-|-|
                                    IP = ret();
                                    //System.out.println("posicione de ip:" +IP);
                                    return;
                                    
            case OPCODE.INC     :       //e.g C=|INC|-2|_|_|
                                    dir = C.getDir(1);      //dir = -2
                                    valor = getValor(dir);
//                                    System.out.println("posicion ip:" +IP);
//                                    System.out.println("valor de inc :" +valor);
                                    setValor(dir, valor+1);
                                    break;
                
            case OPCODE.WRITES  :       //e.g C=|WRITES|-3|-|-|
                                    indexTabla = - C.getDir(1);         //e.g. IndexTabla = 3
                                    String s = tss.getStr(indexTabla);  //e.g s = tss[3]
                                    System.out.print(s);
                                    sInterfaz+=s;
                                    t=s;
                                    break;
                                    
            case OPCODE.NL  :           //e.g C=|NL|-|-|-|
                                    System.out.println();   //Salto de línea
                                    break;                  
           
            case OPCODE.GOTO :        //Traduccion del |GOTO| 2 | - | - |    //Goto E2
                                    //Buscar en el C3 la etiqueta E2 (e.g se encuentra en la posiciób 5)
                                    //IP = 5;
                                    dir = C.getDir(1);
                                    valor = getValor(dir);
                                    
                                    for (int i=0; i<=c3.n; i++){
                                        if (c3.V[i].getOpCode()== OPCODE.ETIQUETA && c3.V[i].getDir(1)==dir){
                                              IP=i;
//                                              System.out.println("posicon ip en GOTO: "+IP);
//                                              System.out.println(c3.V[i].getDir(1));
//                                              System.out.println(dir);
                                        }
                                        //posicionIP=IP;
                                    }
                                    return;
                                    
            case OPCODE.WRITE :     
                                    
                                    dir = C.getDir(1);
                                    valor = getValor(C.getDir(1));
                                    setValor(dir, valor);
                                    System.out.println(valor);
                                    sInterfaz+=valor+" ";
                                    break;
                                    
            case OPCODE.READ :
                                    String name = JOptionPane.showInputDialog(t+" ");
                                    valor = Integer.parseInt(name);
                                    dir = C.getDir(1);
                                    setValor(dir, valor);
                                    sInterfaz+=valor+"\n"+"\n";
                                    break;
            case OPCODE.SUMA :      //|suma||t1|=|t2|+|t3|
                                    dir = C.getDir(1);
                                  
                                    valor = getValor(C.getDir(2))+getValor(C.getDir(3));
                                    setValor(dir, valor);
                                    
                                    break;
                      
            case OPCODE.ASIGNNUM :  //|ASIGNNUM||2||4|||
                                    
                                    
                                    dir = C.getDir(1);
                                    //pila.push(dir);
//                                    System.out.println("direccion "+dir);
//                                    System.out.println("valor "+C.getDir(2));
                                    if (dir<=0){
                                        setValorVar(dir, C.getDir(2));
                                        pila.push(C.getDir(2));
                                        contador++;
                                        
                                    }else{
                                        setValor(dir,C.getDir(2) );
                                    }
                                    // System.out.println(contador);
                                   //pila.push(getValor(dir));
                                    
                                    
                                    break; 
            case OPCODE.DEC :
                                    dir = C.getDir(1);      //dir = -2
                                    valor = getValor(dir);
                                    setValor(dir, valor-1);
                                    break;
                                    
            case OPCODE.MENOS :     
                                    dir = C.getDir(1);
                                    valor = getValor(C.getDir(2))-getValor(C.getDir(3));
                                    setValor(dir, valor);
                                    
                                    break;
            case OPCODE.POR :     
                                    dir = C.getDir(1);
                                    valor = getValor(C.getDir(2))*getValor(C.getDir(3));
                                    setValor(dir, valor);
                                    
                                    break;      
            case OPCODE.DIV :     
                                    dir = C.getDir(1);
                                    valor = getValor(C.getDir(2))/getValor(C.getDir(3));
                                    setValor(dir, valor);
                                    
                                    break;
            case OPCODE.MOD :     
                                    dir = C.getDir(1);
                                    valor = getValor(C.getDir(2))%getValor(C.getDir(3));
                                    setValor(dir, valor);
                                    
                                    break;
            case OPCODE.MINUS :     
                                    dir = C.getDir(1);
                                    valor = -getValor(C.getDir(1));
                                    setValor(dir, valor);
                                    
                                    break;  
                                 
            // sin revisar  
            case OPCODE.NOT :     
                                    dir = C.getDir(1);
                                    valor = getValor(C.getDir(1));
                                    if (valor ==1){
                                        valor =0;
                                    }else {
                                        valor =1;
                                    }
                                    setValor(dir, valor);
                                    
                                    break;   
            case OPCODE.AND :     
                                    dir = C.getDir(1);
                                    valor = 0;
                                    valor1=C.getDir(2);
                                    valor2=C.getDir(3);
                                    if (valor1==1 & valor2==1){
                                        valor =1;
                                    }
                                    setValor(dir, valor);                                    
                                    break; 
                          
            case OPCODE.OR :
                                    dir = C.getDir(1);
                                    valor = 1;
                                    int valor3=C.getDir(2);
                                    int valor4=C.getDir(3);
                                    if (valor3==0 | valor4==0){
                                        valor =0;
                                    }
                                    setValor(dir, valor);                                    
                                    break;
                                    
            case OPCODE.ETIQUETA :
                                    //dir = C.getDir(1);
                                    //IP=IP+1;
                                    //System.out.println(IP);

                                    break;
                                    
            case OPCODE.MEN :
                                    dir = C.getDir(1);
                                    valor=0;
                                    valor1= getValor(C.getDir(2));
                                    valor2= getValor(C.getDir(3));
                                    if (valor1<valor2){
                                        valor=1;
                                    }
//                                    System.out.println(valor1);
//                                    System.out.println(valor2);
                                    setValor(dir, valor);
                                    break;  
                                    
            case OPCODE.MAY :
                                    dir = C.getDir(1);
                                    valor=0;
                                    valor1= getValor(C.getDir(2));
                                    valor2= getValor(C.getDir(3));
                                    if (valor1>valor2){
                                        valor=1;
                                    }
//                                    System.out.println(valor1);
//                                    System.out.println(valor2);
                                    setValor(dir, valor);
                                    break;
                                    
            case OPCODE.IGUAL :
                                    dir = C.getDir(1);
                                    valor=0;
                                    valor1= getValor(C.getDir(2));
                                    valor2= getValor(C.getDir(3));
                                    if (valor1==valor2){
                                        valor=1;
                                    }
                                    setValor(dir, valor);
                                    break;  
                                    
            case OPCODE.DIS :
                                    dir = C.getDir(1);
                                    valor=0;
                                    valor1= getValor(C.getDir(2));
                                    valor2= getValor(C.getDir(3));
                                    if (valor1!=valor2){
                                        valor=1;
                                    }
                                    setValor(dir, valor);
                                    break;                                     
                                    
            case OPCODE.MEI :
                                    dir = C.getDir(1);
                                    valor=0;
                                    valor1= getValor(C.getDir(2));
                                    valor2= getValor(C.getDir(3));
                                    if (valor1 <= valor2){
                                        valor=1;
                                    }
//                                    System.out.println(valor1);
//                                    System.out.println(valor2);
                                    setValor(dir, valor);
                                   // System.out.println(valor);
                                    break; 

            case OPCODE.MAI :
                                    dir = C.getDir(1);
                                    valor=0;
                                    valor1= getValor(C.getDir(2));
                                    valor2= getValor(C.getDir(3));
                                    if (valor1>=valor2){
                                        valor=1;
                                    }
//                                     System.out.println(valor1);
//                                    System.out.println(valor2);                                   
                                    setValor(dir, valor);
                                    break;
                                    
            case OPCODE.IF0 :   //  |IF0||temporal||etiqueta| ---> IF (t2=0) =>GOTO E1
                                    dir = C.getDir(1);
                                    valor = getValor(dir);
                                    valor1 = C.getDir(2);
                                    if (valor ==0){
                                        for (int i=0; i<c3.n; i++){
                                        if (c3.V[i].getOpCode()== OPCODE.ETIQUETA && c3.V[i].getDir(1)==valor1)
                                              IP=i-1;
                                    }
                                    }
//                                    System.out.println("valor del temporal "+valor);
//                                    System.out.println("valor del etiqueta "+valor1);
                                    break;
                                    
            case OPCODE.IF1 :
                                    dir = C.getDir(1);
                                    valor = getValor(dir);
                                    valor1 = C.getDir(2);
                                    if (valor ==1){
                                        for (int i=0; i<c3.n; i++){
                                        if (c3.V[i].getOpCode()== OPCODE.ETIQUETA && c3.V[i].getDir(1)==valor1)
                                              IP=i-1;
                                    }
                                    }
//                                    System.out.println("valor del temporal "+valor);
//                                    System.out.println("valor del etiqueta "+valor1);
                                    break;                                    
            
                                    
                                    
                                    

        } //End switch

        IP++;

    }
    
    
    
    private void setValor(int dir, int valor){     //Para temporales y variables
        if (dir <= 0)
            setValorVar(dir, valor);
        else
            setValorTemp(dir, valor);
    }
    
    private int getValor(int dir){     //Para temporales y variables
        if (dir <= 0)
            return getValorVar(dir);
       
        return getValorTemp(dir);
    }
    
    
    
    private void setValorTemp(int i, int valor){  //ti = valor  (i es positivo)
        int abajo = i + 1;
        pila.setValor(abajo, valor);
    }
    
    private int getValorTemp(int i){  //return ti  (i es positivo)
        int abajo = i + 1;
       // System.out.println("Machine.VirtualMachine.getValorTemp()");
        return pila.getValor(abajo);
        
    }
    
    private void setValorVar(int dir, int valor){  //dir es 0 ó negativo.
        tsId.setValor(-dir, valor);
    }
    
    private int getValorVar(int dir){  //dir es 0 ó negativo.
        return tsId.getValor(-dir);
    }
}
