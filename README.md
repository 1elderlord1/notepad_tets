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

核心功能实现机制

1. 数据持久化（Room 数据库）

1.1 笔记数据模型（Note 实体）

通过 @Entity 注解标记为 Room 数据库表，定义笔记核心属性，包含 getter/setter 及辅助方法：

<img width="771" height="340" alt="image" src="https://github.com/user-attachments/assets/b7bcac62-69a1-491c-b8cc-a2a2138c3e4e" />

1.2 数据库操作（NoteDao）

通过 Room 注解定义数据库 CRUD 操作，返回 LiveData 实现数据变化自动通知：

<img width="698" height="266" alt="image" src="https://github.com/user-attachments/assets/7a0fcc26-a113-4ea0-91b1-9286d1d5a1ee" />

1.3 异步操作封装（NoteRepository）

通过 Executors.newSingleThreadExecutor() 创建单线程池，避免主线程执行数据库操作导致 ANR：

<img width="1051" height="761" alt="image" src="https://github.com/user-attachments/assets/6ab7d792-09e1-4a50-b5b3-f9d659d04033" />

2. 笔记核心功能

2.1 笔记列表展示（MainActivity + NoteAdapter）

数据监听：MainActivity 观察 NoteViewModel 暴露的 getAllNotes()（LiveData 类型），数据变化时自动更新 RecyclerView 列表：

<img width="944" height="180" alt="image" src="https://github.com/user-attachments/assets/e4c3fc83-5333-4ddc-ad88-efc2bd29df7c" />


列表适配：NoteAdapter 封装 RecyclerView 数据绑定逻辑，支持：

展示笔记标题、内容、时间、分类；

根据 isTodo 显示 / 隐藏待办复选框；

根据 color 设置笔记卡片背景色；

绑定点击 / 长按 / 待办状态切换事件。

2.2 笔记新增 / 编辑（NoteActivity）

页面模式判断：通过 Intent 是否携带 note_id 区分「新建」/「编辑」模式：

新建模式：隐藏删除按钮，标题设为「新建笔记」；

编辑模式：标题设为「编辑笔记」，加载对应 ID 的笔记数据填充到输入框。

保存逻辑：

校验：标题 + 内容不能为空，为空则通过 Toast 提示；

新建笔记：初始化 timestamp 为当前时间戳，调用 noteViewModel.insert()；

编辑笔记：复用原有 id，调用 noteViewModel.update() 更新数据。

<img width="927" height="652" alt="image" src="https://github.com/user-attachments/assets/dcb85867-97eb-44eb-bc0d-aada121423b0" />

2.3 笔记删除

支持两种删除方式：

列表长按删除：MainActivity 中长按笔记项，触发 noteViewModel.delete(note)；

编辑页删除：NoteActivity 点击删除按钮，删除当前笔记并关闭页面。

2.4 待办事项功能

标记待办：NoteActivity 中通过 CheckBox 设置 isTodo 字段，保存时写入数据库；

状态切换：NoteAdapter 中监听待办复选框状态变化，触发 onTodoChecked 回调，更新 isCompleted 并调用 noteViewModel.update(note)：

<img width="879" height="186" alt="image" src="https://github.com/user-attachments/assets/323a0d44-a458-46eb-8e0d-76fec5db70ac" />

UI 适配：根据 isTodo 显示 / 隐藏复选框，根据 isCompleted 设置复选框选中状态。

2.5 实时搜索功能

输入监听：MainActivity 为搜索框添加 TextWatcher，监听文本变化；

模糊查询：每次输入变化调用 noteViewModel.searchNotes()，传入拼接通配符的搜索关键词（%query%），观察返回的 LiveData 并更新列表：

<img width="934" height="455" alt="image" src="https://github.com/user-attachments/assets/d93eb1ec-359e-4764-8b7e-c44bee581a3c" />

2.6 分类管理

分类选项：NoteActivity 中通过 Spinner 提供「工作 / 学习 / 生活 / 其他」4 类选项；

数据存储：保存笔记时将选中的分类写入 category 字段；

列表展示：NoteAdapter 中展示笔记分类，便于用户快速识别。

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
                    
