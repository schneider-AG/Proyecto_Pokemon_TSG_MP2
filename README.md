# Proyecto_Pokemon_TSG_MP2

**Integrantes del equipo**

Samuel Mendoza Padilla (2569299 - 3743) - GitHub: LegendUser-CodeJuan
José Bonilla Moya (2569254 - 3743) - GitHub: juanjosebonilla
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
src/media/: contiene todas las imagenes de los pokemons.



