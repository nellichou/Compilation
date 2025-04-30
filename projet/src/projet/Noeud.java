package projet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * La classe Noeud est utilisée pour modéliser une cible.
 * 
 * @author Benjamin Bribant, Nell Telechea
 */
public class Noeud {
    /**
     * Nom de la cible.
     */
    private String cible;

    /**
     * Liste de ses dépendances.
     */
    private List<Noeud> dependances;

    /**
     * Liste de ses recettes.
     */
    private List<String> recettes;

    /**
     * Le constructeur de la classe.
     * @param cible
     */
    public Noeud (String cible) {
        this.cible = cible;
        this.dependances = new ArrayList<>();
        this.recettes = new ArrayList<>();
    }

    /**
     * La méthode getNom renvoie le nom de la cible.
     * @return Le nom de la cible.
     */
    public String getNom () {
        return this.cible;
    }

    /**
     * La méthode ajoutDependance permet d'ajouter les dépendances d'une cible.
     * @param dependance La dépendance à ajouter.
     */
    public void ajoutDependance(Noeud dependance) {
        this.dependances.add(dependance);
    }

    /**
     * La méthode getDependances renvoie les dépendances de la cible.
     * @return La liste des dépendances de la cible.
     */
    public List<Noeud> getDependances() {
        return this.dependances;
    }

    /**
     * La méthode ajoutRecette permet d'ajouter une recette de la cible.
     * @param recette La recette à ajouter.
     */
    public void ajoutRecette (String recette) {
        this.recettes.add(recette);
    }

    /**
     * La méthode getRecettes renvoie les recettes de la cible.
     * @return La liste des recettes de la cible.
     */
    public List<String> getRecettes () {
        return this.recettes;
    }

    /**
     * La méthode aJour permet de savoir si une cible est à jour.
     * @param debogage Paramètre pour afficher le débogage ou non.
     * @return true : si la cible est à jour, false : si elle ne l'est pas.
     */
    public boolean aJour(boolean debogage) {
        File cibleFifi = new File("./projet/build/projet/" + this.cible);

        if (!cibleFifi.exists()) {
            return false;
        }
    
        long dateCible = cibleFifi.lastModified();
        File depJavaFichier = new File("./projet/build/projet/" + this.getNom().replace(".class", ".java"));
    
        if (!depJavaFichier.exists()) {
            System.out.println("Le fichier source .java de la dépendance est introuvable : " + depJavaFichier.getAbsolutePath());
            return false; 
        }

        long dateJava = depJavaFichier.lastModified();

        if(debogage){
            LocalDateTime vraieDateCible = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateCible), ZoneId.systemDefault());
            LocalDateTime vraieDateJava = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateJava), ZoneId.systemDefault());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String dateCibleLisible = vraieDateCible.format(formatter);
            String dateJavaLisible = vraieDateJava.format(formatter);

            System.out.println("Le fichier " + cibleFifi + " a été modifié pour la dernière fois le : " + dateCibleLisible);
            System.out.println("Le fichier " + depJavaFichier + " a été modifié pour la dernière fois le : " + dateJavaLisible);
        }

        if ((dateJava > dateCible) && debogage) {
            System.out.println("Le fichier source .java (" + depJavaFichier.getPath() + ") est plus récent que la cible.");
            return false;
        }

        return true;
    }
}