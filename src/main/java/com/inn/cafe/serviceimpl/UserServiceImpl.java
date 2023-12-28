package com.inn.cafe.serviceimpl;

import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.model.User;
import com.inn.cafe.repository.UserRepository;
import com.inn.cafe.service.UserService;
import com.inn.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;
  @Override
  public ResponseEntity<String> signUp(Map<String, String> requestMap) {
    log.info("inside signup" , requestMap);
    try {
      if(validateSignup(requestMap)){
        User user = userRepository.findByEmailId(requestMap.get("email"));
        if (Objects.isNull(user)) {
          userRepository.save(getUserFromMap(requestMap));
          return CafeUtils.getResponseEntity("Signup Success" , HttpStatus.OK);
        }else {
          return CafeUtils.getResponseEntity("Email Exists" , HttpStatus.BAD_REQUEST);
        }
      } else {
        return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA , HttpStatus.BAD_REQUEST);
      }
    } catch (Exception ex) {
      return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG , HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  private boolean validateSignup(Map<String , String > requestMap) {
    if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password")) {
      return true;
    }
    return false;
  }

  private User getUserFromMap(Map<String , String> requsetMap) {
    User user = new User();
    user.setName(requsetMap.get("name"));
    user.setEmail(requsetMap.get("email"));
    user.setContactNumber(requsetMap.get("contactNumber"));
    user.setPassword(requsetMap.get("password"));
    user.setRole("user");
    user.setStatus("false");
    return user;
  }
}
