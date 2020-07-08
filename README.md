# common-tools
> 开发常用工具类, 为减少重复造轮子而努力!<br />
> 包含的语言：Java, Python, Shell, JavaScript

## 开发模式
- 受保护的分支: master, release_*
- 开发分支: dev_*
- Java 基于JDK 1.8
- Python 基于Python 2.x

## 准入规范
- 代码文件不可带有业务逻辑，文件须仅属于tools code；除非必须，则使用伪代码
- 代码目录根据代码功能领域划分
- 每个代码目录必须存在 `description.yml`(描述文件)；书写规则见 [description-rules.adoc](system-docs/description-rules.adoc)
- 代码内置属性应带有注释, 包括但不限于属性, 类, 方法
- commit msg 应针对一个文件或一个模块的多个文件
- commit msg 应尽可能的涵盖所有改动描述

## Example
文档包含了一个示例，可以参考示例中的代码提交流程以快速上手。
- [Java Example](system-docs/example/java_example.md)
- [Python Example](system-docs/example/python_example.md)
- [Shell Example](system-docs/example/shell_example.md)
- [JavaScript Example](system-docs/example/javascript_example.md)
