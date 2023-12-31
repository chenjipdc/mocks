# mocks

## 简介

mocks是一个模拟数据工具，通过简单的json文件配置，从各种数据源抽取数据，然后组成一个数据池，
以管道的方式随机将数据池的数据流向各种目标数据源，从而快速模拟出各种数据库等需要的随机数据。

> 总之，本着插件化的宗旨，想要什么皆可自己开发实现。

## 架构

架构很简单：

1. SPI实现各种数据源mock插件，抽取/生成需要的字段数据
2. 将各种数据源抽取的字段数据组成一个池子(map)
3. 将池子里的数据进行随机流出，通过线程池异步流向各种sink（SPI插件）
4. sink过程通过converter（SPI插件）自定义转换字段值

## 运行

> 需要先设置好配置文件

1. 直接clone项目到本地，通过ide执行main方法
2. 直接clone项目，运行`mvn clean package -DskipTests -Prelease`打包（target目录下mocks-xxx.jar为可执行jar，libs为依赖jar包），执行`java -jar mocks-xxx.jar`运行即可(缺什么驱动自行下载到libs即可)。

## [项目配置说明wiki](https://github.com/chenjipdc/mocks/wiki/%E9%85%8D%E7%BD%AE%E8%AF%B4%E6%98%8E)

> mock跟sink配置：会读目录`~/.mock/config/`跟当前jar包同级目录`config/`下的所有`*.json`文件当作mock跟sink的配置。

配置文件主要分成两部分：mocks跟sinks
- mocks为数据从哪里来(统称mock插件)
  - type为插件类型
  - columns为字段名称(主要用于数据库字段)
  - aliases为columns的别名（未设置默认为columns，目的是用于解决多字段名称冲突问题）用于sinks里面的mappings映射关系
  - config为插件的配置
- sinks为数据去哪里(统称sink插件)
  - type为插件类型
  - mappings为sink的字段与mock的aliases关系（key为字段名称，value为aliases的value）
  - config为插件的配置

示例：

```json
[
  {
    "size": 10000,
    "mocks": [
      {
        "type": "long",
        "columns": [
          "id"
        ],
        "config": {
          "autoincrement": true,
          "start": 1,
          "end": 10000
        }
      },
      {
        "type": "name",
        "columns": [
          "name"
        ],
        "config": {
        }
      },
      {
        "type": "int",
        "columns": [
          "age"
        ],
        "config": {
          "start": 10,
          "end": 30
        }
      },
      {
        "type": "date",
        "columns": [
          "created_date"
        ],
        "config": {
          "start": "2023-06-01 00:00:00",
          "end": "2023-06-10 00:00:00"
        }
      }
    ],
    "sinks": [
      {
        "type": "log",
        "mappings": {
          "id": "id",
          "name": "name",
          "age": "age",
          "created_date": "date"
        },
        "converters": [
          {
            "column": "age",
            "type": "int-to-string"
          },
          {
            "column": "created_date",
            "type": "date-to-string"
          },
          {
            "column": "created_date",
            "type": "quote-string"
          }]
      },
      {
        "type": "mysql",
        "mappings": {
          "id": "id",
          "name": "name",
          "age": "age",
          "created_date": "date"
        },
        "config": {
          "batch": 1000,
          "jdbcUrl": "jdbc:mysql://127.0.0.1:3306/mydb?characterEncoding=utf8",
          "username": "root",
          "password": "root",
          "table": "my_user"
        }
      }
    ]
  }
]

```

> sink线程池配置：会读文件`~/.mock/pools-sink.json`（优先级高）跟或者当前jar包同级目录文件`pools-sink.json`(优先级低)当作sink线程池的配置。

示例：

```json
{
  "corePoolSize": 5,
  "maxPoolSize": 20,
  "capacity": 50000,
  "keepAliveTime": 0,
  "timeUnit": "MINUTES",
  "daemon": false,
  "priority": 1
}
```

----

### mock插件
#### 普通
- long
- int
- short
- float
- double
- bool
- date
- bytes
- ip
- geo
- text
- uuid
- snowflake

#### 文件
- file-txt
- file-json
- file-delimiter

#### 数据库
- mysql
- mongo
- postgresql
- clickhouse

#### 其他
- name
- thread
- ignore


### sink插件
#### 文件
- file-json
- file-delimiter

#### 数据库
- mysql
- mongo
- elasticsearch
- clickhouse
- postgresql

#### 消息队列
- rocketmq
- rabbitmq
- kafka

#### 其他
- log
- ignore

### 字段转换器(converter)插件
- bigint-to-long
- date-to-string
- double-quote-string
- quote-string
- to-string
- string-to-int
- string-to-long
- string-to-short
- string-to-bool
- string-to-float
- string-to-double
- string-to-decimal
- string-to-date
- string-replace
- string-insert

### mock数据池缓存cache插件
- memory
- redis

## 开发插件
### mock插件
继承类`AbstractMockPlugin`实现以下方法即可：
- String type(): 插件类型
- void init(config): 数据初始化在这里实现（如数据库读取数据）
- Map<String, T> value(): 会传递到sink里的values，作为values的一部分数据
- void close(): close

### sink插件
继承类`AbstractSinkPlugin`实现以下方法即可：
- String type(): 插件类型
- void init(config): 建立连接等可以在这里实现
- void sink(Map<String, Object> values): 从各种mock组合而成的数据
- void close()： 关闭连接等可以在这里实现

### converter插件
实现接口`ConverterPlugin`：
- String type(): 插件类型
- void init(config): 插件配置初始化
- R convert(T value)：转换

### cache插件
实现接口`CachePlugin`：
- String type()：插件类型
- void init(String config)：配置初始化，建立连接等可在这实现
- void cache(Map<String, V> value)：缓存一条数据
- void caches(Collection<Map<String, V>> value)：缓存一个集合数据
- Map<String, V> get(boolean random)：获取一条数据
- int size()：数据大小
- void stop()：关闭连接清理数据等可以在这里实现

### filter插件
