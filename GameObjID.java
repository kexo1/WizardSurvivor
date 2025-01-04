
/** 
 * Enum GameObjID sluzi na identifikaciu objektov v hre.
 */
public enum GameObjID {
    /**
     * objekt, ktory hrac zbiera na doplnenie zivota
     */
    Heal(),

    /**
     * objekt, ktory hrac vystreli na ublizenie nepriatelom
     */
    Orb(),

    /**
     * hrac, zobrazeny ako carodejnik (wizard)
     */
    Player(),

    /**
     * objekt, ktory nepriatel (Wasp) vystreli na ublizenie hracovi
     */
    Sting(),

    /**
     * nepriatel, ktory sa snazi zabit hraca
     */
    Enemy(),

    /**
     * objekt, ktory generuje nepriatelov
     */
    Spawner()
}
