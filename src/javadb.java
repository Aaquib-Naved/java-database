/**
 * 
 */
import java.sql.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import org.postgresql.util.PSQLException;


/**
 * @author axn598
 *
 */
public class javadb {

	Connection conn;
	Statement stmt;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String username = "axn598";
        String password = "bedvo3rj68";
        String database = "axn598";
        String URL = "jdbc:postgresql://mod-intro-databases.cs.bham.ac.uk/";

        javadb test = new javadb();
        
        // Create the Tables in the database. This is commented out to ensure the tables aren't created every time.
//        System.out.println("Creating Tables in Database...");
//        test.createDB(URL, username, password, database);
//        System.out.println("Tables created.");
        
        // Connect to the database.
        System.out.println("Connecting to the database...");
        test.connectToDB(URL, username, password);
        System.out.println("Connected to the database.");
        
        // Populate the data in the database. This is commented out to ensure the data isn't created every time.
//        System.out.println("Populating Database...");
//        test.populateDB();
//        System.out.println("Database populated.");
        
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        int input = -1;
        
        // Take input from user for Child ID and Helper ID to display reports.
        while(input != 0) {
        	try {
        		System.out.println("Enter a child ID (Enter 0 to exit): ");
                input = Integer.parseInt(reader.nextLine());
                
                if(input == 0) {
                	break;
                }
                
                // Print Child Report
                test.printChildReport(input);
                
                System.out.println("Enter a helper ID (Enter 0 to exit): ");
                input = Integer.parseInt(reader.nextLine());
                
                // Print Helper Report
                test.printHelperReport(input);
        	}
        	// When the input is not an integer, let the User input again.
    		catch(NumberFormatException e) {
    			System.out.println("Input was not an integer. Please try again.\n");
    			input = -1;
    		}
        }
        
        // Disconnect from the database.
        test.disconnectToDB();
        reader.close();
        System.out.println("Disconnected from database.");
	}
	
	/*
	 * Constraints for Tables
	 * 
	 * Children Table:
	 * cid -> Not Null, Primary Key
	 * name -> Not Null
	 * address -> Not Null
	 * 
	 * Santas Helpers Table
	 * slhid -> Not Null, Primary Key
	 * name -> Not Null
	 * 
	 * Gifts Table
	 * gid -> Not Null, Primary Key
	 * description -> Not Null
	 * 
	 * Presents
	 * gid -> Foreign Key Referencing gid in Gifts Table, Not Null
	 * cid -> Foreign Key Referencing cid in Children Table, Not Null
	 * slhid -> Foreign Key Referencing slhid in Santas Helpers Table, Not Null
	 */
	public void createDB(String url, String username, String pass, String database) {
		try{
			try {
	            //Load the PostgreSQL JDBC driver
	            Class.forName("org.postgresql.Driver");

	        } catch (ClassNotFoundException ex) {
	            System.out.println("Driver not found");
	            System.exit(1);
	        }

	        System.out.println("PostgreSQL driver registered.");
		    
		    conn = DriverManager.getConnection(url + database, username, pass);
		    System.out.println("Creating database...");
		    stmt = conn.createStatement();

		    // Drop all the current tables so we can recreate them fresh.
		    stmt.executeUpdate("DROP TABLE IF EXISTS Presents;");
		    stmt.executeUpdate("DROP TABLE IF EXISTS Gifts;");
		    stmt.executeUpdate("DROP TABLE IF EXISTS Children;");
		    stmt.executeUpdate("DROP TABLE IF EXISTS SantasLittleHelpers;");
		    
		    // Creating the Child Table
		    String childTable = 
		    		"CREATE TABLE Children "
		    		+	"("
		    		+	"cid int NOT NULL PRIMARY KEY, "
		    		+	"name varchar(50) NOT NULL, "
		    		+	"address varchar(100) NOT NULL "
		    		+	");";
		    stmt.executeUpdate(childTable);
		    
		    // Creating the Santas Helpers Table
		    String santaHelperTable = 
		    		"CREATE TABLE SantasLittleHelpers "
		    		+	"("
		    		+	"slhid int NOT NULL PRIMARY KEY, "
		    		+	"name varchar(50) NOT NULL "
		    		+	");";
		    stmt.executeUpdate(santaHelperTable);
		    
		    // Creating the Gifts Table
		    String giftTable = 
		    		"CREATE TABLE Gifts "
		    		+	"("
		    		+	"gid int NOT NULL PRIMARY KEY, "
		    		+	"description varchar(100) NOT NULL "
		    		+	");";
		    stmt.executeUpdate(giftTable);
		    
		    // Creating the Presents Table
		    String presentTable = 
		    		"CREATE TABLE Presents "
		    		+	"("
		    		+	"gid int NOT NULL, "
		    		+	"cid int NOT NULL, "
		    		+	"slhid int NOT NULL, "
		    		+	"FOREIGN KEY (gid) REFERENCES Gifts(gid) NOT NULL, "
		    		+	"FOREIGN KEY (cid) REFERENCES Children(cid) NOT NULL, "
		    		+	"FOREIGN KEY (slhid) REFERENCES SantasLittleHelpers(slhid) NOT NULL "
		    		+	");";
		    stmt.executeUpdate(presentTable);
		    
		 }
		 catch(SQLException se){
		    //Handle errors for JDBC
		    se.printStackTrace();
		 }
	}
	
	public void populateDB() {
		// Random data
		String[] names = new String[]{"Philip", "John", "Peter", "Tom", "Dixy", "Pepes", "Swiper", "Dora", "Troy", "Charlie", "Richard", "Harry", "Andrew", "Stephen", "Brandon", "William", "Julian", "Julie", "Rachel", "Ellie", "Debora", "Alan", "Mike", "Hannah", "David"};
		String[] addresses = new String[]{"Old Bristol Rd, East Brent, Highbridge TA9 4HT, UK", "Chepstow Hall, 29-31 Earls Ct Square, Kensington, London SW5 9DB, UK", "Timothy House, 6 Kale Rd, Erith DA18 4BQ, UK", "27 Cleveland Gardens, London SW13 0AE, UK", "4 Bryn Siriol, Benllech, Tyn-y-Gongl LL74 8TZ, UK", "34 Foxdell Way, Derby DE73 6PU, UK", "8 Old Hall Rd, Bentley, Doncaster DN5 0DW, UK", "1 The Causeway, Worthing BN12 6FA, UK", "72 Gittin St, Oswestry SY11 1DS, UK", "5 Lulworth Rd, Caerleon, Newport NP18 1QE, UK", "110 Harewood Ave, Bournemouth BH7 6NS, UK", "1 Derby Square, Liverpool L2 9XX, UK", "1A Cornfield Terrace, Eastbourne BN21 4NN, UK", "23 Loanhead Ave, Grangemouth FK3 9EG, UK", "1 Oldacres, Maidenhead SL6 1XH, UK", "2 Trent Ln, East Bridgford, Nottingham NG13 8PF, UK", "3 Douglas Pier, Lochgoilhead, Cairndow PA24 8AE, UK", "Clippens Dr, Edinburgh EH17 8SQ, UK", "3 Earls Ct, Fifth Ave, Gateshead NE11 0HF, UK", "42 Queensway, Worksop S81 0AD, UK", "26 Christopher St, Ince-in-Makerfield, Wigan WN3 4QY, UK", "Old Fen Bank, Wainfleet All Saints, Skegness PE24 4LE, UK", "7 Abingdon St, Barry CF63 2HQ, UK", "29 Lee Rd, Lynton EX35 6BS, UK", "York Way, Borehamwood WD6, UK", "18 Cavendish Ave, Saint Leonards-on-sea TN38 0NR, UK", "3 Vesty Rd, Bootle L30 1NY, UK", "12 New Square, London WC2A 3SW, UK", "5 Green Hill, Llandysul SA44 4BZ, UK", "4 Savoy Parade, Enfield EN1 1RT, UK", "38 Prey Heath Cl, Woking GU22 0SP, UK", "Unnamed Road, Tain IV20 1XJ, UK", "77 Trongate, Glasgow G1 5HB, UK", "5 Furness Grove, Halifax HX2 8NB, UK"};
		String[] descriptions = new String[]{"Random descrption", "Another one", "A third one"};
		Random r = new Random();
		
		try {
			String sql;
			// Create 1000 Children
			for(int i=0; i<1000; i++) {
				int rnd = r.nextInt(names.length);
				int rnd2 = r.nextInt(addresses.length);
				sql = "INSERT INTO Children (cid, name, address) VALUES (" + (i + 1) + ", '" + names[rnd] + "', " + "'" + addresses[rnd2] + "'" + ");";
				//System.out.println(sql);
				stmt.executeUpdate(sql);
				//System.out.println("Executed");
			}
			
			// Create 100 Santas Little Helpers
			for(int i=0; i<100; i++) {
				int rnd = r.nextInt(names.length);
				sql = "INSERT INTO SantasLittleHelpers (slhid, name) VALUES (" + (i+1) + ", '" + names[rnd] + "');";
				stmt.executeUpdate(sql);
			}
			
			// Create 10 different types of sgifts
			for(int i=0; i<10; i++) {
				int rnd = r.nextInt(descriptions.length);
				sql = "INSERT INTO Gifts (gid, description) VALUES (" + (i+1) + ", '" + descriptions[rnd] + "'" + ");";
				stmt.executeUpdate(sql);
			}
			
			// Assign each child and their gifts to random Santas Little Helpers
			int slhid = -1;
			for(int i=0; i<1000; i++) {
				slhid = r.nextInt(99)+1;
				for(int j=r.nextInt(9)+1; j<10; j++) {
					sql = "INSERT INTO Presents (gid, cid, slhid) VALUES (" + (r.nextInt(9)+1) + ", " + (i+1) + ", " + slhid + ");";
					stmt.executeUpdate(sql);
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void printChildReport(int childId) {
		try {
			String sql = "SELECT * FROM Children WHERE cid = ?;";
			PreparedStatement query = conn.prepareStatement(sql);
			query.setInt(1, childId);
			ResultSet rs = query.executeQuery();
			
			// If no child with that cid is in the database, inform the user.
			if (!rs.isBeforeFirst() ) {    
			    System.out.println("Child with that ID does not exist.");
			    return;
			} 
			
			String report = "";
			while(rs.next()) {
				String name = rs.getString("name");
				String address = rs.getString("address");
				
				report += "Child ID: " + childId + ", Name: " + name + ", Address: " + address + "\n";
				
				// Retrieve all the gifts which are assigned to that child.
				sql = "SELECT * FROM Presents WHERE cid = ?;";
				PreparedStatement query1 = conn.prepareStatement(sql);
				query1.setInt(1, childId);
				ResultSet rs1 = query1.executeQuery();
				while(rs1.next()) {
					int giftId = rs1.getInt("gid");
					sql = "SELECT * FROM Gifts WHERE gid = ?;";
					PreparedStatement query2 = conn.prepareStatement(sql);
					query2.setInt(1, giftId);
					ResultSet rs2 = query2.executeQuery();
					while(rs2.next()) {
						report += "Gift -> Gift ID: " + rs2.getInt("gid") + ", Description: " + rs2.getString("description") + "\n";
					}
				}
			}
			
			System.out.println(report);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void printHelperReport(int helperId) {
		try {
			// Store all the gifts associated with a child in this Hash Map
			HashMap<Integer, String> storage = new HashMap<Integer, String>();
			
			// Select the Helper
			String sql = "SELECT * FROM SantasLittleHelpers WHERE slhid = ?;";
			PreparedStatement query = conn.prepareStatement(sql);
			query.setInt(1, helperId);
			ResultSet rs = query.executeQuery();
			
			// If no helper with this slhid exits, inform the User.
			if (!rs.isBeforeFirst() ) {    
			    System.out.println("No Santa's Helper exists with that ID.");
			    return;
			} 
			
			String report = "";
			while(rs.next()) {
				String name = rs.getString("name");
				
				report += "Santas Little Helper ID: " + helperId + ", Name: " + name + "\n\n";
				
				// Retrieve all the presents the Helper needs to deliver
				sql = "SELECT * FROM Presents WHERE slhid = ?;";
				PreparedStatement query1 = conn.prepareStatement(sql);
				query1.setInt(1, helperId);
				ResultSet rs1 = query1.executeQuery();
				while(rs1.next()) {
					int cid = rs1.getInt("cid");
					int gid = rs1.getInt("gid");
					
					// Retrieve the children which need to receive the presents the Helper needs to deliver.
					sql = "SELECT * FROM Children WHERE cid = ?;";
					PreparedStatement query2 = conn.prepareStatement(sql);
					query2.setInt(1, cid);
					ResultSet rs2 = query2.executeQuery();
					String child = "";
					while(rs2.next()) {
						child = "Child ID: " + cid + ", Child Name: " + rs2.getString("name") + ", Child Address: " + rs2.getString("address") + "\n";
					}
					
					// Retrieve the gifts which should be delivered to the children
					sql = "SELECT * FROM Gifts WHERE gid = ?;";
					PreparedStatement query3 = conn.prepareStatement(sql);
					query3.setInt(1, gid);
					ResultSet rs3 = query3.executeQuery();
					String gift = "";
					while(rs3.next()) {
						gift = "Gift ID: " + gid + ", Description: " + rs3.getString("description") + "\n";
					}
					
					if(storage.containsKey(cid)) {
						String presents = storage.get(cid);
						presents += gift;
						storage.put(cid, presents);
					}
					else {
						String presents = child;
						presents += gift;
						storage.put(cid, presents);
					}
				}
				for(String present: storage.values()) {
					report += present;
				}
			}
			
			// Print out the report
			System.out.println(report);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void connectToDB(String url, String username, String pass) {

        try {

            //Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found");
            System.exit(1);
        }

        this.conn = null;
        try {
            this.conn = DriverManager.getConnection(url, username, pass);
            this.stmt = conn.createStatement();
        } catch (SQLException ex) {
            System.out.println("Ooops, couldn't get a connection");
            System.out.println("Check that <username> & <password> are right");
            System.exit(1);
        }

        if (conn != null) {
            System.out.println("Database accessed!");
        } else {
            System.out.println("Failed to make connection");
            System.exit(1);
        }
    }

	public void disconnectToDB() {
		//Now, just tidy up by closing connection
        try {
            conn.close();
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
        }
	    try{
	       if(stmt!=null)
	          stmt.close();
	    }
	    catch(SQLException se){
	    	se.printStackTrace();
	    }
	}
}
