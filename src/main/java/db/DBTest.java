package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBTest {
    Connection conn;
    Statement stmt;
    ResultSet resultSet;

    public static void main(String[] args) {
        new DBTest().queryTest();
    }
    private void queryTest(){
        conn = DBConnection.getConnection();
        try {
            stmt = conn.createStatement();
            //System.out.println("Connected.");
            String sql = "select * from Product;";
            resultSet = stmt.executeQuery(sql);
            System.out.println("query answer:");
            System.out.println("productNo" + '\t' + "productName" + '\t' + "productClass" + '\t' +
                    "productPrice");
            while(resultSet.next()){
                String productNo = resultSet.getString("productNo");
                String productName = resultSet.getString("productName");
                String productClass = resultSet.getString("productClass");
                String productPrice = resultSet.getString("productPrice");
                //int inStock = resultSet.getInt("inStock");
                System.out.println(productNo + '\t' + productName + '\t' + productClass + '\t' +
                        productPrice);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            DBConnection.closeConnection(conn);
            DBConnection.closeStatement(stmt);
            DBConnection.closeResultSet(resultSet);
        }
    }
}
