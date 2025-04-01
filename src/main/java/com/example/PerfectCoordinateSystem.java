package com.example;

// Импорт классов JavaFX для создания графического интерфейса
import javafx.application.Application;  // Базовый класс для JavaFX-приложений
import javafx.geometry.Insets;         // Класс для работы с отступами
import javafx.geometry.Pos;            // Класс для выравнивания элементов
import javafx.scene.Scene;             // Контейнер для содержимого окна
import javafx.scene.control.Label;     // Текстовая метка
import javafx.scene.layout.GridPane;   // Сетка для размещения элементов
import javafx.scene.layout.Pane;       // Базовый контейнер для элементов
import javafx.scene.paint.Color;       // Класс для работы с цветами
import javafx.scene.shape.Circle;      // Класс для рисования окружности
import javafx.scene.shape.Line;        // Класс для рисования линии
import javafx.scene.shape.Polyline;    // Класс для рисования ломаной линии
import javafx.scene.text.Font;         // Класс для работы со шрифтами
import javafx.scene.text.Text;         // Класс для отображения текста
import javafx.stage.Stage;             // Класс, представляющий окно приложения

// Главный класс приложения, наследуется от Application
public class PerfectCoordinateSystem extends Application {

    // Константы для настройки размеров
    private static final int WIDTH = 1024;          // Ширина окна в пикселях
    private static final int HEIGHT = 850;          // Высота окна в пикселях
    private static final int CENTER_X = WIDTH / 2;  // Центр по оси X
    private static final int CENTER_Y = HEIGHT / 2; // Центр по оси Y
    private static final int SCALE = 20;            // Масштаб (пикселей на единицу)
    private static final int TICK_LENGTH = 5;       // Длина делений на осях
    private static final int AXIS_LENGTH = 20;      // Длина осей в единицах
    private static final int AXIS_EXTENSION = 1;    // Дополнительное расширение осей

    // Вершина параболы (x, y) и радиус области для подсказки
    private final double[] vertex = {0, -3};        // Координаты вершины параболы
    private final double VERTEX_RADIUS = 10;        // Радиус области вершины для hover-эффекта

    // Точка входа в приложение
    public static void main(String[] args) {
        launch(args);  // Запуск JavaFX-приложения
    }

    // Основной метод JavaFX, инициализирующий интерфейс
    @Override
    public void start(Stage primaryStage) {
        // Основной контейнер - используем Pane для точного позиционирования
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
        Text coordHint = new Text();          // Текст подсказки
        coordHint.setFont(Font.font(14));     // Устанавливаем шрифт
        coordHint.setFill(Color.BLUE);        // Устанавливаем цвет текста

        // Невидимый круг вокруг вершины для обработки событий мыши
        Circle vertexArea = new Circle(
                CENTER_X + vertex[0] * SCALE, // X-координата вершины
                CENTER_Y - vertex[1] * SCALE, // Y-координата (инвертируем, т.к. ось Y направлена вниз)
                VERTEX_RADIUS, // Радиус области
                Color.TRANSPARENT // Прозрачная заливка
        );
        vertexArea.setStroke(Color.TRANSPARENT);  // Прозрачная граница

        // Обработчики событий для подсказки
        vertexArea.setOnMouseEntered(e -> {   // При наведении мыши
            coordHint.setText(String.format("Вершина: [%.1f, %.1f]", vertex[0], vertex[1]));
            coordHint.setX(e.getSceneX() + 15);  // Позиция подсказки
            coordHint.setY(e.getSceneY() - 15);
            graphPane.getChildren().add(coordHint);  // Добавляем подсказку на сцену
        });

        vertexArea.setOnMouseExited(e -> {    // При уходе мыши
            graphPane.getChildren().remove(coordHint);  // Удаляем подсказку
        });

        vertexArea.setOnMouseMoved(e -> {    // При движении мыши
            coordHint.setX(e.getSceneX() + 15);  // Обновляем позицию подсказки
            coordHint.setY(e.getSceneY() - 15);
        });

        // Добавляем элементы в контейнеры
        graphPane.getChildren().add(vertexArea);
        root.getChildren().addAll(graphPane, coordTable);

        // Создаем сцену (контейнер верхнего уровня)
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.WHITE);
        primaryStage.setTitle("Координатная система с параболой");  // Заголовок окна
        primaryStage.setScene(scene);  // Устанавливаем сцену
        primaryStage.show();           // Показываем окно
    }

    // Метод для создания таблицы с координатами вершины
    private GridPane createCoordTable() {
        GridPane table = new GridPane();  // Создаем сетку
        table.setAlignment(Pos.TOP_RIGHT);  // Выравнивание в правом верхнем углу
        table.setPadding(new Insets(10));   // Внутренние отступы
        table.setHgap(10);                 // Горизонтальный промежуток между ячейками
        table.setVgap(5);                  // Вертикальный промежуток
        // Стиль таблицы через CSS
        table.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-border-color: black; -fx-border-width: 1;");

        // Создаем метки и значения координат
        Label xLabel = new Label("x:");  // Метка для X
        Label yLabel = new Label("y:");  // Метка для Y
        // Устанавливаем стиль через CSS
        xLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        yLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        // Значения координат
        Label xValue = new Label(String.valueOf(vertex[0]));
        Label yValue = new Label(String.valueOf(vertex[1]));
        xValue.setStyle("-fx-font-size: 14;");
        yValue.setStyle("-fx-font-size: 14;");

        // Добавляем элементы в таблицу
        table.addRow(0, xLabel, xValue);  // Первая строка
        table.addRow(1, yLabel, yValue);  // Вторая строка

        return table;
    }

    // Метод для рисования координатных осей
    private void drawAxes(Pane pane) {
        // Ось X (горизонтальная)
        Line xAxis = new Line(
                CENTER_X - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, // Начало оси (левая граница)
                CENTER_Y, // Y-координата оси X
                CENTER_X + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, // Конец оси (правая граница)
                CENTER_Y
        );
        xAxis.setStroke(Color.BLACK);    // Цвет линии
        xAxis.setStrokeWidth(2.0);      // Толщина линии

        // Ось Y (вертикальная)
        Line yAxis = new Line(
                CENTER_X,
                CENTER_Y + (AXIS_LENGTH + AXIS_EXTENSION) * SCALE, // Начало оси (нижняя граница)
                CENTER_X,
                CENTER_Y - (AXIS_LENGTH + AXIS_EXTENSION) * SCALE // Конец оси (верхняя граница)
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
            if (i == 0) {
                continue;  // Пропускаем центр
            }
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
                        xPos - (i < 0 ? 10 : 5), // Смещение для отрицательных чисел
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

    // Метод для рисования параболы y = x²/2 - 3
    private void drawParabola(Pane pane) {
        Polyline parabola = new Polyline();  // Ломаная линия для параболы
        parabola.setStroke(Color.GREEN);       // Цвет линии
        parabola.setStrokeWidth(2.5);       // Толщина линии

        // Вычисляем точки параболы
        for (double x = -AXIS_LENGTH; x <= AXIS_LENGTH; x += 0.1) {
            double y = (x * x) / 2.0 - 3;  // Формула параболы
            // Добавляем точку (преобразуем координаты в пиксели)
            parabola.getPoints().addAll(
                    CENTER_X + x * SCALE, // X в пикселях
                    CENTER_Y - y * SCALE // Y в пикселях (инвертируем)
            );
        }

        // Точка вершины параболы
        Circle vertexPoint = new Circle(
                CENTER_X + vertex[0] * SCALE, // X-координата
                CENTER_Y - vertex[1] * SCALE, // Y-координата
                3, // Радиус точки
                Color.RED // Цвет
        );

        // Подпись формулы параболы
        Text formulaLabel = new Text("y = x²/2 - 3");  // Текст формулы
        formulaLabel.setFont(Font.font("Arial", 16));  // Шрифт и размер
        formulaLabel.setFill(Color.RED);               // Цвет как у параболы

        // Позиция подписи (x = -5)
        double formulaX = -5;                          // X-координата для подписи
        double formulaY = (formulaX * formulaX) / 2.0 - 3;  // Соответствующая Y-координата

        // Угол наклона касательной (производная = x)
        double slope = formulaX;                       // Наклон в точке formulaX
        double angle = Math.toDegrees(Math.atan(slope));  // Угол в градусах

        // Устанавливаем позицию и поворот
        formulaLabel.setX(CENTER_X + formulaX * SCALE + 10);  // Позиция X с небольшим отступом
        formulaLabel.setY(CENTER_Y - formulaY * SCALE - 10);  // Позиция Y с небольшим отступом
        formulaLabel.setRotate(-angle);  // Поворачиваем текст (отрицательный угол для корректного отображения)

        // Добавляем все элементы на график
        pane.getChildren().addAll(parabola, vertexPoint, formulaLabel);
    }
}
