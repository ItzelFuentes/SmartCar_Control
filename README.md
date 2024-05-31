## SmartCar_Control 2024
## Integrantes 
- Álvarez González Sandra Karina
- Ayala Manrique Jorge Luis
- Fuentes Cabrera Itzel Alessandra


## Objetivo general
Este proyecto tiene como objetivo la creación de un dispensador de alimento y de agua para mascota, 

### Objetivos específicos
- El usuario prodrá visualizar mediante una pantalla Si hay agua o No hay para que este informado.
- Mediante una aplicación (Telegram) el usuario podrá mandar mensaje para que pueda llenar nuevamente el plato de comida.
- Mediante un sensor la mascota podrá alimentarse sola con solo acercarse al dispensador.
- Poder programar los dispensadores a ciertas horas para que se llenen automaticamente.
  ## Tabla de Software utilizado
| Id | Software | Version | Tipo |
|----|----------|---------|------|
| 1  |Arduino   |2.0.1    |   IDE|
| 2  |Node-red  |3.0      |Servicio|
|3  |Telegram   |8.3      |App   |
|4| Mosquitto|5.0|Servicio|
|5|Gestor de bases de datos|x|SGBD|
|6|Grafana|x|Visualización y el formato de datos métricos|

## Tabla con el hardware utilizado
| Id | Componente | Descripción | Imagen | Cantidad | Costo total |
|----|------------|-------------|--------|----------|-------------|
|   1|ESP32| El módulo ESP32 es una solución de Wi-Fi/Bluetooth todo en uno, integrada y certificada que proporciona no solo la radio inalámbrica, sino también un procesador integrado con interfaces para conectarse con varios periféricos|![image](https://user-images.githubusercontent.com/104101668/214482867-90fbaa8c-7d6c-42f5-8a1c-004b815030a6.png)|1|$0|
|   2|ServoMotor| Un servomotor es un actuador rotativo o motor que permite un control preciso en términos de posición angular, aceleración y velocidad, capacidades que un motor normal no tiene. En definitiva, utiliza un motor normal y lo combina con un sensor para la retroalimentación de posición.|![image](https://user-images.githubusercontent.com/104101668/214482747-fb832e0e-00df-4c10-8796-c2203a49bea7.png)|1|$50|
|   3|Sensor ultrasónico HC-SR04|Miden la distancia mediante el uso de ondas ultrasónicas. El cabezal emite una onda ultrasónica y recibe la onda reflejada que retorna desde el objeto. Los sensores ultrasónicos miden la distancia al objeto contando el tiempo entre la emisión y la recepción.|![image](https://user-images.githubusercontent.com/104101668/214482421-9ca2aa8e-2227-464a-9980-5fbd23966cac.png)|3|$80|
|   4|Pantalla LCD JIMDO.96C|Una pantalla de cristal líquido o LCD es una pantalla delgada y plana formada por un número de píxeles en color o monocromos colocados delante de una fuente de luz o reflectora. A menudo se utiliza en dispositivos electrónicos de pilas, ya que utiliza cantidades muy pequeñas de energía eléctrica.|![image](https://user-images.githubusercontent.com/106613946/233204465-f5b86683-52ae-438f-b27a-dc355e0c364c.png)|1|$70| 
|   5|Plato|Plato de Plástico|![image](https://user-images.githubusercontent.com/106613946/233208532-32b1dc1c-5961-4ef5-be40-6e8c5de66cf2.png)|2|$5| 
|   6|Sensor nivel del agua|Son instrumentos que activan una alarma en el momento en que el líquido del interior del recipiente alcance el nivel marcado. Se instalan con la finalidad de conseguir la automatización del llenado de recipientes y conocer siempre en tiempo real la capacidad del interior.|![image](https://user-images.githubusercontent.com/104101668/214483265-1638c2d1-11d8-4ffc-962f-96c83813bf62.png)|1|$50|
|   7|Botellas de plastico|Botella de 2 litros|![image](https://user-images.githubusercontent.com/106613946/233208948-30cf8eac-ce1e-40c8-8046-4af8bc13d6bb.png)|2|$0| 
|   8|Madera| 5 pedazos de madera de diferentes medidas|![image](https://user-images.githubusercontent.com/104101668/214483514-01145c2b-edcc-4f15-8fea-c4e9ecbe881a.png)|4|$0| 
|   9|Buzzer| Es un dispositivo que tiene la capacidad de generar una señal de audio al ser alimentado|![image](https://user-images.githubusercontent.com/106613946/233209389-17df52bd-0712-4ba0-9873-b426f1123213.png)|1|$15| 
|   10|Bomba de Diafragma| Es un dispositivo que tiene la capacidad de generar una señal de audio al ser alimentado|![image](https://m.media-amazon.com/images/I/31arBXXRT6L._AC_SY450_.jpg)|1|$0| 
|   11|Protoboard|es prácticamente una PCB temporal con una forma y tamaño generalizados. Utilizada comúnmente para pruebas y prototipos temporales de circuitos|![image](https://user-images.githubusercontent.com/106613946/233217981-e8de4fd7-44f6-463f-b818-45785af1aeb5.png)|1|$0| 
|   12|Adaptador de fuente de alimentación de 12V 5A| Es un dispositivo electrónico comúnmente llamado fuente de alimentación, fuente de poder o fuente conmutada|![image](https://user-images.githubusercontent.com/106613946/233218241-90c26d7e-14da-4751-9295-47106387fa82.png)|1|$0| 
|   13| Cables de Puente de protoboard de Placa de Prueba| Es un elemento que permite cerrar el circuito eléctrico del que forma parte dos conexiones|![image](https://user-images.githubusercontent.com/106613946/233218856-3a7458d8-7c8f-49eb-8914-3ab456faff30.png)|30|$60| 
|   14| Módulo Relevadores Relay 4 Canales 5v | Puede controlar varios dispositivos y otros equipos con gran corriente: ideal para proyectos mecatrónicos. Este relevador soporta hasta 250 VAC o 30 VDC a 10 A.|![image](https://m.media-amazon.com/images/I/61dFbJu-y7L._SX522_.jpg)|1|$0|

## Tabla de historias de usuario
| Id | Historia de usuario | Prioridad | Estimación | Como probarlo | Responsable |
|----|---------------------|-----------|------------|---------------|-------------|
|  1 | Como usuario quiero poder darle de comer a mi mascota cada cierto tiempo.|Alta|2-3 semanas|Se deberá esperar a que se llegue a la hora programada para ver que efectivamente funciona.|Itzel|
|  2 |Como usuario quiero poder ver mediante la aplicación (Telegram o la pantalla quiero poder revisar si hay agua o no hay alimento.|Alta|2-3 semanas|Esperar a que se vacié el dispensador o verifique en la pantalla si es que hay o no hay alimento y se tenga un control de alimentacion.|Abraham|
|  3|Como usuario quiero manejar a tráves de una aplicación controlar el dispensador de alimento y agua|Media|2-3semanas|Observando que al momento de llenar presionar o mandar un mensaje se llene el dispensador de la comida para así tener un control con la comida|Rodrigo|
|  4|Como usuario quiero que mi mascota se pueda alimentar en el momento en el que se acerca al dispensador.|Media|2-3semanas|Observando a la mascota acercarse al sensor y ver como este reacciona|Alma|



## Prototipo en dibujo
![image](https://user-images.githubusercontent.com/104101668/214484615-74ee57c3-b80c-44f1-90d1-d8cd39caa426.png)


## Prototipo en 3D
![image](https://user-images.githubusercontent.com/106613946/233441515-a1216b56-5058-416e-8efc-4c1414ceccf3.png)

![image](https://user-images.githubusercontent.com/106613946/233441541-836b1265-5eff-46b5-80d1-5afa551a1ef0.png)

![image](https://user-images.githubusercontent.com/106613946/233441573-1d6639da-cb21-4d03-b97a-3edda64c7ac7.png)

## Diagrama de Fritzing
![IOT_bb](https://user-images.githubusercontent.com/114689978/234678613-b94c2f78-7185-4521-9122-1ad90e7e5a17.png)

## Resultado Final
![image](https://user-images.githubusercontent.com/106613946/234085571-203d61be-b689-404e-9ce4-9e43d9991f64.png)

![image](https://user-images.githubusercontent.com/106613946/234086210-49be8adb-836e-493b-86f9-208ecf6d7c00.png)

![image](https://user-images.githubusercontent.com/106613946/234086242-fdd2f29b-1e02-4a4a-9b63-d4f64a855d70.png)

![image](https://user-images.githubusercontent.com/106613946/234086268-a95e232e-dd7d-4b33-af16-211bc556cc36.png)

![image](https://user-images.githubusercontent.com/114689978/234426035-18bf5707-eb4c-433c-8ced-04d9d29ff086.png)

![image](https://user-images.githubusercontent.com/114689978/234426058-b602498f-8284-4551-ac81-55a109a83549.png)

![image](https://user-images.githubusercontent.com/114689978/234426074-5b8cf316-053d-4701-8fb4-c9985e416782.png)

![image](https://user-images.githubusercontent.com/114689978/234426096-f140dc18-5c6e-48d5-a730-41ffd7f98547.png)

![image](https://user-images.githubusercontent.com/114689978/234426111-c43e6cae-3a1a-409e-a4cb-14db7a316b5e.png)

## Vídeo Dispensador de Comida
https://drive.google.com/file/d/1poVHWOu1CuLo3tjJZ8d7hvzJSzZdN8J4/view?usp=drivesdk












