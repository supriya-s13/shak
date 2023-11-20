package s231047002;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import java.sql.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class javaTestClass {
	public static void main(String args[])
	{	
		final Logger logger= LogManager.getLogger(javaTestClass.class);
		Connection con=null;
		 Scanner scanner = new Scanner(System.in);
		try {
			String connectionUrl = "jdbc:sqlserver://172.16.51.64;" +"databaseName=231047002;encrypt=true;trustServerCertificate=true;";
			Properties properties = new Properties();
			InputStream input = new FileInputStream("F:/7002 JAVA/java/dbpractice/src/SUP_231047002/jdbc.properties");
			properties.load(input);
			String username = properties.getProperty("username");
			String password = properties.getProperty("password");
		con = DriverManager.getConnection(connectionUrl,username,password);
		logger.info("Connection successful!");
		
		 // Get a string input from the user
        System.out.print("Enter a string: ");
        String userInput = scanner.nextLine();
        
        boolean isPalindrome = checkPalindrome(userInput);
        
        if (isPalindrome) {
            System.out.println("The entered string is a palindrome.");
        } else {
            System.out.println("The entered string is not a palindrome.");
        }
        
        insertIntoDatabase(con, userInput, isPalindrome);
        
        logger.info("Data inserted into the database!");
        
        displaySummary(con);
        

        } catch (Exception e) {
            System.out.println("Catching exception");
            logger.error("Connection unsuccessful", e);

        } finally {
            System.out.println("Entering finally block");
            try {
                if (!con.isClosed()) {
                    con.close();
                    System.out.println("Connection closed in Finally");
                }
            } catch (Exception e) {
                System.out.println("Exception in finally block");
            }
            scanner.close();
        }
    }
	  private static boolean checkPalindrome(String str) {
	        // Implement logic to check if the string is a palindrome
	        //String reversed = new StringBuilder(str).reverse().toString();
	       // return str.equals(reversed);
		 
		  String cleanStr = str.replaceAll("[^a-zA-Z]", "").toLowerCase();
	        String reversed = new StringBuilder(cleanStr).reverse().toString();
	        return cleanStr.equals(reversed);
	    }
	  private static void insertIntoDatabase(Connection con, String inputString, boolean isPalindrome) {
	        try {
	            // Prepare the SQL statement for insertion
	            String insertQuery = "INSERT INTO PalindromeTable (inputString, isPalindrome) VALUES (?, ?)";
	            try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
	                preparedStatement.setString(1, inputString);
	                preparedStatement.setBoolean(2, isPalindrome);

	                // Execute the insert statement
	                preparedStatement.executeUpdate();
	            }
	        } catch (Exception e) {
	            System.out.println("Error while inserting into the database");
	            e.printStackTrace();
	        }
	  }
	  
	  private static void displaySummary(Connection con) {
		    try {
		        // Select all records from the PalindromeTable
		        String selectQuery = "SELECT * FROM PalindromeTable";
		        try (Statement statement = con.createStatement();
		             ResultSet resultSet = statement.executeQuery(selectQuery)) {

		            System.out.println("Summary of Operations:");

		            // Display table header
		            System.out.println("+---------------------+---------------+");
		            System.out.printf("| %-20s | %-13s |\n", "Input String", "Is Palindrome");
		            System.out.println("+---------------------+---------------+");

		            // Iterate through the result set and print the summary in a table format
		            while (resultSet.next()) {
		                String inputString = resultSet.getString("inputString");
		                boolean isPalindrome = resultSet.getBoolean("isPalindrome");

		                // Display data in a table format
		                System.out.printf("| %-20s | %-13s |\n", inputString, (isPalindrome ? "Yes" : "No"));
		            }

		            System.out.println("+---------------------+---------------+");
		        }
		    } catch (Exception e) {
		        System.out.println("Error while displaying summary");
		        e.printStackTrace();
		    }
		}


}
	