package com.comcast.stringinator.service;

/**
 * Gets most frequent, popular and longest chars/string.
 *
 * @author Rama Devi
 */

import com.comcast.stringinator.exception.ApplicationException;
import com.comcast.stringinator.exception.advice.ApplicationExceptionHandler;
import com.comcast.stringinator.model.StatsResponse;
import com.comcast.stringinator.model.StringinatorRequest;
import com.comcast.stringinator.model.StringinatorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.comcast.stringinator.enumeration.ErrorCodeEnum.INVALID_DATA;

@Service
public class StringinatorServiceImpl implements StringinatorService {

    private static final Logger LOGGER = LogManager.getLogger(ApplicationExceptionHandler.class);

    public List<String> longestInput = new ArrayList<>();
    public Map<String, Integer> popularInput = new HashMap<>();

    /** Get the most frequent in the given input.
     * @param stringinatorRequest The given input.
     * @return stringinatorResponse the most frequent chars in the given input along with number of occurrences
     */
    @Override
    public StringinatorResponse stringinate(StringinatorRequest stringinatorRequest) {
        LOGGER.info("Finding most frequent character in the given input");
        String input = stringinatorRequest.getInput();

        findLongestInput(input);
        findPopularInput(input);

        Optional<Map.Entry<Long, List<Map.Entry<Character, Long>>>> occurrences = input
                .chars()
                .boxed()
                .filter(in -> Pattern.matches("[a-zA-Z0-9]", Character.toString((char) in.intValue())))
                .collect(Collectors.groupingBy(i -> (char) i.intValue(), Collectors.counting()))
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(i -> i.getValue()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByKey());

        if (occurrences.isEmpty()) {
            LOGGER.error("No alpha numeric characters in the given input");
            throw new ApplicationException(INVALID_DATA);
        }

        List<Character> frequentChars = occurrences
                .get()
                .getValue()
                .stream()
                .map(i -> i.getKey()).collect(Collectors.toList());
        Long numberOfOccurrences = occurrences.get().getKey();

        LOGGER.info("Most frequent chars {} , number of occurrences {}", frequentChars, numberOfOccurrences);

        return StringinatorResponse.builder()
                .charValue(frequentChars)
                .numberOfOccurrences(numberOfOccurrences).build();
    }


    /** Get the most popular and longest string.
     * @return StatsResponse the most popular and longest string seen by the server
     */
    @Override
    public StatsResponse stats() {
        LOGGER.info("Finding stats of the given inputs");

        String longestInput = this.longestInput.isEmpty() ? "" : this.longestInput.get(0);
        List<String> popularString = new ArrayList<>();
        Optional<Map.Entry<Integer, List<Map.Entry<String, Integer>>>> popularStringMap = popularInput
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(i -> i.getValue()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByKey());


        if (popularStringMap.isPresent()) {
            LOGGER.info("Finding popular String if present");
            popularString = popularStringMap
                    .get()
                    .getValue()
                    .stream()
                    .map(i -> i.getKey()).collect(Collectors.toList());
        }

        return StatsResponse.builder()
                .longestInputReceived(longestInput)
                .mostPopularStrings(popularString)
                .build();
    }

    private void findPopularInput(String input) {
        LOGGER.info("Finding most popular input");
        popularInput.putIfAbsent(input, 0);
        popularInput.put(input, popularInput.get(input) + 1);
    }

    private void findLongestInput(String input) {
        LOGGER.info("Finding longest input");
        longestInput.removeIf(i -> input.length() > i.length());
        addLongestInputIfAbsent(input);
    }

    private void addLongestInputIfAbsent(String input) {
        if (longestInput.isEmpty()) {
            LOGGER.info("Added longest input if its empty");
            longestInput.add(input);
        }
    }
}
