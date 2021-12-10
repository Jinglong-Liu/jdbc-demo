import db.DBHelper;
public class Main {
    public static void main(String[] args){
        new Main().start();
    }
    public void start(){
        DBHelper helper = new DBHelper();
        helper.queryEmployeeOrderBySalary(20);//4-1）查询职工工资按从高到低排序的前20的职工编号、职工姓名和工资。
        helper.insertCustomer();// 4-2）为客户表插入一条新的客户信息。
        helper.deleteEmployeeSalaryOver(5000);//4-3）删除员工表中薪水高于5000的员工信息。
        helper.halfPriceProductPriceOver(1000);//4-4）更新商品基本信息表中价格超过1000的商品价格变为原来的50%。
        helper.addSalaryByDepartment("业务科",200);//5-1）为“业务科”（作为外部输入参数）所有员工增加200的薪水。
        helper.queryCustomersWithCursor();//5-2）查询客户表中的客户名称、客户地址及客户电话并输出。（注：结合游标的使用）
    }
}
