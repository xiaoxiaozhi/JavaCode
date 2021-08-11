package com.zhixun.javacode.annotation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.zhixun.javacode.R;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AnnotationActivity extends AppCompatActivity {

    Button button;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
        button = findViewById(R.id.button);
        text = findViewById(R.id.textView);
        ActionListenerInstaller.processAnnotation(this);
        Path path = Paths.get("");
        try {
            Files.createFile(path);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ActionListenerFor(source = "button")
    public void click() {
        button.setText("click");
    }
}