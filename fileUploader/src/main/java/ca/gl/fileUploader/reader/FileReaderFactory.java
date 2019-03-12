package ca.gl.fileUploader.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ca.gl.fileUploader.model.Stock;
import constant.AppConstants;

/**
 * A factory for creating FileReader objects.
 */
public class FileReaderFactory {

	/**
	 * Instantiates a new file reader factory.
	 */
	private FileReaderFactory() {
	};

	/**
	 * Gets the file reader.
	 *
	 * @param fileName the file name
	 * @return the file reader
	 */
	public static FileReader getFileReader(String fileName) {

		String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
		FileReader reader = null;

		switch (ext) {
		case "xlsx":
		case "xls":
			reader = new ExcelReader(fileName);
			break;

		default:
			;
		}
		return reader;
	}

	/**
	 * The Class ExcelReader.
	 */
	private static class ExcelReader implements FileReader {
		
		/** The file name. */
		String fileName;

		/**
		 * Instantiates a new excel reader.
		 *
		 * @param fileName the file name
		 */
		public ExcelReader(String fileName) {
			this.fileName = fileName;
		}

		/* (non-Javadoc)
		 * @see ca.gl.fileUploader.reader.FileReader#readFile()
		 */
		@Override
		public List<Stock> readFile() {

			Path path = moveFileToArch(fileName);
			if (path != null)
				return readBooksFromExcelFile(path);
			else
				return new ArrayList<>();

		}

		/**
		 * Move file to arch.
		 *
		 * @param fileName the file name
		 * @return the path
		 */
		private Path moveFileToArch(String fileName) {
			return Paths.get(fileName);
			
		}

		/**
		 * Read books from excel file.
		 *
		 * @param path the path
		 * @return the list
		 */
		private List<Stock> readBooksFromExcelFile(Path path) {
			List<Stock> listStocks = new ArrayList<>();
			FileInputStream inputStream;
			Workbook workbook = null;
			try {
				inputStream = new FileInputStream(path.toFile());

				workbook = new XSSFWorkbook(inputStream);
			} catch (IOException e1) {
				e1.printStackTrace();
				return listStocks;
			}
			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = firstSheet.iterator();

			// Stock Symbol Prev Close Price PE EPS Low High Volume 52-Wk Low 52-Wk High
			int count = 1;
			String stockName;
			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				if (count == 1) {
					System.out.println("Skipping first row: ");
					count++;
					continue;
				}
				Stock stock = new Stock();
				while (cellIterator.hasNext()) {
					try {
						stockName=cellIterator.next().getStringCellValue();
						stock.setStockID(AppConstants.LATEST+stockName);
						stock.setStockSymbol(stockName);
						stock.setPrevClose(cellIterator.next().getNumericCellValue());
						stock.setPE(cellIterator.next().getNumericCellValue());
						stock.setEPS(cellIterator.next().getNumericCellValue());
						stock.setLow(cellIterator.next().getNumericCellValue());
						stock.setHigh(cellIterator.next().getNumericCellValue());
						stock.setVolume((long) cellIterator.next().getNumericCellValue());
						stock.setWkLow(cellIterator.next().getNumericCellValue());
						stock.setWkHigh(cellIterator.next().getNumericCellValue());

						listStocks.add(stock);
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
				}
			}

			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return listStocks;
		}
	}
}