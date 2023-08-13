/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modal;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author kasun
 */
public class MySQL {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/db_name", "root", "password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
