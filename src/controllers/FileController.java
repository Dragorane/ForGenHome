package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import com.sun.media.sound.InvalidFormatException;

import project.Genome;
import project.MainLauncher;

/*
 * Class FileManager
 * Creating all files and folders
 */

public class FileController {

	static boolean kingdomInUse = false;

	// Cleaning all excel files
	public static void cleaning(String nameKingdom) {
		File etat = new File("state_"+nameKingdom+".txt");

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

	public static StateController retrieveState(String file) {
		StateController res = null;

		File etat = new File("state_" + file);

		if (etat.exists()) {
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
		writingExcel(genome.getChemin(), genome, type, resultats, "organisme");
		createUpdateFile(genome.getChemin()+genome.getName().replaceAll(" ", "_")+"_updateDate.txt", genome);
		writingExcel(genome.getSubGroupChemin(), genome, type, resultats, "subgroup");
		writingExcel(genome.getGroupChemin(), genome, type, resultats, "group");
		writingExcel(genome.getKingdomChemin(), genome, type, resultats, "kingdom");
	}

	// Sauvegarder les r�sultats dans un onglet du fichier excel
	public static void savingOngletResults(Genome genome, String type, ArrayList<HashMap<String, BigInteger>> resultats,
			String sheetName) {
		writingExcel(genome.getSubGroupChemin(), genome, type, resultats, "organisme", sheetName);
	}

	// Writing in the excel file
	public static void writingExcel(String dirName, Genome genome, String type,
			ArrayList<HashMap<String, BigInteger>> resultats, String location)
			throws IOException, InvalidFormatException {
		try {
			String fileName = "";
			String sheetName = "";
			//long nbSeq = 0;

			switch (location) {
			case "subgroup":
				fileName = dirName + "/Total_" + genome.getSubgroup() + ".xlsx";
				break;
			case "group":
				fileName = dirName + "/Total_" + genome.getGroup() + ".xlsx";
				break;
			case "kingdom":
				fileName = dirName + "/Total_" + genome.getKingdom() + ".xlsx";
				break;
			case "organisme":
				fileName = dirName + "" + genome.getName() + ".xlsx";
				break;
			default:
				System.out.println("Error location : " + location + " for Genome : " + genome.getName());
			}

			if (type.equals("chrom")) {
				sheetName = "Sum_Chromosome";
				//nbSeq = genome.getNbSeqChrom();
			} else if (type.equals("plasm")) {
				sheetName = "Sum_Plasmids";
				//nbSeq = genome.getNbSeqPlasm();
			} else if (type.equals("chloro")) {
				sheetName = "Sum_Chloroplast";
				//nbSeq = genome.getNbSeqChloro();
			} else if (type.equals("mito")) {
				sheetName = "Sum_Mitochondrion";
				//nbSeq = genome.getNbSeqMito();
			}

			ExcelController excel;

			//System.out.println("FileName : " + fileName);
			File fichier = new File(fileName);
			File dossier = new File(fileName);

			if (!dossier.exists()) {
				bewFile(dirName);
			}

			if (fichier.exists()) {
				//System.out.println("File exist !! : " + fileName);
				excel = ExcelController.openingExistingFile(fileName, sheetName);
			} else {
				//System.out.println("New Excel !! : " + fileName);
				excel = ExcelController.newExcel(fileName, sheetName);
			}

			//System.out.println("Debug excel name : " + excel.name);

			if (resultats != null) {
				excel.writingResults(resultats);
				excel.writingResultDinucleotide(resultats);

				Date today = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				switch (location) {
				case "subgroup":
					excel.writingInformations(resultats, "Total " + genome.getSubgroup(), "", sdf.format(today));
					break;
				case "group":
					excel.writingInformations(resultats, "Total " + genome.getGroup(), "", sdf.format(today));
					break;
				case "kingdom":
					excel.writingInformations(resultats, "Total " + genome.getKingdom(), "", sdf.format(today));
					break;
				case "organisme":
					excel.writingInformations(resultats, genome.getName(), genome.getBioproject(),
							genome.getUpdateDate());
					break;
				default:
					System.out.println("Error location : " + location + " for Genome : " + genome.getName());
				}

				excel.addingNbSeq(resultats.get(8).get("nbCDS").doubleValue(),
						resultats.get(8).get("nbCDSDinucleotide").doubleValue());
			}

			//excel.addingNbSeq(nbSeq);

			excel.saving();
		} catch (Exception e) {
			System.out.println("Erreur writing Excel : " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Deuxi�me fonction pour ajouter les onglets sp�cifiques aux
	// s�quences
	public static void writingExcel(String dirName, Genome genome, String type,
			ArrayList<HashMap<String, BigInteger>> resultats, String location, String newSheetName) {
		try {
			String fileName = "";
			// long nbSeq = 0;

			switch (location) {
			case "subgroup":
				fileName = dirName + "/Total_" + genome.getSubgroup() + ".xlsx";
				break;
			case "group":
				fileName = dirName + "/Total_" + genome.getGroup() + ".xlsx";
				break;
			case "kingdom":
				fileName = dirName + "/Total_" + genome.getKingdom() + ".xlsx";
				break;
			case "organisme":
				fileName = dirName + "/" + genome.getName() + ".xlsx";
				break;
			default:
				System.out.println("Error location : " + location + " for Genome : " + genome.getName());
			}

			ExcelController excel;

			File fichier = new File(fileName);
			File dossier = new File(fileName);

			if (!dossier.exists()) {
				bewFile(dirName);
			}

			if (fichier.exists()) {
				// System.out.println("File exist !! : " + fileName);
				excel = ExcelController.openingExistingFile(fileName);
				excel.createNewSheetInExcel(newSheetName);
				excel.selectSheetFromExcel(newSheetName);
				// System.out.println("Debug excel : " + excel.toString());
			} else {
				// System.out.println("New Excel !! : " + fileName);
				excel = ExcelController.newExcel(fileName, newSheetName);
				excel.selectSheetFromExcel(newSheetName);
				// System.out.println("Debug excel : " + excel.toString());
			}

			// System.out.println("Debug excel name : " + excel.name);
			// System.out.println("Debug excel sheet name : " +
			// excel.sheet.getSheetName());

			if (resultats != null) {
				excel.writingResults(resultats);
				excel.writingResultDinucleotide(resultats);
				excel.writingInformations(resultats, newSheetName, genome.getBioproject(), genome.getUpdateDate());
			}

			excel.addingNbSeq(resultats.get(8).get("nbCDS").doubleValue(),
					resultats.get(8).get("nbCDSDinucleotide").doubleValue());
			// excel.addingNbSeq(nbSeq);

			excel.saving();
		} catch (Exception e) {
			System.out.println("");
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
			e.printStackTrace();
		}
	}
}
