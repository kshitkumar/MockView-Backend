package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.UserAwardRequest;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.Award;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.repository.AwardRepository;
import com.hashedin.mockview.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AwardService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AwardRepository awardRepository;

    public void addAwardDetails(User user, UserAwardRequest userAwardRequest) throws ResourceNotFoundException {
        log.debug("Entering addAwardDetails");

        List<Award> inputAwardList = userAwardRequest.getUserAwardList();
        inputAwardList.stream().forEach(item -> item.setUser(user));
        awardRepository.saveAll(inputAwardList);
        log.debug("Award details successfully inserted in database ");


    }

    public List<Award> findAwardDetails(User user) {
        log.debug("Entering findAwardDetails method");
        return awardRepository.findByUser(user);
    }
}
