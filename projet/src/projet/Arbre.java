package projet;

import java.util.*;

/**
 * La classe Arbre est utilisée pour modéliser le graphe de dépendances et représente un arbre de données.
 * 
 * @author Benjamin Bribant, Nell Telechea
 */
public class Arbre {

    /**
     * Le dictionnaire de noeuds de l'arbre.
     */
    private Map<String, Noeud> noeuds;

    /**
     * La liste des cibles à ignorer lors de l'exécution des recettes.
     */
    private List<Noeud> ciblesIgnorees;


    /**
     * Le constructeur de la classe.
     */
    public Arbre() {
        this.noeuds = new HashMap<>();
        this.ciblesIgnorees = new ArrayList<>();
    }

    /**
     * La méthode ajouterNoeud permet d'ajouter un noeud à l'arbre.
     * @param cible La cible à ajouter.
     * @return Le noeud ajouté.
     */
    public Noeud ajouterNoeud(String cible) {
        if (!noeuds.containsKey(cible)) {
            noeuds.put(cible, new Noeud(cible));
        }
        return noeuds.get(cible);
    }

    /**
     * La méthode contientNoeud permet de savoir si l'arbre contient le noeud.
     * @param cible Le noeud.
     * @return Un boolean : true si l'arbre contient le noeud, false : s'il ne le contient pas.
     */
    public boolean contientNoeud(String cible) {
        return noeuds.containsKey(cible);
    }


    /**
     * La méthode getNoeud permet de renvoyer un noeud de l'arbre.
     * @param cible Le noeud.
     * @return Le noeud.
     */
    public Noeud getNoeud(String cible) {
        return noeuds.get(cible);
    }

    /**
     * La méthode ajoutDependance permet d'ajouter les dépendances d'un noeud.
     * @param cible Le noeud.
     * @param dependance La dépendance à ajouter.
     */
    public void ajoutDependance(String cible, String dependance) {
        Noeud noeudCible = ajouterNoeud(cible);
        Noeud noeudDependance = ajouterNoeud(dependance);
        noeudCible.ajoutDependance(noeudDependance);
    }

    /**
     * La méthode ajoutRecette permet d'ajouter une recette du noeud.
     * @param cible Le noeud.
     * @param recette La recette à ajouter.
     */
    public void ajoutRecette(String cible, String recette) {
        Noeud noeudCible = ajouterNoeud(cible);
        noeudCible.ajoutRecette(recette);
    }

    /**
     * La méthode contientCycle renvoie s'il y a des dépendances circulaires ou non.
     * @return true : s'il y a un ou des cycles, false : s'il n'y en a pas.
     */
    public boolean contientCycle() {
        List<Noeud> visite = new LinkedList<>();
        List<Noeud> enCours = new LinkedList<>();

        for (Noeud noeud : noeuds.values()) {
            detecterCycle(noeud, visite, enCours);
        }

        if (!ciblesIgnorees.isEmpty()) {
            System.out.println("Cibles ignorées en raison de cycles :");
            for (Noeud ignorer : ciblesIgnorees) {
                System.out.println("- " + ignorer.getNom());
            }
        }

        return !ciblesIgnorees.isEmpty();
    }

    /**
     * La méthode detecterCycle permet de détecter s'il y a des dépendances circulaires sur une cible et ses dépendances.
     * @param noeud La cible.
     * @param visite La liste de visite des noeuds.
     * @param enCours La liste de visite du noeud en cours.
     * @return true: s'il y a un cycle, false : s'il n'y en a pas.
     */
    private boolean detecterCycle(Noeud noeud, List<Noeud> visite, List<Noeud> enCours) {
        if (enCours.contains(noeud)) {
            ciblesIgnorees.add(noeud);
            return true;
        }
        if (visite.contains(noeud)) {
            return false;
        }

        visite.add(noeud);
        enCours.add(noeud);

        for (Noeud dependance : noeud.getDependances()) {
            if (detecterCycle(dependance, visite, enCours)) {
                return true;
            }
        }

        enCours.remove(noeud);
        return false;
    }

    /**
     * La méthode executerRecette permet d'exécuter les recettes d'une cible.
     * @param cible La cible.
     * @param debogage Paramètre pour afficher le débogage ou non.
     * @throws Exception s'il y a eu un problème dans l'exécution des recettes.
     */
    public void executerRecettes(String cible, boolean debogage) throws Exception {
        if (ciblesIgnorees.contains(cible)) {
            if (debogage) {
                System.out.println("La cible " + cible + " est ignorée en raison d'un cycle.");
            }
            return;
        }

        Noeud noeud = getNoeud(cible);
        if (noeud == null) {
            throw new Exception("La cible '" + cible + "' n'existe pas dans l'arbre.");
        }

        List<Noeud> visite = new LinkedList<>();
        executerRecettesRecursif(noeud, visite, debogage);
    }

    /**
     * La méthode executerRecettesRecursif permet d'exécuter les recettes d'une cible récursivement (pour faire celles de ses dépendances).
     * @param noeud La cible.
     * @param visite La liste qui permet de stocker les cibles exécutées.
     * @param debogage Paramètre pour afficher le débogage ou non.
     * @throws Exception s'il y a eu un problème dans l'exécution des recettes.
     */
    private void executerRecettesRecursif(Noeud noeud, List<Noeud> visite, boolean debogage) throws Exception {
        if (visite.contains(noeud) || ciblesIgnorees.contains(noeud)) {
            return;
        }

        for (Noeud dependance : noeud.getDependances()) {
            executerRecettesRecursif(dependance, visite, debogage);
        }

        if (!noeud.aJour(debogage)) {
            if (debogage) {
                System.out.println("Exécution des recettes pour la cible : " + noeud.getNom());
            }

            ProcessBuilder pb = new ProcessBuilder(noeud.getRecettes());
            pb.inheritIO();
            Process process = pb.start();
            int retour = process.waitFor();

            if (retour != 0) {
                throw new Exception("Échec de la commande pour " + noeud.getNom());
            }
        } else if (debogage) {
            System.out.println("La cible " + noeud.getNom() + " est déjà à jour.");
        }

        visite.add(noeud);
    }

    
}

