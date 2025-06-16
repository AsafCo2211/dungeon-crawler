# Dungeons and Dragons - OOP CLI Game

## 📌 Project Status
🚧 Initial setup – project structure created, no code implemented yet.

---

## 📝 Project Description
A single-player, multi-level **text-based RPG game**, inspired by *Dungeons & Dragons*.  
The project will be implemented in **Java**, using **Object-Oriented Programming (OOP)** principles,  
as part of **HW3 in the Object-Oriented Software Design (OOSD)** course at Ben-Gurion University.

---

## 🎯 Planned Features
- Text-based interface (CLI)
- Multiple player types: Warrior, Mage, Rogue (and optionally Hunter)
- Enemies: Monsters, Traps, Bosses
- Combat system with attack & defense mechanics
- Special abilities and leveling system
- Multi-level game with file-based map input
- Observer & Visitor design patterns

---

## 🧱 Current Structure
```
DungeonsAndDragons-OOP/
├── .gitignore
├── README.md
└── src/
    ├── cli/
    │   ├── CLI.java
    │   └── InputHandler.java
    ├── game/
    │   ├── Board.java
    │   ├── Game.java
    │   └── GameManager.java
    ├── messages/
    │   └── Message.java
    ├── tests/
    │   └── (unit tests)
    ├── tiles/
    │   ├── Empty.java
    │   ├── Position.java
    │   ├── Tile.java
    │   └── Wall.java
    └── units/
        ├── Boss.java
        ├── Enemy.java
        ├── HeroicUnit.java
        ├── Hunter.java
        ├── Mage.java
        ├── Monster.java
        ├── Player.java
        ├── Rogue.java
        ├── Trap.java
        ├── Unit.java
        └── Warrior.java
