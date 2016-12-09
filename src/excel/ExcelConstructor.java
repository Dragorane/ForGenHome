package excel;

import java.awt.Color;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;

import enums.Nucleotide;

public class ExcelConstructor {

	private Workbook wb;

	public ExcelConstructor(String sheetName, String docName) {
		try {
			/* Start with Creating a workbook and worksheet object */
			this.wb = new XSSFWorkbook();
			this.createNewSheet(sheetName);

			/* Write output as File */
			FileOutputStream fileOut;

			fileOut = new FileOutputStream(docName);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ExcelConstructor(Workbook excel) {
		this.wb = excel;
	}

	public void createNewSheet(String sheetName) {
		XSSFSheet newSheet = (XSSFSheet) this.wb.createSheet(sheetName);

		this.stylizedSheet(newSheet);
	}

	private void stylizedSheet(XSSFSheet mySheet) {
		this.createNewTableTrinucleotide(mySheet);
		this.createNewTableDinucleotide(mySheet);
		this.createInformationTable(mySheet);

		// Permet d'ajuster la taille de la cellule en fonction de son contenu
		for (int i = 0; i <= 15; i++) {
			mySheet.autoSizeColumn(i);
			if (mySheet.getColumnWidth(i) < 256 * 16) {
				mySheet.setColumnWidth(i, 256 * 16);
			}
		}
	}

	private void createInformationTable(XSSFSheet mySheet) {
		// Font
		XSSFFont fontTitle = (XSSFFont) this.wb.createFont();
		fontTitle.setBold(true);
		fontTitle.setColor(new XSSFColor(Color.WHITE));
		
		XSSFFont fontBold = (XSSFFont) this.wb.createFont();
		fontBold.setBold(true);

		// Style
		XSSFCellStyle styleDefaultCell = (XSSFCellStyle) this.wb.createCellStyle();
		styleDefaultCell.setAlignment(HorizontalAlignment.CENTER);
		styleDefaultCell.setBorderBottom(BorderStyle.THIN);
		styleDefaultCell.setBottomBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleDefaultCell.setBorderLeft(BorderStyle.THIN);
		styleDefaultCell.setLeftBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleDefaultCell.setBorderRight(BorderStyle.THIN);
		styleDefaultCell.setRightBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleDefaultCell.setBorderTop(BorderStyle.THIN);
		styleDefaultCell.setTopBorderColor(new XSSFColor(new Color(149, 179, 215)));
		
		//NumberStyleCell 1
		XSSFCellStyle styleNumberFormatCell = (XSSFCellStyle) this.wb.createCellStyle();
		styleNumberFormatCell.setAlignment(HorizontalAlignment.CENTER);
		styleNumberFormatCell.setDataFormat(this.wb.createDataFormat().getFormat("# ##0"));
		styleNumberFormatCell.setBorderBottom(BorderStyle.THIN);
		styleNumberFormatCell.setBottomBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleNumberFormatCell.setBorderLeft(BorderStyle.THIN);
		styleNumberFormatCell.setLeftBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleNumberFormatCell.setBorderRight(BorderStyle.THIN);
		styleNumberFormatCell.setRightBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleNumberFormatCell.setBorderTop(BorderStyle.THIN);
		styleNumberFormatCell.setTopBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleNumberFormatCell.setFont(fontBold);
		//NumberStyleCell2
		XSSFCellStyle styleNumberFormatCell2 = (XSSFCellStyle) this.wb.createCellStyle();
		styleNumberFormatCell2.setAlignment(HorizontalAlignment.CENTER);
		styleNumberFormatCell2.setDataFormat(this.wb.createDataFormat().getFormat("# ##0"));
		styleNumberFormatCell2.setFillForegroundColor(new XSSFColor(new Color(220, 230, 241)));
		styleNumberFormatCell2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleNumberFormatCell2.setBorderBottom(BorderStyle.THIN);
		styleNumberFormatCell2.setBottomBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleNumberFormatCell2.setBorderLeft(BorderStyle.THIN);
		styleNumberFormatCell2.setLeftBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleNumberFormatCell2.setBorderRight(BorderStyle.THIN);
		styleNumberFormatCell2.setRightBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleNumberFormatCell2.setBorderTop(BorderStyle.THIN);
		styleNumberFormatCell2.setTopBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleNumberFormatCell2.setFont(fontBold);

		XSSFCellStyle styleTitleCell = (XSSFCellStyle) this.wb.createCellStyle();
		styleTitleCell.setAlignment(HorizontalAlignment.CENTER);
		styleTitleCell.setFillForegroundColor(new XSSFColor(new Color(79, 129, 189)));
		styleTitleCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleTitleCell.setBorderBottom(BorderStyle.THIN);
		styleTitleCell.setBottomBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleTitleCell.setBorderLeft(BorderStyle.THIN);
		styleTitleCell.setLeftBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleTitleCell.setBorderRight(BorderStyle.THIN);
		styleTitleCell.setRightBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleTitleCell.setBorderTop(BorderStyle.THIN);
		styleTitleCell.setTopBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleTitleCell.setFont(fontTitle);

		XSSFCellStyle styleInformationCell = (XSSFCellStyle) this.wb.createCellStyle();
		styleInformationCell.setAlignment(HorizontalAlignment.CENTER);
		styleInformationCell.setFillForegroundColor(new XSSFColor(new Color(220, 230, 241)));
		styleInformationCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleInformationCell.setBorderBottom(BorderStyle.THIN);
		styleInformationCell.setBottomBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleInformationCell.setBorderLeft(BorderStyle.THIN);
		styleInformationCell.setLeftBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleInformationCell.setBorderRight(BorderStyle.THIN);
		styleInformationCell.setRightBorderColor(new XSSFColor(new Color(149, 179, 215)));
		styleInformationCell.setBorderTop(BorderStyle.THIN);
		styleInformationCell.setTopBorderColor(new XSSFColor(new Color(149, 179, 215)));		

		List<String> headerInfo = generateHeaderTableInformation();
		for (int q = 0; q < 6; q++) {
			CellRangeAddress cda = new CellRangeAddress(q, q, 3, 5);
			mySheet.addMergedRegion(cda);
			mySheet.createRow(q).createCell(3);

			if (q % 2 == 0) {
				mySheet.getRow(q).getCell(3).setCellStyle(styleInformationCell);
				mySheet.getRow(q).createCell(4).setCellStyle(styleInformationCell);
				mySheet.getRow(q).createCell(5).setCellStyle(styleInformationCell);
			} else {
				mySheet.getRow(q).getCell(3).setCellStyle(styleDefaultCell);
				mySheet.getRow(q).createCell(4).setCellStyle(styleDefaultCell);
				mySheet.getRow(q).createCell(5).setCellStyle(styleDefaultCell);
			}
			
			if (q >= 2 && q <= 4) {
				if(q % 2 == 0) {
					mySheet.getRow(q).getCell(3).setCellStyle(styleNumberFormatCell2);
					mySheet.getRow(q).createCell(4).setCellStyle(styleNumberFormatCell2);
					mySheet.getRow(q).createCell(5).setCellStyle(styleNumberFormatCell2);
				} else {
					mySheet.getRow(q).getCell(3).setCellStyle(styleNumberFormatCell);
					mySheet.getRow(q).createCell(4).setCellStyle(styleNumberFormatCell);
					mySheet.getRow(q).createCell(5).setCellStyle(styleNumberFormatCell);
				}
				mySheet.getRow(q).getCell(3).setCellValue(0);
			} else {
				mySheet.getRow(q).getCell(3).setCellValue("");
			}

			RegionUtil.setBorderBottom(mySheet.getRow(q).getCell(3).getCellStyle().getBorderBottomEnum(), cda,
					(Sheet) mySheet);
			RegionUtil.setBorderTop(mySheet.getRow(q).getCell(3).getCellStyle().getBorderTopEnum(), cda, mySheet);
			RegionUtil.setBorderLeft(mySheet.getRow(q).getCell(3).getCellStyle().getBorderLeftEnum(), cda, mySheet);
			RegionUtil.setBorderRight(mySheet.getRow(q).getCell(3).getCellStyle().getBorderRightEnum(), cda, mySheet);

			RegionUtil.setBottomBorderColor(mySheet.getRow(q).getCell(3).getCellStyle().getBottomBorderColor(), cda,
					mySheet);
			RegionUtil.setTopBorderColor(mySheet.getRow(q).getCell(3).getCellStyle().getTopBorderColor(), cda, mySheet);
			RegionUtil.setLeftBorderColor(mySheet.getRow(q).getCell(3).getCellStyle().getLeftBorderColor(), cda,
					mySheet);
			RegionUtil.setRightBorderColor(mySheet.getRow(q).getCell(3).getCellStyle().getRightBorderColor(), cda,
					mySheet);
		}
		for (int i = 0; i < 6; i++) {
			CellRangeAddress cda = new CellRangeAddress(i, i, 0, 2);
			mySheet.addMergedRegion(cda);
			mySheet.getRow(i).createCell(0);
			mySheet.getRow(i).getCell(0).setCellValue(headerInfo.get(0));
			headerInfo.remove(0);
			mySheet.getRow(i).getCell(0).setCellStyle(styleTitleCell);
			mySheet.getRow(i).createCell(1).setCellStyle(styleInformationCell);
			mySheet.getRow(i).createCell(2).setCellStyle(styleInformationCell);

			RegionUtil.setBorderBottom(mySheet.getRow(i).getCell(0).getCellStyle().getBorderBottomEnum(), cda,
					(Sheet) mySheet);
			RegionUtil.setBorderTop(mySheet.getRow(i).getCell(0).getCellStyle().getBorderTopEnum(), cda, mySheet);
			RegionUtil.setBorderLeft(mySheet.getRow(i).getCell(0).getCellStyle().getBorderLeftEnum(), cda, mySheet);
			RegionUtil.setBorderRight(mySheet.getRow(i).getCell(0).getCellStyle().getBorderRightEnum(), cda, mySheet);

			RegionUtil.setBottomBorderColor(mySheet.getRow(i).getCell(0).getCellStyle().getBottomBorderColor(), cda,
					mySheet);
			RegionUtil.setTopBorderColor(mySheet.getRow(i).getCell(0).getCellStyle().getTopBorderColor(), cda, mySheet);
			RegionUtil.setLeftBorderColor(mySheet.getRow(i).getCell(0).getCellStyle().getLeftBorderColor(), cda,
					mySheet);
			RegionUtil.setRightBorderColor(mySheet.getRow(i).getCell(0).getCellStyle().getRightBorderColor(), cda,
					mySheet);
		}
		
		CellRangeAddress cda = new CellRangeAddress(1,1,7,8);
		mySheet.addMergedRegion(cda);
		for(int j = 1; j < 4; j++) {
			mySheet.getRow(j).createCell(7);
			mySheet.getRow(j).createCell(8);
			switch(j) {
				case 1:
					mySheet.getRow(j).getCell(7).setCellValue("Trinucleotides");
					mySheet.getRow(j).getCell(7).setCellStyle(styleTitleCell);
					mySheet.getRow(j).getCell(8).setCellStyle(styleTitleCell);
					break;
				case 2: 
					mySheet.getRow(j).getCell(7).setCellValue("Nb Sequences");
					mySheet.getRow(j).getCell(7).setCellStyle(styleNumberFormatCell2);
					mySheet.getRow(j).getCell(8).setCellValue(0);
					mySheet.getRow(j).getCell(8).setCellStyle(styleNumberFormatCell2);
					break;
				case 3: 
					mySheet.getRow(j).getCell(7).setCellValue("Nb Bases");
					mySheet.getRow(j).getCell(7).setCellStyle(styleNumberFormatCell);
					mySheet.getRow(j).getCell(8).setCellValue(0);
					mySheet.getRow(j).getCell(8).setCellStyle(styleNumberFormatCell);
					break;
			}
		}
		RegionUtil.setBorderBottom(mySheet.getRow(1).getCell(7).getCellStyle().getBorderBottomEnum(), cda,
				(Sheet) mySheet);
		RegionUtil.setBorderTop(mySheet.getRow(1).getCell(7).getCellStyle().getBorderTopEnum(), cda, mySheet);
		RegionUtil.setBorderLeft(mySheet.getRow(1).getCell(7).getCellStyle().getBorderLeftEnum(), cda, mySheet);
		RegionUtil.setBorderRight(mySheet.getRow(1).getCell(7).getCellStyle().getBorderRightEnum(), cda, mySheet);

		RegionUtil.setBottomBorderColor(mySheet.getRow(1).getCell(7).getCellStyle().getBottomBorderColor(), cda,
				mySheet);
		RegionUtil.setTopBorderColor(mySheet.getRow(1).getCell(7).getCellStyle().getTopBorderColor(), cda, mySheet);
		RegionUtil.setLeftBorderColor(mySheet.getRow(1).getCell(7).getCellStyle().getLeftBorderColor(), cda,
				mySheet);
		RegionUtil.setRightBorderColor(mySheet.getRow(1).getCell(7).getCellStyle().getRightBorderColor(), cda,
				mySheet);
		
		cda = new CellRangeAddress(1,1,10,11);
		mySheet.addMergedRegion(cda);
		for(int j = 1; j < 4; j++) {
			mySheet.getRow(j).createCell(10);
			mySheet.getRow(j).createCell(11);
			switch(j) {
				case 1:
					mySheet.getRow(j).getCell(10).setCellValue("Dinucleotides");
					mySheet.getRow(j).getCell(10).setCellStyle(styleTitleCell);
					mySheet.getRow(j).getCell(11).setCellStyle(styleTitleCell);
					break;
				case 2: 
					mySheet.getRow(j).getCell(10).setCellValue("Nb Sequences");
					mySheet.getRow(j).getCell(10).setCellStyle(styleNumberFormatCell2);
					mySheet.getRow(j).getCell(11).setCellValue(0);
					mySheet.getRow(j).getCell(11).setCellStyle(styleNumberFormatCell2);
					break;
				case 3: 
					mySheet.getRow(j).getCell(10).setCellValue("Nb Bases");
					mySheet.getRow(j).getCell(10).setCellStyle(styleNumberFormatCell);
					mySheet.getRow(j).getCell(11).setCellValue(0);
					mySheet.getRow(j).getCell(11).setCellStyle(styleNumberFormatCell);
					break;
			}
		}
		RegionUtil.setBorderBottom(mySheet.getRow(1).getCell(10).getCellStyle().getBorderBottomEnum(), cda,
				(Sheet) mySheet);
		RegionUtil.setBorderTop(mySheet.getRow(1).getCell(10).getCellStyle().getBorderTopEnum(), cda, mySheet);
		RegionUtil.setBorderLeft(mySheet.getRow(1).getCell(10).getCellStyle().getBorderLeftEnum(), cda, mySheet);
		RegionUtil.setBorderRight(mySheet.getRow(1).getCell(10).getCellStyle().getBorderRightEnum(), cda, mySheet);

		RegionUtil.setBottomBorderColor(mySheet.getRow(1).getCell(10).getCellStyle().getBottomBorderColor(), cda,
				mySheet);
		RegionUtil.setTopBorderColor(mySheet.getRow(1).getCell(10).getCellStyle().getTopBorderColor(), cda, mySheet);
		RegionUtil.setLeftBorderColor(mySheet.getRow(1).getCell(10).getCellStyle().getLeftBorderColor(), cda,
				mySheet);
		RegionUtil.setRightBorderColor(mySheet.getRow(1).getCell(10).getCellStyle().getRightBorderColor(), cda,
				mySheet);
	}

	private void createNewTableTrinucleotide(XSSFSheet mySheet) {
		// Style
		XSSFCellStyle styleDefaultCell = (XSSFCellStyle) this.wb.createCellStyle();
		XSSFCellStyle styleCell = (XSSFCellStyle) this.wb.createCellStyle();
		styleDefaultCell.setAlignment(HorizontalAlignment.CENTER);
		styleCell.setAlignment(HorizontalAlignment.CENTER);
		XSSFFont fontBold = (XSSFFont) this.wb.createFont();
		fontBold.setBold(true);
		styleCell.setFont(fontBold);
		
		//PercentageStyleCell
		XSSFCellStyle stylePercentageCell = (XSSFCellStyle) this.wb.createCellStyle();
		stylePercentageCell.setAlignment(HorizontalAlignment.CENTER);
		stylePercentageCell.setDataFormat(this.wb.createDataFormat().getFormat("0.000%"));
		
		//NumberStyleCell
		XSSFCellStyle styleNumberFormatCell = (XSSFCellStyle) this.wb.createCellStyle();
		styleNumberFormatCell.setAlignment(HorizontalAlignment.CENTER);
		styleNumberFormatCell.setDataFormat(this.wb.createDataFormat().getFormat("# ##0"));
		
		//PrefStyleCell
		XSSFCellStyle styleDefaultPrefCell = (XSSFCellStyle) this.wb.createCellStyle();
		XSSFCellStyle stylePrefCell = (XSSFCellStyle) this.wb.createCellStyle();
		styleDefaultPrefCell.setAlignment(HorizontalAlignment.CENTER);
		stylePrefCell.setAlignment(HorizontalAlignment.CENTER);
		styleDefaultPrefCell.setFillForegroundColor(new XSSFColor(new Color(220, 230, 241)));
		styleDefaultPrefCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		stylePrefCell.setFillForegroundColor(new XSSFColor(new Color(184, 204, 228)));
		stylePrefCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleDefaultPrefCell.setDataFormat(this.wb.createDataFormat().getFormat("# ##0"));
		stylePrefCell.setDataFormat(this.wb.createDataFormat().getFormat("# ##0"));
		
		// Create
		XSSFTable table = mySheet.createTable();
		table.setDisplayName("Tableau_trinucleotide_" + mySheet.getSheetName());
		CTTable cttable = table.getCTTable();

		// Style configurations
		CTTableStyleInfo style = cttable.addNewTableStyleInfo();
		style.setName("TableStyleMedium2");
		style.setShowColumnStripes(false);
		style.setShowRowStripes(true);

		// Set which area the table should be placed in
		AreaReference reference = new AreaReference(new CellReference(7, 0), new CellReference(72, 9));
		cttable.setRef(reference.formatAsString());
		Random rng = new Random();
		int randomNumber = rng.nextInt(10000 - 0) + 10000;
		cttable.setId(randomNumber);
		cttable.setDisplayName("TABLE_TRINUCLEOTIDE_" + mySheet.getSheetName());
		cttable.setName(mySheet.getSheetName() + "_trinucleotide");

		CTTableColumns columns = cttable.addNewTableColumns();
		columns.setCount(10);
		CTTableColumn column;
		XSSFRow row;
		XSSFCell cell;
		for (int q = 0; q < 10; q++) {
			// Create column
			column = columns.addNewTableColumn();
			column.setName("Column");
			column.setId(q + 1);
		}

		List<String> header = generateHeaderTableTrinucleotide();
		List<String> trinucleotide = generateTriNucleotides();

		for (int i = 7; i < 73; i++) {

			// Create row
			row = mySheet.createRow(i);
			for (int j = 0; j < 10; j++) {
				// Create cell
				cell = row.createCell(j);
				
				// j == 7/8/9
				if (i == 7) {
					cell.setCellValue(header.get(0));
					header.remove(0);
					cell.setCellStyle(styleDefaultCell);
				} else if (j == 0 && !trinucleotide.isEmpty()) {
					cell.setCellValue(trinucleotide.get(0));
					trinucleotide.remove(0);
					cell.setCellStyle(styleCell);
				} else if (j == 0 && trinucleotide.isEmpty()) {
					cell.setCellValue("Total");
					cell.setCellStyle(styleCell);
				} else if(j>=7 && j<=9) {
					if(i%2!=0) {
						cell.setCellValue(0);
						cell.setCellStyle(styleDefaultPrefCell);
					} else {
						cell.setCellValue(0);
						cell.setCellStyle(stylePrefCell);
					}
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(styleNumberFormatCell);
				}
				
				if (j==2||j==4||j==6) {
					cell.setCellStyle(stylePercentageCell);
				}
			}
		}
	}

	private void createNewTableDinucleotide(XSSFSheet mySheet) {
		// Style
		XSSFCellStyle styleDefaultCell = (XSSFCellStyle) this.wb.createCellStyle();
		XSSFCellStyle styleCell = (XSSFCellStyle) this.wb.createCellStyle();
		styleCell.setAlignment(HorizontalAlignment.CENTER);
		styleDefaultCell.setAlignment(HorizontalAlignment.CENTER);
		XSSFFont fontBold = (XSSFFont) this.wb.createFont();
		fontBold.setBold(true);
		styleCell.setFont(fontBold);
		
		//PercentageStyleCell
		XSSFCellStyle stylePercentageCell = (XSSFCellStyle) this.wb.createCellStyle();
		stylePercentageCell.setAlignment(HorizontalAlignment.CENTER);
		stylePercentageCell.setDataFormat(this.wb.createDataFormat().getFormat("0.000%"));

		//NumberStyleCell
		XSSFCellStyle styleNumberFormatCell = (XSSFCellStyle) this.wb.createCellStyle();
		styleNumberFormatCell.setAlignment(HorizontalAlignment.CENTER);
		styleNumberFormatCell.setDataFormat(this.wb.createDataFormat().getFormat("# ##0"));
		
		// Create
		XSSFTable table = mySheet.createTable();
		table.setDisplayName("Tableau_dinucleotide_" + mySheet.getSheetName());
		CTTable cttable = table.getCTTable();

		// Style configurations
		CTTableStyleInfo style = cttable.addNewTableStyleInfo();
		style.setName("TableStyleMedium2");
		style.setShowColumnStripes(false);
		style.setShowRowStripes(true);

		// Set which area the table should be placed in
		AreaReference reference = new AreaReference(new CellReference(7, 11), new CellReference(24, 15));
		cttable.setRef(reference.formatAsString());
		Random rng = new Random();
		int randomNumber = rng.nextInt(10000 - 0) + 10000;
		cttable.setId(randomNumber);
		cttable.setDisplayName("TABLE_DINUCLEOTIDE_" + mySheet.getSheetName());
		cttable.setName(mySheet.getSheetName() + "_dinucleotide");

		CTTableColumns columns = cttable.addNewTableColumns();
		columns.setCount(5);
		CTTableColumn column;
		XSSFRow row;
		XSSFCell cell;
		for (int q = 0; q < 5; q++) {
			// Create column
			column = columns.addNewTableColumn();
			column.setName("Column");
			column.setId(q + 1);
		}

		List<String> header = generateHeaderTableDinucleotide();
		List<String> dinucleotide = generateDiNucleotides();

		for (int i = 7; i < 25; i++) {

			// Create row
			row = mySheet.getRow(i);
			for (int j = 11; j < 16; j++) {
				// Create cell
				cell = row.createCell(j);
				if (i == 7) {
					cell.setCellValue(header.get(0));
					header.remove(0);
					cell.setCellStyle(styleDefaultCell);
				} else if (j == 11 && !dinucleotide.isEmpty()) {
					cell.setCellValue(dinucleotide.get(0));
					dinucleotide.remove(0);
					cell.setCellStyle(styleCell);
				} else if (j == 11 && dinucleotide.isEmpty()) {
					cell.setCellValue("Total");
					cell.setCellStyle(styleCell);
				} else {
					cell.setCellValue(0);
					cell.setCellStyle(styleNumberFormatCell);
				}
				
				if(j==13||j==15) {
					cell.setCellStyle(stylePercentageCell);
				}
			}
		}
	}

	public Workbook getWb() {
		return wb;
	}

	public void setWb(Workbook wb) {
		this.wb = wb;
	}

	// Generate all trinucleotides
	private List<String> generateTriNucleotides() {
		List<String> res = new ArrayList<String>();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					res.add(Nucleotide.values()[i].toString() + Nucleotide.values()[j].toString()
							+ Nucleotide.values()[k].toString());
				}
			}
		}
		return res;
	}

	// Generate all dinucleotides
	private List<String> generateDiNucleotides() {
		List<String> res = new ArrayList<String>();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				res.add(Nucleotide.values()[i].toString() + Nucleotide.values()[j].toString());
			}
		}
		return res;
	}

	private List<String> generateHeaderTableTrinucleotide() {
		List<String> headerTrinucleotide = new ArrayList<String>();
		headerTrinucleotide.add("Trinucleotides");
		headerTrinucleotide.add("Nb Ph0");
		headerTrinucleotide.add("% Ph0");
		headerTrinucleotide.add("Nb Ph1");
		headerTrinucleotide.add("% Ph1");
		headerTrinucleotide.add("Nb Ph2");
		headerTrinucleotide.add("% Ph2");
		headerTrinucleotide.add("Pref Ph0");
		headerTrinucleotide.add("Pref Ph1");
		headerTrinucleotide.add("Pref Ph2");
		return headerTrinucleotide;
	}

	private List<String> generateHeaderTableDinucleotide() {
		List<String> headerDinucleotide = new ArrayList<String>();
		headerDinucleotide.add("Dinucleotides");
		headerDinucleotide.add("Nb Ph0");
		headerDinucleotide.add("% Ph0");
		headerDinucleotide.add("Nb Ph1");
		headerDinucleotide.add("% Ph1");
		return headerDinucleotide;
	}

	private List<String> generateHeaderTableInformation() {
		List<String> headerInformation = new ArrayList<String>();
		headerInformation.add("Organisme Name");
		headerInformation.add("BioProject");
		headerInformation.add("Number of nucleotides");
		headerInformation.add("Number of cds sequences");
		headerInformation.add("Number of invalide cds");
		headerInformation.add("Modification date");
		return headerInformation;
	}
}
