package net.ralphpina.wwd.api;

import org.json.JSONArray;

/**
 * Interface to be mocked out during tests
 */
public interface MixpanelClient {

    JSONArray fetchEngageResults() throws Exception;
}
