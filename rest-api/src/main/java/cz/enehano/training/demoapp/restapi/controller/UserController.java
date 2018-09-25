package cz.enehano.training.demoapp.restapi.controller;

import cz.enehano.training.demoapp.restapi.dto.UserDto;
import cz.enehano.training.demoapp.restapi.service.UserService;
import cz.enehano.training.demoapp.restapi.util.UserConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserConvertor userConvertor;
    private final UserService userService;

    @Autowired
    public UserController(UserService userService, UserConvertor userConvertor) {
        this.userService = userService;
        this.userConvertor = userConvertor;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userConvertor.convertToDto(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userConvertor.convertToDto(userService.getUserById(id));
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto dto, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userConvertor.convertToDto(userService.createUser(userConvertor.convertToEntity(dto), userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto dto, @PathVariable Long id) {
        return userConvertor.convertToDto(userService.updateUser(id, userConvertor.convertToEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
