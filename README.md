# Heroes — задание студента

Этот модуль содержит реализации четырех обязательных алгоритмов для проекта Heroes Battle.

## Модули проекта
- heroes: клиент игры (графика и запуск боя).
- heroes_task: модуль задания студента (этот репозиторий).
- heroes_task_lib: общая библиотека с интерфейсами/классами (поставляется как `libs/heroes_task_lib-1.0-SNAPSHOT.jar`).

## Реализованные алгоритмы
- GeneratePreset.generate: формирует армию компьютера жадно по коэффициенту «атака/стоимость» (при равенстве — «здоровье/стоимость»), соблюдает лимит 11 юнитов каждого типа и заполняет позиции на левом краю поля.
- SimulateBattle.simulate: выполняет бой по раундам, сортирует юнитов по базовой атаке в каждом раунде, проводит атаки и логирует каждое действие.
- SuitableForAttackUnitsFinder.getSuitableUnits: возвращает юнитов противника, которые не прикрыты со стороны подхода (слева или справа, в зависимости от целевой армии).
- UnitTargetPathFinder.getTargetPath: находит кратчайший путь на сетке 27x21 с помощью BFS с диагональными перемещениями и препятствиями в виде юнитов.

## Сложность
Пусть T — число типов юнитов (размер unitList), N — максимум юнитов в армии, U — живые юниты на поле, W = 27, H = 21, R — число раундов.

- GeneratePreset.generate: O(T log T + N) по времени, O(T + N) по памяти; сортировка типов O(T log T) и линейное заполнение позиций O(N).
- SuitableForAttackUnitsFinder.getSuitableUnits: O(U + W*H) по времени, O(W*H) по памяти; один проход по юнитам и константная решетка W*H.
- UnitTargetPathFinder.getTargetPath: O(W*H + U) по времени, O(W*H) по памяти; BFS по клеткам с блокировкой занятых позиций.
- SimulateBattle.simulate: на раунд O(U log U + U*A), где A = O(U) для attack() => O(U^2) на раунд; всего O(R * U^2).

## Сборка JAR (CLI)
Из этой директории:

```bash
javac -cp libs/heroes_task_lib-1.0-SNAPSHOT.jar -d build/classes src/programs/*.java
jar cf dist/heroes_student_task.jar -C build/classes .
```

Собранный JAR будет находиться здесь:
- dist/heroes_student_task.jar

## Использование в игре
Скопируйте `dist/heroes_student_task.jar` в папку игры `heroes/jars/` и замените `obf.jar`.

## Зависимости
- libs/heroes_task_lib-1.0-SNAPSHOT.jar (поставляется вместе с проектом).
