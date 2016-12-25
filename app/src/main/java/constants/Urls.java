package constants;

/**
 * Created by samarth on 12/25/16.
 */

public class Urls {
    public static final String GIPHY_HOST = "http://api.giphy.com/v1/gifs/";
    public static final String GIPHY_TRENDING_API = GIPHY_HOST + "trending?api_key=" + ApiKeys.GIPHY_API + "&limit=" + AppConstants.GIPHY_RESPONSE_LIMIT;
}
