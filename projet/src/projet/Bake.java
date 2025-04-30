package projet;

/**
 * La classe Bake est utilisée pour lancer le programme.
 * 
 * @author Benjamin Bribant, Nell Telechea
 */
public class Bake {
    /**
     * La méthode main récupère les arguments donnés à Bake et en fonction lance le programme différemment.
     * @param args arguments de Bake s'il y a "-d" on rajoute des informations de débogage, s'il y a une cible on exécute les recettes de cette cible.
     */
    public static void main(String[] args) {
        LectureBakefile lecture = new LectureBakefile();
        String premiereCible = lecture.lecture();
        Arbre arbre = lecture.getArbre();

        
        try {
            if(args.length == 0){
                arbre.executerRecettes(premiereCible, false);
            }else{
                if(args.length == 1 && args[0].equals("-d")){
                    arbre.executerRecettes(premiereCible, true);
    
                }else if(args.length > 1 && args[0].equals("-d")){
                    String[] argsSansd = new String[args.length - 1];
                    for(int i = 1; i < args.length; i++){
                        argsSansd[i - 1] = args[i];
                    }
                    for(String c : argsSansd){
                        arbre.executerRecettes(c, true);
                    }
    
                }else if(args.length > 1 && !args[0].equals("-d")){
                    String[] argsSansd = new String[args.length - 1];
                    for(int i = 1; i < args.length; i++){
                        argsSansd[i - 1] = args[i];
                    }
                    for(String c : argsSansd){
                        arbre.executerRecettes(c, false);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Problème dans l'execution des recettes, arrêt du programme.");
        }
        

    }
}