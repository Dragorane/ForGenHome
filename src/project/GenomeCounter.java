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
public class GenomeCounter
{
	public static boolean stop = false;
	private static Interface ihm_log = null;

	/*
	 * Public Functions
	 */
	
	// Demarre le traitement
	public static void demarrerTraitement(Interface ihm) 
	{
		if(ihm != null)
		{
			ihm_log = ihm;
			boolean optionInfo = ihm.cds.isSelected();
			boolean optionSequence = ihm.sequence.isSelected();
			
			StateController etat = StateController.checkState();
			
			ihm_log.addLog("--- Demarrage du traitement ---");
		
			GenomeReader getGenome = new GenomeReader();
			ArrayList<ArrayList<String>> sequence_info = new ArrayList<ArrayList<String>>();
			
			//Pour chaque type, on a la liste des trinucletides avec leur compteur associ�
			ArrayList<HashMap<String, BigInteger>> resultatsChrom = new ArrayList<HashMap<String, BigInteger>>();
			ArrayList<HashMap<String, BigInteger>> resultatsChloro = new ArrayList<HashMap<String, BigInteger>>();
			ArrayList<HashMap<String, BigInteger>> resultatsPlasm = new ArrayList<HashMap<String, BigInteger>>();
			ArrayList<HashMap<String, BigInteger>> resultatsMito = new ArrayList<HashMap<String, BigInteger>>();


			getGenome.getFiles(ihm_log);

			while (!etat.traitementTermine() && !stop) 
			{
				try 
				{
					Genome genome=null;
					resultatsChrom = null;
					resultatsChloro = null;
					resultatsPlasm = null;
					resultatsMito = null;
					sequence_info = null;

					// On recupere un des genomes deja telecharger
					genome = getGenome.get(etat);
					
					if (genome == null) 
					{
						etat.increFile();
						FileController.enregistrer(etat);
						continue;
					}
								
					// Si le genome n'existe pas, ou s'il existe mais que le traitement a ete arete au milieu
					if ( !genome.exists() && genome.getRefseq().size() != 0)
					{
						ihm.addLog("\n--- Nouvel organisme : " + genome + "---");
						ihm.addLog("Contenu dans " + etat.getNomFichier() + " [ " + (etat.getLineNumber()+1) + " ]");
						while (!etat.toutRecuperer(genome.getRefseq().size()))
						{
							// On recupere la refseq et son type
							String[] ref_type = genome.getRefseq().get(etat.getRefSeqNumber());
							String refseq=ref_type[0];
							String type=ref_type[1];						
							
							// On recupere la sequence et les infos (genes) associes 
							sequence_info = getGenome.getSequence(genome, refseq, optionSequence);
							
							// On sauvegarder les infos (option)
							if (optionInfo)
							{
								FileController.sauvegarderInfos(genome, sequence_info.get(1));
							}
							
							// Pour les sequences recuperees, on ajoute les resultats en fonction du type
							for (String sequence : sequence_info.get(0)) 
							{
								if(type.equals("chrom"))
								{
									resultatsChrom = ajoutResultats(genome, sequence, type, resultatsChrom);
								}
								else if (type.equals("chloro"))
								{
									resultatsChloro = ajoutResultats(genome, sequence, type, resultatsChloro);
								}
								else if (type.equals("plasm"))
								{
									resultatsPlasm = ajoutResultats(genome, sequence, type, resultatsPlasm);
								}
								else if (type.equals("mito"))
								{
									resultatsMito = ajoutResultats(genome, sequence, type, resultatsMito);	
								}
							}
							
							etat.increRefseq();;
							FileController.enregistrer(etat);

						}
						
						// Selon le type, on enregistre les resultats 
						if(resultatsChrom!=null)
						{
							FileController.savingResults(genome, "chrom", resultatsChrom);
						}
						if(resultatsChloro!=null)
						{
							FileController.savingResults(genome, "chloro", resultatsChloro);
						}
						if(resultatsPlasm!=null)
						{
							FileController.savingResults(genome, "plasm", resultatsPlasm);
						}
						if(resultatsMito!=null)
						{
							FileController.savingResults(genome, "mito", resultatsMito);
						}
					}
					etat.increLine();
					FileController.enregistrer(etat);
				} 
				catch (Exception ex) 
				{
					ex.printStackTrace();
				}
			}
			ihm.addLog("**** Recuperation terminee ****");
		}	
	}
	
	// Ajoute les resultats a la HashMap de resultat
	public static ArrayList<HashMap<String,BigInteger>> ajoutResultats(Genome genome, String sequence, String type, ArrayList<HashMap<String,BigInteger>> ancienneMap) 
	{
		HashMap<String,BigInteger> Ph0;
		HashMap<String,BigInteger> Ph1;
		HashMap<String,BigInteger> Ph2;
		
		HashMap<String,BigInteger> PrefPh0;
		HashMap<String,BigInteger> PrefPh1;
		HashMap<String,BigInteger> PrefPh2;

		HashMap<String,BigInteger> TempPh0;
		HashMap<String,BigInteger> TempPh1;
		HashMap<String,BigInteger> TempPh2;

		ArrayList<HashMap<String,BigInteger>> mapResultat = new ArrayList<HashMap<String,BigInteger>>();
		
		TempPh0 = genererMap();
		TempPh1 = genererMap();
		TempPh2 = genererMap();
		
		if (ancienneMap == null) 
		{
			Ph0 = genererMap();
			Ph1 = genererMap();
			Ph2 = genererMap();
			PrefPh0 = genererMap();
            PrefPh1 = genererMap();
            PrefPh2 = genererMap();
		} 
		else 
		{
			Ph0 = ancienneMap.get(0);
			Ph1 = ancienneMap.get(1);
			Ph2 = ancienneMap.get(2);
			PrefPh0 = ancienneMap.get(3);
            PrefPh1 = ancienneMap.get(4);
            PrefPh2 = ancienneMap.get(5);
		}
		
		BigInteger nbTrinucleotide;
		BigInteger prefTrinucleotide;
		String trinucleotide;
		
		if (verifSequence(sequence)) 
		{
			if(type.equals("chrom"))
			{
				genome.setNbSeqChrom(genome.getNbSeqChrom()+1);
			}
			else if(type.equals("chloro"))
			{
				genome.setNbSeqChloro(genome.getNbSeqChloro()+1);
			}
			else if(type.equals("plasm"))
			{
				genome.setNbSeqPlasm(genome.getNbSeqPlasm()+1);
			}
			else if(type.equals("mito"))
			{
				genome.setNbSeqMito(genome.getNbSeqMito()+1);
			}
			
			int ph = 0;
		
			for (int i = 0; i < sequence.length() - 3; i++) 
			{	
				trinucleotide = null;
				trinucleotide = sequence.charAt(i) + ""+ sequence.charAt(i + 1) + "" + sequence.charAt(i + 2);
				
				if (ph == 0) 
				{
					nbTrinucleotide = Ph0.get(trinucleotide);
					Ph0.put(trinucleotide, nbTrinucleotide.add(BigInteger.ONE));
					
					prefTrinucleotide = TempPh0.get(trinucleotide);
					TempPh0.put(trinucleotide,prefTrinucleotide.add(BigInteger.ONE));

					ph++;

				} 
				else if (ph == 1) 
				{
					nbTrinucleotide = Ph1.get(trinucleotide);
					Ph1.put(trinucleotide, nbTrinucleotide.add(BigInteger.ONE));
					
					prefTrinucleotide = TempPh1.get(trinucleotide);
					TempPh1.put(trinucleotide,prefTrinucleotide.add(BigInteger.ONE));

					ph++;
				} 
				else if (ph == 2) 
				{
					nbTrinucleotide = Ph2.get(trinucleotide);
					Ph2.put(trinucleotide, nbTrinucleotide.add(BigInteger.ONE));
					
					prefTrinucleotide = TempPh2.get(trinucleotide);
					TempPh2.put(trinucleotide,prefTrinucleotide.add(BigInteger.ONE));

					ph=0;	
				}

				nbTrinucleotide = null;
				trinucleotide = null;
			}
			
			for(@SuppressWarnings("rawtypes") Map.Entry mapentry : TempPh0.entrySet()) {
				ArrayList<Integer> maxtab = maxPh(TempPh0.get(mapentry.getKey()), TempPh1.get(mapentry.getKey()), TempPh2.get(mapentry.getKey()));
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
		}
		
		mapResultat.add(Ph0);
		mapResultat.add(Ph1);
		mapResultat.add(Ph2);
		
		mapResultat.add(PrefPh0);
		mapResultat.add(PrefPh1);
		mapResultat.add(PrefPh2);
		
		return mapResultat;
	}
	
	/*
	 * Private Functions
	 */
	
	
	private static ArrayList<Integer> maxPh(BigInteger ph0, BigInteger ph1, BigInteger ph2) {
		ArrayList<Integer> res = new ArrayList<Integer>();
        if(ph0.intValue() == 0 && ph1.intValue() == 0 && ph2.intValue() == 0){
                return res;
        }

		  int phs [] = {ph0.intValue(), ph1.intValue(), ph2.intValue()};
		
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
	private static HashMap<String,BigInteger> genererMap() 
	{
		HashMap<String,BigInteger> res = new HashMap<String,BigInteger>();
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 4; j++) 
			{
				for (int k = 0; k < 4; k++) 
				{
					String trinucleotide = ""+ Nucleotide.values()[i] + Nucleotide.values()[j] + Nucleotide.values()[k];
					res.put(trinucleotide, new BigInteger("0"));
				}
			}
		}
		return res;
	}

	// Verifie si la sequence est correcte
	private static boolean verifSequence(String sequence) 
	{
		Pattern p = Pattern.compile("^(ATG|GTG|TTG)[ATGC]*(TAA|TAG|TGA)$", Pattern.MULTILINE);
		
		boolean patternOK = p.matcher(sequence).matches();
		boolean multiple3OK = (sequence.length() % 3 == 0);	
		
		return patternOK && multiple3OK;
	}
}
