package com.techspine.cms.repository.user;

import com.techspine.cms.entity.Student;

public interface UserService {

    public Student findUserById(long userId) ;
    public Student findUserProfileByJwt(String jwt) ;
}
