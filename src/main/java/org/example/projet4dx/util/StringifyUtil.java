package org.example.projet4dx.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.example.projet4dx.model.gameEngine.Soldier;

import java.util.List;

public class StringifyUtil {
    /**
     * Converts a list of Soldier objects into a JSON array.
     *
     * @param soldiers The list of Soldier objects to be converted.
     * @return A JsonArray representing the list of Soldier objects in JSON format.
     */
    public static JsonArray jsonifySoldierList(List<Soldier> soldiers) {
        JsonArray soldierArray = new JsonArray();

        for (Soldier soldier : soldiers) {
            JsonObject soldierObject = new JsonObject();
            JsonObject coordinatesObject = new JsonObject();

            coordinatesObject.addProperty("x",soldier.getCoordinates().getX());
            coordinatesObject.addProperty("y",soldier.getCoordinates().getY());

            soldierObject.add("coordinates",coordinatesObject);
            soldierObject.addProperty("login",soldier.getPlayerDTO().getLogin());
            soldierObject.addProperty("id", soldier.getId());

            soldierArray.add(soldierObject);
        }
        return soldierArray;
    }
}
