import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Slider
 */
public class Processus {

    private int priority, arriveTime, id, // au debut
            Temps_execution, Temp_Block, Temp_ES; // a la fin

    public int etat1, etat2, // cpt de etat_Processus/2 (==size actuel )
            Etat_Qauntum,
            etat_exucution;
    boolean fin_de_traitement;
    private EtatProcess etat; // etat actuel (actif/pret /block

    public ArrayList<Object> etat_Processus; // le comportement du process 
    public ArrayList<CompProcess> etat_Processus2; // toutes les sequenses d'etats pour le process

    public Processus() {

        this.etat_Processus = new ArrayList<Object>();
        this.etat_Processus2 = new ArrayList<CompProcess>();

    }

    public Processus(int priority, int arriveTime, int id, int Temps_execution) {
        this.priority = priority;
        this.arriveTime = arriveTime;
        this.id = id;
        this.Temps_execution = Temps_execution;
        this.etat = EtatProcess.Actif;

        this.fin_de_traitement = false;
        this.etat_Processus = new ArrayList<Object>();
        this.etat_Processus2 = new ArrayList<CompProcess>();
        this.etat1 = 0;
        this.etat2 = 0;
        this.Etat_Qauntum = 0;
        this.etat_exucution = 0;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getTemps_execution() {
        return Temps_execution;
    }

    public void setTemps_execution(int Temps_execution) {
        this.Temps_execution = Temps_execution;
    }

    public int getTemp_ES() {
        return Temp_ES;
    }

    public void setTemp_ES(int Temp_ES) {
        this.Temp_ES = Temp_ES;
    }

    public int getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(int arriveTime) {
        this.arriveTime = arriveTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFin_de_traitement() {
        return fin_de_traitement;
    }

    public void setFin_de_traitement(boolean fin_de_traitement) {
        this.fin_de_traitement = fin_de_traitement;
    }

    public int getTemp_Block() {
        return Temp_Block;
    }

    public void setTemp_Block(int Temp_Block) {
        this.Temp_Block = Temp_Block;
    }

}
