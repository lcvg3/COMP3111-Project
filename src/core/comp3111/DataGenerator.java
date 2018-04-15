package core.comp3111;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataGenerator {

	/**
	 * Data generator utilizing csv file io
	 * 
	 * @return DataTable object
	 */
	public static DataTable generateData(String csvpath) {

		DataTable t = new DataTable();

		ArrayList<String[]> values = parseCSV(csvpath);
		int colNum = Integer.parseInt((values.get(0))[0]); //get number of columns
		int rowNum = values.size();
		ArrayList<DataColumn> columns = new ArrayList<DataColumn>();
		String[][] colData = new String[colNum][rowNum];
		
		for (int i = 1; i< colNum; i++) { //skip first entry because its max value
			for (int s = 0; s < rowNum; s++) {
				try {
					colData[i][s] = (values.get(i))[s];
				} catch (Exception e) {
					colData[i][s] = null; //or desired mode of filling in missing values
				}
			}
		}
		
		for (int i = 0; i < colNum; i++) { //create datacolumns with data
			DataColumn col = new DataColumn(DataType.TYPE_OBJECT, colData[i]);
			columns.add(col);
		}

		try {
			for (int i = 0; i < colNum; i++) {
				t.addCol(Integer.toString(i) + 1, columns.get(i)); //number each column, all plus one, so no zero col
			}

		} catch (DataTableException e) {
			e.printStackTrace();

		}

		return t;
	}
	
	/**
	 * Helper function for DataGenerator()
	 * @return ArrayList of String[] with row data of csv file
	 */
	public static ArrayList<String[]> parseCSV(String csvpath) {
		String csvFile = csvpath;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		ArrayList<String[]> data = new ArrayList<String[]>();
		String[] placeholder = {"", "a"};
		int colCount;
		Integer max = 0;
		
		data.add(placeholder); //placeholder for max number of columns
		
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String[] row = line.split(cvsSplitBy);
				if (max < row.length) {
					max = row.length;
				}
				data.add(row);
			}
		} catch (FileNotFoundException e) {
			//say that file was not found, input new one
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			placeholder[0] = Integer.toString(max);
		}
	}

}
