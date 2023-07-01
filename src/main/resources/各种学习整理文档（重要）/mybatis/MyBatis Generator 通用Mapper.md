## MyBatis Generator 通用Mapper
### 1.pom.xml

```xml
	<properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java.version>1.8</java.version>
        <mapper.v>4.1.5</mapper.v>
        <mybatis-generator.v>1.3.7</mybatis-generator.v>
        <mysql-connector-java.v>5.1.47</mysql-connector-java.v>
    </properties>

    <dependencies>
        <dependency>
            <groupId>tk.mybatis</groupId>
            <artifactId>mapper</artifactId>
            <version>${mapper.v}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- mybatis-generator 逆向工程插件 -->
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>${mybatis-generator.v}</version>
                <configuration>
                    <configurationFile>
                        src/main/resources/generatorConfig.xml
                    </configurationFile>
                    <!--允许移动生成的文件 -->
                    <verbose>true</verbose>
                    <overwrite>true</overwrite>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>tk.mybatis</groupId>
                        <artifactId>mapper</artifactId>
                        <version>${mapper.v}</version>
                    </dependency>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>${mysql-connector-java.v}</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
```
### 2. generatorConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>

    <properties resource="application.properties"/>

    <!-- MyBatis3Simple：不生成 Example相关类及方法 defaultModelType="flat" -->
    <context id="MysqlContext" targetRuntime="MyBatis3Simple">

        <property name="javaFileEncoding" value="UTF-8"/>
        <property name="beginningDelimiter" value="`"/>
        <property name="endingDelimiter" value="`"/>

        <!-- 指定生成 Mapper 的继承模板 -->
        <plugin type="tk.mybatis.mapper.generator.MapperPlugin">
        	<!-- Mapper父接口 -->
            <property name="mappers" value="tk.mybatis.mapper.common.Mapper"/>
            <property name="forceAnnotation" value="true"/>
            <!-- caseSensitive默认false，当数据库表名区分大小写时，可以将该属性设置为true -->
            <!-- <property name="caseSensitive" value="true"/>-->
        </plugin>

        <commentGenerator>
            <property name="suppressDate" value="true"/>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>

        <!-- jdbc 连接配置 -->
        <jdbcConnection driverClass="${spring.datasource.driver-class-name}"
                        connectionURL="${spring.datasource.url}"
                        userId="${spring.datasource.username}"
                        password="${spring.datasource.password}">
            <property name="nullCatalogMeansCurrent" value="true"/>
        </jdbcConnection>

        <javaTypeResolver>
            <!-- 是否使用bigDecimal， false可自动转化以下类型（Long, Integer, Short, etc.） -->
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <!-- 生成实体类的包名和位置 ，targetPackage指的是包名,targetProject值得是路径位置-->
        <!-- 生成的pojo所在包,pojo其实就是Domain Entity-->
        <javaModelGenerator 
                      targetPackage="${package.pojo}" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="false"/>
        </javaModelGenerator>

        <!-- mapper.xml所在目录 -->
        <sqlMapGenerator 
                   targetPackage="${package.xml}" targetProject="src/main/resources"/>

        <!-- mapper接口(dao层) -->
        <javaClientGenerator targetPackage="${package.dao}" targetProject="src/main/java" 								type="XMLMAPPER"/>

        <!-- 全部表 -->
        <table tableName="%"></table>
    </context>

</generatorConfiguration>
```
### 3.application.properties

```properties
####
spring.datasource.driver-class-name = com.mysql.jdbc.Driver
spring.datasource.url = jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8
spring.datasource.username = root
spring.datasource.password = 1234
####
package.pojo = me.pojo
package.dao = me.mapper
package.xml = me.mapper
```
### 4. 运行

```properties
mvn mybatis-generator:generate
```

