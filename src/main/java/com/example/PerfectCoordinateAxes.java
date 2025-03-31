package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PerfectCoordinateAxes extends Application {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 850;
    private static final int CENTER_X = WIDTH / 2;
    private static final int CENTER_Y = HEIGHT / 2;
    private static final int SCALE = 20; // Пикселей на единицу
    private static final int TICK_LENGTH = 5;
    private static final int AXIS_LENGTH = 20; // Длина оси в единицах
    private static final int AXIS_EXTENSION = 1; // Дополнительное продление оси

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        
        // 1. Рисуем оси с правильными ограничениями
        drawPerfectAxes(root);
        
        // 2. Рисуем параболу y = x²/2 - 3 (вершина в [0, -3])
        drawParabola(root);
        
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.setTitle("Идеальные оси с параболой");
        stage.show();
    }

    private void drawPerfectAxes(Pane root) {
        // Основные линии осей (с продлением на AXIS_EXTENSION)
        Line xAxis = new Line(
            CENTER_X - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, CENTER_Y,
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, CENTER_Y
        );
        xAxis.setStrokeWidth(1.5);
        
        Line yAxis = new Line(
            CENTER_X, CENTER_Y + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE,
            CENTER_X, CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE
        );
        yAxis.setStrokeWidth(1.5);

        // Стрелки только в положительном направлении
        Line xArrow1 = new Line(
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, CENTER_Y,
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE - 10, CENTER_Y - 5
        );
        Line xArrow2 = new Line(
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, CENTER_Y,
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE - 10, CENTER_Y + 5
        );
        
        Line yArrow1 = new Line(
            CENTER_X, CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE,
            CENTER_X - 5, CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE + 10
        );
        Line yArrow2 = new Line(
            CENTER_X, CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE,
            CENTER_X + 5, CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE + 10
        );

        // Подписи осей
        Text xLabel = new Text(
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE - 20, 
            CENTER_Y - 10, 
            "X"
        );
        xLabel.setFont(Font.font(14));
        
        Text yLabel = new Text(
            CENTER_X + 10, 
            CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE + 20, 
            "Y"
        );
        yLabel.setFont(Font.font(14));
        
        Text origin = new Text(CENTER_X + 5, CENTER_Y + 20, "т.О");

        // Добавляем основные элементы
        root.getChildren().addAll(
            xAxis, yAxis, xArrow1, xArrow2, yArrow1, yArrow2,
            xLabel, yLabel, origin
        );

        // Рисуем метки (черточки строго ВНУТРИ основных линий)
        for (int i = -AXIS_LENGTH; i <= AXIS_LENGTH; i++) {
            if (i == 0) continue; // Пропускаем центр
            
            // Метки на оси X
            double xPos = CENTER_X + i * SCALE;
            Line xTick = new Line(
                xPos, CENTER_Y - TICK_LENGTH,
                xPos, CENTER_Y + TICK_LENGTH
            );
            root.getChildren().add(xTick);
            
            // Метки на оси Y
            double yPos = CENTER_Y - i * SCALE;
            Line yTick = new Line(
                CENTER_X - TICK_LENGTH, yPos,
                CENTER_X + TICK_LENGTH, yPos
            );
            root.getChildren().add(yTick);

            // Подписи чисел
            if (i % 5 == 0) {
                Text xText = new Text(
                    xPos - (i < 0 ? 10 : 5), 
                    CENTER_Y + 20, 
                    String.valueOf(i)
                );
                
                Text yText = new Text(
                    CENTER_X + 10, 
                    yPos + 5, 
                    String.valueOf(i)
                );
                
                root.getChildren().addAll(xText, yText);
            }
        }
    }

    private void drawParabola(Pane root) {
        Polyline parabola = new Polyline();
        parabola.setStroke(Color.RED);
        parabola.setStrokeWidth(2);

        for (double x = -AXIS_LENGTH; x <= AXIS_LENGTH; x += 0.1) {
            double y = (x * x) / 2.0 - 3; // Формула с вершиной в [0, -3]
            parabola.getPoints().addAll(
                CENTER_X + x * SCALE,
                CENTER_Y - y * SCALE
            );
        }

        // Подпись вершины
        Text vertexLabel = new Text(
            CENTER_X + 10, 
            CENTER_Y - (-3) * SCALE + 5, 
            "Вершина [0, -3]"
        );
        vertexLabel.setFill(Color.RED);
        
        root.getChildren().addAll(parabola, vertexLabel);
    }

    public static void main(String[] args) {
        launch(args);
    }
}