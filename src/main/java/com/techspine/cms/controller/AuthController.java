package com.techspine.cms.controller;

import com.techspine.cms.entity.Student;
import com.techspine.cms.repository.user.CustomUserServiceImpl;
import com.techspine.cms.repository.user.StudentRepository;
import com.techspine.cms.request.LoginRequest;
import com.techspine.cms.response.AuthResponse;
import com.techspine.cms.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/auth")
public class AuthController {


    private final StudentRepository StudentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserServiceImpl customUserService;
    @Autowired
    AuthController(StudentRepository StudentRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, CustomUserServiceImpl customUserService) {
        this.StudentRepository = StudentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.customUserService = customUserService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createStudentHandler(@RequestBody Student Student) throws Exception {
        String email = Student.getEmail();
        String password = Student.getPassword();
        String firstName = Student.getFirstName();
        String lastName = Student.getLastName();

        Student isEmailExist = StudentRepository.findByEmail(email);
        if (isEmailExist != null) {
            throw new Exception( "This email is already used with another account.");
        }
        Student createdStudent = new Student();
        createdStudent.setEmail(email);
        createdStudent.setPassword(passwordEncoder.encode(password));
        createdStudent.setFirstName(firstName);
        createdStudent.setLastName(lastName);

        Student savedStudent = StudentRepository.save(createdStudent);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedStudent.getEmail(), savedStudent.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Signup Success...");
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginStudentHandler(@RequestBody LoginRequest loginRequest) {
        String student = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(student, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Signin Success...");
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

        private Authentication authenticate(String username, String password) {

            UserDetails userDetails = customUserService.loadUserByUsername(username);
            if (userDetails == null) {
                throw new BadCredentialsException("Invalid username...");
            }
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password...");
            }
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
}
