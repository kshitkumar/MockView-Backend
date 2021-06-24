package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.UserSkillRequest;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.Skill;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.repository.SkillRepository;
import com.hashedin.mockview.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SkillService {
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    public void addUserSkills(User user, UserSkillRequest userSkillRequest) throws ResourceNotFoundException {
        log.debug("Entering method addUserSkills");

        List<Skill> inputSkillList = userSkillRequest.getSkillList();
        inputSkillList.stream().forEach(item -> item.setUser(user));
        skillRepository.saveAll(inputSkillList);
        log.debug("Saved user skill details in database");


    }


    public List<Skill> findSkillDetails(User user) {
        log.debug("Entering findSkillDetails method");
        return skillRepository.findByUser(user);

    }
}
