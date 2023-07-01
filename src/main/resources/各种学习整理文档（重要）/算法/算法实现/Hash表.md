# 				Hash表

## 1、 哈希表的基本介绍 

散列表（Hash table，也叫哈希表），是根据关键码值(Key value)而直接进行访问的数据结构。也就是说，它通 

过把关键码值映射到表中一个位置来访问记录，以加快查找的速度。这个映射函数叫做散列函数，存放记录的数组 

叫做散列表。

![](D:\java oracle\学习资料\各种学习整理文档（重要）\算法\算法实现\photos\已hash作为缓存结构.PNG)





![](D:\java oracle\学习资料\各种学习整理文档（重要）\算法\算法实现\photos\数组+链表的hash结构.png)





## 2、 google 公司的一个上机题: 

有一个公司,当有新的员工来报道时,要求将该员工的信息加入(id,性别,年龄,名字,住址..),当输入该员工的 id 时, 

要求查找到该员工的 所有信息. 

**要求**: 

1) 不使用数据库,,速度越快越好=>哈希表(散列) 

2) 添加时，保证按照 id 从低到高插入 [课后思考：**如果** **id** **不是从低到高插入**，但要求各条链表仍是从低到 

高，怎么解决?] 

3) 使用链表来实现哈希表, 该链表不带表头[即: 链表的第一个结点就存放雇员信息] 

4) 思路分析并画出示意图 

![](D:\java oracle\学习资料\各种学习整理文档（重要）\算法\算法实现\photos\自己写hash表思路.PNG)



5、代码实现

```
package com.algorithm;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.util.Scanner;

public class HashTabDemo {
    public static void main(String[] args) {

        HashTab hashTab = new HashTab(7);
        Scanner scanner = new Scanner(System.in);
        String key="";

        while (true) {
            System.out.println("add: 添加雇员");
            System.out.println("list: 显示雇员");
            System.out.println("find: 查找雇员");
            System.out.println("del:删除雇员");
            System.out.println("exit: 退出系统");

            key=scanner.next();

            switch (key) {
                case "add":
                    System.out.println("输入员工id:");
                    int id=scanner.nextInt();
                    System.out.println("输入员工姓名：");
                    String name = scanner.next();
                    Emp emp = new Emp(id, name, null);
                    hashTab.add(emp);
                    break;
                case "list":
                    hashTab.list();
                    break;
                case "find":
                    System.out.println("请输入雇员id");
                    id=scanner.nextInt();
                    hashTab.findEmpById(id);
                    break;

                case "del":
                    System.out.println("请输入要删除的雇员的id");
                    id=scanner.nextInt();
                    hashTab.deleteByEmpId(id);
                    break;
                case "exit":
                    scanner.close();
                    System.exit(0);
                default:
                    break;
            }

        }

    }


}

class HashTab{
    private EmpLinkedList[] empLinkedArr; //内部维护的数组

    private int size; //数组大小

    public HashTab(int size){
        empLinkedArr=new EmpLinkedList[size];
        this.size=size;
        //初始化链表
        for (int i = 0; i <empLinkedArr.length ; i++) {
            empLinkedArr[i]=new EmpLinkedList();
        }
    }

    public void add(Emp emp) {
        if (findEmpById(emp.id) != null) {
            System.out.printf("id为%d的员工已经存在~~",emp.id);
        }else {
            int num = hashFun(emp.id);
            empLinkedArr[num].add(emp);
            System.out.println("添加成功！！！");
        }
    }

    public Emp findEmpById(int id){
        int num=hashFun(id);

        Emp empById = empLinkedArr[num].findEmpById(id);
        if (empById == null) {
            System.out.printf("id为%d的员工不存在~~",id);
        }else {
            System.out.printf("员工id=%d,姓名：%s",id,empById.name);
        }

        return empById;

    }

    public void deleteByEmpId(int id) {
        int num=hashFun(id);
        empLinkedArr[num].deleteById(id);
    }

    public void list(){
        for (int i = 0; i <empLinkedArr.length ; i++) {
            empLinkedArr[i].list(i);
        }
    }

    /**
     * 已取模的方式得到数组
     * @param id  员工id
     * @return 数组下标
     */
    public  int hashFun(int id) {
        return id%size;
    }





}




class Emp{
    int id;

    String name;

    String address;

    Emp next;

    public Emp(int id, String name, String address) {
        super();
        this.id=id;
        this.name=name;
        this.address=address;
    }
}
class EmpLinkedList{
    private Emp head;  //默认为null

    /**
     * 按照emp的id大小从小到大排序
     * @param emp 新加的节点
     */

    public void add(Emp emp) {
        if (head == null) {
            head=emp;
            return;
        }

        Emp curEmp =head;

        //如果头结点的id大于添加的emp的id。将要添加的emp赋予头指针
        if (curEmp.id > emp.id) {
            emp.next=head;
            head=emp;
        }else {
        //否则
            while (true) {
                //如果最后一个节点不大于新加的emp.将emp添加到末尾
                if (curEmp.next==null){
                    curEmp.next=emp;
                    break;
                }

                //如果临时指针指向的后一个节点的id大于新加的emp,将新节点加在临时指针和后一个节点之间
                if (curEmp.next.id > emp.id) {
                    emp.next=curEmp.next;
                    curEmp.next=emp;
                    break;
                }

                //临时指针往后移动
                curEmp=curEmp.next;
            }

        }

    }

    public Emp findEmpById(int id) {

        Emp temp=head;

        while (true) {
            if (temp==null) break;

            if (temp.id == id) {
                return temp;
            }

            temp=temp.next;
        }

        return temp;
    }

    public void deleteById(int id) {
        Emp empById = findEmpById(id);
        if (empById == null) {
            System.out.printf("id为%d的员工不存在~~~",id);
            return;
        }

        Emp curEmp=head;

        //如果头结点的id大==emp的id。头节点置null
        if (curEmp.id == id) {
            head=head.next;
            return;
        }else {
            //否则
            while (true) {
                //如果最后一个节点id不等于id.退出
                if (curEmp.next==null){
                    break;
                }

                //如果临时指针指向的后一个节点的id==新加的emp,将新节点加在临时指针和后一个节点之间
                if (curEmp.next.id == id) {
                    curEmp.next=curEmp.next.next;
                    break;
                }

                //临时指针往后移动
                curEmp=curEmp.next;
            }

        }




    }



    /**
     * 遍历某一条链表
     * @param num
     */
    public void list(int num) {
        if (head == null) {
            System.out.printf("第%d条链表为空~~",num);
            System.out.println("");
            return;
        }

        System.out.printf("第%d条链表：",num);
        //临时节点
        Emp tempNode=head;

        while (true){
            System.out.printf("id为%d,名字为%s==>  ",tempNode.id,tempNode.name);

            if (tempNode.next == null) {
                break;
            }

            tempNode=tempNode.next;

        }
        System.out.println("");
    }


}
```