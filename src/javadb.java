/**
 * 
 */
import java.sql.*;
import java.util.HashMap;
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
        String database = "santa";
        String URL = "jdbc:postgresql://mod-intro-databases.cs.bham.ac.uk/";

        javadb test = new javadb();
        
        test.createDB(URL, username, password, database);
        
        test.connectToDB(URL, username, password);
        
        test.populateDB();
        
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a child ID: ");
        int childIdInput = reader.nextInt();
        
        test.printChildReport(childIdInput);
        
        System.out.println("Enter a helper ID: ");
        int helperIdInput = reader.nextInt();
        
        test.printHelperReport(helperIdInput);
        
        test.disconnectToDB();
	}
	
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
			
		    System.out.println("Connecting to database...");
		    conn = DriverManager.getConnection(url, username, pass);

		    System.out.println("Creating database...");
		    stmt = conn.createStatement();
		    
		    ResultSet resultSet = conn.getMetaData().getCatalogs();

		    String databaseName = "";
		    //iterate each catalog in the ResultSet
		    while (resultSet.next()) {
		    	// Get the database name, which is at position 1
		    	databaseName = resultSet.getString(1);
		    }
		    resultSet.close();

	    	String sql = "DROP DATABASE " + database;
		    if (databaseName == database) {
			    stmt.executeUpdate(sql);
		    }
		    
		    sql = "CREATE DATABASE " + database;
		    stmt.executeUpdate(sql);
		    System.out.println("Database created successfully...");
		    conn.close();
		    
		    conn = DriverManager.getConnection(url + database, username, pass);
		    
		    // Creating the Child Table
		    String childTable = 
		    		"CREATE TABLE Children "
		    		+	"("
		    		+	"cid int NOT NULL PRIMARY KEY, "
		    		+	"name varchar(50) NOT NULL, "
		    		+	"address varchar(50) NOT NULL "
		    		+	");";
		    stmt.executeUpdate(childTable);
		    
		    // Creating the Child Table
		    String santaHelperTable = 
		    		"CREATE TABLE SantasLittleHelpers "
		    		+	"("
		    		+	"slhid int NOT NULL PRIMARY KEY, "
		    		+	"name varchar(50) NOT NULL "
		    		+	");";
		    stmt.executeUpdate(santaHelperTable);
		    
		    String giftTable = 
		    		"CREATE TABLE gifts "
		    		+	"("
		    		+	"gid int NOT NULL PRIMARY KEY, "
		    		+	"description varchar(50) NOT NULL "
		    		+	");";
		    stmt.executeUpdate(giftTable);
		    
		    String presentTable = 
		    		"CREATE TABLE presents "
		    		+	"("
		    		+	"gid int NOT NULL PRIMARY KEY, "
		    		+	"cid int NOT NULL, "
		    		+	"slhid int NOT NULL, "
		    		+	"FOREIGN KEY (gid) REFERENCES Gifts(gid), "
		    		+	"FOREIGN KEY (cid) REFERENCES Gifts(cid), "
		    		+	"FOREIGN KEY (slhid) REFERENCES Gifts(slhid), "
		    		+	");";
		    stmt.executeUpdate(presentTable);
		    
		 }catch(SQLException se){
		    //Handle errors for JDBC
			System.out.println("Hello");
		    se.printStackTrace();
		 }finally{
		    //finally block used to close resources
		    try{
		       if(stmt!=null)
		          stmt.close();
		    }catch(SQLException se2){
		    }// nothing we can do
		    try{
		       if(conn!=null)
		          conn.close();
		    }catch(SQLException se){
		       se.printStackTrace();
		    }//end finally try
		 }//end try
		 System.out.println("Goodbye!");
	}
	
	public void populateDB() {
		String[] names = new String[]{"Philip", "John", "Peter", "Tom", "Dixy", "Pepes", "Swiper", "Dora", "Troy", "Charlie", "Richard", "Harry", "Andrew", "Stephen", "Brandon", "William", "Julian", "Julie", "Rachel", "Ellie", "Debora", "Alan", "Mike", "Hannah", "David"};
		String[] addresses = new String[]{"Old Bristol Rd, East Brent, Highbridge TA9 4HT, UK", "Chepstow Hall, 29-31 Earl's Ct Square, Kensington, London SW5 9DB, UK", "Timothy House, 6 Kale Rd, Erith DA18 4BQ, UK", "27 Cleveland Gardens, London SW13 0AE, UK", "4 Bryn Siriol, Benllech, Tyn-y-Gongl LL74 8TZ, UK", "34 Foxdell Way, Derby DE73 6PU, UK", "8 Old Hall Rd, Bentley, Doncaster DN5 0DW, UK", "1 The Causeway, Worthing BN12 6FA, UK", "72 Gittin St, Oswestry SY11 1DS, UK", "5 Lulworth Rd, Caerleon, Newport NP18 1QE, UK", "110 Harewood Ave, Bournemouth BH7 6NS, UK", "1 Derby Square, Liverpool L2 9XX, UK", "1A Cornfield Terrace, Eastbourne BN21 4NN, UK", "23 Loanhead Ave, Grangemouth FK3 9EG, UK", "1 Oldacres, Maidenhead SL6 1XH, UK", "2 Trent Ln, East Bridgford, Nottingham NG13 8PF, UK", "3 Douglas Pier, Lochgoilhead, Cairndow PA24 8AE, UK", "Clippens Dr, Edinburgh EH17 8SQ, UK", "3 Earl's Ct, Fifth Ave, Gateshead NE11 0HF, UK", "42 Queensway, Worksop S81 0AD, UK", "26 Christopher St, Ince-in-Makerfield, Wigan WN3 4QY, UK", "Old Fen Bank, Wainfleet All Saints, Skegness PE24 4LE, UK", "7 Abingdon St, Barry CF63 2HQ, UK", "29 Lee Rd, Lynton EX35 6BS, UK", "York Way, Borehamwood WD6, UK", "18 Cavendish Ave, Saint Leonards-on-sea TN38 0NR, UK", "3 Vesty Rd, Bootle L30 1NY, UK", "12 New Square, London WC2A 3SW, UK", "5 Green Hill, Llandysul SA44 4BZ, UK", "4 Savoy Parade, Enfield EN1 1RT, UK", "38 Prey Heath Cl, Woking GU22 0SP, UK", "Unnamed Road, Tain IV20 1XJ, UK", "77 Trongate, Glasgow G1 5HB, UK", "5 Furness Grove, Halifax HX2 8NB, UK"};
		String[] descriptions = new String[]{};
		Random r = new Random();
		
		try {
			String sql;
			// Create 1000 Children
			for(int i=0; i<1000; i++) {
				int rnd = r.nextInt(names.length);
				int rnd2 = r.nextInt(addresses.length);
				sql = "INSERT INTO TABLE Children (name, address) VALUES (" + names[rnd] + ", " + addresses[rnd2] + ") ";
				stmt.executeUpdate(sql);
			}
			
			for(int i=0; i<100; i++) {
				int rnd = r.nextInt(names.length);
				sql = "INSERT INTO TABLE SantasLittleHelpers (name) VALUES (" + names[rnd] + ") ";
				stmt.executeUpdate(sql);
			}
			
			for(int i=0; i<10; i++) {
				int rnd = r.nextInt(descriptions.length);
				sql = "INSERT INTO TABLE Gifts (name) VALUES (" + descriptions[rnd] + ") ";
				stmt.executeUpdate(sql);
			}
			
			for(int i=0; i<1000; i++) {
				for(int j=r.nextInt(10); j<10; j++) {
					sql = "INSERT INTO TABLE Presents (gid, cid, slhid) VALUES (" + r.nextInt(10) + ", " + r.nextInt(1000) + ", " + r.nextInt(100) + ") ";
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
			String sql = "SELECT * FROM Children WHERE cid = " + childId;
			PreparedStatement query = conn.prepareStatement(sql);
			query.setInt(1, childId);
			ResultSet rs = query.executeQuery();
			String report = "";
			while(rs.next()) {
				String name = rs.getString("name");
				String address = rs.getString("address");
				
				report += "Child ID: " + childId + ", Name: " + name + ", Address: " + address + "\n";
				
				sql = "SELECT * FROM Gifts WHERE cid = " + childId;
				PreparedStatement query1 = conn.prepareStatement(sql);
				query1.setInt(1, childId);
				ResultSet rs1 = query1.executeQuery();
				while(rs1.next()) {
					report += "Gift -> Gift ID: " + rs1.getInt("cid") + ", Description: " + rs1.getString("description") + "\n";
				}
			}
			
			System.out.println(report);
		}
		catch(SQLException e) {
			System.out.println("Child with that ID does not exist");
		}
	}
	
	public void printHelperReport(int helperId) {
		try {
			HashMap<Integer, String> storage = new HashMap<Integer, String>();
			
			String sql = "SELECT * FROM SantasLittleHelpers WHERE slhid = " + helperId;
			PreparedStatement query = conn.prepareStatement(sql);
			query.setInt(1, helperId);
			ResultSet rs = query.executeQuery();
			String report = "";
			while(rs.next()) {
				String name = rs.getString("name");
				
				report += "Santas Little Helper ID: " + helperId + ", Name: " + name + "\n";
				
				sql = "SELECT * FROM Presents WHERE slhid = " + helperId;
				PreparedStatement query1 = conn.prepareStatement(sql);
				query1.setInt(1, helperId);
				ResultSet rs1 = query1.executeQuery();
				while(rs1.next()) {
					int cid = rs1.getInt("cid");
					int gid = rs1.getInt("gid");
					sql = "SELECT * FROM Children WHERE cid = " + cid;
					PreparedStatement query2 = conn.prepareStatement(sql);
					query2.setInt(1, cid);
					ResultSet rs2 = query1.executeQuery();
					String child = "";
					while(rs2.next()) {
						child = "Child ID: " + cid + ", Child Name: " + rs2.getString("name") + ", Child Address" + rs2.getString("address") + "\n";
					}
					
					sql = "SELECT * FROM Gifts WHERE gid = " + gid;
					PreparedStatement query3 = conn.prepareStatement(sql);
					query3.setInt(1, gid);
					ResultSet rs3 = query1.executeQuery();
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
			
			System.out.println(report);
		}
		catch(SQLException e) {
			System.out.println("Child with that ID does not exist");
		}
	}
	
	public void connectToDB(String url, String username, String pass) {

        System.out.println("Example showing connection to mod-intro-databases server");

        try {

            //Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found");
            System.exit(1);
        }

        System.out.println("PostgreSQL driver registered.");

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, pass);
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
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
	}
}
