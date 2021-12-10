package db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DBHelperTest {
    DBHelper helper;
    @Before
    public void setUp() throws Exception {
        helper = new DBHelper();
    }

    @After
    public void tearDown() throws Exception {

    }
    /**
     *4-1）查询职工工资按从高到低排序的前20的职工编号、职工姓名和工资；
     */
    @Test
    public void queryEmployeeOrderBySalary() {
        String queryEmployeeSQL = "select * from Employee;";
        List<List<String>>q1 = helper.getValues(queryEmployeeSQL);//Before call method.
        ResultSet resultSet = helper.queryEmployeeOrderBySalary(20);//call method.
        List<List<String>>result = helper.getValues(resultSet);//result of the method.
        assertTrue(result.size() - 1 <= 20);//result.get(0) is colName.
        for(int i = 1;i<result.size() - 1;i++){
            double salary1 = Double.parseDouble(result.get(i).get(2));
            double salary2 = Double.parseDouble(result.get(i+1).get(2));
            assertTrue(salary1 >= salary2);//Test order desc.
        }
        List<List<String>>q2 = helper.getValues(queryEmployeeSQL);//After call method.
        assertEquals(q1.size(),q2.size());
        for(int i = 0;i < q1.size();i++){
            assertArrayEquals(q1.get(i).toArray(),q1.get(i).toArray());//Assert the method didn't change the table.
        }
    }

    /**
     * 4-2）为客户表插入一条新的客户信息，
     * 客户编号“C20080002”，客户名称“泰康股份有限公司”，客户电话“010-5422685”，客户地址“天津市”，客户邮编“220501”；
     */
    @Test
    public void insertCustomer() {
        helper.dropCustomer();//if contains the customer,delete it.
        String sql = "select * from Customer where customerNo = \'C20080002\';";
        List<List<String>>result1 = helper.getValues(sql);
        assertEquals(result1.size(),1);//Assert no customerNo = C20080002;

        helper.insertCustomer();//calling method.

        List<List<String>>result2 = helper.getValues(sql);//after calling the method.
        String[] expect2 = new String[]{"C20080002","泰康股份有限公司","010-5422685","天津市","220501"};

        assertArrayEquals(result2.get(1).toArray(),expect2);
    }

    /**
     * 4-3）删除员工表中薪水高于5000的员工信息；
     */
    @Test
    public void deleteEmployeeSalaryOver() {
        String insertSQL = "insert Employee values('E2005001','喻自强','M','19650415','南京市青海路18号','13817605008', '19900206','财务科','科长',5800);";
        String sql = "select * from Employee where salary > 5000";
        List<List<String>>result1 = helper.getValues(sql);
        if(result1.size() == 1){
            helper.getSqlExecutor().update(insertSQL);//If has no employee's salary > 5000,insert an employee.
        }
        result1 = helper.getValues(sql);
        assertNotEquals(result1.size(),1);//Before calling method:Contains at least one row.

        helper.deleteEmployeeSalaryOver(5000);//Calling method.

        result1 = helper.getValues(sql);    //After calling method.
        assertEquals(result1.size(),1);//Only contains colName.
        helper.displayTable("Employee");//display the employee table.
    }

    /**
     * 4-4）更新商品基本信息表中价格超过1000的商品价格变为原来的50%；
     */
    @Test
    public void halfPriceProductPriceOver() {
        String querySQL = "select productNo,productPrice from product";
        List<List<String>>result1 = helper.getValues(querySQL);//Before call method.
        helper.displayTable("Product");//display the product table before calling.

        helper.halfPriceProductPriceOver(1000);//Calling method.

        List<List<String>>result2 = helper.getValues(querySQL);//After call method.

        assertEquals(result1.size(),result2.size());
        for(int i = 1;i < result1.size();i++){
            double price1 = Double.parseDouble(result1.get(i).get(1));
            double price2 = Double.parseDouble(result2.get(i).get(1));
            if(price1 > 1000){
                assertEquals(price1/2,price2,0.01); //if original price > 100,half it.
            }
            else{
                assertArrayEquals(result1.get(i).toArray(),result2.get(i).toArray());//else assert equals.
            }
        }
        helper.displayTable("Product");//display the product table after calling.
    }

    /**
     * 5-1）为“业务科”（作为外部输入参数）所有员工增加200的薪水。
     */
    @Test
    public void addSalaryByDepartment() {
        helper.displayTable("Employee");//display table before calling method.
        String querySQL = "select * from employee";
        List<List<String>> result1 = helper.getValues(querySQL);//before calling method.

        helper.addSalaryByDepartment("业务科",200);//calling method.

        List<List<String>> result2 = helper.getValues(querySQL);//after calling method.
        assertEquals(result1.size(),result2.size());
        //assert column name,index
        assertEquals("department",result1.get(0).get(7).toLowerCase());
        assertEquals("salary",result1.get(0).get(9).toLowerCase());
        for(int i = 1;i<result1.size();i++){
            if("业务科".equals(result1.get(i).get(7))){//department
                double salary1 = Float.parseFloat(result1.get(i).get(9));
                double salary2 = Float.parseFloat(result2.get(i).get(9));
                assertEquals(salary1 + 200,salary2,0.01);//add salary 200
            }
            else{
                assertArrayEquals(result1.get(i).toArray(),result2.get(i).toArray());
            }
        }
        helper.displayTable("Employee");//display table after calling method.
    }

    /**
     * 5-2）查询客户表中的客户名称、客户地址及客户电话并输出。（注：结合游标的使用）
     */
    @Test
    public void queryCustomersWithCursor(){
        helper.queryCustomersWithCursor();
    }
}