package org.bitcoin.tfw.ltbc.client;

public class ControllerClient extends AbstractLTBCClient<IController> {
    public ControllerClient(String baseUrl) {
        super(baseUrl, IController.class);
    }
}
