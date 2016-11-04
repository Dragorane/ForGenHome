package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/*
 * Class ExcelManager
 * Writing / Reading excel files. We're using apache POI library
 */

public class ExcelController {
	String name;
	Workbook wb;
	Sheet sheet;
	Row line;
	Cell cell;

	char[] nucleotides = { 'A', 'C', 'G', 'T' };

	// Opening an existing excel file
	public static ExcelController openingExistingFile(String name, String sheetName) throws IOException, InvalidFormatException {
		ExcelController excel = new ExcelController();
		FileInputStream input = null;
		try {
			input = new FileInputStream(name);
			OPCPackage opc = OPCPackage.open(input);
			excel.wb = WorkbookFactory.create(opc);
			excel.name = name;
			// excel.wb = WorkbookFactory.create(new File(name));
			excel.sheet = excel.wb.getSheet(sheetName);

			return excel;
		} catch (Exception e) {
			System.out.println("Debug openingExistingFile ExcelController : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	// Creating a new excel file by using our starting file : base.xls
	public static ExcelController newExcel(String name, String sheetName) throws IOException, InvalidFormatException {
		ExcelController res = new ExcelController();

		try {
			// System.out.println("Test name newExcel : " + name);
			res.wb = WorkbookFactory.create(new File("base.xlsx"));
			res.sheet = res.wb.getSheet(sheetName);
			res.name = name;
			// res.saving();
			res.saving();

			return res;
		} catch (Exception e) {
			System.out.println("Debug newExcel : " + e.getMessage() + " error : " + e.toString());
			e.printStackTrace();
			return null;
		}
	}

	// Generate all trinucleotides for the next step
	/*
	 * public List<String> generateTriNucleotides() { List<String> res = new
	 * ArrayList<String>();
	 * 
	 * for (int i = 0; i < 4; i++) { for (int j = 0; j < 4; j++) { for(int k =
	 * 0; k < 4; k++) { res.add(Nucleotide.values()[i].toString() +
	 * Nucleotide.values()[j].toString() + Nucleotide.values()[k].toString()); }
	 * } } return res; }
	 */

	// Writing results in the excel file
	public void writingResults(ArrayList<HashMap<String, BigInteger>> results) {
		HashMap<String, BigInteger> Ph0 = results.get(0);
		HashMap<String, BigInteger> Ph1 = results.get(1);
		HashMap<String, BigInteger> Ph2 = results.get(2);

		// HashMap<String, BigInteger> PrefPh0 = results.get(3);
		// HashMap<String, BigInteger> PrefPh1 = results.get(4);
		// HashMap<String, BigInteger> PrefPh2 = results.get(5);

		int ligneCourante = 1;

		@SuppressWarnings("unused")
		BigDecimal valeur, valeur2;
		BigInteger nbTrinucleotide;
		BigDecimal pourcentage = new BigDecimal("0");
		BigDecimal totalPh0 = new BigDecimal("0");
		BigDecimal totalPh1 = new BigDecimal("0");
		BigDecimal totalPh2 = new BigDecimal("0");

		/*
		 * for(String nomTrinucleotide : generateTriNucleotides()) { line =
		 * sheet.getRow(ligneCourante);
		 * 
		 * for (int l = 1; l < 7; l = l+2) { cell = line.getCell(l); valeur =
		 * new BigDecimal(cell.toString()); if (l == 1) //Ph0 { nbTrinucleotide
		 * = Ph0.get(nomTrinucleotide); valeur = valeur.add(new
		 * BigDecimal(nbTrinucleotide));
		 * cell.setCellValue(valeur.doubleValue()); totalPh0 =
		 * totalPh0.add(valeur); } else if (l == 3) //Ph1 { nbTrinucleotide =
		 * Ph1.get(nomTrinucleotide); valeur = valeur.add(new
		 * BigDecimal(nbTrinucleotide));
		 * cell.setCellValue(valeur.doubleValue()); totalPh1 =
		 * totalPh1.add(valeur); } else //Ph2 { nbTrinucleotide =
		 * Ph2.get(nomTrinucleotide); valeur = valeur.add(new
		 * BigDecimal(nbTrinucleotide));
		 * cell.setCellValue(valeur.doubleValue()); totalPh2 =
		 * totalPh2.add(valeur); } }
		 * 
		 * for ( int g = 10 ; g < 13; g++){ //if(line.getCell(g) == null) //cell
		 * = line.createCell(g); //line.getCell(g,
		 * Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); cell =
		 * line.getCell(g,Row.CREATE_NULL_AS_BLANK); if(cell.toString() == "")
		 * cell.setCellValue("0"); valeur2 = new BigDecimal(cell.toString()); if
		 * (g == 10) //Ph0 { nbTrinucleotide = PrefPh0.get(nomTrinucleotide);
		 * valeur2 = valeur2.add(new BigDecimal(nbTrinucleotide));
		 * cell.setCellValue( valeur2.doubleValue());
		 * 
		 * } else if (g == 11) //Ph1 { nbTrinucleotide =
		 * PrefPh1.get(nomTrinucleotide); valeur2 = valeur2.add(new
		 * BigDecimal(nbTrinucleotide)); cell.setCellValue(
		 * valeur2.doubleValue()); } else //Ph2 { nbTrinucleotide =
		 * PrefPh2.get(nomTrinucleotide); valeur2 = valeur2.add(new
		 * BigDecimal(nbTrinucleotide)); cell.setCellValue(
		 * valeur2.doubleValue()); } }
		 * 
		 * ligneCourante++; }
		 */

		// Manages the numbers
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					String trinucleotideName = "" + this.nucleotides[i] + this.nucleotides[j] + this.nucleotides[k];
					line = sheet.getRow(ligneCourante);

					for (int l = 1; l < 7; l = l + 2) {
						cell = line.getCell(l);
						valeur = new BigDecimal(cell.toString());
						if (l == 1) // Ph0
						{
							nbTrinucleotide = Ph0.get(trinucleotideName);
							valeur = valeur.add(new BigDecimal(nbTrinucleotide));
							cell.setCellValue(valeur.doubleValue());
							totalPh0 = totalPh0.add(valeur);
						} else if (l == 3) // Ph1
						{
							nbTrinucleotide = Ph1.get(trinucleotideName);
							valeur = valeur.add(new BigDecimal(nbTrinucleotide));
							cell.setCellValue(valeur.doubleValue());
							totalPh1 = totalPh1.add(valeur);
						} else // Ph2
						{
							nbTrinucleotide = Ph2.get(trinucleotideName);
							valeur = valeur.add(new BigDecimal(nbTrinucleotide));
							cell.setCellValue(valeur.doubleValue());
							totalPh2 = totalPh2.add(valeur);
						}
					}

					// for ( int g = 10 ; g < 13; g++){
					// //if(line.getCell(g) == null)
					// //cell = line.createCell(g);
					// //line.getCell(g,
					// Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					// cell = line.getCell(g,Row.CREATE_NULL_AS_BLANK);
					// if(cell.toString() == "")
					// cell.setCellValue("0");
					// valeur2 = new BigDecimal(cell.toString());
					// if (g == 10) //Ph0
					// {
					// nbTrinucleotide = PrefPh0.get(trinucleotideName);
					// valeur2 = valeur2.add(new BigDecimal(nbTrinucleotide));
					// //cell.setCellValue(valeur2.doubleValue());
					//
					// }
					// else if (g == 11) //Ph1
					// {
					// nbTrinucleotide = PrefPh1.get(trinucleotideName);
					// valeur2 = valeur2.add(new BigDecimal(nbTrinucleotide));
					// // cell.setCellValue(valeur2.doubleValue());
					// }
					// else //Ph2
					// {
					// nbTrinucleotide = PrefPh2.get(trinucleotideName);
					// valeur2 = valeur2.add(new BigDecimal(nbTrinucleotide));
					// //cell.setCellValue(valeur2.doubleValue());
					// }
					// }
					ligneCourante++;
				}
			}
		}

		line = sheet.getRow(ligneCourante);

		line.getCell(1).setCellValue(totalPh0.doubleValue());
		line.getCell(3).setCellValue(totalPh1.doubleValue());
		line.getCell(5).setCellValue(totalPh2.doubleValue());

		// Second step for the percentage
		for (int i = 1; i < 65; i++) {
			line = sheet.getRow(i);

			for (int j = 2; j < 7; j = j + 2) {
				cell = line.getCell(j);
				valeur = new BigDecimal(line.getCell(j - 1).toString());

				if (j == 2 && totalPh0.intValue() != 0) {
					pourcentage = valeur.divide(totalPh0, 10, BigDecimal.ROUND_CEILING);
					pourcentage = pourcentage.multiply(new BigDecimal("100"));

				} else if (j == 4 && totalPh1.intValue() != 0) {
					pourcentage = valeur.divide(totalPh1, 10, BigDecimal.ROUND_CEILING);
					pourcentage = pourcentage.multiply(new BigDecimal("100"));
				} else if (j == 6 && totalPh2.intValue() != 0) {
					pourcentage = valeur.divide(totalPh2, 10, BigDecimal.ROUND_CEILING);
					pourcentage = pourcentage.multiply(new BigDecimal("100"));
				}

				cell.setCellValue(pourcentage.doubleValue());
			}

		}

		line = sheet.getRow(1);
		cell = line.getCell(9);
		cell.setCellValue(totalPh0.doubleValue());
	}

	public void writingResultDinucleotide(ArrayList<HashMap<String, BigInteger>> results) {
		HashMap<String, BigInteger> Ph0Di = results.get(3);
		HashMap<String, BigInteger> Ph1Di = results.get(4);

		int ligneCourante = 1;

		@SuppressWarnings("unused")
		BigDecimal valeur, valeur2;
		BigInteger nbDinucleotide;
		BigDecimal pourcentage = new BigDecimal("0");
		BigDecimal totalPh0Di = new BigDecimal("0");
		BigDecimal totalPh1Di = new BigDecimal("0");

		// Manages the numbers
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				String trinucleotideName = "" + this.nucleotides[i] + this.nucleotides[j];
				line = sheet.getRow(ligneCourante);

				for (int l = 12; l < 15; l = l + 2) {
					cell = line.getCell(l);
					valeur = new BigDecimal(cell.toString());
					if (l == 12) // Ph0
					{
						nbDinucleotide = Ph0Di.get(trinucleotideName);
						valeur = valeur.add(new BigDecimal(nbDinucleotide));
						cell.setCellValue(valeur.doubleValue());
						totalPh0Di = totalPh0Di.add(valeur);
					} else if (l == 14) // Ph1
					{
						nbDinucleotide = Ph1Di.get(trinucleotideName);
						valeur = valeur.add(new BigDecimal(nbDinucleotide));
						cell.setCellValue(valeur.doubleValue());
						totalPh1Di = totalPh1Di.add(valeur);
					}
				}

				ligneCourante++;
			}
		}

		line = sheet.getRow(ligneCourante);

		line.getCell(12).setCellValue(totalPh0Di.doubleValue());
		line.getCell(14).setCellValue(totalPh1Di.doubleValue());

		// Second step for the percentage
		for (int i = 1; i < 17; i++) {
			line = sheet.getRow(i);

			for (int j = 13; j < 16; j = j + 2) {
				cell = line.getCell(j);
				valeur = new BigDecimal(line.getCell(j - 1).toString());

				if (j == 13 && totalPh0Di.intValue() != 0) {
					pourcentage = valeur.divide(totalPh0Di, 10, BigDecimal.ROUND_CEILING);
					pourcentage = pourcentage.multiply(new BigDecimal("100"));

				} else if (j == 15 && totalPh1Di.intValue() != 0) {
					pourcentage = valeur.divide(totalPh1Di, 10, BigDecimal.ROUND_CEILING);
					pourcentage = pourcentage.multiply(new BigDecimal("100"));
				}

				cell.setCellValue(pourcentage.doubleValue());
			}

		}
	}

	public void addingNbSeq(long nbSeq) {
		line = sheet.getRow(0);
		cell = line.getCell(9);
		double totalSeq = Double.parseDouble(cell.toString()) + nbSeq;
		cell.setCellValue(totalSeq);

		// Permet d'ajuster la taille de la cellule en fonction de son contenu
		for (int i = 1; i <= 9; i++) {
			sheet.autoSizeColumn(i);
		}
	}

	public void saving() {
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(this.name);
			this.wb.write(output);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.flush();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void saving2() {
		FileOutputStream output = null;
		FileInputStream input = null;

		try {
			// output = new FileOutputStream(this.name);
			// this.wb.write(output);

			input = new FileInputStream(this.name);
			OPCPackage opc = OPCPackage.open(input);
			this.wb = WorkbookFactory.create(opc);

			File inputFile = new File(this.name);
			output = new FileOutputStream(inputFile);
			this.wb.write(output);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.flush();
				output.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
