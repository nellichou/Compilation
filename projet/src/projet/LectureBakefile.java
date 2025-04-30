package projet;

import java.io.*;
import java.util.*;

/**
 * La classe LectureBakefile est utilisée pour lire le Bakefile.
 * 
 * @author Benjamin Bribant, Nell Telechea
 */
public class LectureBakefile {
    /**
     * L'arbre de dépendances.
     */
    private Arbre arbre;

    /**
     * La ligne lue.
     */
    private String ligne;

    /**
     * Les éléments sur la ligne lue.
     */
    private String[] elements;

    /**
     * Des cibles.
     */
    private String cible, premiereCible;

    /**
     * Le dictionnaire de variables.
     */
    private Map<String, String> substitution = new HashMap<>();
    private int i = 0;

    /**
     * Le constructeur de la classe.
     * */
    public LectureBakefile(){
        this.arbre = new Arbre();
    }

    /**
     * La méthode lecture lit le Bakefile et met ses éléments dans l'arbre.
     * @return La première cible de l'arbre.
     */
    public String lecture() {

        try{
            FileReader file = new FileReader("./projet/build/projet/Bakefile");
            BufferedReader lecteur = new BufferedReader(file);

                try {
                    while((ligne = lecteur.readLine()) != null){

                        if(!(ligne.isEmpty() || ligne.startsWith("#"))){

                            if(ligne.contains("=")){
                                ligne = ligne.trim();
                                elements = ligne.split("\\s*=\\s*");

                                if(elements.length == 2){
                                    String variable = elements[0];
                                    String valeur = elements[1];
                                    substitution.put(variable, valeur);
                                }else{
                                    System.err.println("Format de variable incorrect : " + ligne);
                                }

                            } else if(ligne.contains(":")){
                                ligne = ligne.trim();
                                List<String> deps = new ArrayList<>();
                                ligne = remplacerVariables(ligne);
                                elements = ligne.split("\\s+");
                                cible = elements[0];
                                if(i == 0){
                                    premiereCible = cible;
                                    i++;
                                }

                                for(int i = 2; i < elements.length ; i++){
                                    deps.add(elements[i]);
                                }

                                Noeud noeudCible = arbre.ajouterNoeud(cible);
                                for (String dep : deps) {
                                    if (!dep.endsWith(".java")) {
                                        arbre.ajoutDependance(cible, dep);
                                    }
                                }
                                

                            } else if(ligne.startsWith("\t")){
                                if(cible != null) {
                                    String l = ligne.trim();
                                    l = remplacerVariables(l);
                                    elements = l.split("\\s+");
                                    Noeud noeudCible = arbre.getNoeud(cible);
                                    for (String recette : elements) {
                                        arbre.ajoutRecette(cible, recette);
                                    }
                                    
                                }else{
                                    System.err.println("Cette recette n'a pas de cible : " + ligne);
                                }
                            }
                        }
                    }
                    arbre.contientCycle();

                } catch(IOException e){
                    System.err.println("Erreur de lecture dans le Bakefile.");
                }
                try {
                    lecteur.close();
                } catch(IOException e){
                    System.err.println("Erreur de fermeture du Bakefile !");
                }
        
            return premiereCible;
        } catch (IOException ex){
            System.err.println("Erreur dans l'ouverture du Bakefile.");
            return null;
        }
    }

    /**
     * La méthode remplacerVariables substitue les variables lues au début du Bakefile dans la ligne à changer.
     * @param ligne La ligne à changer.
     * @return La ligne changée.
     */
    private String remplacerVariables(String ligne) {
        for (Map.Entry<String, String> entry : substitution.entrySet()) {
            ligne = ligne.replace("$(" + entry.getKey() + ")", entry.getValue());
        }
        return ligne;
    }

    /**
     * La méthode getArbre renvoie l'arbre de dépendance.
     * @return L'arbre.
     */
    public Arbre getArbre() {
        return this.arbre;
    }
}
