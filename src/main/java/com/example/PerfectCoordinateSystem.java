package com.example;


// Импорт необходимых классов JavaFX
import javafx.application.Application;  // Базовый класс для JavaFX-приложений
import javafx.geometry.Insets;         // Для задания отступов
import javafx.geometry.Pos;            // Для выравнивания элементов
import javafx.scene.Scene;             // Контейнер для содержимого окна
import javafx.scene.control.Label;     // Текстовые метки
import javafx.scene.layout.GridPane;   // Сетка для размещения элементов
import javafx.scene.layout.Pane;       // Базовый контейнер для графики
import javafx.scene.paint.Color;  // Контейнер с наложением элементов
import javafx.scene.shape.Circle;       // Работа с цветами
import javafx.scene.shape.Line;      // Круг/окружность
import javafx.scene.shape.Polyline;        // Линия
import javafx.scene.text.Font;   // Ломаная линия
import javafx.scene.text.Text;        // Настройки шрифта
import javafx.stage.Stage;        // Текстовый элемент

/**
 * Класс PerfectCoordinateSystem реализует координатную систему с параболой.
 * Наследуется от Application - базового класса JavaFX-приложений.
 */
public class PerfectCoordinateSystem extends Application {
    // Константы для настройки размеров и масштаба
    private static final int WIDTH = 1024;          // Ширина окна
    private static final int HEIGHT = 850;          // Высота окна
    private static final int CENTER_X = WIDTH / 2;  // Центр по X
    private static final int CENTER_Y = HEIGHT / 2; // Центр по Y
    private static final int SCALE = 20;            // Масштаб (пикселей на единицу)
    private static final int TICK_LENGTH = 5;       // Длина делений на осях
    private static final int AXIS_LENGTH = 20;      // Длина осей в единицах
    private static final int AXIS_EXTENSION = 1;     // Дополнительное расширение осей

    // Вершина параболы (x, y) и радиус области для подсказки
    private final double[] vertex = {0, -3};
    private final double VERTEX_RADIUS = 10;

    /**
     * Точка входа в приложение
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        launch(args);  // Запуск JavaFX-приложения
    }

    /**
     * Основной метод JavaFX, инициализирующий интерфейс
     * @param primaryStage главное окно приложения
     */
    @Override
    public void start(Stage primaryStage) {
        // Основной контейнер - используем Pane вместо StackPane для точного позиционирования
        Pane root = new Pane();
        
        // Контейнер для графики (осей и параболы)
        Pane graphPane = new Pane();
        graphPane.setPrefSize(WIDTH, HEIGHT);  // Устанавливаем размер
        
        // 1. Рисуем оси координат
        drawAxes(graphPane);
        
        // 2. Рисуем параболу
        drawParabola(graphPane);
        
        // 3. Создаем таблицу координат
        GridPane coordTable = createCoordTable();
        // Позиционируем таблицу в правом верхнем углу
        coordTable.setLayoutX(WIDTH - 150);
        coordTable.setLayoutY(10);
        
        // 4. Настраиваем подсказку для вершины параболы
        Text coordHint = new Text();
        coordHint.setFont(Font.font(14));  // Шрифт подсказки
        coordHint.setFill(Color.BLUE);     // Цвет текста
        
        // Невидимый круг вокруг вершины для обработки событий мыши
        Circle vertexArea = new Circle(
            CENTER_X + vertex[0] * SCALE,  // X-координата вершины
            CENTER_Y - vertex[1] * SCALE,  // Y-координата (инвертируем, т.к. ось Y направлена вниз)
            VERTEX_RADIUS,                 // Радиус области
            Color.TRANSPARENT              // Прозрачная заливка
        );
        vertexArea.setStroke(Color.TRANSPARENT);  // Прозрачная граница
        
        // Обработчики событий для подсказки
        vertexArea.setOnMouseEntered(e -> {
            coordHint.setText(String.format("Вершина: [%.1f, %.1f]", vertex[0], vertex[1]));
            coordHint.setX(e.getSceneX() + 15);
            coordHint.setY(e.getSceneY() - 15);
            graphPane.getChildren().add(coordHint);
        });
        
        vertexArea.setOnMouseExited(e -> {
            graphPane.getChildren().remove(coordHint);
        });
        
        vertexArea.setOnMouseMoved(e -> {
            coordHint.setX(e.getSceneX() + 15);
            coordHint.setY(e.getSceneY() - 15);
        });
        
        // Добавляем элементы в контейнеры
        graphPane.getChildren().add(vertexArea);
        root.getChildren().addAll(graphPane, coordTable);
        
        // Создаем сцену (контейнер верхнего уровня)
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.WHITE);
        primaryStage.setTitle("Координатная система с параболой");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Создает таблицу с координатами вершины
     * @return GridPane с координатами
     */
    private GridPane createCoordTable() {
        GridPane table = new GridPane();
        table.setAlignment(Pos.TOP_RIGHT);  // Выравнивание в правом верхнем углу
        table.setPadding(new Insets(10));   // Внутренние отступы
        table.setHgap(10);                  // Горизонтальный промежуток между ячейками
        table.setVgap(5);                   // Вертикальный промежуток
        // Стиль таблицы: белый фон с полупрозрачностью и черная рамка
        table.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-border-color: black; -fx-border-width: 1;");
        
        // Создаем метки и значения координат
        Label xLabel = new Label("x:");
        Label yLabel = new Label("y:");
        // Устанавливаем стиль через CSS
        xLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        yLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        
        Label xValue = new Label(String.valueOf(vertex[0]));
        Label yValue = new Label(String.valueOf(vertex[1]));
        xValue.setStyle("-fx-font-size: 14;");
        yValue.setStyle("-fx-font-size: 14;");
        
        // Добавляем элементы в таблицу
        table.addRow(0, xLabel, xValue);
        table.addRow(1, yLabel, yValue);
        
        return table;
    }

    /**
     * Рисует координатные оси с делениями и подписями
     * @param pane контейнер для рисования
     */
    private void drawAxes(Pane pane) {
        // Ось X (горизонтальная)
        Line xAxis = new Line(
            CENTER_X - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE,  // Начало оси (левая граница)
            CENTER_Y,                                            // Y-координата оси X
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE,   // Конец оси (правая граница)
            CENTER_Y
        );
        xAxis.setStroke(Color.BLACK);    // Цвет линии
        xAxis.setStrokeWidth(2.0);      // Толщина линии
        
        // Ось Y (вертикальная)
        Line yAxis = new Line(
            CENTER_X, 
            CENTER_Y + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE,   // Начало оси (нижняя граница)
            CENTER_X, 
            CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE    // Конец оси (верхняя граница)
        );
        yAxis.setStroke(Color.BLACK);
        yAxis.setStrokeWidth(2.0);

        // Стрелка оси X (правый конец)
        Line xArrow1 = new Line(
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, CENTER_Y,
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE - 15, CENTER_Y - 8
        );
        xArrow1.setStroke(Color.BLACK);
        xArrow1.setStrokeWidth(2.0);
        
        Line xArrow2 = new Line(
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, CENTER_Y,
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE - 15, CENTER_Y + 8
        );
        xArrow2.setStroke(Color.BLACK);
        xArrow2.setStrokeWidth(2.0);
        
        // Стрелка оси Y (верхний конец)
        Line yArrow1 = new Line(
            CENTER_X, CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE,
            CENTER_X - 8, CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE + 15
        );
        yArrow1.setStroke(Color.BLACK);
        yArrow1.setStrokeWidth(2.0);
        
        Line yArrow2 = new Line(
            CENTER_X, CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE,
            CENTER_X + 8, CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE + 15
        );
        yArrow2.setStroke(Color.BLACK);
        yArrow2.setStrokeWidth(2.0);

        // Подписи осей
        Text xLabel = new Text(
            CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE - 25, 
            CENTER_Y - 15, 
            "X"
        );
        xLabel.setFont(Font.font("Arial", 16));  // Шрифт Arial, размер 16
        xLabel.setFill(Color.BLACK);
        
        Text yLabel = new Text(
            CENTER_X + 15, 
            CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE + 25, 
            "Y"
        );
        yLabel.setFont(Font.font("Arial", 16));
        yLabel.setFill(Color.BLACK);
        
        // Подпись начала координат
        Text origin = new Text(CENTER_X + 5, CENTER_Y + 25, "т.О");
        origin.setFont(Font.font("Arial", 14));
        origin.setFill(Color.BLACK);

        // Добавляем все элементы оси в контейнер
        pane.getChildren().addAll(
            xAxis, yAxis, xArrow1, xArrow2, yArrow1, yArrow2,
            xLabel, yLabel, origin
        );

        // Рисуем деления и подписи на осях
        for (int i = -AXIS_LENGTH; i <= AXIS_LENGTH; i++) {
            if (i == 0) continue;  // Пропускаем центр
            
            // Деления на оси X
            double xPos = CENTER_X + i * SCALE;
            Line xTick = new Line(xPos, CENTER_Y - TICK_LENGTH, xPos, CENTER_Y + TICK_LENGTH);
            xTick.setStroke(Color.BLACK);
            
            // Деления на оси Y
            double yPos = CENTER_Y - i * SCALE;
            Line yTick = new Line(CENTER_X - TICK_LENGTH, yPos, CENTER_X + TICK_LENGTH, yPos);
            yTick.setStroke(Color.BLACK);

            // Подписи для делений, кратных 5
            if (i % 5 == 0) {
                // Подпись на оси X
                Text xText = new Text(
                    xPos - (i < 0 ? 10 : 5),  // Смещение для отрицательных чисел
                    CENTER_Y + 20, 
                    String.valueOf(i)
                );
                xText.setFont(Font.font("Arial", 12));
                xText.setFill(Color.BLACK);
                
                // Подпись на оси Y
                Text yText = new Text(
                    CENTER_X + 10, 
                    yPos + 5, 
                    String.valueOf(i)
                );
                yText.setFont(Font.font("Arial", 12));
                yText.setFill(Color.BLACK);
                
                // Добавляем элементы
                pane.getChildren().addAll(xTick, yTick, xText, yText);
            } else {
                // Только деления без подписей
                pane.getChildren().addAll(xTick, yTick);
            }
        }
    }

    /**
     * Рисует параболу y = x²/2 - 3
     * @param pane контейнер для рисования
     */
    private void drawParabola(Pane pane) {
        Polyline parabola = new Polyline();  // Ломаная линия для параболы
        parabola.setStroke(Color.RED);       // Цвет линии
        parabola.setStrokeWidth(2.5);       // Толщина линии

        // Вычисляем точки параболы
        for (double x = -AXIS_LENGTH; x <= AXIS_LENGTH; x += 0.1) {
            double y = (x * x) / 2.0 - 3;  // Формула параболы
            // Добавляем точку (преобразуем координаты в пиксели)
            parabola.getPoints().addAll(
                CENTER_X + x * SCALE,      // X в пикселях
                CENTER_Y - y * SCALE       // Y в пикселях (инвертируем)
            );
        }

        // Точка вершины параболы
        Circle vertexPoint = new Circle(
            CENTER_X + vertex[0] * SCALE,  // X-координата
            CENTER_Y - vertex[1] * SCALE,   // Y-координата
            3,                             // Радиус точки
            Color.RED                       // Цвет
        );
        
        // Добавляем параболу и вершину на график
        pane.getChildren().addAll(parabola, vertexPoint);
    }
}