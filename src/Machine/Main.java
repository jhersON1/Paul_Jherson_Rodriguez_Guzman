package Machine;

import codigo_3.Utils;


public class Main {
    
    
    public static void main(String[] args) {
        //return;
        VirtualMachine VM = new VirtualMachine();
        
        int r = VM.open("factorial.c3");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
        if (r == VirtualMachine.ERRORarchivo){
            System.err.println("No se pudo cargar el archivo ");
            return;
        }
        
        VM.c3.println();      //Mostrar el c3 que tiene cargado la VM
        VM.run();
    }
    
}
