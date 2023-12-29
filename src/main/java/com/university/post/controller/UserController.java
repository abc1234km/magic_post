package com.university.post.controller;

import com.university.post.dto.UserDTO;
import com.university.post.enums.MessageResponse;
import com.university.post.exception.user.UserException;
import com.university.post.model.User;
import com.university.post.payload.request.user.PasswordRequest;
import com.university.post.payload.response.data.ErrorResponse;
import com.university.post.payload.response.user.LoginResponse;
import com.university.post.payload.request.user.LoginRequest;
import com.university.post.payload.response.data.DataRepsonse;
import com.university.post.security.CustomUserDetails;
import com.university.post.security.JwtTokenProvider;
import com.university.post.service.RoleService;
import com.university.post.service.UserService;
import com.university.post.utils.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Operation(description = "Đăng nhập")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Đăng nhập thành công",
                    content = { @Content(schema = @Schema(implementation = LoginResponse.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode  = "400", description = "Đăng nhập không thành công", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode  = "401", description = "Xác thực không thành công", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode  = "404", description = "Username không tìm thấy", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping("/login")
    public DataRepsonse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserId(),
                        loginRequest.getPassword()
                )
        );

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return new DataRepsonse<>(HttpStatus.OK, new LoginResponse(jwt), MessageResponse.SUCCESS_LOGIN.getMessage());
    }

    @Operation(description = "Đăng xuất")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Đăng xuất thành công"),
            @ApiResponse(responseCode  = "401", description = "Xác thực không thành công", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "Xác thực Bearer")
    public DataRepsonse<String> logout() {
        // Thay đổi jwt khi logout
        String jwt = tokenProvider.generateToken((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return new DataRepsonse<>(HttpStatus.OK, "", "Đăng xuất thành công");
    }

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name="Xác thực Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Lấy thông tin user thành công"),
            @ApiResponse(responseCode  = "401", description = "Xác thực không thành công", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public DataRepsonse<UserDTO> getProfier() {
        return new DataRepsonse<>(HttpStatus.OK, new UserDTO(Utils.getUserAuthentication()), "Lấy thông tin user thành công");
    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Thay đổi mật khẩu thành công"),
            @ApiResponse(responseCode  = "401", description = "Xác thực không thành công", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode  = "400", description = "Thay đổi mật khẩu không thành công", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @SecurityRequirement(name = "Xác thực Bearer")
    public DataRepsonse<String> changePassword(@RequestBody PasswordRequest passwordRequest, BindingResult bindingResult) {
        String passwordUser = Utils.getUserAuthentication().getPassword();
        String oldPasswordRequest = passwordEncoder.encode(passwordRequest.getOldPassword());

        if (!passwordUser.equals(oldPasswordRequest) || bindingResult.hasErrors()) {
            throw new UserException("Thay đổi mật khẩu không thành công");
        }
        userService.changePasswordUser(passwordRequest.getNewPassword());
        return new DataRepsonse<>(HttpStatus.OK, "", "Thay đổi mật khẩu thành công");
    }
}
