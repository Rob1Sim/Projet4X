package org.example.projet4dx.model.gameEngine;

public interface ICombat {
    /**
     * Reduces the entity's hit points by a specified amount of damage.
     *
     * @param damage The amount of damage to be subtracted.
     * @param soldier The Soldier that inflict the damage.
     */
    void takeDamage(int damage, Soldier soldier);
    int getHP();
}
