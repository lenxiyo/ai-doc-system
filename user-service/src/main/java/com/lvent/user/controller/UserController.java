package com.lvent.user.controller;


import com.lvent.common.Result;
import com.lvent.user.model.dto.UserDTO;
import com.lvent.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String get(){
        return "Hello World!";
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) throws Exception{
        if (userDTO.getUsername() == null){
            log.error("username为空");
            throw new Exception("username为空");
        }
        if (userDTO.getPassword() == null){
            log.error("password为空");
            throw new Exception("密码为空");
        }
        Integer row = userService.register(userDTO);
        if (row != 0){
            return Result.success();
        }else{
            return Result.error(500,"未知错误",null);
        }
    }

    @PostMapping("/login")
    // @RequestParam用于？后面拼接的参数（URL）或者是表单格式的数据，对于json格式的数据，最好使用DTO接收
    public Result login(@RequestParam("username") String username,@RequestParam("password") String password) throws Exception {
        if ("".equals(username)){
            log.error("username为空");
            throw new Exception("用户名不能为空");
        }
        if ("".equals(password)){
            log.error("password为空");
            throw new Exception("密码不能为空");
        }
        String token = userService.login(username,password);
        if (token != null){
            return Result.success(token);
        }else {
            return Result.error(404,"未找到该用户",null);
        }
    }

    @GetMapping("/info")
    public Result getInfo(){
        return Result.success("info");
    }
}
