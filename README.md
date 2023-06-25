# mocks

## 简介

mocks是一个模拟数据工具，通过简单的json文件配置，从各种数据源抽取数据，然后组成一个数据池，
以管道的方式随机将数据池的数据流向各种目标数据源，从而快速模拟出各种数据库等需要的随机数据。

## 架构

架构很简单：

1. SPI实现各种数据源mock插件，抽取需要的字段数据
2. 将各种数据源抽取的字段数据组成一个池子
3. 将池子里的数据进行随机流出，通过线程池异步流向各种sink
4. 各种sink插件也由SPI实现插件化

## 运行

> 需要先设置好配置文件

1. 直接clone项目到本地，通过ide执行main方法
2. 直接clone项目，运行`mvn clean package -DskipTests`打包（target目录下mocks-xxx.jar为可执行jar，libs为依赖jar包），执行`java -jar mocks-xxx.jar`运行即可。

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
        }
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
- text
- uuid
- snowflake

#### 文件
- file-txt
- file-json

#### 数据库
- mysql
- mongo

#### 其他
- name
- thread


### sink插件
#### 文件
- file-json

#### 数据库
- mysql
- mongo

#### 消息队列
- rocketmq
- rabbitmq
- kafka

#### 其他
- log

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