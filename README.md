TestNettyServer
===============
Приложение(http-сервер на фреймворке netty) собрано и экспортировано в runnable jar файл посредством Eclipse.

 TraficHandler--класс для обработки статистика соединения.
 RequestsHandler -- класс для обработки запроса и формирования ответа.
 ServerInfo --класс для хранения статистики сервера. Так как к этой информации одновременно обращаться будут обращаться несколько потоков, в нужных местах она синхронизирована.
 ConnectionInfo -- класс для гранения статистики соединения.

1. По запросу на "http://localhost:8080/hello" отдает «Hello World» через 10 секунд;
2. По запросу на "http://localhost:8080/redirect?url=<url>" происходит переадресация на указанный url;
3. По запросу на "http://localhost:8080/status" выдается статистика:
	Total number of requests -- Общее количество запросов
	Number of unique requests (one IP) -- количество уникальных запросов (по одному на IP)
	Requests information -- счетчик запросов на каждый IP в виде таблицы с колонкам и IP, кол-во запросов, время последнего запроса
	Url requests information -- количество переадресаций по url'ам  в виде таблицы, с колонками url, кол-во переадресация
	Number of connections that are currently open -- количество соединений, открытых в данный моментколичество соединений, открытых в данный момент
	Last 16 connections info -- в виде таблицы лог из 16 последних обработанных соединений, колонки src_ip, URI, timestamp(время, когда запрос был принят),  sent_bytes(байты записанные каналом), received_bytes(байты прочитанные каналом), speed(скорось чтения данных) (bytes/sec)


	IP указывается без номера порта.
	Для более корректного подсчета скорости необходимо отправлять или принимать большего объема. В данном случае пакеты слишком маленькие для корректного подсчета скорости, так как данные приходят слишком быстро(иногда быстрее чем за одну миллисекунду).

	Также прилагаются:
		Status_screenshot_1 -- на котором вино ччто для каждого рабоего запроса /hello выделяется отдельный канал.
		Status_screenshot_2 -- статистика после вызова /redirect...
		Command_screenshot -- скриншот результата выполнения команды ab –c 100 –n 10000 “http://localhost:8080/status"

Для того, чтобы сбилдить проект нужна netty-4.0.21(http://netty.io/downloads.html) главны класс HttpServer. 
Добавить библиотеку : (ПКМ на проект)Properties ->java build path -> add jas-> добавить netty-all-4.0.21.Final
Настроить Run configurations:  Run -> Run configurations -> Project: TestHTTPServer   Main class: hello.HttpServer -> Apply -> Run  
Export: (ПКМ на проект)Export -> Runnuble jar file -> Launch configuration: run configurations, который мы создали   extract...-галочка
