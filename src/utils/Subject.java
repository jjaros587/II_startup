/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Tým 5
 */
public interface Subject {
    
    /**
     * Registrace observeru.
     * 
     * @param observer Observer
     */
    void registerObserver(Observer observer);
    
    /**
     * Zrušení observeru.
     * 
     * @param observer Observer
     */
    void removeObserver(Observer observer);
    
    /**
     * Oznámení observeru.
     */
    void notifyObservers();
}
