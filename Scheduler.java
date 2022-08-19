import java.util.ArrayList;
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Slider
 */
public class Scheduler {

    private int nbES;
    private int QuantaVal;
    private int E_S[]; // Temps d'entree sortie
    private ArrayList<Processus> proceList;

    private Processus Qui_execute;
    private boolean[] perpi_used;
    private LinkedList<Processus>[] file_attendre_exuction, file_attendre_es;
    private ArrayList<String> E_Snames;

    public Scheduler(ArrayList<Processus> Liste, int Quantum, int E_S[], int nb_perpfirique) {
        super();

        this.proceList = Liste;
        this.QuantaVal = Quantum;
        this.E_S = E_S;
        this.nbES = nb_perpfirique;
        perpi_used = new boolean[nb_perpfirique];
        file_attendre_exuction = new LinkedList[10];
        this.Qui_execute = null;

        for (int j = 0; j < 10; j++) {
            file_attendre_exuction[j] = new LinkedList<Processus>();
        }
        file_attendre_es = new LinkedList[nb_perpfirique + 1];
        for (int j = 0; j < nb_perpfirique + 1; j++) {
            file_attendre_es[j] = new LinkedList<Processus>();
        }

    }

    public void StartSched() {
        int Valeur_final = 0;

        Processus encour;

        while (!test_fin()) {
            for (int i = 0; i < this.proceList.size(); i++) {
                encour = (Processus) (this.proceList.get(i));                

                if (encour.getArriveTime() > Valeur_final) { // process n'est pas encore arrivé
                    if (encour.etat_Processus2.isEmpty()) {
                        encour.etat_Processus2.add(new CompProcess());
                        CompProcess ss = encour.etat_Processus2.get(0);                        
                        ss.end = encour.getArriveTime();
                        ss.start = 0;
                        ss.Statut_Processus = EtatProcess.NonExist;
                        encour.etat2++;
                    }

                } else {
                    if (!encour.fin_de_traitement) { // process existant
                        if (encour.etat_Processus.get(encour.etat1) instanceof String) {

                            Traitement_entre_sortie(encour, Valeur_final);

                        } else {
                            Traitement_de_exution(encour, Valeur_final);
                        }

                    }
                }

            }
            Valeur_final++;
        }
    }

    public void setE_Snames(ArrayList<String> E_Snames) {
        this.E_Snames = E_Snames;
    }

    public boolean test_fin() { // fin de tous les process
        boolean test = true;
        for (int i = 0; i < this.proceList.size(); i++) {
            test = test && this.proceList.get(i).fin_de_traitement;
        }
        return test;

    }

    public Processus droit_exuter() { // return 1er file exe

        boolean test = false;
        int i = 9;
        while (i >= 0) {
            if (!file_attendre_exuction[i].isEmpty()) {
                return file_attendre_exuction[i].getFirst();
            }
            i--;
        }
        return null;
    }

    public boolean file_att_exe_est_vide() {
        boolean test = true;
        for (int i = 0; i < this.file_attendre_exuction.length; i++) {
            test = test && this.file_attendre_exuction[i].isEmpty();
        }
        return test;

    }

    public ArrayList<Processus> getproceList() {
        return proceList;
    }

    public void Traitement_entre_sortie(Processus encour, int Valeur_final) {
        if (!encour.fin_de_traitement) {

            String s = encour.etat_Processus.get(encour.etat1).toString();//.substring(1, 2);

            //int nb = Integer.parseInt(s);
            int nb = E_Snames.indexOf(s);
            if (file_attendre_es[nb].isEmpty()) {

                encour.etat_Processus2.add(new CompProcess());
                encour.etat_Processus2.get(encour.etat2).start = Valeur_final;

                CompProcess ss = encour.etat_Processus2.get(encour.etat2);
                ss.end = Valeur_final;
                ss.Statut_Processus = EtatProcess.E_S;
                file_attendre_es[nb].add(encour);

            } else {
                if (file_attendre_es[nb].getFirst().equals(encour)) {// si le process est en tete de file ES                    
                    
                    if (encour.etat_Processus2.size() == encour.etat2) {
                        encour.etat_Processus2.get(encour.etat2 - 1).end = Valeur_final;
                        encour.etat_Processus2.add(new CompProcess());
                        encour.etat_Processus2.get(encour.etat2).start = Valeur_final;
                        encour.etat_Processus2.get(encour.etat2).Statut_Processus = EtatProcess.E_S;

                    }
                    CompProcess ss = encour.etat_Processus2.get(encour.etat2);
                    ss.end = Valeur_final;
                    
                    if ((ss.end - ss.start) == this.E_S[nb]) {// fin ES
                        
                        file_attendre_es[nb].removeFirst();
                        encour.etat1++;
                        if (encour.etat_Processus.size() == encour.etat1) {
                            encour.fin_de_traitement = true;
                        }
                        encour.etat2++;
                        encour.setPriority(encour.getPriority() + 1);
                        if (!encour.fin_de_traitement) {

                            if (encour.etat_Processus.get(encour.etat1) instanceof String) {

                                Traitement_entre_sortie(encour, Valeur_final);

                            } else {
                                Traitement_de_exution(encour, Valeur_final);
                            }
                        }
                        if (!file_attendre_es[nb].isEmpty()) {

                            Traitement_entre_sortie(file_attendre_es[nb].getFirst(), Valeur_final);
                        }
                    }

                } else {
                    if (file_attendre_es[nb].contains(encour)) {
                        // le process est en att ES
                        CompProcess ss = encour.etat_Processus2.get(encour.etat2 - 1);
                        ss.end = Valeur_final;

                    } else {// new Process ==> inserer dans la file
                        
                        encour.etat2++;
                        encour.etat_Processus2.add(new CompProcess());
                        encour.etat_Processus2.get(encour.etat2 - 1).start = Valeur_final;
                        encour.etat_Processus2.get(encour.etat2 - 1).Statut_Processus = EtatProcess.wait_es;
                        file_attendre_es[nb].add(encour);
                    }
                }

            }

        }

    }

    public void Traitement_de_exution(Processus encour, int Valeur_final) {

        if (!encour.fin_de_traitement) {

            int Valeur_de_exu = (int) encour.etat_Processus.get(encour.etat1);
            
            if (Qui_execute == null) {
                if (file_att_exe_est_vide()) {

                    Qui_execute = encour;
                    encour.etat_Processus2.add(new CompProcess());
                    encour.etat2 = encour.etat_Processus2.size() - 1;
                    encour.etat_Processus2.get(encour.etat2).start = Valeur_final;
                    encour.etat_Processus2.get(encour.etat2).end = Valeur_final;
                    encour.etat_Processus2.get(encour.etat2).Statut_Processus = EtatProcess.Actif;

                } else {
                    if (encour.equals(droit_exuter())) {// si le process est le 1er dans file exe 
                        
                        Qui_execute = encour;
                        encour.etat_Processus2.add(new CompProcess());
                        encour.etat2 = encour.etat_Processus2.size() - 1;
                        encour.etat_Processus2.get(encour.etat2).start = Valeur_final;
                        encour.etat_Processus2.get(encour.etat2).end = Valeur_final;
                        encour.etat_Processus2.get(encour.etat2).Statut_Processus = EtatProcess.Actif;
                        file_attendre_exuction[encour.getPriority()].removeFirst();

                    } else {
                        if (encour.getPriority() > droit_exuter().getPriority()) {
                            Qui_execute = encour;
                            encour.etat_Processus2.add(new CompProcess());
                            encour.etat2 = encour.etat_Processus2.size() - 1;
                            encour.etat_Processus2.get(encour.etat2).start = Valeur_final;
                            encour.etat_Processus2.get(encour.etat2).end = Valeur_final;
                            encour.etat_Processus2.get(encour.etat2).Statut_Processus = EtatProcess.Actif;
                        } else {
                            if (file_attendre_exuction[encour.getPriority()].contains(encour)) {
                                encour.etat_Processus2.get(encour.etat2).end = Valeur_final;
                            } else {
                                encour.etat_Processus2.add(new CompProcess());
                                encour.etat2 = encour.etat_Processus2.size() - 1;
                                encour.etat_Processus2.get(encour.etat2).start = Valeur_final;
                                encour.etat_Processus2.get(encour.etat2).end = Valeur_final;
                                encour.etat_Processus2.get(encour.etat2).Statut_Processus = EtatProcess.Blocked;
                            }
                        }

                    }
                }
            } else {
                if (encour.equals(Qui_execute)) {
                    
                    CompProcess ss = encour.etat_Processus2.get(encour.etat2);
                    ss.end = Valeur_final;                    
                    encour.Etat_Qauntum++;
                    encour.etat_exucution++;
                    if (ss.start == Valeur_final) {
                        encour.etat_exucution--;
                        encour.Etat_Qauntum--;
                    }
                    if (Valeur_de_exu == encour.etat_exucution) {

                        encour.etat1++;
                        encour.etat2++;
                        if (encour.etat_Processus.size() == encour.etat1) {
                            encour.fin_de_traitement = true;
                        }
                        encour.etat_exucution = 0;

                        Qui_execute = null;
                        Traitement_entre_sortie(encour, Valeur_final); // signaler non E/S aux autres
                        if (!file_att_exe_est_vide()) {

                            Qui_execute = droit_exuter();
                            encour = proceList.get(proceList.indexOf(Qui_execute));
                            encour.etat_Processus2.get(encour.etat2).end = Valeur_final;
                            encour.etat_Processus2.add(new CompProcess());
                            encour.etat2 = encour.etat_Processus2.size() - 1;
                            encour.etat_Processus2.get(encour.etat2).start = Valeur_final;
                            encour.etat_Processus2.get(encour.etat2).Statut_Processus = EtatProcess.Actif;
                            file_attendre_exuction[encour.getPriority()].removeFirst();
                        }
                        if (encour.Etat_Qauntum == this.QuantaVal) {
                            encour.setPriority(0);
                            encour.Etat_Qauntum = 0;
                        }
                    } else if (encour.Etat_Qauntum == this.QuantaVal) {
                        
                        encour.Etat_Qauntum = 0;
                        encour.setPriority(0);                        
                        if (!file_att_exe_est_vide()) {

                            file_attendre_exuction[0].add(encour);
                            encour.etat_Processus2.add(new CompProcess());
                            encour.etat2 = encour.etat_Processus2.size() - 1;
                            encour.etat_Processus2.get(encour.etat2).start = Valeur_final;
                            encour.etat_Processus2.get(encour.etat2).Statut_Processus = EtatProcess.Blocked;

                            Qui_execute = droit_exuter();
                            encour = proceList.get(proceList.indexOf(Qui_execute));

                            encour.etat_Processus2.get(encour.etat2).end = Valeur_final;
                            encour.etat_Processus2.add(new CompProcess());
                            encour.etat2 = encour.etat_Processus2.size() - 1;
                            encour.etat_Processus2.get(encour.etat2).start = Valeur_final;
                            encour.etat_Processus2.get(encour.etat2).Statut_Processus = EtatProcess.Actif;

                            file_attendre_exuction[encour.getPriority()].removeFirst();

                        }                        

                    }

                } else { // inserer a la file exe / interompre autre process
                    if (encour.getPriority() > Qui_execute.getPriority()) { // interompre
                        encour.etat_Processus2.add(new CompProcess());
                        Processus Save_Processus = encour;
                        encour.etat2 = encour.etat_Processus2.size() - 1;
                        encour.etat_Processus2.get(encour.etat2).start = Valeur_final;
                        encour.etat_Processus2.get(encour.etat2).end = Valeur_final;
                        encour.etat_Processus2.get(encour.etat2).Statut_Processus = EtatProcess.Actif;
                        encour = proceList.get(proceList.indexOf(Qui_execute));
                        if (Save_Processus.getId() < encour.getId()) {
                            encour.etat_exucution++;
                            encour.Etat_Qauntum++;
                        }
                        if (encour.etat_exucution == (int) encour.etat_Processus.get(encour.etat1)) { // fin exe
                            encour.etat_Processus2.get(encour.etat2).end = Valeur_final;
                            encour.etat_exucution = 0;                            
                            encour.etat1++;
                            encour.etat2++;
                            if (encour.etat_Processus.size() == encour.etat1) {
                                encour.fin_de_traitement = true;
                            }

                            Traitement_entre_sortie(encour, Valeur_final); // signaler non E/S aux autres 
                            if (encour.Etat_Qauntum == this.QuantaVal) { // fin de quantum
                                encour.Etat_Qauntum = 0;
                                encour.setPriority(0);
                            }
                        } else {                            
                            file_attendre_exuction[Qui_execute.getPriority()].addFirst(encour);
                            encour.etat_Processus2.get(encour.etat2).end = Valeur_final;
                            encour.etat_Processus2.add(new CompProcess());
                            encour.etat2 = encour.etat_Processus2.size() - 1;
                            encour.etat_Processus2.get(encour.etat2).start = Valeur_final;
                            encour.etat_Processus2.get(encour.etat2).end = Valeur_final;
                            encour.etat_Processus2.get(encour.etat2).Statut_Processus = EtatProcess.Blocked;
                        }

                        Qui_execute = Save_Processus;

                    } else { // inserer dans la file exe
                        if (file_attendre_exuction[encour.getPriority()].contains(encour)) {
                            encour.etat_Processus2.get(encour.etat2).end = Valeur_final;
                        } else {

                            encour.etat_Processus2.add(new CompProcess());
                            encour.etat2 = encour.etat_Processus2.size() - 1;
                            encour.etat_Processus2.get(encour.etat2).start = Valeur_final;
                            encour.etat_Processus2.get(encour.etat2).Statut_Processus = EtatProcess.Blocked;
                            file_attendre_exuction[encour.getPriority()].add(encour);
                        }
                    }

                }
            }

        }

    }

}
