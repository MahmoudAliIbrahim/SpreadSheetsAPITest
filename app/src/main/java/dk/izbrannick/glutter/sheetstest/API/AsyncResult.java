package dk.izbrannick.glutter.sheetstest.API;

/**
 * Created by luther on 03/11/2016.
 */

import org.json.JSONObject;

/**
 * Created by kstanoev on 1/14/2015.
 */
public interface AsyncResult
{
    void onResult(JSONObject object);
}