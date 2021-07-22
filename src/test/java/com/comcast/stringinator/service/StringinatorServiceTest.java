package com.comcast.stringinator.service;

import com.comcast.stringinator.BaseTest;
import com.comcast.stringinator.exception.ApplicationException;
import com.comcast.stringinator.model.StatsResponse;
import com.comcast.stringinator.model.StringinatorRequest;
import com.comcast.stringinator.model.StringinatorResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class StringinatorServiceTest {

    @Spy
    StringinatorServiceImpl stringinatorService;

    @Mock
    List<String> longestInput = new ArrayList<>();

    Map<String, Integer> popularInput = new HashMap<>();

    private static StringinatorRequest stringinatorRequest;

    private static StringinatorResponse stringinatorResponse;

    private static StatsResponse statsResponse;


    @BeforeAll
    public static void setUpBeforeClass(){

        stringinatorRequest = new StringinatorRequest("Comcast is best place to work!");
        stringinatorResponse = StringinatorResponse.builder()
                .charValue(List.of('o','s','t'))
                .numberOfOccurrences(Long.valueOf(3))
                .build();
        statsResponse = StatsResponse.builder()
                .longestInputReceived("Comcast is best place to work!")
                .mostPopularStrings(List.of("Comcast is best place to work!"))
                .build();

    }

    @BeforeEach
    public void init_mocks() {
        MockitoAnnotations.openMocks(this);
        longestInput  =List.of("Comcast is best place to work!");
        popularInput = Map.of("Comcast is best place to work!",30);
        //stringinatorService = mock(StringinatorServiceImpl.class);
        //ReflectionTestUtils.setField(stringinatorService, "longestInput", List.of("Comcast is best place to work!"));
        //ReflectionTestUtils.setField(stringinatorService,"popularInput", Map.of("Comcast is best place to work!",30));
    }

    @Test
    public void test_stats_success(){
        stringinatorService.longestInput  =List.of("Comcast is best place to work!");
        stringinatorService.popularInput = Map.of("Comcast is best place to work!",30);
        var response = stringinatorService.stats();
        Assertions.assertEquals(statsResponse,response);
    }

    @Test
    public void test_stats_longest_popular_input_empty(){
        stringinatorService.longestInput  =new ArrayList<>();
        stringinatorService.popularInput = new HashMap<>();
        var response = stringinatorService.stats();
        Assertions.assertEquals(new StatsResponse(Collections.emptyList(), ""),response);
    }

    @Test
    public void test_stringinate_success(){
        var response = stringinatorService.stringinate(stringinatorRequest);
        Assertions.assertEquals(stringinatorResponse,response);
    }

    @Test
    public void test_stringinate_throws_exception_if_noAlphaNumeric(){
        stringinatorRequest.setInput("!@#$#$#");
        Exception applicationException = assertThrows(ApplicationException.class,() -> stringinatorService.stringinate(stringinatorRequest));
        assertTrue(applicationException.getMessage().equals("Bad Request"));
    }
}
