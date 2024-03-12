package org.bitcoin.tfw.ltbc.client;

public class DaemonControllerClient extends AbstractLTBCClient<IDaemonController> {
    public DaemonControllerClient(String baseUrl) {
        super(baseUrl, IDaemonController.class);
    }
}
