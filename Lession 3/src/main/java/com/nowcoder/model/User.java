package com.nowcoder.model;

/**
 * Created by nowcoder on 2016/6/26.
 */
//为什么要用这个模型呢,1.在模板中使用该对象；2.将数据库的user表中的内容传过来，User类实际上就是数据库中数据传到模板中的中间容器；3.实际上model沟通三层dao-service-controller
public class User {
    private int id;
    private String name;
    private String password;
    private String salt;
    private String headUrl; //mysql wenda.user表中的对应col name实际上是head_url,这里变成headUrl实际上是在mybatis-config.xml中的：
                            //<setting name="mapUnderscoreToCamelCase" value="true"/> 这句话中改变的，就是把mysql中列名为A_COLUMN(mysql不区分大小写) 的映射成java?中的 aColumn

    public User() {

    }
    public User(String name) {
        this.name = name;
        this.password = "";
        this.salt = "";
        this.headUrl = "";
    }
    //先把一些非空的数据初始化了

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
