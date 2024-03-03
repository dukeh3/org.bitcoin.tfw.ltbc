
package org.bitcoin.tfw.lbc.sba.rest.controllers;

import org.bitcoin.tfw.lbc.LocalTestBlockChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/btc")
public class BlockChainController {
    LocalTestBlockChain ltbc = new LocalTestBlockChain();
//    Logger logger = LoggerFactory.getLogger(BlockChainController.class);

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Entity does not exist")
    public static class EntityNotFoundException extends RuntimeException {
    }

    @GetMapping("/daemonStatus")
    public boolean getDaemonStatus() {
        return ltbc.isDaemonAlive();
    }

    @PutMapping("/start")
    public void start() {
//        logger.info("Hello World!");/

        ltbc.startDaemon();
        ltbc.setDefaultAddress();
        ltbc.mine(101);
    }

    @PutMapping("/stop")
    public void stop() {
        ltbc.stopDaemon();
    }

    @PutMapping("/mine")
    public void mine(@RequestParam(defaultValue = "1", required = false) int blocks) {
        ltbc.mine(blocks);
    }

    @PutMapping("/mineTo")
    public void mineTo(@RequestParam(defaultValue = "1", required = false) int blocks, String address) {
        ltbc.mineTo(blocks, address);
    }

    @PutMapping("/sendTo")
    public void send(@RequestParam(defaultValue = "1.0", required = false) double amount, @RequestParam() String address) {
        ltbc.sendTo(address, amount);
    }
}
