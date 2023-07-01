mybatis文档



### 1、CDATA

ibatis中用<![CDATA[ ]]>标记避免sql中与xml规范相冲突的字符对xml映射文件的合法性造成影响.
<![CDATA[ ]]>以"<![CDATA["标记开始,以"]]>"标记结束,指的是不应由xml解析器进行解析的文本数据(Unparsed Character Data),<![CDATA[ ]]>里所有的内容都会被解析器忽略.



假如在IBATIS中自己的SQL中包含
<、>、&需要用<![CDATA[。
一般ibatis中出现<表示小于号，于XML格式冲突。>号一样。
&符号ibatis中一般不出现，除非很特殊就是写死要查某个值里含有这个&。

```sql
<update id="updateTakeUpAmtByCustId">
    update ifb_cred_inf
    set BAL_AMT=BAL_AMT-(#{amt,jdbcType=DECIMAL}),USE_AMT=USE_AMT+(#{amt,jdbcType=VARCHAR})
    where STAT='1' and CRED_TYP='1' and CUST_ID=(#{custId,jdbcType=VARCHAR}) and <![CDATA[ BAL_AMT>=#{amt,jdbcType=DECIMAL} ]]>
  </update>
```

