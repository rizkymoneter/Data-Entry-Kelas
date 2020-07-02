/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Ouka
 */
public class Koneksi {
    public Connection getConnection(){
        Connection conn = null;
        try{
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("Found mate!");
            try{
                conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "db_rizky", "1");
		System.out.println("Connected!");
                return conn;
            } catch (SQLException e){
                System.out.println("Not connected!");
                return null;
            }
        } catch(ClassNotFoundException ex){
            System.out.println("Not Found Mate!" + ex);
            return null;
        }
    }
}
