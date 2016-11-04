package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import project.Genome;
import project.MainLauncher;

/*
 * Class FileManager
 * Creating all files and folders
 */

public class FileController {

	static boolean kingdomInUse = false;

	// Cleaning all excel files
	public static void cleaning() {
		File etat = new File("/Data/state.txt");

		if (etat.exists()) {
			etat.delete();
			etat = null;
		}

		for (String nomFichier : MainLauncher.fichiersBioinfo) {
			File fichier;
			fichier = new File(nomFichier);
			if (fichier.exists()) {
				fichier.delete();
				fichier = null;
			}

			File dossier;
			String nomDossier;
			nomDossier = nomFichier.split("\\.")[0];
			nomDossier = nomDossier.substring(0, 1).toUpperCase() + nomDossier.substring(1);
			dossier = new File(nomDossier);
			if (dossier.exists() && dossier.isDirectory()) {
				// deletingFolder(dossier);
			}

		}
	}

	// Deleting a folder recursively
	public static void deletingFolder(File folder) {
		File[] fichiers = folder.listFiles();
		for (File fichier : fichiers) {
			if (fichier.isDirectory()) {
				deletingFolder(fichier);
				fichier.delete();
			} else {
				fichier.delete();
			}
		}
		folder.delete();

	}

	// Saving sequences of a genome (in option)
	public static void sauvegarderSequence(Genome genome, String refseq, String sequence) {
		String dossier = genome.getChemin() + "/Genome/";
		bewFile(dossier);

		String fichier = dossier + refseq + ".txt";

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichier, true)));
			out.println(sequence);
			out.close();
		}

		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void bewFile(String chemin) {
		File dossier = new File(chemin);
		dossier.mkdirs();
	}

	// Possibility of saving 'genes' used (option)
	public static void sauvegarderInfos(Genome genome, ArrayList<String> infos) {
		String dossier = genome.getChemin() + "/Gene";
		bewFile(dossier);

		String fichier = dossier + "/infos.txt";

		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(fichier, true)));

			for (String record : infos) {
				out.println(record);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	// Saving the state
	public static void enregistrer(StateController etat, String file) {
		FileWriter fw = null;
		BufferedWriter output = null;
		try {
			fw = new FileWriter("state_" + file, false);
			output = new BufferedWriter(fw);
			output.write(etat.getLineNumber() + "\n");
			output.write(etat.getRefSeqNumber() + "\n");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur enregistrer : " + e.toString());
		} finally {
			try {
				output.flush();
				output.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Erreur enregistrer : " + e.toString());
			}

		}

	}

	// Retrieving the state we saved and creating a new one with these
	// informations
	public static StateController retrieveState(String file) {
		StateController res = null;

		File etat = new File("state_" + file);
		// System.out.println("Test File etat : " + etat.getName());

		if (etat.exists()) {
			// System.out.println("Une sauvegarde a ete trouve !");
			// System.out.println("Recuperation du fichier state.txt");

			Scanner sc = null;
			try {
				sc = new Scanner(etat);
				int ligne = Integer.parseInt(sc.nextLine());

				res = new StateController(file, ligne);
			} catch (Exception ex) {
				System.out.println("Erreur retrieveState : " + ex.toString());
			} finally {
				sc.close();
			}

		}
		return res;
	}

	public static void savingResults(Genome genome, String type, ArrayList<HashMap<String, BigInteger>> resultats)
			throws IOException, InvalidFormatException {
		writingExcel(genome.getChemin(), genome, type, resultats);
		createUpdateFile(genome.getChemin() + "\\updateDate.txt", genome);
		writingExcel(genome.getSubGroupChemin(), genome, type, resultats);
		writingExcel(genome.getGroupChemin(), genome, type, resultats);
		writingExcel(genome.getKingdom(), genome, type, resultats);
	}

	// Writing in the excel file
	public static void writingExcel(String nomDossier, Genome genome, String type,
			ArrayList<HashMap<String, BigInteger>> resultats) throws IOException, InvalidFormatException {
		try {
			String nomFichier = "";
			long nbSeq = 0;

			// System.out.println("Debug type : " + type);
			// System.out.println("Debug nomDossier : " + nomDossier);
			// System.out.println("Debug genome : " + genome.toString());

			if (type.equals("chrom")) {
				nomFichier = nomDossier + "/chromosomes.xlsx";
				nbSeq = genome.getNbSeqChrom();
			} else if (type.equals("plasm")) {
				nomFichier = nomDossier + "/plasmides.xlsx";
				nbSeq = genome.getNbSeqPlasm();
			} else if (type.equals("chloro")) {
				nomFichier = nomDossier + "/chloroplastes.xlsx";
				nbSeq = genome.getNbSeqChloro();
			} else if (type.equals("mito")) {
				nomFichier = nomDossier + "/mitochondries.xlsx";
				nbSeq = genome.getNbSeqMito();
			}

			ExcelController excel;

			File fichier = new File(nomFichier);
			File dossier = new File(nomDossier);

			if (!dossier.exists()) {
				bewFile(nomDossier);
			}

			// System.out.println("Debug fichier exists : " + fichier.exists());

			if (fichier.exists()) {
				System.out.println("File exist !! : " + nomFichier);
				excel = ExcelController.openingExistingFile(nomFichier);
			} else {
				System.out.println("New Excel !! : " + nomFichier);
				excel = ExcelController.newExcel(nomFichier);
				// System.out.println("Debug excel : " + excel.toString());
			}

			// System.out.println("Debug resultats : " + resultats.toString());
			System.out.println("Debug excel name : " + excel.name);

			if (resultats != null) {
				excel.writingResults(resultats);
				excel.writingResultDinucleotide(resultats);
			}

			excel.addingNbSeq(nbSeq);

			excel.saving();
		} catch (Exception e) {
			System.out.println("Erreur writing Excel : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void createUpdateFile(String path, Genome genome) {
		FileWriter fichier;
		File f = new File(path);

		if (f.exists()) {
			f.delete();
		}

		try {
			fichier = new FileWriter(path);
			fichier.write(genome.getUpdateDate());
			fichier.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createUpdateFile2(String string, Genome genome) {
		Path p = Paths.get(string);
		System.out.println("Test createUpdateFile2 : " + p.toString());
		if (java.nio.file.Files.exists(p)) {
			try {
				FileWriter fichier;
				fichier = new FileWriter(string + "\\updateDate.txt");
				fichier.write(genome.getUpdateDate());
				fichier.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
