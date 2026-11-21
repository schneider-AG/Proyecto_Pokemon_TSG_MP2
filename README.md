# Proyecto_Pokemon_TSG_MP2

**Integrantes del equipo**

Samuel Mendoza Padilla (2569299 - 3743) - GitHub: LegendUser-Code

Juan José Bonilla Moya (2569254 - 3743) - GitHub: juanjosebonilla

Schneider Álvarez Gil (2569328 - 3743) - GitHub: schneider-AG

**Sobre el proyecto**

Este es nuestro segundo miniproyecto  continuando la simulación de Pokémon TCG Pocket  Para esta entrega, el juego deja completamente la consola y pasa a ser manejado a través de una interfaz gráfica de escritorio construida con Java . Además, agregamos la mecánica completa de evoluciones y estructuramos el código separando la vista del modelo 

**Qué hay nuevo?**

Interfaz Gráfica Cero consola: Todo el juego, desde registrar los nombres hasta atacar, se maneja mediante ventanas, botones y eventos Tablero en vivo Muestra los PS actuales , las energías adjuntas, las cartas restantes y los puntos de premio,  Un área de texto que va narrando lo que pasa en la partida (ataques, derrotas, cartas jugadas) y hay Pantallas intermedias para evitar que un jugador le vea la mano al otro durante el cambio de turno. 

**Mecánica de Evolución**

Agregamos 7 cartas nuevas correspondientes a las evoluciones (Fase 1 y Fase 2) de los Pokémon básicos.   Implementamos las reglas de evolución: no se puede evolucionar en el primer turno, ni el mismo turno que un Pokémon entra al juego.   Al evolucionar, el Pokémon mantiene el daño acumulado y las energías que ya tenía adjuntas. 
 
**Sistemas base**

La lógica principal del juego sigue funcionando igual que el primer proyecto Mazos estrictos de 20 cartas (máximo 2 copias por carta, mínimo 6 Pokémon básicos).  Combate con sistema de debilidades y coste de energías. Cartas de entrenador y objetos (Misty, Investigación del Profesor, Poción, Poké Ball, Turbo Velocidad)

**Estructura del Código** 

Separamos estrictamente la lógica de la interfaz visual:   

src/modelo/: Contiene toda la lógica del juego (cartas, jugador, tablero).  

src/vista/: Contiene las ventanas y componentes visuales.

src/media/ y src\Cartas_POCKETCG contiene todas las imagenes de los pokemons.

**Instrucciones de ejecucion del juego**

1. Al iniciar, se abre la ventana de bienvenida y te toca presionar cualquier tecla para avanzar.

   <img width="1902" height="978" alt="image" src="https://github.com/user-attachments/assets/83b470d9-0564-4ac1-a2d0-f0e81a03297d" />

2. Escribe el nombre de cada jugador y selecciona su mazo (Fuego\Agua,Electrico\Psiquico o campeones).

   <img width="992" height="626" alt="image" src="https://github.com/user-attachments/assets/2218c68b-7ec7-4f08-a827-0df2b92d9df8" />


3. Cada jugador elige su Pokémon activo y puede colocar hasta dos Pokémon en la banca se hace selecionando el pokemon y dandole click en donde lo quieres poner.

   <img width="1917" height="1001" alt="image" src="https://github.com/user-attachments/assets/3ae5265e-5272-4c84-b0bb-62d392834da1" />

4. Durante tu turno puedes realizar diferentes jugadas tocando la imagen de los pokemon: unir energías, evolucionar, usar cartas de objetivo, o interactuar con tu Pokémon activo para atacar o retirarlo.

   <img width="1900" height="937" alt="image" src="https://github.com/user-attachments/assets/95084501-8c53-4e54-872c-62645c9fda28" />

5. Haz clic en Pasar turno cuando termines tus acciones. Aparecerá una pantalla de transición para dar paso al siguiente jugador. El ciclo continuará hasta que uno de los jugadores consiga los 3 puntos necesarios para ganar la partida.
