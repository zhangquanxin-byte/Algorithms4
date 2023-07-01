# 					JAVA8 Stream



## 1 Stream概述

Java 8 是一个非常成功的版本，这个版本新增的Stream，配合同版本出现的 Lambda ，给我们操作集合（Collection）提供了极大的便利。

那么什么是Stream？

Stream将要处理的元素集合看作一种流，在流的过程中，借助Stream API对流中的元素进行操作，比如：筛选、排序、聚合等。

Stream可以由数组或集合创建，对流的操作分为两种：

中间操作，每次返回一个新的流，可以有多个。
终端操作，每个流只能进行一次终端操作，终端操作结束后流无法再次使用。终端操作会产生一个新的集合或值。
另外，Stream有几个特性：

stream不存储数据，而是按照特定的规则对数据进行计算，一般会输出结果。
stream不会改变数据源，通常情况下会产生一个新的集合或一个值。
stream具有延迟执行特性，只有调用终端操作时，中间操作才会执行。



## 1、创建stream的方式

1.1 使用Collection下的 stream() 和 parallelStream() 方法

```
List<String> list = new ArrayList<>();
Stream<String> stream = list.stream(); //获取一个顺序流
Stream<String> parallelStream = list.parallelStream(); //获取一个并行流
```


1.2 使用Arrays 中的 stream() 方法，将数组转成流

```
Integer[] nums = new Integer[10];
Stream<Integer> stream = Arrays.stream(nums);
```


1.3 使用Stream中的静态方法：of()、iterate()、generate()

```
Stream<Integer> stream = Stream.of(1,2,3,4,5,6);

Stream<Integer> stream2 = Stream.iterate(0, (x) -> x + 2).limit(6);
stream2.forEach(System.out::println); // 0 2 4 6 8 10

Stream<Double> stream3 = Stream.generate(Math::random).limit(2);
stream3.forEach(System.out::println);
```


1.4 使用 BufferedReader.lines() 方法，将每行内容转成流

```
BufferedReader reader = new BufferedReader(new FileReader("F:\\test_stream.txt"));
Stream<String> lineStream = reader.lines();
lineStream.forEach(System.out::println);
```


1.5 使用 Pattern.splitAsStream() 方法，将字符串分隔成流

```
Pattern pattern = Pattern.compile(",");
Stream<String> stringStream = pattern.splitAsStream("a,b,c,d");
stringStream.forEach(System.out::println);
```



##  2、流的中间操作

### 2.1 筛选与切片

​	    filter：过滤流中的某些元素
​        limit(n)：获取n个元素
​        skip(n)：跳过n元素，配合limit(n)可实现分页
​        distinct：通过流中元素的 hashCode() 和 equals() 去除重复元素

​		concat:合并两个流    

```
Stream<Integer> stream = Stream.of(6, 4, 6, 7, 3, 9, 8, 10, 12, 14, 14);

Stream<Integer> newStream = stream.filter(s -> s > 5) //6 6 7 9 8 10 12 14 14
        .distinct() //6 7 9 8 10 12 14
        .skip(2) //9 8 10 12 14
        .limit(2); //9 8
newStream.forEach(System.out::println);
// concat:合并两个流 distinct：去重
		List<String> newList = Stream.concat(stream1, stream2).distinct().collect(Collectors.toList());
```



### 2.2 映射        

​        map：接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
​        flatMap：接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流。

```
List<String> list = Arrays.asList("a,b,c", "1,2,3");

//将每个元素转成一个新的且不带逗号的元素
Stream<String> s1 = list.stream().map(s -> s.replaceAll(",", ""));
s1.forEach(System.out::println); // abc  123

Stream<String> s3 = list.stream().flatMap(s -> {
    //将每个元素转换成一个stream
    String[] split = s.split(",");
    Stream<String> s2 = Arrays.stream(split);
    return s2;
});
s3.forEach(System.out::println); // a b c 1 2 3
```





### 2.3 排序

​        sorted()：自然排序，流中元素需实现Comparable接口
​        sorted(Comparator com)：定制排序，自定义Comparator排序器  

```
List<String> list = Arrays.asList("aa", "ff", "dd");
//String 类自身已实现Compareable接口
list.stream().sorted().forEach(System.out::println);// aa dd ff

Student s1 = new Student("aa", 10);
Student s2 = new Student("bb", 20);
Student s3 = new Student("aa", 30);
Student s4 = new Student("dd", 40);
List<Student> studentList = Arrays.asList(s1, s2, s3, s4);

//自定义排序：先按姓名升序，姓名相同则按年龄升序
studentList.stream().sorted(
        (o1, o2) -> {
            if (o1.getName().equals(o2.getName())) {
                return o1.getAge() - o2.getAge();
            } else {
                return o1.getName().compareTo(o2.getName());
            }
        }
).forEach(System.out::println);
```



### 2.4 消费

​        peek：如同于map，能得到流中的每一个元素。但map接收的是一个Function表达式，有返回值；而peek接收的是Consumer表达式，没有返回值。

```
Student s1 = new Student("aa", 10);
Student s2 = new Student("bb", 20);
List<Student> studentList = Arrays.asList(s1, s2);

studentList.stream()
        .peek(o -> o.setAge(100))
        .forEach(System.out::println);   

//结果：
Student{name='aa', age=100}
Student{name='bb', age=100} 

```



## 3、流的终止操作

### 3.1 匹配、聚合操作

​        allMatch：接收一个 Predicate 函数，当流中每个元素都符合该断言时才返回true，否则返回false
​        noneMatch：接收一个 Predicate 函数，当流中每个元素都不符合该断言时才返回true，否则返回false
​        anyMatch：接收一个 Predicate 函数，只要流中有一个元素满足该断言则返回true，否则返回false
​        findFirst：返回流中第一个元素
​        findAny：返回流中的任意元素
​        count：返回流中元素的总个数
​        max：返回流中元素最大值
​        min：返回流中元素最小值

```
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

boolean allMatch = list.stream().allMatch(e -> e > 10); //false
boolean noneMatch = list.stream().noneMatch(e -> e > 10); //true
boolean anyMatch = list.stream().anyMatch(e -> e > 4);  //true

Integer findFirst = list.stream().findFirst().get(); //1
Integer findAny = list.stream().findAny().get(); //1

long count = list.stream().count(); //5
Integer max = list.stream().max(Integer::compareTo).get(); //5
Integer min = list.stream().min(Integer::compareTo).get(); //1
```



### 3.2 归约操作

归约，也称缩减，顾名思义，是把一个流缩减成一个值，能实现对集合求和、求乘积和求最值操作。

​        **Optional<T> reduce(BinaryOperator<T> accumulator)**：第一次执行时，accumulator函数的第一个参数为流中的第一个元素，第二个参数为流中元素的第二个元素；第二次执行时，第一个参数为第一次函数执行的结果，第二个参数为流中的第三个元素；依次类推。
​        **T reduce(T identity, BinaryOperator<T> accumulator)**：流程跟上面一样，只是第一次执行时，accumulator函数的第一个参数为identity，而第二个参数为流中的第一个元素。
​        **<U> U reduce(U identity,BiFunction<U, ? super T, U> accumulator,BinaryOperator<U> combiner)**：在串行流(stream)中，该方法跟第二个方法一样，即第三个参数combiner不会起作用。在并行流(parallelStream)中,我们知道流被fork join出多个线程进行执行，此时每个线程的执行流程就跟第二个方法reduce(identity,accumulator)一样，而第三个参数combiner函数，则是将每个线程的执行结果当成一个新的流，然后使用第一个方法reduce(accumulator)流程进行规约。

//经过测试，当元素个数小于24时，并行时线程数等于元素个数，当大于等于24时，并行时线程数为16

```
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24);

Integer v = list.stream().reduce((x1, x2) -> x1 + x2).get();
System.out.println(v);   // 300

Integer v1 = list.stream().reduce(10, (x1, x2) -> x1 + x2);
System.out.println(v1);  //310

Integer v2 = list.stream().reduce(0,
        (x1, x2) -> {
            System.out.println("stream accumulator: x1:" + x1 + "  x2:" + x2);
            return x1 - x2;
        },
        (x1, x2) -> {
            System.out.println("stream combiner: x1:" + x1 + "  x2:" + x2);
            return x1 * x2;
        });
System.out.println(v2); // -300

Integer v3 = list.parallelStream().reduce(0,
        (x1, x2) -> {
            System.out.println("parallelStream accumulator: x1:" + x1 + "  x2:" + x2);
            return x1 - x2;
        },
        (x1, x2) -> {
            System.out.println("parallelStream combiner: x1:" + x1 + "  x2:" + x2);
            return x1 * x2;
        });
System.out.println(v3); //197474048
```



3.3 收集操作
        collect：接收一个Collector实例，将流中元素收集成另外一个数据结构。
        Collector<T, A, R> 是一个接口，有以下5个抽象方法：
            Supplier<A> supplier()：创建一个结果容器A
            BiConsumer<A, T> accumulator()：消费型接口，第一个参数为容器A，第二个参数为流中元素T。
            BinaryOperator<A> combiner()：函数接口，该参数的作用跟上一个方法(reduce)中的combiner参数一样，将并行流中各                                                                 个子进程的运行结果(accumulator函数操作后的容器A)进行合并。
            Function<A, R> finisher()：函数式接口，参数为：容器A，返回类型为：collect方法最终想要的结果R。
            Set<Characteristics> characteristics()：返回一个不可变的Set集合，用来表明该Collector的特征。有以下三个特征：
                CONCURRENT：表示此收集器支持并发。（官方文档还有其他描述，暂时没去探索，故不作过多翻译）
                UNORDERED：表示该收集操作不会保留流中元素原有的顺序。
                IDENTITY_FINISH：表示finisher参数只是标识而已，可忽略。
        注：如果对以上函数接口不太理解的话，可参考我另外一篇文章：Java 8 函数式接口

3.3.1 Collector 工具库：Collectors

```
Student s1 = new Student("aa", 10,1);
Student s2 = new Student("bb", 20,2);
Student s3 = new Student("cc", 10,3);
List<Student> list = Arrays.asList(s1, s2, s3);

//装成list
List<Integer> ageList = list.stream().map(Student::getAge).collect(Collectors.toList()); // [10, 20, 10]

//转成set
Set<Integer> ageSet = list.stream().map(Student::getAge).collect(Collectors.toSet()); // [20, 10]

//转成map,注:key不能相同，否则报错
Map<String, Integer> studentMap = list.stream().collect(Collectors.toMap(Student::getName, Student::getAge)); // {cc=10, bb=20, aa=10}

//字符串分隔符连接
String joinName = list.stream().map(Student::getName).collect(Collectors.joining(",", "(", ")")); // (aa,bb,cc)

//聚合操作
//1.学生总数
Long count = list.stream().collect(Collectors.counting()); // 3
//2.最大年龄 (最小的minBy同理)
Integer maxAge = list.stream().map(Student::getAge).collect(Collectors.maxBy(Integer::compare)).get(); // 20
//3.所有人的年龄
Integer sumAge = list.stream().collect(Collectors.summingInt(Student::getAge)); // 40
//4.平均年龄
Double averageAge = list.stream().collect(Collectors.averagingDouble(Student::getAge)); // 13.333333333333334
// 带上以上所有方法
DoubleSummaryStatistics statistics = list.stream().collect(Collectors.summarizingDouble(Student::getAge));
System.out.println("count:" + statistics.getCount() + ",max:" + statistics.getMax() + ",sum:" + statistics.getSum() + ",average:" + statistics.getAverage());

//分组
Map<Integer, List<Student>> ageMap = list.stream().collect(Collectors.groupingBy(Student::getAge));
//多重分组,先根据类型分再根据年龄分
Map<Integer, Map<Integer, List<Student>>> typeAgeMap = list.stream().collect(Collectors.groupingBy(Student::getType, Collectors.groupingBy(Student::getAge)));

//分区
//分成两部分，一部分大于10岁，一部分小于等于10岁
Map<Boolean, List<Student>> partMap = list.stream().collect(Collectors.partitioningBy(v -> v.getAge() > 10));

//规约
Integer allAge = list.stream().map(Student::getAge).collect(Collectors.reducing(Integer::sum)).get(); //40
```



### 3.3 收集(collect)

`collect`，收集，可以说是内容最繁多、功能最丰富的部分了。从字面上去理解，就是把一个流收集起来，最终可以是收集成一个值也可以收集成一个新的集合。

`collect`主要依赖`java.util.stream.Collectors`类内置的静态方法。

#### 3.3.1 归集(toList/toSet/toMap)

因为流不存储数据，那么在流中的数据完成处理后，需要将流中的数据重新归集到新的集合里。toList、toSet和toMap比较常用，另外还有toCollection、toConcurrentMap等复杂一些的用法。

下面用一个案例演示toList、toSet和toMap：

```
public class StreamTest {
	public static void main(String[] args) {
		List<Integer> list = Arrays.asList(1, 6, 3, 4, 6, 7, 9, 6, 20);
		List<Integer> listNew = list.stream().filter(x -> x % 2 == 0).collect(Collectors.toList());
		Set<Integer> set = list.stream().filter(x -> x % 2 == 0).collect(Collectors.toSet());

		List<Person> personList = new ArrayList<Person>();
		personList.add(new Person("Tom", 8900, 23, "male", "New York"));
		personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
		personList.add(new Person("Lily", 7800, 21, "female", "Washington"));
		personList.add(new Person("Anni", 8200, 24, "female", "New York"));
		
		Map<?, Person> map = personList.stream().filter(p -> p.getSalary() > 8000)
				.collect(Collectors.toMap(Person::getName, p -> p));
		System.out.println("toList:" + listNew);
		System.out.println("toSet:" + set);
		System.out.println("toMap:" + map);
	}

}
```

运行结果：

toList：[6, 4, 6, 6, 20]
toSet：[4, 20, 6]
toMap：{Tom=mutest.Person@5fd0d5ae, Anni=mutest.Person@2d98a335}



#### 3.3.2 统计(count/averaging)

Collectors提供了一系列用于数据统计的静态方法：

计数：count
平均值：averagingInt、averagingLong、averagingDouble
最值：maxBy、minBy
求和：summingInt、summingLong、summingDouble
统计以上所有：summarizingInt、summarizingLong、summarizingDouble
案例：统计员工人数、平均工资、工资总额、最高工资。



		public class StreamTest {
		public static void main(String[] args) {
			List<Person> personList = new ArrayList<Person>();
			personList.add(new Person("Tom", 8900, 23, "male", "New York"));
			personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
			personList.add(new Person("Lily", 7800, 21, "female", "Washington"));
		// 求总数
		Long count = personList.stream().collect(Collectors.counting());
		// 求平均工资
		Double average = personList.stream().collect(Collectors.averagingDouble(Person::getSalary));
		// 求最高工资
		Optional<Integer> max = personList.stream().map(Person::getSalary).collect(Collectors.maxBy(Integer::compare));
		// 求工资之和
		Integer sum = personList.stream().collect(Collectors.summingInt(Person::getSalary));
		// 一次性统计所有信息
		DoubleSummaryStatistics collect = personList.stream().collect(Collectors.summarizingDouble(Person::getSalary));
	
		System.out.println("员工总数：" + count);
		System.out.println("员工平均工资：" + average);
		System.out.println("员工工资总和：" + sum);
		System.out.println("员工工资所有统计：" + collect);
	}

运行结果：

员工总数：3
员工平均工资：7900.0
员工工资总和：23700
员工工资所有统计：DoubleSummaryStatistics{count=3, sum=23700.000000,min=7000.000000, average=7900.000000, max=8900.000000}



#### 3.3.3 分组(partitioningBy/groupingBy)

- 分区：将`stream`按条件分为两个`Map`，比如员工按薪资是否高于8000分为两部分。
- 分组：将集合分为多个Map，比如员工按性别分组。有单级分组和多级分组。

案例：将员工按薪资是否高于8000分为两部分；将员工按性别和地区分组

```
public class StreamTest {
	public static void main(String[] args) {
		List<Person> personList = new ArrayList<Person>();
		personList.add(new Person("Tom", 8900, "male", "New York"));
		personList.add(new Person("Jack", 7000, "male", "Washington"));
		personList.add(new Person("Lily", 7800, "female", "Washington"));
		personList.add(new Person("Anni", 8200, "female", "New York"));
		personList.add(new Person("Owen", 9500, "male", "New York"));
		personList.add(new Person("Alisa", 7900, "female", "New York"));

		// 将员工按薪资是否高于8000分组
	    Map<Boolean, List<Person>> part = personList.stream().collect(Collectors.partitioningBy(x -> x.getSalary() > 8000));
	    // 将员工按性别分组
	    Map<String, List<Person>> group = personList.stream().collect(Collectors.groupingBy(Person::getSex));
	    // 将员工先按性别分组，再按地区分组
	    Map<String, Map<String, List<Person>>> group2 = personList.stream().collect(Collectors.groupingBy(Person::getSex, Collectors.groupingBy(Person::getArea)));
	    System.out.println("员工按薪资是否大于8000分组情况：" + part);
	    System.out.println("员工按性别分组情况：" + group);
	    System.out.println("员工按性别、地区：" + group2);
	}

}
```



```
员工按薪资是否大于8000分组情况：{false=[mutest.Person@2d98a335, mutest.Person@16b98e56, mutest.Person@7ef20235], true=[mutest.Person@27d6c5e0, mutest.Person@4f3f5b24, mutest.Person@15aeb7ab]}
员工按性别分组情况：{female=[mutest.Person@16b98e56, mutest.Person@4f3f5b24, mutest.Person@7ef20235], male=[mutest.Person@27d6c5e0, mutest.Person@2d98a335, mutest.Person@15aeb7ab]}
员工按性别、地区：{female={New York=[mutest.Person@4f3f5b24, mutest.Person@7ef20235], Washington=[mutest.Person@16b98e56]}, male={New York=[mutest.Person@27d6c5e0, mutest.Person@15aeb7ab], Washington=[mutest.Person@2d98a335]}}
```



#### 3.3.4 接合(joining)

joining可以将stream中的元素用特定的连接符（没有的话，则直接连接）连接成一个字符串。

		public class StreamTest {
		public static void main(String[] args) {
			List<Person> personList = new ArrayList<Person>();
			personList.add(new Person("Tom", 8900, 23, "male", "New York"));
			personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
			personList.add(new Person("Lily", 7800, 21, "female", "Washington"));
		String names = personList.stream().map(p -> p.getName()).collect(Collectors.joining(","));
		System.out.println("所有员工的姓名：" + names);
		List<String> list = Arrays.asList("A", "B", "C");
		String string = list.stream().collect(Collectors.joining("-"));
		System.out.println("拼接后的字符串：" + string);
	}

运行结果：

所有员工的姓名：Tom,Jack,Lily
拼接后的字符串：A-B-C



#### 3.3.5 归约(reducing)

Collectors类提供的reducing方法，相比于stream本身的reduce方法，增加了对自定义归约的支持。

```
public class StreamTest {
	public static void main(String[] args) {
		List<Person> personList = new ArrayList<Person>();
		personList.add(new Person("Tom", 8900, 23, "male", "New York"));
		personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
		personList.add(new Person("Lily", 7800, 21, "female", "Washington"));

		// 每个员工减去起征点后的薪资之和（这个例子并不严谨，但一时没想到好的例子）
		Integer sum = personList.stream().collect(Collectors.reducing(0, Person::getSalary, (i, j) -> (i + j - 5000)));
		System.out.println("员工扣税薪资总和：" + sum);
	
		// stream的reduce
		Optional<Integer> sum2 = personList.stream().map(Person::getSalary).reduce(Integer::sum);
		System.out.println("员工薪资总和：" + sum2.get());
	}

}
```

运行结果：

> 员工扣税薪资总和：8700
> 员工薪资总和：23700



## 4、运用示例

### 4.1、如何把String集合统计每一个的个数

```java
List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","","efg","abc","abc", "jkl");
Map<String,Long> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));

filtered.entrySet().stream().forEach(t-> System.out.println(t.getKey()+"="+t.getValue()));
```

