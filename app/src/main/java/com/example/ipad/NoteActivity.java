package com.example.ipad;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class NoteActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextContent;
    private Spinner spinnerCategory;
    private CheckBox checkBoxIsTodo;
    private Button btnSave;
    private Button btnDelete;
    private NoteViewModel noteViewModel;
    private Note currentNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initViews();
        setupViewModel();
        setupSpinner();
        checkEditMode();
        setupListeners();
    }

    private void initViews() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        checkBoxIsTodo = findViewById(R.id.checkBoxIsTodo);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void setupViewModel() {
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
    }

    private void setupSpinner() {
        String[] categories = {"工作", "学习", "生活", "其他"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void checkEditMode() {
        Intent intent = getIntent();
        if (intent.hasExtra("note_id")) {
            setTitle("编辑笔记");
            loadNote(intent.getIntExtra("note_id", -1));
        } else {
            setTitle("新建笔记");
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void loadNote(int noteId) {
        noteViewModel.getAllNotes().observe(this, notes -> {
            for (Note note : notes) {
                if (note.getId() == noteId) {
                    currentNote = note;
                    editTextTitle.setText(note.getTitle());
                    editTextContent.setText(note.getContent());
                    checkBoxIsTodo.setChecked(note.isTodo());

                    // 设置分类选择
                    String[] categories = {"工作", "学习", "生活", "其他"};
                    for (int i = 0; i < categories.length; i++) {
                        if (categories[i].equals(note.getCategory())) {
                            spinnerCategory.setSelection(i);
                            break;
                        }
                    }
                    break;
                }
            }
        });
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveNote());
        btnDelete.setOnClickListener(v -> deleteNote());
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, "笔记不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentNote == null) {
            currentNote = new Note();
            currentNote.setTimestamp(System.currentTimeMillis());
        }

        currentNote.setTitle(title);
        currentNote.setContent(content);
        currentNote.setCategory(category);
        currentNote.setTodo(checkBoxIsTodo.isChecked());

        if (currentNote.getId() == 0) {
            noteViewModel.insert(currentNote);
        } else {
            noteViewModel.update(currentNote);
        }

        finish();
    }

    private void deleteNote() {
        if (currentNote != null && currentNote.getId() != 0) {
            noteViewModel.delete(currentNote);
            finish();
        }
    }
}

