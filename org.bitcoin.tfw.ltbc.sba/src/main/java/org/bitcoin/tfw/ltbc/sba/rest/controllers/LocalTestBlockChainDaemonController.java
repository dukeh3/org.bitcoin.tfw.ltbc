
package org.bitcoin.tfw.ltbc.sba.rest.controllers;

import org.bitcoin.tfw.ltbc.LocalTestBlockChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/lbc/daemon")
public class LocalTestBlockChainDaemonController {
    Logger logger = LoggerFactory.getLogger(LocalTestBlockChainDaemonController.class);
    static LocalTestBlockChain ltbc = new LocalTestBlockChain();
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Entity does not exist")
    public static class EntityNotFoundException extends RuntimeException {
    }

    @GetMapping("/status")
    public boolean getStatus() {
        return ltbc.isDaemonAlive();
    }

    @PutMapping("/start")
    public void start() {
        logger.info("Hello World!");

        ltbc.startDaemon();
        ltbc.setDefaultAddress();
        ltbc.mine(101);
    }

    @PutMapping("/stop")
    public void stop() {
        ltbc.stopDaemon();
    }
}
