package request;

import com.google.gson.JsonObject;

/**
 * LoadRequest
 * <pre>
 *     <b>
 *         Domain:
 *     </b>
 *     Users: array of user objects
 *     Persons: array of persons objects
 *     Events: array of event objects
 * </pre>
 */
public class LoadRequest {
    public LoadRequest(JsonObject json)
    {
        this.json = json;
    }

    public JsonObject json;
}
