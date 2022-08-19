/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Slider
 */
public class  CompProcess {
    
    int start,end;
    int Valeur ; 
    Boolean isCPU;
    public EtatProcess Statut_Processus;
    boolean termine;

    public CompProcess() {
    }

                
    // pour initialiser ls procees
                
    public CompProcess(int val , Boolean CPU)
     {
         
         this.Valeur = val ;
         this.isCPU = CPU ;
        this.termine=false;
     } 
    
    // pour déféinir le déroulement d'un process
    public CompProcess(int val, Boolean CPU,int debut , int fin) {
        
        
        this.Valeur = val ;
        this.isCPU = CPU ;
        this.start = debut ;
        this.end = fin ;
	this.termine=false;
    }
    
    
}
