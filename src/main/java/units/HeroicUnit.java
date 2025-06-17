package units;

/** Something that can cast a battle-ability every turn/tick. */
public interface HeroicUnit {
    /** Perform the unit’s special ability (no movement here). */
    void castAbility();
}