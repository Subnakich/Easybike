# EasyBike
**EasyBike** - приложение на Android, которое использует Google Maps API для отслеживания местоположения пользователя.

## Цель проекта

1. Научиться работать с библиотекой Room, которая предоставляет удобную обертку для работы с базой данных SQLite.
2. Научиться применять Kotlin Coroutines для взаимодействия с базой данных, чтобы не блокировать пользовательский интерфейс.
3. Использовать Google Maps API и SDK для встраивания карты в приложение и работы с точками местоположения. 
4. Внедрить сервис, который позволит отслеживать местоположение даже когда телефон заблокирован.
5. Использовать MVVM и Clean Architecture 
6. Научиться работать с android services для определения местоположения

## Модель базы данных
В базе данных находятся 3 таблицы
1. Таблица пользователей
2. Таблица путешествий
3. Таблица точек путешествия, которая реализует отношение many-to-one к таблице путешествий по journeyID

![QuickDBD-export](https://user-images.githubusercontent.com/71465945/143700429-c2df3ba4-874f-41ea-b78d-fb5898fc0bf7.png)
