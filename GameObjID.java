
/** 
 * Tento enum sluzi na identifikaciu objektov v hre.
 * 
 * Player - hrac, zobrazeny ako kuzelnik
 * Heal - objekt, ktory hrac zbiera na doplnenie zivota
 * Orb - objekt, ktory hrac vystreli na ublizenie nepriatelom
 * Sting - objekt, ktory nepriatel (Wasp) vystreli na ublizenie hracovi
 * Enemy - nepriatel, ktory sa snazi zabit hraca
 * Spawner - objekt, ktory generuje nepriatelov
 */
public enum GameObjID {
    
    Player(),
    Heal(),
    Orb(),
    Sting(),
    Enemy(),
    Spawner();

}
