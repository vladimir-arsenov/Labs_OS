# Лабораторные работы по Операционным Системам
## FileSystem - Файловая система
Создать программу, эмулирующую файловую систему. Должны быть базовые функции: перемещение, удаление, добавление, переименование, чтение и тд. 
Создаётся какой-то контейнер, разбитый на блоки, где хранятся файлы в виде 0 и 1. Если файл заполняет блок не полностью, то остаток блока заполняется 0.
Программа должны уметь переводить текст в двоичное представление. Файл может превышать размер блока.

## Memory - Память компьютера
Есть физическая память, жесткий диск(ВП), различные процессы.
Структура хранения на ФП и ВП - двойной список, где первые значения это номер (или название) процесса, а второй номер страницы в процессе [“процесс 1”,1], [0,0]…].
Структура хранения в процессах - тройной список (хранение в ВП или ФП, номер страницы, некоторые данные [[“ФП”,1,”некоторые данные стр1”],…].
Если ФП заполнена, то случайная строчка из ФП переносится в ВП (меняются записи в процессе с “ФП” на “ВП”) и на ее место становится новая строчка.
При удалении: если процесс находится в ВП, то сначала переносится на ФП и затем удаляется (перенесённый в ВП процесс остаётся там).

## Semaphore - Семафор (потоки)
Потоки борятся за семафор. Поток пришедший первый в контрольную точку, порождает новые потоки, остальные потоки останавливаются.
![image](https://github.com/vladimir-arsenov/OS/assets/84202832/9c04f1fc-cc3d-42fc-8bc2-3dde2c56b322)
