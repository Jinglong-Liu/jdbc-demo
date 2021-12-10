package db;
import java.sql.*;

public class DBConnection {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //static final String JDBC_DRIVER = com.mysql.cj.jdbc.Driver;
    static final String DB_URL = "jdbc:mysql://localhost:3306/OrderDB?characterEncoding=utf8&useSSL=false";
    private static String userName = "root";
    private static String password = "root";
    private static String dbName = "OrderDB";
    private static Connection connection = null;

    /**
     * Create a new Connection
     * @return
     */
    public static Connection getConnection() {
        if(connection != null){
            return connection;
        }
        Connection con = null;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_URL, userName, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return con;
    }
    /**
     *
     * @param rs
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param stm
     */
    public static void closeStatement(Statement stm) {
        if (stm != null) {
            try {
                stm.close();
                stm = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param pstm
     */
    public static void closePreparedStatement(PreparedStatement pstm) {
        if (pstm != null) {
            try {
                pstm.close();
                pstm = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param con
     */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            con = null;
        }
    }

}
