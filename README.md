# Jun AI Agent 项目介绍

## 项目概述
Jun AI Agent 是一个基于Spring Boot框架构建的AI代理服务，主要提供恋爱建议和咨询功能。该项目结合了现代Web技术和人工智能算法，为用户提供个性化的互动体验。

## 技术栈
- 后端框架: Spring Boot
- 数据库: PostgreSQL (使用PgVector扩展)
- AI技术: RAG (Retrieval Augmented Generation) 架构
- 文件存储: 阿里云OSS
- 搜索技术: Web搜索、PDF生成、终端操作等工具

## 主要模块
- `advisor`: 包含日志记录和请求重读取的切面组件
- `agent`: 实现不同类型的AI代理核心逻辑（BaseAgent, JunManus, ReActAgent等）
- `app`: LoveApp类实现恋爱咨询服务的核心业务逻辑
- `chatmemory`: 提供基于文件的聊天记忆管理
- `config`: 跨域配置(CorsConfig)和工具注册(ToolRegistration)
- `constant`: 存放文件常量(FileConstant)
- `controller`: AI控制器(AiController)，处理RESTful API请求
- `demo`: 示例代码，包含调用和RAG实现
- `properties`: 配置属性类(AliOssProperties)
- `rag`: 实现检索增强生成(RAG)架构的相关组件
- `tools`: 提供多种实用工具类（文件操作、PDF生成、网络爬虫等）

## 特色功能
- 基于MCP服务器的图像搜索能力
- 多种AI代理模式（ReactAgent, ToolCallAgent等）
- 强大的RAG架构支持文档检索和语义理解
- 完善的工具集（网页搜索、PDF生成、终端操作等）
- 支持对话历史记录和上下文管理

## 配置文件
- application.yml: 主配置文件
- application-local.yml: 本地开发环境配置
- mcp-servers.json: MCP服务器配置文件

## 测试用例
项目包含完整的JUnit测试套件，覆盖所有核心模块和工具类