import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

//随便加了一行注释测试vscode与git,我也不知道成没成功？？？？？？？？？（后一句话是第二次修改）
class Main{
    // 异常处理
    public static void demoException() {
        try {
            int k = 2/1;
            if(k == 2){
                throw new Exception("I meant to");
            }else{
                k /= 0;
            }
        } catch (Exception e) {
            e.getMessage();
            //TODO: handle exception
        }
        finally{
            System.out.println("Just OK.I will take it");
        }
    }

    //以下是数据结构（容器的应用）

    //List 与ArrayList
    public static void demoList() {
        //这里的list导入的是java.util.List;而非java.awt.List
        //引用的是小容器list，创建的是大容器arraylist
        List<String> strList = new ArrayList<String>(10);
        for (int i = 0;i < 4;++i){
            strList.add(String.valueOf(i*i));
        }
        System.out.println(strList);
        List<String> strListB = new ArrayList<String>();
        for (int i = 0;i < 5;++i){
            strListB.add(String.valueOf(i));
        }
        System.out.println(strListB);
        //将strlistB加入strlist
        strList.addAll(strListB);
        System.out.println(strList);

        //翻转strlist
        Collections.reverse(strList);
        System.out.println(strList);
        //排序strlist
        Collections.sort(strList);
        System.out.println(strList);
        //排序的高级版本?????还不会，待解决
        // Collections.sort(strList, new Comparator<String>() {
        // });
    }

    //Map与Hashtable
    public static void demoMapTable() {
        Map<String,String> map = new HashMap<String,String>();
        for (int i = 0;i < 4;++i){
            map.put(String.valueOf(i), String.valueOf(i*i));
        }
        //map格式的键值对
        System.out.println(map);
        //set格式的键值对
        System.out.println(map.entrySet());
        for (Map.Entry<String,String> entry:map.entrySet()){
            System.out.println(entry);
        }
        //常用的键值方法（map方法）
        System.out.println(map.values());
        System.out.println(map.keySet());
        System.out.println(map.get("2"));
        System.out.println(map.containsKey("3"));
    }
    //set与Hashset
    public static void demoSet() {
        Set<String> strSet = new HashSet<String>();
        for (int i =0;i < 3;++i){
            strSet.add(String.valueOf(i));
            strSet.add(String.valueOf(i));
            strSet.add(String.valueOf(i));
            //set中元素不能相同
        }
        System.out.println(strSet);
        //常用set方法
        strSet.remove("1");
        System.out.println(strSet);
        System.out.println(strSet.contains("1"));
        System.out.println(strSet.isEmpty());
        System.out.println(strSet.size());
        //Arrays的静态方法asList
        strSet.addAll(Arrays.asList(new String[] {"A","B","C"}));
        System.out.println(strSet);
        //string数组的地址，不显示，只能用list或者引用具体化??????
        System.out.println(new String[] {"A","B","C"});
    }

    //java 的接口 interface,不做任何具体实现，封装，集成多态，接口.java也在同一个目录下，子类（.java）也在该目录下，也可以是另一个文件，其他该目录下的.java文件都可以创建、引用该对象）

    //java中常用的类
    public static void demoFunction() {
        //随机数(伪随机) Random 类
        Random random = new Random();
        //setseed:固定的随机数
        random.setSeed(2);
        System.out.println(random.nextInt(1000));
        System.out.println(random.nextInt(1000));
        System.out.println(random.nextInt(1000));
        System.out.println(random.nextInt(1000));
        System.out.println(random.nextFloat());


        //时间类 Date
        Date date = new Date();
        System.out.println(date);
        System.out.println(date.getTime());
        //时间格式类DateFormat
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        System.out.println(df1.format(date));
        System.out.println(df2.format(date));

        //随机字符串
        System.out.println(UUID.randomUUID());
        //数学方法
        System.out.println(Math.log10(10));
        System.out.println(Math.ceil(7.9));
        System.out.println(Math.floor(11.23));
        System.out.println(Math.sqrt(6));
        System.out.println(Math.min(2,3));
    }

    public static void main(String[] args) {
        //demoException();
        //demoList();
        //demoMapTable();
        //demoSet();
        demoFunction();
    }
}