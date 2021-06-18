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
import java.util.stream.Collectors;

@Service
@Slf4j
public class SkillService {
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    public void addUserSkills(Integer id, UserSkillRequest userSkillRequest) throws ResourceNotFoundException {
        log.debug("Entering method addUserSkills");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));;
            List<Skill> inputSkillList = userSkillRequest.getSkillList();
            List<Skill> skillList = inputSkillList.stream()
                    .map(x -> x.builder()
                            .name(x.getName())
                            .user(user)
                            .proficiency(x.getProficiency())
                            .type(x.getType())
                            .build())
                    .collect(Collectors.toList());
            skillRepository.saveAll(skillList);
            log.debug("Saved user skill details in database");



    }
}
