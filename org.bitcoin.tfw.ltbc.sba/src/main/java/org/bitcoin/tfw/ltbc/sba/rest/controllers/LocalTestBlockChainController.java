
package org.bitcoin.tfw.ltbc.sba.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.bitcoin.tfw.ltbc.sba.rest.controllers.LocalTestBlockChainDaemonController.ltbc;

@RestController
@RequestMapping("/ltbc/controller")
public class LocalTestBlockChainController {
    Logger logger = LoggerFactory.getLogger(LocalTestBlockChainController.class);

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Entity does not exist")
    public static class EntityNotFoundException extends RuntimeException {
    }

    @PutMapping("/mine")
    public void mine(@RequestParam(defaultValue = "1") Integer blocks) {
        logger.info("Mine " + blocks);
        ltbc.mine(blocks);
        logger.info("Mined");
    }

    @PutMapping("/mineTo")
    public void mineTo(@RequestParam(defaultValue = "1") Integer blocks, @RequestParam() String address) {
        ltbc.mineTo(blocks, address);
    }

    @PutMapping("/sendTo")
    public void send(@RequestParam(defaultValue = "1.0") Double amount, @RequestParam() String address) {
        logger.info("Sending: " + amount + "to " + address);
        ltbc.sendTo(address, amount);
        logger.info("Sent");
    }
}
