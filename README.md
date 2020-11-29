# SafeOut
Safe Out

# Introducción - Problemática y propuesta
En el contexto de pandemia que estamos atravesando, es imperativo que en cualquier lugar público se establezcan límites en cuanto a la cantidad de personas que pueden estar presentes al mismo tiempo.

Ahora que se empieza a flexibilizar la cuarentena, empiezan a visualizarse lugares que antes concentraban un gran número de gente, por lo que van a requerir aplicar medidas para respetar y hacer cumplir el distanciamiento social para poder operar.

A tal fin, consideramos de vital importancia que tanto los dueños de los negocios como sus clientes tengan conocimiento de la cantidad de personas que hay en el lugar en un momento dado, y del nivel porcentual de ocupación respecto de la capacidad máxima que el lugar puede albergar. Solo con esta información es que un cliente podrá decidir si es seguro visitar un lugar, y el dueño de un negocio podrá saber el momento en que tiene que limitar el acceso a su comercio tras haber alcanzado el cupo.

Adicionalmente, creemos que sería sumamente útil para el potencial cliente contar con información no solo de la ocupación general del lugar, sino también de los distintos sectores dentro de él, tales como podrían ser las mesas de afuera, la terraza o la barra, en el caso de un bar o un restorán. Esto le otorgaría una mayor visibilidad a la persona para poder determinar si el sector que quiere visitar está actualmente disponible, si está vacío, lleno o incluso si está momentáneamente cerrado.

Actualmente no existe nada en el mercado que provea este tipo de información en vivo sobre el nivel de ocupación y capacidad actual de los lugares públicos. Es por eso que proponemos una solución software que brinde esta información a las personas, a través de una aplicación móvil nativa para dispositivos Android.

# Roles de usuario
La aplicación contará con dos roles de usuario: el administrador del lugar y el usuario final. 
El administrador del lugar podrá definir y controlar toda clase de parámetros del lugar tales como qué tipo de lugar es (oficina, negocio, restorán, cine, teatro, etc.), qué sectores tiene y cuáles son sus capacidades máximas permitidas. Si la capacidad máxima de un sector es cero, significa que el mismo se encuentra temporalmente cerrado al público.

# Nivel de ocupación y procedimiento de CheckIN y CheckOUT
Como funcionalidad principal de la aplicación se le ofrece a los usuarios la posibilidad de visualizar el nivel de ocupación en vivo de los distintos sectores de lugares públicos de la ciudad.

Esta información será accesible en las entradas de los lugares a partir de dos métodos:
- Escaneo de una imagen QR.
- Escaneo de un dispositivo con NFC. 
Cada vez que una persona se acerque al lugar con la intención de entrar, se le pedirá que registre su ingreso con uno de estos dos métodos. Para ello la persona deberá tener instalada la aplicación en su propio dispositivo y deberá estar registrada. 

El procedimiento de check-in consistirá en lo siguiente:

- Buscar el lugar al que se quiere ingresar
- Presionar el botón de "Check-In"
- Indicar el sector al cual se quiere acceder y registrar el ingreso (los que están llenos o cerrados no se podrán seleccionar)
    a) NFC: Acercar el dispositivo al del lugar para registrar el ingreso por NFC
    b) QR: Escanear con la cámara de su dispositivo la imagen QR del lugar.

Los egresos o "Check-Out" también deberán ser registrados para poder mantener la información actualizada. 
El procedimiento será similar al explicado para los ingresos.

# Reservas
Como funcionalidad secundaria y complementaria a la principal de obtener el nivel de ocupación de un lugar, se brinda también la posibilidad de hacer reservas a través de la app, con el fin de asegurarse el acceso al lugar que se desea visitar.

Un lugar puede o no aceptar reservas, según fuera previamente configurado por su administrador.

El usuario que desea reservar un lugar deberá indicar el día, horario y sector al que planea ir. Al hacerlo se le mostrará el nivel de ocupación que tendrá ese sector en ese momento. Si todavía hay cupo el usuario podrá confirmar y efectuar la reserva.

# Funcionalidades - Casos de uso
## Del administrador del lugar
- Registrarse e iniciar sesión
- Configurar parámetros del lugar
- Consultar el nivel de ocupación actual de los distintos sectores
- Consultar reservas del lugar
- Registrar ingresos y egresos

## Del usuario final
- Registrarse e iniciar sesión
- Ver mapa con lugares públicos marcados en él
- Buscar lugares públicos por el nombre
- Consultar los sectores de un lugar y su nivel de ocupación actual
- Hacer check-in y check-out de un lugar
- Hacer reservas para un sector de un lugar, y cancelarlas si lo desea

# Alcance y Restricciones
- Las funcionalidades del administrador se consideran por fuera del alcance de esta propuesta. 
- Los datos de los lugares configurables por el administrador serán generados en forma aleatoria para cada lugar y permanecerán invariables durante toda su existencia.
- Se limita el uso de la aplicación al territorio de la Ciudad Autónoma de Buenos Aires. Solo se podrá consultar información de lugares públicos radicados dentro de los límites de este territorio.

# Flujo de Pantallas

## Login
<img src="readme-assets/1-login.png" width="30%">


## Menú de Navegación
<img src="readme-assets/2-menu.png" width="30%">

## Home 
<img src="readme-assets/3-home-a.png" width="30%">
<img src="readme-assets/3-home-b.png" width="30%">

## Mapa de lugares 
<img src="readme-assets/4-map.png" width="30%">
<img src="readme-assets/4-map-a.png" width="30%">

## Detalle de lugar
<img src="readme-assets/5-place-detail.png" width="30%">

## Buscar lugares

## Check IN
### QR
<img src="readme-assets/7-checkin-qr.png" width="20%">
<img src="readme-assets/7-checkin-qr-detail.png" width="20%">
<img src="readme-assets/7-checkin-qr-success.png" width="20%">
<img src="readme-assets/7-checkin-qr-error.png" width="20%">
<img src="readme-assets/7-checkin-qr-error-2.png" width="20%">

### NFC
<img src="readme-assets/7-checkin-nfc.png" width="20%">
<img src="readme-assets/7-checkin-nfc-detail.png" width="20%">
<img src="readme-assets/7-checkin-nfc-success.png" width="20%">
<img src="readme-assets/7-checkin-nfc-error-2.png" width="20%">
<img src="readme-assets/7-checkin-nfc-error.png" width="20%">

## Check OUT
### QR
<img src="readme-assets/7-checkout-qr.png" width="20%">
<img src="readme-assets/7-checkout-qr-success.png" width="20%">
<img src="readme-assets/7-checkout-qr-error.png" width="20%">

### NFC
<img src="readme-assets/7-checkout-nfc.png" width="20%">
<img src="readme-assets/7-checkout-nfc-success.png" width="20%">
<img src="readme-assets/7-checkout-nfc-error.png" width="20%">

## Ver info
### QR
<img src="readme-assets/8-info.png" width="20%">
<img src="readme-assets/8-info-detail.png" width="20%">

## Reservas
<img src="readme-assets/9-reservation-1.png" width="20%">
<img src="readme-assets/9-reservation-2.png" width="20%">
<img src="readme-assets/9-reservation-3.png" width="20%">
<img src="readme-assets/9-reservation-4.png" width="20%">
<img src="readme-assets/9-reservation-5.png" width="20%">
<img src="readme-assets/9-reservation-6.png" width="20%">
<img src="readme-assets/9-reservation-7.png" width="20%">
<img src="readme-assets/9-reservation-8.png" width="20%">
<img src="readme-assets/9-reservation-9.png" width="20%">

# Próxima Release
