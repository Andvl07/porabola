package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CoordinateAxes extends Application {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final int CENTER_X = WIDTH / 2;
    private static final int CENTER_Y = HEIGHT / 2;
    private static final int SCALE = 20; // пикселей на единицу
    private static final int TICK_LENGTH = 5;
    private static final int AXIS_LENGTH = 20;
    private static final int AXIS_EXTENSION = 1; // дополнительное продление оси

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        
        // Ось X (основная линия)
        Line xAxis = new Line(
            CENTER_X - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, 
            CENTER_Y, 
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, 
            CENTER_Y
        );
        xAxis.setStroke(Color.BLACK);
        xAxis.setStrokeWidth(1.5);
        
        // Ось Y (основная линия)
        Line yAxis = new Line(
            CENTER_X, 
            CENTER_Y + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, 
            CENTER_X, 
            CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE
        );
        yAxis.setStroke(Color.BLACK);
        yAxis.setStrokeWidth(1.5);
        
        // Стрелка оси X (в положительном направлении)
        Line xArrow1 = new Line(
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, CENTER_Y,
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE - 10, CENTER_Y - 5
        );
        Line xArrow2 = new Line(
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, CENTER_Y,
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE - 10, CENTER_Y + 5
        );
        
        // Стрелка оси Y (в положительном направлении)
        Line yArrow1 = new Line(
            CENTER_X, CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE,
            CENTER_X - 5, CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE + 10
        );
        Line yArrow2 = new Line(
            CENTER_X, CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE,
            CENTER_X + 5, CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE + 10
        );
        
        // Подписи осей рядом со стрелками
        Text xAxisLabel = new Text(
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE - 20, 
            CENTER_Y - 15, 
            "X"
        );
        xAxisLabel.setFont(Font.font(14));
        
        Text yAxisLabel = new Text(
            CENTER_X + 15, 
            CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE + 20, 
            "Y"
        );
        yAxisLabel.setFont(Font.font(14));
        
        // Точка пересечения осей
        Text originLabel = new Text(CENTER_X + 5, CENTER_Y + 20, "т.О");
        
        // Добавляем основные элементы на панель
        root.getChildren().addAll(xAxis, yAxis, xArrow1, xArrow2, yArrow1, yArrow2, 
                                xAxisLabel, yAxisLabel, originLabel);
        
        // Рисуем метки на оси X
        for (int i = -AXIS_LENGTH; i <= AXIS_LENGTH; i++) {
            if (i == 0) continue; // пропускаем 0, чтобы не накладывалось на подпись
            
            double x = CENTER_X + i * SCALE;
            // Черточки не выходят за основную линию оси
            if (Math.abs(i) <= AXIS_LENGTH) {
                Line tick = new Line(x, CENTER_Y - TICK_LENGTH, x, CENTER_Y + TICK_LENGTH);
                tick.setStroke(Color.BLACK);
                root.getChildren().add(tick);
            }
            
            // Подписываем числа
            if (i % 5 == 0 && i != 0) {
                Text label = new Text(x - (i < 0 ? 10 : 5), CENTER_Y + 20, Integer.toString(i));
                root.getChildren().add(label);
            }
        }
        
        // Рисуем метки на оси Y
        for (int i = -AXIS_LENGTH; i <= AXIS_LENGTH; i++) {
            if (i == 0) continue; // пропускаем 0
            
            double y = CENTER_Y - i * SCALE;
            // Черточки не выходят за основную линию оси
            if (Math.abs(i) <= AXIS_LENGTH) {
                Line tick = new Line(CENTER_X - TICK_LENGTH, y, CENTER_X + TICK_LENGTH, y);
                tick.setStroke(Color.BLACK);
                root.getChildren().add(tick);
            }
            
            // Подписываем числа
            if (i % 5 == 0 && i != 0) {
                Text label = new Text(CENTER_X + 10, y + 5, Integer.toString(i));
                root.getChildren().add(label);
            }
        }
        
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setTitle("Координатные оси");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}