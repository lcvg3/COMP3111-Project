package testing.comp3111;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataTableException;
import core.comp3111.DataType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the DataTable class.
 * 
 * You'll be writing tests here for the Unit Testing lab!
 * 
 * @author victorkwan
 *
 */
public class DataTableTest {
	DataColumn testDataColumn;
	
	@BeforeEach
	void init() {
		testDataColumn = new DataColumn(DataType.TYPE_NUMBER, new Number[] {1,2,3});
	}
	
	@Test
	void testGetNumRow_Empty() {
		DataTable dataTable = new DataTable();
		assertEquals(0, dataTable.getNumRow());
	}
	
	@Test 
	void testGetNumRow_NonEmpty() throws DataTableException {
		DataTable dataTable = new DataTable();
		dataTable.addCol("testColumn", new DataColumn());
		
		assertEquals(0, dataTable.getNumRow());
	}
	
	
	@Test
	void testGetNumCol_NonEmpty() throws DataTableException {
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		int numCol = dataTable.getNumCol();
		assertEquals(1, numCol);
	}
	
	@Test
	void testNonExistentCol() throws DataTableException {
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		
		DataColumn dataColumn = dataTable.getCol("testStringColumn");
		
		assertEquals(null, dataColumn);
	}
	
	@Test
	void testAlreadyExists() throws DataTableException {
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		assertThrows(DataTableException.class, () -> dataTable.addCol("testNumberColumn", new DataColumn()));
	}
	
	@Test
	void testGetCol() throws DataTableException {
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		assertEquals(dataTable.getCol("testNumberColumn"), testDataColumn);
	}
	
	@Test
	void testRemoveCol() throws DataTableException {
		DataTable dataTable = new DataTable();
		dataTable.addCol("testRev", testDataColumn);
		dataTable.removeCol("testRev");
		assertEquals(null, dataTable.getCol("testRev"));
	}
	
	@Test
	void testRemoveErr() throws DataTableException {
		DataTable dataTable = new DataTable();
		assertThrows(DataTableException.class, () -> dataTable.removeCol("Not there"));
	}
	
	@Test
	void testAddColNorm() throws DataTableException {
		DataTable dataTable = new DataTable();
		dataTable.addCol("col1", testDataColumn);
		dataTable.addCol("col2", testDataColumn);
		assertEquals(dataTable.getCol("col1"), testDataColumn);
		assertEquals(dataTable.getCol("col2"), testDataColumn);
	}
	
	@Test
	void testWrongFormatCol() throws DataTableException {
		DataTable dataTable = new DataTable();
		dataTable.addCol("first", testDataColumn);
		DataColumn second = new DataColumn(DataType.TYPE_NUMBER, new Number[] {1, 1});
		assertThrows(DataTableException.class, () -> dataTable.addCol("wrong", second));
	}

}
