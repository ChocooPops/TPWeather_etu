
package ConnexionHTTP;

import org.json.JSONException;
import org.json.JSONObject;

public interface Callback{
    public void onWorkDone(JSONObject JO)throws JSONException;
}


