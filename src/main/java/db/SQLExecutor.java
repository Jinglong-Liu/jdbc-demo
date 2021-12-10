package db;

import java.sql.*;

public class SQLExecutor {
    Connection conn;
    /**
     * Create and a db connection.
     */
    public SQLExecutor(){
        conn = DBConnection.getConnection();
    }
    /**
     * execute an sql query command
     * @param sql
     * @return resultSet
     */
    public ResultSet query(String sql){
        ResultSet resultSet = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    /**
     * execute an sql update command
     * @param sql
     */
    public void update(String sql){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * call procedure.
     * @param procedure:procedure,like 'f()','g(1,2)',etc.
     * @return resultSet
     */
    public ResultSet call(String procedure){
        ResultSet resultSet = null;
        try {
            PreparedStatement stmt = conn.prepareCall("{CALL " + procedure + "}");
            stmt = conn.prepareStatement("{CALL " + procedure + "}",ResultSet.CONCUR_READ_ONLY);
            resultSet = stmt.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }
    public void dynamicUpdate(String sql,String []args){
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            for(int i = 0;i< args.length;i++){
                stmt.setString(i+1,args[i]);
            }
            stmt.execute();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
    public ResultSet dynamicQuery(String sql,String []args){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            for(int i = 0;i< args.length;i++){
                stmt.setString(i+1,args[i]);
            }
            resultSet = stmt.executeQuery();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet queryWithCursor(String sql,int fetchSize){
        ResultSet resultSet = null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setFetchSize(fetchSize);
            resultSet = stmt.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }
}
