# Maze Runner Game

## Overview
The Reconquest is a 2D top-down Maze Runner game in Java
using libGDX, where players navigate a character through mazes
filled with walls, traps, enemies, and a key to reach the exit.
Implemented object-oriented design principles, dynamic enemy
movement, HUD displaying lives and key status, interactive game
menus and sound effects and background music. The game has
customizable mazes loaded from properties files and adapts to
various screen sizes with a dynamic camera system.
## Features
- Multiple game screens (Menu, Game, Win, Game Over)
- Preloaded or custom map selection
- Player movement with WASD/Arrow keys
- Various game elements:
  - Static spike traps
  - Dynamic ghost enemies
  - Collectable keys
  - Locked/unlocked exits
  - Life system with hearts
- Background music and sound effects
- Pause menu functionality
- Character animations
- Sprite-based graphics

## Controls
- **Movement**: WASD or Arrow keys
- **Sprint**: Hold SPACE while moving
- **Pause**: ESC
- **Pause Menu**:
  - SPACE: Resume game
  - M: Return to menu
  - S: Toggle sound
  - X: Exit game

## Game Mechanics
1. **Player Movement**:
   - Basic movement in four directions
   - Sprint feature for faster movement
   - Collision detection with walls and objects

2. **Lives System**:
   - Player starts with 3 lives
   - Lives are lost when touching traps or ghosts
   - Game over when all lives are lost

3. **Key and Exit**:
   - Must collect the key before being able to use the exit
   - Exit remains locked until key is collected
   - Visual feedback for key collection status

4. **Enemies and Traps**:
   - Static spike traps that damage on contact
   - Dynamic ghost enemies that move around the maze
   - 2-second invulnerability period after taking damage

## Technical Implementation
### Core Classes
- `MazeRunnerGame`: Main game class managing screens and resources
- `GameScreen`: Handles gameplay logic and rendering
- `MenuScreen`: Manages menu interface and map selection
- `LoadMap`: Handles map loading and rendering
- `Player`: Manages player state and animations
- `Hud`: Displays game UI elements (lives, key status)

### Graphics
- Custom sprite sheets for characters and objects
- Animated traps and enemies using GIF decoder
- Texture regions for efficient rendering
- Orthographic camera with viewport management

### Audio
- Background music for different game states
- Sound effects for:
  - Key collection
  - Trap activation
  - Ghost encounters
  - Win/lose conditions

## Map Format
Maps are stored in .properties files with the following format:
```
x,y=type
```
Where:
- `x,y`: Grid coordinates
- `type`: Object type (0:wall, 1:start, 2:exit, 3:static_trap, 4:dynamic_trap, 5:key)

## Dependencies
- LibGDX framework
- GDX file chooser for desktop implementation
- Custom GIF decoder for animated sprites

## Building and Running
1. Ensure Java JDK is installed
2. Clone the repository
3. Import as Gradle project
4. Run the desktop launcher class
- Level editor
- Save/load game progress
