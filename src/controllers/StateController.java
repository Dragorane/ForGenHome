package controllers;

import java.io.File;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import project.MainLauncher;

/*
 * Class EtatManager
 * Gere le traitement et la sauvegarde pour continuer un traitement si on arrete au milieu
 */


public class StateController  
{	
	/*Un etat est constitue du numero de fichier en cours, de la ligne dans ce fichier
	ainsi que le numero de sequence en cours */
	@Getter @Setter int fileNumber; 
	@Getter @Setter int lineNumber;
	@Getter @Setter int refSeqNumber;
	
	private ArrayList<String> fichiers = MainLauncher.fichiersBioinfo;

	//Creer un un etat a partir d'une sauvegarde
	public StateController(int fichier, int ligne) 
	{
		this.setFileNumber(fichier);
		this.setLineNumber(ligne);
		this.setRefSeqNumber(0);
	}
	
	//Creer un nouveau state
	public StateController() 
	{
		this.setFileNumber(0);
		this.setLineNumber(1);
		this.setRefSeqNumber(0);
	}
	
	//On verifie si un etat a ete sauvegarde (fichier etat.txt)
	public static StateController checkState()
	{
		File sauvegarde = new File("state.txt");
		StateController etat;
		
		if(sauvegarde.exists())
		{
			etat=FileController.retrieveState();
		}		
		else
		{
			etat = new StateController();
		}		
	
		return etat;
	}
	
	public void increFile() 
	{
		this.setFileNumber(this.getFileNumber()+1);
		this.setLineNumber(1);
		this.setRefSeqNumber(0);
	}
	
	public void increLine() 
	{
		this.setLineNumber(getLineNumber()+1);
		this.setRefSeqNumber(0);
	}
	
	public void increRefseq() 
	{
		this.setRefSeqNumber(this.refSeqNumber + 1);
	}
	
	//Le traitement est termine si on a recupere tous les genomes des fichiers
	public boolean traitementTermine() 
	{
		return this.getFileNumber() == fichiers.size();
	}
	
	//On verifie si, pour un genome, on a recupere toutes les sequences associees 
	public boolean toutRecuperer(int numRefSeq) 
	{
		return this.getRefSeqNumber() == numRefSeq;
	}
	
	// Recuperer le nom de fichier
	public String getNomFichier() 
	{
		return this.fichiers.get(fileNumber);
	}
	
	public int getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(int fileNumber) {
		this.fileNumber = fileNumber;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public int getRefSeqNumber() {
		return refSeqNumber;
	}

	public void setRefSeqNumber(int refSeqNumber) {
		this.refSeqNumber = refSeqNumber;
	}

	public ArrayList<String> getFichiers() {
		return fichiers;
	}

	public void setFichiers(ArrayList<String> fichiers) {
		this.fichiers = fichiers;
	}

	public String toString() 
	{
		return fileNumber + ":" + this.getNomFichier() + "; Numero Ligne : " + this.getLineNumber() + "; Numero Nucleotide : " + this.getRefSeqNumber(); 
	}
}
