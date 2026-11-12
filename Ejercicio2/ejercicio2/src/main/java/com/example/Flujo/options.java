import utilsLocal;

public class Options {

    public static String decidirConexion(){

        Scanner sc = new Scanner(System.in);
        
        System.out.println("Decide la conexion: ");
        System.out.println("1. APACHE DERBY");
        System.out.println("2. H2");
        System.out.println("3. HSQLDB");
        int opcion = sc.nextInt();
        switch(opcion){
            case 1 -> {
                return utils.URL_APACHE;
            }
            case 2 ->{

            }
            case 3 ->{

             
            }
            default ->{
                System.out.pritnln("opcion no permitida");
            }
        }
        return "";
    }

}
