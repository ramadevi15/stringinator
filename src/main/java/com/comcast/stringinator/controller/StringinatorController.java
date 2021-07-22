package com.comcast.stringinator.controller;

/**
 *Gets most frequent, popular and longest chars/string.
 *
 * @author Rama Devi
 */

import com.comcast.stringinator.exception.advice.ApplicationExceptionHandler;
import com.comcast.stringinator.model.StatsResponse;
import com.comcast.stringinator.model.StringinatorRequest;
import com.comcast.stringinator.model.StringinatorResponse;
import com.comcast.stringinator.service.StringinatorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "StringinatorController", tags = {"StringinatorController"})
@Slf4j
public class StringinatorController {

    private static final Logger LOGGER = LogManager.getLogger(ApplicationExceptionHandler.class);

    @Autowired
    private StringinatorService stringinatorService;

    /**
     * Displays info about the other endpoints
     * @return String
     */
    @ApiOperation(value = "Displays info ", notes ="displays info about other endpoints", tags = {"StringinatorController"})
    @GetMapping("/")
    public String index() {
        return "<pre>\n" +
                "Welcome to the Stringinator - for all of your string manipulation needs.\n" +
                "GET / - You're here! To know more about Stringinator \n" +
                "POST /stringinate - Get the most frequent character in a string. Takes JSON of the following form: {\"input\":\"your-string-goes-here\"}\n" +
                "GET /stringinate - Get the most frequent character in a string. Takes Query Param of the following form: ?input=<your-input>\n" +
                "GET /stats - Get statistics about all strings the server has seen, including the longest and the most popular strings.\n" +
                "</pre>";
    }


    /**
     * Get the most frequent character in a string
     * @param inputString as query param
     * @return
     */
    @ApiOperation(value = "Get the most frequent character", notes = "Get the number of occurrences of char in a given string using GET", tags = {"StringinatorController"})
    @GetMapping(path = "/stringinate", produces = "application/json")
    public StringinatorResponse stringinate(@RequestParam(name = "input", required = true) String inputString) {
        log.info("Find most popular character");
        StringinatorResponse stringinatorResponse = stringinatorService.stringinate(new StringinatorRequest(inputString));
        return stringinatorResponse;
    }


    /**
     * Get the most frequent character in a string
     * @param inputJson
     * @return
     */
    @ApiOperation(value = "Get the most frequent character", notes = "Get the number of occurrences of char in a given string using POST", tags = {"StringinatorController"})
    @PostMapping(path = "/stringinate", consumes = "application/json", produces = "application/json")
    public StringinatorResponse stringinate(@RequestBody @Valid StringinatorRequest inputJson) {
        StringinatorResponse stringinatorResponse = stringinatorService.stringinate(inputJson);
        return stringinatorResponse;
    }

    /**
     * Get statistics about all strings the server has seen including the longest and most popular strings
     * @return statsResponse
     */
    @ApiOperation(value = "Get the most popular and longest input", notes = "Get the popular and longest for all the input the server has seen ", tags = {"StringinatorController"})
    @GetMapping(path = "/stats")
    public StatsResponse stats() {
        StatsResponse statsResponse = stringinatorService.stats();
        return statsResponse;
    }


}
