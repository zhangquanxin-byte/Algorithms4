# 					java集合详解

## 1、集合与数组的区别

1、数组（可以存储基本数据类型）是用来存现对象的一种容器，但是数组的长度固定，不适合在对象数量未知的情况下使用。

2、集合（只能存储对象，对象类型可以不一样）的长度可变，可在多数情况下使用。

## 2、层次关系

如图所示：图中，实线边框的是实现类，折线边框的是抽象类，而点线边框的是接口

![](D:\java oracle\学习资料\各种学习整理文档（重要）\面试题汇总\photos\集合关系总览.png)

![](D:\java oracle\学习资料\各种学习整理文档（重要）\面试题汇总\photos\collection关系图.png)



Collection接口是集合类的根接口，Java中没有提供这个接口的直接的实现类。但是却让其被继承产生了两个接口，就是Set和List。Set中不能包含重复的元素。List是一个有序的集合，可以包含重复的元素，提供了按索引访问的方式。

Map是Java.util包中的另一个接口，它和Collection接口没有关系，是相互独立的，但是都属于集合类的一部分。Map包含了key-value对。Map不能包含重复的key，但是可以包含相同的value。

Iterator，所有的集合类，都实现了Iterator接口，这是一个用于遍历集合中元素的接口，主要包含以下三种方法：
1.hasNext()是否还有下一个元素。
2.next()返回下一个元素。
3.remove()删除当前元素。



#### 常用集合的分类

Collection 接口的接口 对象的集合（单列集合）
├——-List 接口：元素按进入先后有序保存，可重复
│—————-├ LinkedList 接口实现类， 链表， 插入删除， 没有同步， 线程不安全
│—————-├ ArrayList 接口实现类， 数组， 随机访问， 没有同步， 线程不安全
│—————-└ Vector 接口实现类 数组， 同步， 线程安全
│ ———————-└ Stack 是Vector类的实现类
└——-Set 接口： 仅接收一次，不可重复，并做内部排序
├—————-└HashSet 使用hash表（数组）存储元素
│————————└ LinkedHashSet 链表维护元素的插入次序
└ —————-TreeSet 底层实现为二叉树，元素排好序

Map 接口 键值对的集合 （双列集合）
├———Hashtable 接口实现类， 同步， 线程安全
├———HashMap 接口实现类 ，没有同步， 线程不安全-
│—————–├ LinkedHashMap 双向链表和哈希表实现
│—————–└ WeakHashMap
├ ——–TreeMap 红黑树对所有的key进行排序
└———IdentifyHashMap



## 3、List和Set集合详解：

### 1.list和set的区别

![](D:\java oracle\学习资料\各种学习整理文档（重要）\面试题汇总\photos\list和set的区别.png)



### 2.List：

（1）ArrayList：底层数据结构是数组，查询快，增删慢，线程不安全，效率高，可以存储重复元素



每次添加元素的时候，如果判断数组容量不足，扩容1/2

![](D:\java oracle\学习资料\各种学习整理文档（重要）\面试题汇总\photos\捕获.PNG)

![](D:\java oracle\学习资料\各种学习整理文档（重要）\面试题汇总\photos\arraylist扩容.PNG)



（2）LinkedList 底层数据结构是链表，查询慢，增删快，线程不安全，效率高，可以存储重复元素
（3）Vector:底层数据结构是数组，查询快，增删慢，线程安全，效率低，可以存储重复元素



### 3.LIST小结：

LinkedList：
优点：LinkedList基于链表的数据结构,地址是任意的，所以在开辟内存空间的时候不需要等一个连续的地址，对于新增和删除操作add和remove，LinedList比较占优势。LinkedList 适用于要头尾操作或插入指定位置的场景
缺点：因为LinkedList要移动指针,所以查询操作性能比较低。
适用场景分析：
当需要对数据进行对此访问的情况下选用ArrayList，当需要对数据进行多次增加删除修改时采用LinkedList。

ArrayList和Vector都是用数组实现的，主要有这么三个区别：
（1）.Vector是多线程安全的，线程安全就是说多线程访问同一代码，不会产生不确定的结果。而ArrayList不是，这个可以从源码中看出，Vector类中的方法很多有synchronized进行修饰，这样就导致了Vector在效率上无法与ArrayList相比；
（2）两个都是采用的线性连续空间存储元素，但是当空间不足的时候，两个类的增加方式是不同。
*（3）*Vector可以设置增长因子，而ArrayList不可以。
*（4）*Vector是一种老的动态数组，是线程同步的，效率很低，一般不赞成使用。
适用场景分析：
1.Vector是线程同步的，所以它也是线程安全的，而ArrayList是线程异步的，是不安全的。如果不考虑到线程的安全因素，一般用ArrayList效率比较高。
2.如果集合中的元素的数目大于目前集合数组的长度时，在集合中使用数据量比较大的数据，用Vector有一定的优势。

### 4.set小结：

​        Set 注重独一无二的性质,该体系集合用于存储无序(存入和取出的顺序不一定相同)元素， 值不能重 复。

对象的相等性本质是对象 hashCode 值（java 是依据对象的内存地址计算出的此序号） 判断 的， 如果

想要让两个不同的对象视为相等的，就必须覆盖 Object 的 hashCode 方法和 equals 方 法。 



**(1)HashSet:**底层数据结构采用哈希表实现，元素无序且唯一，线程不安全，效率高，可以存储null元素，元素的唯一性是靠所存储元素类型是否重写hashCode()和equals()方法来保证的，如果没有重写这两个方法，则无法保证元素的唯一性。

HashSet 的实现比较简单，相关HashSet的操作，基本上都是直接调用底层HashMap的相关方法来完成，我们应该为保存到HashSet中的对象覆盖hashCode()和equals()，因为再将对象加入到HashSet中时，会首先调用hashCode方法计算出对象的hash值，接着根据此hash值调用HashMap中的hash方法，得到的值& (length-1)得到该对象在hashMap的transient Entry[] table中的保存位置的索引，接着找到数组中该索引位置保存的对象，并调用equals方法比较这两个对象是否相等，如果相等则不添加，注意：所以要存入HashSet的集合对象中的自定义类必须覆盖hashCode(),equals()两个方法，才能保证集合中元素不重复。

**(2)LinkedHashSet**：底层数据结构采用链表和哈希表共同实现，链表保证了元素的顺序与存储顺序一致，哈希表保证了元素的唯一性。线程不安全，效率高。

​		对于 LinkedHashSet 而言，它继承与 HashSet、又基于 LinkedHashMap 来实现的。 LinkedHashSet底层使用 LinkedHashMap 来保存所有元素，它继承与 HashSet，其所有的方法 操作上又与 HashSet相同，因此 LinkedHashSet 的实现上非常简单，只提供了四个构造方法，并 通过传递一个标识参数，调用父类的构造器，底层构造一个 LinkedHashMap 来实现，在相关操 作上与父类 HashSet 的操作相

同，直接调用父类 HashSet 的方法即可。



**(3)TreeSet**：底层数据结构采用二叉树来实现，元素唯一且已经排好序；唯一性同样需要重写hashCode和equals()方法，二叉树结构保证了元素的有序性。根据构造方法不同，分为自然排序（无参构造）和比较器排序（有参构造），自然排序要求元素必须实现Compareable接口，并重写里面的compareTo()方法，元素通过比较返回的int值来判断排序序列，返回0说明两个对象相同，不需要存储；比较器排需要在TreeSet初始化是时候传入一个实现Comparator接口的比较器对象，或者采用匿名内部类的方式new一个Comparator对象，重写里面的compare()方法；

**适用场景分析**:HashSet是基于Hash算法实现的，其性能通常都优于TreeSet。为快速查找而设计的Set，我们通常都应该使用HashSet，在我们需要排序的功能时，我们才使用TreeSet



小结：Set具有与Collection完全一样的接口，因此没有任何额外的功能，不像前面有两个不同的List。实际上Set就是Collection,只 是行为不同。(这是继承与多态思想的典型应用：表现不同的行为。)Set不保存重复的元素。
Set 存入Set的每个元素都必须是唯一的，因为Set不保存重复元素。加入Set的元素必须定义equals()方法以确保对象的唯一性。Set与Collection有完全一样的接口。Set接口不保证维护元素的次序。

### 5、使用区别图解

![](D:\java oracle\学习资料\各种学习整理文档（重要）\面试题汇总\photos\LIST和SET使用图解.png)



## 4、Map详解：

Map用于保存具有映射关系的数据，Map里保存着两组数据：key和value，它们都可以使任何引用类型的数据，但key不能重复。所以通过指定的key就可以取出对应的value。

（1）、请注意！！！， Map 没有继承 Collection 接口， Map 提供 key 到 value 的映射，你可以通过“键”查找“值”。一个 Map 中不能包含相同的 key ，每个 key 只能映射一个 value 。 Map 接口提供 3 种集合的视图， Map 的内容可以被当作一组 key 集合，一组 value 集合，或者一组 key-value 映射。

### （2）Map：

![](D:\java oracle\学习资料\各种学习整理文档（重要）\面试题汇总\photos\MAP方法.png)



### （3）HashMap和HashTable的比较：

![](D:\java oracle\学习资料\各种学习整理文档（重要）\面试题汇总\photos\HashMap和HashTable的比较：.png)

Hashtable 是遗留类，很多映射的常用功能与 HashMap 类似，不同的是它承自 Dictionary 类， 并且

是线程安全的，任一时间只有一个线程能写 Hashtable，并发性不如 ConcurrentHashMap， 因为

ConcurrentHashMap 引入了分段锁。 Hashtable 不建议在新代码中使用，不需要线程安全 的场合可

以用 HashMap 替换，需要线程安全的场合可以用 ConcurrentHashMap 替换





### （4）TreeMap：

![](D:\java oracle\学习资料\各种学习整理文档（重要）\面试题汇总\photos\TreeMap.png)



### 5）Map的其它类：

IdentityHashMap和HashMap的具体区别，IdentityHashMap使用 == 判断两个key是否相等，而HashMap使用的是equals方法比较key值。有什么区别呢？
对于==，如果作用于基本数据类型的变量，则直接比较其存储的 “值”是否相等； 如果作用于引用类型的变量，则比较的是所指向的对象的地址。
对于equals方法，注意：equals方法不能作用于基本数据类型的变量
如果没有对equals方法进行重写，则比较的是引用类型的变量所指向的对象的地址；
诸如String、Date等类对equals方法进行了重写的话，比较的是所指向的对象的内容。

![](D:\java oracle\学习资料\各种学习整理文档（重要）\面试题汇总\photos\Map的其它类.jpg)



### （6）小结：

HashMap 非线程安全
HashMap：基于哈希表实现。使用HashMap要求添加的键类明确定义了hashCode()和equals()[可以重写hashCode()和equals()]，为了优化HashMap空间的使用，您可以调优初始容量和负载因子。

TreeMap：非线程安全基于红黑树实现。TreeMap没有调优选项，因为该树总处于平衡状态。

适用场景分析：
HashMap和HashTable:HashMap去掉了HashTable的contains方法，但是加上了containsValue()和containsKey()方法。HashTable同步的，而HashMap是非同步的，效率上比HashTable要高。HashMap允许空键值，而HashTable不允许。

HashMap：适用于Map中插入、删除和定位元素。
Treemap：适用于按自然顺序或自定义顺序遍历键(key)。

5.线程安全集合类与非线程安全集合类
LinkedList、ArrayList、HashSet是非线程安全的，Vector是线程安全的;
HashMap是非线程安全的，HashTable是线程安全的;
StringBuilder是非线程安全的，StringBuffer是线程安全的。



## 5、遍历方式

 在类集中提供了以下四种的常见输出方式：

1）Iterator：迭代输出，是使用最多的输出方式。

2）ListIterator：是Iterator的子接口，专门用于输出List中的内容。

3）foreach输出：JDK1.5之后提供的新功能，可以输出数组或集合。

4）for循环

代码示例如下：

 for的形式：for（int i=0;i<arr.size();i++）{...}

 foreach的形式： for（int　i：arr）{...}

 iterator的形式：
Iterator it = arr.iterator();
while(it.hasNext()){ object o =it.next(); ...}



## 6、HashMap原理详解

https://blog.csdn.net/tuke_tuke/article/details/51588156

### 6.3、HashMap的构造函数

HashMap的构造方法有4种，主要涉及到的参数有，指定初始容量，指定填充比和用来初始化的Map

```
//构造函数1
public HashMap(int initialCapacity, float loadFactor) {
    //指定的初始容量非负
    if (initialCapacity < 0)
        throw new IllegalArgumentException(Illegal initial capacity:  +
                                           initialCapacity);
    //如果指定的初始容量大于最大容量,置为最大容量
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    //填充比为正
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException(Illegal load factor:  +
                                           loadFactor);
    this.loadFactor = loadFactor;
    this.threshold = tableSizeFor(initialCapacity);//新的扩容临界值
}

//构造函数2
public HashMap(int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
}

//构造函数3
public HashMap() {
    this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
}

//构造函数4用m的元素初始化散列映射
public HashMap(Map<!--? extends K, ? extends V--> m) {
    this.loadFactor = DEFAULT_LOAD_FACTOR;
    putMapEntries(m, false);
}
```



### 6.4、HashMap的存取机制

#### 1，HashMap如何getValue值，看源码

```
public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }
	  /**
     * Implements Map.get and related methods
     *
     * @param hash hash for key
     * @param key the key
     * @return the node, or null if none
     */
	final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab;//Entry对象数组
	Node<K,V> first,e; //在tab数组中经过散列的第一个位置
	int n;
	K k;
	/*找到插入的第一个Node，方法是hash值和n-1相与，tab[(n - 1) & hash]*/
	//也就是说在一条链上的hash值相同的
        if ((tab = table) != null && (n = tab.length) > 0 &&(first = tab[(n - 1) & hash]) != null) {
	/*检查第一个Node是不是要找的Node*/
            if (first.hash == hash && // always check first node
                ((k = first.key) == key || (key != null && key.equals(k))))//判断条件是hash值要相同，key值要相同
                return first;
	  /*检查first后面的node*/
            if ((e = first.next) != null) {
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
				/*遍历后面的链表，找到key值和hash值都相同的Node*/
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }
```


get(key)方法时获取key的hash值，计算hash&(n-1)得到在链表数组中的位置first=tab[hash&(n-1)],先判断first的key是否与参数key相等，不等就遍历后面的链表找到相同的key值返回对应的Value值即可



#### 2，HashMap如何put(key，value);看源码

```
public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
	 /**
     * Implements Map.put and related methods
     *
     * @param hash hash for key
     * @param key the key
     * @param value the value to put
     * @param onlyIfAbsent if true, don't change existing value
     * @param evict if false, the table is in creation mode.
     * @return previous value, or null if none
     */
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; 
	Node<K,V> p; 
	int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
	/*如果table的在（n-1）&hash的值是空，就新建一个节点插入在该位置*/
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
	/*表示有冲突,开始处理冲突*/
        else {
            Node<K,V> e; 
	    K k;
	/*检查第一个Node，p是不是要找的值*/
            if (p.hash == hash &&((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount) {
		/*指针为空就挂在后面*/
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
		       //如果冲突的节点数已经达到8个，看是否需要改变冲突节点的存储结构，　　　　　　　　　　　　　
　　　　　　　　　　　　//treeifyBin首先判断当前hashMap的长度，如果不足64，只进行
                        //resize，扩容table，如果达到64，那么将冲突的存储结构为红黑树
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
		/*如果有相同的key值就结束遍历*/
                    if (e.hash == hash &&((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
	/*就是链表上有相同的key值*/
            if (e != null) { // existing mapping for key，就是key的Value存在
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;//返回存在的Value值
            }
        }
        ++modCount;
     /*如果当前大小大于门限，门限原本是初始容量*0.75*/
        if (++size > threshold)
            resize();//扩容两倍
        afterNodeInsertion(evict);
        return null;
    }
```



下面简单说下添加键值对put(key,value)的过程：
1，判断键值对数组tab[]是否为空或为null，否则以默认大小resize()；
2，根据键值key计算hash值得到插入的数组索引i，如果tab[i]==null，直接新建节点添加，否则转入3
3，判断当前数组中处理hash冲突的方式为链表还是红黑树(check第一个节点类型即可),分别处理



### 6.5、HasMap的扩容机制resize()

***\*构造hash表时，如果不指明初始大小，默认大小为16（即Node数组大小16），如果Node[]数组中的元素达到（填充比\*Node.length）重新调整HashMap大小 变为原来2倍大小,\****扩容很耗时

```
/**
     * Initializes or doubles table size.  If null, allocates in
     * accord with initial capacity target held in field threshold.
     * Otherwise, because we are using power-of-two expansion, the
     * elements from each bin must either stay at same index, or move
     * with a power of two offset in the new table.
     *
     * @return the table
     */
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
		
	/*如果旧表的长度不是空*/
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
	/*把新表的长度设置为旧表长度的两倍，newCap=2*oldCap*/
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
	      /*把新表的门限设置为旧表门限的两倍，newThr=oldThr*2*/
                newThr = oldThr << 1; // double threshold
        }
     /*如果旧表的长度的是0，就是说第一次初始化表*/
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
		
		
		
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;//新表长度乘以加载因子
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
	/*下面开始构造新表，初始化表中的数据*/
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;//把新表赋值给table
        if (oldTab != null) {//原表不是空要把原表中数据移动到新表中	
            /*遍历原来的旧表*/		
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)//说明这个node没有链表直接放在新表的e.hash & (newCap - 1)位置
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
	/*如果e后边有链表,到这里表示e后面带着个单链表，需要遍历单链表，将每个结点重*/
                    else { // preserve order保证顺序
					新计算在新表的位置，并进行搬运
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
						
                        do {
                            next = e.next;//记录下一个结点
			  //新表是旧表的两倍容量，实例上就把单链表拆分为两队，
　　　　　　　　　　　　　　//e.hash&oldCap为偶数一队，e.hash&oldCap为奇数一对
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
						
                        if (loTail != null) {//lo队不为null，放在新表原位置
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {//hi队不为null，放在新表j+oldCap位置
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

```



### 6.6、JDK1.8使用红黑树的改进

在java jdk8中对HashMap的源码进行了优化，在jdk7中，HashMap处理“碰撞”的时候，都是采用链表来存储，当碰撞的结点很多时，查询时间是O（n）。
在jdk8中，HashMap处理“碰撞”增加了红黑树这种数据结构，当碰撞结点较少时，采用链表存储，当较大时（>8个），采用红黑树（特点是查询时间是O（logn））存储（有一个阀值控制，大于阀值(8个)，将链表存储转换成红黑树存储）

JDK1.8HashMap的红黑树是这样解决的：

         如果某个桶中的记录过大的话（当前是TREEIFY_THRESHOLD = 8），HashMap会动态的使用一个专门的treemap实现来替换掉它。这样做的结果会更好，是O(logn)，而不是糟糕的O(n)。
    
        它是如何工作的？前面产生冲突的那些KEY对应的记录只是简单的追加到一个链表后面，这些记录只能通过遍历来进行查找。但是超过这个阈值后HashMap开始将列表升级成一个二叉树，使用哈希值作为树的分支变量，如果两个哈希值不等，但指向同一个桶的话，较大的那个会插入到右子树里。如果哈希值相等，HashMap希望key值最好是实现了Comparable接口的，这样它可以按照顺序来进行插入。这对HashMap的key来说并不是必须的，不过如果实现了当然最好。如果没有实现这个接口，在出现严重的哈希碰撞的时候，你就并别指望能获得性能提升了




## 7、ConcurrentHashMap详解

### 7.1、HashMap、HashTable、ConcurrentHashMap等的区别

**HashMap**
我们知道HashMap是线程不安全的，在多线程环境下，使用Hashmap进行put操作会引起死循环，导致CPU利用率接近100%，所以在并发情况下不能使用HashMap。

**HashTable**
HashTable和HashMap的实现原理几乎一样，差别无非是

HashTable不允许key和value为null
HashTable是线程安全的
但是HashTable线程安全的策略实现代价却太大了，简单粗暴，get/put所有相关操作都是synchronized的，这相当于给整个哈希表加了一把大锁。

多线程访问时候，只要有一个线程访问或操作该对象，那其他线程只能阻塞，相当于将所有的操作串行化，在竞争激烈的并发场景中性能就会非常差。

**ConcurrentHashMap**
主要就是为了应对hashmap在并发环境下不安全而诞生的，ConcurrentHashMap的设计与实现非常精巧，大量的利用了volatile，final，CAS等lock-free技术来减少锁竞争对于性能的影响。
我们都知道Map一般都是数组+链表结构（JDK1.8该为数组+红黑树）。



### 7.2、JDK1.7版本的CurrentHashMap的实现原理

在JDK1.7中ConcurrentHashMap采用了数组+Segment+分段锁的方式实现。

Segment(分段锁)-减少锁的粒度
ConcurrentHashMap中的分段锁称为Segment，它即类似于HashMap的结构，即内部拥有一个Entry数组，数组中的每个元素又是一个链表,同时又是一个ReentrantLock（Segment继承了ReentrantLock）。

内部结构
ConcurrentHashMap使用分段锁技术，将数据分成一段一段的存储，然后给每一段数据配一把锁，当一个线程占用锁访问其中一个段数据的时候，其他段的数据也能被其他线程访问，能够实现真正的并发访问。如下图是ConcurrentHashMap的内部结构图：

![](D:\java oracle\学习资料\各种学习整理文档（重要）\面试题汇总\photos\java7concurrentHashMap.png)

从上面的结构我们可以了解到，ConcurrentHashMap定位一个元素的过程需要进行两次Hash操作。

第一次Hash定位到Segment，第二次Hash定位到元素所在的链表的头部。

该结构的优劣势
坏处是这一种结构的带来的副作用是Hash的过程要比普通的HashMap要长。

好处是写操作的时候可以只对元素所在的Segment进行加锁即可，不会影响到其他的Segment，这样，在最理想的情况下，ConcurrentHashMap可以最高同时支持Segment数量大小的写操作（刚好这些写操作都非常平均地分布在所有的Segment上）。
所以，通过这一种结构，ConcurrentHashMap的并发能力可以大大的提高。



### 7.3、JDK1.8版本的CurrentHashMap的实现原理

JDK8中ConcurrentHashMap参考了JDK8 HashMap的实现，采用了数组+链表+红黑树的实现方式来设计，内部大量采用CAS操作，这里我简要介绍下CAS。

CAS是compare and swap的缩写，即我们所说的比较交换。cas是一种基于锁的操作，而且是乐观锁。在java中锁分为乐观锁和悲观锁。悲观锁是将资源锁住，等一个之前获得锁的线程释放锁之后，下一个线程才可以访问。而乐观锁采取了一种宽泛的态度，通过某种方式不加锁来处理资源，比如通过给记录加version来获取数据，性能较悲观锁有很大的提高。

CAS 操作包含三个操作数 —— 内存位置（V）、预期原值（A）和新值(B)。如果内存地址里面的值和A的值是一样的，那么就将内存里面的值更新成B。CAS是通过无限循环来获取数据的，如果在第一轮循环中，a线程获取地址里面的值被b线程修改了，那么a线程需要自旋，到下次循环才有可能机会执行。

JDK8中彻底放弃了Segment转而采用的是Node，其设计思想也不再是JDK1.7中的分段锁思想。

Node：保存key，value及key的hash值的数据结构。其中value和next都用volatile修饰，保证并发的可见性。

```
class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    volatile V val;
    volatile Node<K,V> next;
    //... 省略部分代码
}

```

Java8 ConcurrentHashMap结构基本上和Java8的HashMap一样，不过保证线程安全性。

在JDK8中ConcurrentHashMap的结构，由于引入了红黑树，使得ConcurrentHashMap的实现非常复杂，我们都知道，红黑树是一种性能非常好的二叉查找树，其查找性能为O（logN），但是其实现过程也非常复杂，而且可读性也非常差，Doug Lea的思维能力确实不是一般人能比的，早期完全采用链表结构时Map的查找时间复杂度为O（N），JDK8中ConcurrentHashMap在链表的长度大于某个阈值的时候会将链表转换成红黑树进一步提高其查找性能。

![](D:\java oracle\学习资料\各种学习整理文档（重要）\面试题汇总\photos\concurrentHashMap数据结构.png)



### 总结

其实可以看出JDK1.8版本的ConcurrentHashMap的数据结构已经接近HashMap，相对而言，ConcurrentHashMap只是增加了同步的操作来控制并发，从JDK1.7版本的ReentrantLock+Segment+HashEntry，到JDK1.8版本中synchronized+CAS+HashEntry+红黑树。

1.数据结构：取消了Segment分段锁的数据结构，取而代之的是数组+链表+红黑树的结构。
2.保证线程安全机制：JDK1.7采用segment的分段锁机制实现线程安全，其中segment继承自ReentrantLock。JDK1.8采用CAS+Synchronized保证线程安全。
3.锁的粒度：原来是对需要进行数据操作的Segment加锁，现调整为对每个数组元素加锁（Node）。
4.链表转化为红黑树:定位结点的hash算法简化会带来弊端,Hash冲突加剧,因此在链表节点数量大于8时，会将链表转化为红黑树进行存储。
5.查询时间复杂度：从原来的遍历链表O(n)，变成遍历红黑树O(logN)



并发编程中原子性、可见性、有序性

https://www.cnblogs.com/dolphin0520/p/3920373.html