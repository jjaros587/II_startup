/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logika;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 *
 * @author Jakub Jaroš
 */
public class Database {
    
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    
    private static final String DB_URL      = "jdbc:mysql://localhost/xxx";   
    private static final String USER        = "username";
    private static final String PASS        = "password";
    
    public static Connection con = null;
    
    public static Connection getInstance() {
        if (con == null) {
            getConnection(DB_URL, USER, PASS);
        }
        return con;
    }

    private static Connection getConnection(String DB_URL, String USER, String PASS) {
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return con;        
    }
    
}
