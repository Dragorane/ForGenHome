package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import controllers.FileController;
import controllers.StateController;
import ui.Interface;
import utils.DownloadTool;

/*
 * Class GenomeReader
 * Used for getting genomes from ncbi's website
 * We're using jsoup to parse a html page
 */

public class GenomeReader
{
	// Liste des sites ncbi
	public static String LINK_SITE = "https://www.ncbi.nlm.nih.gov";

	public static String LINK_BIOPROJECT = LINK_SITE + "/bioproject/";
	public static String LINK_ASSEMBLY = LINK_SITE + "/assembly/";

	public static String LINK_SEQUENCE = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nuccore&id=";

	public static String LINK_LIST_PROK = LINK_SITE + "/genomes/Genome2BE/genome2srv.cgi?action=download&orgn=&report=proks&status=50|40|30|20|&group=--%20All%20Prokaryotes%20--&subgroup=--%20All%20Prokaryotes%20--";
	public static String LINK_LIST_EUKA = LINK_SITE + "/genomes/Genome2BE/genome2srv.cgi?action=download&orgn=&report=euks&status=50|40|30|20|&group=--%20All%20Eukaryota%20--&subgroup=--%20All%20Eukaryota%20--";
	public static String LINK_LIST_VIRUSES = LINK_SITE + "/genomes/Genome2BE/genome2srv.cgi?action=download&orgn=&report=viruses&status=50|40|30|20|&host=All&group=--%20All%20Viruses%20--&subgroup=--%20All%20Viruses%20--";

	static Interface ihm_log = null;

	// Liste des fichiers e telecharger
	//static ArrayList<String> files = MainLauncher.fichiersBioinfo;

	/*
	 * Public functions
	 */

	//Getting different files on the ncbi's website
	public void getFiles(Interface i, String file)
	{
		ihm_log = i;
		try
		{
			File tmpFile = new File(file);
			//System.out.println("Test File : " + tmpFile.toString());
			
			if (!tmpFile.exists())
			{
				ihm_log.addLog("--- Telechargement de " + tmpFile.getName() + " ---");
				ihm_log.progress_bar.setValue(ihm_log.progress_bar.getValue()+1);
				// Verification du nom de fichier pour telecharger la bonne liste
				if(tmpFile.getName().equals("prokaryotes.txt"))
				{
					//System.out.println("Test prokaryotes");
					DownloadTool.getFile(LINK_LIST_PROK, "prokaryotes.txt");
				}
				else if(tmpFile.getName().equals("eukaryotes.txt"))
				{
					DownloadTool.getFile(LINK_LIST_EUKA, "eukaryotes.txt");
				}
				else if(tmpFile.getName().equals("viruses.txt"))
				{
					DownloadTool.getFile(LINK_LIST_VIRUSES, "viruses.txt");
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	// Getting a genome (from the file we downloaded)
	public Genome get(StateController etat) throws FileNotFoundException
	{
		ihm_log.progress_bar.setValue(ihm_log.progress_bar.getValue()+1);
		ihm_log.progress_bar.setString(ihm_log.progress_bar.getValue()+ " / "+ihm_log.progress_bar.getMaximum());
		Genome genome;

		//System.out.println("Test etat getNomFichier : " + etat.getNomFichier());
		File fichier = new File(etat.getNomFichier()); 
		Scanner sc = new Scanner(fichier);
		String ligneCourante = "";

		for (int i = 0; i <= etat.getLineNumber(); i++)
		{
			if (!sc.hasNext())
			{
				sc.close();
				return null;
			}
			ligneCourante = sc.nextLine();
		}

		String[] tabLigne = ligneCourante.split("\t");

		String kingdom = etat.getNomFichier().substring(0, 1).toUpperCase() + etat.getNomFichier().substring(1, etat.getNomFichier().length()-4);
		
		String group = "";
		String subgroup = "";
		String name = tabLigne[0];
		String bioproject = "";
		
		if(kingdom.equals("Eukaryotes")){
			 group = tabLigne[4];
			 subgroup = tabLigne[5];
			 bioproject = tabLigne[3];	
		} else if(kingdom.equals("Prokaryotes")){
			 group = tabLigne[5];
			 subgroup = tabLigne[6];
			 bioproject = tabLigne[4];	
		} else if(kingdom.equals("Viruses")){
			 group = tabLigne[2];
			 subgroup = tabLigne[3];
			 bioproject = tabLigne[1];	
		}
		

		// Suppression des caracteres interdits par un espace
		String[] toRemove = {"*" ,"?" ,"<" ,">" ,":" ,"/" ,"\\", "\"" };
		for(String c : toRemove)
		{
			if(name.contains(c))
			{
				name = name.replace(c, " ").trim();
			}
		}

		genome = new Genome(kingdom,group,subgroup,name,bioproject);

		// For each type, we have a different parser because they're not written the same way

		// Prokaryotes
		if(genome.getKingdom().equals("Prokaryotes"))
		{
			if(!tabLigne[17].equals("Contig") && !tabLigne[17].equals("Scaffold"))
			{
				genome.setUpdateDate(tabLigne[16]);

				if(!isUptoDate(genome))
				{
					for ( String refseqTab : tabLigne[10].split(";"))
					{
						if(refseqTab.contains(":"))
						{
							String type = "";

							if(!refseqTab.equals("-") && refseqTab.startsWith("chrom")){
								type = "chrom";
							}
							else if (!refseqTab.equals("-")){
								type = "plasm";
							}

							String[] res = {refseqTab.split(":")[1].split("/")[0], type};

							genome.getRefseq().add(res);
						}
					}
					ihm_log.progress_bar.setValue(ihm_log.progress_bar.getValue()+1);
					ihm_log.progress_bar.setString(ihm_log.progress_bar.getValue()+ " / "+ihm_log.progress_bar.getMaximum());

				}
				else
				{
					ihm_log.addLog("[ " + genome.getName() + " ] deja a jour!");
					ihm_log.progress_bar.setValue(ihm_log.progress_bar.getValue()+1);
					ihm_log.progress_bar.setString(ihm_log.progress_bar.getValue()+ " / "+ihm_log.progress_bar.getMaximum());

					ihm_log.addLog("");
					//System.out.println(ihm_log.progress_bar.getValue());
				}
			}
			else
			{
				//ihm_log.addLog(name + " non complet => va etre ignore");
			}
		}

		// Eukaryotes
		else if(genome.getKingdom().equals("Eukaryotes"))
		{
			ihm_log.progress_bar.setValue(ihm_log.progress_bar.getValue()+1);
			ihm_log.progress_bar.setString(ihm_log.progress_bar.getValue()+ " / "+ihm_log.progress_bar.getMaximum());

			genome.setUpdateDate(tabLigne[15]);
			if(!tabLigne[14].equals("Contig") && !tabLigne[14].equals("Scaffold") && !isUptoDate(genome))
			{
				recupererGCF(genome, 1);
				if(genome.getGcf()!=null)
				{
					recupererRefSeqEuka(genome, 1);
				}
			}
			else
			{
				ihm_log.addLog("[ " + genome.getName() + " ] deja a jour!");
				ihm_log.progress_bar.setValue(ihm_log.progress_bar.getValue()+1);
				ihm_log.progress_bar.setString(ihm_log.progress_bar.getValue()+ " / "+ihm_log.progress_bar.getMaximum());
				ihm_log.addLog("");
			}
		}

		// Viruses
		else if(!tabLigne[12].equals("Contig") && !tabLigne[12].equals("Scaffold") && genome.getKingdom().equals("Viruses"))
		{
			genome.setUpdateDate(tabLigne[11]);
			if(!isUptoDate(genome))
			{
				recupererRefSeqVir(genome, 1);
				ihm_log.progress_bar.setValue(ihm_log.progress_bar.getValue()+1);
				ihm_log.progress_bar.setString(ihm_log.progress_bar.getValue()+ " / "+ihm_log.progress_bar.getMaximum());

				
			}
			else
			{
				ihm_log.addLog("[ " + genome.getName() + " ] deja a jour!");
				ihm_log.addLog("");
				ihm_log.progress_bar.setValue(ihm_log.progress_bar.getValue()+1);
				ihm_log.progress_bar.setString(ihm_log.progress_bar.getValue()+ " / "+ihm_log.progress_bar.getMaximum());

			}
		}
		fichier = null;
		sc.close();

		return genome;
	}

	private boolean isUptoDate(Genome genome) {
		String path = genome.getChemin();
		File f = new File(path+"\\updateDate.txt");

		if(f.exists() && !f.isDirectory())
		{
			try {
				BufferedReader in = new BufferedReader(new FileReader(path+"\\updateDate.txt"));
				String line;
				String val = "";
				while ((line = in.readLine()) != null)
				{
					val = line;
				}
				in.close();

				if(val.compareTo(genome.getUpdateDate()) == 0)
				{
					return true;
				}
				else
				{
					if(val.length() > 4 && genome.getUpdateDate().length() > 4)
					{
						if(Integer.parseInt(val.substring(0, 4)) < Integer.parseInt(genome.getUpdateDate().substring(0, 4)))
						{
							return true;
						}
						else
						{
							FileController.createUpdateFile(path+"\\updateDate.txt", genome);
							return false;
						}
					}
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return false;
	}

	//Getting sequence associated to refseq
	public ArrayList<ArrayList<String>> getSequence(Genome genome, String refseq, boolean sauvegardeSequence)
	{
		ArrayList<ArrayList<String>> sequence_info = new ArrayList<ArrayList<String>>();
		ArrayList<String> infos = new ArrayList<String>();

		// ArrayList which keep the sequence
		ArrayList<String> sequence = new ArrayList<String>();

		try
		{
			ihm_log.addLog("Telechargement de la sequence : " + refseq + " ");

			DownloadTool.getFile(LINK_SEQUENCE + refseq + "&rettype=fasta_cds_na", "temp");

			FileReader f = new FileReader("temp");
			BufferedReader br = new BufferedReader(f);

			String ligneCourante;
			StringBuilder blocSequence = new StringBuilder();


			while ((ligneCourante = br.readLine()) != null)
			{
				//Saving the sequence if it's checked in options
				if (sauvegardeSequence)
				{
					FileController.sauvegarderSequence(genome, refseq, ligneCourante);
				}

				//If the line starts with >, it means it's a new sequence
				if (ligneCourante.startsWith(">"))
				{
					infos.add(ligneCourante);

					sequence.add(blocSequence.toString());
					blocSequence = new StringBuilder();
				}
				else
				{
					blocSequence.append(ligneCourante);
				}
			}

			sequence.add(blocSequence.toString());
			br.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		// If we want to modify a sequence
		//sequence= new ArrayList<String>(); sequence.add("TAAAAAATG");

		sequence_info.add(sequence);
		sequence_info.add(infos);
		return sequence_info;
	}

	/*
	 * Private functions
	 */

	//Parsing bioproject's page to get the gcf (eukariotes)
	private void recupererGCF(Genome genome, int nbRelance)
	{
		try
		{
			if(genome!=null && genome.getBioproject()!=null) {
				Connection co = Jsoup.connect(LINK_BIOPROJECT + genome.getBioproject()).timeout(300000);
				Document doc = co.get();
				Elements elements = doc.getElementsByAttributeValue("title","Genome assembly info");
				Element content = elements.first();
				if(content!=null)
				{
					String gcf =content.text();
					genome.setGcf(gcf);
				}
			}
		}

		catch (IOException e)
		{
			System.out.println("Erreur recupererGCF : " + e.toString());
			e.printStackTrace();
			try {
        		ihm_log.addLog("Erreur de connexion");
        		if(nbRelance <= 3) {
        			ihm_log.addLog("Tentative de relance dans 5 secondes, relance n�" + nbRelance);
        			Thread.sleep(5000);
        			nbRelance+=1;
					recupererGCF(genome, 1);
        		}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	//Parsing assembly's page to get refseqs
	private void recupererRefSeqEuka(Genome genome, int nbRelance)
	{
		try
		{
			if(genome!=null && genome.getGcf()!=null) {
				Connection co = Jsoup.connect(LINK_ASSEMBLY + genome.getGcf()).timeout(300000);
				Document doc = co.get();
	
				Element asm = doc.getElementById("asm_Primary_Assembly");
				Elements tr;
				if(asm!=null)
				{
					tr = asm.getElementsByTag("tr");
					for (Element e : tr)
					{
						Elements td = e.getElementsByTag("td");
						if(!td.isEmpty())
						{
							if(td.get(0).text().contains("Chromosome"))
							{
								String refseq = td.get(3).text();
								if(!refseq.equals("n/a"))
								{
									String[] res = {refseq, "chrom"};
									genome.getRefseq().add(res);
								}
							}
						}
					}
				}
	
				asm = doc.getElementById("asm_non-nuclear");
				if(asm!=null)
				{
					tr = asm.getElementsByTag("tr");
					for (Element e : tr)
					{
						Elements td = e.getElementsByTag("td");
						if(!td.isEmpty())
						{
							if(td.get(0).text().contains("Mitochondrion"))
							{
								String refseq = td.get(3).text();
								if(!refseq.equals("n/a") )
								{
									String[] res = {refseq, "mito"};
									genome.getRefseq().add(res);
								}
							}
							else if(td.get(0).text().contains("Chloroplast"))
							{
								String refseq = td.get(3).text();
								if(!refseq.equals("n/a"))
								{
									String[] res = {refseq, "chloro"};
									genome.getRefseq().add(res);
								}
							}
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			System.out.println("Erreur dans recupererRefSeqEuka : " + ex.toString());
			ex.printStackTrace();
			try {
        		ihm_log.addLog("Erreur de connexion");
        		if(nbRelance <= 3) {
        			ihm_log.addLog("Tentative de relance dans 5 secondes, relance n�" + nbRelance);
        			Thread.sleep(5000);
        			nbRelance+=1;
        			recupererRefSeqEuka(genome, nbRelance);
        		}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	//Parsing bioproject's page of viruses to get refseqs
	 private void recupererRefSeqVir(Genome genome, int nbRelance)
     {
         System.out.println(genome.getBioproject());
         try
         {
        	 if(genome!=null && genome.getBioproject()!=null) {
	             Document doc = Jsoup.connect("http://www.ncbi.nlm.nih.gov/bioproject/" + genome.getBioproject()).timeout(300000).get();
	             Elements elements = doc.getElementsByTag("a");
	             if(elements!=null)
	             {
	                 for (Element e : elements)
	                 {
	                   String linkHref = e.attr("href");
	
	                   if(linkHref.startsWith("/nuccore/NC"))
	                   {
	                       String refseq = e.text();
	
	                       String[] res = {refseq, "chrom"};
	                       genome.getRefseq().add(res);
	                   }
	                   else if(linkHref.startsWith("/nuccore/"))
	                   {
	                	   try {
		                       System.out.println(linkHref);
		                       Document doc2 = Jsoup.connect("http://www.ncbi.nlm.nih.gov" + linkHref).timeout(300000).get();
		
		                       // If we only need to download one file
		                       if(e.text().equals("1"))
		                       {
		                           Elements elements3 = doc2.getElementsByClass("itemid");
		
		                           if(elements3 != null)
		                           {
		                               for(Element e3 : elements3)
		                               {
		                                   String tmpString3 = e3.text();
		
		                                   if(tmpString3.startsWith("NCBI Reference Sequence"))
		                                   {
		                                       String[] res3 = tmpString3.split(": ");
		                                       String refseq = res3[res3.length - 1];
		                                       System.out.println("refseq : " + refseq);
		                                       String[] res = {refseq, "chrom"};
		                                       genome.getRefseq().add(res);
		                                       //ihm_log.progress_bar.setValue(ihm_log.progress_bar.getValue()+1);
		                                   }
		                               }
		                           }
		                       }
		                       // We have to download more than one file
		                       else {
		                           Elements elements2 = doc2.getElementsByTag("a");
		
		                           if(elements2 != null)
		                           {
		                               for(Element e2 : elements2)
		                               {
		                                   String linkHref2 = e2.attr("href");
		
		                                   if(linkHref2.startsWith("/nuccore/NC"))
		                                   {
		                                       String tmpString = e2.attr("href");
		                                       String refseq = tmpString.split("/")[tmpString.split("/").length - 1];
		                                       System.out.println("refseq : " + refseq);
		                                       String[] res = {refseq, "chrom"};
		                                       //ihm_log.progress_bar.setValue(ihm_log.progress_bar.getValue()+1);
		                                       genome.getRefseq().add(res);
		                                   }
		                               }
		                           }
		                       }
	                	   } catch (Exception ex2) {
	                		   System.out.println("Erreur de connexion boucle recupererRefSeqVir : " + ex2.toString());
	                		   ex2.printStackTrace();
	                	   }
	                   }
	                }
	             }
        	 }
         }
         catch (Exception ex)
         {
        	 System.out.println("Erreur recupererRefSeqVir : " + ex.toString());
        	 ex.printStackTrace();
        	 try {
         		ihm_log.addLog("Erreur de connexion");
         		if(nbRelance <= 3) {
         			ihm_log.addLog("Tentative de relance dans 5 secondes, relance n�" + nbRelance);
        			Thread.sleep(5000);
        			nbRelance+=1;
        			recupererRefSeqEuka(genome, nbRelance);
         		} 				
 			} catch (InterruptedException e1) {
 				e1.printStackTrace();
 			}
         }
     }
}
