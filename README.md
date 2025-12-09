现代记事本应用 (ModernNotePad)

项目简介

ModernNotePad 是一款基于 Android 平台的轻量级记事本应用，采用 Material Design 设计风格，支持笔记的创建、编辑、删除、搜索及分类管理等功能。应用采用 MVVM 架构模式，结合 Room 数据库实现本地数据持久化，确保用户笔记安全存储。

核心功能

笔记管理：支持添加、编辑、删除笔记，满足日常记录需求

分类管理：提供工作、学习、生活、其他四类笔记分类，方便整理

待办事项：支持将笔记标记为待办事项，并可勾选完成状态

搜索功能：实时搜索笔记内容，快速定位所需信息

时间记录：自动记录笔记创建 / 修改时间，便于追溯

响应式 UI：适配不同屏幕尺寸，提供流畅的交互体验

界面展示

主界面

显示所有笔记列表，支持搜索和添加新笔记

<img width="324" height="645" alt="主界面" src="https://github.com/user-attachments/assets/124ccca6-3e43-4af8-9233-b2185322350f" />

添加笔记界面

可输入标题、内容，选择分类及标记为待办事项

<img width="342" height="635" alt="添加笔记界面" src="https://github.com/user-attachments/assets/22c02411-3091-4966-8658-405ac223bf5c" />

编辑笔记界面

支持修改笔记内容、分类及待办状态，提供删除功能

<img width="395" height="690" alt="编辑笔记界面" src="https://github.com/user-attachments/assets/5932cd30-bbb3-4c97-b035-b947eab3ae1b" />
技术实现

架构设计

采用 MVVM 架构模式，实现数据与 UI 的解耦：

Model：通过 Room 数据库定义数据实体（Note）

View：Activity 与 XML 布局文件负责 UI 展示

ViewModel：协调数据与 UI，处理业务逻辑

核心技术栈

Room：本地数据库框架，负责数据持久化

ViewModel & LiveData：管理 UI 相关数据，确保配置变更时数据不丢失

RecyclerView：高效展示笔记列表，支持动态更新

Material Design：提供统一的设计风格和交互组件

Data Binding：简化 UI 与数据的绑定逻辑

数据流程

数据库操作通过 NoteDao 接口定义

NoteRepository 作为数据访问中介，协调本地数据库操作

NoteViewModel 暴露可观察数据给 UI 层

UI 层（Activity）观察数据变化并更新界面

以下为代码结构：

notepad/

└── app/

    ├── build/
    
    │   ├── intermediates/
    
    │   │   ├── local_only_symbol_list/
    
    │   │   │   └── debug/
    
    │   │   │       └── parseDebugLocalResources/
    
    │   │   │           └── R-def.txt
    
    │   │   └── packaged_res/
    
    │   │       └── debug/
    
    │   │           └── packageDebugResources/
    
    │   │               └── color/
    
    │   │                   └── strings.xml
    
    └── src/
    
        └── main/
        
            ├── java/
            
            │   └── com/
            
            │       └── example/
            
            │           └── ipad/
            
            │               ├── Note.java
            
            │               ├── NoteActivity.java
            
            │               ├── NoteAdapter.java
            
            │               ├── NoteDao.java

            │               ├── NoteRepository.java
            
            │               ├── NoteViewModel.java
            
            │               └── ipad/
            
            │                   ├── Note.java
            
            │                   ├── NoteActivity.java
            
            │                   ├── NoteAdapter.java
            
            │                   ├── NoteDao.java
            
            │                   ├── NoteRepository.java
            
            │                   └── NoteViewModel.java
            
            └── res/
            
                ├── color/
                
                │   └── strings.xml
                
                └── values/
                
                    └── strings.xml
                    
