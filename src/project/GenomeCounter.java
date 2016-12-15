package project;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import controllers.FileController;
import controllers.StateController;
import enums.Nucleotide;
import ui.Interface;

/*
 * Class GenomeCounter
 * Classe qui permet de gerer les differents traitement en appelant les autres classes
 */
public class GenomeCounter extends Thread {
	public static boolean stop = false;
	private Interface ihm_log = null;
	private String file = null;

	/*
	 * Public Functions
	 */

	public GenomeCounter(Interface ihm, String tmpFile) {
		if (ihm != null && tmpFile != null) {
			ihm_log = ihm;
			file = tmpFile;
		}
	}

	public void run() {
		try {
			demarrerTraitement(file);
		} catch (Exception e) {
			ihm_log.addLog("Erreur dans le lancement du Thread GenomeCounter fichier : " + file);
			ihm_log.addLog(e.toString());
		}
	}

	// Ajoute les resultats a la HashMap de resultat
	public static ArrayList<HashMap<String, BigInteger>> ajoutResultats(Genome genome, String sequence, String type,
			ArrayList<HashMap<String, BigInteger>> ancienneMap) {
		HashMap<String, BigInteger> Ph0;
		HashMap<String, BigInteger> Ph1;
		HashMap<String, BigInteger> Ph2;

		HashMap<String, BigInteger> PrefPh0;
		HashMap<String, BigInteger> PrefPh1;
		HashMap<String, BigInteger> PrefPh2;

		HashMap<String, BigInteger> TempPh0;
		HashMap<String, BigInteger> TempPh1;
		HashMap<String, BigInteger> TempPh2;

		HashMap<String, BigInteger> Ph0Dinucleotide;
		HashMap<String, BigInteger> Ph1Dinucleotide;

		HashMap<String, BigInteger> TempPh0Dinucleotide;
		HashMap<String, BigInteger> TempPh1Dinucleotide;
		
		HashMap<String, BigInteger> Informations;

		ArrayList<HashMap<String, BigInteger>> mapResultat = new ArrayList<HashMap<String, BigInteger>>();

		TempPh0 = genererMap();
		TempPh1 = genererMap();
		TempPh2 = genererMap();

		TempPh0Dinucleotide = genererMapDinucleotide();
		TempPh1Dinucleotide = genererMapDinucleotide();

		if (ancienneMap == null) {
			Ph0 = genererMap();
			Ph1 = genererMap();
			Ph2 = genererMap();
			Ph0Dinucleotide = genererMapDinucleotide();
			Ph1Dinucleotide = genererMapDinucleotide();
			PrefPh0 = genererMap();
			PrefPh1 = genererMap();
			PrefPh2 = genererMap();
			Informations = genererMapInformation();
		} else {
			Ph0 = ancienneMap.get(0);
			Ph1 = ancienneMap.get(1);
			Ph2 = ancienneMap.get(2);
			PrefPh0 = ancienneMap.get(3);
			PrefPh1 = ancienneMap.get(4);
			PrefPh2 = ancienneMap.get(5);

			Ph0Dinucleotide = ancienneMap.get(6);
			Ph1Dinucleotide = ancienneMap.get(7);
			
			Informations = ancienneMap.get(8);
		}

		BigInteger nbTrinucleotide;
		BigInteger prefTrinucleotide;
		String trinucleotide;
		String dinucleotide;
		BigInteger nbDinucleotide;
		BigInteger prefDinucleotide;

		BigInteger nbCDS;
		BigInteger nbInvalideCDS;
		BigInteger nbNucleotide;
		BigInteger nbCDSDinucleotide;
		
		if (verifSequence(sequence)) {
			nbCDS = Informations.get("nbCDS");
			Informations.put("nbCDS", nbCDS.add(BigInteger.ONE));
			
			nbNucleotide = Informations.get("nbNucleotide");
			Informations.put("nbNucleotide", nbNucleotide.add(BigInteger.valueOf(sequence.length())));
			
			if (type.equals("chrom")) {
				genome.setNbSeqChrom(genome.getNbSeqChrom() + 1);
			} else if (type.equals("chloro")) {
				genome.setNbSeqChloro(genome.getNbSeqChloro() + 1);
			} else if (type.equals("plasm")) {
				genome.setNbSeqPlasm(genome.getNbSeqPlasm() + 1);
			} else if (type.equals("mito")) {
				genome.setNbSeqMito(genome.getNbSeqMito() + 1);
			}

			int ph = 0;

			for (int i = 0; i < sequence.length() - 3; i++) {
				trinucleotide = null;
				trinucleotide = sequence.charAt(i) + "" + sequence.charAt(i + 1) + "" + sequence.charAt(i + 2);

				if (ph == 0) {
					nbTrinucleotide = Ph0.get(trinucleotide);
					Ph0.put(trinucleotide, nbTrinucleotide.add(BigInteger.ONE));

					prefTrinucleotide = TempPh0.get(trinucleotide);
					TempPh0.put(trinucleotide, prefTrinucleotide.add(BigInteger.ONE));

					ph++;

				} else if (ph == 1) {
					nbTrinucleotide = Ph1.get(trinucleotide);
					Ph1.put(trinucleotide, nbTrinucleotide.add(BigInteger.ONE));

					prefTrinucleotide = TempPh1.get(trinucleotide);
					TempPh1.put(trinucleotide, prefTrinucleotide.add(BigInteger.ONE));

					ph++;
				} else if (ph == 2) {
					nbTrinucleotide = Ph2.get(trinucleotide);
					Ph2.put(trinucleotide, nbTrinucleotide.add(BigInteger.ONE));

					prefTrinucleotide = TempPh2.get(trinucleotide);
					TempPh2.put(trinucleotide, prefTrinucleotide.add(BigInteger.ONE));

					ph = 0;
				}

				nbTrinucleotide = null;
				trinucleotide = null;
			}

			ph = 0;

			// SI sequence est modulo 3 (teste ulterieurement) et PAIR
			if (sequence.length() % 2 == 0) {
				nbCDSDinucleotide = Informations.get("nbCDSDinucleotide");
				Informations.put("nbCDSDinucleotide", nbCDSDinucleotide.add(BigInteger.ONE));
				
				for (int i = 0; i < sequence.length() - 4; i++) {
					dinucleotide = null;
					dinucleotide = sequence.charAt(i) + "" + sequence.charAt(i + 1);

					if (ph == 0) {
						nbDinucleotide = Ph0Dinucleotide.get(dinucleotide);
						Ph0Dinucleotide.put(dinucleotide, nbDinucleotide.add(BigInteger.ONE));

						prefDinucleotide = TempPh0Dinucleotide.get(dinucleotide);
						TempPh0Dinucleotide.put(dinucleotide, prefDinucleotide.add(BigInteger.ONE));

						ph++;

					} else if (ph == 1) {
						nbDinucleotide = Ph1Dinucleotide.get(dinucleotide);
						Ph1Dinucleotide.put(dinucleotide, nbDinucleotide.add(BigInteger.ONE));

						prefDinucleotide = TempPh1Dinucleotide.get(dinucleotide);
						TempPh1Dinucleotide.put(dinucleotide, prefDinucleotide.add(BigInteger.ONE));

						ph = 0;
					}

					nbDinucleotide = null;
					dinucleotide = null;
				}
			} else {
				for (int i = 0; i < sequence.length() - 3; i++) {
					dinucleotide = null;
					dinucleotide = sequence.charAt(i) + "" + sequence.charAt(i + 1);

					if (ph == 0) {
						nbDinucleotide = Ph0Dinucleotide.get(dinucleotide);
						Ph0Dinucleotide.put(dinucleotide, nbDinucleotide.add(BigInteger.ONE));

						prefDinucleotide = TempPh0Dinucleotide.get(dinucleotide);
						TempPh0Dinucleotide.put(dinucleotide, prefDinucleotide.add(BigInteger.ONE));

						ph++;

					} else if (ph == 1) {
						nbDinucleotide = Ph1Dinucleotide.get(dinucleotide);
						Ph1Dinucleotide.put(dinucleotide, nbDinucleotide.add(BigInteger.ONE));

						prefDinucleotide = TempPh1Dinucleotide.get(dinucleotide);
						TempPh1Dinucleotide.put(dinucleotide, prefDinucleotide.add(BigInteger.ONE));

						ph = 0;
					}

					nbDinucleotide = null;
					dinucleotide = null;
				}
			}

			for (@SuppressWarnings("rawtypes")
			Map.Entry mapentry : TempPh0.entrySet()) {
				ArrayList<Integer> maxtab = maxPh(TempPh0.get(mapentry.getKey()), TempPh1.get(mapentry.getKey()),
						TempPh2.get(mapentry.getKey()));
				for (Integer w : maxtab) {
					if (w == 0) {
						BigInteger bg = PrefPh0.get(mapentry.getKey());
						PrefPh0.put((String) mapentry.getKey(), bg.add(BigInteger.ONE));
					} else if (w == 1) {
						BigInteger bg = PrefPh1.get(mapentry.getKey());
						PrefPh1.put((String) mapentry.getKey(), bg.add(BigInteger.ONE));
					} else {
						BigInteger bg = PrefPh2.get(mapentry.getKey());
						PrefPh2.put((String) mapentry.getKey(), bg.add(BigInteger.ONE));
					}
				}
			}
		} // SEQUENCE NON CONFORME
		else {			
			nbInvalideCDS = Informations.get("nbInvalideCDS");
			Informations.put("nbInvalideCDS", nbInvalideCDS.add(BigInteger.ONE));
		}

		mapResultat.add(Ph0);
		mapResultat.add(Ph1);
		mapResultat.add(Ph2);

		mapResultat.add(PrefPh0);
		mapResultat.add(PrefPh1);
		mapResultat.add(PrefPh2);

		mapResultat.add(Ph0Dinucleotide);
		mapResultat.add(Ph1Dinucleotide);
		
		mapResultat.add(Informations);

		return mapResultat;
	}

	/*
	 * Private Functions
	 */
	// Demarre le traitement
	private void demarrerTraitement(String file) {
		boolean optionInfo = this.ihm_log.check_cds.isSelected();
		boolean optionSequence = this.ihm_log.check_sequence.isSelected();

		StateController etat = StateController.checkState(file);

		ihm_log.addLog("--- Demarrage du traitement " + file + "---\n");

		GenomeReader getGenome = new GenomeReader();
		ArrayList<ArrayList<String>> sequence_info = new ArrayList<ArrayList<String>>();

		// Pour chaque type, on a la liste des trinucletides avec leur compteur
		// associe
		ArrayList<HashMap<String, BigInteger>> resultatsChrom = new ArrayList<HashMap<String, BigInteger>>();
		ArrayList<HashMap<String, BigInteger>> resultatsChloro = new ArrayList<HashMap<String, BigInteger>>();
		ArrayList<HashMap<String, BigInteger>> resultatsPlasm = new ArrayList<HashMap<String, BigInteger>>();
		ArrayList<HashMap<String, BigInteger>> resultatsMito = new ArrayList<HashMap<String, BigInteger>>();
		// Ajout du compteur pour la s�quence actuelle
		ArrayList<HashMap<String, BigInteger>> resultatsSequenceTemp = new ArrayList<HashMap<String, BigInteger>>();

		getGenome.getFiles(ihm_log, file);

		getGenome.resetDate();
		while (!etat.traitementTermine() && !stop) {
			try {
				Genome genome = null;
				resultatsChrom = null;
				resultatsChloro = null;
				resultatsPlasm = null;
				resultatsMito = null;
				sequence_info = null;

				// On recupere un des genomes deja telecharger
				genome = getGenome.get(etat);

				if (genome == null) {
					System.out.println("Test Thread : " + file + " termine.");
					etat.setEnd(true);
					FileController.enregistrer(etat, file);
					continue;
				}

				// Si le genome n'existe pas, ou s'il existe mais que le
				// traitement a ete arrete au milieu
				genome.setListeSequence(new ArrayList<String>());
				if (!genome.exists() && genome.getRefseq().size() != 0) {
					ihm_log.addLog("\n--- Nouvel organisme : " + genome + "---");
					ihm_log.addLog("Contenu dans " + etat.getNomFichier() + " [ " + (etat.getLineNumber() + 1) + " ]");
					getGenome.setNombreSequencesLues(getGenome.getNombreSequencesLues() + 1);
					while (!etat.toutRecuperer(genome.getRefseq().size())) {
						// On recupere la refseq et son type
						String[] ref_type = genome.getRefseq().get(etat.getRefSeqNumber());
						String refseq = ref_type[0];
						String type = ref_type[1];

						// On recupere la sequence et les infos (genes) associes
						sequence_info = getGenome.getSequence(genome, refseq, optionSequence, file);

						// On sauvegarder les infos (option)
						if (optionInfo) {
							FileController.sauvegarderInfos(genome, sequence_info.get(1), sequence_info.get(0));
						}
						
						resultatsSequenceTemp = null;
						String typeName = "";
						// Pour les sequences recuperees, on ajoute les
						// resultats en fonction du type
						for (String sequence : sequence_info.get(0)) {
							if (type.equals("chrom")) {
								typeName = "Chromosome_";
								resultatsChrom = ajoutResultats(genome, sequence, type, resultatsChrom);
							} else if (type.equals("chloro")) {
								typeName = "Chloroplast_";
								resultatsChloro = ajoutResultats(genome, sequence, type, resultatsChloro);
							} else if (type.equals("plasm")) {
								typeName = "Plasmid_";
								resultatsPlasm = ajoutResultats(genome, sequence, type, resultatsPlasm);
							} else if (type.equals("mito")) {
								typeName = "Mitochondrion_";
								resultatsMito = ajoutResultats(genome, sequence, type, resultatsMito);
							}

							// Calcul de la hashMap Resultat de la sequence en
							// cours
							resultatsSequenceTemp = ajoutResultats(genome, sequence, type, resultatsSequenceTemp);
						}

						// Sauvegarder la HashMap de resultat d'un fichier du
						// genome
						FileController.savingOngletResults(genome, type, resultatsSequenceTemp, typeName + refseq);
						resultatsSequenceTemp.clear();

						etat.increRefseq();
						FileController.enregistrer(etat, file);
					}

					// Selon le type, on enregistre les resultats
					if (resultatsChrom != null) {
						FileController.savingResults(genome, "chrom", resultatsChrom);
					}
					if (resultatsChloro != null) {
						FileController.savingResults(genome, "chloro", resultatsChloro);
					}
					if (resultatsPlasm != null) {
						FileController.savingResults(genome, "plasm", resultatsPlasm);
					}
					if (resultatsMito != null) {
						FileController.savingResults(genome, "mito", resultatsMito);
					}
				}




				etat.increLine();
				FileController.enregistrer(etat, file);
				if (optionSequence)
					FileController.sauvegarderGenome(genome);

				switch(file) {
				case "eukaryotes.txt": ihm_log.setNbEukae_current(ihm_log.getNbEukae_current()+1);break;
				case "prokaryotes.txt":ihm_log.setNbProka_current(ihm_log.getNbProka_current()+1);break;
				case "viruses.txt":ihm_log.setNbVirus_current(ihm_log.getNbVirus_current()+1);break;
				}
				
				//FIN D'UN GENOME - BARRE DE PROGRESSION EVOLUE ICI !
				ihm_log.progress_bar.setValue(ihm_log.progress_bar.getValue()+1);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		ihm_log.addLog("**** Recuperation terminee de " + file + "****");

	}

	private static ArrayList<Integer> maxPh(BigInteger ph0, BigInteger ph1, BigInteger ph2) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		if (ph0.intValue() == 0 && ph1.intValue() == 0 && ph2.intValue() == 0) {
			return res;
		}

		int phs[] = { ph0.intValue(), ph1.intValue(), ph2.intValue() };

		int i = 0;
		int maxIndex = 0;
		int maxCount = 1;

		for (i = 0; i < 3; i++) {
			if (phs[i] > phs[maxIndex]) {
				maxIndex = i;
				maxCount = 1;
			} else if (phs[i] == phs[maxIndex]) {
				maxCount++;
			}
		}

		if (maxCount == 1) {
			res.add(i);
			return res;
		} else {
			for (i = 0; i < 3; i++) {
				if (phs[i] == phs[maxIndex]) {
					res.add(i);
				}
			}
			return res;
		}
	}

	// Generation des Hashmap de resultats
	private static HashMap<String, BigInteger> genererMap() {
		HashMap<String, BigInteger> res = new HashMap<String, BigInteger>();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					String trinucleotide = "" + Nucleotide.values()[i] + Nucleotide.values()[j]
							+ Nucleotide.values()[k];
					res.put(trinucleotide, new BigInteger("0"));
				}
			}
		}
		return res;
	}

	private static HashMap<String, BigInteger> genererMapDinucleotide() {
		HashMap<String, BigInteger> res = new HashMap<String, BigInteger>();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				String dinucleatide = "" + Nucleotide.values()[i] + Nucleotide.values()[j];
				res.put(dinucleatide, new BigInteger("0"));
			}
		}
		return res;
	}
	
	private static HashMap<String, BigInteger> genererMapInformation() {
		HashMap<String, BigInteger> res = new HashMap<String, BigInteger>();
		res.put("nbNucleotide", new BigInteger("0"));
		res.put("nbCDS", new BigInteger("0"));
		res.put("nbInvalideCDS", new BigInteger("0"));
		res.put("nbCDSDinucleotide", new BigInteger("0"));
		return res;
	}

	// Verifie si la sequence est correcte
	private static boolean verifSequence(String sequence) {
		Pattern p = Pattern.compile("^(ATG|GTG|TTG)[ATGC]*(TAA|TAG|TGA)$", Pattern.MULTILINE);

		boolean patternOK = p.matcher(sequence).matches();
		boolean multiple3OK = (sequence.length() % 3 == 0);

		return patternOK && multiple3OK;
	}
}
