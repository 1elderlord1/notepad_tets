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

项目简介

ModernNotePad 是一款基于 Android 平台的轻量级记事本应用，采用 MVVM 架构设计，支持笔记的增删改查、分类管理、待办事项标记、实时搜索等核心功能，通过 Room 实现本地数据持久化，保证数据不丢失。应用遵循 Material Design 设计规范，提供简洁流畅的用户体验。

技术栈

类别	技术 / 工具
    
开发语言        Java

构建工具        	Gradle (Kotlin DSL)

架构模式    	    MVVM（Model-View-ViewModel）

数据持久化	    Room Database（Android 官方推荐的本地数据库框架）

生命周期管理    	ViewModel + LiveData（感知生命周期，避免内存泄漏）

UI 组件	        RecyclerView、Material Design（CardView/TextInputLayout/FAB）、Spinner 等

异步处理	        ExecutorService（单线程池处理数据库异步操作）

其他	        TextWatcher（实时搜索）、Intent（页面跳转）、Toast（用户提示）

核心架构与实现机制

1. 架构设计：MVVM
2. 
应用采用 MVVM 架构解耦 UI 层与数据层，各层职责清晰，便于维护和扩展：

plaintext

View (Activity/Adapter) <--> ViewModel <--> Repository <--> Data Layer (Room)

各层职责说明

层级	                                        核心组件	                                                    职责

View 层	                    MainActivity、NoteActivity、NoteAdapter	            负责 UI 展示、用户交互（点击 / 输入 / 长按等），通过观察 LiveData 响应数据变化

ViewModel 层	                NoteViewModel                                  	作为 View 与 Repository 的中间层，持有 Repository 实例，暴露可观察数据，处理 UI 相关业务逻辑，不持有 UI 引用

Repository 层	                NoteRepository	                                封装数据操作逻辑，统一管理数据源（本项目仅本地 Room 数据库），处理异步任务，隔离 ViewModel 与数据层

Data 层	                    Note（Entity）、NoteDao、AppDatabase	                负责数据持久化，通过 Room 实现数据库的创建、增删改查

4. 核心功能实现机制

2.1 数据模型与持久化（Room）
   
（1）笔记实体定义（Note.java）

通过 Room 的 @Entity 注解标记为数据库表，定义核心字段并提供 getter/setter：

主键：id

内容字段：title（标题）、content（内容）、timestamp（时间戳）、category（分类）

扩展字段：color（背景色）、isTodo（是否为待办）、isCompleted（待办是否完成）

辅助方法：getFormattedTime() 格式化时间戳为「MM-dd HH:mm」格式，用于 UI 展示

（2）数据库操作（NoteDao + AppDatabase）

NoteDao：数据访问接口，通过 Room 注解定义数据库操作，核心方法包括：

// 查询所有笔记（返回 LiveData，自动感知数据变化）
@Query("SELECT * FROM notes")
LiveData<List<Note>> getAllNotes();

// 模糊搜索笔记（匹配标题/内容）
@Query("SELECT * FROM notes WHERE title LIKE :query OR content LIKE :query")
LiveData<List<Note>> searchNotes(String query);

// 增删改操作
@Insert
void insert(Note note);
@Update
void update(Note note);
@Delete
void delete(Note note);

AppDatabase：单例模式的数据库类，继承 RoomDatabase，提供 NoteDao 实例，保证全局唯一数据库连接。

（3）异步操作封装（NoteRepository）

通过 ExecutorService 单线程池执行数据库操作，避免主线程阻塞：


public void update(Note note) {
    executorService.execute(() -> noteDao.update(note));
}

2.2 笔记增删改查（CRUD）
（1）新增 / 编辑笔记（NoteActivity）
页面跳转：MainActivity 点击「+」按钮（FAB）跳转到 NoteActivity（新建模式）；点击笔记项传递 note_id 跳转到 NoteActivity（编辑模式）
数据填充：编辑模式下，通过 noteViewModel.getAllNotes().observe() 监听数据，匹配 note_id 加载对应笔记内容到输入框
保存逻辑：
校验：标题和内容不能为空
新建：调用 noteViewModel.insert(currentNote)，设置时间戳为当前毫秒数
编辑：调用 noteViewModel.update(currentNote)，复用原有 id 更新数据
（2）删除笔记
长按删除：MainActivity 中长按笔记项，触发 noteViewModel.delete(note)
编辑页删除：NoteActivity 点击「删除」按钮，调用 noteViewModel.delete(currentNote) 并关闭页面
（3）笔记列表展示（MainActivity + NoteAdapter）
ViewModel 层暴露 getAllNotes()（返回 LiveData<List<Note>>），MainActivity 观察该数据：
java
运行
noteViewModel.getAllNotes().observe(this, notes -> {
    adapter.setNotes(notes); // 数据变化时更新 Adapter
});
NoteAdapter：RecyclerView 适配器，封装 ViewHolder 展示笔记卡片，核心逻辑：
绑定数据：设置标题、内容、时间、分类、背景色
待办适配：根据 isTodo 显示 / 隐藏 CheckBox，同步 isCompleted 状态
2.3 待办事项功能
标记待办：NoteActivity 中通过 CheckBox 设置 isTodo 字段，保存时写入数据库
状态切换：NoteAdapter 中监听 CheckBox 状态变化，触发 onTodoChecked 回调，更新 isCompleted 并调用 noteViewModel.update(note)：
java
运行
holder.checkBoxTodo.setOnCheckedChangeListener((buttonView, isChecked) -> {
    if (listener != null) {
        listener.onTodoChecked(currentNote, isChecked);
    }
});
2.4 实时搜索功能
输入监听：MainActivity 的搜索框添加 TextWatcher，监听文本变化
搜索逻辑：每次文本变化调用 noteViewModel.searchNotes(s.toString())，观察返回的 LiveData 并更新列表：
java
运行
searchEditText.addTextChangedListener(new TextWatcher() {
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        noteViewModel.searchNotes(s.toString()).observe(MainActivity.this, notes -> {
            adapter.setNotes(notes); // 实时更新搜索结果
        });
    }
});
2.5 笔记分类管理
分类选项：NoteActivity 中通过 Spinner 提供「工作 / 学习 / 生活 / 其他」4 类选项
数据存储：保存笔记时将选中的分类写入 category 字段
展示：NoteAdapter 中展示笔记的分类信息，便于用户快速识别
3. UI 交互与体验优化
Material Design 规范：使用 MaterialCardView 作为笔记卡片，TextInputLayout 优化输入体验，FAB 按钮突出「新增笔记」核心操作
列表分割线：RecyclerView 添加 DividerItemDecoration，提升列表可读性
页面跳转：通过 Intent 传递参数，编辑模式自动填充数据，新建模式隐藏「删除」按钮
提示反馈：空笔记保存时通过 Toast 提示「笔记不能为空」，提升用户感知

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
                    
