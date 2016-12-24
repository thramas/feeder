package network;

/**
 * Created by samarth on 12/23/16.
 */

public interface NetworkListenerCallback {
    void onNetworkRequestSuccess(int requestCode, String response);

    void onNetworkRequestFailure(int requestCode, NetworkUtils.NetworkException e);
}
