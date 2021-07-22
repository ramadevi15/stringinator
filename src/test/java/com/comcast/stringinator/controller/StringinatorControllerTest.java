package com.comcast.stringinator.controller;

import com.comcast.stringinator.BaseTest;
import com.comcast.stringinator.exception.ApplicationException;
import com.comcast.stringinator.model.StatsResponse;
import com.comcast.stringinator.model.StringinatorRequest;
import com.comcast.stringinator.model.StringinatorResponse;
import com.comcast.stringinator.service.StringinatorService;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class StringinatorControllerTest extends BaseTest {

    @InjectMocks
    StringinatorController stringinatorController;

    @Mock
    StringinatorService stringinatorService;

    private static StringinatorRequest stringinatorRequest;

    private static StringinatorResponse stringinatorResponse;

    private static StatsResponse statsResponse;

    @BeforeAll
    public static void setUpBeforeClass(){

        stringinatorRequest = new StringinatorRequest("Comcast is best place to work!");
        stringinatorResponse = StringinatorResponse.builder()
                .charValue(List.of('o'))
                .numberOfOccurrences(Long.valueOf(3))
                .build();
        statsResponse = StatsResponse.builder()
                .longestInputReceived("Comcast is best place to work!")
                .mostPopularStrings(List.of("Comcast is best place to work!"))
                .build();

    }

    @Test
    public void test_index_success(){
        var response = stringinatorController.index();
        Assertions.assertNotNull(response);
    }

    @Test
    public void test_stringinate_post_success(){
        when(stringinatorService.stringinate(stringinatorRequest)).thenReturn(stringinatorResponse);
        var response = stringinatorController.stringinate(stringinatorRequest);
        Assertions.assertEquals(stringinatorResponse,response);
    }

    @Test
    public void test_stringinate_post_throws_if_empty_input(){
        stringinatorRequest.setInput(null);
        Exception applicationException = assertThrows(MethodArgumentNotValidException.class,() ->
                stringinatorController.stringinate(stringinatorRequest));
        assertTrue(applicationException.getMessage().equals("Bad Request"));
    }

    @Test
    public void test_stringinate_get_success(){
        when(stringinatorService.stringinate(stringinatorRequest)).thenReturn(stringinatorResponse);
        var response = stringinatorController.stringinate("Comcast is best place to work!");
        Assertions.assertEquals(stringinatorResponse,response);
    }

    @Test
    public void test_stringinate_get_throws_if_empty_input(){
        stringinatorRequest.setInput(null);
        Exception applicationException = assertThrows(Exception.class,() ->
                stringinatorController.stringinate(""));
        assertTrue(applicationException.getMessage().equals("Bad Request"));
    }

    @Test
    public void test_stats_success(){
        when(stringinatorService.stats()).thenReturn(statsResponse);
        var response = stringinatorController.stats();
        Assertions.assertEquals(statsResponse,response);
    }
}
