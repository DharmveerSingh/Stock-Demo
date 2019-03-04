package ca.gl.fileUploader.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javax.naming.OperationNotSupportedException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ca.gl.fileUploader.model.Stock;
import ca.gl.fileUploader.utils.Utils;
import constant.AppConstants;

public class ExcelReader implements FileReader {
	private File file;

	public ExcelReader(File file) {
		this.file = file;
	}

	@Override
	public List<Stock> readFileAsList() {

		Path path = Utils.moveFileToArch(file.getName());
		if (path != null)
			return readBooksFromExcelFile(path);
		else
			return new ArrayList<>();

	}

	private List<Stock> readBooksFromExcelFile(Path path) {
		List<Stock> listStocks = new ArrayList<>();
		FileInputStream inputStream;
		Workbook workbook = null;
		try {
			inputStream = new FileInputStream(path.toFile());

			workbook = new XSSFWorkbook(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
			return new ArrayList<>();
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
				count++;
				continue;
			}

			Stock stock = new Stock();
			while (cellIterator.hasNext()) {
				try {
					stockName = cellIterator.next().getStringCellValue();
					stock.setStockID(AppConstants.LATEST + stockName);
					stock.setStockSymbol(stockName);
					stock.setPrevClose(cellIterator.next().getNumericCellValue());
					stock.setPrice(cellIterator.next().getNumericCellValue());
					stock.setPE(cellIterator.next().getNumericCellValue());
					stock.setEPS(cellIterator.next().getNumericCellValue());
					stock.setLow(cellIterator.next().getNumericCellValue());
					stock.setHigh(cellIterator.next().getNumericCellValue());
					stock.setVolume((long) cellIterator.next().getNumericCellValue());
					stock.setWkLow(cellIterator.next().getNumericCellValue());
					stock.setWkHigh(cellIterator.next().getNumericCellValue());
					stock.setOpenPrice(cellIterator.next().getNumericCellValue());

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

	@Override
	public Stream<Stock> readFileAsStream() throws OperationNotSupportedException {
		throw new OperationNotSupportedException("don't have stream support yet");
		// return
	}

}