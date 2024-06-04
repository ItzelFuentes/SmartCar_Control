## SmartCar_Control 2024
## Integrantes 
- Álvarez González Sandra Karina
- Ayala Manrique Jorge Luis
- Fuentes Cabrera Itzel Alessandra

## Visión
Desarrollar un sistema IoT integral para vehículos que permita a los conductores controlar el acceso y la seguridad, monitorear datos críticos del vehículo y configurar parámetros personalizados de manera remota, mejorando así la comodidad, seguridad y eficiencia del vehículo.

## Software empleado
  
| Id | Software | Version | Tipo |
|----|----------|---------|------|
| 1  |Arduino   |2.0.1    |   IDE|
| 2  |Node-red  |3.0      |Servicio|
|3|Sqlite|3.46.0|SGBD|

## Hardware empleado
| Id | Componente | Descripción | Imagen | Cantidad | Costo total |
|----|------------|-------------|--------|----------|-------------|
|   1|Pantalla táctil 2.4'' | Resolución de 240*320, color de 18 bits. Controlador spdf5408 con memoria RAM de vídeo integrada. |<img src="https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/9692adcf-d0c8-4b60-a0ec-93edf5e8dc08" width="400">|1|$200|
|   2|Switch interruptor de puerta Sedan Vocho|Switch para controlar las puertas del vehículo VOKSWAGEN Sedan Vocho. |<img src="https://http2.mlstatic.com/D_NQ_NP_729931-MLM52542567964_112022-O.webp" width="400">|2|$100|
|   3|Flotador tanque gasolina Sedan Vocho|El flotador de tanque de gasolina Flotamex para Volkswagen Sedan Vocho (1968-1978) es un sensor de palanca que garantiza lecturas precisas del nivel de combustible en el depósito, mejorando la eficiencia y seguridad del vehículo.|<img src="https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcT9xfEkWam9GfQci0_2qMXFN706pD4-3qgrR-UJ7dHPn98f183bBvIHhETWvM7uIAH1kt6rMZZLOIb1A2wrSx8_d_D0Cd-jzaahV7EEtMtBrL7YbksHhu8u6ib40Y3Uvt8cIdv_yOk&usqp=CAc" width="400">|1|$342| 
|   4|Arduino Mega 2560|El Arduino Mega es una placa de microcontrolador basada en el ATmega2560. Destinada a proyectos más complejos, cuenta con 54 pines digitales de entrada/salida, 16 entradas analógicas, 4 puertos seriales y una mayor memoria. Ideal para aplicaciones que requieren múltiples sensores y actuadores|<img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQToJmLk83X8PZjmg0WeH2C-mQDYY6COj6vIA&s" width="400">|1|$300| 
|   5| Cables de Puente de protoboard de Placa de Prueba| Es un elemento que permite cerrar el circuito eléctrico del que forma parte dos conexiones|<img src="https://user-images.githubusercontent.com/106613946/233218856-3a7458d8-7c8f-49eb-8914-3ab456faff30.png" width="400">|30|$60|
|   6|Módulo Bluetooth Hc-06|El modulo Bluetooth HC-06 utiliza el protocolo UART RS 232 serial. Es ideal para aplicaciones inalámbricas, fácil de implementar con PC, microcontrolador o módulos Arduinos.|<img src="https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/c9f9fddb-f793-4262-819a-b35f75585f87" width="400">|1|$80|
|   7|Modulo Reloj de tiempo real Rtc Ds3231| Mantiene registro de: segundos, minutos, horas, día de la semana, fecha, mes y año. La fecha es ajustada automáticamente a final de mes para meses con menos de 31 días, incluyendo las correcciones para año bisiesto.|<img src="https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/a63fc699-6c6e-458d-953b-9c904f381b41" width="400">|1|$80|
|   8|Buzzer| Es un dispositivo que tiene la capacidad de generar una señal de audio al ser alimentado|<img src="https://user-images.githubusercontent.com/106613946/233209389-17df52bd-0712-4ba0-9873-b426f1123213.png" width="400">|1|$15| 
|   9|Cable USB para Arduino mega| Cable USB que permite la alimentación y comunicación compatible con Arduino Mega.|<img src="https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/36ffa2b7-0adc-48ec-9e8c-b3cbd7d995dd" width="400">|1|$25|
|  10|Fotorresistencia|Es un componente electrónico cuya resistencia disminuye con el aumento de intensidad de luz incidente.|<img src="https://github.com/ItzelFuentes/SmartCar_Control/assets/106613946/3edfb97d-ca44-4a34-9ee3-f907f25e72a7" width="400">|1|$45|


<!-- |   12|Protoboard|es prácticamente una PCB temporal con una forma y tamaño generalizados. Utilizada comúnmente para pruebas y prototipos temporales de circuitos|![image](https://user-images.githubusercontent.com/106613946/233217981-e8de4fd7-44f6-463f-b818-45785af1aeb5.png)|1|$0| 
|   13|Adaptador de fuente de alimentación de 12V 5A| Es un dispositivo electrónico comúnmente llamado fuente de alimentación, fuente de poder o fuente conmutada|![image](https://user-images.githubusercontent.com/106613946/233218241-90c26d7e-14da-4751-9295-47106387fa82.png)|1|$0| 
|   14| Módulo Relevadores Relay 4 Canales 5v | Puede controlar varios dispositivos y otros equipos con gran corriente: ideal para proyectos mecatrónicos. Este relevador soporta hasta 250 VAC o 30 VDC a 10 A.|![image](https://m.media-amazon.com/images/I/61dFbJu-y7L._SX522_.jpg)|1|$0|
|   1|ESP32| El módulo ESP32 es una solución de Wi-Fi/Bluetooth todo en uno, integrada y certificada que proporciona no solo la radio inalámbrica, sino también un procesador integrado con interfaces para conectarse con varios periféricos|![image](https://user-images.githubusercontent.com/104101668/214482867-90fbaa8c-7d6c-42f5-8a1c-004b815030a6.png)|1|$100|-->

## Historias de usuario epicas
| Id | Historia de usuario | Prioridad | Estimación | Como probarlo | Responsable |
|----|---------------------|-----------|------------|---------------|-------------|
|  1 | Control de acceso y seguridad del vehículo. |Alta|3-4 semanas|Abrir y cerrar los seguros desde la aplicación móvil, verificar el cierre automático al alejarse y desconectar Bluetooth, y probar el encendido automático de luces en baja luminosidad.|Jorge|
|  2 |Monitoreo de datos del vehículo. |Alta|3-4 semanas|Visualizar en la aplicación móvil los datos del vehículo como nivel de gasolina, nivel de luz y temperatura, y verificar que se puedan monitorear estos datos de manera remota.|Itzel|
|  3 |Configuración remota del vehículo.|Alta|3-4 semanas|Configurar el tiempo de espera para el cierre automático de los seguros desde la aplicación móvil y verificar que la configuración se aplique correctamente de forma remota.|Sandra|


## Fotografía tomada del dibujo del prototipo propuesto por el equipo para el proyecto


## Arquitectura del proyecto
se solicita que contenga al menos los siguientes elementos: sensores, actuadores, controladores, plataforma de base de datos, protocolo de comunicación, gestión de energía, dispositivos receptores y/o transmisores, puede contener además elementos de nube, redes, seguridad, dispositivos pasivos, direccionalidad de comunicación

## Captura de pantalla del tablero kanban para cada uno de los sprints del proyecto
En cada sprint se contemplan los siguientes tableros: Entrada, análisis, construcción, revisión y terminado.

| Sprint | Tablero Trello | 
|----|----------|
| 1  |![image](https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/cd066a9d-b3c2-4754-930b-8b0534ba15f6)|
| 2 |![image](https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/c453dfaf-1874-41cb-9672-a3620610aee0) ![image](https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/133eb33a-a6de-472c-bf37-e1e14e0671ed)|
| 3 |![image](https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/affa7877-c9ee-4b10-99e4-893a795a47d2) ![image](https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/8037ad2f-8811-4e55-91f0-b00f84e035c5)|



## Circuito diseñado para el proyecto completo 
![image](https://github.com/ItzelFuentes/SmartCar_Control/assets/106613946/ee4bb620-b094-4952-a988-cab3342de3bc)



## Resultados, screenshot de cada uno de los resultados del proyecto, con una descripción sintetizada
En esta primera entrega se logró concretar un circuito integrado por un arduino mega, una pantalla touch de 2.4'', un buzzer, un módulo bluetooth y un módulo reloj. Se logró enlazar la pantalla a con una app móvil a través de bluetooth.

| Circuito | Aplicación móvil | 
|----|----------|
|<img src = "https://github.com/ItzelFuentes/SmartCar_Control/assets/106613946/591c2e9b-1f85-4306-8d99-eb7074ee0edb" width="500"> <img src = "https://github.com/ItzelFuentes/SmartCar_Control/assets/106613946/ebb0a19d-2160-47e3-a07e-3874fd91453c" width="500"> | <img src = "https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/de7cfabb-ed8e-427b-8c33-7caf32ed3ab4" width="500"> <img src = "https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/45584c5a-5581-43be-bda1-567bdcdd6adc" width="500">|













