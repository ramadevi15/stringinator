package com.comcast.stringinator.service;

import com.comcast.stringinator.model.StatsResponse;
import com.comcast.stringinator.model.StringinatorRequest;
import com.comcast.stringinator.model.StringinatorResponse;

public interface StringinatorService {

    StringinatorResponse stringinate(StringinatorRequest stringinatorRequest);

    StatsResponse stats();
}
