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

public class StateController {
	/*
	 * Un etat est constitue du nom de fichier en cours, de la ligne dans ce
	 * fichier ainsi que le numero de sequence en cours
	 */
	@Getter
	@Setter
	String fileName;
	@Getter
	@Setter
	int lineNumber;
	@Getter
	@Setter
	int refSeqNumber;
	private boolean end = false;

	private ArrayList<String> fichiers = MainLauncher.fichiersBioinfo;

	// Creer un un etat a partir d'une sauvegarde
	public StateController(String name, int ligne) {
		this.setLineNumber(ligne);
		this.setRefSeqNumber(0);
	}

	// Creer un nouveau state
	public StateController(String name) {
		this.setFileName(name);
		this.setLineNumber(1);
		this.setRefSeqNumber(0);
	}

	// On verifie si un etat a ete sauvegarde (fichier etat.txt)
	public static StateController checkState(String file) {
		File sauvegarde = new File("/Data/state_" + file);
		// System.out.println("Test file StateController : " +
		// sauvegarde.getName() + " test exists : " + sauvegarde.exists());
		StateController etat;

		if (sauvegarde.exists()) {
			etat = FileController.retrieveState(file);
		} else {
			etat = new StateController(file);
		}

		return etat;
	}

	public void increLine() {
		this.setLineNumber(getLineNumber() + 1);
		this.setRefSeqNumber(0);
	}

	public void increRefseq() {
		this.setRefSeqNumber(this.refSeqNumber + 1);
	}

	// Le traitement est termine si on a recupere tous les genomes des fichiers
	public boolean traitementTermine() {
		return isEnd();
	}

	// On verifie si, pour un genome, on a recupere toutes les sequences
	// associees
	public boolean toutRecuperer(int numRefSeq) {
		return this.getRefSeqNumber() == numRefSeq;
	}

	// Recuperer le nom de fichier
	public String getNomFichier() {
		return this.fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public String toString() {
		return fileName + ":" + this.getNomFichier() + "; Numero Ligne : " + this.getLineNumber()
				+ "; Numero Nucleotide : " + this.getRefSeqNumber();
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}
}
