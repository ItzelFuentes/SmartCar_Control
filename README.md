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
| 2  |B4A  |12.80      |IDE|

## Hardware empleado
| Id | Componente | Descripción | Imagen | Cantidad | Costo total |
|----|------------|-------------|--------|----------|-------------|
|   1|Pantalla táctil 2.4'' | Resolución de 240*320, color de 18 bits. Controlador spdf5408 con memoria RAM de vídeo integrada. |<img src="https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/9692adcf-d0c8-4b60-a0ec-93edf5e8dc08" width="400">|1|$200|
|   2|Switch interruptor de puerta Sedan Vocho|Switch para controlar las puertas del vehículo VOKSWAGEN Sedan Vocho. |<img src="https://http2.mlstatic.com/D_NQ_NP_729931-MLM52542567964_112022-O.webp" width="400">|3|$150|
|   3|Flotador tanque gasolina Sedan Vocho|El flotador de tanque de gasolina Flotamex para Volkswagen Sedan Vocho (1968-1978) es un sensor de palanca que garantiza lecturas precisas del nivel de combustible en el depósito, mejorando la eficiencia y seguridad del vehículo.|<img src="https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcT9xfEkWam9GfQci0_2qMXFN706pD4-3qgrR-UJ7dHPn98f183bBvIHhETWvM7uIAH1kt6rMZZLOIb1A2wrSx8_d_D0Cd-jzaahV7EEtMtBrL7YbksHhu8u6ib40Y3Uvt8cIdv_yOk&usqp=CAc" width="400">|1|$342| 
|   4|Arduino Mega 2560|El Arduino Mega es una placa de microcontrolador basada en el ATmega2560. Destinada a proyectos más complejos, cuenta con 54 pines digitales de entrada/salida, 16 entradas analógicas, 4 puertos seriales y una mayor memoria. Ideal para aplicaciones que requieren múltiples sensores y actuadores|<img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQToJmLk83X8PZjmg0WeH2C-mQDYY6COj6vIA&s" width="400">|1|$300| 
|   5| Cables de Puente de protoboard de Placa de Prueba| Es un elemento que permite cerrar el circuito eléctrico del que forma parte dos conexiones|<img src="https://user-images.githubusercontent.com/106613946/233218856-3a7458d8-7c8f-49eb-8914-3ab456faff30.png" width="400">|30|$60|
|   6|Módulo Bluetooth Hc-06|El modulo Bluetooth HC-06 utiliza el protocolo UART RS 232 serial. Es ideal para aplicaciones inalámbricas, fácil de implementar con PC, microcontrolador o módulos Arduinos.|<img src="https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/c9f9fddb-f793-4262-819a-b35f75585f87" width="400">|1|$80|
|   7|Modulo Reloj de tiempo real Rtc Ds3231| Mantiene registro de: segundos, minutos, horas, día de la semana, fecha, mes y año. La fecha es ajustada automáticamente a final de mes para meses con menos de 31 días, incluyendo las correcciones para año bisiesto.|<img src="https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/a63fc699-6c6e-458d-953b-9c904f381b41" width="400">|1|$80|
|   8|Buzzer| Es un dispositivo que tiene la capacidad de generar una señal de audio al ser alimentado|<img src="https://user-images.githubusercontent.com/106613946/233209389-17df52bd-0712-4ba0-9873-b426f1123213.png" width="400">|2|$30| 
|   9|Cable USB para Arduino mega| Cable USB que permite la alimentación y comunicación compatible con Arduino Mega.|<img src="https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/36ffa2b7-0adc-48ec-9e8c-b3cbd7d995dd" width="400">|1|$25|
|  10|Fotorresistencia|Es un componente electrónico cuya resistencia disminuye con el aumento de intensidad de luz incidente.|<img src="https://github.com/ItzelFuentes/SmartCar_Control/assets/106613946/3edfb97d-ca44-4a34-9ee3-f907f25e72a7" width="400">|1|$45|
| 11 | Arduino Uno | El Arduino Uno es una placa de microcontrolador de código abierto basado en el microchip ATmega328P y desarrollado por Arduino.cc. ​​ La placa está equipada con conjuntos de pines de E/S digitales y analógicas que pueden conectarse a varias placas de expansión y otros circuitos. | ![image](https://github.com/user-attachments/assets/f3196b17-c114-4fdc-bcbc-7f9fb9a044a8) | 1 | $250 |
| 12 | Placa Rele 16 canales | Es un módulo electrónico que permite controlar dispositivos de alta potencia o alta corriente utilizando señales de baja potencia, como las que provienen de un microcontrolador o un Arduino. | ![image](https://github.com/user-attachments/assets/412b089b-22bb-4a45-b306-f0202d66ef48) | 1 | $513 |
| 13 | Pláca felónica | Es una herramienta esencial en la electrónica para la construcción y prototipado de circuitos electrónicos. Esta placa permite a los usuarios soldar componentes electrónicos de manera permanente para probar circuitos antes de diseñar un PCB personalizado. | ![image](https://github.com/user-attachments/assets/f34dbb6c-e5a8-4ca6-ba4b-9730b213dcf8) | 2 | $96 |
| 14 | Cilindro maestro | Su función principal es convertir la fuerza aplicada al pedal de freno en presión hidráulica, la cual se transmite a través del líquido de frenos hacia los cilindros de rueda, activando las zapatas o pastillas de freno para detener el vehículo. | ![image](https://github.com/user-attachments/assets/ef76526e-aed0-4f81-b030-8f7796bf047d) | 1 | $799 |
| 15 | Micro switch con botón | Es un tipo de interruptor eléctrico que se caracteriza por su tamaño compacto y su mecanismo de accionamiento sensible y rápido. Estos interruptores son ampliamente utilizados en aplicaciones donde se requiere una activación precisa y confiable en respuesta a un pequeño movimiento físico. | ![image](https://github.com/user-attachments/assets/d8fbd9d0-3ae4-4088-a853-a2c1f0304ef0) | 1 | $29|


## Historias de usuario epicas
| Id | Historia de usuario | Prioridad | Estimación | Como probarlo | Responsable |
|----|---------------------|-----------|------------|---------------|-------------|
|  1 | Control de acceso y seguridad del vehículo. |Alta|3-4 semanas|Abrir y cerrar los seguros desde la aplicación móvil, verificar el cierre automático al alejarse y desconectar Bluetooth, y probar el encendido automático de luces en baja luminosidad.|Jorge|
|  2 |Monitoreo de datos del vehículo. |Alta|3-4 semanas|Visualizar en la aplicación móvil los datos del vehículo como nivel de gasolina, nivel de luz y temperatura, y verificar que se puedan monitorear estos datos de manera remota.|Itzel|
|  3 |Configuración remota del vehículo.|Alta|3-4 semanas|Configurar el tiempo de espera para el cierre automático de los seguros desde la aplicación móvil y verificar que la configuración se aplique correctamente de forma remota.|Sandra|


## Prototipo del proyecto
![Doc1_page-0001 (1)](https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/6293bcb3-25da-4827-9edd-7b08098e2b30)


## Circuito diseñado para el proyecto completo 
![image](https://github.com/user-attachments/assets/593cc8da-3d8f-4fc3-b15f-94d43c1049b1)


## Resultados

### Primer avance
En la primer entrega se logró concretar un circuito integrado por un arduino mega, una pantalla touch de 2.4'', un buzzer, un módulo bluetooth y un módulo reloj. Se logró enlazar la pantalla a con una app móvil a través de bluetooth. 
![image](https://github.com/ItzelFuentes/SmartCar_Control/assets/106613946/fb2796a6-654e-4c32-82cd-eae0b239953c)

| Circuito | Aplicación móvil | 
|----|----------|
|<img src = "https://github.com/ItzelFuentes/SmartCar_Control/assets/106613946/591c2e9b-1f85-4306-8d99-eb7074ee0edb" width="500"> <img src = "https://github.com/ItzelFuentes/SmartCar_Control/assets/106613946/ebb0a19d-2160-47e3-a07e-3874fd91453c" width="500"> | <img src = "https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/de7cfabb-ed8e-427b-8c33-7caf32ed3ab4" width="500"> <img src = "https://github.com/ItzelFuentes/SmartCar_Control/assets/108686186/45584c5a-5581-43be-bda1-567bdcdd6adc" width="500">|

### Evidencia del proceso
<p align="center">
  <img src="https://github.com/user-attachments/assets/aa334c66-69cd-4e6f-88b9-b4e278f93fce" width="300" alt="image1">
  <img src="https://github.com/user-attachments/assets/42f0e77d-0849-406b-b284-4d1a9f82d761" width="300" alt="image2">
  <img src="https://github.com/user-attachments/assets/33978e2e-d2a5-46dc-87e1-7ab4c986c833" width="300" alt="image3">
  <img src="https://github.com/user-attachments/assets/3e8d4335-f7b1-4adf-bae3-daf5d0d3088a" width="300" alt="image4">
</p>


### Resultado final
<p align="center">
  <img src="https://github.com/user-attachments/assets/16e4a527-40f5-4356-be32-7c6f0ecce2b8" width="300" alt="image1">
  <img src="https://github.com/user-attachments/assets/529988ed-549e-4498-91d6-3e70d72ab521" width="300" alt="image2">
  <img src="https://github.com/user-attachments/assets/d78592b2-2cd3-4832-9e34-9973739ec9b9" width="300" alt="image3">
</p>


# Vídeo demostrativo del proyecto
https://youtu.be/_dml0_VoMwM

# Vídeo del beneficiario
https://youtu.be/iRV3VPJSR0M

# Carta de agradecimiento
![image](https://github.com/user-attachments/assets/809c0ead-7023-4a06-91cc-d69897338d31)












