package db;

import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper {
    private SQLExecutor sqlExecutor = new SQLExecutor();

    public SQLExecutor getSqlExecutor() {
        return sqlExecutor;
    }

    /**
     *
     * @param limit
     * 1）查询职工工资按从高到低排序的前20的职工编号、职工姓名和工资；
     * @return resultSet
     */
    public ResultSet queryEmployeeOrderBySalary(int limit){
        String sql = "select employeeNo,employeeName,salary " +
                "from Employee order by salary desc limit " +
                limit+";";
        ResultSet resultSet = sqlExecutor.query(sql);
        outputResultSet(resultSet,System.out);
        
        return resultSet;
    }
    /**
     * 2）为客户表插入一条新的客户信息，
     * 客户编号“C20080002”，客户名称“泰康股份有限公司”，客户电话“010-5422685”，客户地址“天津市”，客户邮编“220501”；
     * insert a customer.
     */
    public void insertCustomer(){
        dropCustomer();
        String sql = "insert Customer values('C20080002','泰康股份有限公司',  '010-5422685',  '天津市', '220501');";
        sqlExecutor.update(sql);
        System.out.println("插入数据成功");
    }

    public void dropCustomer(){
        String sql = "delete from Customer where customerNo = 'C20080002';";
        sqlExecutor.update(sql);
        //System.out.println("删除数据成功");
    }

    /**
     * 3）删除员工表中薪水高于5000的员工信息；
     * @param salaryLimit :the limit of salary.
     * delete employee whose salary over limit.
     * sql = delete from Employee where salary > salaryLimit;
     */
    public void deleteEmployeeSalaryOver(int salaryLimit){
        String sql = "delete from Employee where salary > " +salaryLimit +";";
        sqlExecutor.update(sql);
        System.out.println("删除数据成功");
    }

    /**
     * 4）更新商品基本信息表中价格超过1000的商品价格变为原来的50%；
     * half the price if price over the limit.
     * sql = update Product set productPrice = 0.5 * productPrice where productPrice > limit"
     * @param priceLimit:limit of price to half.
     */
    public void halfPriceProductPriceOver(int priceLimit){
        String sql = "update Product set productPrice = 0.5 * productPrice where productPrice > "+ priceLimit+ ";";
        sqlExecutor.update(sql);
        System.out.println("修改数据成功");
    }

    /**
     * out put resultSet format.
     * @param resultSet
     * @param out:PrintStream,like "System.out".
     */
    public void outputResultSet(ResultSet resultSet, PrintStream out){
        List<String> colName = new ArrayList<>();
        List<Integer>colSize = new ArrayList<>();
        ResultSetMetaData rsMetaData = null;
        int count = 0;
        try {
            rsMetaData = resultSet.getMetaData();
            count = rsMetaData.getColumnCount();
            for(int i = 1; i<=count; i++) {

                String name = rsMetaData.getColumnName(i);
                int length = Math.max(rsMetaData.getColumnDisplaySize(i),name.length());
                colSize.add(length);
                colName.add(String.format("%-"+length+"s\t",rsMetaData.getColumnName(i)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for(String str:colName){
            out.print(str);
        }
        out.println();
        out.println("--------------------------");
        //
        int row = 0;
        while(true){
            try {
                if (!resultSet.next()) break;
                for(int i = 1;i<=count;i++){
                    int length = colSize.get(i-1);
                    out.print(String.format("%-"+length+"s\t",resultSet.getString(i)));
                }
                out.println();
                row++;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        out.println("--------------------------");
        if(row > 0){
            out.println("Query OK. " + row + " rows");
        }
        else{
            out.println("Empty Set.");
        }
        out.println("--------------------------");
    }

    public void printResultSet(ResultSet resultSet){
        outputResultSet(resultSet, System.out);
    }
    /**
     * 5-1）为“业务科”（作为外部输入参数）所有员工增加200的薪水。
     * @param department the employee's department.
     * @param increase:the salary increase.
     */
    public void addSalaryByDepartment(String department,int increase){
        String sql = "update Employee set salary = salary + ? where department = ?;";
        String[] args = new String[2];
        args[0] = String.valueOf(increase);
        args[1] = department;
        sqlExecutor.dynamicUpdate(sql,args);
        System.out.println("修改数据成功");
    }
    /**
     * 5-2）查询客户表中的客户名称、客户地址及客户电话并输出。（注：结合游标的使用）
     */
    public ResultSet queryCustomersWithCursor(){
        String sql = "select customerName,address,telephone from Customer";
        ResultSet resultSet = sqlExecutor.queryWithCursor(sql,1);
        outputResultSet(resultSet,System.out);
        return resultSet;
    }
    /*********************************************************************************************
     * helper methods.
     */
    /**
     * Display whole table. Sql = select * from tableName;
     * @param tableName
     */
    public void displayTable(String tableName){
        String sql = "select * from " + tableName + ";";
        ResultSet resultSet = sqlExecutor.query(sql);
        outputResultSet(resultSet,System.out);
    }

    /**
     * @param sql
     * @return return first line(not contains the colName) if result is not empty,
     *          or return the empty String array ([])
     */
    public String[] getValue(String sql){
        //ResultSet resultSet = sqlExecutor.dynamicQuery(sql,args);
        ResultSet resultSet = sqlExecutor.query(sql);
        String[] result = new String[0];
        try {
            if(!resultSet.next()){
                //empty
            }
            else{
                ResultSetMetaData rsMetaData = resultSet.getMetaData();
                int count = rsMetaData.getColumnCount();
                result = new String[count];
                for(int i = 1;i<=count;i++){
                    result[i-1] = resultSet.getString(i);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param sql
     * @return getValues(resultSet)
     */
    public List<List<String>>getValues(String sql){
        ResultSet resultSet = sqlExecutor.query(sql);
        return getValues(resultSet);
    }

    /**
     * return whole result to list<list<String>>.The first line is ColName,so travel begin with i = 1.
     * @param resultSet
     * @return
     */
    public List<List<String>>getValues(ResultSet resultSet){
        List<List<String>>result = new ArrayList<>();
        ResultSetMetaData rsMetaData = null;
        int count = 0;
        result.add(new ArrayList<>());
        try {
            rsMetaData = resultSet.getMetaData();
            count = rsMetaData.getColumnCount();
            for(int i = 1; i<=count; i++) {
                result.get(0).add(rsMetaData.getColumnName(i));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //
        int row = 1;
        while(true){
            try {
                if (!resultSet.next()) break;
                result.add(new ArrayList<>());
                for(int i = 1;i<=count;i++){
                    result.get(row).add(resultSet.getString(i));
                }
                row++;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return result;
    }
}
