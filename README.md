# common-tools
> 开发常用工具类, 为减少重复造轮子而努力!<br />
> 包含的语言：Java, Python, CodeDoc

## 快速开始
- 受保护的分支: master, release_*
- 开发分支: dev_*
- Java 基于JDK 1.8
- Python 基于Python 2.x
- [启动服务](system-docs/start-ct-server.adoc)

## 准入规范
- `code-repositories`代码目录文件不可带有业务逻辑，文件须仅属于tools code；除非必须，则使用伪代码
- `code-repositories`代码目录根据代码功能领域划分
- 功能领域划分根目录必须存在 `description.yml`(描述文件)；书写规则见 [description-rules.adoc](system-docs/description-rules.adoc)
- 代码内置属性应带有注释, 包括但不限于属性, 类, 方法
- commit msg 应针对一个文件或一个模块的多个文件
- commit msg 应尽可能的涵盖所有改动描述

## 设计文档
文档包含了 `common-tools`实现的内容及设计  
[common-tools.adoc](system-docs/common-tools.adoc)

## 示例文档
文档包含了使用示例，可以参考示例中的代码提交流程以快速上手。
- [Java Example](system-docs/example/java_example.md)
- [Python Example](system-docs/example/python_example.md)
- [CodeDoc Example](system-docs/example/codedoc_example.md)

## 交流群
钉钉群：35027067
