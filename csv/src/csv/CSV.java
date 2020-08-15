package csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.google.gson.Gson;

public class CSV {
	
	private static String CSV_filename = "E:\\Downloads\\inventorycsv.csv";

	private static List < String > fieldNames = null;
	
	private static List <Map<String,String>> list = new ArrayList<Map<String,String>>();
	
	public static void main(String[] args) throws FileNotFoundException, IOException {

		saveCSV();
		
	}
	
	public static void saveCSV() throws IOException {
		
		try (InputStream in = new FileInputStream(new File(CSV_filename))) {
		    CSVLib csv = new CSVLib(true, ',', in );
		    
		    if (csv.hasNext()) fieldNames = new ArrayList < > (csv.next());
		    
		    
		    while (csv.hasNext()) {
		        List < String > x = csv.next();
		        Map < String, String > obj = new LinkedHashMap < > ();
		        for (int i = 0; i < fieldNames.size(); i++) {
		            obj.put(fieldNames.get(i), x.get(i));
		        }
		        
		        
		        Gson gson = new Gson();
		        
		        String response = HttpClient.post(gson.toJson(obj));
		        obj.put("response", response);
		        
		        list.add(obj);
		    }
		 
		    System.out.println("All Done!");
		    
		    CSVWithResponses();
		  
		}

	}
	
	public static void CSVWithResponses() throws IOException {
	    FileWriter out = new FileWriter(CSV_filename);
	    
		String[] HEADERS = { "id", "name","barcode","response"};
		
	    try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {
	        
	    	for(int k=0; k < list.size(); k++)
	    	{
	    		Map<String,String> row = list.get(k);
	    		 try {
	 				printer.printRecord(row.get("id"), row.get("name"),row.get("barcode"),row.get("response"));
	 			} catch (IOException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}
	    	}
           
	        
	    }
	}

}
