package codigo_3;

public class Main {
    
   private static void programar(Codigo3 c3){
        TSID tsid = c3.getTSID();
        TSS tss   = c3.getTSS();
        
            //Add las StringCttes del programa a la TSS
            tss.add("N= ");  //0
             tss.add("*");   //1
       
            //Instalar las vars y procs del programa a la TSID
        tsid.addProc("Lectura", 0, 2);       //0
        tsid.addVar("N", TSID.TIPOINT);      //1
        tsid.addProc("Linea", 7, 1);         //2
        tsid.addVar("i", TSID.TIPOINT);      //3
        tsid.addVar("k", TSID.TIPOINT);      //4
            tsid.addProc("$main",17,2);        //5
       
           // Proc Lectura
        c3.add(OPCODE.ETIQUETA, 1);     //E1;
        c3.add(OPCODE.WRITES, 0);       //WRITES("N=")
        c3.add(OPCODE.READ, -1);        //READ(N)
       // c3.add(OPCODE.ASIGNNUM, -1, -1);
        c3.add(OPCODE.ASIGNNUM, 1, 0);  //t1 = 0
        c3.add(OPCODE.MAY, 2, -1, 1);   //t2 = (N > t1)
        c3.add(OPCODE.IF0, 2, 5);       //IF (t2=0) =>GOTO E1
        c3.add(OPCODE.RET);             //RET
        
            //Proc Linea
        c3.add(OPCODE.ASIGNNUM, -3, 1);     //i = 1
        c3.add(OPCODE.ETIQUETA, 2);         //E2:
        c3.add(OPCODE.MEI, 1, -3, -1);      //t1 = (i <= N)
        c3.add(OPCODE.IF0, 1, 3);           //IF (t1=0) =>GOTO E3
        c3.add(OPCODE.WRITES, -1);          //WRITES("*")
        c3.add(OPCODE.INC, -3);             //INC i
        c3.add(OPCODE.GOTO, 2);             //GOTO E2
        c3.add(OPCODE.ETIQUETA, 3);         //E3:   [[nombre,dir1,dir2,dir2],[],[]...
        c3.add(OPCODE.NL);                  //NL
        c3.add(OPCODE.RET);                 //RET
        
            //Proc $Main
        c3.add(OPCODE.CALL, 0);             //CALL Lectura    
        c3.add(OPCODE.ASIGNNUM, -4, 1);     //k = 1
        c3.add(OPCODE.ETIQUETA, 4);         //E4:
        c3.add(OPCODE.MEI, 1, -4, -1);      //t1 = (k <= N)
        c3.add(OPCODE.IF0, 1, 5);           //IF (t1=0) =>GOTO E5
        c3.add(OPCODE.CALL, -2);            //CALL Linea
        c3.add(OPCODE.INC, -4);             //INC k
        c3.add(OPCODE.GOTO, 4);             //GOTO E4
        c3.add(OPCODE.ETIQUETA, 5);         //E5:
        c3.add(OPCODE.RET);                 //RET
//           c3.add(OPCODE.ASIGNNUM,1,8);
//          // c3.add(OPCODE.GOTO,1);
//           c3.add(OPCODE.ASIGNNUM,2,5);     //t1=5
//           //c3.add(OPCODE.ASIGNNUM,-3,5);     //t2=8
//           //c3.add(OPCODE.READ, -3);
//           c3.add(OPCODE.MEI,3,1,2);        //t3= t1<=t2
//           c3.add(OPCODE.IF0,3,1);          // if (t3=0) --> goto E1:
//           c3.add(OPCODE.SUMA,3,1,2);       //t3=t1+t2
//           //c3.add(OPCODE.MINUS,3);
////           
////           //c3.add(OPCODE.ETIQUETA, 2);
////           c3.add(OPCODE.WRITE,1);           //write(t3);
//           c3.add(OPCODE.GOTO,2);             //ir a la etiqueta E2:
//           c3.add(OPCODE.ETIQUETA, 1);
//           c3.add(OPCODE.WRITES,0);
//           c3.add(OPCODE.ETIQUETA, 2);
//           c3.add(OPCODE.RET);
//          
          
//          c3.add(OPCODE.ASIGNNUM,1,5);      //t1=5
//          c3.add(OPCODE.ASIGNNUM,2,0);      //t2=0
//          c3.add(OPCODE.ETIQUETA,1);        //E1:
//         
//          
//          c3.add(OPCODE.INC,2);   
//          c3.add(OPCODE.WRITE,2);
//          c3.add(OPCODE.MEI,3,2,1);         // t3 = t2<=t1
//          c3.add(OPCODE.IF1,3,1); 
//          
            
    }
    
    
    
    public static void main(String[] args) {
        Codigo3 c3 = new Codigo3();
        
        programar(c3);
        c3.save("cuadrado.c3");
        //c3.open("cuadrado.c3");
        c3.println();
            
    }
    
}
