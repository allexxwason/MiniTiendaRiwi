# 🛒 MiniTiendaRiwi

## 📖 Descripción del Proyecto
**MiniTiendaRiwi** es una aplicación de consola en **Java** diseñada para gestionar un inventario básico de productos.  
El sistema utiliza ventanas emergentes con `JOptionPane`, lo que facilita la interacción del usuario de forma visual y sencilla.

El proyecto integra varias estructuras de datos de Java para manejar la información del inventario:

- **ArrayList<String>** → Almacena los nombres de los productos (lista dinámica).  
- **double[]** → Array primitivo que guarda los precios, sincronizado por índice con el `ArrayList` de nombres.  
- **HashMap<String, Integer>** → Asocia el nombre de cada producto con su cantidad en stock, permitiendo búsqueda y actualización rápidas.  

---

## ⚙️ Funcionalidades
La aplicación ofrece un menú principal con las siguientes opciones:

1. **Agregar producto** → Añade un nuevo producto (nombre, precio y cantidad). Incluye validaciones para evitar duplicados y errores de formato.  
2. **Listar inventario** → Muestra todos los productos registrados con sus nombres, precios y stock disponible.  
3. **Comprar producto** → Permite comprar productos, validando existencia y stock. El inventario se actualiza automáticamente.  
4. **Mostrar estadísticas** → Calcula y muestra el producto más barato y el más caro.  
5. **Buscar producto** → Busca productos por nombre (incluso coincidencias parciales).  
6. **Salir** → Finaliza la aplicación mostrando un ticket con el total de compras realizadas en la sesión.  

---

## 🚀 Cómo Ejecutar el Proyecto

### 🔹 Requisitos previos
- Tener instalado **Java Development Kit (JDK)** versión 8 o superior.  
- Asegúrate de que la variable de entorno `JAVA_HOME` esté configurada.

### 🔹 Pasos

1. **Clonar el repositorio**
   ```bash
   git clone <URL_de_tu_repositorio>
   cd MiniTiendaRiwi
