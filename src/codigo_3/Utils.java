package codigo_3;

public class Utils { //Esta class, contiene procedimientos de uso común.
    
    public static String FieldLeft(String S, int Ancho){ //Devuelve S con Ancho caracteres (padding derecho con espacios).  (Padding singnifica "relleno").        
        return S + espacios(Ancho-S.length());
    }

    
    public static String FieldRight(String S, int Ancho){ //Devuelve S con Ancho caracteres (padding izquierdo con espacios).
        return espacios(Ancho-S.length()) + S;
    }

    public static String FieldCenter(String S, int Ancho){
        if (S.length() > Ancho)
            return S.substring(0, Ancho-1);

        int Padding = (Ancho - S.length())/2;
        
        S = espacios(Padding) + S;
        return S + espacios(Ancho-S.length());
    }
    
    public static String espacios(int n){
        final char BLANK = 32;
        return RL(BLANK, n);
    }
    
    
    /**
     * 
     * @param c char a repetir
     * @param n cantidad de veces que se repite c
     * @return un Run-Length (seguidilla) de n caracteres c.
     */
    public static String RL(char c, int n){
        String S = "";
        for (int i=1; i<=n; i++)
            S += c;
        
        return S;        
    }
    
}
