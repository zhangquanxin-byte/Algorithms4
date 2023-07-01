##  MyBatis Generator 使用

# 1.pom.xml
```xml-dtd
		<properties>
            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
            <java.version>1.8</java.version>
            <log4j-version>1.2.17</log4j-version>
            <slf4j-version>1.7.26</slf4j-version>
            <mybatis.generator.version>1.3.7</mybatis.generator.version>
        </properties>

        <dependencies>

            <dependency>
                <groupId>log4j</groupId>
                <artifactId>log4j</artifactId>
                <version>${log4j-version}</version>
            </dependency>

            <dependency>
                <groupId>org.slf4j</groupId>
                <artifactId>slf4j-api</artifactId>
                <version>${slf4j-version}</version>
            </dependency>

            <dependency>
                <groupId>org.slf4j</groupId>
                <artifactId>slf4j-log4j12</artifactId>
                <version>${slf4j-version}</version>
            </dependency>

            <!-- mybatis-generator-core -->
            <dependency>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-core</artifactId>
                <version>${mybatis.generator.version}</version>
            </dependency>

        </dependencies>

        <build>
            <plugins>
                <!-- mybatis-generator插件，自动生成代码 -->
                <!-- Goals:mybatis-generator:generate -->
                <plugin>
                    <groupId>org.mybatis.generator</groupId>
                    <artifactId>mybatis-generator-maven-plugin</artifactId>
                    <version>${mybatis.generator.version}</version>
                    <configuration>
                        <configurationFile>
							${project.basedir}/src/main/resources/mbgConfiguration.xml
						</configurationFile>
                        <verbose>true</verbose>
                        <overwrite>true</overwrite>
                    </configuration>
                </plugin>
            </plugins>
        </build>
```

# 2.mbgConfiguration.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <!--导入属性配置 -->
    <properties resource="db.properties"/>

    <!-- 配置mysql 驱动jar包路径 用了绝对路径 -->
    <classPathEntry location="${db.driver-location}"/>

    <context id="my_tables" targetRuntime="MyBatis3">
    
        <!-- 为了防止生成的代码中有很多注释，比较难看，加入下面的配置控制 -->
        <commentGenerator>
            <property name="suppressAllComments" value="true"/>
            <property name="suppressDate" value="true"/>
        </commentGenerator>

        <!-- 数据库连接 -->
        <jdbcConnection driverClass="${db.driverClassName}" 
        				connectionURL="${db.url}" 
        				userId="${db.username}"
                        password="${db.password}">
        </jdbcConnection>

        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <!-- model层 -->
        <javaModelGenerator targetPackage="me.pojo" targetProject="src/main/java">
            <!-- 是否允许子包 -->
            <property name="enableSubPackages" value="false"/>
            <!-- 是否对modal添加构造函数 -->
            <property name="constructorBased" value="false"/>
            <!-- 是否清理从数据库中查询出的字符串左右两边的空白字符 -->
            <property name="trimStrings" value="false"/>
            <!-- 建立modal对象是否不可改变 即生成的modal对象不会有setter方法，只有构造方法 -->
            <property name="immutable" value="false"/>
        </javaModelGenerator>

        <!-- mapper文件 -->
        <sqlMapGenerator targetPackage="me.mapper" targetProject="src/main/resources">
            <property name="enableSubPackages" value="false"/>
        </sqlMapGenerator>

        <!-- mapper接口 -->
        <javaClientGenerator targetPackage="me.mapper" targetProject="src/main/java" 									type="XMLMAPPER">
            <property name="enableSubPackages" value="false"/>
        </javaClientGenerator>

        <!-- 要操作哪些表,必须有一个 -->
        <table 	tableName="admin" domainObjectName="Admin" 
        		enableCountByExample="false" enableUpdateByExample="false"
               	enableDeleteByExample="false" enableSelectByExample="false"
               	selectByExampleQueryId="false">
            <!-- 返回自增主键 -->
            <generatedKey column="id" sqlStatement="JDBC"/>
        </table>
        <table 	tableName="area" domainObjectName="Area" 
        		enableCountByExample="false" enableUpdateByExample="false"
               	enableDeleteByExample="false" enableSelectByExample="false" 
               	selectByExampleQueryId="false">
        </table>

    </context>

</generatorConfiguration>
```

##  3.db.properties

```properties
db.driverClassName=com.mysql.jdbc.Driver
db.url=jdbc:mysql://localhost:3366/test?useUnicode=true&characterEncoding=utf8
db.username=root
db.password=1234
db.driver-location=E:/Program/apache-maven-repo-3.6.0/mysql/mysql-connector-java/5.1.47/mysql-connector-java-5.1.47.jar
```


## 4.运行
### 4.1 main方法

```java
	public static void main(String[] args) throws Exception {
		List<String> warnings = new ArrayList<>();
		InputStream in = Main.class.getResourceAsStream("/mbgConfiguration.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(in);
		DefaultShellCallback callback = new DefaultShellCallback(true);
		MyBatisGenerator myBatisGenerator = 
            						new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
	}
```



### 4.2 maven

```properties
mvn mybatis-generator:generate
```

